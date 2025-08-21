package it.unict.gallosiciliani.liph.validator;

import it.unict.gallosiciliani.liph.LinguisticPhenomena;
import it.unict.gallosiciliani.liph.LiphDerivation;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;
import it.unict.gallosiciliani.liph.regex.RegexLinguisticPhenomenaReader;
import lombok.Getter;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Validate a model against linguistic phenomena
 */
public class Validator implements Consumer<LinguisticPhenomenon> {

    private final Model model;
    @Getter
    private final List<LiphDerivation> invalidDerivations = new LinkedList<>();
    @Getter
    private int processedDerivations = 0;

    /**
     * Create a validator for derivations in the given model
     * @param model where derivations will be checked
     */
    public Validator(final Model model){
        this.model=model;
    }

    @Override
    public void accept(final LinguisticPhenomenon phenomenon){
        final ParameterizedSparqlString queryStr = new ParameterizedSparqlString("SELECT ?src ?srcr ?target ?targetr WHERE{\n" +
                "\t?occurrence <"+ LinguisticPhenomena.OCCURRENCE_OF_OBJ_PROPERTY+"> ?phenomenon;\n"+
                "\t\t<"+LinguisticPhenomena.SOURCE_OBJ_PROPERTY+"> ?src;\n"+
                "\t\t<"+LinguisticPhenomena.TARGET_OBJ_PROPERTY+"> ?target .\n"+
                "\t?src <"+ LinguisticPhenomena.WRITTEN_REP_DATA_PROPERTY+"> ?srcr .\n" +
                "\t?target <"+LinguisticPhenomena.WRITTEN_REP_DATA_PROPERTY+"> ?targetr .\n}");
        queryStr.setIri("phenomenon", phenomenon.getId());
        try (QueryExecution ex = QueryExecution.create(queryStr.asQuery(), model)) {
            final ResultSet rs = ex.execSelect();
            while(rs.hasNext()){
                processedDerivations++;
                final QuerySolution s = rs.next();
                final String src = s.getResource("src").getURI();
                final String srcr = s.getLiteral("srcr").getString();
                final String target = s.getResource("target").getURI();
                final String targetr = s.getLiteral("targetr").getString();
                if (!phenomenon.apply(srcr).contains(targetr)){
                    invalidDerivations.add(new LiphDerivation() {
                        @Override
                        public String getSourceIndividual() {
                            return src;
                        }

                        @Override
                        public String getSourceWrittenRep() {
                            return srcr;
                        }

                        @Override
                        public LinguisticPhenomenon getPhenomenon() {
                            return phenomenon;
                        }

                        @Override
                        public String getTargetIndividual() {
                            return target;
                        }

                        @Override
                        public String getTargetWrittenRep() {
                            return targetr;
                        }
                    });
                }
            }
        }
    }

    /**
     * Validate the derivations in the model against the (actionable) phenomena
     * described in phenomenaDefs.
     *
     * @param phenomenaDefs a model containing definitions of linguistic phenomena
     */
    public void validate(final Model phenomenaDefs){
        RegexLinguisticPhenomenaReader.read(phenomenaDefs).getFeatures().forEach(this);
    }
}

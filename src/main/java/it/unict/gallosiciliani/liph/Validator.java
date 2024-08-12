package it.unict.gallosiciliani.liph;

import it.unict.gallosiciliani.liph.regex.RegexLinguisticPhenomenaReader;
import lombok.Getter;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFLanguages;
import org.apache.jena.riot.RDFParser;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Validate a model against linguistic phenomena
 */
public class Validator implements Consumer<LinguisticPhenomenon> {

    private final Model model;
    @Getter
    private final List<LiphDerivation> invalidDerivations = new LinkedList<LiphDerivation>();
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
                "\t?src ?phenomenon ?target .\n" +
                "\t?src <"+LinguisticPhenomena.ONTOLEX_WRITTEN_REP_DATA_PROPERTY+"> ?srcr .\n" +
                "\t?target <"+LinguisticPhenomena.ONTOLEX_WRITTEN_REP_DATA_PROPERTY+"> ?targetr .\n}");
        queryStr.setIri("phenomenon", phenomenon.getIRI());
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

    public static void main(final String[] args) throws URISyntaxException {
        if (args.length<2){
            System.out.println("Usage Validator <derivationsOntologyIRI> <definitionsOntologyIRI>");
            return;
        }
        final Model data = RDFParser.source(args[0]).lang(RDFLanguages.TTL).toModel();
        final Model defs = RDFParser.source(args[1]).lang(RDFLanguages.TTL).toModel();

        final Validator v = new Validator(data);
        v.validate(defs);

        System.out.println("Processed "+v.getProcessedDerivations()+" derivations.");
        final List<LiphDerivation> illegalDerivations = v.getInvalidDerivations();
        if (illegalDerivations.isEmpty()){
            System.out.println("All derivations are compliant with phenomena definitions");
            return;
        }
        System.out.println("Found "+illegalDerivations.size()+" illegal derivations");
        for(final LiphDerivation d : illegalDerivations){
            System.out.println("<"+d.getSourceIndividual()+"> writtenRep \""+d.getSourceWrittenRep()+"\";");
            System.out.println("\t<"+d.getPhenomenon().getIRI()+">  <"+d.getTargetIndividual()+"> writtenRep \""+d.getTargetWrittenRep()+"\" .");
        }
    }
}

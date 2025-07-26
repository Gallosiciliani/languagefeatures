package it.unict.gallosiciliani.gs;

import it.unict.gallosiciliani.liph.LinguisticPhenomena;
import it.unict.gallosiciliani.liph.LinguisticPhenomenonByLabelRetriever;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;
import it.unict.gallosiciliani.liph.regex.FiniteStatePhenomenaQuery;
import it.unict.gallosiciliani.liph.regex.RegexLinguisticPhenomenaReader;
import it.unict.gallosiciliani.liph.util.HashedOntologyItem;
import it.unict.gallosiciliani.liph.util.LinguisticPhenomenonByLabelRetrieverImpl;
import it.unict.gallosiciliani.liph.util.OntologyLoader;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionDatasetBuilder;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.vocabulary.RDFS;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * An ontology for all the language features defined in the
 * Gallosiciliani project.
 */

@Getter
@Slf4j
public class GSFeatures extends OntologyLoader implements LinguisticPhenomenonByLabelRetriever{
    public static final String IRI = "https://gallosiciliani.unict.it/ns/gs-features";
    public static final String NS = IRI+"#";
    public static final String VERSION = "2.0.1";

    public static final String GALLOSICILIAN_FEATURE_CLASS=NS+"GalloSicilianFeature";
    public static final String LENIZ_CLASS=NS+"Leniz";
    public static final String DEGEM_CLASS=NS+"Degem";
    public static final String ASSIB_CLASS=NS+"Assib";
    public static final String DISSIM_CLASS=NS+"Dissim";
    public static final String DITT_CLASS=NS+"Ditt";
    public static final String VOCAL_CLASS=NS+"Vocal";
    public static final String AFER_CLASS=NS+"Afer";
    public static final String ELIM_CLASS =NS+"Elim";
    public static final String PALAT_CLASS=NS+"Palat";
    public static final String INF_CLASS =NS+"Inf";
    public static final String DERETR_CLASS=NS+"Deretr";


    private final List<LinguisticPhenomenon> regexLinguisticPhenomena;
    private final LinguisticPhenomenonByLabelRetriever phenomenonByLabelRetriever;
    private final List<GSFeaturesCategory> categories;

    /**
     * Private constructor, use factory methods.
     */
    public GSFeatures() throws IOException {
        super("gs-features.ttl", IRI);
        final RegexLinguisticPhenomenaReader reader=new RegexLinguisticPhenomenaReader();
        reader.read(getModel(), new FiniteStatePhenomenaQuery());
        regexLinguisticPhenomena=reader.getFeatures();
        phenomenonByLabelRetriever=LinguisticPhenomenonByLabelRetrieverImpl.build(regexLinguisticPhenomena);
        categories=retrieveCategories(getModel());
    }


    @Override
    public LinguisticPhenomenon getByLabel(final String label, final Locale locale) {
        return phenomenonByLabelRetriever.getByLabel(label, locale);
    }

    private List<GSFeaturesCategory> retrieveCategories(final Model model) {
        final String queryString="SELECT distinct ?category ?label ?comment WHERE {\n"+
                "?feature a <https://gallosiciliani.unict.it/ns/gs-features#GalloSicilianFeature>;\n"+
                "\t a ?category .\n"+
                "?category <"+ RDFS.subClassOf.getURI()+"> <"+LinguisticPhenomena.LINGUISTIC_PHENOMENON_CLASS+">;\n"+
                "\t <"+RDFS.label.getURI()+"> ?label;\n"+
                "\t <"+RDFS.comment.getURI()+"> ?comment;\n"+
                "} ORDER BY ?category";
        final List<GSFeaturesCategory> res=new LinkedList<>();
        try(final QueryExecution ex= QueryExecutionDatasetBuilder.create().model(model).query(queryString).build()){
            final ResultSet rs=ex.execSelect();
            while(rs.hasNext()) {
                final QuerySolution s=rs.next();
                final String iri=s.getResource("category").getURI();
                if (!GALLOSICILIAN_FEATURE_CLASS.equals(iri) && !isLiPhClass(iri))
                    res.add(buildCategory(s, model));
            }
        }
        return res;
    }

    /**
     * Check if the given IRI corresponds to a class in the {@link LinguisticPhenomena} ontolopgy
     * @param iri iri
     * @return true if it is a LiPh class, false otherwise
     */
    private boolean isLiPhClass(final String iri){
        for(int i=0; i<LinguisticPhenomena.CLASSES.length; i++)
            if (LinguisticPhenomena.CLASSES[i].equals(iri))
                return true;
        return false;
    }
    private GSFeaturesCategory buildCategory(final QuerySolution row, Model model){
        final String categoryIri=row.getResource("category").getURI();
        final GSFeaturesCategory c=new GSFeaturesCategory(categoryIri, NS);
        c.setLabel(row.getLiteral("label").getString());
        c.setComment(row.getLiteral("comment").getString());
        final String childrenQueryStr="SELECT ?x ?label ?comment WHERE {\n"+
                "?x a <"+categoryIri+">;\n"+
                "\t <"+RDFS.label.getURI()+"> ?label;\n"+
                "\t <"+RDFS.comment.getURI()+"> ?comment .}";
        try(final QueryExecution ex=QueryExecutionDatasetBuilder.create().model(model).query(childrenQueryStr).build()){
            final ResultSet rs=ex.execSelect();
            while(rs.hasNext()){
                final QuerySolution qs=rs.next();
                final String itemIri=qs.getResource("x").getURI();
                final String label=qs.getLiteral("label").getString();
                final String comment=qs.getLiteral("comment").getString();
                final HashedOntologyItem child=new HashedOntologyItem(itemIri, NS) {
                    @Override
                    public String getLabel() {
                        return label;
                    }

                    @Override
                    public String getComment() {
                        return comment;
                    }
                };
                c.addMember(child);
            }
        }
        return c;
    }
}

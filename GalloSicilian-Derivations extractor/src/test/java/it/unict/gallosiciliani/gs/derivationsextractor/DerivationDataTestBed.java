package it.unict.gallosiciliani.gs.derivationsextractor;

import cz.cvut.kbss.jopa.model.MultilingualString;
import it.unict.gallosiciliani.liph.LinguisticPhenomenaProvider;
import it.unict.gallosiciliani.liph.model.LexicalObject;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenonOccurrence;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.Form;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry;
import it.unict.gallosiciliani.liph.model.lemonety.EtyLink;
import it.unict.gallosiciliani.liph.model.lemonety.Etymology;
import it.unict.gallosiciliani.liph.model.lexinfo.LexInfo;
import it.unict.gallosiciliani.liph.model.lexinfo.PartOfSpeech;

import java.util.Collections;
import java.util.List;

public class DerivationDataTestBed{
    protected static final String NS = "http://test.org/derivationDataReaderTest#";
    protected static final String ENTRY_LANG = "und";
    public final LinguisticPhenomenon p = createLinguisticPhenomenon("p");
    public final LinguisticPhenomenon q = createLinguisticPhenomenon("q");
    public final LinguisticPhenomenon r = createLinguisticPhenomenon("r");
    public final LinguisticPhenomenon s = createLinguisticPhenomenon("s");
    public final LinguisticPhenomenaProvider lpProvider = new LinguisticPhenomenaProvider(List.of(p, q, r, s));
    public final PartOfSpeech noun=new PartOfSpeech();
    public final PartOfSpeech verb=new PartOfSpeech();

    public final LexicalEntry entryWithDerivation;
    public final List<LinguisticPhenomenonOccurrence> derivation;
    public final LexicalObject intermediateForm = new LexicalObject();
    public final LexicalEntry entryWithEtymonButNoDerivation;
    public final LexicalEntry entryWithNoEtymology;

    public DerivationDataTestBed(){
        noun.setId(LexInfo.NOUN_INDIVIDUAL);
        verb.setId(LexInfo.VERB_INDIVIDUAL);

        intermediateForm.setWrittenRep(new MultilingualString().set(ENTRY_LANG, "intermediate form"));

        entryWithDerivation=createEntry("1");
        addEtymology(entryWithDerivation);
        derivation=addDerivation(entryWithDerivation);

        entryWithEtymonButNoDerivation=createEntry("2");
        addEtymology(entryWithEtymonButNoDerivation);

        entryWithNoEtymology=createEntry("3");
    }

    /**
     * Create a {@link LinguisticPhenomenon} for test purposes
     *
     * @param phenomenonId a unique identifier for linguistic phenomenon
     * @return a linguistic phenomenon
     */
    private static LinguisticPhenomenon createLinguisticPhenomenon(String phenomenonId) {
        final LinguisticPhenomenon p=new LinguisticPhenomenon();
        p.setId("phenomenon"+phenomenonId);
        p.setLabel("phenomenon "+phenomenonId);
        p.setComment("Comment for phenomenon "+phenomenonId);
        return p;
    }

    private static LexicalEntry createEntry(final String entryId) {
        final String iri=NS+"entry"+entryId;
        final LexicalEntry entry=new LexicalEntry();
        entry.setId(iri);
        entry.setCanonicalForm(new Form());
        entry.getCanonicalForm().setId(iri+"form");
        entry.getCanonicalForm().setWrittenRep(new MultilingualString().set(ENTRY_LANG, "entry "+entryId));
        entry.setPartOfSpeech(new PartOfSpeech());
        entry.getPartOfSpeech().setId(LexInfo.NOUN_INDIVIDUAL);
        return entry;
    }

    /**
     * @param entry the entry to which the etymology will be added
     */
    private static void addEtymology(final LexicalEntry entry){
        final Form etymon=new Form();
        etymon.setId(entry.getId()+"etymon");
        etymon.setWrittenRep(new MultilingualString().set(ENTRY_LANG, "etymon"));

        final Etymology etymology=new Etymology();
        entry.setEtymology(Collections.singleton(etymology));
        etymology.setStartingLink(new EtyLink());
        etymology.getStartingLink().setEtySubSource(Collections.singleton(etymon));
    }

    private List<LinguisticPhenomenonOccurrence> addDerivation(final LexicalEntry entry){
        final Form etymon=entry.getEtymology().iterator().next().getStartingLink().getEtySubSource().iterator().next();
        final LinguisticPhenomenonOccurrence o1=new LinguisticPhenomenonOccurrence();
        o1.setId(NS+"o1");
        o1.setSource(etymon);
        o1.setTarget(intermediateForm);
        o1.setOccurrenceOf(p);

        final LinguisticPhenomenonOccurrence o2=new LinguisticPhenomenonOccurrence();
        o2.setId(NS+"o2");
        o2.setSource(intermediateForm);
        o2.setTarget(entry.getCanonicalForm());
        o2.setOccurrenceOf(q);
        return List.of(o2, o1);
    }

}

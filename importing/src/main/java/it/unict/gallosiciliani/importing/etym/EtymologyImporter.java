package it.unict.gallosiciliani.importing.etym;

import cz.cvut.kbss.jopa.model.EntityManager;
import it.unict.gallosiciliani.derivations.DerivationPathNode;
import it.unict.gallosiciliani.importing.iri.EtymologyIRIProvider;
import it.unict.gallosiciliani.importing.iri.IRIProvider;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry;
import it.unict.gallosiciliani.liph.model.lemonety.EtyLink;
import it.unict.gallosiciliani.liph.model.lemonety.Etymology;

import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Import etymologies for {@link it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry} already in the knowledge
 * base.
 */
public class EtymologyImporter implements Consumer<DerivationPathNode> {
    private final EntityManager entityManager;
    private final EtymologyDerivationImporter derivationImporter;

    public EtymologyImporter(final EntityManager entityManager,
                      final IRIProvider iriProvider,
                      final String etymonLanguageTag){
        this.entityManager=entityManager;
        derivationImporter=new EtymologyDerivationImporter(entityManager, iriProvider, etymonLanguageTag);
    }


    @Override
    public void accept(final DerivationPathNode derivationPathNode) {
        if (!derivationImporter.importDerivation(derivationPathNode))
            return;
        final Etymology etymology=new Etymology();
        final EtymologyIRIProvider etymologyIRIProvider=derivationImporter.getEtymologyIRIProvider();
        etymology.setId(etymologyIRIProvider.getEtymolgyIRI());
        final LexicalEntry entry=derivationImporter.getLemmaEntry();
        entry.getEtymology().add(etymology);

        etymology.setStartingLink(new EtyLink());
        etymology.getStartingLink().setId(etymologyIRIProvider.getEtyLinkIRI());
        etymology.getStartingLink().setEtyTarget(entry);
        etymology.getStartingLink().setEtySubTarget(entry.getCanonicalForm());
        etymology.getStartingLink().setEtySubSource(Set.of(derivationImporter.getEtymon()));
        etymology.setLabel(derivationImporter.getEtymon().getWrittenRep().get());

        entityManager.persist(etymology.getStartingLink());
        entityManager.persist(etymology);
    }

    public Collection<String> getMissingLemmas(){
        return derivationImporter.getMissingLemmas();
    }

    public Collection<String> getMultipleEntriesLemmas(){
        return derivationImporter.getMultipleEntriesLemmas();
    }
}

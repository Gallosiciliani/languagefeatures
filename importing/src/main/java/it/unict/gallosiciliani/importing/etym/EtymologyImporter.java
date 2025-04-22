package it.unict.gallosiciliani.importing.etym;

import cz.cvut.kbss.jopa.model.EntityManager;
import it.unict.gallosiciliani.derivations.DerivationPathNode;
import it.unict.gallosiciliani.importing.iri.EtymologyIRIProvider;
import it.unict.gallosiciliani.importing.iri.IRIProvider;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.Form;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry;
import it.unict.gallosiciliani.liph.model.lemonety.EtyLink;
import it.unict.gallosiciliani.liph.model.lemonety.Etymology;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Import etymologies for {@link it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry} already in the knowledge
 * base.
 */
public class EtymologyImporter implements Consumer<DerivationPathNode> {
    private final EntityManager entityManager;
    private final EtymologyDerivationImporter derivationImporter;


    EtymologyImporter(final EntityManager entityManager, final Function<String,Form> etymonProvider, final IRIProvider iriProvider){
        this.entityManager=entityManager;
        derivationImporter=new EtymologyDerivationImporter(entityManager, iriProvider, etymonProvider);
    }


    @Override
    public void accept(final DerivationPathNode derivationPathNode) {
        derivationImporter.importDerivation(derivationPathNode);
        final Etymology etymology=new Etymology();
        final EtymologyIRIProvider etymologyIRIProvider=derivationImporter.getEtymologyIRIProvider();
        etymology.setId(etymologyIRIProvider.getEtymolgyIRI());
        final LexicalEntry entry=derivationImporter.getLemmaEntry();
        entry.getEtymology().add(etymology);

        etymology.setStartingLink(new EtyLink());
        etymology.getStartingLink().setId(etymologyIRIProvider.getEtyLinkIRI());
        etymology.getStartingLink().setEtySource(entry);
        etymology.getStartingLink().getEtySubSource().add(entry.getCanonicalForm());
        etymology.getStartingLink().setEtySubTarget(derivationImporter.getEtymon());

        entityManager.persist(etymology.getStartingLink());
        entityManager.persist(etymology);
    }
}

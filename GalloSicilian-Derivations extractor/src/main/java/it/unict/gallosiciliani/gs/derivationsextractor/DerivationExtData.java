package it.unict.gallosiciliani.gs.derivationsextractor;

import it.unict.gallosiciliani.derivations.DerivationPathNode;
import it.unict.gallosiciliani.derivations.DerivationPathNodeImpl;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenonOccurrence;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.LexicalEntry;
import it.unict.gallosiciliani.liph.model.lexinfo.LexInfo;
import lombok.Getter;

import java.util.List;

/**
 * All the information of interest concerning a single derivation. Perform all the required calculations
 * provided the raw data concerning an entry and the corresponding derivations
 *
 * @author Cristiano Longo
 */
public class DerivationExtData {
    @Getter
    private final String lemma;
    @Getter
    private final boolean noun;
    @Getter
    private final DerivationPathNode derivation;


    public DerivationExtData(final DerivationRawData src){
        final LexicalEntry entry=src.getEntry();
        lemma=entry.getCanonicalForm().getWrittenRep().get();
        noun=LexInfo.NOUN_INDIVIDUAL.equals(entry.getPartOfSpeech().getId());
        derivation=derivationChain2PathNode(src);
    }

    private DerivationPathNode derivationChain2PathNode(final DerivationRawData src) {
        final DerivationPathNode etymonPathNode=new DerivationPathNodeImpl(src.getEntry().getEtymology().iterator().next()
                .getStartingLink().getEtySubSource().iterator().next().getWrittenRep().get());
        return derivationChain2PathNode(src.getDerivationChain(), etymonPathNode);
    }

    /**
     * Convert a list of {@link LinguisticPhenomenonOccurrence} to a corresponding {@link DerivationPathNode}
     * @param derivationChain list of {@link LinguisticPhenomenonOccurrence}
     * @param etymonPathNode last derivation step
     * @return DerivationPathNode
     */
    private DerivationPathNode derivationChain2PathNode(final List<LinguisticPhenomenonOccurrence> derivationChain,
                                                        final DerivationPathNode etymonPathNode) {
        if (derivationChain.isEmpty()){
            return etymonPathNode;
        }
        final LinguisticPhenomenonOccurrence o=derivationChain.get(0);
        return new DerivationPathNodeImpl(o.getTarget().getWrittenRep().get(), o.getOccurrenceOf(),
                derivationChain2PathNode(derivationChain.subList(1, derivationChain.size()), etymonPathNode));
    }
}

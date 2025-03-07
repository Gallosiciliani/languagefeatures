package it.unict.gallosiciliani.derivations;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Consumer;

/**
 * Given a target (nicosian) lemma, holds derivations to it but save
 * only the shortest ones
 *
 * @author Cristiano Longo
 */
@Getter
public class ShortestDerivation implements Consumer<DerivationPathNode> {

    private final Collection<DerivationPathNode> derivation=new LinkedList<>();

    private int length=Integer.MAX_VALUE;

    @Override
    public void accept(final DerivationPathNode d) {
        final int dlength=d.length();
        if (dlength>length)
            return;
        if (dlength<length){
            length=dlength;
            derivation.clear();
        }
        derivation.add(d);
    }
}

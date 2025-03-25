package it.unict.gallosiciliani.derivations;

import java.util.*;
import java.util.function.Consumer;

/**
 * @author Cristiano Longo
 */
public class ShortestDerivationMap implements Consumer<DerivationPathNode> {
    private final Map<String, ShortestDerivation> lemmaToDerivations=new TreeMap<>();

    public ShortestDerivationMap(final List<String> expectedTargets){
        expectedTargets.forEach((lemma) -> lemmaToDerivations.put(lemma, new ShortestDerivation()));
    }

    @Override
    public void accept(final DerivationPathNode d) {
        final ShortestDerivation derivationsForLemma=lemmaToDerivations.get(d.get());
        if (derivationsForLemma!=null)
            derivationsForLemma.accept(d);
    }

    public Collection<ShortestDerivation> getDerivations(){
        return lemmaToDerivations.values();
    }
}

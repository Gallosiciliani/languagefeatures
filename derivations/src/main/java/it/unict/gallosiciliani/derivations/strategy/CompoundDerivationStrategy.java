package it.unict.gallosiciliani.derivations.strategy;

import it.unict.gallosiciliani.derivations.DerivationPathNode;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A derivation strategy consisting of several child strategies. Such a strategy goes on until at least one of the children strategies do.
 *
 * @author Cristiano Longo
 */
public class CompoundDerivationStrategy implements DerivationStrategy {

    @Getter
    private final DerivationPathNode derivation;
    private final Collection<DerivationStrategy> children;

    public CompoundDerivationStrategy(final Collection<DerivationStrategy> children) {
        derivation=children.iterator().next().getDerivation(); //assuming that all children share the same derivation
        this.children=children;
    }

    @Override
    public Collection<DerivationStrategy> branch(final Collection<DerivationPathNode> branches) {
        final Stream<DerivationStrategy> allGrandChildren=children.stream()
                .flatMap((c)->c.branch(branches).stream());
        final Map<DerivationPathNode, List<DerivationStrategy>> allGrandChilrenPerDerivation=allGrandChildren
                .collect(Collectors.groupingBy(DerivationStrategy::getDerivation));
        return allGrandChilrenPerDerivation.values().stream()
                .<DerivationStrategy>map(CompoundDerivationStrategy::new).toList();
    }

    @Override
    public void end() {
        children.forEach(DerivationStrategy::end);
    }

    public String toString(){
        return "CompoudDerivationFor("+derivation+")";
    }
}
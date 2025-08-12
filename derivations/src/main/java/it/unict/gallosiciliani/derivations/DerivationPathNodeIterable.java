package it.unict.gallosiciliani.derivations;

import java.util.Iterator;

/**
 * Iterate over a {@link DerivationPathNode}
 *
 * @author Cristiano Longo
 */
public class DerivationPathNodeIterable implements Iterable<DerivationPathNode>{

    private static class DerivationPathNodeIterator implements Iterator<DerivationPathNode>{
        private DerivationPathNode current;
        public DerivationPathNodeIterator(final DerivationPathNode src){
            this.current=src;
        }
        @Override
        public boolean hasNext() {
            return current!=null;
        }

        @Override
        public DerivationPathNode next() {
            final DerivationPathNode r=current;
            current=r.prev();
            return r;
        }
    }

    private final DerivationPathNode src;

    public DerivationPathNodeIterable(final DerivationPathNode src){
        this.src=src;
    }

    @Override
    public Iterator<DerivationPathNode> iterator() {
        return new DerivationPathNodeIterator(src);
    }
}

package it.unict.gallosiciliani.derivations;

/**
 * Produce derivations starting from a source form
 * @author Cristiano Longo
 */
public interface DerivationBuilder {

    void apply(final String src);
}

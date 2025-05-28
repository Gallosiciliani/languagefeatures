package it.unict.gallosiciliani.liph.util;


/**
 * Test for {@link TonicVowelAccentExplicitor}
 *
 * @author Cristiano Longo
 */
public class TonicVowelAccentExplicitorTest extends AbstractAccentExplicitorTest{
    private final TonicVowelAccentExplicitor e=new TonicVowelAccentExplicitor();

    @Override
    protected String getAccented(final String expr){
        return e.addGraveAccent(expr);
    }
}

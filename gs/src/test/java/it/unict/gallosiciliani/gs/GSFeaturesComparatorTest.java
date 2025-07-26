package it.unict.gallosiciliani.gs;

import it.unict.gallosiciliani.liph.util.HashedOntologyItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for {@link GSFeaturesComparator}
 * @author Cristiano Longo
 */
public class GSFeaturesComparatorTest {

    @Test
    void compareExecutableFeaturesOfDifferentCategories(){
        assertTrue(compare("assib.1", "degem.2") < 0);
        assertTrue(compare("degem.2", "assib.1") > 0);
        assertTrue(compare("assib.2", "degem.1") < 0);
        assertTrue(compare("degem.1", "assib.2") > 0);
    }

    @Test
    void compareExecutableFeaturesOfTheSameCategory(){
        assertTrue(compare("assib.1", "assib.2") < 0);
        assertTrue(compare("assib.2", "assib.1") > 0);
    }

    @Test
    void ensureFeatureNumberIsTreatedAsNumber(){
        assertTrue(compare("assib.2", "assib.11") < 0);
        assertTrue(compare("assib.11", "assib.2") > 0);
    }

    @Test
    void compareCategories(){
        assertTrue(compare("assib", "degem") < 0);
        assertTrue(compare("degem", "assib") > 0);
    }

    @Test
    void compareCategoriesWithExecutableFeatures(){
        assertTrue(compare("assib", "degem.5") < 0);
        assertTrue(compare("degem.5", "assib") > 0);
    }

    @Test
    void ensureCategoriesAreBeforeTheirMembers(){
        assertTrue(compare("assib", "assib.5") < 0);
        assertTrue(compare("assib.5", "assib") > 0);
    }

    @Test
    void shouldTakeIntoAccountThirdParts(){
        assertTrue(compare("assib.1.a", "assib.1.b") < 0);
        assertTrue(compare("assib.1.b", "assib.1.a") > 0);
    }

    int compare(final String pId, final String qId){
        final HashedOntologyItem p=mock(HashedOntologyItem.class);
        when(p.getId()).thenReturn(pId);
        final HashedOntologyItem q=mock(HashedOntologyItem.class);
        when(q.getId()).thenReturn(qId);
        return new GSFeaturesComparator().compare(p, q);
    }

}

package it.unict.gallosiciliani.importing.persistence;

import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.ontodriver.jena.connector.StorageConnector;
import it.unict.gallosiciliani.model.lemon.ontolex.Form;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for {@link InMemoryEntityManagerFactoryHelper}
 */
public class FileEntityManagerFactoryTest {

    @Test
    @Disabled
    void shouldCreateTheFile(){
        final String testFilePath="testFileEntityManagerFactory.ttl";
        final File f=new File(testFilePath);
        assertFalse(f.exists());
        try(final EntityManagerFactoryHelper h=new FileEntityManagerFactoryHelper(testFilePath)){
            final EntityManager m=h.createEntityManager();

            m.getTransaction().begin();
            final Form form=new Form();
            form.setId("http://example.org/form");
            m.persist(form);
            m.getTransaction().commit();

            m.getTransaction().begin();
            final Form form1=new Form();
            form.setId("http://example.org/form1");
            m.persist(form1);
            m.getTransaction().commit();

            assertTrue(f.exists());
            assertTrue(f.length()>0);
            m.close();
            m.unwrap(StorageConnector.class);
            System.out.println(f.getAbsolutePath());
        } finally {
            assertTrue(f.exists());
            f.delete();
        }
    }
}

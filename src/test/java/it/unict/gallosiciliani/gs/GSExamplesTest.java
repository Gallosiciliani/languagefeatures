package it.unict.gallosiciliani.gs;

import it.unict.gallosiciliani.liph.Validator;
import it.unict.gallosiciliani.util.OntologyLoader;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Validate derivations in the examples directory against Gallo-Sicilian
 * features
 */
public class GSExamplesTest {
    /**
     * àbetö<-N19b--àbetu<-N13b--àbitu
     */
    @Test
    void validateEx1() throws IOException {
        assertEquals(2, validate("ex1.ttl").getProcessedDerivations());
    }

    /**
     * bàcölö<-N19b--bàcölu<-N19a--bàculu
     */
    @Test
    void validateEx2() throws IOException{
        assertEquals(2, validate("ex2.ttl").getProcessedDerivations());
    }

    /**
     * agrefuòghjö<-S17--agrefuòljö<-N54a1--acrefuòljö<-N19b--acrefuòlju<-N16a--acrefólju<-N13b--acrifólju
     */
    @Test
    void validateEx3() throws IOException{
        assertEquals(5, validate("ex3.ttl").getProcessedDerivations());
    }

    /**
     * amè<-N03b--amàre
     */
    @Test
    void validateEx4() throws IOException{
        assertEquals(1, validate("ex4.ttl").getProcessedDerivations());
    }

    /**
     * Validate derivations in the specified file against Gallo-Sicilian features
     * @param dataFileName where derivations to be tested are stores
     * @throws IOException on error reading ontologies
     * @return the validator
     */
    private Validator validate(final String dataFileName) throws IOException{
        try(final OntologyLoader dataLoader = new OntologyLoader(dataFileName);
            final OntologyLoader featuresLoader = new OntologyLoader("gs-features.ttl")){
            final Validator v = new Validator(dataLoader.getModel());
            v.validate(featuresLoader.getModel());
            System.out.println("Validated "+v.getProcessedDerivations()+" derivations in "+dataFileName);
            assert v.getInvalidDerivations().isEmpty();
            return v;
        }
    }
}

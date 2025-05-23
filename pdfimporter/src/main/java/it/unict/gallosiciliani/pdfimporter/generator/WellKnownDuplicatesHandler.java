package it.unict.gallosiciliani.pdfimporter.generator;

import java.util.Set;
import java.util.TreeSet;

/**
 * Handle duplicate lemmas. Check whether a corresponding entry should be output or not (if it is a suplicate entry).
 *
 * @author Cristiano Longo
 */
class WellKnownDuplicatesHandler{

    private final Set<String> expectedDuplicates;
    private final Set<String> foundDuplicates;

    WellKnownDuplicatesHandler(){
        expectedDuplicates=new TreeSet<>();
        expectedDuplicates.add("cömenazziön"); //page 331 "cömenazziön1 sost.masch.", page 337 "cömenazziön sost.femm." both occurrences as noun
        expectedDuplicates.add("mangè"); // page 561 duplicate, both occurrences as verb
        expectedDuplicates.add("nvidè"); // page 704 "nvidè1 verbo QF(1) e nvidessë verbo pronom. QF(17)" and 705 "nvidè2 verbo QF(1)", different semantics but both verbs
        expectedDuplicates.add("scömödessë"); // page 900 "scömödessë verbo pronom.", 901 "scömödessë verbo pronom." duplicate entry, both occurrences as verb
        expectedDuplicates.add("scömödè"); // page 900 "scömödè verbo", 901 "scömödè verbo" duplicate entry, both occurrences as verb
        expectedDuplicates.add("stè"); // page 986 "stè1 verbo QF(26) e stessë verbo pronom." and 987 "stè2 verbo", both occurrences as verb
        expectedDuplicates.add("svëndö"); // page 1007 "svëndö verbo" "svëndö verbo", duplicate entry, both occurrences as verb
        expectedDuplicates.add("ventïessë"); // page 1056 "ventïessë verbo" and 1065 "ventïessë verbo pronom.", duplicate entry, both occurrences as verb
        expectedDuplicates.add("ventïè"); // page 1056 "ventïè verbo", 1065 "ventïè verbo QF(2) impers.", duplicate entry, both occurrences as verb
        foundDuplicates=new TreeSet<>();
    }

    /**
     *
     * @param lemma the lemma under consideration
     * @return true if a corresponding entry has to be created, false otherwise
     */
    boolean handle(final String lemma){
        return !expectedDuplicates.contains(lemma) || foundDuplicates.add(lemma);
    }
}

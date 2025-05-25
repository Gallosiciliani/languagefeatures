package it.unict.gallosiciliani.gs.derivationsimporter;

/**
 * Parse a derivation file and add the derivations listed in it to an OWL ontology containing the
 * derivations' lemmas.
 * The first argument is the path of the file containing the derivations. The second is the destination OWL file.
 * Finally, the third indicates a language tag that will be attached to the etymons in the OWL file.
 *
 * @author Cristiano Longo
 */
public class Main {
    public static void main(final String[] args){
        final String derivationFile=args[0];
        final String ontologyFile=args[1];
        final String sourceLanguageTag=args[2];
        System.out.println("Importing derivations from "+derivationFile+" to "+ontologyFile);
    }
}

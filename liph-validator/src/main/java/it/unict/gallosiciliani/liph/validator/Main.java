package it.unict.gallosiciliani.liph.validator;

import it.unict.gallosiciliani.liph.LiphDerivation;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFLanguages;
import org.apache.jena.riot.RDFParser;

import java.util.List;

/**
 * Validate a model against linguistic phenomena
 */
public class Main{


    public static void main(final String[] args) {
        if (args.length<2){
            System.out.println("Usage Validator <derivationsOntologyIRI> <definitionsOntologyIRI>");
            return;
        }
        final Model data = RDFParser.source(args[0]).lang(RDFLanguages.TTL).toModel();
        final Model defs = RDFParser.source(args[1]).lang(RDFLanguages.TTL).toModel();

        final Validator v = new Validator(data);
        v.validate(defs);

        System.out.println("Processed "+v.getProcessedDerivations()+" derivations.");
        final List<LiphDerivation> illegalDerivations = v.getInvalidDerivations();
        if (illegalDerivations.isEmpty()){
            System.out.println("All derivations are compliant with phenomena definitions");
            return;
        }
        System.out.println("Found "+illegalDerivations.size()+" illegal derivations");
        for(final LiphDerivation d : illegalDerivations){
            System.out.println("<"+d.getSourceIndividual()+"> writtenRep \""+d.getSourceWrittenRep()+"\";");
            System.out.println("\t<"+d.getPhenomenon().getId()+">  <"+d.getTargetIndividual()+"> writtenRep \""+d.getTargetWrittenRep()+"\" .");
        }
    }
}

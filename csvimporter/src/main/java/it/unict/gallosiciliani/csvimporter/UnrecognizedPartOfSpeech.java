package it.unict.gallosiciliani.csvimporter;

/**
 * During import an unrecognized part of speech has been found
 * @author Cristiano Longo
 */
public class UnrecognizedPartOfSpeech extends RuntimeException{
    UnrecognizedPartOfSpeech(final String category){
        super("Unrecognized part of speech "+category);
    }
}

package it.unict.gallosiciliani.gs.derivationsextractor;

import it.unict.gallosiciliani.gs.GSFeaturesCategory;
import it.unict.gallosiciliani.liph.model.LinguisticPhenomenon;

import java.util.List;

/**
 * Headers of the generated CSV file
 */
public enum DerivationDataCSVHeader {

    ID("id"), LEMMA("lemma vnisp"), TYPE("verbo/nome"), DERIVATION("derivazione"),
    MISSED("tratti disattesi"), RATE("nuovo indice di galloitalicità");

    private final String caption;

    DerivationDataCSVHeader(String caption){
        this.caption=caption;
    }

    @Override
    public String toString(){
        return caption;
    }

    /**
     * Convert enums to string
     * @return an array of header strings
     */
    public static String[] getHeaderRow(final List<GSFeaturesCategory> categories, final List<LinguisticPhenomenon> phenomena){
        final String[] row=new String[6+categories.size()+phenomena.size()];
        row[0]=ID.toString();
        row[1]=LEMMA.toString();
        row[2]=TYPE.toString();
        row[3]= DERIVATION.toString();
        row[4]= MISSED.toString();
        row[5]= RATE.toString();
        int i=6;
        for(final GSFeaturesCategory c: categories)
            row[i++]=c.getId();
        for(final LinguisticPhenomenon p: phenomena)
            row[i++]=p.getLabel();
        return row;
    }
}

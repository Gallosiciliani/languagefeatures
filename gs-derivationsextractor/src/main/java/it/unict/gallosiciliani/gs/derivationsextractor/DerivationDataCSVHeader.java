package it.unict.gallosiciliani.gs.derivationsextractor;

import it.unict.gallosiciliani.gs.GSFeaturesCategory;

import java.util.List;

/**
 * Headers of the generated CSV file
 */
public enum DerivationDataCSVHeader {

    ID("id"), LEMMA("lemma vnisp"), TYPE("verbo/nome"), DERIVATION("derivazione"),
    MISSED("tratti disattesi"), RATE("nuovo indice di galloitalicità"), FEATURE1("microtratto 1"),
    FEATURE2("microtratto 2"), FEATURE3("microtratto 3"), FEATURE4("microtratto 4"),
    FEATURE5("microtratto 5"), FEATURE6("microtratto 6"), FEATURE7("microtratto 7"),
    FEATURE8("microtratto 8");

    public static final DerivationDataCSVHeader[] FEATURE_HEADERS={FEATURE1, FEATURE2, FEATURE3, FEATURE4, FEATURE5,
            FEATURE6, FEATURE7, FEATURE8};

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
    public static String[] getHeaderRow(final List<GSFeaturesCategory> categories){
        final String[] row=new String[6+categories.size()+FEATURE_HEADERS.length];
        row[0]=ID.toString();
        row[1]=LEMMA.toString();
        row[2]=TYPE.toString();
        row[3]= DERIVATION.toString();
        row[4]= MISSED.toString();
        row[5]= RATE.toString();
        int i=6;
        for(final GSFeaturesCategory c: categories)
            row[i++]=c.getId();
        for(final DerivationDataCSVHeader featureHeader: FEATURE_HEADERS)
            row[i++]=featureHeader.toString();
        return row;
    }
}

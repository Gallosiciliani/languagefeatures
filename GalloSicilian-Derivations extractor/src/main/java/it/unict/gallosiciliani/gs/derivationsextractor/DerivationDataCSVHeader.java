package it.unict.gallosiciliani.gs.derivationsextractor;

/**
 * Headers of the generated CSV file
 */
public enum DerivationDataCSVHeader {

    ID("id"), TYPE("verbo/nome");

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
    public static String[] getHeaderRow(){
        final String[] row=new String[DerivationDataCSVHeader.values().length];
        for(int i=0; i<DerivationDataCSVHeader.values().length; i++)
            row[i]=DerivationDataCSVHeader.values()[i].toString();
        return row;
    }
}

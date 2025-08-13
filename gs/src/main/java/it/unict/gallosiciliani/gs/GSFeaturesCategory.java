package it.unict.gallosiciliani.gs;

import it.unict.gallosiciliani.liph.util.HashedOntologyItem;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A set of gallo-sicilian features
 */
public class GSFeaturesCategory extends HashedOntologyItem {

    public static final Comparator<GSFeaturesCategory> COMPARATOR_BY_IRI= Comparator.comparing(HashedOntologyItem::getIri);

    @Getter
    @Setter
    private String label;
    @Getter
    @Setter
    private String comment;
    @Getter
    private final SortedSet<HashedOntologyItem> members;
    public GSFeaturesCategory(final String iri, final String ns){
        super(iri, ns);
        members=new TreeSet<>(new GSFeaturesComparator());
    }

    public void addMember(final HashedOntologyItem member){
        if (!members.add(member))
            throw new IllegalArgumentException("Duplicate category member "+member.getIri());
    }

}

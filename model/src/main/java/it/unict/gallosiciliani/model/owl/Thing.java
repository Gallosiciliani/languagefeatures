
package it.unict.gallosiciliani.model.owl;

import cz.cvut.kbss.jopa.model.MultilingualString;
import cz.cvut.kbss.jopa.model.annotations.*;
import cz.cvut.kbss.jopa.vocabulary.OWL;
import cz.cvut.kbss.jopa.vocabulary.RDFS;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.net.URI;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


/**
 * This class was generated by OWL2Java 1.2.2
 * 
 */
@OWLClass(iri = OWL.THING)
public class Thing
    implements Serializable
{

    @Id(generated = true)
    protected String id;
    @OWLAnnotationProperty(iri = RDFS.LABEL)
    protected String name;
    @OWLAnnotationProperty(iri = cz.cvut.kbss.jopa.vocabulary.DC.Elements.DESCRIPTION)
    protected String description;
    @OWLAnnotationProperty(iri = RDFS.COMMENT)
    private MultilingualString comment;
    @OWLAnnotationProperty(iri = RDFS.NAMESPACE+"seeAlso")
    @Getter
    @Setter
    private URI seeAlso;

    @Types(readOnly = true, fetchType = FetchType.EAGER)
    private Set<String> types = new TreeSet<>();

    @Properties
    protected Map<String, Set<String>> properties;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
    }

    public Set<String> getTypes() {
        return types;
    }

    public void setProperties(Map<String, Set<String>> properties) {
        this.properties = properties;
    }

    public Map<String, Set<String>> getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        return ((((("Thing {"+ name)+"<")+ id)+">")+"}");
    }

}

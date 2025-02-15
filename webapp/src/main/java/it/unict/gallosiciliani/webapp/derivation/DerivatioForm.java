package it.unict.gallosiciliani.webapp.derivation;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * Model object for the derivation form
 */
@Data
public class DerivatioForm {
    private String lemma;
    private String etymon;
    private List<String> derivations=new LinkedList<>();
}

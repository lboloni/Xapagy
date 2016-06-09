/*
   This file is part of the Xapagy project
   Created on: Aug 4, 2013
 
   org.xapagy.ui.tempdyn.tdComponent
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.tempdyn;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.xapagy.headless_shadows.Choice;

/**
 * The representation of a component in the TD - these can be instance, vi or
 * choice.
 * 
 * What are we representing about this choice, will be described in the tdValue
 * 
 * @author Ladislau Boloni
 * 
 */
public class tdComponent implements Serializable {

    enum tdComponentType {
        CHOICE, INSTANCE, VI
    }

    private static final long serialVersionUID = -5506541486615966013L;;

    /**
     * The choice object (only for the case when it is a choice object)
     */
    private Choice choice;
    /**
     * The identifier - the same as the identifier of the components
     */
    private String identifier;
    /**
     * The list of the pretty prints of the component - covers the changes
     */
    private List<String> prettyPrints = new ArrayList<>();
    /**
     * The type of the component - currently instance or VI. Future
     * possibilities are Concept and Verb for learning.
     */
    private tdComponentType type;

    public tdComponent(Choice choice) {
        this.type = tdComponentType.CHOICE;
        this.choice = choice;
        this.identifier = choice.getIdentifier();
    }

    /**
     * Creates a tdComponent of type Instance or Vi
     * 
     * @param type
     * @param identifier
     */
    public tdComponent(tdComponentType type, String identifier) {
        super();
        this.type = type;
        this.identifier = identifier;
    }

    /**
     * @return the choice
     */
    public Choice getChoice() {
        return choice;
    }

    public String getIdentifier() {
        return identifier;
    }

    /**
     * Gets the last pretty print
     * 
     * @return
     */
    public String getLastPrettyPrint() {
        return prettyPrints.get(prettyPrints.size() - 1);
    }

    /**
     * Returns the list of pretty prints
     * 
     * @return
     */
    public List<String> getPrettyPrints() {
        return prettyPrints;
    }

    public tdComponentType getType() {
        return type;
    }

    /**
     * If the pretty print is already there, ignores it, if not, appends it
     * 
     * @param pp
     */
    public void recordPrettyPrint(String pp) {
        for (String p : prettyPrints) {
            if (pp.equals(p)) {
                return;
            }
        }
        prettyPrints.add(pp);
    }

}

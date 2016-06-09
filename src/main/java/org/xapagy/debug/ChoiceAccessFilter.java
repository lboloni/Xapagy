/*
   This file is part of the Xapagy project
   Created on: Jun 29, 2014

   org.xapagy.debug.ChoiceAccessFilter

   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.debug;

import java.io.Serializable;

import org.xapagy.headless_shadows.Choice.ChoiceType;
import org.xapagy.instances.ViStructureHelper.ViType;

/**
 * This object captures all the parameters of the filtering in
 * the choice access in a single object.
 * 
 * It also adds a name field, which allows for later printing etc.
 * 
 * It is an unmutable object.
 * 
 * @author Ladislau Boloni
 *
 */
public class ChoiceAccessFilter implements Serializable {

    private static final long serialVersionUID = -2088887124297993609L;
    private ChoiceType choiceType;
    private String name;
    private String params[];
    private ViType viType;

    /**
     * Constructor. It also allows the passing of the params as a variable lenght 
     * param list
     * 
     * @param name
     * @param choiceType
     * @param viType
     * @param params
     */
    public ChoiceAccessFilter(String name, ChoiceType choiceType,
            ViType viType, String... params) {
        super();
        this.name = name;
        this.choiceType = choiceType;
        this.viType = viType;
        this.params = params;
    }

    public ChoiceType getChoiceType() {
        return choiceType;
    }

    public String getName() {
        return name;
    }

    public String[] getParams() {
        return params;
    }

    public ViType getViType() {
        return viType;
    }

}

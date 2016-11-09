/*
   
    This file is part of the Xapagy Cognitive Architecture 
    Copyright (C) 2008-2017 Ladislau Boloni

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
   
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
 * Created on: Jun 29, 2014
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

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
package org.xapagy.metaverbs;

import org.xapagy.activity.SpikeActivity;
import org.xapagy.agents.Agent;
import org.xapagy.instances.ViStructureHelper.ViType;

/**
 * An SA associated with a specific type of verbs. Triggered when a meta-verb is
 * entered into the focus.
 * 
 * Current additional functionality is only an additional check that it is
 * applied to the right kind of VI.
 * 
 * @author Ladislau Boloni
 * Created on: May 20, 2011
 */
public abstract class AbstractSaMetaVerb extends SpikeActivity {

    private static final long serialVersionUID = -100545172467514753L;
    private ViType viType;

    /**
     * Creates a metaverb SA.
     * 
     * @param agent
     * @param name
     * @param viType
     */
    public AbstractSaMetaVerb(Agent agent, String name, ViType viType) {
        super(agent, name);
        this.viType = viType;
    }

    /**
     * @return the ViType
     */
    public ViType getViType() {
        return viType;
    }

}

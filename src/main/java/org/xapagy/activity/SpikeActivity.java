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
package org.xapagy.activity;

import org.xapagy.agents.Agent;
import org.xapagy.instances.VerbInstance;

/**
 * A spike activity describes an activity which happens instantanously, makes
 * changes in the Xapagy agent etc.
 * 
 * @author Ladislau Boloni
 * Created on: Apr 22, 2011
 */
public abstract class SpikeActivity extends Activity {

    private static final long serialVersionUID = 1962197695621001378L;

    /**
     * Default constructor, forces the agent and name on the others
     * 
     * @param agent
     * @param name
     */
    public SpikeActivity(Agent agent, String name) {
        super(agent, name);
    }

    /**
     * Final version, overwrite apply inner. The idea is that you have hooks
     * here for tracing
     */
    public final void apply(VerbInstance vi) {
        applyInner(vi);
    }

    /**
     * This is where inherited classes should put their component
     */
    public abstract void applyInner(VerbInstance vi);

}

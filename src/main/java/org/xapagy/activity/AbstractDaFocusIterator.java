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
import org.xapagy.agents.Focus;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.set.EnergyColors;
import org.xapagy.shadows.Shadows;

/**
 * Abstract class, the root of all the iterators which iterate over the focus
 * and deal with all the components separately. If the DA does something with
 * any of the components, it should overwrite the specific item
 * 
 * @author Ladislau Boloni
 * Created on: Jan 31, 2012
 */
public abstract class AbstractDaFocusIterator extends DiffusionActivity {

    private static final long serialVersionUID = 6988458321004717052L;
    protected Focus fc;
    protected Shadows sf;

    /**
     * @param agent
     * @param strength
     * @throws Exception
     */
    public AbstractDaFocusIterator(Agent agent, String name) {
        super(agent, name);
        fc = agent.getFocus();
        sf = agent.getShadows();
    }

    /**
     * Called for every non-scene instance in the focus. If the DA want's to
     * perform some operation, it should override this function
     * 
     * @param fi
     * @param time
     */
    protected void applyFocusNonSceneInstance(Instance fi, double time) {
        // empty here, to be overridden
    }

    /**
     * Called for every scene in the focus. If the DA want's to perform some
     * operation, it should override this function
     * 
     * @param fi
     * @param time
     */
    protected void applyFocusScene(Instance fi, double time) {
        // empty here, to be overridden
    }

    /**
     * Called for every VI in the focus. If the DA want's to perform some
     * operation, it should override this function
     * 
     * @param fvi
     * @param time
     */
    protected void applyFocusVi(VerbInstance fvi, double time) {
        // empty here, to be overridden above
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xapagy.activity.DiffusionActivity#applyInner(double)
     */
    @Override
    protected final void applyInner(double time) {
        for (Instance f : fc.getInstanceList(EnergyColors.FOCUS_INSTANCE)) {
            applyFocusNonSceneInstance(f, time);
        }
        for (Instance f : fc.getSceneList(EnergyColors.FOCUS_INSTANCE)) {
            applyFocusScene(f, time);
        }
        for (VerbInstance fvi : fc.getViListAllEnergies()) {
            applyFocusVi(fvi, time);
        }
    }

}

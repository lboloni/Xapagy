/*
   This file is part of the Xapagy project
   Created on: Jan 31, 2012
 
   org.xapagy.activity.shadowmaintenance.AbstractDaShm
 
   Copyright (c) 2008-2014 Ladislau Boloni
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
 * 
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

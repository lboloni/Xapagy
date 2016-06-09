/*
   This file is part of the Xapagy project
   Created on: Nov 11, 2011
 
   org.xapagy.activity.DaShadowIdentity
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.activity.shadowmaintenance;

import java.util.HashSet;
import java.util.Set;

import org.xapagy.activity.AbstractDaFocusIterator;
import org.xapagy.agents.Agent;
import org.xapagy.concepts.Hardwired;
import org.xapagy.instances.Instance;
import org.xapagy.instances.RelationHelper;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.EnergyQuantum;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * The objective of this DA is to reinforce in the shadow instances which are
 * identity linked to the focus instance.
 * 
 * May 2014: for the time being I disconnected this in the default, because it
 * will need to be thought through and parameterized.
 * 
 * @author Ladislau Boloni
 * 
 */
public class DaShmIdentity extends AbstractDaFocusIterator {

    private static final long serialVersionUID = -6984435852097176580L;

    /**
     * 
     * @param agent
     * @param name
     */
    public DaShmIdentity(Agent agent, String name) {
        super(agent, name);
    }

    /**
     * (a) Brings in instances which are identity related to this one
     * 
     * (b) Reinforces those instances which are identity related
     * 
     * FIXME: this will probably need to be a multiplicative component
     * 
     * @param fi
     * @param timeSlice
     */
    @Override
    protected void applyFocusNonSceneInstance(Instance fi, double timeSlice) {
        Set<Instance> ids = new HashSet<>();
        // From
        Set<Instance> idsFrom =
                RelationHelper.getRelationSpecificPart(agent,
                        Hardwired.VR_IDENTITY, fi, ViPart.Subject,
                        ViPart.Object);
        ids.addAll(idsFrom);
        // To
        Set<Instance> idsTo =
                RelationHelper.getRelationSpecificPart(agent,
                        Hardwired.VR_IDENTITY, fi, ViPart.Object,
                        ViPart.Subject);
        ids.addAll(idsTo);
        // if (!ids.isEmpty()) {
        // TextUi.println("Found some identity links!!!");
        // }

        for (Instance id : ids) {
            if (id != fi) {
                double addition = 1.0;
                EnergyQuantum<Instance> sq =
                        EnergyQuantum.createAdd(fi, id, addition, timeSlice,
                                EnergyColors.SHI_GENERIC,
                                "DaShmIdentity + applyFocusNonSceneInstance-1");
               sf.applyInstanceEnergyQuantum(sq);
            }
        }
        // reinforcing those instances which are identical
        for (Instance si : sf.getMembers(fi, EnergyColors.SHI_GENERIC)) {
            if (ids.contains(si)) {
                double oldValue =
                        sf.getSalience(fi, si, EnergyColors.SHI_GENERIC);
                EnergyQuantum<Instance> sq =
                        EnergyQuantum.createAdd(fi, si, oldValue, timeSlice,
                                EnergyColors.SHI_GENERIC, "DaShmIdentity + applyFocusNonSceneInstance-2");
                sf.applyInstanceEnergyQuantum(sq);
            }
        }

    }

    /* (non-Javadoc)
     * @see org.xapagy.activity.DiffusionActivity#extractParameters()
     */
    @Override
    public void extractParameters() {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IxwFormattable#formatTo(org.xapagy.ui.formatters.IXwFormatter, int)
     */
    @Override
    public void formatTo(IXwFormatter fmt, int detailLevel) {
         fmt.add("DaShmIdentity");
    }
}

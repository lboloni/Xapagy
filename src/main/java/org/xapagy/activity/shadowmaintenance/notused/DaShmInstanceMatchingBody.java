/*
   This file is part of the Xapagy project
   Created on: Apr 23, 2011
 
   org.xapagy.story.activity.DAIMatchingShadow
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.activity.shadowmaintenance.notused;

import org.xapagy.activity.AbstractDaFocusIterator;
import org.xapagy.activity.shadowmaintenance.AmLookup;
import org.xapagy.activity.shadowmaintenance.SaicHelper;
import org.xapagy.activity.shadowmaintenance.ShmAddItem;
import org.xapagy.activity.shadowmaintenance.ShmAddItemCollection;
import org.xapagy.agents.Agent;
import org.xapagy.instances.Instance;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.InstanceSet;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * For each instance shadow, increase the instances which are matching the
 * existing components of the shadow. This Da allows shadowing where there is no
 * direct overlap between the attributes of the focus instance and the shadow
 * instance
 * 
 * As of May 2014 this is disconnected as too expensive...
 * 
 * @author Ladislau Boloni
 * 
 */
public class DaShmInstanceMatchingBody extends AbstractDaFocusIterator {

    private static final long serialVersionUID = -314368013802146707L;

    /**
     * 
     * @param agent
     * @param name
     */
    public DaShmInstanceMatchingBody(Agent agent, String name) {
        super(agent, name);
    }
 
    /**
     * The salience of the root in the shadow initiates a additive the energy
     * transfer to the secondary
     */
    @Override
    protected void applyFocusNonSceneInstance(Instance fi, double timeSlice) {
        ShmAddItemCollection saic = new ShmAddItemCollection();
        String ec = EnergyColors.SHI_GENERIC;
        for (Instance si : sf.getMembers(fi, ec)) {
            double salience = sf.getSalience(fi, si, ec);
            InstanceSet coreMatches =
                    AmLookup.lookupCo(agent.getAutobiographicalMemory(),
                            si.getConcepts(), EnergyColors.AM_INSTANCE);
            for (Instance matchInstance : coreMatches.getParticipants()) {
                // a guard for preventing scenes to shadow non-scenes
                if (matchInstance.isScene()) {
                    continue;
                }
                double matchLevel = coreMatches.value(matchInstance);
                double addEnergy = salience * matchLevel;
                ShmAddItem sai =
                        new ShmAddItem(agent, matchInstance, fi, addEnergy, ec);
                saic.addShmAddItem(sai);
            }
        }
        SaicHelper.applySAIC_Instances(agent, saic, timeSlice, "DaShmInstanceMatchingBody.applyFNSI_Stochastic");
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
        fmt.add("DaShmInstanceMatchingBody");
    }

}

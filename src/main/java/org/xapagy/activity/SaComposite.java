/*
   This file is part of the Xapagy project
   Created on: Feb 12, 2016
 
   org.xapagy.activity.SaComposite
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.instances.VerbInstance;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * A composite SA that triggers the associated SAs in order
 * 
 * @author Ladislau Boloni
 *
 */
public class SaComposite extends SpikeActivity {

    /**
     * @param agent
     * @param name
     */
    public SaComposite(Agent agent, String name) {
        super(agent, name);
    }

    private static final long serialVersionUID = 2627662683710067302L;


    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IxwFormattable#formatTo(org.xapagy.ui.formatters.IXwFormatter, int)
     */
    @Override
    public void formatTo(IXwFormatter fmt, int detailLevel) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.xapagy.activity.Activity#extractParameters()
     */
    @Override
    public void extractParameters() {
    }

    private List<SpikeActivity> members = new ArrayList<>();
    
    
    /**
     * Returns an unmodifiable list of the member DAs
     * @return
     */
    public List<SpikeActivity> getSpikeActivities() {
        return Collections.unmodifiableList(members);
    }
    
    
    /**
     * Adds a new diffusion activity at the end of the member DA list
     * @param da
     */
    public void addSpikeActivity(SpikeActivity sa) {
        members.add(sa);
    }
    
    public void clearSpikeActivities() {
        members.clear();
    }
    
    /* (non-Javadoc)
     * @see org.xapagy.activity.DiffusionActivity#applyInner(double)
     */
    @Override
    public void applyInner(VerbInstance vi) {
        for(SpikeActivity member: members) {
            member.applyInner(vi);
        }
    }
    
}

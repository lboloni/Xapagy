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
 * Created on: Feb 12, 2016
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

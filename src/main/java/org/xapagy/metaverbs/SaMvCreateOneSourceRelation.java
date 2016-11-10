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

import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.instances.RelationHelper;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * @author Ladislau Boloni
 * Created on: Jan 5, 2012
 */
public class SaMvCreateOneSourceRelation extends AbstractSaMvRelation {
    private static final long serialVersionUID = 892120352386709441L;

    /**
     * @param name
     * @param agent
     * @param activityResources
     * @param viType
     */
    public SaMvCreateOneSourceRelation(Agent agent) {
        super(agent, "SaMvCreateOneSourceRelation", ViType.S_V_O);
    }

    /**
     * Remove the relations incompatible between the two instances, and the
     * compatible relations from the same source. Add the new relation.
     */
    @Override
    public void applyInner(VerbInstance verbInstance) {
        VerbOverlay voNew = getRelationVo(verbInstance);
        List<VerbInstance> relationsToRemove =
                getIncompatiblesBetweenTheSameInstances(verbInstance, voNew);
        relationsToRemove.addAll(getCompatiblesFromSameSource(verbInstance, voNew));
        removeRelations(relationsToRemove);
        RelationHelper.createAndAddRelation(agent, voNew,
                verbInstance.getSubject(), verbInstance.getObject());
    }
    
    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IxwFormattable#formatTo(org.xapagy.ui.formatters.IXwFormatter, int)
     */
    @Override
    public void formatTo(IXwFormatter fmt, int detailLevel) {
        fmt.add("SaMvCreateOneSourceRelation");
    }

    /* (non-Javadoc)
     * @see org.xapagy.activity.Activity#extractParameters()
     */
    public void extractParameters() {
    }

}

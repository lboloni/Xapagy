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
 * Adds a new relation of a specific type between the subject and the object
 * 
 * Removes the incompatibles between the same nodes
 * 
 * @author Ladislau Boloni
 * Created on: Aug 17, 2010
 */
public class SaMvCreateRelation extends AbstractSaMvRelation {
    private static final long serialVersionUID = 6385252189678936219L;

    public SaMvCreateRelation(Agent agent) {
        super(agent, "SaMvCreateRelation", ViType.S_V_O);
    }

    /**
     * Expire the relations which are incompatible between the two components.
     * 
     * Add the new relations.
     * 
     */
    @Override
    public void applyInner(VerbInstance verbInstance) {
        VerbOverlay voNew = getRelationVo(verbInstance);
        List<VerbInstance> relationsToRemove =
                getIncompatiblesBetweenTheSameInstances(verbInstance, voNew);
        removeRelations(relationsToRemove);
        RelationHelper.createAndAddRelation(agent, voNew,
                verbInstance.getSubject(), verbInstance.getObject());
    }

    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IxwFormattable#formatTo(org.xapagy.ui.formatters.IXwFormatter, int)
     */
    @Override
    public void formatTo(IXwFormatter fmt, int detailLevel) {
        fmt.add("SaMvCreateRelation");
    }

    /* (non-Javadoc)
     * @see org.xapagy.activity.Activity#extractParameters()
     */
    public void extractParameters() {
    }
    
}

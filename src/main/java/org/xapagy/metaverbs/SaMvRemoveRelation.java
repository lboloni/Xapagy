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
import org.xapagy.instances.VerbInstance;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * Removes all the relations of a specific type between the subject and the
 * object
 * 
 * @author Ladislau Boloni
 * Created on: Oct 27, 2010
 */
public class SaMvRemoveRelation extends AbstractSaMvRelation {
    private static final long serialVersionUID = 495622561212938906L;

    public SaMvRemoveRelation(Agent agent) {
        super(agent, "SaMvRemoveRelation", null);
    }

    /**
     * Removes all the relations between the same subject and object which are
     * matching the relations specified in the verb part
     */
    @Override
    public void applyInner(VerbInstance verbInstance) {
        VerbOverlay vo = getRelationVo(verbInstance);
        List<VerbInstance> relationsToRemove =
                getCompatiblesBetweenTheSameInstances(verbInstance, vo);
        removeRelations(relationsToRemove);
    }

    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IxwFormattable#formatTo(org.xapagy.ui.formatters.IXwFormatter, int)
     */
    @Override
    public void formatTo(IXwFormatter fmt, int detailLevel) {
        fmt.add("SaMvRemoveRelation");
    }

    /* (non-Javadoc)
     * @see org.xapagy.activity.Activity#extractParameters()
     */
    public void extractParameters() {
    }
}

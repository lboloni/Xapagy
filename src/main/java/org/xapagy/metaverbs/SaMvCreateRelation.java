/*
   This file is part of the Xapagy project
   Created on: Aug 17, 2010
 
   org.xapagy.storyvisualizer.model.verbs.vbHasA
 
   Copyright (c) 2008-2010 Ladislau Boloni
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
 * 
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

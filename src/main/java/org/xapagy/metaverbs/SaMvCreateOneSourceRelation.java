/*
   This file is part of the Xapagy project
   Created on: Jan 5, 2012
 
   org.xapagy.metaverbs.SaMvCreateOneSourceRelation
 
   Copyright (c) 2008-2014 Ladislau Boloni
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
 * 
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

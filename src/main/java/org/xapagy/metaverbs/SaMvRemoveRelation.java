/*
   This file is part of the Xapagy project
   Created on: Oct 27, 2010
 
   org.xapagy.domain.basic.vbGives
 
   Copyright (c) 2008-2010 Ladislau Boloni
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
 * 
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

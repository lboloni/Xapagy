/*
   This file is part of the Xapagy project
   Created on: Apr 12, 2014
 
   org.xapagy.ui.prettyhtml.objects.pwRrContext
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettygeneral;

import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.instances.VerbInstance;
import org.xapagy.reference.rrContext;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * @author Ladislau Boloni
 * 
 */
public class xwRrContext {

    public static String
            xwDetailed(IXwFormatter xw, rrContext rrc, Agent agent) {
        xw.addLabelParagraph("RrContext");
        xw.indent();
        xw.is("referenceType", rrc.getReferenceType());
        switch (rrc.getReferenceType()) {
        case REF_DIRECT: {
            xw.is("coDirect", xwConceptOverlay.xwConcise(xw.getEmpty(), rrc.getCoDirect(), agent));
            break;
        }
        case REF_GROUP: {
            xw.addLabelParagraph("group members:");
            xw.indent();
            for (rrContext member : rrc.getGroupMembers()) {
                xwRrContext.xwDetailed(xw, member, agent);
            }
            xw.deindent();
            break;
        }
        case REF_RELATIONAL: {
            xw.addLabelParagraph("relations");
            xw.indent();
            List<ConceptOverlay> cos = rrc.getRelationCOs();
            List<VerbOverlay> relations = rrc.getRelationChain();
            for (int i = 0; i != cos.size(); i++) {
                ConceptOverlay co = cos.get(i);
                xw.is("CO " + i, xwConceptOverlay.xwConcise(xw.getEmpty(), co, agent));
                if (i < relations.size()) {
                    VerbOverlay relation = relations.get(i);
                    xw.is("REL " + i, xwVerbOverlay.xwConcise(xw, relation, agent));
                }
            }
            xw.deindent();
            break;
        }

        default: {
            xw.addErrorMessage("<ERROR:> pwRrContext: I don't know how to print an rrContext of this type");
            break;
        }
        }
        xw.is("partInVi", rrc.getPartInVi());
        xw.is("verbsInVi", xwVerbOverlay.xwConcise(xw.getEmpty(), rrc.getVerbsInVi(), agent));
        xw.is("scene", xwInstance.xwConcise(xw.getEmpty(), rrc.getScene(), agent));
        VerbInstance viInquitParent = rrc.getViInquitParent();
        if (viInquitParent != null) {
            xw.is("viInquitParent",
                    xwVerbInstance.xwConcise(xw.getEmpty(), viInquitParent, agent));
        } else {
            xw.is("viInquitParent", "<< null >>");
        }
        xw.deindent();
        return xw.toString();
    }

}

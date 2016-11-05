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
import org.xapagy.ui.prettyprint.PpConceptOverlay;
import org.xapagy.ui.prettyprint.PpInstance;
import org.xapagy.ui.prettyprint.PpVerbInstance;
import org.xapagy.ui.prettyprint.PpVerbOverlay;

/**
 * @author Ladislau Boloni
 * 
 */
public class xwRrContext {

    public static String
            xwDetailed(IXwFormatter xwf, rrContext rrc, Agent agent) {
        xwf.addLabelParagraph("RrContext");
        xwf.indent();
        xwf.is("referenceType", rrc.getReferenceType());
        switch (rrc.getReferenceType()) {
        case REF_DIRECT: {
            // FIXME: make it pwConceptOverlay.pwConcise
            xwf.is("coDirect",
                    PpConceptOverlay.ppConcise(rrc.getCoDirect(), agent));
            break;
        }
        case REF_GROUP: {
            xwf.addLabelParagraph("group members:");
            xwf.indent();
            for (rrContext member : rrc.getGroupMembers()) {
                xwRrContext.xwDetailed(xwf, member, agent);
            }
            xwf.deindent();
            break;
        }
        case REF_RELATIONAL: {
            xwf.addLabelParagraph("relations");
            xwf.indent();
            List<ConceptOverlay> cos = rrc.getRelationCOs();
            List<VerbOverlay> relations = rrc.getRelationChain();
            for (int i = 0; i != cos.size(); i++) {
                ConceptOverlay co = cos.get(i);
                xwf.is("CO " + i, PpConceptOverlay.ppConcise(co, agent));
                if (i < relations.size()) {
                    VerbOverlay relation = relations.get(i);
                    xwf.is("REL " + i, PpVerbOverlay.ppConcise(relation, agent));
                }
            }
            xwf.deindent();
            break;
        }

        default: {
            xwf.addErrorMessage("<ERROR:> pwRrContext: I don't know how to print an rrContext of this type");
            break;
        }
        }
        xwf.is("partInVi", rrc.getPartInVi());
        xwf.is("verbsInVi", PpVerbOverlay.ppConcise(rrc.getVerbsInVi(), agent));
        xwf.is("scene", PpInstance.ppConcise(rrc.getScene(), agent));
        VerbInstance viInquitParent = rrc.getViInquitParent();
        if (viInquitParent != null) {
            xwf.is("viInquitParent",
                    PpVerbInstance.ppConcise(viInquitParent, agent));
        } else {
            xwf.is("viInquitParent", "<< null >>");
        }
        xwf.deindent();
        return xwf.toString();
    }

}

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
 * Created on: Apr 12, 2014
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

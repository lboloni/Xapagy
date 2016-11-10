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

import org.xapagy.agents.Agent;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.concepts.operations.Incompatibility;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.prettyprint.PrettyPrint;

/**
 * is-a verb
 * 
 * Adds a new concept to the existing ones. Good question whether negation can
 * happen here, but it will be no chaining, that is for sure.
 * 
 * @author Ladislau Boloni
 * Created on: Aug 17, 2010
 */
public class SaMvIsA extends AbstractSaMetaVerb {
    private static final long serialVersionUID = 3512703713289011223L;

    public SaMvIsA(Agent agent) {
        super(agent, "SaMvIsA", ViType.S_ADJ);
    }

    /**
     * Executing the is-a verb component
     */
    @Override
    public void applyInner(VerbInstance verbInstance) {
        Instance subject = verbInstance.getSubject();
        ConceptOverlay adjective = verbInstance.getAdjective();
        if (Incompatibility.decideIncompatibility(subject.getConcepts(),
                adjective)) {
            TextUi.errorPrint("SaMvIsA: adding incompatible concepts!");
            TextUi.errorPrint("at: "
                    + PrettyPrint.ppDetailed(verbInstance, agent));
            Incompatibility.decideIncompatibility(subject.getConcepts(),
                    adjective);
            throw new Error("SaMvIsA: adding incompatible concepts!");
        }
        subject.getConcepts().addOverlay(adjective, 1.0);
        for (String label : adjective.getLabels()) {
            subject.getConcepts().addFullLabel(label, agent);
        }
    }

    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IxwFormattable#formatTo(org.xapagy.ui.formatters.IXwFormatter, int)
     */
    @Override
    public void formatTo(IXwFormatter fmt, int detailLevel) {
        fmt.add("SaMvIs");
    }

    /* (non-Javadoc)
     * @see org.xapagy.activity.Activity#extractParameters()
     */
    public void extractParameters() {
    }
}

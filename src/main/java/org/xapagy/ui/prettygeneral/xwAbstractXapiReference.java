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

import org.xapagy.agents.Agent;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.prettyprint.PrettyPrint;
import org.xapagy.xapi.reference.AbstractXapiReference;

/**
 * @author Ladislau Boloni
 * Created on: Jan 3, 2012
 */
public class xwAbstractXapiReference {

    /**
     * Normally, this will not be called directly, only from the more specific
     * variants
     * 
     * @param xw
     * @param axr
     * @param agent
     * @return
     */
    public static String xwConcise(IXwFormatter xw, AbstractXapiReference axr, Agent agent) {
        xw.is("Text", axr.getText());
        xw.is("Type / Level", axr.getType() + " / " + axr.getXapiLevel());
        xw.is("Position in parent", axr.getPositionInParent());
        return xw.toString();
    }

    /**
     * Normally, this will not be called directly, only from the more specific
     * variants
     * 
     * @param xw
     * @param axr
     * @param agent
     * @return
     */
    public static String xwDetailed(IXwFormatter xw, AbstractXapiReference axr, Agent agent) {
        xw.add(xwAbstractXapiReference.xwConcise(xw.getEmpty(), axr, agent));
        xw.addLabelParagraph("ResolutionConfidence");
        xw.indent();
        xw.addP(PrettyPrint.ppDetailed(axr.getResolutionConfidence(),
                agent));
        xw.deindent();
        xw.addLabelParagraph("Parent");
        xw.indent();
        xw.add(PrettyPrint.ppDetailed(axr.getParent(), agent));
        xw.deindent();
        return xw.toString();
    }
}

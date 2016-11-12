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
package org.xapagy.ui.prettyprint;

import org.xapagy.agents.Agent;
import org.xapagy.ui.formatters.Formatter;
import org.xapagy.xapi.reference.AbstractXapiReference;

/**
 * @author Ladislau Boloni
 * Created on: Jan 3, 2012
 */
public class PpAbstractXapiReference {

    /**
     * Normally, this will not be called directly, only from the more specific
     * variants
     * 
     * @param axr
     * @param agent
     * @return
     */
    public static String ppConcise(AbstractXapiReference axr, Agent agent) {
        Formatter fmt = new Formatter();
        fmt.is("Text", axr.getText());
        fmt.is("Type / Level", axr.getType() + " / " + axr.getXapiLevel());
        fmt.is("Position in parent", axr.getPositionInParent());
        return fmt.toString();
    }

    /**
     * Normally, this will not be called directly, only from the more specific
     * variants
     * 
     * @param axr
     * @param agent
     * @return
     */
    public static String ppDetailed(AbstractXapiReference axr, Agent agent) {
        Formatter fmt = new Formatter();
        fmt.add(PpAbstractXapiReference.ppConcise(axr, agent));
        fmt.add("ResolutionConfidence");
        fmt.addIndented(PrettyPrint.ppDetailed(axr.getResolutionConfidence(),
                agent));
        fmt.add("Parent");
        fmt.addIndented(PrettyPrint.ppDetailed(axr.getParent(), agent));
        return fmt.toString();
    }
}

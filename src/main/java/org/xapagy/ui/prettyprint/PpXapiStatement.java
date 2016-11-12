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
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.ui.formatters.Formatter;
import org.xapagy.xapi.reference.XrefStatement;

/**
 * @author Ladislau Boloni
 * Created on: Jun 7, 2011
 */
public class PpXapiStatement {
    public static String ppConcise(XrefStatement xapiStatement, Agent topLevel) {
        return PpXapiStatement.ppDetailed(xapiStatement, topLevel);
    }

    /**
     * Prints XapiStatement in a detailed form
     * 
     * @param is
     * @param topLevel
     * @return
     */
    public static String
            ppDetailed(XrefStatement xapiStatement, Agent topLevel) {
        Formatter fmt = new Formatter();
        String firstLine = "Statement " + xapiStatement.getViType();
        if (xapiStatement.getLabel() == null) {
            firstLine += " <<no label>>";
        } else {
            firstLine += " <" + xapiStatement.getLabel() + ">";
        }
        fmt.add(firstLine);
        fmt.indent();
        fmt.is("subject", xapiStatement.getSubject());
        fmt.is("verb", xapiStatement.getVerb());
        fmt.is("object", xapiStatement.getObject());
        if (xapiStatement.getViType().equals(ViType.QUOTE)) {
            fmt.add("quoted statement:");
            fmt.addIndented(PpXapiStatement.ppDetailed(
                    (XrefStatement) xapiStatement.getQuote(), topLevel));
        }
        return fmt.toString();
    }

}

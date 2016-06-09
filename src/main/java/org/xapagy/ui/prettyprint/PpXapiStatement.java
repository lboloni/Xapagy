/*
   This file is part of the Xapagy project
   Created on: Jun 7, 2011
 
   org.xapagy.ui.prettyprint.PpXapiStatement
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettyprint;

import org.xapagy.agents.Agent;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.xapi.reference.XrefStatement;

/**
 * @author Ladislau Boloni
 * 
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

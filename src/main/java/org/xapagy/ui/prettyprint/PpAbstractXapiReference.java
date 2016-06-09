/*
   This file is part of the Xapagy project
   Created on: Jan 3, 2012
 
   org.xapagy.ui.prettyprint.PpAbstractXapiReference
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettyprint;

import org.xapagy.agents.Agent;
import org.xapagy.xapi.reference.AbstractXapiReference;

/**
 * @author Ladislau Boloni
 * 
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

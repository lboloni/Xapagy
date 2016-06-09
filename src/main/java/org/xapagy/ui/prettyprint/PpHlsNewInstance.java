/*
   This file is part of the Xapagy project
   Created on: Mar 8, 2012
 
   org.xapagy.ui.prettyprint.PpHlsInstanceCreation
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettyprint;

import java.util.Collection;

import org.xapagy.agents.Agent;
import org.xapagy.headless_shadows.HlsNewInstance;

/**
 * @author Ladislau Boloni
 * 
 */
public class PpHlsNewInstance {

    public static String pp(HlsNewInstance hlsni, Agent agent,
            PrintDetail detailLevel) {
        XFormatter fmt = new XFormatter(agent);
        fmt.add("HlsNewInstance id = " + hlsni.getIdentifier());
        fmt.indent();
        fmt.addPpc("Scene", hlsni.getScene());
        fmt.addPpc("Attributes", hlsni.getAttributes());
        fmt.is("Resolved", hlsni.isResolved());
        if (hlsni.isResolved()) {
            fmt.addPpc("ResolvedInstance", hlsni.getResolvedInstance());
        }
        return fmt.toString();
    }

    /**
     * Pretty print in a concise way
     * 
     * @param focus
     * @param topLevel
     * @return
     */
    public static String ppConcise(HlsNewInstance hlsni, Agent agent) {
        return PpHlsNewInstance.pp(hlsni, agent, PrintDetail.DTL_CONCISE);
    }

    /**
     * Pretty print in a detailed way
     * 
     * @param focus
     * @param topLevel
     * @return
     */
    public static String ppDetailed(HlsNewInstance hlsni, Agent agent) {
        return PpHlsNewInstance.pp(hlsni, agent, PrintDetail.DTL_DETAIL);
    }

    /**
     * @param hlsNewInstances
     * @param agent
     * @param detailLevel
     * @return
     */
    public static String ppList(Collection<HlsNewInstance> hlsNewInstances,
            Agent agent, PrintDetail detailLevel) {
        XFormatter fmt = new XFormatter(agent);
        for (HlsNewInstance hlsni : hlsNewInstances) {
            fmt.addPp(hlsni, detailLevel);
        }
        return fmt.toString();
    }

}

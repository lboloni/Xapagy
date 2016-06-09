/*
   This file is part of the Xapagy project
   Created on: Feb 17, 2013
 
   org.xapagy.ui.prettyprint.PpRsScene
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettyprint;

import org.xapagy.agents.Agent;
import org.xapagy.debug.storygenerator.RsScene;
import org.xapagy.instances.Instance;
import org.xapagy.ui.smartprint.SpInstance;

/**
 * @author Ladislau Boloni
 * 
 */
public class PpRsScene {
    /**
     * Concise printing: fall back on the toString
     * 
     * @param rs
     * @param agent
     * @return
     */
    public static String ppConcise(RsScene rsc, Agent agent) {
        return rsc.toString();
    }

    /**
     * Detailed printout
     * 
     * @param rsc
     * @param agent
     * @return
     */
    public static String ppDetailed(RsScene rsc, Agent agent) {
        Formatter fmt = new Formatter();
        fmt.add("RsScene");
        fmt.indent();
        fmt.is("Name", rsc.getName());
        fmt.is("LabelScene", rsc.getLabelScene());
        fmt.is("Scene instance", SpInstance.spc(rsc.getSceneInstance(), agent));
        fmt.add("Instance labels");
        fmt.indent();
        for (String label : rsc.getInstanceLabels()) {
            fmt.add(label);
        }
        fmt.deindent();
        fmt.add("Instances");
        fmt.indent();
        for (Instance inst : rsc.getInstances()) {
            fmt.add(SpInstance.spc(inst, agent));
        }
        fmt.deindent();

        fmt.deindent();
        return fmt.toString();
    }
}

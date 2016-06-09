/*
   This file is part of the Xapagy project
   Created on: Apr 4, 2013
 
   org.xapagy.ui.prettyprint.PpRsTestingUnit
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettyprint;

import org.xapagy.agents.Agent;
import org.xapagy.debug.storygenerator.RecordedStory;
import org.xapagy.debug.storygenerator.RsScene;
import org.xapagy.debug.storygenerator.RsTestingUnit;
import org.xapagy.set.EnergyColors;

/**
 * @author Ladislau Boloni
 * 
 */
public class PpRsTestingUnit {
    /**
     * Fall back on detailed
     * 
     * @param rtu
     * @param agent
     * @return
     */
    public static String ppConcise(RsTestingUnit rtu, Agent agent) {
        return PpRsTestingUnit.ppDetailed(rtu, agent);
    }

    /**
     * Prints both the instance shadows and the other ones
     * 
     * @param sf
     * @param agent
     * @return
     */
    public static String ppDetailed(RsTestingUnit rtu, Agent agent) {
        Formatter fmt = new Formatter();
        fmt.add("RsTestingUnit");
        fmt.indent();
        // add the stories
        fmt.add("The stories:");
        fmt.indent();
        fmt.add("Focus story:");
        fmt.addIndented(rtu.getRsFocus());
        fmt.add("Shadow story:");
        fmt.addIndented(rtu.getRsShadow());
        fmt.add("History:");
        fmt.indent();
        int count = 0;
        for (RecordedStory rsh : rtu.getRsHistory()) {
            fmt.add("History Rs " + count++ + ":");
            fmt.addIndented(rsh);
        }
        fmt.deindent();
        fmt.deindent();
        // add the instance shadowing matrix
        fmt.add("Instance shadowing");
        // FIXME: iterate over the scenes of the focus instance
        for (RsScene rssF : rtu.getRsFocus().getRsScenes()) {
            for (RsScene rssS : rtu.getRsShadow().getRsScenes()) {
                fmt.indent();
                fmt.add("Shadowing between focus " + rssF.getName() + " and "
                        + rssS.getName());
                fmt.addIndented(PpRsTestingUnit.toStringInstanceShadowing(rtu,
                        rssF.getName(), rssS.getName()));
                fmt.deindent();
            }
        }

        // if the main instance has a quoted
        fmt.add("VI shadowing");
        fmt.addIndented(PpRsTestingUnit.toStringViShadowing(rtu));
        return fmt.toString();
    }

    /**
     * Prints the shadowing matrix between the instances of certain scenes
     * 
     * @param focusSceneId
     * @param shadowSceneId
     * @return
     */
    public static String toStringInstanceShadowing(RsTestingUnit rtu,
            String focusSceneId, String shadowSceneId) {
        Formatter fmt = new Formatter();
        RsScene focusScene = rtu.getRsFocus().getRsScene(focusSceneId);
        RsScene shadowScene = rtu.getRsShadow().getRsScene(shadowSceneId);
        for (int i = 0; i != focusScene.getInstances().size(); i++) {
            String temp = "";
            for (int j = 0; j != shadowScene.getInstances().size(); j++) {
                double value =
                        rtu.getInstanceShadowSalience(focusSceneId, i, shadowSceneId, j, EnergyColors.SHI_GENERIC);
                temp = temp + Formatter.padTo(Formatter.fmt(value), 8);
            }
            fmt.add(temp);
        }
        return fmt.toString();
    }

    /**
     * Prints the matrix of the VI shadowing between the focus story and the
     * shadow story
     * 
     */
    public static String toStringViShadowing(RsTestingUnit rtu) {
        Formatter fmt = new Formatter();
        for (int i = 0; i != rtu.getRsFocus().getRecordedVis().size(); i++) {
            // VerbInstance fvi = rtu.getRsFocus().getRecordedVis().get(i);
            String temp = "";
            for (int j = 0; j != rtu.getRsShadow().getRecordedVis().size(); j++) {
                // VerbInstance svi = rtu.getRsShadow().getRecordedVis().get(j);
                // double value = sf.getRelativeValue(fvi, svi);
                double value = rtu.getViShadowSalience(i, j, EnergyColors.SHV_GENERIC);
                temp = temp + Formatter.padTo(Formatter.fmt(value), 8);
            }
            fmt.add(temp);
        }
        return fmt.toString();
    }

}

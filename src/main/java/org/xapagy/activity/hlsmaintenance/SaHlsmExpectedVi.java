/*
   This file is part of the Xapagy project
   Created on: May 26, 2011
 
   org.xapagy.activity.SaExpectedViShadow
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.activity.hlsmaintenance;

import java.util.ArrayList;
import java.util.List;

import org.xapagy.activity.SpikeActivity;
import org.xapagy.agents.Agent;
import org.xapagy.debug.DebugEvent;
import org.xapagy.debug.DebugEvent.DebugEventType;
import org.xapagy.headless_shadows.Choice;
import org.xapagy.headless_shadows.Choice.ChoiceType;
import org.xapagy.headless_shadows.HeadlessComponents;
import org.xapagy.headless_shadows.Hls;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViSimilarityHelper;
import org.xapagy.set.ViSet;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.prettyprint.Formatter;
import org.xapagy.xapi.reference.XrefStatement;

/**
 * SA called when instantiating a VI, converts the predicting Hls into a shadow
 * and traces the expectedness
 * 
 * @author Ladislau Boloni
 * 
 */
public class SaHlsmExpectedVi extends SpikeActivity {

    private static final long serialVersionUID = -8070303294628004504L;
    private XrefStatement xapiStatement;

    public SaHlsmExpectedVi(Agent agent, String name) {
        super(agent, name);
    }

    /**
     * 
     */
    @Override
    public void applyInner(VerbInstance verbInstance) {
        // Resources resources = agent.getResources();
        HeadlessComponents hlss = agent.getHeadlessComponents();
        List<Choice> compatibles = new ArrayList<>();
        // picks the first which is compatible
        for (Choice choice : hlss
                .getChoices(HeadlessComponents.comparatorDependentScore)) {
            if (!choice.getChoiceType().equals(ChoiceType.CONTINUATION)) {
                continue;
            }
            Hls hls = choice.getHls();
            if (ViSimilarityHelper.decideSimilarityVi(hls.getViTemplate(),
                    verbInstance, agent, false)) {
                compatibles.add(choice);
            }
        }
        // no prediction
        if (compatibles.isEmpty()) {
            // if no HLS predicted it, then create a shadow anyhow
            agent.notifyObservers(new DebugEvent(
                    DebugEventType.RESOLVE_SURPRISE, null, -1, verbInstance,
                    null));
            return;
        }
        // single prediction
        if (compatibles.size() == 1) {
            Choice choice = compatibles.get(0);
            Hls hls = choice.getHls();
            agent.notifyObservers(new DebugEvent(
                    DebugEventType.RESOLVE_SURPRISE, null, -1, verbInstance,
                    hls));
            // ViSet vis = ChoiceScore.getVirtualShadow(agent,
            // ChoiceType.CONTINUATION, hls);
            ViSet vis = choice.getChoiceScore().getVirtualShadow();
            agent.getShadows().mergeInShadow(verbInstance, vis, 1.0,
                    "SaHlsmExpectedVi");
            // expectedness is the weight of the new shadow, here
            double expectedness = vis.getSum();
            verbInstance.setExpectedness(expectedness);
            return;
        }
        // more than one prediction
        TextUi.errorPrint("More than one compatible prediction in SaExpectedViShadow, none of them chosen!!!");
        // System.exit(1);
        return;
    }

    @Override
    public String toString() {
        Formatter fmt = new Formatter();
        fmt.add(super.toString());
        fmt.indent();
        fmt.addIndented(xapiStatement);
        return fmt.toString();
    }

    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IxwFormattable#formatTo(org.xapagy.ui.formatters.IXwFormatter, int)
     */
    @Override
    public void formatTo(IXwFormatter fmt, int detailLevel) {
        fmt.add("SaHlsmExpectedVi");
    }

    /* (non-Javadoc)
     * @see org.xapagy.activity.Activity#extractParameters()
     */
    public void extractParameters() {
    }

}

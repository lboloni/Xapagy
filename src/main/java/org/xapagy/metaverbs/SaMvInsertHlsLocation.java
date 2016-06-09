/*
   This file is part of the Xapagy project
   Created on: Aug 24, 2012
 
   org.xapagy.metaverbs.SaMvInsertHlsLocation
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.metaverbs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.xapagy.agents.Agent;
import org.xapagy.agents.Focus;
import org.xapagy.concepts.Hardwired;
import org.xapagy.headless_shadows.FslInterpretation;
import org.xapagy.headless_shadows.FslType;
import org.xapagy.headless_shadows.Hls;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.parameters.Parameters;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.EnergyQuantum;
import org.xapagy.set.ViSet;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * This is a specialized SA which replaces the regular action successor metaverb (it is 
 * passed into Execute.executeVerbInstance as a successorVerbReplacement. The goal of this
 * SA is to insert the "verbInstance" to the position which it would occupy according to the
 * way it is described in the HLS. 
 * 
 * As far as I can tell, this is mostly important for MISSING_ACTION type internal instantiations.
 * 
 * @author Ladislau Boloni
 * 
 */
public class SaMvInsertHlsLocation extends AbstractSaMetaVerb {

    private static final long serialVersionUID = -1206282337726941704L;

    private Hls hls;

    /**
     * @param agent
     * @param viType
     */
    public SaMvInsertHlsLocation(Agent agent, ViType viType, Hls hls) {
        super(agent, "SaMvInsertHlsLocation", viType);
        this.hls = hls;
    }

    /**
     *  It receives the verbInstance the freshly instantiated VI from the choice. 
     *  The HLS is the HLS which contributed to the choice.
     *  
     *  It will create the SUCCESSOR and PREDECESSOR links, then it will perform a multiplicative
     *  decrease on the EnergyColor.FOCUS (only!!!) to put it in the position where its peers are
     *  going to be. 
     *  
     */
    @Override
    public void applyInner(VerbInstance verbInstance) {
        // TextUi.println("Executing SaMvInsertHlsLocation");
        Focus fc = agent.getFocus();
        Parameters p = agent.getParameters();
        // create a map off all the types
        Map<FslType, ViSet> map = new HashMap<>();
        for (FslType fslType : FslType.values()) {
            map.put(fslType, new ViSet());
        }
        for (FslInterpretation fsli : hls.getSupports()) {
            VerbInstance source = fsli.getFsl().getViFocus();
            double value = fsli.getTotalSupport(agent);
            ViSet vis = map.get(fsli.getFsl().getFslType());
            vis.change(source, value);
        }
        // for the time being, only deal with successor and predecessor
        ViSet visSuccessor = map.get(FslType.SUCCESSOR);
        ViSet visPredecessor = map.get(FslType.PREDECESSOR);
        // subtract them from each other
        ViSet visS2 = new ViSet();
        ViSet visP2 = new ViSet();
        Set<VerbInstance> set = new HashSet<>();
        set.addAll(visSuccessor.getParticipants());
        set.addAll(visPredecessor.getParticipants());
        for (VerbInstance vif : set) {
            double valueS = visSuccessor.value(vif);
            double valueP = visPredecessor.value(vif);
            if (valueS - valueP > 0) {
                visS2.changeTo(vif, valueS - valueP);
            }
            if (valueP - valueS > 0) {
                visP2.changeTo(vif, valueP - valueS);
            }
        }
        // normalize them
        double w = visS2.getSum() + visP2.getSum();
        // if there is nothing, exit but print
        if (w == 0.0) {
            TextUi.println("insertSuccessionChain: w=0");
            return;
        }
        double scale = 1 / w;
        for (VerbInstance vif : visS2.getParticipants()) {
            double val = scale * visS2.value(vif);
            visS2.changeTo(vif, val);
        }
        for (VerbInstance vif : visP2.getParticipants()) {
            double val = scale * visP2.value(vif);
            visP2.changeTo(vif, val);
        }
        // set the successor links
        for (VerbInstance vif : visS2.getParticipants()) {
            double val = visS2.value(vif);
            agent.getLinks().changeLinkByName(Hardwired.LINK_SUCCESSOR, vif, verbInstance, val, "SaMvInsertHlsLocation/Successor"
            + "+changeSuccessor");
        }
        // set the predecessor links
        for (VerbInstance vif : visP2.getParticipants()) {
            double val = visP2.value(vif);
            agent.getLinks().changeLinkByName(Hardwired.LINK_PREDECESSOR, vif, verbInstance, val, "SaMvInsertHlsLocation/Predecessor"
            + "+changePredecessor");
        }
        // now, the pushout is the sum of the predecessor multiplied with the pushout parameter
        // note that this will be a negative value...
        double realPushout =
                visP2.getSum()
                        * p.get("A_GENERAL",
                                "G_GENERAL",
                                "N_SA_MV_INSERT_HLS_LOCATION_ASSSUMED_PUSHOUT");
        double currentValue = fc.getEnergy(verbInstance, EnergyColors.FOCUS_VI);
        double multiplicativeDecrease = (currentValue - realPushout) / currentValue;
        // TextUi.println("realPushOut:" + realPushout);
        // TextUi.println("viS2:" + visS2.getSum());
        //fc.addEnergy(verbInstance, Focus.TIMESLICE_ONE, realPushout, EnergyColor.FOCUS);
        EnergyQuantum<VerbInstance> eq = EnergyQuantum.createMult(verbInstance, multiplicativeDecrease, EnergyColors.FOCUS_VI, "SaMvInsertHlsLocation");
        fc.applyViEnergyQuantum(eq);
    }

    
    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IxwFormattable#formatTo(org.xapagy.ui.formatters.IXwFormatter, int)
     */
    @Override
    public void formatTo(IXwFormatter fmt, int detailLevel) {
        fmt.add("SaMvInsertHlsLocation");
    }

    /* (non-Javadoc)
     * @see org.xapagy.activity.Activity#extractParameters()
     */
    public void extractParameters() {
    }
}

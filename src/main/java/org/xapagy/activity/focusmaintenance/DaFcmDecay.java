/*
   This file is part of the Xapagy project
   Created on: Apr 24, 2011
 
   org.xapagy.story.activity.DADecayFocus
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.activity.focusmaintenance;

import org.xapagy.activity.AbstractDaFocusIterator;
import org.xapagy.agents.Agent;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViClassifier;
import org.xapagy.instances.ViClassifier.ViClass;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.EnergyQuantum;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * The gradual decay of all the components in the focus
 * 
 * Parameters:
 * 
 * <ul>
 * <li> decayInstance
 * <li> decayVi
 * </ul>
 * 
 * @author Ladislau Boloni
 * 
 */
public class DaFcmDecay extends AbstractDaFocusIterator {
    
    private static final long serialVersionUID = 847927033187310966L;
    /** 
     * Multiplicative decay of the EnergyColor.FOCUS for instances and scenes which lost all their instances.
     */
    private double decayInstance;
    /**
     * Multiplicative decay for action VIs
     */
    private double decayActionVi;
    /** 
     * Multiplicative decay for non-action VIs (like is-a, change-scene etc)
     */
    private double decayNonActionVi;
    
    /**
     * @param name
     * @param agent
     * @throws Exception 
     */
    public DaFcmDecay(Agent agent, String name) {
        super(agent, name);
    }
    
    @Override
    public void extractParameters() {
        decayInstance = getParameterDouble("decayInstance");
        decayActionVi = getParameterDouble("decayActionVi");
        decayNonActionVi = getParameterDouble("decayNonActionVi");
    }
    
    /**
     * Decay of the focus energy
     * 
     * @param fi
     * @param time
     */
    @Override
    protected void applyFocusNonSceneInstance(Instance fi, double time) {
        EnergyQuantum<Instance> eq =
                EnergyQuantum.createMult(fi, decayInstance, time,
                        EnergyColors.FOCUS_INSTANCE, "DaFcmDecay");
        fc.applyInstanceEnergyQuantum(eq);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.xapagy.activity.AbstractDaFocusIterator#applyFocusScene(com.xapagy
     * .instances.Instance, double)
     */
    @Override
    protected void applyFocusScene(Instance fi, double time) {
        // the current scene does not decay
        if (agent.getFocus().getCurrentScene().equals(fi)) {
            return;
        }
        //
        // if the scene has any members of the focus, then it does not decay
        //
        double sum = 0;
        for (Instance member : fi.getSceneMembers()) {
            sum = sum + fc.getEnergy(member, EnergyColors.FOCUS_INSTANCE);
        }
        if (sum > 0) {
            return;
        }
        //
        // scenes who lost all their members, decay gradually
        // FIXME: what is the purpose of this???
        //
        EnergyQuantum<Instance> eq =
                EnergyQuantum.createMult(fi, decayInstance, time,
                        EnergyColors.FOCUS_INSTANCE, "DaFcmDecay");
        fc.applyInstanceEnergyQuantum(eq);
    }

    /**
     * @param fvi
     * @param time
     */
    @Override
    protected void applyFocusVi(VerbInstance fvi, double time) {
        // double currentValue = fc.getSalience(fvi, EnergyColor.FOCUS);
        if (ViClassifier.decideViClass(ViClass.ACTION, fvi, agent)) {
            //double decayActionVi =
            //        p.get("A_FCM", "G_DECAY",
            //                "N_ACTION_VI");
            EnergyQuantum<VerbInstance> eq =
                    EnergyQuantum.createMult(fvi, decayActionVi, time,
                            EnergyColors.FOCUS_VI, "DaFcmDecay");
            fc.applyViEnergyQuantum(eq);
            return;
        }
        // TEST : what if we leave the sticky's alone?
        if (ViClassifier.decideViClass(ViClass.STICKY, fvi, agent)) {
            // decays only when the member instances decay
            /*
             * double memberValue = 1.0; for (ViPart vip :
             * ViStructureHelper.getAllowedInstanceParts(fvi .getViType())) {
             * Instance instPart = (Instance) fvi.getPart(vip); memberValue =
             * Math.min(memberValue, fc.getSalience(instPart)); } if
             * (memberValue < currentValue) { fc.addToSalience(fvi, memberValue
             * - currentValue); } handleMinimumVi(fvi);
             */
            return;
        }
        // not an action verb, nor a relation verb, nor a summary
        //double decayNonActionVi =
        //        p.get("A_FCM", "G_DECAY",
        //                "N_NON_ACTION_VI");
         EnergyQuantum<VerbInstance> eq =
                EnergyQuantum.createMult(fvi, decayNonActionVi, time,
                        EnergyColors.FOCUS_VI, "DaFcmDecay");
        fc.applyViEnergyQuantum(eq);

    }

    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IxwFormattable#formatTo(org.xapagy.ui.formatters.IXwFormatter, int)
     */
    @Override
    public void formatTo(IXwFormatter fmt, int detailLevel) {
        fmt.add("DaFcmDecay");
        fmt.indent();
        fmt.is("decayInstance", decayInstance);
        fmt.is("decayActionVi", decayActionVi);
        fmt.is("decayNonActionVi", decayNonActionVi);
        fmt.add("description");
        fmt.indent();
        fmt.add(getDescription());
        fmt.deindent();
        fmt.deindent();
    }

}

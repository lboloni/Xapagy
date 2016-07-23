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
import org.xapagy.set.EnergyQuantum;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * The gradual decay of the energy of all the components in the focus
 * 
 * Parameters:
 * 
 * <ul>
 * <li> decayInstance
 * <li> decayActionVi
 * <li> decayNonActionVi
 * </ul>
 * 
 * @author Ladislau Boloni
 * 
 */
public class DaFcmDecay extends AbstractDaFocusIterator {
    
    private static final long serialVersionUID = 847927033187310966L;
    /** 
     * Multiplicative decay for the instances
     */
    private double decayInstance;
    /** 
     * Multiplicative decay for the scenes (only apply for the ones that had lost all their instances)
     */
    private double decayScene /* = 0 */;
    /**
     * Multiplicative decay for action VIs
     */
    private double decayActionVi;
    /** 
     * Multiplicative decay for non-action VIs (like is-a, change-scene etc)
     */
    private double decayNonActionVi;
    /**
     * The focus instance energy we are considering
     */
    private String ecInstance /* = EnergyColors.FOCUS_INSTANCE */;
    /**
     * The focus VI energy we are considering
     */
    private String ecVI /* = EnergyColors.FOCUS_VI */;
    
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
        decayScene = getParameterDouble("decayScene");
        decayActionVi = getParameterDouble("decayActionVi");
        decayNonActionVi = getParameterDouble("decayNonActionVi");
        ecInstance = getParameterString("ecInstance");
        ecVI = getParameterString("ecVI");
    }
    
    /**
     * Decay of the energy for a non-scene instance
     * 
     * @param fi
     * @param time
     */
    @Override
    protected void applyFocusNonSceneInstance(Instance fi, double time) {
        EnergyQuantum<Instance> eq =
                EnergyQuantum.createMult(fi, decayInstance, time,
                        ecInstance, "DaFcmDecay");
        fc.applyInstanceEnergyQuantum(eq);
    }

    /**
     * Decay of the energy of the scene. If any of the members has this particular type of
     * energy, the given energy of the scene does not decay. When they are going, it starts 
     * to decay with a specific parameter
     * 
     * @param fi
     * @param time
     */
    @Override
    protected void applyFocusScene(Instance fi, double time) {
        // the current scene does not decay
        //if (agent.getFocus().getCurrentScene().equals(fi)) {
        //    return;
        //}
        //
        // if the scene has any members in the focus, then it does not decay
        //
        double sum = 0;
        for (Instance member : fi.getSceneMembers()) {
            sum = sum + fc.getEnergy(member, ecInstance);
        }
        if (sum > 0) {
            return;
        }
        //
        // scenes who lost all their members, decay 
        // FIXME: what is the purpose of this???
        //
        EnergyQuantum<Instance> eq =
                EnergyQuantum.createMult(fi, decayScene, time,
                        ecInstance, "DaFcmDecay");
        // JUST TRYING, WHY iS THIS FAILING???
        fc.applyInstanceEnergyQuantum(eq);
    }

    /**
     * @param fvi
     * @param time
     */
    @Override
    protected void applyFocusVi(VerbInstance fvi, double time) {
        if (ViClassifier.decideViClass(ViClass.ACTION, fvi, agent)) {
            EnergyQuantum<VerbInstance> eq =
                    EnergyQuantum.createMult(fvi, decayActionVi, time,
                            ecVI, "DaFcmDecay");
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
         EnergyQuantum<VerbInstance> eq =
                EnergyQuantum.createMult(fvi, decayNonActionVi, time,
                        ecVI, "DaFcmDecay");
        fc.applyViEnergyQuantum(eq);

    }

    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IxwFormattable#formatTo(org.xapagy.ui.formatters.IXwFormatter, int)
     */
    @Override
    public void formatTo(IXwFormatter fmt, int detailLevel) {
        fmt.add("DaFcmDecay");
        fmt.indent();
        fmt.is("ecInstance", ecInstance);
        fmt.is("decayInstance", decayInstance);
        fmt.is("decayScene", decayScene);
        fmt.is("ecVI", ecVI);
        fmt.is("decayActionVi", decayActionVi);
        fmt.is("decayNonActionVi", decayNonActionVi);
        fmt.add("description");
        fmt.indent();
        fmt.add(getDescription());
        fmt.deindent();
        fmt.deindent();
    }

}

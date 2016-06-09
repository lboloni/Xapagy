/*

   This file is part of the Xapagy project
   Created on: Apr 24, 2011
 
   org.xapagy.story.activity.DAEpisodicMemoryIncrease
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.activity.focusmaintenance;

import org.xapagy.activity.AbstractDaFocusIterator;
import org.xapagy.agents.Agent;
import org.xapagy.agents.AutobiographicalMemory;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViClassifier;
import org.xapagy.instances.ViStructureHelper;
import org.xapagy.instances.ViClassifier.ViClass;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.EnergyQuantum;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * Implements the memorization of the instances and VIs by transforming the
 * salience of Ec.Focus to the energy of Ec.AM
 * 
 * @author Ladislau Boloni
 * 
 */
public class DaFcmMemorization extends AbstractDaFocusIterator {
    private static final long serialVersionUID = -7421294458868302176L;

    /**
     * a multiplier for the transformation of the salience of EnergyColor.FOCUS
     * into the energy of EnergyColor.AM for an instance
     */
    private double s2eInstance;
    /**
     * a multiplier for the transformation of the salience of EnergyColor.FOCUS
     * into the energy of EnergyColor.AM for a VI
     */
    private double s2eVi;
    /**
     * a multiplier for the transformation of the salience of an action VI
     * EnergyColor.FOCUS into the energy of EnergyColor.AM for an instance
     */
    private double s2eViInstance;

    /**
     * @param name
     * @param agent
     * @throws Exception
     */
    public DaFcmMemorization(Agent agent, String name) {
        super(agent, name);
    }

    @Override
    public void extractParameters() {
        s2eInstance = getParameterDouble("I_S_Focus_to_I_E_AM");
        s2eVi = getParameterDouble("VI_S_Focus_to_VI_E_AM");
        s2eViInstance = getParameterDouble("VI_S_Focus_to_I_E_AM");
    }

    /**
     * Transforms the salience of EnergyColor.FOCUS into the energy of
     * EnergyColor.AM for an instance in the focus.
     * 
     * FIXME: the problem with this approach is that the FOCUS energy is not
     * consumed, so if a scene stays too long in the focus, the AM energy will
     * keep increasing.
     * 
     * @param f
     * @param time
     */
    @Override
    protected void applyFocusNonSceneInstance(Instance f, double time) {
        AutobiographicalMemory am = agent.getAutobiographicalMemory();
        double salienceFOCUS = fc.getSalience(f, EnergyColors.FOCUS_INSTANCE);
        double energyAM = salienceFOCUS * s2eInstance;
        EnergyQuantum<Instance> eq = EnergyQuantum.createAdd(f, energyAM, time,
                EnergyColors.AM_INSTANCE, "DaFcmMemorization");
        am.applyInstanceEnergyQuantum(eq);
    }

    /**
     * 
     * Transforms the salience of EnergyColor.FOCUS into the energy of
     * EnergyColor.AM for a scene in the focus.
     * 
     * FIXME: it is not clear what the AM energy of the scene is supposed to
     * represent.
     * 
     * @param f
     * @param time
     */
    @Override
    protected void applyFocusScene(Instance f, double time) {
        AutobiographicalMemory am = agent.getAutobiographicalMemory();
        double salienceFOCUS = fc.getSalience(f, EnergyColors.FOCUS_INSTANCE);
        double energyAM = salienceFOCUS * s2eInstance;
        EnergyQuantum<Instance> eq = EnergyQuantum.createAdd(f, energyAM, time,
                EnergyColors.AM_INSTANCE, "DaFcmMemorization");
        am.applyInstanceEnergyQuantum(eq);
    }

    /**
     * Transforms the salience of EnergyColor.FOCUS into the energy of
     * EnergyColor.AM for a VI in the focus.
     * 
     * In addition, for actions, adds AM energy to the instances of the VI,
     * proportional to the FOCUS salience of the VI.
     * 
     * FIXME: this will probably become the main way
     * 
     * @param fvi
     * @param time
     */
    @Override
    protected void applyFocusVi(VerbInstance fvi, double time) {
        AutobiographicalMemory am = agent.getAutobiographicalMemory();
        double salienceFOCUS = fc.getSalience(fvi, EnergyColors.FOCUS_VI);
        double energyAM = salienceFOCUS * s2eVi;
        EnergyQuantum<VerbInstance> eq = EnergyQuantum.createAdd(fvi, energyAM,
                time, EnergyColors.AM_VI, "DaFcmMemorization");
        am.applyViEnergyQuantum(eq);
        //
        // adding AM energy to the instances, proportional to the VI FOCUS
        // salience
        //
        if (ViClassifier.decideViClass(ViClass.ACTION, fvi, agent)) {
            for (ViPart part : ViStructureHelper
                    .getAllowedInstanceParts(fvi.getViType())) {
                Instance instance = (Instance) fvi.getPart(part);
                double energyAMInstance = salienceFOCUS * s2eViInstance;
                EnergyQuantum<Instance> eqi =
                        EnergyQuantum.createAdd(instance, energyAMInstance,
                                time, EnergyColors.AM_INSTANCE, "DaFcmMemorization");
                am.applyInstanceEnergyQuantum(eqi);
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.xapagy.ui.formatters.IxwFormattable#formatTo(org.xapagy.ui.formatters
     * .IXwFormatter, int)
     */
    @Override
    public void formatTo(IXwFormatter fmt, int detailLevel) {
        fmt.add("DaFcmMemorization");
        fmt.indent();
        fmt.is("s2eInstance", s2eInstance);
        fmt.explanatoryNote(
                "a multiplier for the transformation of the salience of EnergyColor.FOCUS "
                        + "into the energy of EnergyColor.AM for an instance");
        fmt.is("s2eVi", s2eVi);
        fmt.explanatoryNote("a multiplier for the transformation of the salience of " +
         "EnergyColor.FOCUS into the energy of EnergyColor.AM for a VI");
        fmt.is("s2eViInstance", s2eViInstance);
        fmt.explanatoryNote("a multiplier for the transformation of the salience of " + 
          "an action VI EnergyColor.FOCUS into the energy of EnergyColor.AM for an instance");
        fmt.deindent();
    }

}

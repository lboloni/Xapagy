/*
   This file is part of the Xapagy project
   Created on: Jun 4, 2011
 
   org.xapagy.activity.SaFocusInsertVi
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.activity.focusmaintenance;

import org.xapagy.activity.SpikeActivity;
import org.xapagy.agents.Agent;
import org.xapagy.agents.Focus;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.EnergyQuantum;
import org.xapagy.summarization.SummarizationClassifier;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * 
 * The spike activity which is triggered when a verb instance is inserted into
 * the focus
 * 
 * @author Ladislau Boloni
 * 
 */
public class SaFcmInsertVi extends SpikeActivity {

    private static final long serialVersionUID = -6199646656543411299L;

    /**
     * 
     * @param agent
     * @param name
     * @param vi
     */
    public SaFcmInsertVi(Agent agent, String name) {
        super(agent, name);
    }

    /**
     *  Performs the insertion of the VI into the focus. Normal VIs are inserted
     *  using FOCUS energy, but the other ones marked as summarization VIs are inserted 
     *  with summarization energy. 
     */
    @Override
    public void applyInner(VerbInstance vi) {
        Focus fc = agent.getFocus();
        //
        //  Handle the case of the VIs marked as summarizations by label 
        //
        if (SummarizationClassifier.isExplicitSummarizationVi(vi)) {
            EnergyQuantum<VerbInstance> eq =
                    EnergyQuantum.createAdd(vi, Focus.INITIAL_ENERGY_VI,
                            EnergyColors.FOCUS_SUMMARIZATION_VI, "SaFcmInsertVi + VI");
            fc.applyViEnergyQuantum(eq);
            return;
        }
        //
        //  Ok, this is regular VI, we add focus energy
        //        
        EnergyQuantum<VerbInstance> eq =
                EnergyQuantum.createAdd(vi, Focus.INITIAL_ENERGY_VI,
                        EnergyColors.FOCUS_VI, "SaFcmInsertVi + VI");
        fc.applyViEnergyQuantum(eq);
        for (ViPart part : ViStructureHelper.getAllowedInstanceParts(vi
                .getViType())) {
            Instance instance = (Instance) vi.getPart(part);
            EnergyQuantum<Instance> eq1 =
                    EnergyQuantum.createAdd(instance,
                            Focus.INITIAL_ENERGY_INSTANCE, EnergyColors.FOCUS_INSTANCE,
                            "SaFcmInsertVi + InstStrength");
            fc.applyInstanceEnergyQuantum(eq1);

            EnergyQuantum<Instance> eq2 =
                    EnergyQuantum.createAdd(instance.getScene(),
                            Focus.INITIAL_ENERGY_INSTANCE, EnergyColors.FOCUS_INSTANCE,
                            "SaFcmInsertVi + SceneStrength");
            fc.applyInstanceEnergyQuantum(eq2);
        }
    }


    
    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IxwFormattable#formatTo(org.xapagy.ui.formatters.IXwFormatter, int)
     */
    @Override
    public void formatTo(IXwFormatter fmt, int detailLevel) {
        fmt.add("SaFcmInsertVi");
    }

    /* (non-Javadoc)
     * @see org.xapagy.activity.Activity#extractParameters()
     */
    public void extractParameters() {
    }
}

/*
   This file is part of the Xapagy project
   Created on: May 20, 2011
 
   org.xapagy.domain.metaverb.SACreateInstance
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.metaverbs;

import org.xapagy.agents.Agent;
import org.xapagy.agents.Focus;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.instances.Instance;
import org.xapagy.instances.InstanceClassifier;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.EnergyQuantum;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * Meta-verb spike for the creation of a new instance
 * 
 * The SACreateInstance spike is used in sentences of the form: Scene /
 * create-instance / properties.
 * 
 * It creates a new instance, immediately assigns to it the new properties and
 * then inserts it to the specified scene.
 * 
 * @author Ladislau Boloni
 * 
 */
public class SaMvCreateInstance extends AbstractSaMetaVerb {
    private static final long serialVersionUID = 4933907644302352703L;

    /**
     * Creates the SACreateInstance verb - this is used only in the model.
     * 
     * @param type
     * @param name
     */
    public SaMvCreateInstance(Agent agent) {
        super(agent, "SaMvCreateInstance", ViType.S_ADJ);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xapagy.story.activity.SpikeActivity#apply()
     */
    @Override
    public void applyInner(VerbInstance verbInstance) {
        Focus fc = agent.getFocus();
        ConceptOverlay co = verbInstance.getAdjective();
        // do we create an instance or a scene?
        Instance instance;
        if (InstanceClassifier.decideSceneCo(co, agent)) {
            instance = agent.createInstance(null);
        } else {
            instance = agent.createInstance(verbInstance.getSubject());
        }
        instance.getConcepts().addOverlay(co, 1.0);
        // add the labels
        for (String label : co.getLabels()) {
            instance.getConcepts().addFullLabel(label, agent);
        }
        verbInstance.setCreatedInstance(instance);
        EnergyQuantum<Instance> eq = EnergyQuantum.createAdd(instance, Focus.INITIAL_ENERGY_INSTANCE, EnergyColors.FOCUS_INSTANCE, "SaMvCreateInstance");
        fc.applyInstanceEnergyQuantum(eq);
    }
    
    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IxwFormattable#formatTo(org.xapagy.ui.formatters.IXwFormatter, int)
     */
    @Override
    public void formatTo(IXwFormatter fmt, int detailLevel) {
        fmt.add("SaMvCreateInstance");
    }

    /* (non-Javadoc)
     * @see org.xapagy.activity.Activity#extractParameters()
     */
    public void extractParameters() {
    }
}

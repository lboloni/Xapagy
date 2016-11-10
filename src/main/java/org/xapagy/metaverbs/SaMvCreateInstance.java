/*
   
    This file is part of the Xapagy Cognitive Architecture 
    Copyright (C) 2008-2017 Ladislau Boloni

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
   
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
 * Created on: May 20, 2011
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

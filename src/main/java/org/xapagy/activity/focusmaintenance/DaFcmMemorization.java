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
package org.xapagy.activity.focusmaintenance;

import org.xapagy.activity.AbstractDaFocusIterator;
import org.xapagy.agents.Agent;
import org.xapagy.agents.AutobiographicalMemory;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViClassifier;
import org.xapagy.instances.ViClassifier.ViClass;
import org.xapagy.instances.ViStructureHelper;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.set.EnergyQuantum;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * Implements the memorization of the instances and VIs
 * 
 * 
 * @author Ladislau Boloni
 * Created on: Apr 24, 2011
 */
public class DaFcmMemorization extends AbstractDaFocusIterator {
    private static final long serialVersionUID = -7421294458868302176L;

    /**
     * The focus VI energy whose salience is the source of memorization
     */
    private String ecFocusVI;
    /**
     * The AM instance energy we are adding to
     */
    private String ecAMInstance;
    /**
     * The AM VI energy we are adding to 
     */
    private String ecAMVI;
    
    /**
     * a multiplier for the transformation of the salience of ecFocusVi
     * into the energy of ecAMVI for a VI
     */
    private double s2eVi;
    /**
     * a multiplier for the transformation of the salience of an action VI
     * ecFocusVI into the energy of an ecAMInstance for an instance that is part of an action
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
    	ecFocusVI = getParameterString("ecFocusVI");
    	ecAMInstance = getParameterString("ecAMInstance");
    	ecAMVI = getParameterString("ecAMVI");
        s2eVi = getParameterDouble("VI_S_Focus_to_VI_E_AM");
        s2eViInstance = getParameterDouble("VI_S_Focus_to_I_E_AM");
    }


    /**
     * Current behavior:
     * 
     * For all VIs, it transforms ecFocusVi into ecAMVI. 
     * 
     * In addition, for 
     * 
     * @param fvi
     * @param time
     */
    @Override
    protected void applyFocusVi(VerbInstance fvi, double time) {
        AutobiographicalMemory am = agent.getAutobiographicalMemory();
        double salienceFOCUS = fc.getSalience(fvi, ecFocusVI);
        double energyAM = salienceFOCUS * s2eVi;
        EnergyQuantum<VerbInstance> eq = EnergyQuantum.createAdd(fvi, energyAM,
                time, ecAMVI, "DaFcmMemorization");
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
                                time, ecAMInstance, "DaFcmMemorization");
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
        fmt.is("ecFocusVI", ecFocusVI);
        fmt.explanatoryNote("The focus VI energy whose salience is the source of the memorization");
        fmt.is("ecAMInstance", ecAMInstance);
        fmt.explanatoryNote("The AM instance energy we are adding to");
        fmt.is("ecAMVI", ecAMVI);
        fmt.explanatoryNote("The AM VI energy we are adding to");
        fmt.is("s2eVi", s2eVi);
        fmt.explanatoryNote("a multiplier for the transformation of the salience of " +
         "ecFocusVI into ecAMVI for a VI");
        fmt.is("s2eViInstance", s2eViInstance);
        fmt.explanatoryNote("a multiplier for the transformation of the salience of " + 
          "ecFocusVI into the ecAmInstance for an instance");
        fmt.deindent();
    }

}

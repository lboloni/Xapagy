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
package org.xapagy.activity.shadowmaintenance;

import org.xapagy.activity.AbstractDaFocusIterator;
import org.xapagy.agents.Agent;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViClassifier;
import org.xapagy.instances.ViClassifier.ViClass;
import org.xapagy.instances.ViStructureHelper;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.InstanceSet;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * 
 * This DA adds SHI_ATTRIBUTE energy based on the matching of returned by AmLookup.lookupCo.
 * 
 * There are two occasions of energies added: every iteration for every instance, and to all
 * the instances participating in an action VI (which fades out with the action VI)
 * 
 * @author Ladislau Boloni
 * Created on: Apr 23, 2011
 */
public class DaShmInstanceMatchingHead extends AbstractDaFocusIterator {

    private static final long serialVersionUID = 1705064759740445621L;

    /**
     * Multiplies the amount of energy SHI_ATTRIBUTE energy added to all
     * in-focus instances based on attribute matching
     */
    private double scaleInstanceByAttribute;
    /**
     * Multiplies the amount of energy SHI_ATTRIBUTE energy added to all
     * in-focus instances based on VI matching
     */
    private double scaleInstanceInAction;

    /*
     * (non-Javadoc)
     * 
     * @see org.xapagy.activity.DiffusionActivity#extractParameters()
     */
    @Override
    public void extractParameters() {
        scaleInstanceByAttribute =
                getParameterDouble("scaleInstanceByAttribute");
        scaleInstanceInAction = getParameterDouble("scaleInstanceInAction");
    }

    /**
     * 
     * @param agent
     * @param name
     */
    public DaShmInstanceMatchingHead(Agent agent, String name) {
        super(agent, name);
    }

    /**
     * Performs an instance reinforcement for all the instances which are part
     * of the focus, in the specific instance energy
     * 
     * @param fi
     * @param timeSlice
     */
    @Override
    protected void applyFocusNonSceneInstance(Instance fi, double timeSlice) {
        addShadowsBasedOnMatch(fi, timeSlice, scaleInstanceByAttribute,
                EnergyColors.SHI_ATTRIBUTE);
    }

    /**
     * Experiment: perform instance reinforcement only for the active action
     * instances
     * 
     * This would prevent the shadows accumulating based on purely structural
     * features for quiescent instances.
     * 
     */
    @Override
    protected void applyFocusVi(VerbInstance fvi, double timeSlice) {
        if (!ViClassifier.decideViClass(ViClass.ACTION, fvi, agent)) {
            return;
        }
        for (ViPart part : ViStructureHelper
                .getAllowedInstanceParts(fvi.getViType())) {
            Instance instance = (Instance) fvi.getPart(part);
            addShadowsBasedOnMatch(instance, timeSlice, scaleInstanceInAction,
                    EnergyColors.SHI_ATTRIBUTE);
        }
    }

    /**
     * This function adds shadows (by adding a certain shadow energy) to a focus
     * instance based on the match between the attributes of the focus instance
     * and the shadow instance.
     * 
     * @param fi
     *            - the focus instance
     * @param timeSlice
     * @param scale
     *            - parameter scaling the shadow energy added
     * @param ec
     *            - the energy color of the energy added
     */
    private void addShadowsBasedOnMatch(Instance fi, double timeSlice,
            double scale, String ec) {
        ShmAddItemCollection saic = new ShmAddItemCollection();
        InstanceSet matches =
                AmLookup.lookupCo(agent.getAutobiographicalMemory(),
                        fi.getConcepts(), EnergyColors.AM_INSTANCE);
        for (Instance match : matches.getParticipants()) {
            // a guard to prevent scenes shadowing non scenes
            if (match.isScene()) {
                continue;
            }
            double matchLevel = matches.value(match);
            double addLevel = matchLevel * scale;
            ShmAddItem sai = new ShmAddItem(agent, match, fi, addLevel, ec);
            saic.addShmAddItem(sai);
        }
        SaicHelper.applySAIC_Instances(agent, saic, timeSlice,
                "DaShmInstanceMatchingHead");
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
        fmt.add("DaShmInstanceMatchingHead");
        fmt.indent();
        fmt.is("scaleInstanceByAttribute", scaleInstanceByAttribute);
        fmt.explanatoryNote(
                "Multiplies the amount of energy SHI_ATTRIBUTE energy added to all "
                        + "in-focus instances based on attribute matching");
        fmt.is("scaleInstanceInAction", scaleInstanceInAction);
        fmt.explanatoryNote(
                "Multiplies the amount of energy SHI_ATTRIBUTE energy added to all "
                        + "in-focus instances based on VI matching");
        fmt.deindent();
    }

}

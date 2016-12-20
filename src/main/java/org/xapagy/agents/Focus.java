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
package org.xapagy.agents;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.parameters.Parameters;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.EnergyColors.EnergyColorType;
import org.xapagy.set.EnergyQuantum;
import org.xapagy.set.EnergySet;

public class Focus implements Serializable {

    public static final double INITIAL_ENERGY_INSTANCE = 1.0;
    //
    // This should be probably more of a parameter, but here we are
    //
    public static final double INITIAL_ENERGY_VI = 1.0;
    private static final long serialVersionUID = -9199234140913785418L;
    private Agent agent;
    private Instance currentScene;
    private EnergySet<Instance> ise;
    private EnergySet<VerbInstance> vise;

    public Focus(Agent agent) {
        this.agent = agent;
        ise = new EnergySet<>(agent);
        vise = new EnergySet<>(agent);
    }



    /**
     * Applies the energy quantum to the instance energies. This is the only way
     * the instance energies in the focus can be changed. If it is a decreasing 
     * multiplicative charge, call the gc.
     * 
     * @param eq
     */
    public void applyInstanceEnergyQuantum(EnergyQuantum<Instance> eq) {
        ise.applyEnergyQuantum(eq);
        if (eq.getMultiplicativeChange() < 1.0) {
            gcInstance(eq.getFocusComponent());
        }
    }

    /**
     * Applies the energy quantum to the VI energies. This is the only way the
     * VI energies in the focus can be changed. If it is a decreasing multiplicative
     * change, call the gc.      
     *       
     * @param eq
     */
    public void applyViEnergyQuantum(EnergyQuantum<VerbInstance> eq) {
        if (vise.valueEnergy(eq.getFocusComponent(), EnergyColors.FOCUS_VI) == 0) {
            eq.getFocusComponent().markReferringInstances();
        }
        vise.applyEnergyQuantum(eq);
        if (eq.getMultiplicativeChange() < 1.0) {
            gcVerbInstance(eq.getFocusComponent());
        }
    }



    /**
     * Returns the current scene
     * 
     * @return the current
     */
    public Instance getCurrentScene() {
        return currentScene;
    }

    /**
     * Returns the energy of an instance in the focus
     * 
     * @param instance
     * @param ec
     *            - the energy color we are querying
     * 
     * @return - the energy value of the instance
     */
    public double getEnergy(Instance instance, String ec) {
        return ise.valueEnergy(instance, ec);
    }

    /**
     * Returns the salience of a VI in the focus
     * 
     * @param vi
     * @param ec
     *            - the energy color
     * @return
     */
    public double getEnergy(VerbInstance vi, String ec) {
        return vise.valueEnergy(vi, ec);
    }

    /**
     * Returns the in-focus instances in a new list. Only instances, not scenes.
     * 
     * @return
     */
    public List<Instance> getInstanceList(String ec) {
        List<Instance> retval = new ArrayList<>();
        for (Instance instance : ise.getParticipants(ec)) {
            if (!instance.isScene()) {
                retval.add(instance);
            }
        }
        return retval;
    }

    /**
     * Returns the Instances that have any of the energies
     * 
     * @return
     */
    public List<Instance> getInstanceListAllEnergies() {
        List<Instance> retval = new ArrayList<>();
        for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.FOCUS_INSTANCE)) {
            for (Instance instance : getInstanceList(ec)) {
                if (!retval.contains(instance)) {
                    retval.add(instance);
                }
            }
        }
        return retval;
    }

    /**
     * Returns the salience of an instance in the focus
     * 
     * @param instance
     * @param ec
     *            the energy color
     * @return
     */
    public double getSalience(Instance instance, String ec) {
        double param = agent.getEnergyColors().getEnergyToSalience(ec);
        return EnergyColors.convert(ise.valueEnergy(instance, ec), param);
    }

    /**
     * Returns the salience of a VI in the focus
     * 
     * @param vi
     * @param ec
     *            the energy color
     * @return
     */
    public double getSalience(VerbInstance vi, String ec) {
        double param = agent.getEnergyColors().getEnergyToSalience(ec);
        return EnergyColors.convert(vise.valueEnergy(vi, ec), param);
    }

    /**
     * Returns all the scenes
     * 
     * @return
     */
    public List<Instance> getSceneList(String ec) {
        List<Instance> retval = new ArrayList<>();
        for (Instance instance : ise.getParticipants(ec)) {
            if (instance.isScene()) {
                retval.add(instance);
            }
        }
        return retval;
    }

    /**
     * Returns the scenes that have any of the energies
     * 
     * @return
     */
    public List<Instance> getSceneListAllEnergies() {
        List<Instance> retval = new ArrayList<>();
        for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.FOCUS_INSTANCE)) {
            for (Instance instance : getSceneList(ec)) {
                if (!retval.contains(instance)) {
                    retval.add(instance);
                }
            }
        }
        return retval;
    }

    /**
     * Returns the in-focus VIs in a new list
     * 
     * @return
     */
    public List<VerbInstance> getViList(String ec) {
        return vise.getParticipants(ec);
    }

    /**
     * Returns the VIs who have any of the energies of type FOCUS_VI
     * 
     * @return
     */
    public List<VerbInstance> getViListAllEnergies() {
        List<VerbInstance> retval = new ArrayList<>();
        for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.FOCUS_VI)) {
            for (VerbInstance vi : getViList(ec)) {
                if (!retval.contains(vi)) {
                    retval.add(vi);
                }
            }
        }
        return retval;
    }



    /**
     * Possibly garbage collect a instance.
     * 
     * Garbage collect the instances where all the energy colors are smaller
     * than the minimum. This can happen either due to decay, SaMvForget,
     * SaMvChange or the removal of the scene (which takes all the nodes with
     * it)
     * 
     * @param instance
     */
    private void gcInstance(Instance instance) {
        Parameters p = agent.getParameters();
        double minimumInstance =
                p.get("A_FCM", "G_GENERAL",
                        "N_MINIMUM_INSTANCE");
        boolean toRemove = true;
        for (String ec2 : agent.getEnergyColors().getEnergies(EnergyColorType.FOCUS_INSTANCE)) {
            if (ise.valueEnergy(instance, ec2) > minimumInstance) {
                toRemove = false;
            }
        }
        if (toRemove) {
            ise.remove(instance);
        }
    }


    /**
     * Garbage collect: remove the VIs whose total energy is smaller than a
     * minimum. This can happen due to decay, successor etc.
     * 
     * @param vi
     */
    public void gcVerbInstance(VerbInstance vi) {
        Parameters p = agent.getParameters();
        double minimumVi =
                p.get("A_FCM", "G_GENERAL",
                        "N_MINIMUM_VI");
        // KLUDGE: do not garbage collect summarization energy instances
        //if (vise.valueEnergy(vi, EnergyColors.FOCUS_SUMMARIZATION_VI) > 0.0) {
        //    return;
        //}
        
        //
        //  Garbage collect those VIs whose total energy over all energy types is smaller 
        //
        double totalEnergy = 0;
        for(String ec: agent.getEnergyColors().getEnergies(EnergyColorType.FOCUS_VI)) {
            totalEnergy += vise.valueEnergy(vi, ec);
         }     
        if (totalEnergy < minimumVi) {
            vise.remove(vi);
        }
        /*
        boolean toRemove = true;
        for (String ec2 : agent.getEnergyColors().getEnergies(EnergyColorType.FOCUS_VI)) {
            if (vise.valueEnergy(vi, ec2) > minimumVi) {
                toRemove = false;
                break;
            }
        }
        if (toRemove) {
            vise.remove(vi);
        }
        */
        //
        // sanity check: check if we have VIs which refer to instances which are
        // not in the focus
        //
        for (VerbInstance vicheck : getViListAllEnergies()) {
            boolean toDelete = true;
            @SuppressWarnings("unused")
            Instance icheckMissing = null;
            for (ViPart part : ViStructureHelper
                    .getAllowedInstanceParts(vicheck.getViType())) {
                Instance icheck = (Instance) vicheck.getPart(part);
                icheckMissing = icheck;
                for (String ec2 : agent.getEnergyColors().getEnergies(EnergyColorType.FOCUS_INSTANCE)) {
                    if (ise.valueEnergy(icheck, ec2) > 0) {
                        toDelete = false;
                        break;
                    }
                }
            }
            if (toDelete) {
                // TextUi.println("Removing: " + PrettyPrint.ppConcise(vicheck,
                // agent));
                // TextUi.println("  because of missing instance: " +
                // PrettyPrint.ppConcise(icheckMissing, agent));
                vise.remove(vicheck);
            }
        }
    }

    /**
     * The current scene is the new one
     * 
     * @param instance
     */
    public void setCurrentScene(Instance scene) {
        currentScene = scene;
    }

    /**
     * Returns a list of the energy quantums affecting the focus of fi for
     * energy ec
     * 
     * @param fi
     * @param si
     * @param ec
     * @return
     */
    public List<EnergyQuantum<Instance>> getEnergyQuantums(Instance fi,
            String ec) {
        return ise.getEnergyQuantums(fi, ec);
    }

    /**
     * Returns a list of the energy quantums affecting the focus of fvi in
     * energy color ec. Used for the explanation
     * 
     * @param fi
     * @param si
     * @param ec
     * @return
     */
    public List<EnergyQuantum<VerbInstance>> getEnergyQuantums(
            VerbInstance fvi, String ec) {
        return vise.getEnergyQuantums(fvi, ec);
    }

}

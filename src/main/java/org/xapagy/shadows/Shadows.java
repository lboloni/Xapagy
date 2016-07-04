/*
   This file is part of the Xapagy project
   Created on: Sep 13, 2010
 
   org.xapagy.story.OrganicShadow
 
   Copyright (c) 2008-2010 Ladislau Boloni
 */

package org.xapagy.shadows;

import java.io.Serializable;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.xapagy.agents.Agent;
import org.xapagy.agents.Focus;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViSimilarityHelper;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.EnergyColors.EnergyColorType;
import org.xapagy.set.EnergyQuantum;
import org.xapagy.set.InstanceSet;
import org.xapagy.set.PairEnergySet;
import org.xapagy.set.ViSet;
import org.xapagy.shadows.ShadowInstanceComparator.SortedBy;

/**
 * Shadow focus. Normally, the only external interface should be progress...
 * 
 * @author Ladislau Boloni
 * 
 */
public class Shadows implements Serializable {

    private static final long serialVersionUID = 4960463475868147197L;
    private Agent agent;
    private PairEnergySet<Instance> ise;
    private PairEnergySet<VerbInstance> vise;

    public Shadows(Agent agent) {
        super();
        this.agent = agent;
        ise = new PairEnergySet<>(agent);
        vise = new PairEnergySet<>(agent);
    }

    /**
     * Applies the shadow quantum to the instance, first checking the
     * parameters, whether same scene instances are allowed (usually, they are
     * not)
     * 
     */
    public void applyInstanceEnergyQuantum(EnergyQuantum<Instance> eq) {
        boolean allowSameSceneInstance =
                agent.getParameters().getBoolean("A_SHM",
                        "G_GENERAL",
                        "N_ALLOW_SAME_SCENE_SHADOW_INSTANCE");
        //
        // Current implementation: if !allowSameSceneInstance - do not allow
        // shadows which are in the same scene and still in the focus. This will
        // still allow changed instances to shadow their new version
        //
        Focus fc = agent.getFocus();
        Instance fi = eq.getFocusComponent();
        Instance si = eq.getShadowComponent();
        if (!allowSameSceneInstance) {
            if ((fc.getEnergy(si, EnergyColors.FOCUS_INSTANCE) > 0.0)
                    && (fi.getScene() == si.getScene())) {
                return;
            }
        }
        //
        // Ok, apply the shadow quantum
        //
        ise.applyEnergyQuantum(eq);
    }

    /**
     * Applies a shadow quantum to a VI.
     * 
     * First it performs a number of validation tests if the application is
     * possible, then just calls the one in ShadowEnergy
     * 
     * @param eq
     */
    public void applyViEnergyQuantum(EnergyQuantum<VerbInstance> eq) {
        boolean allowSameSceneVi =
                agent.getParameters().getBoolean("A_SHM",
                        "G_GENERAL",
                        "N_ALLOW_SAME_SCENE_SHADOW_VI");
        VerbInstance fvi = eq.getFocusComponent();
        VerbInstance svi = eq.getShadowComponent();
        //
        // If they have common scenes and the parameters do not allow it, skip
        //
        if (allowSameSceneVi) {
            // if the two VI's share a scene, skip
            Set<Instance> fviScenes = fvi.getReferencedScenes();
            Set<Instance> sviScenes = svi.getReferencedScenes();
            fviScenes.retainAll(sviScenes);
            if (!fviScenes.isEmpty()) {
                return;
            }
        }
        //
        // If they are not compatible (eg. different types) skip
        //
        if (!ViSimilarityHelper.isCompatible(agent, fvi, svi)) {
            return;
        }
        //
        // Ok, apply the shadow quantum
        //
        vise.applyEnergyQuantum(eq);
    }

    /**
     * Calls the garbage collector for all the components. Called from
     * DaShmGarbageCollect
     * 
     * FIXME: relying on the GENERIC energies here is not a good idea!!!
     * 
     * @param component
     * @param time
     * @param safeEnergy
     * @param inflectionPopulation
     * @param beta
     */
    public void garbageCollect(double time, double safeEnergy,
            double inflectionPopulation, double beta) {
        // FIXME: for the time being, we are only garbage collecting shadow
        // generic
        String ecI = EnergyColors.SHI_GENERIC;
        for (Instance instance : ise.getHeads(ecI)) {
            ise.garbageCollectStochastic(instance, time, safeEnergy,
                    inflectionPopulation, beta, agent.getEnergyColors().getEnergies(EnergyColorType.SHADOW_INSTANCE));
        }
        String ecV = EnergyColors.SHV_GENERIC;
        for (VerbInstance vi : vise.getHeads(ecV)) {
            vise.garbageCollectStochastic(vi, time, safeEnergy,
                    inflectionPopulation, beta, agent.getEnergyColors().getEnergies(EnergyColorType.SHADOW_VI));
        }
    }

    /**
     * The energy value of instance s in the shadow of instance f
     * 
     * @param fi
     * @param si
     * @return
     */
    public double getEnergy(Instance fi, Instance si, String sc) {
        return ise.valueEnergy(fi, si, sc);
    }

    /**
     * The absolute value of svi in the shadow of the fvi
     * 
     * @param focusInstance
     * @param shadowInstance
     * @return
     */
    public double getEnergy(VerbInstance fvi, VerbInstance svi, String sc) {
        return vise.valueEnergy(fvi, svi, sc);
    }

    /**
     * Returns the members of the shadow for a given energy in a new list,
     * sorted in the decreasing order of the absolute energy
     * 
     * @param fi
     * @param ec
     *            - the energy color
     * @return
     */
    public List<Instance> getMembers(Instance fi, String ec) {
        List<Instance> retval = ise.getComponents(fi, ec);
        ShadowInstanceComparator sic =
                new ShadowInstanceComparator(SortedBy.ENERGY, agent, fi, ec);
        Collections.sort(retval, sic);
        Collections.reverse(retval);
        return retval;
    }

    /**
     * Returns the members of the shadow in a new list
     * 
     * @param fvi
     * @return
     */
    public List<VerbInstance> getMembers(VerbInstance fvi, String ec) {
        List<VerbInstance> retval = vise.getComponents(fvi, ec);
        ShadowViComparator svic =
                new ShadowViComparator(SortedBy.ENERGY, agent, fvi, ec);
        Collections.sort(retval, svic);
        Collections.reverse(retval);
        return retval;
    }

    /**
     * Returns an InstanceSet composed of all the values into whose shadows the instance si participates.
     * The participation in the instanceset is equal with the <em>salience<\em> of the shadow. 
     * 
     * @param si
     * @param ec
     *            - the energy color in which we are considering
     * @return
     */
    public InstanceSet getReverseShadow(Instance si, String ec) {
        InstanceSet retval = new InstanceSet();
        for (Instance fi : agent.getFocus().getInstanceList(EnergyColors.FOCUS_INSTANCE)) {
            double reverseSalience = agent.getShadows().getSalience(fi, si, ec);
            if (reverseSalience != 0.0) {
                retval.change(fi, reverseSalience);
            }
        }
        return retval;
    }

    /**
     * Returns a ViSet composed of all the values into whose shadows the instance svi participates.
     * The participation in the ViSet is equal with the <em>salience<\em> of the shadow. 
     * 
     * @param svi
     * @param ec
     *            - the energy color on which the reversal is done
     * @return
     */
    public ViSet getReverseShadow(VerbInstance svi, String ec) {
        ViSet retval = new ViSet();
        for (VerbInstance fvi : agent.getFocus().getViList(EnergyColors.FOCUS_VI)) {
            double reverseSalience =
                    agent.getShadows().getSalience(fvi, svi, ec);
            if (reverseSalience != 0.0) {
                retval.change(fvi, reverseSalience);
            }
        }
        return retval;
    }

    /**
     * The salience the shadow of instance s in the shadow of instance f
     * 
     * @param fi
     * @param si
     * @param sc - the energy color
     * @return
     */
    public double getSalience(Instance fi, Instance si, String sc) {
        double param = agent.getEnergyColors().getEnergyToSalience(sc);
        return EnergyColors.convert(ise.valueEnergy(fi, si, sc), param);
    }

    /**
     * The salience of the shadow svi in the shadow of the fvi
     * 
     * @param focusInstance
     * @param shadowInstance
     * @param sc - the energy color
     * @return
     */
    public double
            getSalience(VerbInstance fvi, VerbInstance svi, String sc) {
        double param = agent.getEnergyColors().getEnergyToSalience(sc);
        return EnergyColors.convert(vise.valueEnergy(fvi, svi, sc), param);
    }

    /**
     * Returns a list of the energy quantums affecting the shadowing of fi by
     * si, for energy ec
     * 
     * @param fi
     * @param si
     * @param ec
     * @return
     */
    public List<EnergyQuantum<Instance>> getEnergyQuantums(Instance fi,
            Instance si, String ec) {
        return ise.getEnergyQuantums(fi, si, ec);
    }

    /**
     * Returns a list of the energy quantums affecting the shadowing of fvi by
     * svi in energy color ec. Used for the explanation
     * 
     * @param fi
     * @param si
     * @param ec
     * @return
     */
    public List<EnergyQuantum<VerbInstance>> getEnergyQuantums(
            VerbInstance fvi, VerbInstance svi, String ec) {
        return vise.getEnergyQuantums(fvi, svi, ec);
    }

    /**
     * Merge a set of VIs into a shadow - this is used when converting HLSs to
     * shadows. It is only converting the SHADOW_GENERIC energy color!!!
     * 
     * @param fvi
     *            - the focus VI into which we are merging
     * @param toMerge
     * @param ratio
     * @param calledFrom
     *            - the calling chain, used for explanation
     * 
     */
    public void mergeInShadow(VerbInstance fvi, ViSet toMerge, double ratio,
            String calledFrom) {
        for (VerbInstance s : toMerge.getParticipants()) {
            // if (!ViSimilarityHelper.isCompatible(agent, fvi, s)) {
            // continue;
            // }
            double addition = ratio * toMerge.value(s);
            EnergyQuantum<VerbInstance> sq =
                    EnergyQuantum.createAdd(fvi, s, addition,
                            EnergyQuantum.TIMESLICE_ONE,
                            EnergyColors.SHV_GENERIC, "mergeInShadow + "
                                    + calledFrom);
            applyViEnergyQuantum(sq);
        }
    }

   
    /**
     * This utility function first calls the EnergyQuantum.compositeAdd, with
     * the ratioInstanceToVi being given by the parameter. Then, it applies all
     * the resulting energy quantums.
     * 
     * @param fvi
     * @param svi
     * @param source
     *            - the calling chain, used for explanation
     * @param additiveStrengt
     *            - the additive value for the VI
     * 
     */
    public void compositeAdd(VerbInstance fvi, VerbInstance svi,
            double additiveChange, double timeSlice, String source,
            String ecVi, String ecInstance) {
        double ratioInstanceToVi =
                agent.getParameters().get("A_SHM",
                        "G_GENERAL",
                        "N_FRACTION_STRENGTHEN_INSTANCE_PART");
        SimpleEntry<EnergyQuantum<VerbInstance>, List<EnergyQuantum<Instance>>> eqs =
                EnergyQuantum.createCompositeAdd(fvi, svi, additiveChange,
                        timeSlice, ratioInstanceToVi, source, ecVi, ecInstance);
        // now, apply all the eqs
        applyViEnergyQuantum(eqs.getKey());
        for (EnergyQuantum<Instance> eqi : eqs.getValue()) {
            applyInstanceEnergyQuantum(eqi);
        }
    }

    /**
     * Gets all the members of any energy of a given focus instance. The order
     * in which this is presented, is that first they appear in the order of the
     * first EnergyColor, then for the ones for which the first energy is zero,
     * in the order of the second one and so on.
     * 
     * @param fi
     *            - the head of the shadow
     * @return
     */
    public List<Instance> getMembersAnyEnergy(Instance fi) {
        List<Instance> retval = new ArrayList<>();
        for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.SHADOW_INSTANCE)) {
            for (Instance instance : getMembers(fi, ec)) {
                if (!retval.contains(instance)) {
                    retval.add(instance);
                }
            }
        }
        return retval;
    }

    /**
     * Gets all the members, of any energy, of a given focus VI. The order in
     * which this is presented, is that first they appear in the order of the
     * first EnergyColor, then for the ones for which the first energy is zero,
     * in the order of the second one and so on.
     * 
     * @param fvi
     * @return
     */
    public List<VerbInstance> getMembersAnyEnergy(VerbInstance fvi) {
        List<VerbInstance> retval = new ArrayList<>();
        for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.SHADOW_VI)) {
            for (VerbInstance vi : getMembers(fvi, ec)) {
                if (!retval.contains(vi)) {
                    retval.add(vi);
                }
            }
        }
        return retval;
    }

}

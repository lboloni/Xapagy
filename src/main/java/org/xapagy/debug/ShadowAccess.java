/*
   This file is part of the Xapagy project
   Created on: Jul 12, 2014
 
   org.xapagy.debug.ShadowAccess
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.debug;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.ui.TextUi;

/**
 * To be used during debugging shadows. It allows us to access the shadows using
 * text mode parameters. We are passing InstanceMatch and ViMatch objects, and
 * return shadow record objects.
 * 
 * @author Ladislau Boloni
 *
 */
public class ShadowAccess implements Serializable {

    private static final long serialVersionUID = -2263516423226766208L;
    private Agent agent;
    private ViMatch viMatch;
    private InstanceMatch instanceMatch;

    /**
     * Constructor, preparas the access points
     * 
     * @param agent
     */
    public ShadowAccess(Agent agent) {
        this.agent = agent;
        this.viMatch = new ViMatch(agent);
        this.instanceMatch = new InstanceMatch(agent);
    }

    /**
     * Returns a list of shadows records
     * 
     * @param imfFocus
     *            - the focus record, it looks up
     * @param imfShadow
     * @param sortBy
     *            - if not null, sort in descending order by this color
     * @return
     */
    public List<ShadowRecord<Instance>> getShadows(
            InstanceMatchFilter imfFocus, InstanceMatchFilter imfShadow,
            String sortBy) {
        List<ShadowRecord<Instance>> retval = new ArrayList<>();
        Instance instanceFocus = identifyFocusInstance(imfFocus);
        if (instanceFocus == null) {
            return null;
        }
        List<Instance> shadowFocus =
                identifyShadowInstances(instanceFocus, imfShadow);
        for (Instance si : shadowFocus) {
            ShadowRecord<Instance> sr =
                    new ShadowRecord<>(instanceFocus, si, sortBy, agent);
            retval.add(sr);
        }
        if (sortBy != null) {
            ShadowRecordComparator<Instance> comp =
                    new ShadowRecordComparator<>(sortBy);
            Collections.sort(retval, comp);
            Collections.reverse(retval);
        }
        return retval;
    }

    /**
     * Return the shadow record for the shadowing between two instances
     * 
     * @param imfFocus
     * @param imfShadow
     * @param sortBy
     * @param dontFailOnEmpty
     *            - if we set this to true, if we are at empty, return an
     *            artificially created shadow record with empty energies
     * @return
     */
    public ShadowRecord<Instance> getOneShadow(InstanceMatchFilter imfFocus,
            InstanceMatchFilter imfShadow, String sortBy,
            boolean dontFailOnEmpty) {
        List<ShadowRecord<Instance>> srs =
                getShadows(imfFocus, imfShadow, sortBy);
        if (srs == null) {
            return new ShadowRecord<>(false, true, agent);
        }
        // if we have too many records, it means that our filters were not
        // specific enough for this function
        if (srs.size() > 1) {
            if (sortBy != null) {
                return srs.get(0);
            } else {
                TextUi.abort("ShadowAccess.getOneShadow<Instance>: multiple results");
            }
        }
        if (srs.size() == 1) {
            return srs.get(0);
        }
        // so we have zero. If we have don't fail on empty, create an empty one
        if (dontFailOnEmpty) {
            return new ShadowRecord<>(true, true, agent);
        }
        TextUi.abort("ShadowAccess.getOneShadow<Instance>: no result.");
        return null;
    }

    /**
     * Return the shadow record for the shadowing between two VIs
     * 
     * @param vmfFocus
     * @param vmfShadow
     * @param dontFailOnNoShadow
     *            - if we set this to true, if we are at empty, return an
     *            artificially created shadow record with empty energies
     * @return
     */
    public ShadowRecord<VerbInstance> getOneShadow(ViMatchFilter vmfFocus,
            ViMatchFilter vmfShadow, String sortBy,
            boolean dontFailOnNoShadow) {
        List<ShadowRecord<VerbInstance>> srs =
                getShadows(vmfFocus, vmfShadow, sortBy);
        // if we have too many records, it means that our filters were not
        // specific enough for this function
        if (srs == null) {
            return new ShadowRecord<>(false, false, agent);
        }
        if (srs.size() > 1) {
            TextUi.abort("ShadowAccess.getOneShadow<VerbInstance>: multiple results");
        }
        if (srs.size() == 1) {
            return srs.get(0);
        }
        // so we have zero. If we have don't fail on empty, create an empty one
        if (dontFailOnNoShadow) {
            return new ShadowRecord<>(true, false, agent);
        }
        TextUi.abort("ShadowAccess.getOneShadow: no result.");
        return null;
    }

    /**
     * Return the shadow record for the shadowing between two VerbInstances
     * 
     * First we look up the instance in the focus. Then we look up the shadow in
     * its shadows.
     * 
     * @param vmfFocus
     *            - filter describing the focus VI
     * @param vmfShadow
     *            - filter describing the shadow VI
     * @param sortBy
     *            - if not null, sort the shadow records in the descending order
     * 
     * @return returns null if the focus VI could not be found
     */
    public List<ShadowRecord<VerbInstance>> getShadows(ViMatchFilter vmfFocus,
            ViMatchFilter vmfShadow, String sortBy) {
        List<ShadowRecord<VerbInstance>> retval = new ArrayList<>();
        VerbInstance viFocus = identifyFocusVi(vmfFocus);
        if (viFocus == null) {
            return null;
        }
        List<VerbInstance> visShadow = identifyShadowVis(viFocus, vmfShadow);
        for (VerbInstance svi : visShadow) {
            ShadowRecord<VerbInstance> sr =
                    new ShadowRecord<>(viFocus, svi, sortBy, agent);
            retval.add(sr);
        }
        if (sortBy != null) {
            ShadowRecordComparator<VerbInstance> comp =
                    new ShadowRecordComparator<>(sortBy);
            Collections.sort(retval, comp);
            Collections.reverse(retval);
        }
        return retval;
    }

    /**
     * Identifies an instance in the focus based on an instance match filter
     * 
     * @param imf
     */
    private Instance identifyFocusInstance(InstanceMatchFilter imf) {
        List<Instance> focusInstances =
                agent.getFocus().getInstanceListAllEnergies();
        List<Instance> selected = instanceMatch.select(focusInstances, imf);
        if (selected.size() == 0) {
            // TextUi.abort("ShadowAccess.identifyFocusInstance: found 0 but it should have been exactly 1"
            // + imf);
            return null;
        }
        if (selected.size() > 1) {
            TextUi.abort("ShadowAccess.identifyFocusInstance: found "
                    + selected.size() + " but it should have been exactly 1");
        }
        return selected.get(0);
    }

    /**
     * Identifies an instance in the focus based on an instance match filter
     * 
     * @param imf
     */
    private List<Instance> identifyShadowInstances(Instance fi,
            InstanceMatchFilter imf) {
        List<Instance> shadowInstances =
                agent.getShadows().getMembersAnyEnergy(fi);
        List<Instance> selected = instanceMatch.select(shadowInstances, imf);
        return selected;
    }

    /**
     * Identifies an instance in the focus based on an instance match filter
     * 
     * @param imf
     */
    private VerbInstance identifyFocusVi(ViMatchFilter vmf) {
        List<VerbInstance> focusVis = agent.getFocus().getViListAllEnergies();
        List<VerbInstance> selected = viMatch.select(focusVis, vmf);
        if (selected.size() == 0) {
            // TextUi.abort("ShadowAccess.identifyFocusVi: found 0 but it should have been exactly 1 - filter was: "
            // + vmf);
            return null;
        }
        if (selected.size() > 1) {
            TextUi.abort("ShadowAccess.identifyFocusInstance: found "
                    + selected.size() + " but it should have been exactly 1");
        }
        return selected.get(0);
    }

    /**
     * Identifies an instance in the focus based on an instance match filter
     * 
     * @param imf
     */
    private List<VerbInstance> identifyShadowVis(VerbInstance fvi,
            ViMatchFilter vmf) {
        List<VerbInstance> shadowVis =
                agent.getShadows().getMembersAnyEnergy(fvi);
        List<VerbInstance> selected = viMatch.select(shadowVis, vmf);
        return selected;
    }

}

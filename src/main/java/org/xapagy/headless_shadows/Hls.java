/*
   This file is part of the Xapagy project
   Created on: Mar 6, 2011
 
   org.xapagy.story.RVIAContinuation
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.headless_shadows;

import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.xapagy.agents.Agent;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.set.ViSet;
import org.xapagy.ui.prettyprint.PrettyPrint;

/**
 * A headless shadow is a collection of Fsli's which, if instantiated, map into
 * the same VI.
 * 
 * @author Ladislau Boloni
 * 
 */
public class Hls extends AbstractHls {

    private static final long serialVersionUID = -5602737711917063942L;;

    /**
     * The dependencies of the Hls, are the instances which need to be created
     * first before the VI associated with the HLS can be instantiated.
     * 
     * This will be gradually emptied as the dependencies are resolved by the
     * instantiation of the HlsNewInstance
     */
    private Map<ViPart, HlsNewInstance> dependencies = new HashMap<>();
    /**
     * The resolved dependencies of the HlsSupported, for every part. When a
     * dependency had been resolved, it will be moved here
     */
    private Map<ViPart, HlsNewInstance> resolvedDependencies = new HashMap<>();

    /**
     * The set of supports for this HLS (in the form of FslInterpretation
     * objects whose interpretations are compatible with the current ViTemplate)
     */
    private Set<FslInterpretation> supports = new HashSet<>();
    /**
     * The supports sorted by the link component of the FslInterpretation
     */
    private Map<VerbInstance, Set<FslInterpretation>> supportsByViLinked =
            new HashMap<>();

    /**
     * Create a Hls from a viTemplate
     * 
     * @param viTemplate
     * @param agent
     */
    public Hls(VerbInstance viTemplate, Agent agent) {
        this.viTemplate = viTemplate;
        // make a copy of the original viTemplate for later debugging
        this.viTemplateOriginal =
                VerbInstance.createViTemplateFromModel(viTemplate);
        this.identifier = agent.getIdentifierGenerator().getHlsIdentifier();
    }

    /**
     * Adds support from a single FSLI
     * 
     * @param fsli
     */
    public void addSupport(FslInterpretation fsli) {
        supports.add(fsli);
        VerbInstance vir = fsli.getFsl().getViLinked();
        Set<FslInterpretation> set = supportsByViLinked.get(vir);
        if (set == null) {
            set = new HashSet<>();
            supportsByViLinked.put(vir, set);
        }
        set.add(fsli);
    }

    /**
     * Returns an unmodifiable map of the dependencies
     * 
     * @return the dependencies
     */
    public Map<ViPart, HlsNewInstance> getDependencies() {
        return Collections.unmodifiableMap(dependencies);
    }

    /**
     * @return the resolvedDependencies
     */
    public Map<ViPart, HlsNewInstance> getResolvedDependencies() {
        return Collections.unmodifiableMap(resolvedDependencies);
    }

    /**
     * Returns an unmodifiable set of supports
     * 
     * @return the supports
     */
    public Set<FslInterpretation> getSupports() {
        return Collections.unmodifiableSet(supports);
    }

    /**
     * Returns all the linked VIs for this HLS. This must be called only for the
     * values returned from getViLinkeds, otherwise there will be an error
     * 
     * @return
     */
    public Set<FslInterpretation> getSupportsForViLinked(VerbInstance vir) {
        return Collections.unmodifiableSet(supportsByViLinked.get(vir));
    }

    /**
     * Returns all the VI linked components from the supports of this HLS
     * 
     * @return
     */
    public Set<VerbInstance> getViLinkeds() {
        return Collections.unmodifiableSet(supportsByViLinked.keySet());
    }

    /**
     * Resolves a dependency - called from HlsNewInstance
     * 
     * FIXME: perform a tracking...
     * 
     * @param hlsni
     */
    public void resolveDependency(Instance instance, ViPart part,
            HlsNewInstance hlsni) {
        getViTemplate().setResolvedPart(part, instance);
        dependencies.remove(part);

    }

    /**
     * Adds a dependency for a specific part, which can only be resolved by the
     * instantiation of a specific HlsNewInstance
     * 
     * @param part
     * @param hlsni
     */
    public void setDependency(ViPart part, HlsNewInstance hlsni) {
        dependencies.put(part, hlsni);
    }

    /**
     * Calculates the sum of the supports according to the different type of
     * algorithms. Note that not all of them make sense as a summation (for
     * instance, for the summarization it does not make sense)
     * 
     * Returns a pair of (1) a ViSet containing the ViLinked components which
     * provide this kind of support and (2) the sum
     * 
     * FIXME: the ViSet-s value should actually do this. FIXME: this is
     * cacheable, I don't know if it actually makes sense to do it
     * 
     * @return
     */
    public SimpleEntry<ViSet, Double> summativeSupport(FslType fslType,
            Agent agent) {
        double sum = 0;
        ViSet vis = new ViSet();
        for (VerbInstance vir : getViLinkeds()) {
            for (FslInterpretation fsli : getSupportsForViLinked(vir)) {
                if (fsli.getFsl().getFslType().equals(fslType)) {
                    double value = fsli.getTotalSupport(agent);
                    sum += value;
                    vis.change(vir, value);
                }
            }
        }
        return new SimpleEntry<>(vis, sum);
    }

    @Override
    public String toString() {
        return PrettyPrint.ppString(this);
    }
}

/*
   This file is part of the Xapagy project
   Created on: May 30, 2011
 
   org.xapagy.activity.DaHlsSupport
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.activity.hlsmaintenance;

import java.util.ArrayList;
import java.util.List;
import org.xapagy.activity.DiffusionActivity;
import org.xapagy.agents.Agent;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.concepts.operations.Coverage;
import org.xapagy.headless_shadows.FocusShadowLinked;
import org.xapagy.headless_shadows.FslInterpretation;
import org.xapagy.headless_shadows.HeadlessComponents;
import org.xapagy.headless_shadows.Hls;
import org.xapagy.headless_shadows.HlsNewInstance;
import org.xapagy.instances.Instance;
import org.xapagy.instances.ViSimilarityHelper;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * A DA which maintains all the supported type headless components: creates the
 * FSLs, FSLIs and the HLSs objects.
 * 
 * @author Ladislau Boloni
 * 
 */
public class DaHlsmSupport extends DiffusionActivity {
    private static final long serialVersionUID = -3549588739690352302L;

    /**
     * 
     * @param agent
     * @param name
     */
    public DaHlsmSupport(Agent agent, String name) {
        super(agent, name);
    }


    /* (non-Javadoc)
     * @see org.xapagy.activity.DiffusionActivity#extractParameters()
     */
    @Override
    public void extractParameters() {
        // Nothing here, for the time being
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.xapagy.activity.DiffusionActivity#applyInner(double)
     * 
     * The creation of the different supported HLS types
     */
    @Override
    protected void applyInner(double time) {
        HeadlessComponents hlc = agent.getHeadlessComponents();
        //
        // Generate all the FSLs and replace the old ones with the new ones
        //
        List<FocusShadowLinked> fsls = FslGenerator.generateFsls(agent);
        //
        // Explode all the interpretations
        //
        List<FslInterpretation> fslis =
                FslInterpreter.generateInterpretations(agent,
                        fsls);
        //
        // Update the list of HLSs with the new FSLIs. If the FSLI can be
        // perceived
        // as a support for a HLS, add it. If not, create a new HLS.
        //
        List<Hls> hlss = new ArrayList<>();
        for (FslInterpretation support : fslis) {
            boolean added = false;
            for (Hls hls : hlss) {
                if (ViSimilarityHelper.decideSimilarityVi(
                        support.getViInterpretation(), hls.getViTemplate(),
                        agent, true)) {
                    hls.addSupport(support);
                    added = true;
                    break;
                }
            }
            if (!added) { // create the Hls
                Hls hls = new Hls(support.getViInterpretation(), agent);
                hls.addSupport(support);
                hlss.add(hls);
            }
        }
        //
        // The maintenance of the stores happens only here which shows that we
        // can
        // later virtualize then away
        //
        hlc.replaceHlss(hlss);
        hlc.replaceFsls(fsls, agent);
        hlc.replaceFslis(fslis);

        //
        // Maintain the Hlsni collection
        //
        maintainNewInstanceHlsCollection();

    }

    /**
     * If it finds the appropriate HLSNI in the list returns it. Otherwise,
     * creates a new HLSNI adds it to the list and returns it.
     * 
     * @param scene
     * @param co
     * @return
     */
    private HlsNewInstance findOrCreateHlsni(List<HlsNewInstance> hlsnis,
            Instance scene, ConceptOverlay co) {
        for (HlsNewInstance hlsni : hlsnis) {
            if (!hlsni.getScene().equals(scene)) {
                continue;
            }
            if (!Coverage.decideSimilarity(hlsni.getAttributes(), co)) {
                continue;
            }
            return hlsni;
        }
        HlsNewInstance hlsni = new HlsNewInstance(agent, scene, co);
        hlsnis.add(hlsni);
        return hlsni;
    }

    /**
     * Maintains the new instance Hlss - at the same time also re-allocates the
     * Hls-s if necessary
     */
    private void maintainNewInstanceHlsCollection() {
        HeadlessComponents hlc = agent.getHeadlessComponents();
        List<HlsNewInstance> hlsnis = new ArrayList<>();
        for (Hls hls : hlc.getHlss()) {
            if (hls.getViTemplate().getNewParts().isEmpty()) {
                continue;
            }
            for (ViPart part : hls.getViTemplate().getNewParts().keySet()) {
                ConceptOverlay co = hls.getViTemplate().getNewParts().get(part);
                // FIXME: identify the right scene here
                Instance scene = agent.getFocus().getCurrentScene();
                HlsNewInstance hlsni = findOrCreateHlsni(hlsnis, scene, co);
                hls.setDependency(part, hlsni);
                hlsni.setMotivation(hls, part);
            }
        }
        hlc.replaceHlsnis(hlsnis);
    }


    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IxwFormattable#formatTo(org.xapagy.ui.formatters.IXwFormatter, int)
     */
    @Override
    public void formatTo(IXwFormatter fmt, int detailLevel) {
        fmt.add("DaHlsmSupport");
    }

}

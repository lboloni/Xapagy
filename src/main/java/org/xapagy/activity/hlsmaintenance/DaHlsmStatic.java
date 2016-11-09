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
package org.xapagy.activity.hlsmaintenance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.xapagy.activity.DiffusionActivity;
import org.xapagy.agents.Agent;
import org.xapagy.agents.Focus;
import org.xapagy.headless_shadows.HeadlessComponents;
import org.xapagy.headless_shadows.SOSP;
import org.xapagy.headless_shadows.StaticFSLI;
import org.xapagy.headless_shadows.StaticHls;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViClassifier;
import org.xapagy.instances.ViSimilarityHelper;
import org.xapagy.instances.ViClassifier.ViClass;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.set.EnergyColors;
import org.xapagy.shadows.Shadows;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * A DA which maintains the StaticFSLIs and related components (static HLSs)
 * 
 * @author Ladislau Boloni
 * Created on: Aug 30, 2014
 */
public class DaHlsmStatic extends DiffusionActivity {

    /**
     * 
     * @param agent
     * @param name
     */
    public DaHlsmStatic(Agent agent, String name) {
        super(agent, name);
    }

    /**
     * 
     */
    private static final long serialVersionUID = -4264685412431025839L;

    /*
     * (non-Javadoc)
     * 
     * @see org.xapagy.activity.DiffusionActivity#applyInner(double)
     */
    @Override
    protected void applyInner(double time) {
        generateSOPSs();
        generateStaticFslis();
        generateStaticHlss();
    }

    /**
     * Generates the StaticHls objects by clustering together the StaticFLSIs
     */
    private void generateStaticHlss() {
        HeadlessComponents hc = agent.getHeadlessComponents();
        List<StaticHls> shlss = new ArrayList<>();
        for (StaticFSLI sfsli: hc.getStaticFSLIs()) {
            boolean added = false;
            for (StaticHls shls : shlss) {
                if (ViSimilarityHelper.decideSimilarityVi(
                        sfsli.getViInterpretation(), shls.getViTemplate(),
                        agent, true)) {
                    shls.addSupport(sfsli);
                    added = true;
                    break;
                }
            }
            if (!added) { // create the Hls
                StaticHls hls = new StaticHls(sfsli.getViInterpretation(), agent);
                hls.addSupport(sfsli);
                shlss.add(hls);
            }
        }
        Collections.sort(shlss);
        Collections.reverse(shlss);
        hc.replaceStaticHlss(shlss);
    }

    /**
     * Generates the StaticFSLI objects
     */
    private void generateStaticFslis() {
        HeadlessComponents hc = agent.getHeadlessComponents();
        //
        // For each sops stronger than a score
        // look for the actions of the scene
        // generate the StaticFSLIs from that
        //
        List<StaticFSLI> sfslis = new ArrayList<>();
        for (SOSP sosp : hc.getSOSPs()) {
            Instance scene = sosp.getShadowScene();
            // find all the VIs in the scene
            Set<VerbInstance> vis = new HashSet<>();
            vis.addAll(scene.getReferringVis());
            for(Instance member: scene.getSceneMembers()) {
                vis.addAll(member.getReferringVis());
            }
            for (VerbInstance vi : vis) {
                // only action VI
                if (!ViClassifier.decideViClass(ViClass.ACTION, vi, agent)) {
                    continue;
                }
                if (sosp.isSV()) {
                    if (vi.getViType() != ViType.S_V) {
                        continue;
                    }
                    if (!vi.getSubject().equals(sosp.getSiSubject())) {
                        continue;
                    }
                } else {
                    if (vi.getViType() != ViType.S_V_O) {
                        continue;
                    }
                    if (!vi.getSubject().equals(sosp.getSiSubject())) {
                        continue;
                    }
                    if (!vi.getObject().equals(sosp.getSiObject())) {
                        continue;
                    }
                }
                String identifier =
                        agent.getIdentifierGenerator()
                                .getStaticFsliIdentifier();
                StaticFSLI sfsli =
                        new StaticFSLI(agent, identifier, vi, sosp, sosp.getScore());
                sfslis.add(sfsli);
            }
        }
        Collections.sort(sfslis);
        Collections.reverse(sfslis);
        hc.replaceStaticFslis(sfslis);
    }

    /**
     * Generate all the SOPSs.
     * 
     * NOTE: This implementation does not generate SOPS where any of the focus
     * instances are missing
     */
    private void generateSOPSs() {
        HeadlessComponents hc = agent.getHeadlessComponents();
        Shadows sh = agent.getShadows();
        Focus fc = agent.getFocus();
        List<SOSP> sosps = new ArrayList<>();
        for (Instance focusScene : fc.getSceneListAllEnergies()) {
            for (Instance fiSubject : focusScene.getSceneMembers()) {
                for (Instance fiObject : focusScene.getSceneMembers()) {
                    for (Instance siSubject : sh.getMembersAnyEnergy(fiSubject)) {
                        double subjectSalience =
                                sh.getSalience(fiSubject, siSubject,
                                        EnergyColors.SHI_GENERIC);
                        Instance shadowScene = siSubject.getScene();
                        for (Instance siObject : shadowScene.getSceneMembers()) {
                            double objectSalience =
                                    sh.getSalience(fiObject, siObject,
                                            EnergyColors.SHI_GENERIC);
                            if (objectSalience == 0.0) {
                                continue;
                            }
                            // the si-s should be the same only for SV
                            if ((fiSubject != fiObject) && (siSubject == siObject)) {
                                continue;
                            }
                            // create the quadruplets
                            double score = subjectSalience * objectSalience;
                            String identifier = agent.getIdentifierGenerator().getSOSPIdentifier();
                            SOSP quad =
                                    new SOSP(identifier, fiSubject, siSubject, fiObject,
                                            siObject, shadowScene, score);
                            sosps.add(quad);
                        }
                    }
                }
            }
        }
        Collections.sort(sosps);
        Collections.reverse(sosps);
        hc.replaceSOSPs(sosps);
    }

    /* (non-Javadoc)
     * @see org.xapagy.activity.DiffusionActivity#extractParameters()
     */
    @Override
    public void extractParameters() {
        // Nothing here, for the time being
    }

    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IxwFormattable#formatTo(org.xapagy.ui.formatters.IXwFormatter, int)
     */
    @Override
    public void formatTo(IXwFormatter fmt, int detailLevel) {
        fmt.add("DaHlsmStatic");
    }

}

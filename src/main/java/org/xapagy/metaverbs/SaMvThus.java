/*
   This file is part of the Xapagy project
   Created on: Jan 18, 2012
 
   org.xapagy.metaverbs.SaMvThus
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.metaverbs;

import java.util.List;
import java.util.Set;

import org.xapagy.agents.Agent;
import org.xapagy.agents.Focus;
import org.xapagy.concepts.Hardwired;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViClassifier;
import org.xapagy.instances.ViClassifier.ViClass;
import org.xapagy.links.Links;
import org.xapagy.set.EnergyColors;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * 
 * 
 * 
 * @author Ladislau Boloni
 * 
 */
public class SaMvThus extends AbstractSaMetaVerb {

    private static final long serialVersionUID = -7114446061249114025L;

    /**
     * 
     * @param agent
     */
    public SaMvThus(Agent agent) {
        super(agent, "SaMvThus", null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xapagy.activity.SpikeActivity#applyInner()
     */
    @Override
    public void applyInner(VerbInstance verbInstance) {
        VerbInstance viLeader = resolveCoincidenceLeader(verbInstance);
        if (viLeader == null) {
            TextUi.errorPrint("SaMvThus: coincidence, could not resolve the leader!!!");
        }
        Links la = agent.getLinks();
        la.copyAllLinks(viLeader, verbInstance, "SaMvThus");
        la.changeLinkByName(Hardwired.LINK_COINCIDENCE, viLeader, verbInstance, 1.0, "SaMvThus-front"
        + "+changeCoincidence");
        la.changeLinkByName(Hardwired.LINK_COINCIDENCE, verbInstance, viLeader, 1.0, "SaMvThus-back"
        + "+changeCoincidence");
    }

    /**
     * Resolves the hook, the vi to which the verb instance will cling.
     */
    private VerbInstance resolveCoincidenceLeader(VerbInstance verbInstance) throws Error {
        List<String> labels = verbInstance.getVerbs().getLabels();
        // depending whether the VI has verb labels or not, different way to
        // choose the summarization
        if (labels.isEmpty()) {
            return resolveCoincidenceLeaderStrongest(verbInstance);
        } else {
            return resolveCoincidenceLeaderLabelBased(verbInstance, labels);
        }
    }

    /**
     * Resolves the hook to be the strongest VI in the focus which has the label
     * 
     * Essentially, this will then link the others...
     * 
     * @param labels
     * @return
     * @throws Error
     */
    private VerbInstance
            resolveCoincidenceLeaderLabelBased(VerbInstance verbInstance, List<String> labels)
                    throws Error {
        List<VerbInstance> vis =
                agent.getReferenceAPI().VisByLabels(labels,EnergyColors.FOCUS_VI);
        if (vis.isEmpty()) {
            throw new Error("Using thus but nothing with labels: " + labels
                    + " to connect to.");
        }
        for (VerbInstance vi : vis) {
            if (vi.equals(verbInstance)) {
                continue;
            }
            return vi;
        }
        throw new Error("Using thus but nothing with labels: " + labels
                + " to connect to.");
    }

    /**
     * Resolves the hook, the vi to which the verb instance will cling.
     * 
     * Currently, it is the strongest action VI in the focus.
     * 
     * @return
     * @throws Error
     */
    private VerbInstance resolveCoincidenceLeaderStrongest(VerbInstance verbInstance) throws Error {
        VerbInstance viHook = null;
        Focus fc = agent.getFocus();
        double maxVal = 0;
        for (VerbInstance fvi : fc.getViList(EnergyColors.FOCUS_VI)) {
            if (fvi == verbInstance) {
                continue;
            }
            // if the two vi's do not share a scene, skip
            Set<Instance> fviScenes = fvi.getReferencedScenes();
            Set<Instance> viScenes = verbInstance.getReferencedScenes();
            fviScenes.retainAll(viScenes);
            if (fviScenes.isEmpty()) {
                continue;
            }
            if (ViClassifier.decideViClass(ViClass.ACTION, fvi, agent)) {
                double fviValue = fc.getSalience(fvi, EnergyColors.FOCUS_VI);
                if (fviValue > maxVal) {
                    viHook = fvi;
                    maxVal = fviValue;
                }
            }
        }
        if (viHook == null) {
            throw new Error("Using thus but nothing to connect to!!!");
        }
        return viHook;
    }
    
    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IxwFormattable#formatTo(org.xapagy.ui.formatters.IXwFormatter, int)
     */
    @Override
    public void formatTo(IXwFormatter fmt, int detailLevel) {
        fmt.add("SaMvThus");
    }

    /* (non-Javadoc)
     * @see org.xapagy.activity.Activity#extractParameters()
     */
    public void extractParameters() {
    }
}

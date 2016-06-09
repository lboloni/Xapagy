/*
   This file is part of the Xapagy project
   Created on: Apr 23, 2011
 
   org.xapagy.story.HeadlessShadows
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.headless_shadows;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xapagy.agents.Agent;
import org.xapagy.headless_shadows.ChoiceComparator.ChoiceComparatorType;

/**
 * 
 * Headless components: a collection of the various component types which are
 * related to the idea of headless shadows
 * 
 * @author Ladislau Boloni
 * 
 */
public class HeadlessComponents implements Serializable {

    // a comparator for sorting the choices the order of the independent score
    public static ChoiceComparator comparatorIndependentScore =
            new ChoiceComparator(ChoiceComparatorType.INDEPENDENT);
    
    // a comparator for sorting the choices the order of the dependent score
    public static ChoiceComparator comparatorDependentScore =
            new ChoiceComparator(ChoiceComparatorType.DEPENDENT);
    
    // a comparator for sorting the choices in mood score order, if equals,
    // falls back to dependent score
    public static ChoiceComparator comparatorMoodScore =
            new ChoiceComparator(ChoiceComparatorType.MOOD);
    
    private static final long serialVersionUID = -1405336720741068381L;
    /**
     * The choices, indexed by string
     */
    private Map<String, Choice> choices = new HashMap<>();

    /**
     * All the FSL interpretations
     */
    private List<FslInterpretation> fslis = new ArrayList<>();

    /**
     * All the StaticFSLIs
     */
    private List<StaticFSLI> sfslis = new ArrayList<>();
    /**
     * All the SOSPs
     */
    private List<SOSP> sosps = new ArrayList<>();

    
    /**
     * All the FSLs
     */
    private List<FocusShadowLinked> fsls = new ArrayList<>();
    /**
     * The new instance HLSs
     */
    private List<HlsNewInstance> hlsNewInstances = new ArrayList<>();
    /**
     * All the HLSs
     */
    private List<Hls> hlss = new ArrayList<>();
    /**
     * All the StaticHls-s
     */
    private List<StaticHls> shlss = new ArrayList<>();

    /**
     * Empty constructor
     * 
     * @param agent
     */
    public HeadlessComponents() {
    }

    /**
     * @return the choices
     */
    public Map<String, Choice> getChoices() {
        return choices;
    }

    /**
     * Return the choices, in function of a certain comparator
     * 
     * @return the choices
     */
    public List<Choice> getChoices(Comparator<Choice> comparator) {
        List<Choice> retval = new ArrayList<>();
        retval.addAll(choices.values());
        Collections.sort(retval, comparator);
        Collections.reverse(retval);
        return retval;
    }

    /**
     * @return the fslis
     */
    public List<FslInterpretation> getFslis() {
        return Collections.unmodifiableList(fslis);
    }

    /**
     * @return the fslis
     */
    public List<StaticFSLI> getStaticFSLIs() {
        return Collections.unmodifiableList(sfslis);
    }

    
    /**
     * @return the fsls
     */
    public List<FocusShadowLinked> getFsls() {
        return fsls;
    }

    /**
     * @return the hlsNewInstances
     */
    public List<HlsNewInstance> getHlsNewInstances() {
        return Collections.unmodifiableList(hlsNewInstances);
    }

    /**
     * Returns an unmodifiable list of Hlss
     * 
     * @return
     */
    public List<Hls> getHlss() {
        return Collections.unmodifiableList(hlss);
    }

    
    /**
     * Returns an unmodifiable list of StaticHlss
     * 
     * @return
     */
    public List<StaticHls> getStaticHlss() {
        return Collections.unmodifiableList(shlss);
    }

    
    /**
     * Returns an unmodifiable list of SOSPs
     * 
     * @return
     */
    public List<SOSP> getSOSPs() {
        return Collections.unmodifiableList(sosps);
    }
    
    /**
     * Replaces the current collection of SOSPs
     * 
     * @param newFslis
     */
    public void replaceSOSPs(List<SOSP> newSOSPs) {
        sosps.clear();
        sosps.addAll(newSOSPs);
    }
    
    
    
    /**
     * Replaces the current collection of FSLIs
     * 
     * @param newFslis
     */
    public void replaceFslis(List<FslInterpretation> newFslis) {
        fslis.clear();
        fslis.addAll(newFslis);
    }
    
    /**
     * Replaces the current collection of StaticFSLIs
     * 
     * @param newFslis
     */
    public void replaceStaticFslis(List<StaticFSLI> newSfslis) {
        sfslis.clear();
        sfslis.addAll(newSfslis);
    }

    /**
     * Replaces the current collection of StaticHLSs
     * 
     * @param newShlss
     */
    public void replaceStaticHlss(List<StaticHls> newShlss) {
        shlss.clear();
        shlss.addAll(newShlss);
    }

    
    
    /**
     * Replaces the existing FSLs with the new set.
     * 
     */
    public void replaceFsls(List<FocusShadowLinked> toMerge, Agent agent) {
        fsls.clear();
        fsls.addAll(toMerge);
    }

    /**
     * @param hlsnis
     */
    public void replaceHlsnis(List<HlsNewInstance> hlsnis) {
        hlsNewInstances.clear();
        hlsNewInstances.addAll(hlsnis);
    }

    /**
     * @param hlss2
     */
    public void replaceHlss(List<Hls> hlss1) {
        hlss.clear();
        hlss.addAll(hlss1);
    }

    /**
     * Sets the choices, discarding all the previous choices
     * 
     */
    public void replaceChoices(List<Choice> choiceList) {
        choices.clear();
        for (Choice choice : choiceList) {
            choices.put(choice.getIdentifier(), choice);
        }
    }

}

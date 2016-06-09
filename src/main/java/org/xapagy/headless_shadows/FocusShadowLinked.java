/*
   This file is part of the Xapagy project
   Created on: May 30, 2011
 
   org.xapagy.recall.FocusShadowLinked
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.headless_shadows;

import java.io.Serializable;

import org.xapagy.agents.Agent;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.XapagyComponent;
import org.xapagy.set.EnergyColors;
import org.xapagy.ui.prettyprint.PrettyPrint;

/**
 * 
 * A (direct) focus-shadow-linked (FSL) VI triplet project from a focus VI, to a
 * VI in its shadow, and another VI from the autobiographical memory which is in
 * some way "related" to the shadow VI.
 * 
 * A variant of the FSL, the indirect FSL starts not from an instantiated,
 * in-focus VI, but from a potential VI expressed in a choice object.
 * 
 * @author Ladislau Boloni
 * 
 */
public class FocusShadowLinked implements XapagyComponent, Serializable {
    private static final long serialVersionUID = -3044123800882944079L;
    /**
     * For indirect FSL's this choice is the source of the focusVI
     */
    private Choice choice;
    /**
     * The type of the relation connecting the relative to the shadow
     */
    private FslType fslType;

    private String identifier;

    /**
     * An FSL is indirect if the source VI is not a realized focus item, but a
     * Choice
     */
    private boolean isIndirect = false;

    /**
     * The head of the shadow which in which the root participates This is
     * normally a VI in the focus, although it can possibly be coming from a
     * headless shadow
     * 
     */
    private VerbInstance viFocus;

    /**
     * A VI related to the viShadow with the specific relation, this is the VI
     * which we are effectively "predicting". Identical to viShadow if the type
     * is IN_SHADOW
     */
    private VerbInstance viLinked;

    /**
     * A VI in the shadow of viFocus, from which the specific relation starts.
     */
    private VerbInstance viShadow;

    /**
     * Constructing an indirect FSL
     * 
     * @param agent
     *            - passed on to find the identifier generator
     * @param fslType
     * @param viFocus
     * @param viShadow
     * @param viLinked
     */
    public FocusShadowLinked(Agent agent, FslType fslType, Choice choice,
            VerbInstance viShadow, VerbInstance viLinked) {
        super();
        this.identifier =
                agent.getIdentifierGenerator().getFocusShadowLinkedIdentifier();
        this.isIndirect = true;
        this.choice = choice;
        this.fslType = fslType;
        switch (choice.getChoiceType()) {
        case CONTINUATION:
            this.viFocus = choice.getHls().getViTemplate();
            break;
        default:
            throw new Error("Cannot make an indirect FSL out of choice type: "
                    + choice.getChoiceType());
        }
        this.viShadow = viShadow;
        this.viLinked = viLinked;
    }

    /**
     * Constructing a direct FSL
     * 
     * @param agent
     *            - passed on to get to the identifier generator
     * @param fslType
     * @param viFocus
     * @param viShadow
     * @param viLinked
     */
    public FocusShadowLinked(Agent agent, FslType fslType,
            VerbInstance viFocus, VerbInstance viShadow, VerbInstance viLinked) {
        super();
        this.identifier =
                agent.getIdentifierGenerator().getFocusShadowLinkedIdentifier();
        this.isIndirect = false;
        this.choice = null;
        this.fslType = fslType;
        this.viFocus = viFocus;
        this.viShadow = viShadow;
        this.viLinked = viLinked;
    }

    /**
     * Used to measure equality for the purposes of compression Based on the
     * item by item comparison (except for the identifier)
     * 
     * @return
     */
    public boolean deepEqual(FocusShadowLinked other) {
        if (this.isIndirect != other.isIndirect) {
            return false;
        }
        if (this.choice != other.choice) {
            return false;
        }
        if (this.fslType != other.fslType) {
            return false;
        }
        if (this.viFocus != other.viFocus) {
            return false;
        }
        if (this.viShadow != other.viShadow) {
            return false;
        }
        if (this.viLinked != other.viLinked) {
            return false;
        }
        return true;
    }

    /**
     * @return the choice
     */
    public Choice getChoice() {
        return choice;
    }

    /**
     * @return the fslType of the FSL
     */
    public FslType getFslType() {
        return fslType;
    }

    /**
     * @return the identifier
     */
    @Override
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Returns the link strength (dynamically calculated)
     * 
     * @return the relationStrength
     */
    public double getLinkStrength(Agent agent) {
        if (fslType.equals(FslType.IN_SHADOW)) {
            return 1.0;
            // it does not matter if the support calculation is a minimum
        }
        String linkName = FslType.getLinksForFslType(fslType);
        double linkStrength =
                agent.getLinks().getLinkValueByLinkName(viShadow, viLinked,
                        linkName);
        return linkStrength;
    }

    /**
     * The total strength of the FSL
     * 
     * It is defined by the multiple of the shadow strength, relation strength
     * and focus strength
     * 
     * @return
     */
    public double getTotalSupport(Agent agent) {
        double retval = 0;
        if (!isIndirect) {
            double focusStrength =
                    agent.getFocus().getSalience(viFocus, EnergyColors.FOCUS_VI);
            double shadowStrength =
                    agent.getShadows().getSalience(viFocus, viShadow,
                            EnergyColors.SHV_GENERIC);
            double linkStrength = getLinkStrength(agent);
            retval =
                    Math.min(focusStrength,
                            Math.min(shadowStrength, linkStrength));
        } else {
            double focusStrength = choice.getChoiceScore().getScoreDependent();
            double shadowStrength =
                    choice.getChoiceScore().getVirtualShadow().value(viShadow);
            double linkStrength = getLinkStrength(agent);
            retval =
                    Math.min(focusStrength,
                            Math.min(shadowStrength, linkStrength));
        }
        return retval;
    }

    /**
     * @return the ViFocus part of the FSL
     * 
     */
    public VerbInstance getViFocus() {
        return viFocus;
    }

    /**
     * @return the ViLinked part of the FSL
     */
    public VerbInstance getViLinked() {
        return viLinked;
    }

    /**
     * @return the ViShadow part of the FSL
     */
    public VerbInstance getViShadow() {
        return viShadow;
    }

    /**
     * @return the isIndirect
     */
    public boolean isIndirect() {
        return isIndirect;
    }

    @Override
    public String toString() {
        return PrettyPrint.ppString(this);
    }
}

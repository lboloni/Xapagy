/*
   This file is part of the Xapagy project
   Created on: Mar 17, 2013
 
   org.xapagy.agents.Calibration
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.agents;

/**
 * The objective of this class is to collect the type of values which are
 * supposed to be achieved in various components of the system.
 * 
 * These are really numbers without too much meaning, but it is better for them
 * to be here and explained.
 * 
 * 
 * @author Ladislau Boloni
 * 
 */
public class Calibration {

    /**
     * This is a value used by Incompatibility.decideIncompatibility to decide
     * whether two overlays are incompatible or not.
     */
    public static final double decideIncompatibility = 0.05; // was 0.5

    /**
     * Used by relation helper to decide whether if a relation verb is
     * sufficiently present in order to be considered...
     */
    public static final double decideRelationPresent = 0.5;
    /**
     * This is a value used by Similarity.decideSimilarity to decide whether two
     * overlays are similar enough to be considered the same. This is set up in
     * such a way that two action verbs are not similar just because of the
     * verbs....
     */
    public static final double decideSimilarity = 0.7;
}

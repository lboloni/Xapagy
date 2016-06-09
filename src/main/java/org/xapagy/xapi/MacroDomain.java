/*
   This file is part of the Xapagy project
   Created on: Oct 30, 2015
 
   org.xapagy.xapi.MacroDomain
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.xapi;

import org.xapagy.agents.Agent;

/**
 * @author Ladislau Boloni
 *
 */
public class MacroDomain {

    /**
     * Create a new concept or update a concept to a new size
     * 
     * $Concept conceptName, size
     * 
     * @param params - the parameters passed to the original command, to be parsed here
     * @param agent
     */
    public static void domainConcept(String params, Agent agent) {
        throw new Error("Not implemented yet");
    }
    
    /**
     * Sets the overlap between two concepts to a new value
     * 
     * $ConceptOverlap conceptName, conceptName2, overlap
     * 
     * @param params - the parameters passed to the original command, to be parsed here
     * @param agent
     */
    public static void domainConceptOverlap(String params, Agent agent) {
        throw new Error("Not implemented yet");
    }

    /**
     * Sets the impact between two concepts to a new value
     * 
     * $ConceptImpact conceptName, conceptName2, impact
     * 
     * @param params - the parameters passed to the original command, to be parsed here
     * @param agent
     */
    public static void domainConceptImpact(String params, Agent agent) {
        throw new Error("Not implemented yet");
    }

    
    /**
     * A concept word is a fixed way to refer to a concept overlay 
     * 
     * $ConceptWord conceptWord = [conceptName multiplier]
     * 
     * @param params - the parameters passed to the original command, to be parsed here
     * @param agent
     */
    public static void domainConceptWord(String params, Agent agent) {
        throw new Error("Not implemented yet");
    }

    
    
    /**
     * Create a new verb or update a verb to a new size
     * 
     * $Verb conceptName, size
     * 
     * @param params - the parameters passed to the original command, to be parsed here
     * @param agent
     */
    public static void domainVerb(String params, Agent agent) {
        throw new Error("Not implemented yet");
    }
    
    /**
     * Sets the overlap between two verbs to a new value
     * 
     * $VerbOverlap conceptName, conceptName2, overlap
     * 
     * @param params - the parameters passed to the original command, to be parsed here
     * @param agent
     */
    public static void domainVerbOverlap(String params, Agent agent) {
        throw new Error("Not implemented yet");
    }

    /**
     * Sets the impact between two verbs to a new value
     * 
     * $VerbImpact conceptName, conceptName2, impact
     * 
     * @param params - the parameters passed to the original command, to be parsed here
     * @param agent
     */
    public static void domainVerbImpact(String params, Agent agent) {
        throw new Error("Not implemented yet");
    }

    /**
     * A verb word is a fixed way to refer to a verb overlay 
     * 
     * $VerbWord conceptWord = [verbName multiplier]
     * 
     * @param params - the parameters passed to the original command, to be parsed here
     * @param agent
     */
    public static void domainVerbWord(String params, Agent agent) {
        throw new Error("Not implemented yet");
    }

    
}

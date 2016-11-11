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
package org.xapagy.xapi;

import org.xapagy.agents.Agent;

/**
 * @author Ladislau Boloni
 * Created on: Oct 30, 2015
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

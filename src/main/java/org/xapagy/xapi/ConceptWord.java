/*
   This file is part of the Xapagy project
   Created on: May 19, 2012
 
   org.xapagy.xapi.ConceptWord
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.xapi;

import java.io.Serializable;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.instances.XapagyComponent;

/**
 * @author Ladislau Boloni
 * 
 */
public class ConceptWord implements XapagyComponent, Serializable {

    private static final long serialVersionUID = -8562501524043451223L;
    private ConceptOverlay co;
    /**
     * A comment used to document the concept word
     */
    private String comment = "<< no-comment >>";
    private String identifier;
    private String textFormat;

    /**
     * Constructor - the agent must be passed on because it creates its own
     * identifier
     * 
     * It creates it local copy of the Co
     * 
     * @param identifier
     * @param textFormat
     * @param co
     */
    public ConceptWord(Agent agent, String textFormat, ConceptOverlay co) {
        this.identifier =
                agent.getIdentifierGenerator().getConceptWordIdentifier();
        this.textFormat = textFormat;
        ConceptOverlay co2 = new ConceptOverlay(agent);
        co2.addOverlay(co);
        this.co = co2;
    }

    /**
     * @return the co
     */
    public ConceptOverlay getCo() {
        return co;
    }

    public String getComment() {
        return comment;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xapagy.instances.XapagyComponent#getIdentifier()
     */
    @Override
    public String getIdentifier() {
        return identifier;
    }

    /**
     * @return the textFormat
     */
    public String getTextFormat() {
        return textFormat;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}

/*
   This file is part of the Xapagy project
   Created on: May 20, 2012
 
   org.xapagy.xapi.VerbWord
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.xapi;

import java.io.Serializable;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.instances.XapagyComponent;

/**
 * @author Ladislau Boloni
 * 
 */
public class VerbWord implements XapagyComponent, Serializable {

    private static final long serialVersionUID = 5466557157799323862L;
    /**
     * A comment used to document the verb word
     */
    private String comment = "<< no-comment >>";
    private String identifier;
    private String textFormat;
    private VerbOverlay vo;

    /**
     * Constructor - the agent must be passed on because it creates its own
     * identifier
     * 
     * @param identifier
     * @param textFormat
     * @param co
     */
    public VerbWord(Agent agent, String textFormat, VerbOverlay vo) {
        this.identifier =
                agent.getIdentifierGenerator().getVerbWordIdentifier();
        this.textFormat = textFormat;
        VerbOverlay vo2 = new VerbOverlay(agent);
        vo2.addOverlay(vo);
        this.vo = vo2;
    }

    public String getComment() {
        return comment;
    }

    /**
     * @return the identifier
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

    /**
     * @return the vo
     */
    public VerbOverlay getVo() {
        return vo;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}

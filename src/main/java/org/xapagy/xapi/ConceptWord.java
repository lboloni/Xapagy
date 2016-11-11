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

import java.io.Serializable;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.instances.XapagyComponent;

/**
 * @author Ladislau Boloni
 * Created on: May 19, 2012
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

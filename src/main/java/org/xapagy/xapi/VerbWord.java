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
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.instances.XapagyComponent;

/**
 * @author Ladislau Boloni
 * Created on: May 20, 2012
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

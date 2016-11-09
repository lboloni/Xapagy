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
package org.xapagy.instances;

import java.io.Serializable;

import org.xapagy.agents.Agent;

/**
 * Generates new identifiers. Allows the generation of specific identifiers
 * patterns.
 * 
 * @author Ladislau Boloni
 * Created on: Aug 12, 2010
 */
public class IdentifierGenerator implements Serializable {

    private static final long serialVersionUID = 4577891746043200799L;
    private Agent agent;
    private double lastTime;
    private String prefix = "gen";
    private int sequenceNo;

    /**
     * Creates an identifier generator, pass the agent over
     * 
     * @param agent
     */
    public IdentifierGenerator(Agent agent) {
        this.agent = agent;
    }

    /**
     * @return
     */
    public String getChoiceIdentifier() {
        return "choice_" + getNewIdentifier();
    }

    /**
     * Gets a new concept identifier
     */
    public String getConceptIdentifier() {
        return "c_" + getNewIdentifier();
    }

    /**
     * Gets a new concept overlay identifier
     */
    public String getConceptOverlayIdentifier() {
        return "co_" + getNewIdentifier();
    }

    /**
     * Returns an identifier for concept words
     * 
     * @return
     */
    public String getConceptWordIdentifier() {
        return "conceptword_" + getNewIdentifier();
    }

    /**
     * Gets a new shadow Vi relative identifier
     */
    public String getFocusShadowLinkedIdentifier() {
        return "fsl_" + getNewIdentifier();
    }

    /**
     * Gets a new FSL interpretation identifier
     */
    public String getFslInterpretationIdentifier() {
        return "fsli_" + getNewIdentifier();
    }

    /**
     * Gets a new SOSP identifier
     */
    public String getSOSPIdentifier() {
        return "sosp_" + getNewIdentifier();
    }

    
    
    /**
     * Gets a new StaticFSLI identifier
     */
    public String getStaticFsliIdentifier() {
        return "sfsli_" + getNewIdentifier();
    }

    /**
     * Gets a new StaticHLS identifier
     */
    public String getStaticHlsIdentifier() {
        return "shls_" + getNewIdentifier();
    }

    
    
    /**
     * Gets a new HLS identifier
     */
    public String getHlsIdentifier() {
        return "hls_" + getNewIdentifier();
    }

    /**
     * @return
     */
    public String getHlsNewInstanceIdentifier() {
        return "hlsnewinst_" + getNewIdentifier();
    }

    /**
     * Gets a new instance identifier
     * 
     * @return
     */
    public String getInstanceIdentifier() {
        return "i_" + getNewIdentifier();
    }

    /**
     * @return
     */
    public String getLogRecordIdentifier() {
        return "verbword_" + getNewIdentifier();
    }

    /**
     * Gets a new loop item identifier
     */
    public String getLoopItemIdentifier() {
        return "li_" + getNewIdentifier();
    }

    /**
     * Returns a new identifier according to several algorithms. This will still
     * be actively checked by the agent for uniqueness
     * 
     * @param agent
     * @return
     */
    private String getNewIdentifier() {
        //Parameters p = agent.getParameters();
        //double timeStepOfDAs =
        //        p.get("A_GENERAL", "G_GENERAL",
        //                "N_TIME_STEP_OF_DAS");
        //String format = "%.1f";
        //if (timeStepOfDAs < 0.1) {
        //    format = "%.2f";
        //}
        String format = "%.2f";
        double currentTime = agent.getTime();
        if (lastTime != currentTime) {
            sequenceNo = 0;
            lastTime = currentTime;
        }
        sequenceNo++;
        String id =
                prefix + "-" + String.format(format, currentTime) + "-"
                        + sequenceNo;
        return id;
    }

    /**
     * Gets a new identifier for the RsOneLine object
     * 
     * @return
     */
    public String getRecordedStoryIdentifier() {
        return "rs_" + getNewIdentifier();
    }

    /**
     * Gets a new identifier for the RsOneLine object
     * 
     * @return
     */
    public String getRsOneLineIdentifier() {
        return "rsol_" + getNewIdentifier();
    }

    /**
     * Gets a new verb overlay identifier
     */
    public String getSummaryBlockIdentifier() {
        return "sb_" + getNewIdentifier();
    }

    /**
     * Gets a new concept identifier
     */
    public String getVerbIdentifier() {
        return "v_" + getNewIdentifier();
    }

    /**
     * Gets a new VI identifier
     */
    public String getVerbInstanceIdentifier() {
        return "vi_" + getNewIdentifier();
    }

    /**
     * Gets a new verb overlay identifier
     */
    public String getVerbOverlayIdentifier() {
        return "vo_" + getNewIdentifier();
    }

    /**
     * Returns an identifier for concept words
     * 
     * @return
     */
    public String getVerbWordIdentifier() {
        return "verbword_" + getNewIdentifier();
    }

}

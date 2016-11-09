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
package org.xapagy.headless_shadows;

import java.io.Serializable;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.instances.XapagyComponent;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.ui.TextUi;

/**
 * This class is the equivalent of the FslInterpretation, with the difference
 * that the source of this is a VI in the memory, which is predicted based on
 * the shadows of instances
 *
 * @author Ladislau Boloni
 * Created on: Aug 30, 2014
 */
public class StaticFSLI implements XapagyComponent, Serializable, Comparable<StaticFSLI> {

    /**
     *
     */
    private static final long serialVersionUID = -3847810306747923359L;
    private String identifier;
    private SOSP sosp;
    /**
     * The total support for this StaticFSLI (equivalent to the total support of
     * the FSLInterpretation)
     */
    private double totalSupport;
    /**
     * The interpretation, a template based on which VIs might be later created.
     * It can be either fully resolved, or it might have some instance
     * components empty (FIXME: not quite clear, discuss it).
     */
    private VerbInstance viInterpretation;
    private VerbInstance viMemory;

    /**
     * @param identifier
     * @param viInterpretation
     * @param totalSupport
     */
    public StaticFSLI(Agent agent, String identifier, VerbInstance viMemory,
            SOSP sosp, double totalSupport) {
        super();
        this.identifier = identifier;
        this.viMemory = viMemory;
        this.sosp = sosp;
        this.totalSupport = totalSupport;
        ViType viType = viMemory.getViType();
        VerbOverlay verbs = viMemory.getVerbs();
        viInterpretation = VerbInstance.createViTemplate(agent, viType, verbs);
        switch (viType) {
        case S_V_O:
            viInterpretation.setResolvedPart(ViPart.Subject,
                    sosp.getFiSubject());
            viInterpretation.setResolvedPart(ViPart.Object, sosp.getFiObject());
            break;
        case S_V:
            viInterpretation.setResolvedPart(ViPart.Subject,
                    sosp.getFiSubject());
            break;
        default:
            TextUi.abort("StaticFSLI constructor, can not handle verbs of type " + viType);
        }
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

    public SOSP getSosp() {
        return sosp;
    }

    public double getTotalSupport() {
        return totalSupport;
    }

    public VerbInstance getViInterpretation() {
        return viInterpretation;
    }

    public VerbInstance getViMemory() {
        return viMemory;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(StaticFSLI other) {
        return Double.compare(getTotalSupport(), other.getTotalSupport());
    }

}

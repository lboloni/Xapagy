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
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.XapagyComponent;
import org.xapagy.ui.prettyprint.PrettyPrint;

/**
 * 
 * An interpretation of a Focus-Shadow-Linked VI triplet, where all the instance
 * parts are resolved in terms of existing instances in the focus, or the
 * decision to create a new instance has been made.
 * 
 * @author Ladislau Boloni
 * Created on: May 30, 2011
 */
public class FslInterpretation implements XapagyComponent, Serializable {

    private static final long serialVersionUID = 81371368851147702L;
    /**
     * Link back to the FSL from which this FSLI has been instantiated
     */
    private FocusShadowLinked fsl;

    private String identifier;

    /**
     * The fraction of the support this interpretation of the FSL receives. If
     * this is the only possible interpretation, the value will be 1.0. The sum
     * of all interpretations must be 1.0.
     */
    private double supportFraction;

    /**
     * The interpretation, a template based on which VIs might be later created.
     * It can be either fully resolved, or it might have some instance
     * components empty (FIXME: not quite clear, discuss it).
     */
    private VerbInstance viInterpretation;

    public FslInterpretation(String identifier, FocusShadowLinked fsl,
            VerbInstance viInterpretation, double supportFraction) {
        this.identifier = identifier;
        this.fsl = fsl;
        this.viInterpretation = viInterpretation;
        this.supportFraction = supportFraction;
    }

    /**
     * @return the FSL
     */
    public FocusShadowLinked getFsl() {
        return fsl;
    }

    /**
     * @return the identifier
     */
    @Override
    public String getIdentifier() {
        return identifier;
    }

    /**
     * @return the supportPercent
     */
    public double getSupportFraction() {
        return supportFraction;
    }

    /**
     * The total support: the support fraction times the total support of the
     * FSL
     * 
     * @return
     */
    public double getTotalSupport(Agent agent) {
        return supportFraction * fsl.getTotalSupport(agent);
    }

    /**
     * @return the verb instance
     */
    public VerbInstance getViInterpretation() {
        return viInterpretation;
    }

    @Override
    public String toString() {
        return PrettyPrint.ppString(this);
    }

}

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
package org.xapagy.verbalize;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.xapi.reference.XapiReference;

/**
 * @author Ladislau Boloni
 * Created on: Nov 23, 2011
 */
public class VmVoReference extends AbstractVmReference {
    private static final long serialVersionUID = 3484151953731984140L;
    private VerbInstance vi;
    private ViPart viPart;
    private VerbOverlay voAtReference;

    /**
     * @param voAtReference
     * @param vi
     * @param viPart
     * @param referenceText
     */
    public VmVoReference(Agent agent, VerbOverlay vo, VerbInstance vi,
            ViPart viPart, XapiReference xapiReference) {
        super(vo, agent.getTime(), xapiReference);
        this.vi = vi;
        this.viPart = viPart;
        // the voAtReference is created here
        voAtReference = (VerbOverlay) vo.newOverlay();
        voAtReference.addOverlay(vo);

    }

    /**
     * @return the vi
     */
    public VerbInstance getVi() {
        return vi;
    }

    /**
     * @return the viPart
     */
    public ViPart getViPart() {
        return viPart;
    }

    /**
     * @return the voAtReference
     */
    public VerbOverlay getVoAtReference() {
        return voAtReference;
    }

}

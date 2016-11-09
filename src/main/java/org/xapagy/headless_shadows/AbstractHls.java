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
import org.xapagy.ui.TextUi;

/**
 * Common ancestor of Hls and StaticHls
 * 
 * @author Ladislau Boloni
 * Created on: Sep 1, 2014
 */
public abstract class AbstractHls implements XapagyComponent, Serializable {
    
    private static final long serialVersionUID = -5689290068018314058L;


    protected String identifier;


    /**
     * The partial implementation of the VI which will be created if this HLS is
     * implemented. This is gradually moved towards a fully resolved one, as the
     * dependencies are instantiated.
     */
    protected VerbInstance viTemplate;
    /**
     * The original VI template. If there are unresolved components, this will
     * stay unresolved.
     */
    protected VerbInstance viTemplateOriginal;
    
    /**
     * @return the viTemplateOriginal
     */
    public VerbInstance getViTemplateOriginal() {
        return viTemplateOriginal;
    }
    public VerbInstance getViTemplate() {
        return viTemplate;
    }

    /* (non-Javadoc)
     * @see org.xapagy.instances.XapagyComponent#getIdentifier()
     */
    @Override
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Generates a VI corresponding to this HLS.
     * 
     * @return
     */
    public VerbInstance instantiate(Agent agent) {
        // verify for missing parts, this should not have been called
        if (!viTemplate.getMissingParts().isEmpty()) {
            // TextUi.println(PrettyPrint.ppDetailed(this, agent));
            TextUi.println("Hls has missing parts at instantiate"
                    + viTemplate.getMissingParts());
            throw new Error("Hls has missing parts at instantiate");
        }
        // verify for new parts, this should not have been called
        if (!viTemplate.getNewParts().isEmpty()) {
            // TextUi.println(PrettyPrint.ppDetailed(this, agent));
            TextUi.println("Hls has new parts at instantiate"
                    + viTemplate.getMissingParts());
            throw new Error("Hls has new parts at instantiate");
        }
        VerbInstance vi =
                VerbInstance.createViFromResolvedTemplate(agent, viTemplate);
        return vi;
    }


}

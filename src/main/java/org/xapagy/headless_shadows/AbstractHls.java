/*
   This file is part of the Xapagy project
   Created on: Sep 1, 2014
 
   org.xapagy.headless_shadows.AbstractHls
 
   Copyright (c) 2008-2014 Ladislau Boloni
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
 *
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

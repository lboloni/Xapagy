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
package org.xapagy.concepts;

import java.io.Serializable;

import org.xapagy.agents.Agent;
import org.xapagy.instances.VerbInstance;
import org.xapagy.metaverbs.AbstractSaMetaVerb;

public class Verb extends AbstractConcept implements Serializable {

    private static final long serialVersionUID = 8759727500307496910L;
    /**
     * The spike associated with this verb
     */
    private AbstractSaMetaVerb spike;
    /**
     *  The hierarchy level of this verb
     */
    private int summarizationLevel = 0;


    /**
	 * @return the summarization level of this verb
	 */
	public int getSummarizationLevel() {
		return summarizationLevel;
	}

	/**
	 * @param set the summarization level of this verb
	 */
	public void setSummarizationLevel(int summarizationLevel) {
		this.summarizationLevel = summarizationLevel;
	}

	/**
     * @param type
     * @param label
     */
    public Verb(String name, String identifier, AbstractSaMetaVerb spike) {
        super(name, identifier);
        this.spike = spike;
    }

    /**
     * @return the spike
     */
    public AbstractSaMetaVerb getSpike() {
        return spike;
    }

    /**
     * If the verb is a meta-verb, execute it
     * 
     * @param vi - the VI on which we apply the meta-verb
     * @param agent
     */
    public void safeExecute(VerbInstance vi, Agent agent) {
        if (spike == null) {
            return;
        }
        if (spike.getViType() != null
                && !vi.getViType().equals(spike.getViType())) {
            throw new Error("Verb " + name + " can only be applied to "
                    + spike.getViType() + " and the current instance is "
                    + vi.getViType());
        }
        spike.apply(vi);
    }

    @Override
    public String toString() {
        return "[" + name + "]";
    }

}

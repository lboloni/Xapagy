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
package org.xapagy.debug;

import java.io.Serializable;
import java.util.List;

import junit.framework.TestCase;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.Hardwired;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.ui.prettyprint.PrettyPrint;
import org.xapagy.xapi.XapiParserException;

/**
 * @author Ladislau Boloni
 * Created on: Aug 21, 2011
 */
public class ExecHelper implements Serializable {

    private static final long serialVersionUID = -530554166848089538L;
    private Agent agent;
    private AssertionHelper ah;

    public ExecHelper(Agent agent) {
        this.agent = agent;
        ah = new AssertionHelper(agent, false);
    }

    /**
     * 
     * Extracts the action from a verb instance list, as returned from the
     * execution of a single Xapi sentence
     * 
     * @param listVi
     * @return
     */
    public VerbInstance getAction(List<VerbInstance> listVi) {
        // there was a single verb instance
        if (listVi.size() == 1) {
            return listVi.get(0);
        }
        // this was a creation of a new verb instance
        if (listVi.size() == 2) {
            // make sure the first one is a create
            VerbInstance createVi = listVi.get(0);
            if (!isACreate(createVi)) {
                TestCase.fail("The first of two must be a create:"
                        + PrettyPrint.ppDetailed(createVi, agent));
            }
            // FIXME: either it is a create, or a quoted create...
            return listVi.get(1);
        }
        // throw new Error("I don't know how to parse this vi list");        
        return null;
    }

    /**
     * Verifies that this is a create, or a quoted create VI
     * 
     * @param vi
     * @return
     * @throws XapiParserException 
     */
    private boolean isACreate(VerbInstance vi) {
        if (vi.getViType().equals(ViType.QUOTE)) {
            return isACreate(vi.getQuote());
        }
        return ah.viIs(vi, ViType.S_ADJ, "wv_" + Hardwired.VM_CREATE_INSTANCE);
    }

}

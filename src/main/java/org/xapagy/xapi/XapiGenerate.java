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

import java.util.ArrayList;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.ui.TextUi;
import org.xapagy.xapi.reference.XrefStatement;

/**
 * The starting point of speech generation
 * 
 * @author Ladislau Boloni
 * Created on: Sep 27, 2010
 */
public class XapiGenerate {

    /**
     * Generates code for ensuring the existence of an instance
     * 
     * It will not generate code if the instance is already in the focus. Other
     * 
     * @return
     */
    public static List<XrefStatement> generateEnsureInstance(Agent agent,
            Instance instance) {
        List<XrefStatement> retval = new ArrayList<>();
        TextUi.println("XapiGenerate:generateEnsureInstance --- not implemented yet");
        return retval;
    }

    /**
     * Generates code for ensuring the existence of an instance
     * 
     * It will not generate code if the instance is already in the focus. Other
     * 
     * @return
     */
    public static List<XrefStatement> generateStatement(Agent agent,
            VerbInstance vi) {
        List<XrefStatement> retval = new ArrayList<>();
        TextUi.println("XapiGenerate:generateStatement --- not implemented yet");
        return retval;
    }

}

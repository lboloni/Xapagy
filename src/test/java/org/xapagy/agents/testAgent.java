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
package org.xapagy.agents;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;
import org.xapagy.ui.SaveLoadUtil;

/**
 * @author Ladislau Boloni
 * Created on: Aug 31, 2010
 */
public class testAgent {

    public static final String outputDirName = "output";

    /**
     * Tests whether the agent can be serialized and deserialized
     */
    @Test
    public void test() throws IOException {
        String description = "Serialization / deserialization of the agent.";
        TestHelper.testStart(description);

        File outputDir = new File(testAgent.outputDirName);
        outputDir.mkdirs();
        File agentFile = new File(outputDir, "agentJohn.xag");
        Runner r = new Runner("Human");
        r.exec("$Include IfNotDefined Human 'Human'");
        r.exec("A thing 'A' / exists.");
        r.exec("A thing 'B' / exists.");
        SaveLoadUtil<Agent> slu = new SaveLoadUtil<>();
        Assert.assertTrue(slu.save(r.agent, agentFile));

        Agent agent2 = slu.load(agentFile);
        agent2.getLoop().addReading("The 'A' / is-a / man.");
        agent2.getLoop().proceed();

        TestHelper.testDone();
    }
}

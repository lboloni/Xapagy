/*
   This file is part of the Xapagy project
   Created on: Aug 31, 2010
 
   org.xapagy.model.testAgent
 
   Copyright (c) 2008-2010 Ladislau Boloni
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
 * 
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

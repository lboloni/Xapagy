/*
   This file is part of the Xapagy project
   Created on: Sep 27, 2010
 
   org.xapagy.xapi.XapiGenerate
 
   Copyright (c) 2008-2010 Ladislau Boloni
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
 * 
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

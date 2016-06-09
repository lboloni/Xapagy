/*
   This file is part of the Xapagy project
   Created on: Jan 3, 2011
 
   org.xapagy.xapi.testXapiDictionary
 
   Copyright (c) 2008-2010 Ladislau Boloni
 */

package org.xapagy.xapi;

import org.junit.Assert;
import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.debug.Runner;

/**
 * Tests the dictionary. At the same time, it also allows to test the way in
 * which concepts and verbs are created.
 * 
 * @author Ladislau Boloni
 * 
 */
public class testXapiDictionary {

    /**
     * Test for negation in the dictionary
     */
    /*
    @Test
    public void testNegationWord() {
        String description = "Negation for dictionary.";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.createAabConcepts();
        XapiDictionary dict = r.agent.getXapiDictionary();
        ConceptOverlay coInd = dict.getCoForWord("many");
        Assert.assertTrue(coInd != null);
        ConceptOverlay coNotInd = dict.getCoForWord("not-many");
        Assert.assertTrue(coNotInd != null);
        TestHelper.testDone();
    }
    */

    /**
     * The verb exists and is-a must always exist
     */
    @Test
    public void testVerbExists() {
        String description = "Dictionary must have exists and is-a.";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.createAabConcepts();
        XapiDictionary dict = r.agent.getXapiDictionary();
        VerbOverlay verb = dict.getVoForWord("exists");
        Assert.assertTrue(verb != null);
        VerbOverlay verbIsA = dict.getVoForWord("is-a");
        Assert.assertTrue(verbIsA != null);
        TestHelper.testDone();
    }

}

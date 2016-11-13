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
 * Created on: Jan 3, 2011
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
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        XapiDictionary dict = r.agent.getXapiDictionary();
        VerbOverlay verb = dict.getVoForWord("exists");
        Assert.assertTrue(verb != null);
        VerbOverlay verbIsA = dict.getVoForWord("is-a");
        Assert.assertTrue(verbIsA != null);
        TestHelper.testDone();
    }

}

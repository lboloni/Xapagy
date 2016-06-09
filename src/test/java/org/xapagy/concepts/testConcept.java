/*
   This file is part of the Xapagy project
   Created on: Jan 26, 2011
 
   org.xapagy.model.testConcept
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.concepts;

import org.junit.Assert;
import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;
import org.xapagy.ui.prettyprint.PrettyPrint;

/**
 * 
 * Tests the concepts, their printing, as well as the creation of the negation
 * concepts
 * 
 * @author Ladislau Boloni
 * 
 */
@SuppressWarnings("unused")
public class testConcept {

    /**
     * Test whether a negation is correctly created
     * 
     */
    @Test
    public void testNegation() {
        String description =
                "Negations of concepts has been correctly created.";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.createAabConcepts();
        Concept cnotbai10 = r.agent.getConceptDB().getConcept("not-c_bai10");
        Double area = r.agent.getConceptDB().getArea(cnotbai10);
        // TextUi.println("area = " + area);
        Assert.assertTrue(cnotbai10 != null);
        String cnothuman_detail = PrettyPrint.ppDetailed(cnotbai10, r.agent);
        Assert.assertTrue(cnothuman_detail.length() > 30);
        // TextUi.println(cnothuman_detail);
        TestHelper.testDone();
    }

}

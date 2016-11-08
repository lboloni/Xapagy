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
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
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

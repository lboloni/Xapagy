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
package org.xapagy.xapi.language;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.agents.Agent;
import org.xapagy.concepts.AbstractConceptDB;
import org.xapagy.concepts.Concept;
import org.xapagy.debug.Runner;
import org.xapagy.ui.TextUi;
import org.xapagy.xapi.language.parser.ParseException;
import org.xapagy.xapi.language.parser.XapiLang;

/**
 * @author Ladislau Boloni
 * Created on: Nov 1, 2015
 */
public class testXapiLang {



    /**
     * Tests whether the string literal parses a string correctly
     * 
     * @throws ParseException
     */
    @Test
    public void test_string_literal() throws ParseException {
        // simple string double quote separated
        XapiLang xl = parse("\"Hello\"");
        String test = xl.string_literal();
        assertEquals("\"Hello\"", test);
        // simple string single quote separated
        xl = parse("'Hello'");
        test = xl.string_literal();
        assertEquals("\"Hello\"", test);
        // simple string with double quote in the middle
        xl = parse("'Hello \"World\"'");
        test = xl.string_literal();
        // assertEquals("\"Hello\"", test);
        TextUi.println(test);
    }

    /**
     * Creates the parser and tries to parse a line
     */
    //private void parseLine(String text) throws ParseException, Exception {
    //    XapiLang xl = parse(text);
    //    xl.line();
    //}

    /**
     * Tries to do the parsing
     * 
     * @param text
     * @throws ParseException
     */
    private XapiLang parse(String text) {
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        ByteArrayInputStream bais = null;
        try {
            bais = new ByteArrayInputStream(text.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        XapiLang xl = new XapiLang(bais);
        xl.setAgent(r.agent);
        return xl;
    }

    
    
    
    @Test
    public void testCoSpecAsString() throws ParseException {
        String description =
                "Test that the parser indeed correctly parses the CO spec - strings, labels, identifiers";
        TestHelper.testStart(description);
        XapiLang xl = parse("test #foo \"boo\"");
        String temp = xl.cospecInStatement();
        TextUi.println(temp);
        TestHelper.testDone();
    }

    /**
     * Tests for the specification of concepts from Xap
     * 
     * @param xapiText
     * @throws ParseException
     */
    //@Test
    public void testConceptCreate() {
        String description = "The specification of concepts from Xapi";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        AbstractConceptDB<Concept> cdb = r.agent.getConceptDB();
        r.exec("$-- 'Bingo is a game of skill and tenacity'");
        r.exec("$Create Concept c_bingo");
        // check if it exists now
        Concept c_bingo = cdb.getConcept("c_bingo");
        assertEquals(cdb.getArea(c_bingo), 1.0, 0.0001);
        TextUi.println(c_bingo.getDocumentation());
        r.exec("A w_c_bingo / exists.");
        // now create another with a specific area
        r.exec("$Create Concept c_bingo");
        // check if it exists now
        r.exec("$Create Concept c_biiingo with area = 0.05");
        Concept c_biiingo = cdb.getConcept("c_biiingo");
        assertEquals(cdb.getArea(c_biiingo), 0.05, 0.0001);
        // make them overlap
        assertEquals(0.0, cdb.getOverlap(c_bingo, c_biiingo), 0.0);
        r.exec("$Create ConceptOverlap c_biiingo overlaps c_bingo with 1.0");
        assertEquals(0.05, cdb.getOverlap( c_bingo, c_biiingo), 0.0);
        r.exec("The w_c_biiingo / exists.");
        // the for impact
        r.exec("$Create Concept c_lasvegas");
        r.exec("$Create ConceptImpact c_bingo impacts c_lasvegas with 0.8");
        Concept c_lasvegas = cdb.getConcept("c_lasvegas");
        assertEquals(0.8, cdb.getImpact(c_bingo, c_lasvegas), 0.001);
        r.exec("A w_c_biiingo #two/ exists.");
        r.exec("The #two / is-a / w_c_bingo.");
        r.exec("$DebugHere");
        //r.exec("The w_c_lasvegas / exists.");        
        // 
        //r.exec("The w_c_lasvegas / exists.");        
        r.exec("$Create ConceptWord lasvegas = [c_lasvegas]");
        r.exec("A lasvegas / exists.");
        TestHelper.testDone();
    }

    
    /**
     * Tests for the specification of concepts from Xapi
     * 
     * @param xapiText
     * @throws ParseException
     */
    @Test
    public void testIncludeCore() {
        String description = "The inclusion of the core domain";
        TestHelper.testStart(description);
        Agent agent = new Agent("Johnny");
        Runner r = new Runner(agent);
        // r.exec("$Include 'file:../Xapagy-1.2/src/com/xapagy/domain/Core-Core.xapi'");
        //r.exec("$Include 'Core-Core'");
        r.exec("$Include  IfNotDefined Core-Core 'Core-Core'");
        TestHelper.testDone();
    }
    
    
    /**
     * Tests for the specification of concepts from Xapi
     * 
     * @param xapiText
     * @throws ParseException
     */
    //@Test
    public void testMarkedInclude() {
        String description = "The inclusion of the core domain";
        TestHelper.testStart(description);
        Agent agent = new Agent("Johnny");
        Runner r = new Runner(agent);
        // r.exec("$Include 'file:LotsOfPrints.xapi'");
        r.exec("$Include From 'Here' 'file:LotsOfPrints.xapi'");
        TestHelper.testDone();
    }
    
    
    /**
     * Tests for the print representation
     * 
     * @param xapiText
     * @throws ParseException
     */
    @Test
    public void testPrint() {
        String description = "Printing from Xapi";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.exec("$Observer Add 'default' Triggered-at 'always' Executing 'Observe'");
        r.exec("$Print 'Hello world!'");
        r.exec("A w_c_bai20 #two/ exists.");
        r.exec("$Print Trace");
        TestHelper.testDone();
    }

}

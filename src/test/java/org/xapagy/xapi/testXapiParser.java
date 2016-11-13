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
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.prettyprint.PrettyPrint;
import org.xapagy.xapi.reference.XapiReference;
import org.xapagy.xapi.reference.XapiReference.XapiPositionInParent;
import org.xapagy.xapi.reference.XrefStatement;
import org.xapagy.xapi.reference.XrefToInstance;
import org.xapagy.xapi.reference.XrefVerb;

/**
 * Tests the parser
 * 
 * @author Ladislau Boloni
 * Created on: Aug 10, 2010
 */
public class testXapiParser {

    
    /**
     * Debug a bug pointed out by Sivi, the parser accepts this
     * because it interprets it as an S-V and ignores the rest.
     * 
     * @throws XapiParserException 
     */
    //@Test
    public void testSiviFound() throws XapiParserException {
        String description = "Parser accepts ";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        String text = "'Hector' / wa_v_av10 // I / has / a friend tons.";
        XrefStatement xst =
                (XrefStatement) r.agent.getXapiParser().parseLine(text);
        TextUi.println(PrettyPrint.ppDetailed(xst, r.agent));
        // Assert.assertTrue(xst.getSubject().getType() == XapiReference.XapiReferenceType.REF_GROUP);
        Assert.fail();
        TestHelper.testDone();
    }

    
    
    /**
     * Tests whether group references (with +) are correctly parsed
     * @throws XapiParserException 
     */
    @Test
    public void testGroup() throws XapiParserException {
        String description = "Correct parsing of group reference";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        XrefStatement xst =
                (XrefStatement) r.agent.getXapiParser().parseLine(
                        "'Achilles' + 'Hector'/ exists.");
        // TextUi.println(PrettyPrint.ppDetailed(entry.getValue(), r.agent));
        Assert.assertTrue(xst.getSubject().getType() == XapiReference.XapiReferenceType.REF_GROUP);
        TestHelper.testDone();
    }

    /**
     * Tests whether the parsing of an isolated noun phrase works. This is
     * important because it will help a lot of other stuff
     * @throws XapiParserException 
     */
    @Test
    public void testParseNounPhrase() throws XapiParserException {
        String description = "Parsing of an isolated noun phrase";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        XapiParser xp = r.agent.getXapiParser();
        XrefToInstance xtoi;
        // direct reference
        xtoi = xp.parseNounPhrase("\"Achilles\"", XapiPositionInParent.SUBJECT);
        Assert.assertTrue(xtoi.getType() == XapiReference.XapiReferenceType.REF_DIRECT);
        // group
        xtoi =
                xp.parseNounPhrase("\"Achilles\" + \"Hector\"",
                        XapiPositionInParent.SUBJECT);
        Assert.assertTrue(xtoi.getType() == XapiReference.XapiReferenceType.REF_GROUP);
        // relational
        xtoi =
                xp.parseNounPhrase("\"Achilles\" -- of -- \"Hector\"",
                        XapiPositionInParent.SUBJECT);
        Assert.assertTrue(xtoi.getType() == XapiReference.XapiReferenceType.REF_RELATIONAL);
        TestHelper.testDone();
    }

    /**
     * Tests whether the parsing of an isolated noun phrase works. This is
     * important because it will help a lot of other stuff
     * @throws XapiParserException 
     */
    @Test
    public void testParseVerbPhrase() throws XapiParserException {
        String description = "Parsing of an isolated verb phrase";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        XapiParser xp = r.agent.getXapiParser();
        XrefVerb xtov;
        // direct reference
        xtov = xp.parseVerbPhrase("exists");
        TextUi.println(xtov.getVo().toString());
        // implicit definition
        xtov = xp.parseVerbPhrase("wa_v_av10");
        TextUi.println(xtov.getVo().toString());

        // Assert.assertTrue(xtoi.getType() ==
        // XapiReference.XapiReferenceType.REF_DIRECT);
        TestHelper.testDone();
    }

    /**
     * Tests whether an quote statement is parsed correctly
     * @throws XapiParserException 
     * 
     */
    @Test
    public void testQuote() throws XapiParserException {
        String description = "Correct parsing of quote statements";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        XrefStatement statement =
                (XrefStatement) r.agent.getXapiParser().parseLine(
                        "A 'John' / says in scene // A 'Billy' / exists.");
        // r.printOn = true;
        r.print(statement);
        Assert.assertTrue(statement.getViType().equals(ViType.QUOTE));
        XrefStatement stat2 = (XrefStatement) statement.getQuote();
        Assert.assertTrue(stat2.getViType().equals(ViType.S_V));
        r.print(stat2);
        TestHelper.testDone();
    }

    /**
     * Tests whether the relational references are correctly parsed
     * @throws XapiParserException 
     */
    @Test
    public void testRelational() throws XapiParserException {
        String description = "Correct parsing of relational reference";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        XrefStatement xst =
                (XrefStatement) r.agent
                        .getXapiParser()
                        .parseLine(
                                "'Achilles' / wa_v_av40 / the w_c_bai21 -- has -- 'Hector'.");
        // TextUi.println(PrettyPrint.ppDetailed(xst.getObject(), r.agent));
        Assert.assertTrue(xst.getObject().getType() == XapiReference.XapiReferenceType.REF_RELATIONAL);
        TestHelper.testDone();
    }

    /**
     * Tests whether VO labels are correctly assigned
     * @throws XapiParserException 
     */
    @Test
    public void testVoLabel() throws XapiParserException {
        String description = "Correct parsing of VO labels";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        XapiParser xp = r.agent.getXapiParser();
        XrefStatement xst =
                (XrefStatement) xp
                        .parseLine("'Achilles' / wa_v_av40 #A / 'Hector'.");
        VerbOverlay vo = xst.getVerb().getVo();
        TextUi.println(PrettyPrint.ppDetailed(vo, r.agent));
        Assert.assertTrue(vo.getLabels().contains("#XNS_0.A"));
        XrefStatement xst2 =
                (XrefStatement) xp
                        .parseLine("'Achilles' / wa_v_av40 / 'Hector'.");
        VerbOverlay vo2 = xst2.getVerb().getVo();
        Assert.assertTrue(vo2.getLabels().isEmpty());
        TestHelper.testDone();

    }

}

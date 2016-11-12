/*
   This file is part of the Xapagy project
   Created on: Dec 23, 2014
 
   org.xapagy.xapi.testMacroCreateLink
 
   Copyright (c) 2008-2013 Ladislau Boloni
 */

package org.xapagy.xapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.concepts.Hardwired;
import org.xapagy.debug.Runner;
import org.xapagy.instances.VerbInstance;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.Formatter;
import org.xapagy.xapi.KludgeCreateLink.CreateLinkSpec;

/**
 * @author Ladislau Boloni
 *
 */
public class testCreateLink {

    @Test
    public void testParsing() {
        String description = "Test the parsing of a CreateLink macro parameters";
        TestHelper.testStart(description);
        Formatter fmt = new Formatter();
        // a correct parse
        CreateLinkSpec cls = KludgeCreateLink.parseCreateLink("successor { #A } ==> { #B }");
        fmt.add(cls.toString());
        // and incorrect parse
        boolean fine = false;
        try {
        KludgeCreateLink.parseCreateLink("successor { #A } ==> { #B ");
        } catch(Error e) {
            // got the error, it is fine!!
            fmt.add(e.getMessage() + " but it is fine, this is what we expected!");
            fine = true;
        }
        if (!fine) {
            fail("Should have catched this");
        }
        TextUi.println(fmt.toString());
        TestHelper.testDone();
    }

    @Test
    public void testInterpretLinkName() {
        String description = "Test the parsing of a CreateLink macro parameters";
        TestHelper.testStart(description);        
        Formatter fmt = new Formatter();
        String linkName = KludgeCreateLink.interpretLinkName("predecessor");
        assertEquals(Hardwired.LINK_PREDECESSOR, linkName);
        boolean fine = false;
        try {
            linkName = KludgeCreateLink.interpretLinkName("predecessorxxx");
        } catch(Error e) {
            fmt.add(e.getMessage() + " but it is fine, this is what we expected!");
            fine = true;
        }
        if (!fine) {
            fail("Should have catched this");
        }
        TextUi.println(fmt.toString());
        TestHelper.testDone();
    }

    
    @Test
    public void testTheKludge() {
        String description =
                "Test that the CreateLink kludge indeed creates a link";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.exec("$CreateScene #Troy CloseOthers With Instances w_c_bai20 'Hector', w_c_bai21 'Achilles'");

        VerbInstance vi1 = r.exac("'Hector' / wa_v_av1 / 'Achilles'.");
        VerbInstance vi2 = r.exac("'Achilles' / wa_v_av2 / 'Hector'.");
        double time = r.agent.getTime();
        r.exec("$CreateLink 'Summarization_Begin' { 'wa_v_av2' } ==> { 'wa_v_av1' }");
        assertEquals(0.0, r.agent.getTime() - time, 0.0);
        // test the link mesh
        r.ah.linkedBy(Hardwired.LINK_SUMMARIZATION_BEGIN, vi2, vi1);
        r.exec("$CreateLink 'Summarization_Close' { 'wa_v_av1' } ==> { 'wa_v_av2' } 0.1");
        assertEquals(0.0, r.agent.getTime() - time, 0.0);
        r.ah.linkedBy(Hardwired.LINK_SUMMARIZATION_CLOSE, vi1, vi2);
        // r.ah.linkedBy(ViLinkDB.SUCCESSOR, vi2, viskip);
        // WebGui.startWebGui(r.agent);
        // TextUi.enterToTerminate();
        TestHelper.testDone();
    }    
}

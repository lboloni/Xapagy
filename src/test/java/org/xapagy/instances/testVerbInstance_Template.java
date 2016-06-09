/*
   This file is part of the Xapagy project
   Created on: Jul 1, 2014
 
   org.xapagy.instances.testVerbInstance_Template
 
   Copyright (c) 2008-2013 Ladislau Boloni
 */

package org.xapagy.instances;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.debug.Runner;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.prettyprint.Formatter;
import org.xapagy.ui.prettyprint.PrettyPrint;
import org.xapagy.xapi.XapiParser;
import org.xapagy.xapi.XapiParserException;

/**
 * 
 * VIs which have either missing or "new" components are called templates. A
 * typical application of these are as the virtual heads of HSLs.
 * 
 * These series of tests verify the ways in which a template VI is constructed and modified.
 * 
 * @author Ladislau Boloni
 *
 */
public class testVerbInstance_Template {

    @Test
    public void test() throws XapiParserException {
        String description =
                "Experiments with Template VIs";
        Formatter fmt = new Formatter();
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.createAabConcepts();        
        XapiParser xp = r.agent.getXapiParser();
        
        r.exec("$CreateScene #one CloseOthers AddSummary With Instances w_c_bai20 'Hector', w_c_bai21 'Achilles'");
        VerbInstance vi = r.exac("'Achilles'/ #act wa_v_av40 / 'Hector'.");
        Instance instHector = vi.getSubject();
        
        // create one
        VerbOverlay verbs = xp.parseVo("wa_v_av10");
        VerbInstance vit = VerbInstance.createViTemplate(ViType.S_V_O, verbs);
        fmt.add("SVO after creation, only verb resolved");
        fmt.addIndented(PrettyPrint.ppDetailed(vit, r.agent));
        // set a new component for subject
        ConceptOverlay co = xp.parseCo("w_c_bai10");
        vit.setNewPart(ViPart.Subject, co);
        fmt.add("SVO after setting the subject to be new");
        fmt.addIndented(PrettyPrint.ppDetailed(vit, r.agent));
        // resolve the object to be Hector
        vit.setResolvedPart(ViPart.Object, instHector);
        fmt.add("SVO after setting the object to be Hector");
        fmt.addIndented(PrettyPrint.ppDetailed(vit, r.agent));
        
        // printing what we found
        TextUi.print(fmt.toString());
        TestHelper.testDone();
    }
    
}

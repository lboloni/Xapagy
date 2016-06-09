/*
   This file is part of the Xapagy project
   Created on: Sep 24, 2012
 
   org.xapagy.autobiography.testABStory
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.autobiography;

import org.junit.Assert;
import org.junit.Test;

import org.xapagy.TestHelper;
import org.xapagy.agents.Agent;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.prettyprint.Formatter;
import org.xapagy.ui.prettyprint.PrettyPrint;

/**
 * @author Ladislau Boloni
 * 
 */
public class testABStory {

    @Test
    public void testConstruction() {
        String description =
                "Tests the construction of a story using the ABStory class";
        TestHelper.testStart(description);
        Agent agent = new Agent();
        Formatter fmt = new Formatter();
        ABStory abs = new ABStory();
        abs.add("A man 'Hector'/ exists.");
        abs.add("A man 'Achilles'/ exists.");
        abs.add("'Achilles'/ hits / 'Hector'.");
        abs.add("'Hector'/ strikes / 'Achilles'.");
        abs.add("'Achilles'/ cuts / 'Hector'.");
        abs.add("'Hector'/ kicks / 'Achilles'.");
        fmt.add("ABStory.toString");
        fmt.addIndented(abs);
        fmt.add("ABStory prettyprint detailed");
        fmt.addIndented(PrettyPrint.ppDetailed(abs, agent));
        int lineCuts = abs.find("cuts");
        Assert.assertTrue(lineCuts == 4);
        ABStory abs2 = abs.substory(2, 5);
        fmt.add("Substory 2 - 5");
        fmt.addIndented(PrettyPrint.ppDetailed(abs2, agent));
        abs2.deleteLine("cuts");
        fmt.add("Substory 2 - 5, cuts deleted");
        fmt.addIndented(PrettyPrint.ppDetailed(abs2, agent));
        abs2.insertAfter("strikes", "'Mungo' / strikes / 'Achilles'.");
        fmt.add("Substory 2 - 5, cuts deleted, strikes inserted after strikes");
        fmt.addIndented(PrettyPrint.ppDetailed(abs2, agent));
        // TextUi.println(fmt);
        TestHelper.testDone();
    }

    
    @Test 
    public void testMultiline() {
        String text = "one two three\nfour five\nsix seven\n\neight ";
        ABStory abs = new ABStory(text);
        TextUi.println(abs);
    }
    
    
    /**
     * Tests the move function if it does what it supposed to do
     */
    @Test
    public void testMove() {
        String description = "Tests the move function for various values";
        TestHelper.testStart(description);
        Formatter fmt = new Formatter();
        ABStory abs = new ABStory();
        for (int i = 0; i != 7; i++) {
            abs.add("line" + i);
        }
        fmt.add("Original");
        fmt.addIndented(abs);
        // test 1 - moving 3 to 4
        ABStory test1 = new ABStory(abs);
        test1.move(3, 4);
        fmt.add("Test 1");
        fmt.addIndented(test1);
        // test 2 - moving 3 to 1
        ABStory test2 = new ABStory(abs);
        test2.move(3, 1);
        fmt.add("Test 2");
        fmt.addIndented(test2);
        // test 3 - moving 1 to 1
        ABStory test3 = new ABStory(abs);
        test3.move(3, 3);
        fmt.add("Test 3");
        fmt.addIndented(test3);

        TextUi.println(fmt.toString());
        TestHelper.testDone();
    }

    /**
     * Tests the substitute functions
     */
    @Test
    public void testSubstitution() {
        String description = "Tests the substitution function";
        TestHelper.testStart(description);
        Formatter fmt = new Formatter();
        ABStory abs = new ABStory();
        abs.add("A I1 / V1.");
        abs.add("A I2/ V1.");
        abs.add("I1 / V2 / I2.");
        abs.add("I2 / V3 / I1.");
        fmt.add("ABStory.toString");
        fmt.addIndented(abs);
        abs.subs("I", "'Hector'", "'Achilles'");
        fmt.add("ABStory substituted instances");
        fmt.addIndented(abs);
        abs.subs("V", "exists", "hits", "kicks");
        fmt.add("ABStory substituted verbs");
        fmt.addIndented(abs);
        TextUi.println(fmt);
        TestHelper.testDone();
    }
}

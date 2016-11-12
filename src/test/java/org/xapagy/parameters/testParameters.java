/*
   This file is part of the Xapagy project
   Created on: Apr 18, 2014
 
   org.xapagy.parameters.testParameters
 
   Copyright (c) 2008-2013 Ladislau Boloni
 */

package org.xapagy.parameters;

import org.junit.Assert;
import org.junit.Test;

import org.xapagy.TestHelper;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.Formatter;

/**
 * @author Ladislau Boloni
 * 
 */
public class testParameters {

    /**
     * Tests the operations of setting and getting
     */
    @Test
    public void operations() {
        String description = "Operations of parameters";
        TestHelper.testStart(description);
        Formatter fmt = new Formatter();
        double value;
        //
        // create the parameter
        //
        Parameters p = new Parameters();
        p.addParam("A_GENERAL", "group", "name", 3.0, null);
        value = p.get("A_GENERAL", "group", "name");
        Assert.assertEquals(3.0, value, 0.0);
        fmt.is("initial GENERAL-group-name", value);
        //
        // setting the parameter
        //
        p.set("A_GENERAL", "group", "name", 4.0);
        value = p.get("A_GENERAL", "group", "name");
        Assert.assertEquals(4.0, value, 0.0);
        fmt.is("initial GENERAL-group-name", value);
        // TextUi.print(fmt.toString());
        TestHelper.testDone();
    }

    /**
     * Tests the operations of setting and getting
     */
    @Test
    public void print() {
        String description = "Printing the parameters";
        TestHelper.testStart(description);
        Formatter fmt = new Formatter();
        Parameters p = new Parameters();
        p.addParam("A_GENERAL", "g1", "name1", 4.0,
                "Very interesting, \nvery very interesting");
        p.addParam("A_GENERAL", "g1", "name2", 5.0, null);
        p.addParam("A_GENERAL", "g2", "nx", 5.0, null);
        p.addParam("A_GENERAL", "g2", "ny", 6.0, null);
        p.addParam("A_DEBUG", "dgr1", "boo", 14.0, null);
        p.addParam("A_DEBUG", "dgr1", "foo", 15.0, null);
        p.addParam("A_DEBUG", "dgr1", "moo", 16.0, null);
        p.addParam("A_DEBUG", "dgr1", "woo", 17.0, null);

        fmt.add(p.toString());
        TextUi.print(fmt.toString());
        TestHelper.testDone();
    }

}

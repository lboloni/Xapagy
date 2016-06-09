/*
   This file is part of the Xapagy project
   Created on: Nov 30, 2015
 
   org.xapagy.testXapagy
 
   Copyright (c) 2008-2013 Ladislau Boloni
 */

package org.xapagy;

/**
 * Test the main commandline 
 * 
 * @author Ladislau Boloni
 *
 */
public class testXapagy {

    //@Test
    public void testNoParameters() {
        Xapagy.main();
    }
    
    //@Test
    public void testHelp() {
        Xapagy.main("--help");
    }
    
    //@Test
    public void testSimple() {
        Xapagy.main("file:stories/LotsOfPrints.xapi");
    }
}

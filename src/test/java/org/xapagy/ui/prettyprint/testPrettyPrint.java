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
package org.xapagy.ui.prettyprint;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.ui.formatters.Formatter;
import org.xapagy.verbalize.VmInstanceReference;

/**
 * @author Ladislau Boloni
 * Created on: Dec 28, 2011
 */
public class testPrettyPrint {

    @Test
    public void testFindFormatter() {
        String description = "Tests whether it prints based on the ancestor";
        TestHelper.testStart(description);
        Formatter fmt = new Formatter();
        String pp = null;
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.printOn = true;
        // r.tso.trace();
        // r.tso.setCompactTraceIncludingVerbalization(true);
        r.exec("$CreateScene #Scene1 CloseOthers With Instances 'Jenny'");

        // Trying to print a VI
        VerbInstance vi = r.exac("'Jenny' / is-a / w_c_bai20.");
        pp = PrettyPrint.ppDetailed(vi, r.agent);
        fmt.add("Printing a VI");
        fmt.addIndented(pp);
        
        // Trying to print an instance
        Instance instJenny = vi.getSubject();
        pp = PrettyPrint.ppDetailed(instJenny, r.agent);
        fmt.add("Printing an instance");
        fmt.addIndented(pp);
        
        // Try to print a VmInstance reference
        VmInstanceReference vir =
                r.agent.getVerbalMemory().getVmInstanceReferences()
                        .get(instJenny).get(1);
        pp = PrettyPrint.ppDetailed(vir.getXapiReference(), r.agent);
        fmt.add("Printing a VM instance reference");
        fmt.addIndented(pp);
        
        // TestHelper.verbose = true;
        TestHelper.printIfVerbose(fmt.toString());
        TestHelper.testDone();
    }

    @Test
    public void testInstancePrint() {
        String description = "Tests the printing of an instance";
        TestHelper.testStart(description);
        Formatter fmt = new Formatter();
        String pp = null;
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.printOn = true;
        // r.tso.trace();
        // r.tso.setCompactTraceIncludingVerbalization(true);
        r.exec("$CreateScene #Scene1 CloseOthers With Instances 'Jenny'");
        // Trying to print a VI
        VerbInstance vi = r.exac("'Jenny' / is-a / w_c_bai20.");
        Instance instJenny = vi.getSubject();
        // Concise printing of an instance
        pp = PrettyPrint.ppConcise(instJenny, r.agent);
        fmt.add("Concise printing an instance");
        fmt.addIndented(pp);
        
        // Detailed printing on an instance
        pp = PrettyPrint.ppDetailed(instJenny, r.agent);
        fmt.add("Detailed printing an instance");
        fmt.addIndented(pp);
        
        TestHelper.verbose = true;
        TestHelper.printIfVerbose(fmt.toString());
        TestHelper.testDone();
    }
    
    
}

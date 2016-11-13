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

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.debug.Runner;
import org.xapagy.instances.VerbInstance;

/**
 * @author Ladislau Boloni
 * Created on: Feb 16, 2016
 */
public class testScripting {

    /**
     * This test simply checks whether the Javascript had been passed to the
     * system
     */
    @Test
    public void testExecution() {
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.exec("!!print('Hello world! ' + agent);");
        r.exec("$BeginScript");
        r.exec("print('One ' + agent);");
        r.exec("print('Two ' + agent);");
        r.exec("$EndScript");
    }

    /**
     * This test simply checks whether the Javascript had been passed to the
     * system
     */
    @Test
    public void testRef() {
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.exec("$CreateScene #first CloseOthers With Instances w_c_bai20 'Hector' #H, w_c_bai20 'Achilles' #A");
        @SuppressWarnings("unused")
        VerbInstance vi1 =
                r.exac("'Achilles' / #labeled wa_v_av40 / 'Hector'.");
        r.exec("!!print(ref.ViByLabelFromFocus('#labeled'));");
    }

}

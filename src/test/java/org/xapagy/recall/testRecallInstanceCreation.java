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
package org.xapagy.recall;

import java.util.List;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;
import org.xapagy.instances.VerbInstance;
import org.xapagy.ui.observers.AbstractAgentObserver.TraceWhat;

/**
 * @author Ladislau Boloni
 * Created on: Jun 22, 2011
 */
public class testRecallInstanceCreation {
    public static final String outputDirName = "output";

    /**
     * 
     * Trying it out with more concrete values, as the above one with the story
     * generator has too w_c_bai20y choices.
     * 
     */
    @Test
    public void test() {
        String description = "Recall - instance creation";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.exec("$Include 'P-FocusOnly'");
        // r.tso.setTrace();
       r.exec("$CreateScene #One CloseOthers With Instances w_c_bai20 'Hector', w_c_bai20 'Achilles', w_c_bai21");
        r.exec("'Achilles' / wa_v_av40 / 'Hector'.");
        r.exec("'Hector'/ wa_v_av41 / 'Achilles'.");
        r.exec("'Achilles'/ wa_v_av42 / 'Hector'.");
        r.exec("'Achilles'/ wa_v_av43 / the w_c_bai21.");
        r.exec("----");
        r.exec("$Include 'P-Default'");
        r.exec("$CreateScene #Two CloseOthers With Instances w_c_bai20 'Hector', w_c_bai20 'Achilles'");
        r.exec("'Achilles'/ wa_v_av40 / 'Hector'.");
        r.tso.setTrace(TraceWhat.CHOICES_DYNAMIC);
        @SuppressWarnings("unused")
        List<VerbInstance> recalls = r.exec("Scene / recall narrate.");
    }
}

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
package org.xapagy.activity.hlsmaintenance;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;
import org.xapagy.parameters.Parameters;

/**
 * @author Ladislau Boloni
 * Created on: Nov 12, 2011
 */
public class testSaHlsmUpdateSummaries {

    /**
     * Tests the creation of the summaries
     */
    @Test
    public void testAlternative() {
        String description = "Create alternative summaries";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        Parameters p = r.agent.getParameters();
        p.set("A_HLSM", "G_GENERAL",
                "N_PROCESS_SUMMARIZATION_BLOCKS", true);
        // r.tso.setTrace();
        r.exec("$CreateScene #Troy CloseOthers With Instances w_c_bai20 'Hector', w_c_bai20 'Achilles'");
        r.exec("'Hector' / wa_v_av40 / 'Achilles'.");
        r.exec("'Achilles' / wa_v_av40 / 'Hector'.");
        r.tso.ppd("FOCUS_STRUCTURED");
        TestHelper.testIncomplete();
    }

    /**
     * Tests the creation of the summaries
     */
    @Test
    public void testRepetitive() {
        String description = "SaUpdateSummaries - create repetitive summaries";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        // r.tso.setTrace();
        Parameters p = r.agent.getParameters();
        p.set("A_HLSM", "G_GENERAL",
                "N_PROCESS_SUMMARIZATION_BLOCKS", true);
        r.exec("$CreateScene #Troy CloseOthers With Instances w_c_bai20 'Hector', w_c_bai20 'Achilles'");
        r.exec("'Hector' / wa_v_av40 / 'Achilles'.");
        r.exec("'Hector' / wa_v_av40 / 'Achilles'.");
        r.tso.ppd("FOCUS_STRUCTURED");
        TestHelper.testIncomplete();
    }

}

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
package org.xapagy.verbalize;

import java.util.List;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.debug.Runner;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.Formatter;

/**
 * @author Ladislau Boloni
 * Created on: Oct 30, 2013
 */
public class testVrblzInstance {

    @Test
    public void test() {
        Formatter fmt = new Formatter();
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.exec("$CreateScene #Scene1 CloseOthers With Instances w_c_bai20, w_c_bai21");
        VerbInstance vi = r.exac("The w_c_bai20 / wa_v_av20 / the w_c_bai21.");
        // Instance inst = vi.getSubject();
        List<VrblzCandidateInstance> candidates =
                VrblzInstance.generateCandidates(r.agent, vi, ViPart.Subject);
        for (VrblzCandidateInstance cnd : candidates) {
            fmt.add(cnd.toString());
        }
        TextUi.println(fmt.toString());
    }

}

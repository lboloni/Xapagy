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
package org.xapagy.activity.focusmaintenance;

import org.junit.Assert;
import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;
import org.xapagy.instances.VerbInstance;
import org.xapagy.questions.QuestionHelper;

/**
 * @author Ladislau Boloni
 * Created on: Oct 13, 2011
 */
public class testSaFcmFoundAnswer {

    @Test
    public void testIdentifyAnswer() {
        String description = "Checks whether matched answers are linked.";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.exec("$CreateScene #Reality CloseOthers With Instances w_c_bai20, w_c_bai21");

        VerbInstance viAnswer =
                r.exac("The w_c_bai20 / wa_v_av40 / the w_c_bai21.");
        VerbInstance viNotAnswer =
                r.exac("The w_c_bai20 / wa_v_av41 / the w_c_bai21.");
        VerbInstance viQuestion = r.exac("Wh / wa_v_av40 / the w_c_bai21?");
        Assert.assertTrue(QuestionHelper.decideQuestionAnswerLinkExists(
                r.agent, viQuestion, viAnswer));
        Assert.assertTrue(!QuestionHelper.decideQuestionAnswerLinkExists(
                r.agent, viQuestion, viNotAnswer));
        TestHelper.testDone();
    }

}

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
package org.xapagy.ui.prettygeneral;

import org.xapagy.agents.Agent;
import org.xapagy.reference.rrState;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * @author Ladislau Boloni
 * Created on: Apr 15, 2014
 */
public class xwRrState {

    public static String xwDetailed(IXwFormatter xwf, rrState rrs, Agent agent) {
        xwf.addLabelParagraph("RrState");
        xwf.indent();
        xwf.is("compositionParent", rrs.getCompositionParent());
        xwf.is("phase", rrs.getPhase());
        xwf.is("justification", rrs.getJustification());
        xwf.is("scoreIncompatibility", rrs.getScoreIncompatibility());
        xwf.is("scoreSimilarity", rrs.getScoreSimilarity());
        xwf.is("scoreBias", rrs.getScoreBias());
        xwf.is("overallScore (calculated)", rrs.getOverallScore());
        xwf.deindent();
        return xwf.toString();
    }
}

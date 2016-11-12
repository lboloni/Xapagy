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
package org.xapagy.concepts.operations;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;

import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.Formatter;
import org.xapagy.util.FileWritingUtil;

/**
 * Generate a Latex file of all the operations, intended to be introduced in the
 * domain engineering report
 * 
 * @author Ladislau Boloni
 * Created on: Feb 1, 2013
 */
public class GenerateLatex {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Formatter fmt = new Formatter();
        SimpleEntry<String, String> val = null;
        // incompatibility
        testIncompatibilityCo ti = new testIncompatibilityCo();
        ti.doTest = false;
        val = ti.incompatibilityExperiments();
        fmt.add("\\subsection*{Incompatibility}");
        fmt.add(val.getValue());
        fmt.add("\\bigskip");
        // similarity
        testCoverageCo ts = new testCoverageCo();
        ts.doTest = false;
        val = ts.coverageExperiments();
        fmt.add("\\subsection*{Similarity}");
        fmt.add(val.getValue());
        fmt.add("\\bigskip");
        // referability
        testResolutionConfidence tr = new testResolutionConfidence();
        tr.doTest = false;
        val = tr.resolutionConfidenceExperiments();
        fmt.add("\\subsection*{Referability}");
        fmt.add(val.getValue());
        fmt.add("\\bigskip");
        //
        String latex = fmt.toString();
        TextUi.println(latex);
        FileWritingUtil.writeToTextFile(
                new File("ConceptOperationsTables.tex"), latex);

    }

}

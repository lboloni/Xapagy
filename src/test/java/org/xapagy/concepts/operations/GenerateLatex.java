/*
   This file is part of the Xapagy project
   Created on: Feb 1, 2013
 
   org.xapagy.concepts.operations.GenerateLatex
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.concepts.operations;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;

import org.xapagy.ui.TextUi;
import org.xapagy.ui.prettyprint.Formatter;
import org.xapagy.util.FileWritingUtil;

/**
 * Generate a Latex file of all the operations, intended to be introduced in the
 * domain engineering report
 * 
 * @author Ladislau Boloni
 * 
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

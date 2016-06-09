/*
   This file is part of the Xapagy project
   Created on: Aug 6, 2013
 
   org.xapagy.ui.matlab.MatlabUtil
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.matlab;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Ladislau Boloni
 * 
 */
public class MatlabUtil {

    public static int ITEM_IN_A_LINE = 10;

    /**
     * Generates the basic graph prefix
     * 
     * @param buffer
     */
    public static String generateBasicGraphPrefix(int figureCount, int xpos,
            int ypos, int xsize, int ysize) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("figure" + figureCount + " = figure('Position', [" + xpos
                + " " + ypos + " " + xsize + " " + ysize + "]);\n");

        buffer.append("axes" + figureCount + " = axes('Parent',figure"
                + figureCount + ");\n");
        // buffer.append("legend('show')\n");
        buffer.append("hold('all')\n");
        return buffer.toString();
    }

    /**
     * Generates matlab text for basic graph properties
     */
    public static String generateBasicGraphProperties(
            AbstractGraphDescription graphDescription) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("hold on\n");
        if (graphDescription.getTitle() != null) {
            buffer.append("title('" + graphDescription.getTitle() + "')\n");
        }
        buffer.append("hold on\n");
        buffer.append("xlabel('" + graphDescription.getXLabel() + "')\n");
        buffer.append("hold on\n");
        buffer.append("ylabel('" + graphDescription.getYLabel() + "')\n");
        return buffer.toString();
    }

    /**
     * Generates a file for processing all the figures in a directory and
     * convert them to pdf...
     * 
     * -Note: this had been originally developed for Octave, to be seen whether
     * it works for matlab or not.
     * 
     * @throws IOException
     */
    public static void generateProcessAll(File dir) throws IOException {
        OctaveUtil.generateProcessAll(dir);
    }

    /**
     * Returns a two dimensional matrix description in Matlab
     * 
     * The assumption here is that the first is the x and the second is the y
     * 
     * @param varName
     * @param z
     * @return
     */
    public static String getMatlabMatrix(String varName, double[][] values) {
        final StringBuffer buffer = new StringBuffer(varName + " = [");
        int ysize = values[0].length;
        int xsize = values.length;
        for (int i = 0; i < xsize; i++) {
            int count = 0;
            for (int j = 0; j < ysize; j++) {
                buffer.append(values[i][j] + " ");
                count++;
                if (count % MatlabUtil.ITEM_IN_A_LINE == 0) {
                    buffer.append("...\n       ");
                }
            }
            buffer.append("\n");
        }

        buffer.append("];\n");
        return buffer.toString();
    }

    /**
     * Returns a one dimensional vector description in Matlab for a List<String>
     * of form {'one','two','three'}
     */
    public static String getMatlabVector(List<String> values) {
        final StringBuffer buffer = new StringBuffer("{");
        for (int i = 0; i != values.size(); i++) {
            String s = values.get(i);
            buffer.append("'" + s + "'");
            if (i != values.size() - 1) {
                buffer.append(",");
            }
        }
        buffer.append("}");
        return buffer.toString();
    }

    /**
     * Returns a one dimensional vector description in Matlab.
     * 
     * @return
     */
    public static String getMatlabVector(String varName, double[] values) {
        final StringBuffer buffer = new StringBuffer();
        if (varName == null) {
            buffer.append("[");
        } else {
            buffer.append(varName + " = [");
        }
        int count = 0;
        for (double element : values) {
            buffer.append(element + " ");
            count++;
            if (count % MatlabUtil.ITEM_IN_A_LINE == 0) {
                buffer.append("...\n       ");
            }
        }
        buffer.append("]");
        if (varName != null) {
            buffer.append("\n");
        }
        return buffer.toString();
    }

    /**
     * Returns a one dimensional vector description in Matlab for a List<Double>
     */
    public static String getMatlabVector(String varName,
            List<? extends Number> values) {
        final StringBuffer buffer = new StringBuffer();
        if (varName == null) {
            buffer.append("[");
        } else {
            buffer.append(varName + " = [");
        }
        int count = 0;
        for (Number n : values) {
            buffer.append(n + " ");
            count++;
            if (count % MatlabUtil.ITEM_IN_A_LINE == 0) {
                buffer.append("...\n       ");
            }
        }
        buffer.append("]");
        if (varName != null) {
            buffer.append("\n");
        }
        return buffer.toString();
    }

}
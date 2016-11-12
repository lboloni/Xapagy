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
package org.xapagy.ui;

/**
 * Various progress bars in text mode
 * 
 * @author Ladislau Boloni
 * 
 */
public class TextProgress {

    /**
     * Returns a progress representation of a number of the 0 .. 1 range
     * 
     * @param val
     * @return
     */
    public static String progress(double value) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(TextProgress.progressBar(value, 0.01, 0.1, 0.01));
        buffer.append(TextProgress.progressBar(value, 0.1, 1, 0.1));
        buffer.append(" ");
        buffer.append(String.format("%5.4f", value));
        return buffer.toString();
    }

    /**
     * Returns a progress representation of a number of the 0 .. 1 range
     * 
     * @param val
     * @return
     */
    public static String progressBar(double value, double low, double high,
            double step) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("(");
        for (double val = low; val <= high; val = val + step) {
            if (value > val) {
                buffer.append("#");
            } else {
                buffer.append(".");
            }
        }
        buffer.append(")");
        return buffer.toString();
    }

    /**
     * Returns a representation where smaller numbers are pushed to the side 0.9
     * ..0.7 ........0.1
     * 
     * @param val
     * @return
     */
    public static String shiftSmaller(double value) {
        StringBuffer buffer = new StringBuffer();
        double val = value;
        while (val < 0.8) {
            buffer.append(".");
            val = val + 0.1;
        }
        return buffer.toString();
    }

}

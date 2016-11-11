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
package org.xapagy.ui.matlab;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import org.xapagy.util.FileWritingUtil;

/**
 * @author Ladislau Boloni
 * Created on: Aug 6, 2013
 */
public abstract class AbstractGraphDescription implements Serializable {
    public static final String lineType[] = { "-", "--", ":", "-." };

    private static final long serialVersionUID = 359600216019599477L;

    public static final String sign[] = { "*", "+", "s", "x", "d", ".", "o",
            "v", "^", "<", ">", "p", "h", };

    /**
     * Generates the linestyle component
     * 
     * @param count
     * @return
     */
    public static String getLineStyle(int count) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(",'LineStyle','"
                + AbstractGraphDescription.lineType[count
                        % AbstractGraphDescription.lineType.length] + "'");
        if (count >= AbstractGraphDescription.lineType.length) {
            buffer.append(",'LineWidth',2");
        }
        return buffer.toString();
    }

    private String title = "";
    private String xLabel = "";
    private String yLabel = "";

    private String zLabel = "";

    /**
     * Constructor, initialize all the values
     * 
     * @param title
     * @param xLabel
     * @param yLabel
     * @param zLabel
     */
    public AbstractGraphDescription(String title, String xLabel, String yLabel,
            String zLabel) {
        this.title = title;
        this.xLabel = xLabel;
        this.yLabel = yLabel;
        this.zLabel = zLabel;
    }

    /**
     * Generates a graph and writes it to a file
     * 
     * @param file
     * @throws IOException
     */
    public void generate(File file, int xsize, int ysize) throws IOException {
        String value = generate(1, 0, 0, xsize, ysize);
        FileWritingUtil.writeToTextFile(file, value);
    }

    /**
     * Generates a graph for matlab
     * 
     * @param xsize
     *            - the size of the graph
     * @param ysize
     *            - the size of the graph
     * @return
     */
    public abstract String generate(int figureCount, int xpos, int ypos,
            int xsize, int ysize);

    /**
     * Generates a graph and writes it to a file
     * 
     * @param file
     * @throws IOException
     */
    public void generateErrorBarPlot(File file, int xsize, int ysize)
            throws IOException {
        String value = generateErrorBarPlot(xsize, ysize);
        FileWritingUtil.writeToTextFile(file, value);
    }

    /**
     * @return
     */
    public String generateErrorBarPlot(int xsize, int ysize) {
        return "%%% No errorbar plot generated\n";
    }

    /**
     * Generates a graph and writes it to a file
     * 
     * @param file
     * @throws IOException
     */
    public void generateOctave(File file, int xsize, int ysize)
            throws IOException {
        String value = generateOctave(xsize, ysize);
        FileWritingUtil.writeToTextFile(file, value);
    }

    /**
     * @param xsize
     *            - the size of the graph
     * @param ysize
     *            - the size of the graph
     * @return
     */
    public abstract String generateOctave(int xsize, int ysize);

    protected String getLineStyleOctave(int count) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(",'" + count + "'");
        return buffer.toString();
    }

    public String getTitle() {
        return title;
    }

    public String getXLabel() {
        return xLabel;
    }

    public String getYLabel() {
        return yLabel;
    }

    /**
     * @return the zLabel
     */
    public String getzLabel() {
        return zLabel;
    }
}

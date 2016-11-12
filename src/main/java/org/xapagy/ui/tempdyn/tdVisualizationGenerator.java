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
package org.xapagy.ui.tempdyn;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import org.xapagy.agents.Agent;
import org.xapagy.set.EnergyColors;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.FormatTable;
import org.xapagy.ui.formatters.Formatter;
import org.xapagy.ui.formatters.LatexFormatter;

/**
 * Generates a table from the tdValues
 * 
 * @author Ladislau Boloni
 * Created on: Aug 5, 2013
 */
public class tdVisualizationGenerator {

    public enum Presentation {
        FOCUS_HORIZONTAL_LATEX_TABLE, FOCUS_HORIZONTAL_TEXT_TABLE,
        FOCUS_JFREECHART, FOCUS_MATLAB_GRAPHS, MEMORY_HORIZONTAL_LATEX_TABLE,
        MEMORY_HORIZONTAL_TEXT_TABLE, MEMORY_JFREECHART, MEMORY_MATLAB_GRAPHS
    };

    /**
     * Generates presentations for the focus and memory values of the instances
     * 
     * 
     * 
     * @param database
     * @param agent
     * @param presentation
     *            - the type of the presentation we want do
     * @param isInstance
     *            - do we want the instances (true) or VIs (false)
     * @return
     */
    public static String generateFocusMemory(tdDataBase database, Agent agent,
            Presentation presentation, boolean isInstance, double timeStep) {
        double timeStart = database.getTimeStart();
        double timeEnd = database.getTimeEnd();
        String focusEnergyColor = "";
        String amEnergyColor = "";
        if (isInstance) {
            focusEnergyColor = EnergyColors.FOCUS_INSTANCE;
            amEnergyColor = EnergyColors.AM_INSTANCE;
        } else {
            focusEnergyColor = EnergyColors.FOCUS_VI;
            amEnergyColor = EnergyColors.AM_VI;
        }
        //
        // determine ticks on the X axis
        //
        List<Double> index = new ArrayList<>();
        for (double time = timeStart; time <= timeEnd; time = time + timeStep) {
            index.add(time);
        }
        // the labels
        List<String> labels = new ArrayList<>();
        // the Y axis - focus values and memory values
        List<List<Double>> focusValues = new ArrayList<>();
        List<List<Double>> memoryValues = new ArrayList<>();
        List<tdComponent> components = null;
        if (isInstance) {
            components = database.getFocusInstances();
        } else {
            components = database.getFocusVis();
        }
        // now fill them in
        for (tdComponent component : components) {
            String label = component.getLastPrettyPrint();
            labels.add(label);
            List<Double> lineFocus = new ArrayList<>();
            List<Double> lineMemory = new ArrayList<>();
            for (Double time : index) {
                double valueFocus =
                        database.getSalience(component.getIdentifier(),
                                focusEnergyColor, time);
                lineFocus.add(valueFocus);
                double valueMemory =
                        database.getSalience(component.getIdentifier(),
                                amEnergyColor, time);
                lineMemory.add(valueMemory);
            }
            focusValues.add(lineFocus);
            memoryValues.add(lineMemory);
        }
        // now generate stuff based on this
        switch (presentation) {
        case FOCUS_HORIZONTAL_TEXT_TABLE:
            return tdVisualizationGenerator.generateHorizontalTextTable(labels,
                    index, focusValues);
        case MEMORY_HORIZONTAL_TEXT_TABLE:
            return tdVisualizationGenerator.generateHorizontalTextTable(labels,
                    index, memoryValues);
        case FOCUS_HORIZONTAL_LATEX_TABLE:
            return tdVisualizationGenerator.generateHorizontalLatexTable(
                    labels, index, focusValues);
        case MEMORY_HORIZONTAL_LATEX_TABLE:
            return tdVisualizationGenerator.generateHorizontalLatexTable(
                    labels, index, memoryValues);
        case FOCUS_MATLAB_GRAPHS:
            return tdVisualizationGenerator.generateMatlabGraphs(labels, index,
                    focusValues, "Focus");
        case MEMORY_MATLAB_GRAPHS:
            return tdVisualizationGenerator.generateMatlabGraphs(labels, index,
                    memoryValues, "Memory");
        case FOCUS_JFREECHART:
            return tdVisualizationGenerator.generateJFreeChart(labels, index,
                    focusValues);
        case MEMORY_JFREECHART:
            return tdVisualizationGenerator.generateJFreeChart(labels, index,
                    memoryValues);
        default:
            TextUi.abort("invalid presentation type: " + presentation);
            return ""; // will never get here
        }
    }

    /**
     * Generate a horizontal text table
     * 
     * @param labels
     * @param index
     * @param values
     * @return
     */
    public static String generateHorizontalLatexTable(List<String> labels,
            List<Double> index, List<List<Double>> values) {
        String format = "";
        Object header[] = new String[index.size() + 1];
        format += "|p{4.5cm}";
        header[0] = "Labels";
        for (int i = 1; i <= index.size(); i++) {
            format += "|p{0.75cm}";
            header[i] = Formatter.fmt(index.get(i - 1));
        }
        LatexFormatter lf = new LatexFormatter();
        lf.beginTabular(format);
        lf.add("\\hline");
        lf.addTableLine(header);
        lf.add("\\hline"); // now do the rest
        for (int i = 0; i != values.size(); i++) {
            Object row[] = new String[index.size() + 1];
            row[0] = labels.get(i);
            for (int j = 1; j <= index.size(); j++) {
                row[j] = Formatter.fmt(values.get(i).get(j - 1));
            }
            lf.addTableLine(row);
        }
        lf.add("\\hline");
        lf.endTabular();
        return lf.toString();
    }

    /**
     * Generate a horizontal text table
     * 
     * @param labels
     * @param index
     * @param values
     * @return
     */
    public static String generateHorizontalTextTable(List<String> labels,
            List<Double> index, List<List<Double>> values) {
        int widths[] = new int[index.size() + 1];
        String header[] = new String[index.size() + 1];
        widths[0] = 20;
        header[0] = "Labels";
        for (int i = 1; i <= index.size(); i++) {
            widths[i] = 10;
            header[i] = Formatter.fmt(index.get(i - 1));
        }
        FormatTable ft = new FormatTable(widths);
        ft.header(header);
        ft.internalSeparator();
        // now do the rest
        for (int i = 0; i != values.size(); i++) {
            Object row[] = new String[index.size() + 1];
            row[0] = labels.get(i);
            for (int j = 1; j <= index.size(); j++) {
                row[j] = Formatter.fmt(values.get(i).get(j - 1));
            }
            ft.wrappedRow(row);
        }
        ft.externalSeparator();
        return ft.toString();
    }

    /**
     * Generate a vertical list of JFreeCharts
     * 
     * @param labels
     *            - the labels for the values
     * @param index
     *            - the index
     * @param values
     * @return
     */
    public static String generateJFreeChart(List<String> labels,
            List<Double> index, List<List<Double>> values) {
        JPanel panel = new JPanel();
        // create a layout
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        SequentialGroup sgv = layout.createSequentialGroup();
        layout.setVerticalGroup(sgv);
        ParallelGroup pgh = layout.createParallelGroup();
        layout.setHorizontalGroup(pgh);
        for (int i = 0; i != labels.size(); i++) {
            XYSeries xys = new XYSeries(labels.get(i));
            for (int j = 0; j != index.size(); j++) {
                xys.add(index.get(j), values.get(i).get(j));
            }
            XYSeriesCollection xysc = new XYSeriesCollection(xys);
            JFreeChart chart =
                    ChartFactory.createXYLineChart(labels.get(i), "Time",
                            "Focus", xysc, PlotOrientation.VERTICAL, false,
                            false, false);
            chart.setBackgroundPaint(Color.white);
            chart.getTitle().setFont(new Font("Tahoma", Font.BOLD, 14));
            ChartPanel cp = new ChartPanel(chart);
            sgv.addComponent(cp);
            pgh.addComponent(cp);
        }
        JFrame frame = new JFrame();
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        return null;
    }

    /**
     * Generate a one graph per label in a single text
     * 
     * @param labels
     * @param index
     * @param values
     * @return
     */
    public static String generateMatlabGraphs(List<String> labels,
            List<Double> index, List<List<Double>> values, String plotLegend) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i != labels.size(); i++) {
            String label = labels.get(i);
            label = label.replace("_", "\\_");
            tdGraphDescription tdgr =
                    new tdGraphDescription(label, "Time", plotLegend, null,
                            index, values.get(i));
            String graph = tdgr.generate(i, 0, 220 * i, 600, 200);
            buffer.append(graph);
        }
        return buffer.toString();
    }

}

/*
   This file is part of the Xapagy project
   Created on: Aug 11, 2013
 
   org.xapagy.ui.tempdyn.tdKitchenSinkJFreeChart
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.tempdyn;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;
import java.util.AbstractMap.SimpleEntry;
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
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import org.xapagy.agents.Agent;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.parameters.Parameters;
import org.xapagy.set.EnergyColors;
import org.xapagy.ui.prettyprint.PpChoice;
import org.xapagy.ui.smartprint.SpInstance;
import org.xapagy.ui.smartprint.XapiPrint;
import org.xapagy.ui.tempdyn.tdComponent.tdComponentType;

/**
 * @author Ladislau Boloni
 * 
 */
public class GraphEvolution {

    public static List<SimpleEntry<Color, Stroke>> lineStylesColorful = null;
    public static List<SimpleEntry<Color, Stroke>> lineStylesConservative =
            null;

    static {
        GraphEvolution.initializeLineStyles();
    }

    /**
     * Generates the graph which plots the three choice scores (independent,
     * dependent and mood) for the evolution of a choice in time.
     * 
     * @param tdc
     *            - encompasses the selected choice
     * @param database
     *            - the database of values collected
     * @param agent
     * @param index
     *            - a list of time points which will be plotted on the x axis
     * @param choiceRange
     *            - the y axis will be [0, choiceRange]
     */
    public static void graphChoiceEvolution(tdComponent tdc,
            tdDataBase database, Agent agent, List<Double> index,
            double choiceRange) {
        String label = PpChoice.ppConcise(tdc.getChoice(), agent);

        // create a general purpose xy collection for jfreechart
        XYSeriesCollection xysc = new XYSeriesCollection();
        // focus and memory
        xysc.addSeries(new XYSeries("ChoiceScoreIndependent"));
        xysc.addSeries(new XYSeries("ChoiceScoreDependent"));
        xysc.addSeries(new XYSeries("ChoiceScoreMood"));
        // Fill in the values
        for (Double time : index) {
            double dtime = time;
            double valueChoiceScoreIndependent =
                    database.getChoiceScoreDependent(tdc.getIdentifier(), time);
            xysc.getSeries("ChoiceScoreIndependent").add(dtime,
                    valueChoiceScoreIndependent);
            double valueChoiceScoreDependent =
                    database.getChoiceScoreDependent(tdc.getIdentifier(), time);
            xysc.getSeries("ChoiceScoreDependent").add(dtime,
                    valueChoiceScoreDependent);
            double valueChoiceScoreMood =
                    database.getChoiceScoreDependent(tdc.getIdentifier(), time);
            xysc.getSeries("ChoiceScoreMood").add(dtime, valueChoiceScoreMood);
        }
        //
        // ok, now let us create a graph
        //
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
        //
        // the graph with the focus and the memory
        //
        XYSeriesCollection xysFM = new XYSeriesCollection();
        xysFM.addSeries(xysc.getSeries("ChoiceScoreIndependent"));
        xysFM.addSeries(xysc.getSeries("ChoiceScoreDependent"));
        xysFM.addSeries(xysc.getSeries("ChoiceScoreMood"));
        JFreeChart chart =
                ChartFactory.createXYLineChart(label + " - Choice", "Time",
                        "Value", xysFM, PlotOrientation.VERTICAL, true, false,
                        false);
        GraphEvolution.setChartProperties(chart,
                GraphEvolution.lineStylesColorful);
        ChartPanel cp = new ChartPanel(chart);
        sgv.addComponent(cp);
        pgh.addComponent(cp);
        JFrame frame = new JFrame();
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Graphs the evolution of the links of all types (PRED, SUCC, SUMMARY,
     * CONTEXT etc) between two Vis.
     * 
     * @param fromVi
     * @param toVi
     * @param tdb
     * @param agent
     * @param index
     *            - a list of time points which will be plotted on the x axis
     */
    public static void graphLinksBetweenVis(tdComponent fromVi,
            tdComponent toVi, tdDataBase tdb, Agent agent, List<Double> index) {
        String label =
                "Links between " + fromVi.getIdentifier() + " and "
                        + toVi.getIdentifier();
        // create a general purpose xy collection for jfreechart
        XYSeriesCollection xysc = new XYSeriesCollection();
        // add a series for each link type
        for (String linkName : agent.getLinks().getLinkTypeNames()) {
            XYSeries linkSeries = new XYSeries(linkName);
            xysc.addSeries(linkSeries);
            // now fill in the series with values
            for (Double time : index) {
                double dtime = time;
                double linkValue =
                        tdb.getLinkValue(fromVi.getIdentifier(),
                                toVi.getIdentifier(), linkName, time);
                linkSeries.add(dtime, linkValue);
            }
        }
        //
        // ok, now let us create a graph
        //
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
        JFreeChart chart =
                ChartFactory.createXYLineChart(label, "Time", "Value", xysc,
                        PlotOrientation.VERTICAL, true, false, false);
        GraphEvolution.setChartProperties(chart,
                GraphEvolution.lineStylesColorful);
        ChartPanel cp = new ChartPanel(chart);
        sgv.addComponent(cp);
        pgh.addComponent(cp);
        JFrame frame = new JFrame();
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Graphs which plots the evolution of all the links from a given VI. If the
     * linkType is not null, it filters based on that, otherwise, it plots all
     * the link types
     * 
     * @param fromVi
     * @param linkType
     * @param tdb
     * @param agent
     * @param index
     */
    public static void graphLinksFromAVi(tdComponent fromVi, String linkType,
            tdDataBase tdb, Agent agent, List<Double> index) {
        String label;
        if (linkType != null) {
            label =
                    "Links of type " + linkType + " from "
                            + fromVi.getIdentifier();
        } else {
            label = "Links of all types from " + fromVi.getIdentifier();
        }
        // create a general purpose xy collection for jfreechart
        XYSeriesCollection xysc = new XYSeriesCollection();
        List<tdComponent> linkedVis = tdb.getFocusVis();

        List<String> types = new ArrayList<>();
        if (linkType != null) {
            types.add(linkType);
        } else {
            types.addAll(agent.getLinks().getLinkTypeNames());
        }

        // add a series for each VI - if not null
        for (tdComponent toVi : linkedVis) {
            for (String linkName : types) {
                boolean addDecision = false;
                String id;
                if (linkType != null) {
                    id = toVi.getIdentifier() + "-" + toVi.getLastPrettyPrint();
                } else {
                    id =
                            linkName + " to " + toVi.getIdentifier() + "-"
                                    + toVi.getLastPrettyPrint();
                }
                XYSeries linkSeries = new XYSeries(id);
                // now fill in the series with values
                for (Double time : index) {
                    double dtime = time;
                    double linkValue =
                            tdb.getLinkValue(fromVi.getIdentifier(),
                                    toVi.getIdentifier(), linkName, time);
                    if (linkValue != 0.0) {
                        addDecision = true;
                    }
                    linkSeries.add(dtime, linkValue);
                }
                if (addDecision) {
                    xysc.addSeries(linkSeries);
                }
            }
        }
        //
        // ok, now let us create a graph
        //
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
        JFreeChart chart =
                ChartFactory.createXYLineChart(label, "Time", "Value", xysc,
                        PlotOrientation.VERTICAL, true, false, false);
        GraphEvolution.setChartProperties(chart,
                GraphEvolution.lineStylesColorful);
        ChartPanel cp = new ChartPanel(chart);
        sgv.addComponent(cp);
        pgh.addComponent(cp);
        JFrame frame = new JFrame();
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * This function generates a frame into which a number of graphs are
     * arranged horizontally. Each graph describes the time series values for a
     * given in-focus object. We have: the focus (with all the energy colors -
     * salience / energy), the memory (with all the energy colors - salience /
     * energy), and a list of shadows (with all the energy colors - salience /
     * energy).
     * 
     * @param tdc
     * @param database
     * @param agent
     * @param index
     *            - the index of time values
     * @param isInstance
     *            - true for instances
     * @param shadowComponents
     *            - how many components will we enter in the graph
     * @param shadowRange
     *            - the range of the y plot on the shadows - needs to be unique.
     * 
     * 
     */
    public static void graphFMSComposite(tdComponent tdc,
            tdDataBase database, Agent agent, List<Double> index,
            int shadowComponents, double shadowRange,
            GraphEvolutionDescriptor ged) {
        String label = tdc.getLastPrettyPrint();
        // FIXME: this is a tiny bit iffy: we are getting the shadows based on a
        // certain energy color
        String ecx = EnergyColors.SHI_GENERIC;
        List<String> shadowList =
                database.getShadowComponents(tdc.getIdentifier(), ecx,
                        shadowComponents);
        //
        // ok, now let us create a graph
        //
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
        //
        // the graph with the focus values
        //
        if (ged.graphFocusEnergy || ged.graphFocusSalience) {
            JFreeChart chart = chartFocusEvolution(tdc, label, database, agent, index, ged);
            ChartPanel cp = new ChartPanel(chart);
            sgv.addComponent(cp);
            pgh.addComponent(cp);
        }
        //
        // the graph with the memory values
        //
        if (ged.graphMemoryEnergy || ged.graphMemorySalience) {
            JFreeChart chart = chartMemoryEvolution(tdc, label, database, agent, index, ged);
            ChartPanel cp = new ChartPanel(chart);
            sgv.addComponent(cp);
            pgh.addComponent(cp);
        }
        //
        // the graphs with the shadow components
        //
        if (ged.graphShadowEnergy || ged.graphShadowSalience) {
            for (String sh : shadowList) {
                JFreeChart chart = chartShadowEvolution(tdc, sh, database, agent, index, shadowRange, ged);
                ChartPanel cp = new ChartPanel(chart);
                sgv.addComponent(cp);
                pgh.addComponent(cp);
            }
        }
        JFrame frame = new JFrame();
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Generate a timeline based on ticks for every da timestep
     * 
     * @param database
     * @param agent
     * @param startFrom
     * @return
     */
    public static List<Double> getTimelineDaSteps(tdDataBase database,
            Agent agent, double startFrom) {
        Parameters p = agent.getParameters();
        double timeStepOfDAs =
                p.get("A_GENERAL", "G_GENERAL",
                        "N_TIME_STEP_OF_DAS");
        double timeStart = database.getTimeStart();
        if (startFrom > 0) {
            timeStart = startFrom;
        }
        double timeEnd = database.getTimeEnd();
        //
        // determine ticks on the X axis
        //
        List<Double> index = new ArrayList<>();
        for (double time = timeStart; time <= timeEnd; time =
                time + timeStepOfDAs) {
            index.add(time);
        }
        return index;
    }

    /**
     * Initializes
     */
    public static void initializeLineStyles() {
        /**
         * Conservative line styles: gray and black various thicknesses
         */
        GraphEvolution.lineStylesConservative = new ArrayList<>();
        List<Color> colors = new ArrayList<>();
        colors.add(Color.black);
        colors.add(Color.gray);
        List<Stroke> strokes = new ArrayList<>();
        strokes.add(new BasicStroke(3.0f));
        strokes.add(new BasicStroke(1.0f));
        for (Color color : colors) {
            for (Stroke stroke : strokes) {
                GraphEvolution.lineStylesConservative.add(new SimpleEntry<>(
                        color, stroke));
            }
        }
        /**
         * Colorful line styles: striking colors
         */
        GraphEvolution.lineStylesColorful = new ArrayList<>();
        colors = new ArrayList<>();
        colors.add(Color.black);
        colors.add(Color.blue);
        colors.add(Color.red);
        colors.add(Color.green);
        colors.add(Color.magenta);
        colors.add(Color.orange);
        for (Stroke stroke : strokes) {
            for (Color color : colors) {
                GraphEvolution.lineStylesColorful.add(new SimpleEntry<>(color,
                        stroke));
            }
        }
    }

    /**
     * Creates a chart the style we like.
     * 
     * @param chart
     */
    public static void setChartProperties(JFreeChart chart,
            List<SimpleEntry<Color, Stroke>> lineStyles) {
        // set the background and title font
        chart.setBackgroundPaint(Color.white);
        chart.getTitle().setFont(new Font("Tahoma", Font.BOLD, 14));
        // set the background and the grid all white
        XYPlot plot = chart.getXYPlot();
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.setBackgroundPaint(Color.white);
        // now
        plot.setOutlineStroke(null);

        XYItemRenderer rend = plot.getRenderer();
        Stroke stroke = new BasicStroke(2.0f);
        rend.setBaseStroke(stroke);
        // rend.setDrawSeriesLineAsPath(true);

        // stroke = new BasicStroke(2.0f, BasicStroke.CAP_ROUND,
        // BasicStroke.JOIN_ROUND, 10, new float[] {10, 10}, 0);
        // strokes.add(stroke);
        // stroke = new BasicStroke(2.0f, BasicStroke.CAP_ROUND,
        // BasicStroke.JOIN_ROUND, 10, new float[] {2, 4}, 0);
        // strokes.add(stroke);
        int seriesCount = plot.getSeriesCount();
        for (int i = 0; i != seriesCount; i++) {
            SimpleEntry<Color, Stroke> lineStyle =
                    lineStyles.get(i % lineStyles.size());
            rend.setSeriesStroke(i, lineStyle.getValue());
            rend.setSeriesPaint(i, lineStyle.getKey());
        }
    }

    /**
     * 
     * Returns a chart of the evolution of the focus component tdc
     * 
     * @param tdc
     * @param label
     * @param database
     * @param agent
     * @param index
     * @param ged
     * @return
     */
    public static JFreeChart chartFocusEvolution(tdComponent tdc, String label,
            tdDataBase database, Agent agent, List<Double> index,
            GraphEvolutionDescriptor ged) {
        List<String> focusEnergyColors = null;
        switch(tdc.getType()) {
        case INSTANCE:
            focusEnergyColors = ged.focusInstanceEnergyColors;
            break;
        case VI:
            focusEnergyColors = ged.focusViEnergyColors;
            break;
        case CHOICE:
            focusEnergyColors = null;
            break;
        }
        
        
        // create a general purpose xy collection for jfreechart
        XYSeriesCollection xysc = new XYSeriesCollection();
        // focus energy values (if needed)
        if (ged.graphFocusEnergy) {
            for (String ec : focusEnergyColors) {
                xysc.addSeries(new XYSeries("FocusEnergy_" + ec));
            }
        }
        // focus salience values (if needed)
        if (ged.graphFocusSalience) {
            for (String ec : focusEnergyColors) {
                xysc.addSeries(new XYSeries("FocusSalience_" + ec));
            }
        }
        //
        // Fill in the values into the xysc
        //
        for (Double time : index) {
            double dtime = time;
            if (ged.graphFocusEnergy) {
                for (String ec : focusEnergyColors) {
                    double value =
                            database.getEnergy(tdc.getIdentifier(), ec, time);
                    xysc.getSeries("FocusEnergy_" + ec).add(dtime, value);
                }
            }
            // focus salience values (if needed)
            if (ged.graphFocusSalience) {
                for (String ec : focusEnergyColors) {
                    double value =
                            database.getSalience(tdc.getIdentifier(), ec, time);
                    xysc.getSeries("FocusSalience_" + ec).add(dtime, value);
                }
            }
        }
        JFreeChart chart =
                ChartFactory.createXYLineChart(label + " - Focus", "Time",
                        "Value", xysc, PlotOrientation.VERTICAL, true, false,
                        false);
        GraphEvolution.setChartProperties(chart,
                GraphEvolution.lineStylesConservative);
        return chart;
    }

    /**
     * Returns a chart of the evolution of the memory component tdc
     * 
     * @param tdc
     * @param label
     * @param database
     * @param agent
     * @param index
     * @param ged
     * @return
     */
    public static JFreeChart chartMemoryEvolution(tdComponent tdc,
            String label, tdDataBase database, Agent agent, List<Double> index,
            GraphEvolutionDescriptor ged) {
        List<String> memoryColors = null;
        if (tdc.getType() == tdComponentType.INSTANCE) {
            memoryColors = ged.memoryInstanceEnergyColors;
        } else {
            memoryColors = ged.memoryViEnergyColors;
        }

        // create a general purpose xy collection for jfreechart
        XYSeriesCollection xysc = new XYSeriesCollection();
        // memory energy values (if needed)
        if (ged.graphMemoryEnergy) {
            for (String ec : memoryColors) {
                xysc.addSeries(new XYSeries("MemoryEnergy_" + ec));
            }
        }
        // memory salience values (if needed)
        if (ged.graphMemorySalience) {
            for (String ec : memoryColors) {
                xysc.addSeries(new XYSeries("MemorySalience_" + ec));
            }
        }
        //
        // Fill in the values into the xysc
        //
        for (Double time : index) {
            double dtime = time;
            // memory energy values (if needed)
            if (ged.graphMemoryEnergy) {
                for (String ec : memoryColors) {
                    double value =
                            database.getEnergy(tdc.getIdentifier(), ec, time);
                    xysc.getSeries("MemoryEnergy_" + ec).add(dtime, value);
                }
            }
            // memory salience values (if needed)
            if (ged.graphMemorySalience) {
                for (String ec : memoryColors) {
                    double value =
                            database.getSalience(tdc.getIdentifier(), ec, time);
                    xysc.getSeries("MemorySalience_" + ec).add(dtime, value);
                }
            }
        }
        //
        // the chart with the memory values
        //
        JFreeChart chart =
                ChartFactory.createXYLineChart(label + " - memory", "Time",
                        "Value", xysc, PlotOrientation.VERTICAL, true, false,
                        false);
        GraphEvolution.setChartProperties(chart,
                GraphEvolution.lineStylesConservative);
        return chart;
    }

    
    /**
     * Returns a chart for the evolution of the shadowing of the component tdc with sh.
     * 
     * @param tdc
     * @param sh
     * @param database
     * @param agent
     * @param index
     * @param shadowRange
     * @param ged
     * @return
     */
    public static JFreeChart
            chartShadowEvolution(tdComponent tdc, String sh,
                    tdDataBase database, Agent agent, List<Double> index,
                    double shadowRange, GraphEvolutionDescriptor ged) {
        List<String> shadowColors = null;
        if (tdc.getType() == tdComponentType.INSTANCE) {
            shadowColors = ged.shadowInstanceEnergyColors;
        } else {
            shadowColors = ged.shadowViEnergyColors;
        }
        // create a general purpose xy collection for jfreechart
        XYSeriesCollection xysc = new XYSeriesCollection();
        // shadow energy values (if needed)
        if (ged.graphShadowEnergy) {
            for (String ec : shadowColors) {
                xysc.addSeries(new XYSeries("ShadowEnergy_" + ec + "_" + sh));
            }
        }
        // shadow salience values (if needed)
        if (ged.graphShadowSalience) {
            for (String ec : shadowColors) {
                xysc.addSeries(new XYSeries("ShadowSalience_" + ec + "_" + sh));
            }
        }
        //
        // Fill in the values into the xysc
        //
        for (Double time : index) {
            double dtime = time;
            // shadow energy values (if needed)
            if (ged.graphShadowEnergy) {
                for (String ec : shadowColors) {
                    double value =
                            database.getEnergy(tdc.getIdentifier(), sh, ec,
                                    time);
                    xysc.getSeries("ShadowEnergy_" + ec + "_" + sh).add(dtime, value);
                }
            }
            // shadow salience values (if needed)
            if (ged.graphShadowSalience) {
                for (String ec : shadowColors) {
                    double value =
                            database.getSalience(tdc.getIdentifier(), sh, ec,
                                    time);
                    xysc.getSeries("ShadowSalience_" + ec+ "_" + sh).add(dtime, value);
                }
            }
        }
        XYSeriesCollection xysSH = new XYSeriesCollection();
        // shadow energy (if needed)
        if (ged.graphShadowEnergy) {
            for (String ec : shadowColors) {
                xysSH.addSeries(xysc.getSeries("ShadowEnergy_" + ec+ "_" + sh));
            }
        }
        // shadow salience (if needed)
        if (ged.graphShadowSalience) {
            for (String ec : shadowColors) {
                xysSH.addSeries(xysc.getSeries("ShadowSalience_" + ec+ "_" + sh));
            }
        }
        // FIND a label
        String shadowLabel = "Shadow:" + sh;
        if (tdc.getType() == tdComponentType.INSTANCE) {
            Instance instance =
                    agent.getAutobiographicalMemory().getInstance(sh);
            shadowLabel += " - " + SpInstance.spc(instance, agent);
        } else {
            VerbInstance vi =
                    agent.getAutobiographicalMemory().getVerbInstance(sh);
            shadowLabel += " - " + XapiPrint.ppsViXapiForm(vi, agent);
        }
        JFreeChart chart =
                ChartFactory.createXYLineChart(shadowLabel, "Time", "Value",
                        xysSH, PlotOrientation.VERTICAL, true, false, false);
        GraphEvolution.setChartProperties(chart,
                GraphEvolution.lineStylesConservative);
        XYPlot plot = chart.getXYPlot();
        plot.getRangeAxis(0).setRange(0, shadowRange);
        return chart;
    }

}

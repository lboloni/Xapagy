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
package org.xapagy.debug;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xapagy.agents.Agent;
import org.xapagy.headless_shadows.Choice;
import org.xapagy.headless_shadows.Choice.ChoiceType;
import org.xapagy.headless_shadows.ChoiceTypeHelper;
import org.xapagy.headless_shadows.HeadlessComponents;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.prettyprint.FormatTable;
import org.xapagy.ui.prettyprint.Formatter;
import org.xapagy.ui.prettyprint.PpVerbInstanceTemplate;
import org.xapagy.ui.prettyprint.PrettyPrint;
import org.xapagy.ui.smartprint.XapiPrint;
import org.xapagy.xapi.XapiParserException;

/**
 * This class collects the results of a run with respect to choices. Its columns
 * are filters for a specific type of choice, while the rows are times in the
 * evolution of the agent - both the last executed VI, and the current time.
 * 
 * One way to use this matrix is to look at it - another is to programmatically
 * test and compare it.
 * 
 * 
 * @author Ladislau Boloni
 * Created on: Jul 2, 2014
 */
public class ChoiceEvolutionMatrix extends AbstractEvolutionMatrix {

    private static final long serialVersionUID = -9141499617600656392L;
    /**
     * The columns: the filters used to extract the matrix. Note that it is
     * desirable that the filters are specific enough the there is no ambiguity
     * about the choice...
     * 
     * The filters have to be initialized before the recording starts.
     */
    private List<ChoiceAccessFilter> columns = new ArrayList<>();
    /**
     * Predefined ChoiceAccessFilters - these will not be used as properly
     * filters, only indexes in the data
     */
    private ChoiceAccessFilter bestIndependent = new ChoiceAccessFilter(
            "bestIndependent", null, null, "*");
    private ChoiceAccessFilter bestDependent = new ChoiceAccessFilter(
            "bestDependent", null, null, "*");
    private ChoiceAccessFilter bestMood = new ChoiceAccessFilter("bestMood",
            null, null, "*");

    /**
     * The collection of the data: map time of maps of ChoiceAccessFilters
     */
    private Map<Double, Map<ChoiceAccessFilter, SimpleEntry<Choice, double[]>>> data =
            new HashMap<>();
    /**
     * If this variable is set, we are verifying that there is an unambiguous
     * choice!!!
     * 
     * I can imagine scenarios where we don't want this, but I think that most
     * of the time we want it.
     */
    private boolean enforceUnambiguousChoice;
    /**
     * The order in which the ambiguous comparators are picked up
     */
    private Comparator<Choice> choiceComparator;
    /**
     * A Choice access object, through which the choices of the agent will be
     * query-d
     */
    protected ChoiceAccess choiceAccess;

    /**
     * Constructs an empty ChoiceEvolutionMatrix, while enforcing unambiguous
     * choices.
     * 
     * @param agent
     */
    public ChoiceEvolutionMatrix(Agent agent) {
        super(agent);
        choiceComparator = HeadlessComponents.comparatorIndependentScore;
        this.choiceAccess = new ChoiceAccess(agent, choiceComparator);
        enforceUnambiguousChoice = true;
    }

    /**
     * Creates a new ChoiceAccessFilter with the specified parameters, and adds
     * it as a column
     * 
     * @param name
     * @param choiceType
     * @param viType
     * @param params
     */
    public void addColumn(String name, ChoiceType choiceType, ViType viType,
            String... params) {
        ChoiceAccessFilter ca =
                new ChoiceAccessFilter(name, choiceType, viType, params);
        columns.add(ca);
    }

    /**
     * Performs the recording of the choices from the agent. This function adds
     * a new row.
     * @throws XapiParserException 
     */
    public void record() {
        Double time = addRow();
        // add the data
        data.put(
                time,
                new HashMap<ChoiceAccessFilter, SimpleEntry<Choice, double[]>>());
        // get the choices by different orders to allow the extraction of
        // rankings
        //
        // independent
        //
        List<Choice> chByInd =
                agent.getHeadlessComponents().getChoices(
                        HeadlessComponents.comparatorIndependentScore);
        if (!chByInd.isEmpty()) {
            double rankings[] = new double[3];
            rankings[0] = 0;
            rankings[1] = 0;
            rankings[2] = 0;
            SimpleEntry<Choice, double[]> dataPoint =
                    new SimpleEntry<>(chByInd.get(0), rankings);
            data.get(time).put(bestIndependent, dataPoint);
        }
        //
        // dependent
        //
        List<Choice> chByDep =
                agent.getHeadlessComponents().getChoices(
                        HeadlessComponents.comparatorDependentScore);
        if (!chByDep.isEmpty()) {
            double rankings[] = new double[3];
            rankings[0] = 0;
            rankings[1] = 0;
            rankings[2] = 0;
            SimpleEntry<Choice, double[]> dataPoint =
                    new SimpleEntry<>(chByDep.get(0), rankings);
            data.get(time).put(bestDependent, dataPoint);
        }
        //
        // mood
        //
        List<Choice> chByMood =
                agent.getHeadlessComponents().getChoices(
                        HeadlessComponents.comparatorMoodScore);
        if (!chByMood.isEmpty()) {
            double rankings[] = new double[3];
            rankings[0] = 0;
            rankings[1] = 0;
            rankings[2] = 0;
            SimpleEntry<Choice, double[]> dataPoint =
                    new SimpleEntry<>(chByMood.get(0), rankings);
            data.get(time).put(bestMood, dataPoint);
        }

        for (ChoiceAccessFilter caf : columns) {
            List<Choice> filtered = choiceAccess.getChoices(caf);
            if (enforceUnambiguousChoice) {
                if (filtered.size() > 1) {
                    TextUi.abort("ChoiceEvolutionMatrix.record - we are enforcing unambiguous filters: offending filter was:"
                            + caf.getName());
                }
            }
            if (filtered.isEmpty()) {
                data.get(time).put(caf, null);
            } else {
                Choice ch = filtered.get(0);
                // find the rankings!!!
                double rankings[] = new double[3];
                rankings[0] = chByInd.indexOf(ch);
                rankings[1] = chByDep.indexOf(ch);
                rankings[2] = chByMood.indexOf(ch);
                SimpleEntry<Choice, double[]> dataPoint =
                        new SimpleEntry<>(ch, rankings);
                data.get(time).put(caf, dataPoint);
            }
        }
    }

    /**
     * Returns the independent value
     * 
     * @param column
     * @param row
     */
    public double queryIndependent(String column, int row) {
        Choice choice = query(column, row);
        if (choice != null) {
            return choice.getChoiceScore().getScoreIndependent();
        }
        return -1.0;
    }

    /**
     * Returns the dependent value
     * 
     * @param column
     * @param row
     */
    public double queryDependent(String column, int row) {
        Choice choice = query(column, row);
        if (choice != null) {
            return choice.getChoiceScore().getScoreIndependent();
        }
        return -1.0;
    }

    /**
     * Returns the mood value
     * 
     * @param column
     * @param row
     */
    public double queryMood(String column, int row) {
        Choice choice = query(column, row);
        if (choice != null) {
            return choice.getChoiceScore().getScoreIndependent();
        }
        return -1.0;
    }

    /**
     * Returns the choice indicated by the specific column name and row, or null
     * if there was no choice recorded at that point.
     * 
     * @param column
     *            - the name of the filter which specifies the column
     * @param row
     * @return
     */
    public Choice query(String column, int row) {
        double time = rows.get(row).getValue();
        for (ChoiceAccessFilter caf : columns) {
            if (caf.getName().equals(column)) {
                SimpleEntry<Choice, double[]> entry = data.get(time).get(caf);
                if (entry == null) {
                    return null;
                }
                Choice choice = entry.getKey();
                return choice;
            }
        }
        TextUi.abort("ChoiceEvolutionMatrix.query: could not identify: "
                + column + " " + row);
        return null;
    }

    /**
     * Returns the choice indicated by the specific column name and a ViMatch
     * parametrization for the row - matches
     * 
     * @param column
     *            - the name of the filter specifying the column
     * @param viType
     *            - the type of the verb of the VI instantiated at the row
     * @param params
     *            - the parameters for identifying the VI instantiated at the
     *            row - the format being the one of ViMatch
     * @return
     * @throws XapiParserException 
     */
    public Choice query(String column, ViType viType, String... params) throws XapiParserException {
        int row = findRow(viType, params);
        return query(column, row);
    }

    /**
     * Returns the independent value
     * 
     * @param column
     * @param row
     * @throws XapiParserException 
     */
    public double queryIndependent(String column, ViType viType,
            String... params) throws XapiParserException {
        Choice choice = query(column, viType, params);
        if (choice != null) {
            return choice.getChoiceScore().getScoreIndependent();
        }
        return -1.0;
    }

    /**
     * Returns the dependent value
     * 
     * @param column
     * @param row
     * @throws XapiParserException 
     */
    public double
            queryDependent(String column, ViType viType, String... params) throws XapiParserException {
        Choice choice = query(column, viType, params);
        if (choice != null) {
            return choice.getChoiceScore().getScoreIndependent();
        }
        return -1.0;
    }

    /**
     * Returns the mood value
     * 
     * @param column
     * @param row
     * @throws XapiParserException 
     */
    public double queryMood(String column, ViType viType, String... params) throws XapiParserException {
        Choice choice = query(column, viType, params);
        if (choice != null) {
            return choice.getChoiceScore().getScoreIndependent();
        }
        return -1.0;
    }

    /**
     * Prints the matrix into a table. The scores, if it has idm it puts the
     * independent, dependent and mood score in the lines. If we add IDM, it
     * adds an additional column for the best independent, dependent and/or mood
     * winner.
     * 
     * @param scoresToPrint
     *            "idm"
     */
    public String printMatrix(String scoresToPrint) {
        // columns: time, exec, followed by the choices
        int tableColumnsPrelim = 2; // Time, last VI
        int tableColumnsMiddle = columns.size();
        int tableColumnsEnd = 0;
        if (scoresToPrint.contains("I")) {
            tableColumnsEnd++;
        }
        if (scoresToPrint.contains("D")) {
            tableColumnsEnd++;
        }
        if (scoresToPrint.contains("M")) {
            tableColumnsEnd++;
        }

        int tableColumns[] =
                new int[tableColumnsPrelim + tableColumnsMiddle
                        + tableColumnsEnd];
        String headers[] =
                new String[tableColumnsPrelim + tableColumnsMiddle
                        + tableColumnsEnd];
        tableColumns[0] = 10;
        headers[0] = "Time";
        tableColumns[1] = 40;
        headers[1] = "Last VI";
        for (int i = 2; i != tableColumnsPrelim + tableColumnsMiddle; i++) {
            tableColumns[i] = 20;
            headers[i] = columns.get(i - 2).getName();
        }
        int currentColumn = tableColumnsPrelim + tableColumnsMiddle;
        if (scoresToPrint.contains("I")) {
            tableColumns[currentColumn] = 40;
            headers[currentColumn] = "Best independent";
            currentColumn++;
        }
        if (scoresToPrint.contains("D")) {
            tableColumns[currentColumn] = 40;
            headers[currentColumn] = "Best dependent";
            currentColumn++;
        }
        if (scoresToPrint.contains("M")) {
            tableColumns[currentColumn] = 40;
            headers[currentColumn] = "Best dependent";
        }

        // the header
        FormatTable ft = new FormatTable(tableColumns);
        ft.header(headers);
        ft.internalSeparator();
        // add the remaining rows
        for (int rowCount = 0; rowCount < rows.size(); rowCount++) {
            String row[] = new String[tableColumns.length];
            Double time = rows.get(rowCount).getValue();
            row[0] = Formatter.fmt(time);
            // the second item: a formatted one
            VerbInstance vi = rows.get(rowCount).getKey();
            String s = XapiPrint.ppsViXapiForm(vi, agent);
            row[1] = s;
            for (int i = 0; i != columns.size(); i++) {
                SimpleEntry<Choice, double[]> entry =
                        data.get(time).get(columns.get(i));
                String val = " - ";
                if (entry != null) {
                    Choice choice = entry.getKey();
                    double ranking[] = entry.getValue();
                    val = "";
                    if (scoresToPrint.contains("i")) {
                        double value =
                                choice.getChoiceScore().getScoreIndependent();
                        val += "i=" + Formatter.fmt(value);
                        if (value > 0 && ranking[0] < 10) {
                            val += " (#" + (ranking[0] + 1) + ")";
                        }
                        if (scoresToPrint.contains("d")
                                || scoresToPrint.contains("m")) {
                            val += "  ";
                        }
                    }
                    if (scoresToPrint.contains("d")) {
                        double value =
                                choice.getChoiceScore().getScoreDependent();
                        val += "d=" + Formatter.fmt(value);
                        if (value > 0 && ranking[1] < 10) {
                            val += " (#" + (ranking[1] + 1) + ")";
                        }
                        if (scoresToPrint.contains("m")) {
                            val += "  ";
                        }
                    }
                    if (scoresToPrint.contains("m")) {
                        double value = choice.getChoiceScore().getScoreMood();
                        val += "m=" + Formatter.fmt(value);
                        if (value > 0 && ranking[2] > 0 && ranking[2] < 10) {
                            val += " (#" + (ranking[2] + 1) + ")";
                        }
                    }
                }
                row[i + 2] = val;
            }
            //
            // now add the best ones if needed
            //
            currentColumn = tableColumnsPrelim + tableColumnsMiddle;
            if (scoresToPrint.contains("I")) {
                String val = printTop(data.get(time).get(bestIndependent));
                row[currentColumn++] = val;
            }
            if (scoresToPrint.contains("D")) {
                String val = printTop(data.get(time).get(bestDependent));
                row[currentColumn++] = val;
            }
            if (scoresToPrint.contains("M")) {
                String val = printTop(data.get(time).get(bestMood));
                row[currentColumn] = val;
            }
            //
            // add the row to the table
            //
            ft.wrappedRow((Object[]) row);
            if (scoresToPrint.length() > 1 && (rowCount < rows.size() - 1)) {
                ft.internalSeparator();
            }
        }
        ft.endTable();
        return ft.toString();
    }

    /**
     * Creates the printing for a top choice
     * 
     * @return
     */
    private String printTop(SimpleEntry<Choice, double[]> entry) {
        String val = "";
        if (entry == null) {
            return " *NONE* ";
        }
        Choice c = entry.getKey();
        if (ChoiceTypeHelper.isCharacterization(c)) {
            val +=
                    "CHARACTERIZATION: "
                            + PrettyPrint.ppConcise(c.getHlsCharacterization()
                                    .getAttributes(), agent);
        }
        if (ChoiceTypeHelper.isHlsBased(c)) {
            val +=
                    PpVerbInstanceTemplate.ppConciseViTemplate(c.getHls()
                            .getViTemplate(), agent);
        }
        if (ChoiceTypeHelper.isStatic(c)) {
            val +=
                    PpVerbInstanceTemplate.ppConciseViTemplate(c.getStaticHls()
                            .getViTemplate(), agent);
        }
        val +=
                "I=" + Formatter.fmt(c.getChoiceScore().getScoreIndependent())
                        + ", ";
        val +=
                "D=" + Formatter.fmt(c.getChoiceScore().getScoreDependent())
                        + ", ";
        val += "M=" + Formatter.fmt(c.getChoiceScore().getScoreMood()) + ", ";
        return val;
    }

    /**
     * @return
     */
    public int getRowCount() {
        return rows.size();
    }
}

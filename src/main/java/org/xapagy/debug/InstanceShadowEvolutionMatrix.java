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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xapagy.agents.Agent;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.EnergyColors.EnergyColorType;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.FormatTable;
import org.xapagy.ui.formatters.Formatter;
import org.xapagy.ui.smartprint.SpInstance;
import org.xapagy.ui.smartprint.XapiPrint;

/**
 * This class collects the results of a run with respect to shadows of
 * instances. Its columns are filters for shadow instance pairs, while the rows
 * are time points in the evolution of the agent.
 * 
 * @author Ladislau Boloni
 * Created on: Jul 13, 2014
 */
public class InstanceShadowEvolutionMatrix extends AbstractEvolutionMatrix {

    private static final long serialVersionUID = 2366168937144442540L;
    /**
     * The columns: the filters used to extract the matrix. The filters have to
     * be initialized before the recording starts.
     */
    private List<InstanceShadowAccessFilter> columns = new ArrayList<>();
    /**
     * The access object through which the shadows will be queried
     */
    private ShadowAccess shadowAccess;
    /**
     * The data: maps time to map of shadow access filter to shadow record
     */
    private Map<Double, Map<InstanceShadowAccessFilter, ShadowRecord<Instance>>> data =
            new HashMap<>();

    /**
     * Constructor: basic initialization
     * 
     * @param agent
     */
    public InstanceShadowEvolutionMatrix(Agent agent) {
        super(agent);
        this.shadowAccess = new ShadowAccess(agent);
    }

    /**
     * Adds a new column 7
     * 
     * @param isaf
     */
    public void addColumn(InstanceShadowAccessFilter isaf) {
        columns.add(isaf);
    }

    /**
     * Performs the recording of the data from the agent. The function adds a
     * new row.
     */
    public void record() {
        Double time = addRow();
        data.put(
                time,
                new HashMap<InstanceShadowAccessFilter, ShadowRecord<Instance>>());
        for (InstanceShadowAccessFilter isaf : columns) {
            ShadowRecord<Instance> sr =
                    shadowAccess.getOneShadow(isaf.getImfFocus(),
                            isaf.getImfShadow(), isaf.getSortBy(), true);
            data.get(time).put(isaf, sr);
        }
    }

    /**
     * Returns the ShadowRecord indicated by the specific column name and row,
     * or null if there was no shadow record at that point.
     * 
     * @param column
     *            - the name of the filter which specifies the column
     * @param row
     * @return
     */
    public ShadowRecord<Instance> query(String column, int row) {
        double time = rows.get(row).getValue();
        for (InstanceShadowAccessFilter isaf : columns) {
            if (isaf.getName().equals(column)) {
                ShadowRecord<Instance> shadowRecord = data.get(time).get(isaf);
                return shadowRecord;
            }
        }
        TextUi.abort("InstanceShadowEvolutionMatrix.query: could not identify: "
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
     */
    public ShadowRecord<Instance> query(String column, ViType viType,
            String... params) {
        int row = findRow(viType, params);
        return query(column, row);
    }

    /**
     * Prints the matrix into a table. The scores, if it has "color" in the
     * string in lowercase, it puts them in the lines. If it has then in
     * uppercase, it adds a column with the strongest shadow according to the
     * given color.
     * 
     * @param topColorsToPrint
     *            - the string specifying the colors
     * @return
     */
    public String printMatrix(String topColorsToPrint) {
        // columns: time, exec, followed by the shadows
        int tableColumnsPrelim = 2; // time, last VI
        int tableColumnsMiddle = columns.size();
        int tableColumnsEnd = 0;
        for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.SHADOW_INSTANCE)) {
            String ecUpperCase = ec.toString().toUpperCase();
            if (topColorsToPrint.contains(ecUpperCase)) {
                tableColumnsEnd++;
            }
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
            InstanceShadowAccessFilter isaf = columns.get(i - 2);
            if (isaf.getSortBy() == null) {
                tableColumns[i] = 20;
            } else {
                tableColumns[i] = 40;
            }
            headers[i] = isaf.getName();
        }
        int currentColumn = tableColumnsPrelim + tableColumnsMiddle;
        for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.SHADOW_INSTANCE)) {
            String ecUpperCase = ec.toString().toUpperCase();
            if (topColorsToPrint.contains(ecUpperCase)) {
                tableColumns[currentColumn] = 40;
                headers[currentColumn] = "best " + ecUpperCase;
            }
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
            // now the remaining columns
            for (int i = 0; i != columns.size(); i++) {
                ShadowRecord<Instance> sr = data.get(time).get(columns.get(i));
                row[i + 2] = formatShadowRecord(sr, topColorsToPrint);
            }
            //
            // add the row to the table
            //
            ft.wrappedRow((Object[]) row);
        }
        ft.endTable();
        return ft.toString();
    }

    /**
     * Formats one cell describing the shadow record
     */
    public String formatShadowRecord(ShadowRecord<Instance> sr,
            String topColorsToPrint) {
        String val = "";
        // We probably don't need this
        // if (sr == null) {
        // return " - ";
        // }
        // if it was obtained as a result of sorting, print the shadow instance
        if (sr.getSortBy() != null) {
            Instance instance = sr.getShadowObject();
            val = SpInstance.spc(instance, agent) + "\n";
        }

        for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.SHADOW_INSTANCE)) {
            String ecLowerCase = ec.toString().toLowerCase();
            ecLowerCase = ecLowerCase.replace("shadow_", "");
            if (topColorsToPrint.contains(ecLowerCase)) {
                double energy = sr.getEnergy(ec);
                double param = agent.getEnergyColors().getEnergyToSalience(ec);
                double salience =
                        EnergyColors.convert(energy, param);
                val +=
                        ecLowerCase + "=" + Formatter.fmt(salience) + "("
                                + Formatter.fmt(energy) + ")";
            }
        }
        return val;
    }

}

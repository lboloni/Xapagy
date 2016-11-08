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
package org.xapagy.shadows;

import org.junit.Test;
import org.xapagy.TestHelper;
import org.xapagy.set.EnergyColors;
import org.xapagy.ui.prettyprint.FormatTable;
import org.xapagy.ui.prettyprint.Formatter;

/**
 * @author Ladislau Boloni
 * Created on: Jan 30, 2012
 */
public class testEnergyToSalience {

    /**
     * Tests the energy to salience curve
     */
    @Test
    public void testEnergy2Salience() {
        TestHelper.testStart("Test the energy to salience conversion");
        int cnt = 10;
        String header[] = new String[cnt + 1];
        int columnwidths[] = new int[cnt + 1];
        double value[] = new double[cnt];
        double e2ss[] = new double[cnt];
        header[0] = "energy/scale";
        columnwidths[0] = header[0].length();
        for (int i = 0; i != cnt; i++) {
            value[i] = 0.1 + i * 0.3;
            e2ss[i] = value[i];
            header[i + 1] = Formatter.fmt(value[i]);
            columnwidths[i + 1] = 8;
        }
        FormatTable ft = new FormatTable(columnwidths);
        ft.header(header);
        ft.internalSeparator();
        for (double energy = 0; energy < 20.0; energy = energy + 0.1) {
            String[] labels = new String[cnt + 1];
            labels[0] = Formatter.fmt(energy);
            for (int i = 0; i != cnt; i++) {
                double v = EnergyColors.convert(energy, e2ss[i]);
                labels[i + 1] = Formatter.fmt(v);
            }
            ft.row((Object[]) labels);
        }
        ft.endTable();
        TestHelper.printIfVerbose(ft.toString());
        TestHelper.testDone();
    }

}

/*
   This file is part of the Xapagy project
   Created on: Nov 20, 2015
 
   org.xapagy.parameters.ParameterExporter
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.parameters;

import org.xapagy.ui.TextUiHelper;
import org.xapagy.ui.prettyprint.Formatter;

/**
 * 
 * This class takes all the parameters and exports them into Xapi format
 * (together with comments etc)
 * 
 * @author Ladislau Boloni
 *
 */
public class ParameterExporter {

    /**
     * Exports the parameters into Xapi
     * 
     * @param p
     * @return
     */
    public static String exportParameters(Parameters p) {
        Formatter fmt = new Formatter();
        for (String area : p.listAreas()) {
            exportArea(fmt, p, area);
        }
        return fmt.toString();
    }

    /**
     * Exports the parameters in a specific area into Xapi
     * 
     * @param fmt
     * @param p
     * @param area
     */
    private static void exportArea(Formatter fmt, Parameters p, String area) {
        fmt.add("// ********************************************");
        fmt.add("// area: " + area);
        fmt.add("// ********************************************");
        for (String group : p.listGroups(area)) {
            exportGroup(fmt, p, area, group);
        }
    }

    /**
     * @param fmt
     * @param p
     * @param area
     * @param group
     */
    private static void exportGroup(Formatter fmt, Parameters p, String area,
            String group) {
        fmt.add("// --------------------------------------------");
        fmt.add("// group: " + group);
        fmt.add("// --------------------------------------------");
        for (String name : p.listNames(area, group)) {
            String description = p.getDescription(area, group, name);
            double value = p.get(area, group, name);
            String line = "$SetParameter " + area + "/" + group + "/" + name + "=" + value;
            // need to add something
            String comment = TextUiHelper.shiftText(2, "/", " " + description);
            fmt.add(comment);
            fmt.add(line);
        }
    }

}

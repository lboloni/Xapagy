/*
   This file is part of the Xapagy project
   Created on: Apr 18, 2014
 
   org.xapagy.ui.prettygeneral.xwParamNew
 
   Copyright (c) 2008-2016 Ladislau Boloni
 */

package org.xapagy.ui.prettygeneral;

import org.xapagy.parameters.Parameters;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * @author Ladislau Boloni
 * 
 */
public class xwParameters {
    /**
     * Print the parameters, organized by area and group
     * 
     * @param fmt
     * @param p
     * @return
     */
    public static String xwDetailed(IXwFormatter fmt, Parameters p) {
        for (String area : p.listAreas()) {
            fmt.addH2(area);
            fmt.indent();
            xwParameters.xwDetailedArea(fmt, p, area);
            fmt.deindent();
        }
        return fmt.toString();

    }

    /**
     * @param debug
     */
    private static void xwDetailedArea(IXwFormatter fmt, Parameters p,
            String area) {
        for (String group : p.listGroups(area)) {
            fmt.addLabelParagraph(group);
            fmt.indent();
            xwParameters.xwDetailedGroup(fmt, p, area, group);
            fmt.deindent();
        }
    }

    /**
     * @param fmt
     * @param p
     * @param area
     * @param group
     */
    private static void xwDetailedGroup(IXwFormatter fmt, Parameters p,
            String area, String group) {
        for (String name : p.listNames(area, group)) {
            fmt.is(name, p.get(area, group, name));
            String description = p.getDescription(area, group, name);
            if (description != null) {
                fmt.explanatoryNote(description);
            }
        }
    }
}
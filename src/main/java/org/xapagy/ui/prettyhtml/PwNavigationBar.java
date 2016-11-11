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
package org.xapagy.ui.prettyhtml;

import org.xapagy.Version;
import org.xapagy.agents.Agent;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.ui.formatters.HtmlFormatter;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.observers.BreakObserver;
import org.xapagy.ui.observers.IAgentObserver;
import org.xapagy.ui.prettyprint.Formatter;

/**
 * Functions which deal with the navigation bar in Xapagy webpage
 * 
 * @author Ladislau Boloni
 * Created on: Nov 8, 2012
 */
public class PwNavigationBar implements IQueryAttributes {

    /**
     * Adds the general purpose JavaScript content for a page: the show/hide and
     * the page refresh ones
     */
    private static String addJsFunctions(HtmlFormatter fmt, RESTQuery query) {
        StringBuffer buf = new StringBuffer();
        // show hide
        buf.append("function showHide(shID) {\n");
        buf.append("	   if (document.getElementById(shID)) {\n");
        buf.append("	      if (document.getElementById(shID+'-show').style.display != 'none') {\n");
        buf.append("	         document.getElementById(shID+'-show').style.display = 'none';\n");
        buf.append("	         document.getElementById(shID).style.display = 'block';\n");
        buf.append("	      }\n");
        buf.append("	      else {\n");
        buf.append("	         document.getElementById(shID+'-show').style.display = 'inline';\n");
        buf.append("	         document.getElementById(shID).style.display = 'none';\n");
        buf.append("	      }\n");
        buf.append("	   }\n");
        buf.append("}\n");
        buf.append("\n");
        // timed refresh
        buf.append("function timedRefresh(timeoutPeriod) {\n");
        buf.append("    var interval = setInterval(refreshPage, timeoutPeriod);\n");
        buf.append("}\n");
        buf.append("\n");
        // refresh page
        buf.append("function refreshPage() {\n");
        buf.append("    if (document.RefreshForm.autoRefreshCheckboxes.checked == 1) {\n");
        // buf.append("        location.reload(true);\n");
        // if we are refreshing through this, this will be an auto-refresh one
        // for sure
        RESTQuery refreshQuery = QueryHelper.copyWithEmptyCommand(query);
        refreshQuery.setAttribute(Q_AUTO_REFRESH, "true");
        buf.append("        location.href =\"" + refreshQuery.toQuery()
                + "\";\n");
        // trying to reset the refresh back where it was
        // buf.append("        document.RefreshForm.autoRefreshCheckboxes.checked = 1;\n");
        buf.append("    }\n");
        buf.append("}\n");
        fmt.addJavascript(buf.toString());
        return fmt.toString();
    }

    /**
     * Adds the refresh checkbox
     * 
     * @param fmt
     * @return
     */
    public static String addRefreshCheckbox(HtmlFormatter fmt,
            boolean initiallyChecked) {
        StringBuffer buf = new StringBuffer();
        String checked;
        if (initiallyChecked) {
            checked = "checked=\"checked\"";
        } else {
            checked = "";
        }
        buf.append("<form name=\"RefreshForm\">\n");
        buf.append("<span class=version>");
        buf.append("<input type=\"checkbox\" " + checked
                + " name=\"autoRefreshCheckboxes\" >Auto refresh</input>\n");
        buf.append("</span>");
        buf.append("</form>\n");
        fmt.add(buf.toString());
        return fmt.toString();
    }

    /**
     * The navigation bar - appears at the top of all the pages. Contains links
     * to all the queries.
     * 
     * @param fmt
     * @param agent
     * @param query
     */
    public static String navigationBar(PwFormatter fmt, Agent agent,
            RESTQuery query) {
        // this is as good place as any to add the show / hide javascript
        PwNavigationBar.addJsFunctions(fmt, query);
        fmt.openDiv("class=" + PwFormatter.CLASS_NAVBAR);
        fmt.openTable();
        fmt.openTR();
        fmt.openTD();
        fmt.openA("href=" + query.toQuery(), "class=xapagylabel");
        fmt.add("Xapagy");
        fmt.closeA();
        fmt.addP("v" + Version.versionNumberString(), "class=version");
        fmt.addP("time: " + Formatter.fmt(agent.getTime()), "class="
                + PwFormatter.CLASS_NAVBAR);
        // if there is a breakobserver, add a link
        boolean hasBreakObserver = false;
        for (IAgentObserver iobs : agent.getObservers()) {
            if (iobs instanceof BreakObserver) {
                hasBreakObserver = true;
            }
        }
        if (hasBreakObserver) {
            RESTQuery gqInstance = QueryHelper.copyWithEmptyCommand(query);
            gqInstance.setAttribute(Q_COMMAND_TYPE, "ADVANCE_STEP");
            PwQueryLinks.addLinkToQuery(fmt, gqInstance, "Step >>>",
                    PwFormatter.CLASS_NAVBAR);
        }
        if (agent.getDebugInfo().getCurrentDebugEvent() != null) {
            fmt.addP("stopped at: " + agent.getDebugInfo().getCurrentDebugEvent(), "class="
                    + PwFormatter.CLASS_NAVBAR);
            // FIXME: this is not useful in the current form
            // PwBreakObserver.generate(fmt, agent, query);
        }
        String autoRefreshString = query.getAttribute(Q_AUTO_REFRESH);
        boolean autorefresh = (autoRefreshString != null) && autoRefreshString.equals("true");
        PwNavigationBar.addRefreshCheckbox(fmt, autorefresh);
        fmt.closeTD();
        // add some space
        fmt.openTD("style=\"width:30px;\"");
        fmt.add("   ");
        fmt.closeTD();
        fmt.openTD();
        //
        // Focus and shadow
        //
        fmt.openP("class=" + PwFormatter.CLASS_NAVBAR);
        fmt.add("Focus and shadow:");
        // FOCUS_VERBINSTANCES
        RESTQuery gq2 = QueryHelper.copyWithEmptyCommand(query);
        gq2.setAttribute(Q_QUERY_TYPE, "ALL_FOCUS_VERBINSTANCES");
        PwQueryLinks.addLinkToQuery(fmt, gq2, "Focus VIs", PwFormatter.CLASS_NAVBAR);
        fmt.add("&bull;");
        // SHADOW VIS
        gq2 = QueryHelper.copyWithEmptyCommand(query);
        gq2.setAttribute(Q_QUERY_TYPE, "SHADOW_VIS");
        PwQueryLinks.addLinkToQuery(fmt, gq2, "Shadow VIs", PwFormatter.CLASS_NAVBAR);
        fmt.add("&bull;");
        // FOCUS_INSTANCES
        gq2 = QueryHelper.copyWithEmptyCommand(query);
        gq2.setAttribute(Q_QUERY_TYPE, "ALL_FOCUS_INSTANCES");
        PwQueryLinks.addLinkToQuery(fmt, gq2, "Focus instances",
                PwFormatter.CLASS_NAVBAR);
        fmt.add("&bull;");
        // SHADOW_INSTANCES
        gq2 = QueryHelper.copyWithEmptyCommand(query);
        gq2.setAttribute(Q_QUERY_TYPE, "SHADOW_INSTANCES");
        PwQueryLinks.addLinkToQuery(fmt, gq2, "Shadow instances",
                PwFormatter.CLASS_NAVBAR);
        fmt.closeP();
        //
        // Headless shadows
        //
        fmt.openP("class=" + PwFormatter.CLASS_NAVBAR);
        fmt.add("Headless");
        // HEADLESS_SHADOWS
        gq2 = QueryHelper.copyWithEmptyCommand(query);
        gq2.setAttribute(Q_QUERY_TYPE, "HEADLESS_COMPONENTS");
        PwQueryLinks.addLinkToQuery(fmt, gq2, "Headless: ", PwFormatter.CLASS_NAVBAR);
        // choices
        gq2 = QueryHelper.copyWithEmptyCommand(query);
        gq2.setAttribute(Q_QUERY_TYPE, "ALL_CHOICES");
        gq2.setAttribute(Q_SORTED_BY, "SORTED_BY_DEPENDENT_SCORE");
        PwQueryLinks.addLinkToQuery(fmt, gq2, "Choices", PwFormatter.CLASS_NAVBAR);
        fmt.add("&bull;");
        // hls supported
        gq2 = QueryHelper.copyWithEmptyCommand(query);
        gq2.setAttribute(Q_QUERY_TYPE, "ALL_HLSS");
        gq2.setAttribute(Q_SORTED_BY, "SORTED_BY_CONTINUATION");
        PwQueryLinks.addLinkToQuery(fmt, gq2, "HLSs", PwFormatter.CLASS_NAVBAR);
        fmt.add("&bull;");
        // hls new instance
        gq2 = QueryHelper.copyWithEmptyCommand(query);
        gq2.setAttribute(Q_QUERY_TYPE, "ALL_HLS_NEW_INSTANCES");
        gq2.setAttribute(Q_SORTED_BY, "SORTED_BY_STRENGTH");
        PwQueryLinks.addLinkToQuery(fmt, gq2, "HlsNewInstance",
                PwFormatter.CLASS_NAVBAR);
        fmt.add("&bull;");
        // FSLs
        gq2 = QueryHelper.copyWithEmptyCommand(query);
        gq2.setAttribute(Q_QUERY_TYPE, "ALL_FSLS");
        gq2.setAttribute(Q_SORTED_BY, "SORTED_BY_STRENGTH");
        PwQueryLinks.addLinkToQuery(fmt, gq2, "FSLs", PwFormatter.CLASS_NAVBAR);
        fmt.add("&bull;");
        // FSLIs
        gq2 = QueryHelper.copyWithEmptyCommand(query);
        gq2.setAttribute(Q_QUERY_TYPE, "ALL_FSLIS");
        gq2.setAttribute(Q_SORTED_BY, "SORTED_BY_STRENGTH");
        PwQueryLinks.addLinkToQuery(fmt, gq2, "FSLIs", PwFormatter.CLASS_NAVBAR);
        //fmt.add("&bull;");
        fmt.closeP();
        //
        //  Static
        //
        fmt.openP("class=" + PwFormatter.CLASS_NAVBAR);
        fmt.add("Static");
        // SOSPs
        // StaticHlss
        gq2 = QueryHelper.copyWithEmptyCommand(query);
        gq2.setAttribute(Q_QUERY_TYPE, "ALL_STATIC_HLSS");
        PwQueryLinks.addLinkToQuery(fmt, gq2, "StaticHLSs", PwFormatter.CLASS_NAVBAR);
        fmt.add("&bull;");
        gq2 = QueryHelper.copyWithEmptyCommand(query);
        gq2.setAttribute(Q_QUERY_TYPE, "ALL_STATIC_FSLIS");
        PwQueryLinks.addLinkToQuery(fmt, gq2, "StaticFSLIs", PwFormatter.CLASS_NAVBAR);
        fmt.add("&bull;");
        gq2 = QueryHelper.copyWithEmptyCommand(query);
        gq2.setAttribute(Q_QUERY_TYPE, "ALL_SOSPS");
        PwQueryLinks.addLinkToQuery(fmt, gq2, "SOSPs", PwFormatter.CLASS_NAVBAR);
        // StaticFSLIs
        fmt.closeP();
        //fmt.closeP();
        //
        // General stuff: first line: loop, history, performance, parameters, logs
        //
        //
        fmt.openP("class=" + PwFormatter.CLASS_NAVBAR);
        // LOOP
        gq2 = QueryHelper.copyWithEmptyCommand(query);
        gq2.setAttribute(Q_QUERY_TYPE, "DAS");
        PwQueryLinks.addLinkToQuery(fmt, gq2, "DAs", PwFormatter.CLASS_NAVBAR);
        fmt.add("&bull;");
        // LOOP
        gq2 = QueryHelper.copyWithEmptyCommand(query);
        gq2.setAttribute(Q_QUERY_TYPE, "LOOP");
        PwQueryLinks.addLinkToQuery(fmt, gq2, "Loop", PwFormatter.CLASS_NAVBAR);
        fmt.add("&bull;");
        // HISTORY
        gq2 = QueryHelper.copyWithEmptyCommand(query);
        gq2.setAttribute(Q_QUERY_TYPE, "LOOP_HISTORY");
        PwQueryLinks.addLinkToQuery(fmt, gq2, "History", PwFormatter.CLASS_NAVBAR);
        fmt.add("&bull;");
        // PERFORMANCE
        gq2 = QueryHelper.copyWithEmptyCommand(query);
        gq2.setAttribute(Q_QUERY_TYPE, "PERFORMANCE");
        PwQueryLinks.addLinkToQuery(fmt, gq2, "Performance",
                PwFormatter.CLASS_NAVBAR);
        fmt.add("&bull;");
        // PARAMETERS
        gq2 = QueryHelper.copyWithEmptyCommand(query);
        gq2.setAttribute(Q_QUERY_TYPE, "PARAMETERS");
        PwQueryLinks.addLinkToQuery(fmt, gq2, "Parameters", PwFormatter.CLASS_NAVBAR);
        fmt.add("&bull;");
        // LOGS
        gq2 = QueryHelper.copyWithEmptyCommand(query);
        gq2.setAttribute(Q_QUERY_TYPE, "LOGGER");
        PwQueryLinks.addLinkToQuery(fmt, gq2, "Logs", PwFormatter.CLASS_NAVBAR);
        fmt.closeP();
        //
        // General stuff, second line: concepts, verbs, concept words, verb words, 
        //    am instances, am VIs
        //
        // Memory
        fmt.openP("class=" + PwFormatter.CLASS_NAVBAR);
        // Concepts
        gq2 = QueryHelper.copyWithEmptyCommand(query);
        gq2.setAttribute(Q_QUERY_TYPE, "ALL_CONCEPTS");
        PwQueryLinks.addLinkToQuery(fmt, gq2, "Concepts", PwFormatter.CLASS_NAVBAR);
        fmt.add("&bull;");
        // verbs
        gq2 = QueryHelper.copyWithEmptyCommand(query);
        gq2.setAttribute(Q_QUERY_TYPE, "ALL_VERBS");
        PwQueryLinks.addLinkToQuery(fmt, gq2, "Verbs", PwFormatter.CLASS_NAVBAR);
        fmt.add("&bull;");
        // concept words
        gq2 = QueryHelper.copyWithEmptyCommand(query);
        gq2.setAttribute(Q_QUERY_TYPE, "ALL_CONCEPT_WORDS");
        PwQueryLinks.addLinkToQuery(fmt, gq2, "Concept words",
                PwFormatter.CLASS_NAVBAR);
        fmt.add("&bull;");
        // verb words
        gq2 = QueryHelper.copyWithEmptyCommand(query);
        gq2.setAttribute(Q_QUERY_TYPE, "ALL_VERB_WORDS");
        PwQueryLinks.addLinkToQuery(fmt, gq2, "Verb words", PwFormatter.CLASS_NAVBAR);
        fmt.add("&bull;");
        // autobiographical memory instances
        gq2 = QueryHelper.copyWithEmptyCommand(query);
        gq2.setAttribute(Q_QUERY_TYPE, "ALL_MEMORY_INSTANCES");
        gq2.setAttribute(Q_CURSOR_TOTAL, "0");
        gq2.setAttribute(Q_SORTED_BY, "SORTED_BY_ID");
        PwQueryLinks.addLinkToQuery(fmt, gq2, "Memory instances",
                PwFormatter.CLASS_NAVBAR);
        fmt.add("&bull;");
        // autobiographical memory verb instances
        gq2 = QueryHelper.copyWithEmptyCommand(query);
        gq2.setAttribute(Q_QUERY_TYPE, "ALL_MEMORY_VIS");
        gq2.setAttribute(Q_CURSOR_TOTAL, "0");
        gq2.setAttribute(Q_SORTED_BY, "SORTED_BY_ID");
        PwQueryLinks.addLinkToQuery(fmt, gq2, "Memory VIs", PwFormatter.CLASS_NAVBAR);
        // if there is a RsTestingUnit, add link to it
        if (agent.getDebugInfo().getRsTestingUnit() != null) {
            fmt.add("&bull;");
            gq2 = QueryHelper.copyWithEmptyCommand(query);
            gq2.setAttribute(Q_QUERY_TYPE, "RS_TESTING_UNIT");
            PwQueryLinks.addLinkToQuery(fmt, gq2, "RsTestingUnit",
                    PwFormatter.CLASS_NAVBAR);
        }
        // end of list of links
        fmt.closeP();
        fmt.closeTD();
        fmt.closeTR();
        fmt.closeTable();
        fmt.closeDiv();
        return fmt.toString();
    }

}

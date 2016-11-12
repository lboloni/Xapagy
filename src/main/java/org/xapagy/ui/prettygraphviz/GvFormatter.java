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
package org.xapagy.ui.prettygraphviz;

import java.util.Stack;

import org.xapagy.ui.formatters.Formatter;

/**
 * Special version of formatter, for the purpose of formatting to GraphViz
 * 
 * @author Ladislau Boloni
 * Created on: Apr 3, 2012
 */
public class GvFormatter extends Formatter {

    private enum OpenType {
        GRAPH, LINK, NODE, SUBGRAPH
    }

    public LabelHandler lh;;
    private Stack<OpenType> stack;

    public GvFormatter() {
        lh = new LabelHandler();
        stack = new Stack<>();
    }

    /**
     * Closes an open element, guesses based on the stack
     */
    public void close() {
        OpenType closed = stack.pop();
        deleteSeparator();
        switch (closed) {
        case GRAPH:
            closeDiGraph();
            return;
        case NODE:
            closeNode();
            return;
        case LINK:
            closeNode();
            return;
        case SUBGRAPH:
            closeSubGraph();
            return;
        }
    }

    public void closeDiGraph() {
        deindent();
        add("}");
    }

    /**
     * Closes a node
     * 
     * @param id
     */
    private void closeNode() {
        deindent();
        add("]");
    }

    /**
     * Closes a subgraph
     */
    private void closeSubGraph() {
        deindent();
        add("}");
    }

    /**
     * Creep back and delete the last, unnecessary separator
     */
    private void deleteSeparator() {
        int pos = buffer.length() - 2;
        char c = buffer.charAt(pos);
        if (c == ';' || c == ',') {
            buffer.deleteCharAt(pos);
        }
    }

    /**
     * Adds an equality in one
     * 
     * @param name
     * @param value
     */
    @Override
    public void is(String name, Object value) {
        OpenType current = stack.peek();
        switch (current) {
        case GRAPH:
        case SUBGRAPH:
            add(name + "=\"" + value + "\";");
            break;
        case LINK:
        case NODE:
            add(name + "=\"" + value + "\",");
            break;
        }
    }

    /**
     * Sets the label parameter, preprocesses the label
     * 
     * @param value
     */
    public void label(String value) {
        String l2 = GraphVizHelper.formatLabel(value);
        is("label", l2);
    }

    /**
     * Opens a digraph and sets some default parameters
     */
    public void openDiGraph() {
        stack.push(OpenType.GRAPH);
        add("digraph {");
        indent();
        // is("rankdir", "LR");
        // is("splines", "ortho");
        // is("compound", true);
    }

    /**
     * @param instance
     * @param object
     */
    public void openLink(String id1, String id2) {
        stack.push(OpenType.NODE);
        String id1b = GraphVizHelper.formatIdentifier(id1);
        String id2b = GraphVizHelper.formatIdentifier(id2);
        add(id1b + "->" + id2b + " [");
        indent();
        is(GvParameters.WEIGHT, "1.2");
    }

    /**
     * Opens a node
     * 
     * @param id
     */
    public void openNode(String id) {
        stack.push(OpenType.NODE);
        String id2 = GraphVizHelper.formatIdentifier(id);
        add(id2 + " [");
        indent();
        is(GvParameters.NODESEP, "0.7");
        is(GvParameters.RANKSEP, "0.7");
    }

    /**
     * Opens a subgraph
     * 
     * @param id
     */
    public void openSubGraph(String id) {
        stack.push(OpenType.SUBGRAPH);
        String id2 = GraphVizHelper.formatIdentifier(id);
        add("subgraph cluster_" + id2 + " {");
        indent();
    }

}

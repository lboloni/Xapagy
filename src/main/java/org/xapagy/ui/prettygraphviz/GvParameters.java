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

/**
 * A class representing the parameters of the GraphViz representation of a graph
 * 
 * @author Ladislau Boloni
 * Created on: Apr 4, 2012
 */
public class GvParameters {

    // here
    public static final String BASEFONT = "Helvetica"; // was "Verdana"
    public static final String COLOR = "color";
    public static final String FILLCOLOR = "fillcolor";
    public static final String FONTCOLOR = "fontcolor";
    // constants
    public static final String FONTNAME = "fontname";
    public static final String FONTSIZE = "fontsize";
    public static final String NODESEP = "nodesep";
    public static final String PENWIDTH = "penwidth";
    public static final String RANKSEP = "ranksep";
    public static final String SHAPE = "shape";
    public static final String SPLINES = "splines";
    public static final String STYLE = "style";
    public static final String WEIGHT = "weight";

    public String fontname = GvParameters.BASEFONT;
    public int fontsize = 10;
    public String instance_color_active = "black";

    public String instance_color_inactive = "gray70";
    public String instance_fillcolor_group = "cadetblue1";
    public String instance_fillcolor_regular = "white";
    public String instance_fontcolor_active = "black";
    public String instance_fontcolor_inactive = "gray70";

    public String instance_shape = "box";
    public String instance_style = "filled";
    // instances
    public int instance_wrap = 20;

    // coincidence links
    public String link_color_coincidence = "black";
    // quote links
    public String link_color_quote = "chocolate4";
    // links between verb instances
    public String link_color_successor = "gray";
    public String link_color_summarization = "blue";
    public String link_color_summarization_begin = "greenyellow";
    public String link_color_summarization_body = "green";
    public String link_color_summarization_close = "green4";

    public double link_penwidth_coincidence = 3;
    public double link_penwidth_quote = 3;
    public double link_weight_coincidence = 10;
    public double link_weight_quote = 100;
    public String relations_color_identity = "black";
    public String relations_color_nonIdentity = "blue";
    public String relations_color_sceneFictionalFuture = "blue";
    public String relations_color_sceneSuccession = "black";
    public String relations_color_sceneView = "green";

    public boolean relations_show_identity = true;
    public boolean relations_show_nonIdentityRelations = true;
    public String relations_style_identity = "dotted";
    public String scene_color = "gray90";
    // scenes
    public String scene_fontname = GvParameters.BASEFONT + " bold";
    public int scene_fontsize = 12;
    public String scene_style = "filled";
    /**
     * How are the splines drawn: "curved" - does not seem to show labels
     * "ortho" - it puts the labels to the wrong place "spline"
     */
    public String splines = "spline"; // was "ortho"
    public String vi_fillcolor_action = "white";
    public String vi_fillcolor_other = "gray";
    // verb instances
    public String vi_shape = "box";

    public String vi_style = "filled";
    public int vi_wrap = 20;
}

/*
   This file is part of the Xapagy project
   Created on: Apr 11, 2013
 
   org.xapagy.ui.prettyhtml.ColorCode
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettyhtml;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a 4 item color code associated with Xapagy entities,
 * which makes GUI representations easier
 * 
 * @author Ladislau Boloni
 * 
 */
public class ColorCode {

    /**
     * A list of 10 colors
     */
    public static List<Color> colorList;

    static {
        ColorCode.colorList = new ArrayList<>();
        ColorCode.colorList.add(Color.decode("#DDDDDD")); // light gray
        ColorCode.colorList.add(Color.decode("#FFDADA")); // pink
        ColorCode.colorList.add(Color.decode("#B2E6FF")); // pastel blue
        ColorCode.colorList.add(Color.decode("#B8FFB8")); // pastel green
        ColorCode.colorList.add(Color.decode("#FFFF99")); // pastel yellow
        ColorCode.colorList.add(Color.decode("#0000EE")); // blue
        ColorCode.colorList.add(Color.decode("#EE0000")); // red
        ColorCode.colorList.add(Color.decode("#00B85C")); // green
        ColorCode.colorList.add(Color.decode("#6600CC")); // purple
        ColorCode.colorList.add(Color.decode("#444444")); // almost black
    }

    private Color colorFirst = null;
    private Color colorSecond = null;
    private Color colorThird = null;

    /**
     * @param colorFirst
     * @param colorSecond
     * @param colorThird
     */
    public ColorCode(Color colorFirst, Color colorSecond, Color colorThird) {
        super();
        this.colorFirst = colorFirst;
        this.colorSecond = colorSecond;
        this.colorThird = colorThird;
    }

    public Color getColorFirst() {
        return colorFirst;
    }

    public Color getColorSecond() {
        return colorSecond;
    }

    public Color getColorThird() {
        return colorThird;
    }

}

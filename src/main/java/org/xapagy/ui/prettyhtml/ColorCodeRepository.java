/*
   This file is part of the Xapagy project
   Created on: Apr 11, 2013
 
   org.xapagy.ui.prettyhtml.ColorCodeRepository
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettyhtml;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * This class associates a ColorCode to every XapagyComponent it has
 * 
 * @author Ladislau Boloni
 * 
 */
public class ColorCodeRepository {

    private Map<Object, ColorCode> matching = new HashMap<>();

    /**
     * Returns the color code for a specific Xapagy component. If it has already
     * be assigned, return it. Otherwise generate a new color code. It will
     * cycle after 1000
     * 
     * @return
     */
    public ColorCode getColorCode(Object xc) {
        ColorCode retval = matching.get(xc);
        if (retval == null) {
            int count = matching.values().size() + 1;
            int x = 0;
            Color colorFirst = null;
            Color colorSecond = null;
            Color colorThird = null;
            // first color
            x = count % ColorCode.colorList.size();
            colorFirst = ColorCode.colorList.get(x);
            count = count / ColorCode.colorList.size();
            if (count != 0) {
                // second color
                x = count % ColorCode.colorList.size();
                colorSecond = ColorCode.colorList.get(x);
                if (colorSecond.equals(colorFirst)) {
                    colorFirst = colorFirst.brighter();
                    colorSecond = colorSecond.darker();
                }
                count = count / ColorCode.colorList.size();
                // third color
                if (count != 0) {
                    x = count % ColorCode.colorList.size();
                    colorThird = ColorCode.colorList.get(x);
                    count = count / ColorCode.colorList.size();
                }
            }
            retval = new ColorCode(colorFirst, colorSecond, colorThird);
            matching.put(xc, retval);
        }
        return retval;
    }

}

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

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * This class associates a ColorCode to every XapagyComponent it has
 * 
 * @author Ladislau Boloni
 * Created on: Apr 11, 2013
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

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
package org.xapagy.ui.prettyprint;

import java.util.List;

import org.xapagy.autobiography.ABStory;
import org.xapagy.ui.TextUiHelper;

/**
 * @author Ladislau Boloni
 * Created on: Jul 9, 2011
 */
public class PpStory {

    /**
     * Simple printing of an ABStory to allow turning it on and off
     * 
     * @param story
     */
    public static String pp(String header, ABStory story) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(TextUiHelper.createHeader(header));
        for (String line : story.getLines()) {
            buffer.append(line + "\n");
        }
        return buffer.toString();
    }

    /**
     * simple printing... to allow turning it on and off
     * 
     * @param story
     */
    public static String pp(String header, List<String> story) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(TextUiHelper.createHeader(header));
        for (String line : story) {
            buffer.append(line + "\n");
        }
        return buffer.toString();
    }

}

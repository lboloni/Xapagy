/*
   This file is part of the Xapagy project
   Created on: Jul 9, 2011
 
   org.xapagy.ui.prettyprint.PpStory
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettyprint;

import java.util.List;

import org.xapagy.autobiography.ABStory;
import org.xapagy.ui.TextUiHelper;

/**
 * @author Ladislau Boloni
 * 
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

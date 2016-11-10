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
package org.xapagy.util;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.xapagy.ui.TextUi;

/**
 * 
 * <code>org.xapagy.util.ClassResourceHelper</code>
 * 
 * Contains code which simplifies addressing the correct class resource
 * 
 * 
 * @author Ladislau Boloni (lotzi.boloni@gmail.com)
 * Created on: Nov 16, 2008
 */
public class ClassResourceHelper {

    /**
     * Returns the name of the resource file in the form of a string
     * 
     * @param resource
     * @return
     */
    public static String getResourceContent(Object object, String resource) {
        URL testURL = object.getClass().getResource(resource);
        String testFile = null;
        try {
            testFile = new URI(testURL.toString()).getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            // should not normally happen
        }
        File file = new File(testFile);
        if (!file.exists()) {
            throw new Error("File: " + testFile + " does not exist");
        }
        return testFile;
    }

    /**
     * Returns the resource file in the form of a File object
     * 
     * @param resource
     * @return
     * @throws URISyntaxException
     */
    @SuppressWarnings("rawtypes")
    public static File getResourceFile(Object object, String resource) {
        URL urlL = null;
        if (object instanceof Class) {
            urlL = ((Class) object).getResource(resource);
        } else {
            urlL = object.getClass().getResource(resource);
        }
        if (urlL == null) {
            TextUi.errorPrint("Could not find resource:" + resource);
            throw new Error("Could not find resource:" + resource);
        }
        // String testFile = null;
        URI uri = null;
        try {
            // testFile = new URI(urlL.toString()).getPath();
            uri = urlL.toURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        // File file = new File(testFile);
        File file = new File(uri);
        return file;
    }

}

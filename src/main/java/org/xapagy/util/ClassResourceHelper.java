/*
   This file is part of the Golyoscsapagy project
   Created on: Nov 16, 2008
 
   golyoscsapagy.util.ResourceHelper
 
   Copyright (c) 2008 Ladislau Boloni
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

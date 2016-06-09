/*
   This file is part of the Xapagy project
   Created on: Nov 16, 2015
 
   org.xapagy.domain.IncludeHelper
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.domain;

import java.io.File;

import org.xapagy.ui.TextUi;
import org.xapagy.util.ClassResourceHelper;

/**
 * This file contains helpers which allow the XapiLang parser to conveniently
 * include the various Xapi files (particularly the standard ones from the
 * domain directories)
 * 
 * @author Ladislau Boloni
 *
 */
public class IncludeHelper {
    /**
     * The prefix used when referring to files directly
     */
    public static final String FILE_PREFIX = "file:";
    /**
     * The Xapi extension
     */
    public static final String FILE_EXTENSION = ".xapi";

    /**
     * Finds the Xapi file for inclusion based on a set of rules
     * 
     * <ul>
     * <li> If the includename is starts with the file: prefix, takes the rest precisely as it is typed
     * <li> 
     * </ul>
     * 
     * @param includeName
     * @return
     */
    public static File findXapiFile(String includeName) {
        // if specified as an explicit path
        if (includeName.startsWith(FILE_PREFIX)) {
            String filePath = includeName.substring(FILE_PREFIX.length());
            File retval = new File(filePath);
            return retval;
        }
        // 
        String name = includeName;
        if (!name.endsWith(FILE_EXTENSION)) {
            name = name + FILE_EXTENSION;
        }
        // try the local directory:
        File dir = new File(".");
        File retval = new File(dir, name);
        if (retval.exists()) {
            return retval;
        }
        // try the local domain directory
        dir = new File("domain");
        retval = new File(dir, name);
        if (retval.exists()) {
            return retval;
        }
        retval = ClassResourceHelper.getResourceFile(IncludeHelper.class,
                    name);
        if (retval.exists()) {
            return retval;
        }
        // if even now is not there, time to give up.
        TextUi.abort(
                    "Could not resolve" + includeName + " to a valid file.");
        return retval;
    }

}

/*
 *This file is part of the Golyoscsapagy project
 * Created on Apr 4, 2007
 *
 */
package org.xapagy.util;

import java.io.File;

import org.xapagy.ui.TextUi;

public class DirUtil {
    /**
     * Guarantees that a given directory exists or exits!!!
     * 
     * @param dir
     * @return
     */
    public static File guaranteeDirectory(final String dir) {
        final File outputDir = new File(dir);
        if (!outputDir.isDirectory()) {
            TextUi.errorPrint("Output directory: " + outputDir
                    + " does not exist!");
            if (!outputDir.mkdirs()) {
                TextUi.errorPrint("Creation of output directory: " + outputDir
                        + " unsuccessful.");
                System.exit(1);
            } else {
                return outputDir;
            }
        }
        return outputDir;
    }
}

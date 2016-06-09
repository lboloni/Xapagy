/*
 * Created on Apr 7, 2007
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.xapagy.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import org.xapagy.ui.TextUi;

public class FileWritingUtil {
    /**
     * Writes the buffer to a Matlab file
     * 
     * @param file
     * @param buffer
     * @throws IOException
     */
    public static void appendToTextFile(final File file, final String text)
            throws IOException {
        try (FileOutputStream out = new FileOutputStream(file, true);
                PrintWriter pw = new PrintWriter(out);) {
            pw.write(text);
            pw.flush();
            pw.close();
            out.close();
        }
    }

    /**
     * A helper function, which very agressively tries to find a place to write
     * the output stream
     * 
     * @param fileName
     * @return
     */
    private static FileOutputStream createOutputStream(File file) {
        final File dir = file.getParentFile();
        if (dir != null && !dir.exists()) {
            dir.mkdir();
        }
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
        } catch (final FileNotFoundException e) {
            TextUi.println("Can not create file:" + file);
            File fileReplacement = TextUi.inputFile("Enter new file:", false);
            try {
                out = new FileOutputStream(fileReplacement);
            } catch (final FileNotFoundException e1) {
                throw new Error("This is still not working.");
            }
            e.printStackTrace();
        }
        return out;
    }

    /**
     * Writes the buffer to a Matlab file
     * 
     * @param file
     * @param buffer
     * @throws IOException
     */
    public static void writeToTextFile(final File file, final String text)
            throws IOException {
        try (FileOutputStream out = FileWritingUtil.createOutputStream(file);
                PrintWriter pw = new PrintWriter(out);) {
            pw.write(text);
            pw.flush();
            pw.close();
            out.close();
        }
    }
}

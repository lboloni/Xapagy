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
package org.xapagy.ui;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SaveLoadUtil<T> {

    public SaveLoadUtil() {

    }

    /**
     * Loads the knowledgebase from a file
     * 
     * @param file
     * @return the KB if successful, null if not
     */
    @SuppressWarnings("unchecked")
    public T load(final File file) {
        T data = null;
        // ObjectInputStream in;
        try (ObjectInputStream in =
                new ObjectInputStream(new BufferedInputStream(
                        new FileInputStream(file)))) {
            // in =
            // new ObjectInputStream(new BufferedInputStream(
            // new FileInputStream(file)));
            data = (T) in.readObject();

        } catch (final FileNotFoundException e) {
            TextUi.errorPrint("FileNotFoundException while loading from file: "
                    + file.getAbsolutePath());
            return null;
        } catch (final IOException e) {
            TextUi.errorPrint("IOException while loading from file: "
                    + file.getAbsolutePath() + "\n" + e.getMessage());
            return null;
        } catch (final ClassNotFoundException e) {
            TextUi.errorPrint("ClassNotFoundException while loading from file: "
                    + file.getAbsolutePath());
            return null;
        }
        return data;

    }

    /**
     * Saves the knowledgebase to a file.
     * 
     * @param data
     * @param file
     * @return true if successful, false if not
     */
    public boolean save(final T data, final File file) {
        // ObjectOutputStream out;
        try (ObjectOutputStream out =
                new ObjectOutputStream(new BufferedOutputStream(
                        new FileOutputStream(file)))) {
            out.writeObject(data);
            out.flush();
            out.close();
        } catch (final FileNotFoundException e) {
            TextUi.errorPrint("FileNotFoundException while saving the KB to file: "
                    + file.getAbsolutePath());
            return false;
        } catch (final IOException e) {
            TextUi.errorPrint("IOException while saving the KB to file: "
                    + file.getAbsolutePath());
            e.printStackTrace();
            return false;
        }
        return true;
    }

}

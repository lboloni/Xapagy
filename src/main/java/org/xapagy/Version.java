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
package org.xapagy;

import java.io.FileOutputStream;
import java.util.Properties;

public class Version {

	// Use: Development, Alpha, Beta, Release
	/**
	 * Description of the Field
	 */
	public enum VersionStatus {
		Alpha, Beta, Development, Release
	}

	public static final String PROJECTNAME = "Xapagy";
	public static String VERSION_DATE = "June 18, 2017";
	public static int VERSION_MAJOR = 1;
	public static int VERSION_MICRO = 560;
	public static int VERSION_MINOR = 0;
	public static VersionStatus VERSION_STATUS = VersionStatus.Beta;

	/**
	 * Creates a property file for the use of the ant generator
	 * 
	 * @param args
	 *            The command line arguments
	 */
	public static void main(final String[] args) {
		System.out.println("Created the version property:"
				+ Version.versionNumberString());
		try (FileOutputStream fos = new FileOutputStream(
				"version.properties")) {
			final Properties property = new Properties();
			property.setProperty("version", Version.versionNumberString());
			property.store(fos, "Xapagy");
			fos.close();
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @return Description of the Return Value
	 */
	public static int versionNumber() {
		return 10000 * Version.VERSION_MAJOR + 100 * Version.VERSION_MINOR
				+ Version.VERSION_MICRO;
	}

	/**
	 * Description of the Method
	 * 
	 * @return Description of the Return Value
	 */
	public static String versionNumberString() {
		return "" + Version.VERSION_MAJOR + "." + Version.VERSION_MINOR + "."
				+ Version.VERSION_MICRO;
	}

	/**
	 * Description of the Method
	 * 
	 * @return Description of the Return Value
	 */
	public static String versionString() {
		return Version.PROJECTNAME + " " + Version.VERSION_MAJOR + "."
				+ Version.VERSION_MINOR + "." + Version.VERSION_MICRO + " ("
				+ Version.VERSION_DATE + ")";
	}

	public static String versionStringNoDate() {
		return Version.PROJECTNAME + " " + Version.VERSION_MAJOR + "."
				+ Version.VERSION_MINOR + "." + Version.VERSION_MICRO;
	}

}

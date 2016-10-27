/*
   This file is part of the Xapagy project
   Created on: Aug 20, 2010
 
   org.xapagy.storyvisualizer.agent.AgentHelperUiStubs
 
   Copyright (c) 2008-2014 Ladislau Boloni
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
    public static String VERSION_DATE = "Oct 27, 2016";
    public static int VERSION_MAJOR = 1;
    public static int VERSION_MICRO = 532;
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
        try (FileOutputStream fos = new FileOutputStream("version.properties")) {
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

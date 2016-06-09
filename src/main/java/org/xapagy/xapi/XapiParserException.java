/*
   This file is part of the Xapagy project
   Created on: Mar 22, 2015

   org.xapagy.xapi.XapiParserException

   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.xapi;

/**
 * An exception thrown when a regular Xapi statement was malformed
 * @author Ladislau Boloni
 *
 */
public class XapiParserException extends Exception {

    private static final long serialVersionUID = -6351708254865469908L;
    private String diagnosis;

    /**
     * @param diagnosis
     */
    public XapiParserException(String diagnosis) {
        super();
        this.diagnosis = diagnosis;
    }

    /**
     * @return the diagnosis
     */
    public String getDiagnosis() {
        return diagnosis;
    }

}

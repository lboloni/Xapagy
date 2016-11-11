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
package org.xapagy.xapi.reference;

import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.ui.prettyprint.PrettyPrint;

/**
 * 
 * A statement in the Xapi L2 language. It can be decomposed into one or more
 * XapiReferences (typically a create + action + wait)
 * 
 * In the current version, this can be connected to a specific line in the Xapi
 * text, thus it has these debug items.
 * 
 * @author Ladislau Boloni
 * 
 */
public class XrefStatement extends AbstractXrefVi {

    private static final long serialVersionUID = -1460455518248121883L;
    private String debugFileName = null;
    private int debugLineNo = -1;
    protected String label;
    private double timeWait = 1.0; // default wait


    /**
     * Constructor for initializing the statement
     * 
     * @param subject
     * @param verb
     * @param object
     */
    public XrefStatement(String text, String label, ViType statementType,
            XapiReference subject, XapiReference verb, XapiReference object,
            double timeWait) {
        super(XapiReferenceType.STATEMENT, null,
                XapiPositionInParent.NO_PARENT, text);
        // set itself as the parent for these!!!
        subject.setParent(this);
        verb.setParent(this);
        if (object != null) {
            object.setParent(this);
        }
        this.label = label;
        this.statementType = statementType;
        // if (!statementType.equals(ViType.QUOTE)) {
        initFromReferences(subject, verb, object, null);
        // } else {
        // throw new Error("This constructor cannot initialize a quote!!!");
        // }
        this.timeWait = timeWait;
    }

    public String getDebugFileName() {
        return debugFileName;
    }

    public int getDebugLineNo() {
        return debugLineNo;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @return the timeWait
     */
    public double getTimeWait() {
        return timeWait;
    }

    @Override
    public ViType getViType() {
        return statementType;
    }

    public void setDebugFileName(String debugFileName) {
        this.debugFileName = debugFileName;
    }

    public void setDebugLineNo(int debugLineNo) {
        this.debugLineNo = debugLineNo;
    }

    @Override
    public String toString() {
        return PrettyPrint.ppConcise(this, null);
    }

}

/*
   This file is part of the Xapagy project
   Created on: Nov 30, 2011
 
   org.xapagy.xapi.DecompositionHelper
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.xapi;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.ui.TextUi;
import org.xapagy.xapi.reference.XapiReference;
import org.xapagy.xapi.reference.XapiReference.XapiPositionInParent;
import org.xapagy.xapi.reference.XapiReference.XapiReferenceType;
import org.xapagy.xapi.reference.XrefStatement;
import org.xapagy.xapi.reference.XrefVi;
import org.xapagy.xapi.reference.XrefWait;

/**
 * The decomposition applies to higher level referential units which are
 * decomposed into directly executable units.
 * 
 * The first case is when a statement is decomposed into a VI + wait, or
 * possibly creation-VI + VI + wait
 * 
 * But in the future this is where we want to infer things such implicit
 * creation of new scenes etc.
 * 
 * @author Ladislau Boloni
 * 
 */
public class DecompositionHelper {

    /**
     * Decompose a statement into atomic statements (creation, action, wait)
     * 
     * FIXME: the assumption is that this is called on STATEMENT type
     * 
     * @return
     */
    public static List<XapiReference> decomposeStatementIntoVisAndWait(
            Agent agent, XrefStatement statement) {
        if (statement.getType() != XapiReferenceType.STATEMENT) {
            TextUi.println("decomposeIntoAtomicStatements should only be called on statement syntactic units!!!");
            System.exit(1);
        }
        // handle the case if this is already a wait statement:
        List<XapiReference> statementList = new ArrayList<>();
        if (statement.getType() == XapiReferenceType.WAIT) {
            statementList.add(statement);
            return statementList;
        }
        // check whether there is a creation
        int creationCount = ImplicitCreationHelper.countCreations(statement);
        if (creationCount > 1) {
            throw new Error(
                    "Xapi only allows at most 1 creation item per sentence.");
        }
        if (creationCount == 0) {
            XrefVi xapiVi =
                    new XrefVi(statement, XapiPositionInParent.ACTION,
                            statement.getText());
            statementList.add(xapiVi);
        } else {
            SimpleEntry<XrefVi, XrefVi> split =
                    ImplicitCreationHelper.splitCreation(agent, statement);
            XrefVi create = split.getKey();
            statementList.add(create);
            XrefVi action = split.getValue();
            statementList.add(action);
        }
        // add the wait statement
        XrefWait waitStatement =
                new XrefWait(statement.getTimeWait(), statement);
        statementList.add(waitStatement);
        // if we have a creation, add an extra wait statement...
        // otherwise we will exhaust the immediate Vi resource
        if (creationCount > 0) {
            waitStatement = new XrefWait(statement.getTimeWait(), statement);
            statementList.add(waitStatement);
        }
        return statementList;
    }

}

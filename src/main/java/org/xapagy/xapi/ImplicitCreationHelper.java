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
package org.xapagy.xapi;

import java.util.AbstractMap.SimpleEntry;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.concepts.Hardwired;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.xapi.reference.XapiReference;
import org.xapagy.xapi.reference.XapiReference.XapiLevel;
import org.xapagy.xapi.reference.XapiReference.XapiPositionInParent;
import org.xapagy.xapi.reference.XrefAdjective;
import org.xapagy.xapi.reference.XrefDirect;
import org.xapagy.xapi.reference.XrefNewInstance;
import org.xapagy.xapi.reference.XrefStatement;
import org.xapagy.xapi.reference.XrefVerb;
import org.xapagy.xapi.reference.XrefVi;

/**
 * Splits the implicit creation from the statements
 * 
 * @author Ladislau Boloni
 * Created on: May 21, 2011
 */
public class ImplicitCreationHelper {

    /**
     * Counts the number of create instance subsets in a statement. It there is
     * more than one, it is a trouble.
     * 
     * @param statement
     * @return
     */
    public static int countCreations(XrefStatement statement) {
        int count = 0;
        XrefStatement cursor = statement;
        while (cursor != null) {
            if (cursor.getSubject().getType() == XapiReference.XapiReferenceType.TEMPL_INSTANCE) {
                count++;
            }
            if (cursor.getObject() != null
                    && cursor.getObject().getType() == XapiReference.XapiReferenceType.TEMPL_INSTANCE) {
                count++;
            }
            if (cursor.getViType() == ViType.QUOTE) {
                cursor = (XrefStatement) cursor.getQuote();
            } else {
                cursor = null;
            }
        }
        return count;
    }

    /**
     * Starting from an INSTANCE_NEW type of reference, create a statement which
     * creates it and return a reference to it as an existing component
     * 
     * @return
     */
    private static SimpleEntry<XrefVi, XapiReference>
            createCreationAndReference(Agent agent, XrefStatement statement,
                    XrefNewInstance reference, XapiReference refScene,
                    String label) {
        // create the create instance reference
        VerbOverlay voCreateInstance =
                VerbOverlay.createVO(agent, Hardwired.VM_CREATE_INSTANCE,
                        Hardwired.VR_SUBJECT_IS_SCENE);
        XapiReference refVerbCreateInstance =
                new XrefVerb(null, voCreateInstance, "create-instance",
                        XapiPositionInParent.VERB);
        // now create the instance and the adjective - the parent of the
        // adjective is the vi
        XrefVi xsCreationPart =
                new XrefVi(statement, XapiPositionInParent.IMPLICIT_CREATION,
                        ImplicitCreationHelper.creationLabel(label),
                        ViType.S_ADJ, refScene, refVerbCreateInstance, null);
        XrefAdjective refConcepts =
                new XrefAdjective(xsCreationPart, reference.getCo(),
                        reference.getText(), XapiPositionInParent.ADJECTIVE);
        xsCreationPart.setPart(ViPart.Adjective, refConcepts);
        // xsCreationPart.setPart(ViPart.Verb, refVerbCreateInstance);

        // the reference instance, the parent is the statement
        XapiReference refAsOld =
                new XrefDirect(null, XapiPositionInParent.SUBJECT,
                        reference.getCo(), reference.getText());
        return new SimpleEntry<>(xsCreationPart, refAsOld);
    }

    /**
     * For a label of a statement, returns the modified label which is going to
     * be assigned to the create statement part after the split
     * 
     * @return
     */
    public static final String creationLabel(String label) {
        if (label == null) {
            return null;
        }
        return label + ":create";
    }

    /**
     * Splits a sentence with an instance creation into the creation statement
     * (returned in the key) and the reference statement (returned in the value)
     * 
     * @return
     */
    public static SimpleEntry<XrefVi, XrefVi> splitCreation(Agent agent,
            XrefStatement statement) {
        XrefVi xsCreation = null;
        // the current end
        XrefVi xsCreationTail = null;
        XrefVi xsReference = null;
        // the current end
        XrefVi xsReferenceTail = null;
        XrefStatement cursor = statement;
        // create the scene reference
        ConceptOverlay coScene = new ConceptOverlay(agent);
        coScene.addFullEnergy(agent.getConceptDB()
                .getConcept(Hardwired.C_SCENE));
        XapiReference refScene =
                new XrefDirect(null, XapiPositionInParent.QUOTE_SCENE, coScene,
                        "scene");
        // create the splitted statements
        boolean splitHappened = false;
        while (cursor != null) {
            XrefVi xsReferencePart = null;
            XrefVi xsCreationPart = null;
            // creating in subject
            if (cursor.getSubject().getType() == XapiReference.XapiReferenceType.TEMPL_INSTANCE) {
                splitHappened = true;
                SimpleEntry<XrefVi, XapiReference> creation =
                        ImplicitCreationHelper.createCreationAndReference(
                                agent, cursor,
                                (XrefNewInstance) cursor.getSubject(),
                                refScene, cursor.getLabel());
                xsCreationPart = creation.getKey();
                xsReferencePart =
                        new XrefVi(
                                cursor,
                                XapiPositionInParent.IMPLICIT_CREATION_ACTIONPART,
                                null);
                creation.getValue().setParent(xsReferencePart);
                creation.getValue().setXapiLevel(XapiLevel.L0);
                xsReferencePart.setPart(ViPart.Subject, creation.getValue());
            }
            // creating in object
            if (cursor.getObject() != null
                    && cursor.getObject().getType() == XapiReference.XapiReferenceType.TEMPL_INSTANCE) {
                splitHappened = true;
                SimpleEntry<XrefVi, XapiReference> creation =
                        ImplicitCreationHelper.createCreationAndReference(
                                agent, cursor,
                                (XrefNewInstance) cursor.getObject(), refScene,
                                cursor.getLabel());
                xsCreationPart = creation.getKey();
                xsReferencePart =
                        new XrefVi(
                                cursor,
                                XapiPositionInParent.IMPLICIT_CREATION_ACTIONPART,
                                null);
                creation.getValue().setParent(xsReferencePart);
                creation.getValue().setXapiLevel(XapiLevel.L0);
                xsReferencePart.setPart(ViPart.Object, creation.getValue());
            }
            // if there was no splitting at this level, go down
            if (xsCreationPart == null) { // no splitting at this level
                xsReferencePart =
                        new XrefVi(cursor, XapiPositionInParent.ACTION,
                                cursor.getText());
                if (!splitHappened) {
                    xsCreationPart =
                            new XrefVi(cursor,
                                    XapiPositionInParent.IMPLICIT_CREATION,
                                    null);
                }
            }
            if (xsCreation == null) { // we are at the top level
                xsCreation = xsCreationPart;
                xsCreationTail = xsCreation;
                xsReference = xsReferencePart;
                xsReferenceTail = xsReference;
            } else {
                if (xsCreationPart != null) {
                    xsCreationTail.setQuoteStatement(xsCreationPart);
                    xsCreationTail = xsCreationPart;
                }
                xsReferenceTail.setQuoteStatement(xsReferencePart);
                xsReferenceTail = xsReferencePart;
            }
            // recurse
            if (cursor.getViType() == ViType.QUOTE) {
                refScene = cursor.getQuoteScene();
                cursor = (XrefStatement) cursor.getQuote();
            } else {
                cursor = null;
            }
        }
        return new SimpleEntry<>(xsCreation, xsReference);
    }
}

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

import java.io.Serializable;

import org.xapagy.reference.rrState;

/**
 * Container for a reference type
 * 
 * ADJECTIVE - a single CO which will be later added to an instance
 * 
 * ADVERB - a single VO which will be later added to a VI
 * 
 * REF_INSTANCE - a single CO which will be later resolved to an instance
 * 
 * TEMPL_INSTANCE - a single CO which will be later used to create an instance
 * 
 * WH_INSTANCE - a single CO which will be later used to create a question HLS
 * 
 * TEMPL_VERB - a single VO which will become the verb of a new VI
 * 
 * REF_VERBINSTANCE - a single VO which will be later resolved to an existing VI
 * in an adverb statement
 * 
 * REF_RELATIONAL - a chain of CO...VO...CO... etc. ending with a CO. The CO-s
 * will be resolved to instances connected by the VO type relations. The scope
 * of the relation is the first CO.
 * 
 * TEMPL_RELATIONAL - a chain of CO...VO...CO... etc. ending with a CO. The last
 * n-1 CO-s will be resolved to instances connected by the VO type relations.
 * The first CO will be created as new. The scope of the relation is the first
 * CO.
 * 
 * REF_GROUP - a list of COs, all of them must be related to instances, then the
 * list reference resolves to a group which has all these.
 * 
 * WAIT - a wait
 * 
 * STATEMENT
 * 
 * @author Ladislau Boloni
 * 
 */
public interface XapiReference extends Serializable {

    public enum XapiLevel {
        L0, L1
    }

    /**
     * The way in which the reference relates to the upper part
     * 
     * FIXME: the QUOTEVI_IN_STATEMENTQOUTE is not necessarily the best idea,
     * but this is how it was working out, see later.
     * 
     */
    public enum XapiPositionInParent {
        ACTION, ADJECTIVE, ADVERB, ADVERBSUBJECT, GROUP_MEMBER,
        IMPLICIT_CREATION, IMPLICIT_CREATION_ACTIONPART, NO_PARENT, OBJECT,
        QUOTE, QUOTE_SCENE, RELATION_INSTANCE, RELATION_RELATION, SUBJECT,
        VERB, WAIT
    }

    /**
     * The type of the reference - see in the upper comment for the type of
     * references
     * 
     * @author Ladislau Boloni
     * 
     */
    public enum XapiReferenceType {
        ADJECTIVE, ADVERB, ADVERB_SUBJECT, REF_DIRECT, REF_GROUP,
        REF_RELATIONAL, REF_VERBINSTANCE, STATEMENT, TEMPL_INSTANCE,
        TEMPL_RELATIONAL, VERB, VI, WAIT, WH_INSTANCE, MACRO
    }

    public XapiReference cloneL0(XrefVi xrefvi);

    public XapiReference getParent();

    public XapiPositionInParent getPositionInParent();

    public rrState getResolutionConfidence();

    public String getText();

    public XapiReferenceType getType();

    public XapiReference getUpperIdentity();

    public XapiLevel getXapiLevel();

    public void setParent(XapiReference parent);

    public void setResolutionConfidence(rrState resolutionConfidence);

    public void setUpperIdentity(XapiReference upperIdentity);

    public void setXapiLevel(XapiLevel xapiLevel);
}

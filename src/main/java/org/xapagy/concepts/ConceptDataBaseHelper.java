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
package org.xapagy.concepts;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map.Entry;

import org.xapagy.agents.Agent;
import org.xapagy.metaverbs.AbstractSaMetaVerb;

/**
 * A helper class for concept database, managing the subsets, supersets and
 * creation of these
 * 
 * These kind of things are just helpers for the program, so we don't put them
 * to ConceptDataBase itself.
 * 
 * @author Lotzi Boloni
 * 
 */
public final class ConceptDataBaseHelper<T extends AbstractConcept> {

    public enum ContentType {
        TYPE_CONCEPT, TYPE_VERB
    }

    private Agent agent;
    private AbstractConceptDB<T> cdb;
    private ContentType contentType;

    public ConceptDataBaseHelper(AbstractConceptDB<T> cdb,
            ContentType contentType, Agent agent) {
        this.cdb = cdb;
        this.contentType = contentType;
        this.agent = agent;
    }

    /**
     * This is a somewhat hackish method to fix the type erasure
     * 
     */
    @SuppressWarnings("unchecked")
    private T createT(String name, String spikeName) {
        switch (contentType) {
        case TYPE_CONCEPT: {
            String id = agent.getIdentifierGenerator().getConceptIdentifier();
            return (T) new Concept(name, id);
        }
        case TYPE_VERB: {
            String id = agent.getIdentifierGenerator().getVerbIdentifier();
            Verb verb = null;
            if (spikeName == null) {
                verb = new Verb(name, id, null);
                // cdb.addConcept((T) verb);
                // cdb.setArea((T) verb, 1.0); // no area specified
            } else {
                AbstractSaMetaVerb spike = null;
                try {
                    Class<AbstractSaMetaVerb> spikeClass =
                            (Class<AbstractSaMetaVerb>) Class
                                    .forName(spikeName);
                    Constructor<AbstractSaMetaVerb> constructor =
                            spikeClass.getConstructor(Agent.class);
                    spike = constructor.newInstance(agent);
                } catch (ClassNotFoundException e) {
                    throw new Error(e.toString());
                } catch (InstantiationException e) {
                    throw new Error(e.toString());
                } catch (IllegalAccessException e) {
                    throw new Error(e.toString());
                } catch (NoSuchMethodException e) {
                    throw new Error(e.toString());
                } catch (SecurityException e) {
                    throw new Error(e.toString());
                } catch (IllegalArgumentException e) {
                    throw new Error(e.toString());
                } catch (InvocationTargetException e) {
                    throw new Error(e.toString());
                }
                // cdb.addConcept((T) verb);
                // cdb.setArea((T) verb, 1.0); // no area specified
                verb = new Verb(name, id, spike);
            }
            return (T) verb;
        }
        }
        throw new Error("One should never reach this");
    }

    /**
     * Creates a new concept/verb with no metaverb attached and specified area
     * and adds it to the database
     * 
     * @param name
     * @param area
     *            - the area of the concept
     */
    public T createWithArea(String name, double area) {
        T concept = createT(name, null);
        cdb.addConcept(concept);
        cdb.setArea(concept, area);
        return concept;
    }

    /**
     * Creates a new verb with the specified area and metaverb, and adds it to
     * the database.
     * 
     * @param name
     * @param area
     *            - the area of the verb
     * @param metaverb
     *            - the full path to the metaverb
     */
    public T createWithAreaAndMetaverb(String name, double area,
            String metaverb) {
        T concept = createT(name, metaverb);
        cdb.addConcept(concept);
        cdb.setArea(concept, area);
        return concept;
    }

    /**
     * Implies an activation of the other side of the given value
     * 
     * @param concept1
     * @param concept2
     * @param activation
     */
    public void makeImpact(String conceptName1, String conceptName2,
            double activation) {
        T concept1 = cdb.getConcept(conceptName1);
        T concept2 = cdb.getConcept(conceptName2);
        cdb.setImpact(concept1, concept2, activation);
    }

    /**
     * Creates the negation of the concept. Returns null if we are asking for a
     * double negation
     * 
     * @param conceptName
     */
    public T makeNegation(String conceptName) {
        // don't make double negations
        if (conceptName.startsWith(Hardwired.CONCEPT_PREFIX_NEGATION)) {
            return null;
        }
        String negationName = AbstractConceptDB.getNegationName(conceptName);
        T concept = cdb.getConcept(conceptName);
        T negatedConcept = createWithArea(negationName, cdb.getArea(concept));
        @SuppressWarnings("unused")
        double area = cdb.getArea(negatedConcept);
        // add an impact between the original and negation
        makeImpact(conceptName, negationName, -1);
        makeImpact(negationName, conceptName, -1);
        // impacts with other
        for (Entry<T, Double> entry : cdb
                .getImpacts(cdb.getConcept(conceptName)).entrySet()) {
            String otherConceptNegation =
                    AbstractConceptDB.getNegationName(entry.getKey().getName());
            makeImpact(negationName, otherConceptNegation, entry.getValue());
        }
        // overlaps with other
        for (Entry<T, Double> entry : cdb
                .getOverlaps(cdb.getConcept(conceptName)).entrySet()) {
            String otherConceptNegation =
                    AbstractConceptDB.getNegationName(entry.getKey().getName());
            makeOverlap(negationName, otherConceptNegation, entry.getValue());
        }
        return negatedConcept;
    }

    /**
     * Makes an overlap which is a percentage of the area of the smaller area
     * 
     * @param concept
     * @param cdb
     * @param concepts
     */
    public void makeOverlap(String conceptName1, String conceptName2,
            double fraction) {
        T concept1 = cdb.getConcept(conceptName1);
        T concept2 = cdb.getConcept(conceptName2);
        double area1 = cdb.getArea(concept1);
        double area2 = cdb.getArea(concept2);
        cdb.setOverlap(concept1, concept2, Math.min(area1, area2) * fraction);
    }

}

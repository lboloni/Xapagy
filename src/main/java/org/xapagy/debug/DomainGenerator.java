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
package org.xapagy.debug;

import org.xapagy.autobiography.ABStory;
import org.xapagy.concepts.ConceptNamingConventions;
import org.xapagy.ui.formatters.Formatter;

/**
 * Utility function for generating domains. It is used to generate automated
 * tests of the domain.
 * 
 * For the creation of concepts it is implementing the design patterns as
 * described by 2013/Report-DomainEngineering
 * 
 * <ul>
 * <li>base attribute, independent (BAI)
 * <li>base attribute, with overlap (BAO)
 * <li>category (CAT)
 * <li>category member, independent (CMI)
 * <li>category member, overlapping (CMO)
 * <li>proper name (PN)
 * <li>vague attribute (VA)
 * <li>negation (NEG)
 * </ul>
 * 
 * Similar discussion is needed for the verbs.
 * 
 * In general, for every verb and concept it creates, it generates direct
 * mapping words.
 * 
 * As a note: this generator also generates the negations. This creates problems
 * with the reader... which will do double double negations etc. So this code is
 * not supposed to be exported and read back again.
 * 
 * @author Ladislau Boloni
 * Created on: Feb 18, 2012
 */
public class DomainGenerator {

    /**
     * when generating words, use this prefix to distinguish it from the
     * original concept...
     */
    public static final String PREFIX_WORD = "w_";


    public DomainGenerator() {
    }

    /**
     * Creates n BAI concepts, with the specific prefix and corresponding words
     * 
     * @param n
     * @param prefix
     */
    public void createBAIs(int n, String prefix, ABStory story) {
        createIndependentConcepts(n, prefix, 1.0, story);
    }

    /**
     * Creates 2 BAO concepts prefix1 and prefix2 with the specified degree of
     * overlap
     * 
     * @param n
     * @param prefix
     */
    public void createBAOPair(String prefix, double overlap, ABStory story) {
        String name1 =
                ConceptNamingConventions.PREFIX_SIMPLE_CONCEPT + prefix + "1";
        String command = "$Create Concept " + name1 + " With Area = 1.0";
        story.add(command);
        String name2 =
                ConceptNamingConventions.PREFIX_SIMPLE_CONCEPT + prefix + "2";
        command = "$Create Concept " + name2 + " With Area = 1.0";
        story.add(command);
        command = "$Create ConceptOverlap " + name1 + " Overlaps " + name2
                + " With " + Formatter.fmt(overlap);
        story.add(command);
    }

    /**
     * Creates a category
     * 
     * @param nameRoot
     */
    public void createCAT(String nameRoot, double area, ABStory story) {
        String name =
                ConceptNamingConventions.PREFIX_CATEGORY_CONCEPT + nameRoot;
        String command = "$Create Concept " + name + " With Area = "
                + Formatter.fmt(area);
        story.add(command);
    }

    /**
     * Creates a set of n relation verbs, impacting a common category
     * 
     * @param n
     * @param prefix
     */
    public void createCategoryRelationVerbs(String categoryRoot,
            double categorySize, int n, String prefix, double overlap,
            ABStory story) {
        String categoryName = ConceptNamingConventions.PREFIX_META_CATEGORY_VERB
                + categoryRoot;
        for (int i = 0; i != n; i++) {
            String name =
                    ConceptNamingConventions.PREFIX_RELATION_VERB + prefix + i;
            String command = "$Create Relation " + name;
            story.add(command);
            command = "$Create VerbImpact " + name + " Impacts " + categoryName
                    + " With " + Formatter.fmt(categorySize);
            story.add(command);
            if (i > 0 && overlap > 0.0) {
                String previousName =
                        ConceptNamingConventions.PREFIX_RELATION_VERB + prefix
                                + (i - 1);
                command = "$Create VerbOverlap " + name + " Overlaps "
                        + previousName + " With " + Formatter.fmt(overlap);
                story.add(command);
            }
        }
    }

    /**
     * Creates a category verb
     * 
     * @param nameRoot
     * @param size
     */
    public void createCategoryVerb(String nameRoot, double size, ABStory story) {
        String name =
                ConceptNamingConventions.PREFIX_META_CATEGORY_VERB + nameRoot;
        String command = "$Create Verb " + name;
        story.add(command);
    }

    /**
     * Creates a set of overlapping category members of the specified category,
     * where the successive numbers have a specific overlap
     * 
     * @param categoryName
     * @param categorySize
     * @param prefix
     * @param n
     * @param overlap
     *            - the degree of overlap between successive ones
     */
    public void createCMO(String categoryRoot, double categorySize,
            String prefix, int n, double overlap, ABStory story) {
        String categoryName =
                ConceptNamingConventions.PREFIX_CATEGORY_CONCEPT + categoryRoot;
        for (int i = 0; i != n; i++) {
            String name =
                    ConceptNamingConventions.PREFIX_SIMPLE_CONCEPT + prefix + i;
            String command = "$Create Concept " + name;
            story.add(command);
            command = "$Create ConceptImpact " + name + " Impacts "
                    + categoryName + " With " + Formatter.fmt(categorySize);
            story.add(command);
            if (i > 0 && overlap > 0.0) {
                String previousName =
                        ConceptNamingConventions.PREFIX_SIMPLE_CONCEPT + prefix
                                + (i - 1);
                command = "$Create ConceptOverlap " + name + " Overlaps "
                        + previousName + " With " + Formatter.fmt(overlap);
                story.add(command);
            }
        }
    }

    /**
     * Creates n independent action verbs
     * 
     * @param n
     * @param prefix
     */
    public void createIndependentActionVerbs(int n, String prefix,
            ABStory story) {
        for (int i = 0; i != n; i++) {
            String name = "v_" + prefix + i;
            String command = "$Create Verb " + name;
            story.add(command);
        }
    }

    /**
     * Creates n independent concepts, with the specific prefix and
     * corresponding words
     * 
     * @param n
     * @param prefix
     * @param area
     */
    public void createIndependentConcepts(int n, String prefix, double area,
            ABStory story) {
        for (int i = 0; i != n; i++) {
            String name =
                    ConceptNamingConventions.PREFIX_SIMPLE_CONCEPT + prefix + i;
            String command = "$Create Concept " + name + " With Area = "
                    + Formatter.fmt(area);
            story.add(command);
        }
    }

    /**
     * Creates n independent relation verbs, it also creates words for the verb
     * itself, for the combination with a CreateRelation and combination with
     * RemoveRelation
     * 
     * @param n
     * @param prefix
     */
    public void createIndependentRelationVerbs(int n, String prefix,
            ABStory story) {
        for (int i = 0; i != n; i++) {
            String name =
                    ConceptNamingConventions.PREFIX_RELATION_VERB + prefix + i;
            String command = "$Create Relation " + name;
            story.add(command);
        }
    }

    /**
     * Creates count proper names - change
     * 
     * @param string
     * @param i
     * @param story - to which we add the changes
     */
    public void createPropernames(String root, int count, ABStory story) {
        for (int i = 0; i != count; i++) {
            String name = "\"" + root + i + "\"";
            // LearnProperNames.learnTheWord(agent, name);
            String command = "A " + name + " / exists.";
            story.add(command);
        }
    }

}

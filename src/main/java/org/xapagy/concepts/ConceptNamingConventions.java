/*
   This file is part of the Xapagy project
   Created on: Oct 4, 2012
 
   org.xapagy.concepts.ConceptNamingConventions
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.concepts;

import java.util.AbstractMap.SimpleEntry;

import org.xapagy.ui.TextUi;

/**
 * A common place for the concept and verb naming conventions
 * 
 * This had been originally motivated by the visualization (PwAllConcepts,
 * PwAllVerbs) but other applications are likely
 * 
 * @author Ladislau Boloni
 * 
 */
public class ConceptNamingConventions {

    /**
     * Concept types - codified by their prefixes
     * 
     * @author Ladislau Boloni
     * 
     */
    public enum ConceptType {
        Category, ProperNoun, Simple
    };

    /**
     * Verb types - codified by their prefixes
     * 
     * @author Ladislau Boloni
     * 
     */
    public enum VerbType {
        Adverb, Category, Meta, MetaCategory, Relation, Simple
    };

    /**
     * Adverbs
     */
    public static final String PREFIX_ADVERB_VERB = "va_";
    /**
     * The prefix of category concepts
     */
    public static final String PREFIX_CATEGORY_CONCEPT = "cc_";
    /**
     * Category verbs
     */
    private static final String PREFIX_CATEGORY_VERB = "vc_";
    /**
     * Meta verb - verbs with side effects
     */
    public static final String PREFIX_META_CATEGORY_VERB = "vmc_";
    /**
     * Meta verb - verbs with side effects
     */
    public static final String PREFIX_META_VERB = "vm_";
    /**
     * Relation verbs
     */
    public static final String PREFIX_RELATION_VERB = "vr_";
    /**
     * The prefix of simple concepts
     */
    public static final String PREFIX_SIMPLE_CONCEPT = "c_";
    /**
     * Simple verbs - mostly action verbs
     */
    public static final String PREFIX_SIMPLE_VERB = "v_";
    /**
     * Prefix for words for direct access of concepts from Xapi
     */
    public static final String PREFIX_WORD = "w_";
    /**
     * Prefix for words for direct access of an action VO from Xapi
     */
    public static final String PREFIX_WORD_ACTION_VO = "wa_";
    /**
     * Prefix for words for direct access for a create relation VO from Xapi
     */
    public static final String PREFIX_WORD_CREATE_RELATION_VO = "wcr_";
    /**
     * Prefix for words for direct access for a remove relation VO from Xapi
     */
    public static final String PREFIX_WORD_REMOVE_RELATION_VO = "wrr_";
    /**
     * Prefix for words for direct access of verbs from Xapi
     */
    public static final String PREFIX_WORD_VERB = "wv_";

    /**
     * Returns a lead letter for a concept name - this is the letter under which
     * the concept will be sorted in the visual dictionary of concepts
     * 
     * FIXME: this is hardwired to some conventions in the concepts - it needs
     * to be formalized somewhere
     * 
     */
    public static char getConceptLetter(Concept c) {
        String name = c.getName();
        if (name.startsWith(ConceptNamingConventions.PREFIX_SIMPLE_CONCEPT)) {
            return Character.toUpperCase(name
                    .charAt(ConceptNamingConventions.PREFIX_SIMPLE_CONCEPT
                            .length()));
        }
        if (name.startsWith(ConceptNamingConventions.PREFIX_CATEGORY_CONCEPT)) {
            return Character.toUpperCase(name
                    .charAt(ConceptNamingConventions.PREFIX_CATEGORY_CONCEPT
                            .length()));
        }
        if (name.startsWith("\"")) {
            return name.charAt(0);
        }
        TextUi.abort("What kind of concept is this: " + c.toString());
        return 'x';
    }

    /**
     * Returns the pair of the concept type and the root name of it
     * 
     * @param c
     * @return
     */
    public static SimpleEntry<ConceptType, String> getTypeAndRoot(Concept c) {
        String name = c.getName();
        if (name.startsWith(ConceptNamingConventions.PREFIX_SIMPLE_CONCEPT)) {
            String root =
                    name.substring(ConceptNamingConventions.PREFIX_SIMPLE_CONCEPT
                            .length());
            return new SimpleEntry<>(ConceptType.Simple, root);
        }
        if (name.startsWith(ConceptNamingConventions.PREFIX_CATEGORY_CONCEPT)) {
            String root =
                    name.substring(ConceptNamingConventions.PREFIX_CATEGORY_CONCEPT
                            .length());
            return new SimpleEntry<>(ConceptType.Category, root);
        }
        if (name.startsWith("\"")) {
            String root = name;
            return new SimpleEntry<>(ConceptType.ProperNoun, root);
        }
        TextUi.abort("What kind of concept is this: " + c.toString());
        return null;
    }

    /**
     * Returns the pair of the verb type and the root name of it
     * 
     * @param v
     * @return
     */
    public static SimpleEntry<VerbType, String> getTypeAndRoot(Verb v) {
        String name = v.getName();
        // simple verb
        if (name.startsWith(ConceptNamingConventions.PREFIX_SIMPLE_VERB)) {
            String root =
                    name.substring(ConceptNamingConventions.PREFIX_SIMPLE_VERB
                            .length());
            return new SimpleEntry<>(VerbType.Simple, root);
        }
        // category verb
        if (name.startsWith(ConceptNamingConventions.PREFIX_CATEGORY_VERB)) {
            String root =
                    name.substring(ConceptNamingConventions.PREFIX_CATEGORY_VERB
                            .length());
            return new SimpleEntry<>(VerbType.Category, root);
        }
        // adverb
        if (name.startsWith(ConceptNamingConventions.PREFIX_ADVERB_VERB)) {
            String root =
                    name.substring(ConceptNamingConventions.PREFIX_ADVERB_VERB
                            .length());
            return new SimpleEntry<>(VerbType.Adverb, root);
        }
        // meta-verb
        if (name.startsWith(ConceptNamingConventions.PREFIX_META_VERB)) {
            String root =
                    name.substring(ConceptNamingConventions.PREFIX_META_VERB
                            .length());
            return new SimpleEntry<>(VerbType.Meta, root);
        }
        // meta-verb category
        if (name.startsWith(ConceptNamingConventions.PREFIX_META_CATEGORY_VERB)) {
            String root =
                    name.substring(ConceptNamingConventions.PREFIX_META_CATEGORY_VERB
                            .length());
            return new SimpleEntry<>(VerbType.MetaCategory, root);
        }
        // relation
        if (name.startsWith(ConceptNamingConventions.PREFIX_RELATION_VERB)) {
            String root =
                    name.substring(ConceptNamingConventions.PREFIX_RELATION_VERB
                            .length());
            return new SimpleEntry<>(VerbType.Relation, root);
        }
        TextUi.abort("What kind of verb is this: " + v.toString());
        return null;
    }

    /**
     * Returns a lead letter for the verb name - this is the letter under which
     * the concept will be sorted in the visual dictionary of verbs
     * 
     * FIXME: this is hardwired to some conventions in the verbs - it needs to
     * be formalized somewhere
     * 
     */
    public static char getVerbLetter(Verb v) {
        String name = v.getName();
        // simple verb
        if (name.startsWith(ConceptNamingConventions.PREFIX_SIMPLE_VERB)) {
            return Character.toUpperCase(name
                    .charAt(ConceptNamingConventions.PREFIX_SIMPLE_VERB
                            .length()));
        }
        // category verb
        if (name.startsWith(ConceptNamingConventions.PREFIX_CATEGORY_VERB)) {
            return Character.toUpperCase(name
                    .charAt(ConceptNamingConventions.PREFIX_CATEGORY_VERB
                            .length()));
        }
        // adverb
        if (name.startsWith(ConceptNamingConventions.PREFIX_ADVERB_VERB)) {
            return Character.toUpperCase(name
                    .charAt(ConceptNamingConventions.PREFIX_ADVERB_VERB
                            .length()));
        }
        // meta-verb
        if (name.startsWith(ConceptNamingConventions.PREFIX_META_VERB)) {
            return Character
                    .toUpperCase(name
                            .charAt(ConceptNamingConventions.PREFIX_META_VERB
                                    .length()));
        }
        // meta-verb category
        if (name.startsWith(ConceptNamingConventions.PREFIX_META_CATEGORY_VERB)) {
            return Character.toUpperCase(name
                    .charAt(ConceptNamingConventions.PREFIX_META_CATEGORY_VERB
                            .length()));
        }
        // relation
        if (name.startsWith(ConceptNamingConventions.PREFIX_RELATION_VERB)) {
            return Character.toUpperCase(name
                    .charAt(ConceptNamingConventions.PREFIX_RELATION_VERB
                            .length()));
        }
        TextUi.abort("What kind of verb is this: " + v.toString());
        return 'x';
    }

}

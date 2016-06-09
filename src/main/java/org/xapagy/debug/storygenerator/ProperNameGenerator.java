/*
   This file is part of the Xapagy project
   Created on: Jun 18, 2011
 
   org.xapagy.debug.SG2
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.debug.storygenerator;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * @author Ladislau Boloni
 * 
 */
public class ProperNameGenerator implements Serializable {

    private static String[] consonants = { "b", "c", "d", "f", "g", "h", "j",
            "k", "l", "m", "n", "p", "q", "r", "s", "t", "v", "x", "z" };
    private static final long serialVersionUID = -38648205489228986L;
    private static String[] wovels = { "a", "e", "i", "o", "u" };

    private Set<String> properNames = new HashSet<>();
    private Random random;

    /**
     * Use a local random name generator
     */
    public ProperNameGenerator() {
        random = new Random();
    }

    /**
     * Use an external random name generator
     */
    public ProperNameGenerator(Random random) {
        this.random = random;
    }

    /**
     * Generates a guaranteed unique concept name
     * 
     * @return
     */
    public String generateProperName() {
        while (true) {
            String candidate =
                    "\"" + randomConsonant().toUpperCase() + randomWovel()
                            + randomConsonant() + randomWovel()
                            + randomConsonant() + "\"";
            if (!properNames.contains(candidate)) {
                properNames.add(candidate);
                return candidate;
            }
        }
    }

    /**
     * Generates an array of count proper names
     * 
     * @param count
     * @return
     */
    public String[] generateProperNames(int count) {
        String[] retval = new String[count];
        for (int i = 0; i != count; i++) {
            retval[i] = generateProperName();
        }
        return retval;
    }

    /**
     * Returns a random consonant
     * 
     * @return
     */
    private String randomConsonant() {
        int index = random.nextInt(ProperNameGenerator.consonants.length);
        return ProperNameGenerator.consonants[index];
    }

    /**
     * Returns a random consonant
     * 
     * @return
     */
    private String randomWovel() {
        int index = random.nextInt(ProperNameGenerator.wovels.length);
        return ProperNameGenerator.wovels[index];
    }
}

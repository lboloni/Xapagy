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
package org.xapagy.debug.storygenerator;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * @author Ladislau Boloni
 * Created on: Jun 18, 2011
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

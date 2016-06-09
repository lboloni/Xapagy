/*
 * Glob.java
 *
 * Brazil project web application toolkit,
 * export version: 2.1 
 * Copyright (c) 1999-2004 Sun Microsystems, Inc.
 *
 * Sun Public License Notice
 *
 * The contents of this file are subject to the Sun Public License Version 
 * 1.0 (the "License"). You may not use this file except in compliance with 
 * the License. A copy of the License is included as the file "license.terms",
 * and also available at http://www.sun.com/
 * 
 * The Original Code is from:
 *    Brazil project web application toolkit release 2.1.
 * The Initial Developer of the Original Code is: suhler.
 * Portions created by suhler are Copyright (C) Sun Microsystems, Inc.
 * All Rights Reserved.
 * 
 * Contributor(s): cstevens, suhler.
 *
 * Version:  2.16
 * Created by suhler on 99/08/05
 * Last modified by suhler on 04/11/30 14:59:15
 */
package org.xapagy.util;

/**
 * Glob-style string matching and substring extraction. Glob was implemented by
 * translating the glob package for <a
 * href="http://www.scriptics.com">tcl8.0</a>.
 * <ul>
 * <li>"*" matches 0 or more characters
 * <li>"?" matches a single character
 * <li>"[...]" matches a set and/or range of characters
 * <li>"\" following character is not special
 * </ul>
 * 
 * Each of the substrings matching (?, *, or [..]) are returned.
 * 
 * @author Colin Stevens (colin.stevens@sun.com)
 * @version 2.16
 */
public class Glob {
    private static void addMatch(final String str, final int start,
            final int end, final String[] substrs, final int subIndex) {
        if (substrs == null || subIndex >= substrs.length) {
            return;
        }
        substrs[subIndex] = str.substring(start, end);
    }

    private static boolean match(final String pat, int pIndexInit,
            final String str, int sIndexPassed, final String[] substrs, int subIndexPassed) {
        int sIndex = sIndexPassed;
        int subIndex = subIndexPassed;
        int pIndex = pIndexInit;
        final int pLen = pat.length();
        final int sLen = str.length();
        while (true) {
            if (pIndex == pLen) {
                if (sIndex == sLen) {
                    return true;
                } else {
                    return false;
                }
            } else if (sIndex == sLen && pat.charAt(pIndex) != '*') {
                return false;
            }
            switch (pat.charAt(pIndex)) {
            case '*': {
                final int start = sIndex;
                pIndex++;
                if (pIndex >= pLen) {
                    Glob.addMatch(str, start, sLen, substrs, subIndex);
                    return true;
                }
                while (true) {
                    if (Glob.match(pat, pIndex, str, sIndex, substrs,
                            subIndex + 1)) {
                        Glob.addMatch(str, start, sIndex, substrs, subIndex);
                        return true;
                    }
                    if (sIndex == sLen) {
                        return false;
                    }
                    sIndex++;
                }
            }
            case '?': {
                pIndex++;
                Glob.addMatch(str, sIndex, sIndex + 1, substrs, subIndex++);
                sIndex++;
                break;
            }
            case '[': {
                try {
                    pIndex++;
                    final char s = str.charAt(sIndex);
                    char p = pat.charAt(pIndex);
                    while (true) {
                        if (p == ']') {
                            return false;
                        }
                        if (p == s) {
                            break;
                        }
                        pIndex++;
                        char next = pat.charAt(pIndex);
                        if (next == '-') {
                            pIndex++;
                            final char p2 = pat.charAt(pIndex);
                            if (p <= s && s <= p2) {
                                break;
                            }
                            pIndex++;
                            next = pat.charAt(pIndex);
                        }
                        p = next;
                    }
                    pIndex = pat.indexOf(']', pIndex) + 1;
                    if (pIndex <= 0) {
                        return false;
                    }
                    Glob.addMatch(str, sIndex, sIndex + 1, substrs, subIndex++);
                    sIndex++;
                } catch (final StringIndexOutOfBoundsException e) {
                    /*
                     * Easier just to catch malformed [] sequences than to check
                     * bounds all the time.
                     */
                    return false;
                }
                break;
            }
            case '\\': {
                pIndex++;
                if (pIndex >= pLen) {
                    return false;
                }
                // fall through
            }
            //$FALL-THROUGH$
            default: {
                if (pat.charAt(pIndex) != str.charAt(sIndex)) {
                    return false;
                }
                pIndex++;
                sIndex++;
            }
            }
        }
    }

    /**
     * Match a string against a pattern.
     * 
     * @param pattern
     *            Glob pattern. Nothing matches if pattern==null.
     * 
     * @param string
     *            String to match against pattern.
     * 
     * @return <code>true</code> if the string matched the pattern,
     *         <code>false</code> otherwise.
     */
    public static boolean match(final String pattern, final String string) {
        return Glob.match(pattern, string, null);
    }

    /**
     * Match a string against a pattern, and return sub-matches.
     * <p>
     * The caller can provide an array of strings that will be filled in with
     * the substrings of <code>string</code> that matched the glob
     * meta-characters in <code>pattern</code>. The array of strings may be
     * partially modified even if the string did not match the glob pattern. The
     * array may contain more elements than glob meta-characters, in which case
     * those extra elements will not be modified; the array may also contain
     * fewer elements or even be <code>null</code>, to ignore some or all of the
     * glob meta-characters. In other words, the user can pass pretty much
     * anything and this method defines errors out of existence.
     * 
     * @param pattern
     *            Glob pattern.
     * 
     * @param string
     *            String to match against pattern.
     * 
     * @param substr
     *            Array of strings provided by the caller, to be filled in with
     *            the substrings that matched the glob meta-characters. May be
     *            <code>null</code>.
     * 
     * @return <code>true</code> if the string matched the pattern,
     *         <code>false</code> otherwise.
     */
    public static boolean match(final String pattern, final String string,
            final String[] substr) {
        return pattern != null && Glob.match(pattern, 0, string, 0, substr, 0);
    }

    private Glob() {
    }
}

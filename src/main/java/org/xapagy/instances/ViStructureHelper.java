/*
   This file is part of the Xapagy project
   Created on: Dec 26, 2011
 
   org.xapagy.instances.ViStructureHelper
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.instances;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ladislau Boloni
 * 
 */
public class ViStructureHelper {

    public enum ViPart {
        Adjective, Object, Quote, QuoteScene, Subject, Verb
    }

    public enum ViType {
        QUOTE, S_ADJ, S_V, S_V_O
    }

    /**
     * The list of allowed parts of type instance for each type
     */
    public static Map<ViType, List<ViPart>> allowedInstanceParts;
    /**
     * The list of allowed parts for each type (including parts which are not
     * instances)
     */
    public static Map<ViType, List<ViPart>> allowedParts;

    /**
     * Checks whether a certain part of instance is allowed in this type. This
     * will throw the code, as this is always a programming error
     * 
     * @param vip
     * @return
     */
    public static boolean allowed(ViPart vip, ViType viType) {
        boolean allowed =
                ViStructureHelper.getAllowedParts(viType).contains(vip);
        if (!allowed) {
            throw new Error("Part type :" + vip + " is not allowed in "
                    + viType);
        }
        return true;
    }

    /**
     * Returns the allowed instance parts (and create the static map if not
     * ready yet)
     * 
     * @return
     */
    public static List<ViPart> getAllowedInstanceParts(ViType type) {
        if (ViStructureHelper.allowedInstanceParts != null) {
            return ViStructureHelper.allowedInstanceParts.get(type);
        }
        List<ViPart> list;
        Map<ViType, List<ViPart>> ap = new HashMap<>();
        // S_V
        list = new ArrayList<>();
        list.add(ViPart.Subject);
        ap.put(ViType.S_V, Collections.unmodifiableList(list));
        // S_V_O
        list = new ArrayList<>();
        list.add(ViPart.Subject);
        list.add(ViPart.Object);
        ap.put(ViType.S_V_O, Collections.unmodifiableList(list));
        // S_ADJECTIVE
        list = new ArrayList<>();
        list.add(ViPart.Subject);
        ap.put(ViType.S_ADJ, Collections.unmodifiableList(list));
        // QUOTE
        list = new ArrayList<>();
        list.add(ViPart.QuoteScene);
        list.add(ViPart.Subject);
        ap.put(ViType.QUOTE, Collections.unmodifiableList(list));
        // allowedParts
        ViStructureHelper.allowedInstanceParts =
                Collections.unmodifiableMap(ap);
        return ViStructureHelper.allowedInstanceParts.get(type);
    }

    /**
     * Returns the allowed parts (and create the static map if not ready yet)
     * 
     * @return
     */
    public static List<ViPart> getAllowedParts(ViType type) {
        if (ViStructureHelper.allowedParts != null) {
            return ViStructureHelper.allowedParts.get(type);
        }
        List<ViPart> list;
        Map<ViType, List<ViPart>> ap = new HashMap<>();
        // S_V
        list = new ArrayList<>();
        list.add(ViPart.Subject);
        list.add(ViPart.Verb);
        ap.put(ViType.S_V, Collections.unmodifiableList(list));
        // S_V_O
        list = new ArrayList<>();
        list.add(ViPart.Subject);
        list.add(ViPart.Verb);
        list.add(ViPart.Object);
        ap.put(ViType.S_V_O, Collections.unmodifiableList(list));
        // S_ADJECTIVE
        list = new ArrayList<>();
        list.add(ViPart.Subject);
        list.add(ViPart.Verb);
        list.add(ViPart.Adjective);
        ap.put(ViType.S_ADJ, Collections.unmodifiableList(list));
        // QUOTE
        list = new ArrayList<>();
        list.add(ViPart.Subject);
        list.add(ViPart.Verb);
        list.add(ViPart.QuoteScene);
        list.add(ViPart.Quote);
        ap.put(ViType.QUOTE, Collections.unmodifiableList(list));
        // allowedParts
        ViStructureHelper.allowedParts = Collections.unmodifiableMap(ap);
        return ViStructureHelper.allowedParts.get(type);
    }

}

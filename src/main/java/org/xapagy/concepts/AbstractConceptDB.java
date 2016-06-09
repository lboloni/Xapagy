package org.xapagy.concepts;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.xapagy.exceptions.MalformedConceptOrVerbName;
import org.xapagy.exceptions.NoSuchConceptOrVerb;
import org.xapagy.ui.TextUi;

/**
 * 
 * Database of concepts, includes the representation of the areas and the
 * overlaps between the concepts
 * 
 * To be used both for Concept and Verb
 * 
 * @author Lotzi Boloni
 * 
 */
public class AbstractConceptDB<T extends AbstractConcept> implements
        Serializable {

    public static final double MINIMAL_AREA = 0.001;
    private static final long serialVersionUID = -8987645116455111624L;

    /**
     * Returns the name of the negation of the specific concept
     * 
     * @param concept
     * @return
     */
    public static String getNegationName(String conceptName) {
        String negationName;
        if (conceptName.startsWith(Hardwired.CONCEPT_PREFIX_NEGATION)) {
            negationName =
                    conceptName.substring(Hardwired.CONCEPT_PREFIX_NEGATION
                            .length());
        } else {
            negationName = Hardwired.CONCEPT_PREFIX_NEGATION + conceptName;
        }
        return negationName;
    }

    /**
     * Returns true if the concept is a negation
     * 
     * @param concept
     * @return
     */
    public static boolean isANegation(AbstractConcept concept) {
        return concept.getName().startsWith(Hardwired.CONCEPT_PREFIX_NEGATION);
    }

    /**
     * 
     * Verifies the name of the concept or verb, if it conforms to the standard
     * c_ or v_ names
     * 
     * @return true if it is a concept, false it is a verb, and throws exception
     *         if it is a name that matches neither
     */
    private static boolean verifyName(String originalName)
            throws MalformedConceptOrVerbName {
        String name = originalName;
        if (name.startsWith(Hardwired.CONCEPT_PREFIX_NEGATION)) {
            name = name.substring(Hardwired.CONCEPT_PREFIX_NEGATION.length());
        }
        // verify for proper names - they are concepts
        if (name.startsWith("\"")) {
            return true;
        }
        int locUnd = name.indexOf("_");
        if (!originalName.equals(originalName.toLowerCase())) {
            throw new MalformedConceptOrVerbName(originalName,
                    "A concept or verb should not contain uppercase letters:");
        }
        if (locUnd < 0 || locUnd > 3) {
            throw new MalformedConceptOrVerbName(originalName,
                    "Misnamed concept, must start with cxx_ or vxx_ !!!");
        }
        if (name.startsWith("c")) {
            return true;
        }
        if (name.startsWith("v")) {
            return false;
        }
        throw new MalformedConceptOrVerbName(originalName,
                "Misnamed concept, must start with cxx_ or vxx_ !!!");
    }

    private Map<T, Double> area = new HashMap<>();

    private Map<String, T> concepts = new HashMap<>();

    private Map<T, Map<T, Double>> impactRatio = new HashMap<>();

    private Map<T, Map<T, Double>> overlapArea = new HashMap<>();

    public AbstractConceptDB() {
        // initializes an empty concept database
    }

    /**
     * Adds a new concept
     * 
     * @param concept
     */
    public void addConcept(T concept) {
        String name = concept.getName();
        concepts.put(name, concept);
    }

    /**
     * Returns all the concepts
     * 
     * @return
     */
    public Collection<T> getAllConcepts() {
        return concepts.values();
    }

    /**
     * Returns a set of all the concepts which overlap with the specified
     * overlay
     * 
     * @param concept
     * @return
     */
    public Set<T> getAllOverlappingConcepts(Overlay<T> ovr) {
        Set<T> retval = new HashSet<>();
        for (T t : ovr.getAsSet()) {
            Map<T, Double> overlaps = overlapArea.get(t);
            if (overlaps != null) {
                retval.addAll(overlaps.keySet());
            }
            retval.add(t);
        }
        return retval;
    }

    /**
     * Gets the area (or minimal if not explicitly set)
     * 
     * @param concept
     * @return
     */
    public double getArea(T concept) {
        Double areaSize = area.get(concept);
        if (areaSize == null) {
            return AbstractConceptDB.MINIMAL_AREA;
        }
        return areaSize;
    }

    /**
     * Returns a specific concept
     * 
     * @param name
     * @return
     */
    public T getConcept(String name) throws MalformedConceptOrVerbName,
            NoSuchConceptOrVerb {
        boolean isConcept = AbstractConceptDB.verifyName(name);
        T retval = concepts.get(name);
        if (retval == null) {
            if (isConcept) {
                throw new NoSuchConceptOrVerb(name);
            } else {
                throw new NoSuchConceptOrVerb(name);
            }
        }
        return retval;
    }

    /**
     * Sets the implication area from concept a to concept b As opposed to the
     * overlap, this is assymetric
     * 
     * @param a
     * @param b
     * @param implicationValue
     */
    public double getImpact(T a, T b) {
        Map<T, Double> implication = impactRatio.get(a);
        if (implication == null) {
            return 0;
        }
        Double d = implication.get(b);
        if (d == null) {
            return 0.0;
        }
        return d;
    }

    /**
     * Returns all the impacts from a
     * 
     * @return
     */
    public Map<T, Double> getImpacts(T a) {
        Map<T, Double> ovr = impactRatio.get(a);
        if (ovr == null) {
            ovr = new HashMap<>();
        }
        return Collections.unmodifiableMap(ovr);
    }

    /**
     * Returns area of overlap between the two concepts
     * 
     * @param first
     * @param second
     * @return
     */
    public double getOverlap(T first, T second) {
        T a = null, b = null;
        int compare = first.getName().compareTo(second.getName());
        if (compare < 0) {
            a = first;
            b = second;
        }
        if (compare == 0) {
            // TextUi.errorPrint("Trying to get overlap area for the same concept "
            // + first + "\n Returning own area.");
            return getArea(first);
        }
        if (compare > 0) {
            a = second;
            b = first;
        }
        Map<T, Double> overlap = overlapArea.get(a);
        if (overlap == null) {
            return 0;
        }
        Double d = overlap.get(b);
        if (d == null) {
            return 0;
        } else {
            return d;
        }
    }

    /**
     * Returns all the overlaps from the current concept to the other ones.
     * 
     * @return
     */
    public Map<T, Double> getOverlaps(T a) {
        Map<T, Double> ovr = overlapArea.get(a);
        if (ovr == null) {
            ovr = new HashMap<>();
        }
        return Collections.unmodifiableMap(ovr);
    }

    /**
     * Sets the area of a concept
     * 
     * @param concept
     * @param areaSize
     */
    public void setArea(T concept, double areaSize) {
        area.put(concept, areaSize);
    }

    /**
     * Sets the impact from a to b
     * 
     * In contrast to the overlap, this is asymmetric.
     * 
     * @param a
     * @param b
     * @param implicationValue
     */
    public void setImpact(T a, T b, double implicationValue) {
        Map<T, Double> implication = impactRatio.get(a);
        if (implication == null) {
            implication = new HashMap<>();
            impactRatio.put(a, implication);
        }
        implication.put(b, implicationValue);
    }

    /**
     * Sets the ratio of first which overlaps with the second
     * 
     * @param first
     * @param second
     * @param overlapValue
     */
    public void setOverlap(T first, T second, double overlapValue) {
        int compare = first.getName().compareTo(second.getName());
        if (compare == 0) {
            TextUi.errorPrint("Trying to set overlap area for the same concept "
                    + first + "\n Ignoring.");
            return;
        }
        // from first to second
        Map<T, Double> overlap = overlapArea.get(first);
        if (overlap == null) {
            overlap = new HashMap<>();
            overlapArea.put(first, overlap);
        }
        overlap.put(second, overlapValue);
        // from second to first
        Map<T, Double> overlap2 = overlapArea.get(second);
        if (overlap2 == null) {
            overlap2 = new HashMap<>();
            overlapArea.put(second, overlap2);
        }
        overlap2.put(first, overlapValue);
    }

}

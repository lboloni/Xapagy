package org.xapagy.set;

import java.io.Serializable;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.xapagy.ui.TextUi;
import org.xapagy.ui.prettyprint.PrettyPrint;
import org.xapagy.util.SimpleEntryComparator;

/**
 * Participation of a certain kind of object in a weighted subset
 * 
 * @author Ladislau Boloni
 * 
 * @param <T>
 *            the type
 * @param <V>
 */
public class WeightedSet<T> implements Serializable {

    /**
     * The minimum participation, once it gets lower than that, it is removed
     */
    public static final double LOW_CUTOFF = 0.0001;
    private static final long serialVersionUID = -4833352433971363470L;
    protected Map<T, Double> map = new HashMap<>();
    protected double sum = 0;
    protected double upperBound;

    /**
     * 
     */
    public WeightedSet() {
        this(1.0);
    }

    /**
     * @param upperBound
     */
    public WeightedSet(double upperBound) {
        super();
        this.upperBound = upperBound;
    }

    /**
     * Changes the value of T with the specified value If a new one is created,
     * it is discards the weakest of necessary.
     * 
     * If necessary, adds a new one, or removes it
     * 
     * @param t
     * @param value
     * @return the current new value
     */
    public double change(T t, double value) {
        if (t == null) {
            throw new Error("Cannot support null values in WeightedSet.change");
        }
        Double d = map.get(t);
        double current;
        if (d == null) {
            current = 0;
        } else {
            current = d;
        }
        if (value == 0.0) {
            return current;
        }
        double currentNew = current + value;
        if (currentNew > upperBound) {
            currentNew = upperBound;
        }
        if (currentNew <= 0.0) { // was <
            sum = sum - current;
            map.remove(t);
        } else {
            sum = sum - (current - currentNew);
            map.put(t, currentNew);
        }
        if (currentNew == Double.NaN) {
            TextUi.abort("Trouble, NaN in weighted set!!!");
        }
        return currentNew;
    }

    /**
     * Changes the value to the new value
     * 
     * @param t
     * @param value
     * @return
     */
    public double changeTo(T t, double value) {
        double oldval = value(t);
        return change(t, value - oldval);
    }

    public void clear() {
        map.clear();
        sum = 0;
    }

    /**
     * Discards the weakest value
     */
    public void discardWeakest() {
        double weakestValue = Double.MAX_VALUE;
        T weakest = null;
        for (T t : map.keySet()) {
            double value = value(t);
            if (value < weakestValue) {
                weakestValue = value;
                weakest = t;
            }
        }
        sum = sum - weakestValue;
        map.remove(weakest);
    }

    /**
     * Returns a list of the entries in the decreasing order
     * 
     * @return
     */
    public List<SimpleEntry<T, Double>> getDecreasingStrengthList() {
        List<SimpleEntry<T, Double>> list = new ArrayList<>();
        for (T t : getParticipants()) {
            list.add(new SimpleEntry<>(t, value(t)));
        }
        Comparator<SimpleEntry<T, Double>> comp = new SimpleEntryComparator<>();
        comp = Collections.reverseOrder(comp);
        Collections.sort(list, comp);
        return list;
    }

    /**
     * Returns a list of the entries with their weights
     * 
     * @return
     */
    public List<SimpleEntry<T, Double>> getList() {
        List<SimpleEntry<T, Double>> list = new ArrayList<>();
        for (T t : getParticipants()) {
            list.add(new SimpleEntry<>(t, value(t)));
        }
        return list;
    }

    /**
     * Returns the participants
     * 
     * @return
     */
    public Set<T> getParticipants() {
        return map.keySet();
    }

    public double getSum() {
        return sum;
    }

    public double getUpperBound() {
        return upperBound;
    }

    /**
     * Is empty
     * 
     * @return
     */
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * Merges in the other participation -the problem with this approach is that
     * in the case of limits, a weak one from the new one can dislodge a
     * stronger one if it comes from the separate one
     * 
     * @param other
     *            the other participation
     * @param ratio
     *            can be 1.0, -1.0, -100.0 (for full exclusion etc)
     * 
     */
    public void mergeIn(WeightedSet<T> other, double ratio) {
        List<SimpleEntry<T, Double>> list = other.getList();
        for (SimpleEntry<T, Double> entry : list) {
            change(entry.getKey(), entry.getValue() * ratio);
        }
    }

    /**
     * Get the current value
     */
    public double relativeValue(T t) {
        if (sum == 0) {
            return 0;
        }
        return value(t) / sum;
    }

    /**
     * Simple printing
     */
    @Override
    public String toString() {
        return PrettyPrint.ppConcise(this, null);
    }

    /**
     * Get the current value
     */
    public double value(T t) {
        Double d = map.get(t);
        if (d == null) {
            return 0;
        } else {
            return d;
        }
    }
}

package org.xapagy.ui;

/**
 * Various progress bars in text mode
 * 
 * @author Ladislau Boloni
 * 
 */
public class TextProgress {

    /**
     * Returns a progress representation of a number of the 0 .. 1 range
     * 
     * @param val
     * @return
     */
    public static String progress(double value) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(TextProgress.progressBar(value, 0.01, 0.1, 0.01));
        buffer.append(TextProgress.progressBar(value, 0.1, 1, 0.1));
        buffer.append(" ");
        buffer.append(String.format("%5.4f", value));
        return buffer.toString();
    }

    /**
     * Returns a progress representation of a number of the 0 .. 1 range
     * 
     * @param val
     * @return
     */
    public static String progressBar(double value, double low, double high,
            double step) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("(");
        for (double val = low; val <= high; val = val + step) {
            if (value > val) {
                buffer.append("#");
            } else {
                buffer.append(".");
            }
        }
        buffer.append(")");
        return buffer.toString();
    }

    /**
     * Returns a representation where smaller numbers are pushed to the side 0.9
     * ..0.7 ........0.1
     * 
     * @param val
     * @return
     */
    public static String shiftSmaller(double value) {
        StringBuffer buffer = new StringBuffer();
        double val = value;
        while (val < 0.8) {
            buffer.append(".");
            val = val + 0.1;
        }
        return buffer.toString();
    }

}

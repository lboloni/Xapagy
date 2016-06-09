/*
   This file is part of the Xapagy project
   Created on: Aug 6, 2013
 
   org.xapagy.ui.tempdyn.tdGraphDescription
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.tempdyn;

import java.util.List;

import org.xapagy.ui.matlab.AbstractGraphDescription;
import org.xapagy.ui.matlab.MatlabUtil;

/**
 * @author Ladislau Boloni
 * 
 */
public class tdGraphDescription extends AbstractGraphDescription {

    private static final long serialVersionUID = 3888528952957037375L;
    private List<Double> index;
    private List<Double> values;

    /**
     * @param title
     * @param xLabel
     * @param yLabel
     * @param zLabel
     */
    public tdGraphDescription(String title, String xLabel, String yLabel,
            String zLabel, List<Double> index, List<Double> values) {
        super(title, xLabel, yLabel, zLabel);
        this.index = index;
        this.values = values;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xapagy.ui.matlab.AbstractGraphDescription#generate(int, int)
     */
    @Override
    public String generate(int figureCount, int xpos, int ypos, int xsize,
            int ysize) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(MatlabUtil.generateBasicGraphPrefix(figureCount, xpos,
                ypos, xsize, ysize));
        int count = 0;
        String xLbl = "x" + getXLabel();
        buffer.append(MatlabUtil.getMatlabVector(xLbl, index));
        String yLbl = "y" + getXLabel();
        buffer.append(MatlabUtil.getMatlabVector(yLbl, values));
        // String lineStyleSpecification = getLineStyle(count);
        String signSpecification =
                ", 'b-"
                        + AbstractGraphDescription.sign[count
                                % AbstractGraphDescription.sign.length] + "'";
        String labelSpecification = ",'DisplayName','" + "noname" + "'";
        String plotLabel = "plot_" + getXLabel();
        buffer.append(plotLabel + " = plot(" + xLbl + ", " + yLbl
                + signSpecification + labelSpecification + ")\n");
        buffer.append(MatlabUtil.generateBasicGraphProperties(this));
        buffer.append("legend" + figureCount + " = legend(axes" + figureCount
                + ",'show');\n");
        buffer.append("hold off\n");
        return buffer.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xapagy.ui.matlab.AbstractGraphDescription#generateOctave(int,
     * int)
     */
    @Override
    public String generateOctave(int xsize, int ysize) {
        // TODO Auto-generated method stub
        return null;
    }

}

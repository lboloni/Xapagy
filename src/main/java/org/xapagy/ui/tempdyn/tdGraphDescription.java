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
package org.xapagy.ui.tempdyn;

import java.util.List;

import org.xapagy.ui.matlab.AbstractGraphDescription;
import org.xapagy.ui.matlab.MatlabUtil;

/**
 * @author Ladislau Boloni
 * Created on: Aug 6, 2013
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

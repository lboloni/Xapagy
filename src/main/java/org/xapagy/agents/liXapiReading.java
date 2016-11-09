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
package org.xapagy.agents;

import java.io.File;

/**
 * A loopitem based on reading a Xapi sentence. 
 * 
 * @author lboloni
 * Created on November 1, 2016
 */

public class liXapiReading extends AbstractXapiLoopItem {

	private static final long serialVersionUID = -726480028511080865L;
	/**
	 * File line number when this comes from reading
	 */
	private int fileLineNo = -1;
	/**
	 * File name, when this comes from reading
	 */
	private String fileName = null;

	/**
	 * Creates a reading type loop item, hand edited (no source file)
	 *
	 * @param xapiText
	 */
	public liXapiReading(Agent agent, String xapiText) {
		super(agent, xapiText);
	}

	/**
	 * Creates a reading type loop item (called from Loop.addFileToReading)
	 *
	 * @param xapiText
	 */
	public liXapiReading(Agent agent, String xapiText, File file, int fileLineNo) {
		super(agent, xapiText);
		this.fileName = file.getName();
		this.fileLineNo = fileLineNo;
	}

	/**
	 * Formats a specific throwable (Error or Exception) by indicating where it
	 * was coming from
	 *
	 * @param t
	 * @param xapiText
	 * @return
	 */
	@Override
	public String formatException(Throwable t, String description) {
		if (fileName != null) {
			return "At " + fileName + ":" + fileLineNo + " = " + xapiText + "\nError was found: "
					+ t.getClass().getCanonicalName() + " " + description;
		} else {
			return "At generated Xapi = " + xapiText + "\nError was found: " + t.getClass().getCanonicalName() + " "
					+ description;
		}
	}

	/**
	 * @return the fileLineNo
	 */
	public int getFileLineNo() {
		return fileLineNo;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	@Override
	protected void internalExecute() {
		Execute.executeXapiText(agent, this);
	}

}

package org.xapagy.agents;

import java.io.File;

public class liXapiReading extends AbstractXapiLoopItem {

	/**
	 *
	 */
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

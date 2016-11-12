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
package org.xapagy.ui.observers;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.xapagy.agents.AbstractLoopItem;
import org.xapagy.agents.Agent;
import org.xapagy.agents.Focus;
import org.xapagy.agents.liHlsChoiceBased;
import org.xapagy.agents.liViBased;
import org.xapagy.agents.liXapiReading;
import org.xapagy.agents.liXapiScheduled;
import org.xapagy.debug.DebugEvent;
import org.xapagy.debug.DebugEvent.DebugEventType;
import org.xapagy.headless_shadows.HeadlessComponents;
import org.xapagy.instances.VerbInstance;
import org.xapagy.set.EnergyColors;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.TextUiHelper;
import org.xapagy.ui.formatters.Formatter;
import org.xapagy.ui.formatters.TwFormatter;
import org.xapagy.ui.prettygeneral.xwLiHlsChoiceBased;
import org.xapagy.ui.prettygeneral.xwLiViBased;
import org.xapagy.ui.prettygeneral.xwLiXapiReading;
import org.xapagy.ui.prettygeneral.xwLiXapiScheduled;
import org.xapagy.ui.prettyprint.PpChoices;
import org.xapagy.ui.prettyprint.PpHlsNewInstance;
import org.xapagy.ui.prettyprint.PpInstanceResolutionChoices;
import org.xapagy.ui.prettyprint.PpShadows;
import org.xapagy.ui.prettyprint.PrettyPrint;
import org.xapagy.ui.prettyprint.PrintDetail;
import org.xapagy.ui.smartprint.SpFocus;
import org.xapagy.util.FileWritingUtil;

/**
 * @author Ladislau Boloni
 * Created on: Aug 30, 2010
 */
public class ToStringObserver extends AbstractAgentObserver {

	private static final long serialVersionUID = -7218334055175882085L;;

	private File outputDir;
	private File outputFile;
	private transient PrintStream printStream; // = System.out;

	private boolean printToFile = false;
	private boolean printToOutput = true;
	private boolean printToTextFiles = false;
	private Map<DebugEventType, Map<String, PrintDetail>> printWhat = new HashMap<>();
	private int stepCount = 1;
	private String stepFileName = "step";
	private Set<TraceWhat> traceWhat = new HashSet<>();
	private boolean waitForKey = false;

	/**
	 * The default constructor, setting only the detail level
	 * 
	 * @param detailLevel
	 */
	public ToStringObserver() {
		this.outputDir = new File("output");
	}

	/**
	 * Species an additional component to print at an event
	 * 
	 * It automatically turns on the listening to that event as well
	 * 
	 * @param what
	 */
	public void addPrintWhat(DebugEventType eventType, String what) {
		Map<String, PrintDetail> pw = printWhat.get(eventType);
		if (pw == null) {
			pw = new HashMap<>();
			printWhat.put(eventType, pw);
		}
		pw.put(what, PrintDetail.DTL_DETAIL);
		addObserveWhat(eventType);
	}

	/**
	 * Species an additional component to print at an event
	 * 
	 * It automatically turns on the listening to that event as well
	 * 
	 * @param what
	 */
	public void addPrintWhat(DebugEventType eventType, String what, PrintDetail detailLevel) {
		Map<String, PrintDetail> pw = printWhat.get(eventType);
		if (pw == null) {
			pw = new HashMap<>();
			printWhat.put(eventType, pw);
		}
		pw.put(what, detailLevel);
		addObserveWhat(eventType);
	}

	/**
	 * Clears the component from printing. If there is nothing left to print it
	 * will also remove the listening
	 * 
	 * @param what
	 */
	public void clearPrintWhat(DebugEventType eventType, String what) {
		Map<String, PrintDetail> pw = printWhat.get(eventType);
		if (pw == null) {
			return;
		}
		pw.remove(what);
		if (pw.isEmpty()) {
			removeObserveWhat(eventType);
		}
	}

	/**
	 * Returns a formatted text of the event
	 * 
	 * @param event
	 */
	private String formatEvent(DebugEvent event) {
		StringBuffer buffer = new StringBuffer();
		String header = event.toString();
		Map<String, PrintDetail> whatToPrintNow = printWhat.get(event.getEventType());
		// if it is only tracing, do not bother with the big header
		if (!traceWhat.isEmpty()) {
			buffer.append(formatTrace(event));
		}
		// if there are things to print, print them
		if (whatToPrintNow != null) {
			buffer.append(TextUiHelper.createHeader(header, "-", 100) + "\n");
			for (String what : whatToPrintNow.keySet()) {
				PrintDetail detailInt = whatToPrintNow.get(what);
				if (detailInt == null) {
					continue;
				}
				printSomething(event, buffer, what, detailInt);
			}
		}
		return buffer.toString();
	}

	/**
	 * Return the compact trace of the item currently in execution
	 * 
	 * Mostly relies on the loop current item
	 * 
	 * @param event
	 * 
	 * @return
	 */
	private String formatTrace(DebugEvent event) {
		return formatTraceString(agent, event.getEventType(), traceWhat);
	}

	/**
	 * A static function to help formatting a trace - to be used by the Xapi
	 * $Print as well
	 * 
	 * @param agent
	 * @param eventType
	 * @param traceWhat
	 * @return
	 */
	public static String formatTraceString(Agent agent, DebugEventType eventType, Set<TraceWhat> traceWhat) {
		StringBuffer buff = new StringBuffer();
		AbstractLoopItem current = agent.getLoop().getInExecution();
		if (current == null && !eventType.equals(DebugEventType.AFTER_LOOP_ITEM_EXECUTION)) {
			current = agent.getLoop().getHistory().get(agent.getLoop().getHistory().size() - 1);
		}
		if (current == null) {
			return "formatTraceString: No current execution!";
		}
		// at this moment we have the loop item
		TwFormatter pwf = new TwFormatter();
		pwf.add(String.format("%5.1f", agent.getTime()) + " > ");

		if (current instanceof liXapiScheduled) {
			return xwLiXapiScheduled.xwConcise(pwf, (liXapiScheduled)current, agent);
		}
		if (current instanceof liHlsChoiceBased) {
			return xwLiHlsChoiceBased.xwConcise(pwf, (liHlsChoiceBased)current, agent);
		}		
		if (current instanceof liViBased) {
			return xwLiViBased.xwConcise(pwf, (liViBased) current, agent);
		}		
		if (current instanceof liXapiReading) {
			return xwLiXapiReading.xwConcise(pwf, (liXapiReading) current, agent);
		}

		// buff.append("\n");
		if (traceWhat.contains(TraceWhat.VERBALIZATION)) {
			for (VerbInstance vi : current.getExecutionResult()) {
				buff.append("\n--- v ---" + agent.getVerbalize().verbalize(vi));
			}
		}
		if (traceWhat.contains(TraceWhat.CHOICES_NATIVE)) {
			buff.append("\n" + PpChoices.pp(
					agent.getHeadlessComponents().getChoices(HeadlessComponents.comparatorDependentScore), agent,
					PrintDetail.DTL_CONCISE));
		}
		if (traceWhat.contains(TraceWhat.CHOICES_DYNAMIC)) {
			buff.append("\n"
					+ PpChoices.pp(agent.getHeadlessComponents().getChoices(HeadlessComponents.comparatorMoodScore),
							agent, PrintDetail.DTL_CONCISE));
		}
		if (traceWhat.contains(TraceWhat.HLS_NEW_INSTANCES)) {
			buff.append("\n" + "HlsNewInstances\n" + PpHlsNewInstance
					.ppList(agent.getHeadlessComponents().getHlsNewInstances(), agent, PrintDetail.DTL_CONCISE));
		}
		return buff.toString();
	}

	public String getStepFileName() {
		return stepFileName;
	}

	/**
	 * @return the waitForKey
	 */
	public boolean isWaitForKey() {
		return waitForKey;
	}

	/**
	 * The callback function
	 */
	@Override
	public void observeInner(DebugEvent event) throws IOException, InterruptedException {
		PrintStream temp = TextUi.writer;
		// resetting after a deserialization
		if (printStream == null) {
			printStream = System.out;
		}
		TextUi.writer = printStream;
		String eventText = formatEvent(event);
		if (printToOutput) {
			TextUi.println(eventText);
		}
		if (printToTextFiles) {
			String fileName = stepFileName + String.format("%03d", stepCount);
			stepCount++;
			FileWritingUtil.writeToTextFile(new File(outputDir, fileName + ".txt"), eventText);
		}
		if (printToFile) {
			// should be append!!!
			FileWritingUtil.appendToTextFile(outputFile, eventText);
		}
		TextUi.writer = temp;
		if (waitForKey) {
			waitForKey = TextUi.confirm("Press <ENTER> to continue, [n] to not stop any more", true);
		}
	}

	/**
	 * Concise printing, directly to the output
	 * 
	 * @param what
	 * @param detailInt
	 */
	public void ppc(String what) {
		TextUi.println(printNow(what, PrintDetail.DTL_CONCISE));
	}

	/**
	 * Concise printing, directly to the output
	 * 
	 * @param what
	 * @param detailInt
	 */
	public void ppd(String what) {
		TextUi.println(printNow(what, PrintDetail.DTL_DETAIL));
	}

	/**
	 * One shot printing of some stuff
	 * 
	 * @param what
	 * @param detailInt
	 */
	public String printNow(String what, PrintDetail detailLevel) {
		StringBuffer buffer = new StringBuffer();
		printSomething(null, buffer, what, detailLevel);
		return buffer.toString();
	}

	/**
	 * @param event
	 * @param buffer
	 * @param what
	 * @param detailInt
	 * @throws Error
	 */
	private void printSomething(DebugEvent event, StringBuffer buffer, String what, PrintDetail detailLevel)
			throws Error {
		HeadlessComponents hls = agent.getHeadlessComponents();
		Focus fc = agent.getFocus();
		switch (what) {
		case "AGENT": {
			buffer.append(TextUiHelper.createHeader("Agent", "*", 100) + "\n");
			buffer.append(agent.toString() + "\n");
			break;
		}
		case "FOCUS": {
			buffer.append(TextUiHelper.createHeader("Focus", "*", 100) + "\n");
			buffer.append(PrettyPrint.pp(fc, agent, detailLevel) + "\n");
			break;
		}
		case "ALL_FOCUS_INSTANCES": {
			buffer.append(TextUiHelper.createHeader("Focus instances", "*", 100) + "\n");
			buffer.append(PrettyPrint.pp(fc.getInstanceList(EnergyColors.FOCUS_INSTANCE), agent, detailLevel) + "\n");
			break;
		}
		case "FOCUS_STRUCTURED": {
			buffer.append(SpFocus.spFocus(fc, agent) + "\n");
			break;
		}
		case "ALL_FOCUS_VERBINSTANCES": {
			buffer.append(TextUiHelper.createHeader("Focus verb instances", "*", 100) + "\n");
			buffer.append(PrettyPrint.pp(fc.getViList(EnergyColors.FOCUS_VI), agent, detailLevel) + "\n");
			break;
		}
		case "SHADOW_INSTANCES": {
			buffer.append(TextUiHelper.createHeader("Shadow instances", "*", 100) + "\n");
			buffer.append(PpShadows.ppPrint(agent.getShadows(), agent, true, false) + "\n");
			break;
		}
		case "SHADOW_VIS": {
			buffer.append(TextUiHelper.createHeader("Shadow", "*", 100) + "\n");
			buffer.append(PpShadows.ppPrint(agent.getShadows(), agent, false, true) + "\n");
			break;
		}
		case "LAST_VERBINSTANCE": {
			buffer.append(TextUiHelper.createHeader("Last VerbInstance", "*", 100) + "\n");
			buffer.append(PrettyPrint.pp(agent.getLastVerbInstance(), agent, detailLevel) + "\n");
			break;
		}
		case "HEADLESS_COMPONENTS": {
			buffer.append(TextUiHelper.createHeader("Headless shadows", "*", 100) + "\n");
			buffer.append(PrettyPrint.pp(hls, agent, detailLevel));
			break;
		}
		case "TIME": {
			buffer.append("t=" + Formatter.fmt(agent.getTime()) + "\n");
			break;
		}
		case "INSTANCE_RESOLUTION_CHOICES": {
			buffer.append(TextUiHelper.createHeader("Instance resolution choices", "*", 100) + "\n");
			if (event.getEventType() != DebugEventType.AFTER_INSTANCE_RESOLUTION) {
				throw new Error("Instance resolution choices can only be printed after instance resolution!");
			}
			buffer.append(PpInstanceResolutionChoices.pp(event.getObjects(), agent));
			break;
		}
		case "CHOICES_NATIVE": {
			buffer.append(TextUiHelper.createHeader("Recall choices sorted by native", "*", 100) + "\n");
			buffer.append(
					PpChoices.pp(hls.getChoices(HeadlessComponents.comparatorDependentScore), agent, detailLevel));
			break;
		}
		case "ALL_CHOICES": {
			buffer.append(TextUiHelper.createHeader("Recall choices sorted by dynamic score", "*", 100) + "\n");
			buffer.append(PpChoices.pp(hls.getChoices(HeadlessComponents.comparatorMoodScore), agent, detailLevel));
			break;
		}
		case "HLS_NEW_INSTANCES": {
			buffer.append(TextUiHelper.createHeader("collection of HlsNewInstance", "*", 100) + "\n");
			buffer.append(PpHlsNewInstance.ppList(hls.getHlsNewInstances(), agent, detailLevel));
			break;
		}
		case "PERFORMANCE": {
			buffer.append(TextUiHelper.createHeader("Performance", "*", 100) + "\n");
			buffer.append(PrettyPrint.pp(agent.getDebugInfo().getPerformanceMeter(), agent, detailLevel));
			// reset the performance meter
			agent.getDebugInfo().getPerformanceMeter().timeCountReset();
			break;
		}
		case "LOOP": {
			buffer.append(TextUiHelper.createHeader("Loop", "*", 100) + "\n");
			buffer.append(PrettyPrint.pp(agent.getLoop(), agent, detailLevel));
			break;
		}
		case "LOOP_CURRENT": {
			buffer.append(TextUiHelper.createHeader("Loop currently executing", "*", 100) + "\n");
			AbstractLoopItem current = agent.getLoop().getInExecution();
			if (current != null) {
				buffer.append(PrettyPrint.pp(current, agent, detailLevel));
			} else {
				buffer.append("No LoopItem is executing currently.\n");
			}
			break;
		}
		default: {
			buffer.append("ToStringObserver can not handle query: " + what);
			break;
		}
		}
	}

	/**
	 * Sets the output
	 * 
	 * @param file
	 */
	public void setOutput(File file) {
		outputFile = file;
	}

	public void setOutputDir(File outputDir) {
		this.outputDir = outputDir;
	}

	public void setPrintToFile(boolean printToFile) {
		this.printToFile = printToFile;
	}

	public void setPrintToOutput(boolean printToOutput) {
		this.printToOutput = printToOutput;
	}

	public void setPrintToTextFiles(boolean printToTextFiles) {
		this.printToTextFiles = printToTextFiles;
	}

	public void setStepFileName(String stepFileName) {
		this.stepFileName = stepFileName;
	}

	/**
	 * Turning on the trace and specifying what to trace
	 */
	public void setTrace(TraceWhat... what) {
		traceWhat.clear();
		for (TraceWhat w : what) {
			traceWhat.add(w);
		}
		traceWhat.add(TraceWhat.COMPACT);
		addObserveWhat(DebugEventType.AFTER_LOOP_ITEM_EXECUTION);
	}

	/**
	 * @param waitForKey
	 *            the waitForKey to set
	 */
	public void setWaitForKey(boolean waitForKey) {
		this.waitForKey = waitForKey;
	}

}

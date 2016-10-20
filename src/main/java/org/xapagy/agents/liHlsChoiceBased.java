package org.xapagy.agents;

import java.util.List;

import org.xapagy.headless_shadows.Choice;
import org.xapagy.headless_shadows.ExecuteChoice;
import org.xapagy.headless_shadows.HeadlessComponents;

public class liHlsChoiceBased extends AbstractLoopItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5668168521389864977L;
	/**
	 * For internal recalls, the choice which let to its instantiation
	 */
	private Choice choice;
	/**
	 * The choices which are active when the loop item goes into execution.
	 * These will be recorded only when the parameter is set
	 */
	private List<Choice> recordedChoices = null;
	/**
	 * For internal events, the willingness of the choice with which it was
	 * instantiated
	 */
	private double willingness = 0;

	public liHlsChoiceBased(Agent agent, double time, Choice choice, double willingness) {
		super(agent);
		this.type = LoopItemType.HLS_CHOICE_BASED;
		this.executionTime = time;
		this.willingness = willingness;
		this.choice = choice;
	}
	

	/**
	 * Returns how much pause are we going to have after after a specific
	 * choice. Currently, all of them imply a pause of 1.0 second.
	 * 
	 * Essentially, this is the time we have to run the Da's before the next
	 * loop, so if we return 0 here, we won't take into account the newly
	 * created VI.
	 * 
	 * @return
	 */
	public double getPauseAfterInternalLoopItem() {
		switch (choice.getChoiceType()) {
		case CHARACTERIZATION:
			return 1.0;
		case CONTINUATION:
			return 1.0;
		case MISSING_ACTION:
			return 1.0;
		case MISSING_RELATION:
			return 1.0;
		case STATIC:
			return 1.0;
		}
		throw new Error("unknown choice type");
	}

	
	/**
	 * @return the willingness
	 */
	public double getWillingness() {
		return willingness;
	}


	/**
	 * Returns the recorded choices...
	 * 
	 * @return
	 */
	public List<Choice> getRecordedChoices() {
		return recordedChoices;
	}

	/**
	 * @return the choice
	 */
	public Choice getChoice() {
		return choice;
	}

	@Override
	protected void internalExecute() {
		//
		// if this parameter is turned on, the choices which had been active at
		// the moment the loopitem had been executed will be recorded into the
		// LoopItem - this would allow us to debug why certain choices had been
		// chosen at certain moments.
		//
		boolean debugRecordChoices = agent.getParameters().getBoolean("A_DEBUG", "G_GENERAL",
				"N_RECORD_CHOICES_INTO_LOOPITEM");
		if (debugRecordChoices) {
			recordedChoices = agent.getHeadlessComponents().getChoices(HeadlessComponents.comparatorMoodScore);
		}
		executionResult = ExecuteChoice.executeChoice(agent, choice);
		double pause = getPauseAfterInternalLoopItem();
		Execute.executeDiffusionActivities(agent, pause);
	}

	@Override
	public String formatException(Throwable t, String description) {
		return description + t.toString() ;
	}

	
}

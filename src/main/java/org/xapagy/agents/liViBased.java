package org.xapagy.agents;

import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;

/**
 * A VI based loopitem. Used in the case when we already have the VI we want to
 * execute in the loop item.
 *
 * @author lboloni
 *
 */
public class liViBased extends AbstractLoopItem {

	private static final long serialVersionUID = 4361342266891541296L;
	/**
	 * The VI we have forced when it is of type FORCED
	 */
	private VerbInstance forcedVi = null;
	/**
	 * The time after we are forcng
	 */
	private double forcedTimeAfter = 0;

	/**
	 * Creates a forced VI loop item
	 *
	 * @param agent
	 * @param time
	 *            - normally the current time - this will be the execution time
	 * @param forcedVi
	 *            - the VI whose execution we are forcing at this moment
	 * @param forcedTimeAfter
	 *            - the time after the execution, when the DAs will operate
	 */
	public liViBased(Agent agent, VerbInstance forcedVi, double forcedTimeAfter) {
		super(agent);
		this.forcedVi = forcedVi;
		this.forcedTimeAfter = forcedTimeAfter;
	}

	@Override
	public String formatException(Throwable t, String description) {
		return description + t.toString();
	}

	/**
	 * @return the forcedTimeAfter
	 */
	public double getForcedTimeAfter() {
		return forcedTimeAfter;
	}

	/**
	 * @return the forcedVi
	 */
	public VerbInstance getForcedVi() {
		return forcedVi;
	}

	/**
	 * Executes a LoopItem of type "Forced". This assumes that we have the VI.
	 */
	@Override
	protected void internalExecute() {
		Execute.executeVIandSAs(agent, forcedVi, null);
		for (Instance scene : forcedVi.getReferencedScenes()) {
			scene.getSceneParameters().resetInterstitialEnergy();
		}
		executionResult.add(forcedVi);
		if (forcedTimeAfter > 0) {
			Execute.executeDiffusionActivities(agent, forcedTimeAfter);
		}
	}

}

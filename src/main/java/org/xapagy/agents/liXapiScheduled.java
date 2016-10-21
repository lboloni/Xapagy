package org.xapagy.agents;

public class liXapiScheduled extends AbstractLoopItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4790922158730493388L;
	private double scheduledExecutionTime = -1;

	/**
	 * @param xapiText
	 * @param time
	 */
	public liXapiScheduled(Agent agent, String xapiText, double time) {
		super(agent);
		this.state = LoopItemState.NOT_EXECUTED;
		this.xapiText = xapiText;
		this.scheduledExecutionTime = time;
	}

	/**
	 * @return the scheduledExecutionTime
	 */
	public double getScheduledExecutionTime() {
		return scheduledExecutionTime;
	}

	/**
	 * Executes a LoopItem of type "Forced". This assumes that we have the VI.
	 */
	@Override
	protected void internalExecute() {
		Execute.executeXapiText(agent, this);
	}

	@Override
	public String formatException(Throwable t, String description) {
		return "liScheduled: At generated Xapi = " + xapiText + "\nError was found: " + t.getClass().getCanonicalName()
				+ " " + description;
	}

}

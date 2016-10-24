package org.xapagy.agents;

/**
 * Implements a loop item that is based on interpreting a line of Xapi 
 * 
 * @author lboloni
 *
 */
public abstract class AbstractXapiLoopItem extends AbstractLoopItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3265034876385485965L;

	public String getXapiText() {
		return xapiText;
	}

	/**
	 * The xapi text which the agent is reading at this point - it 
	 * is strictly one line
	 */
	protected String xapiText;
	
	public AbstractXapiLoopItem(Agent agent, String xapiText) {
		super(agent);
		this.xapiText = xapiText;
	}
	
}

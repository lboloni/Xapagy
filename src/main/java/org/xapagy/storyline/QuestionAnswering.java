package org.xapagy.storyline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.instances.VerbInstance;
import org.xapagy.introspect.Introspect;
import org.xapagy.questions.QuestionAnswerPairing;
import org.xapagy.ui.TextUi;

/**
 * This class describes the story line based question answering mechanism
 * 
 * @author lboloni
 *
 */
public class QuestionAnswering {

	public static List<VerbInstance> answerQuestion(Agent agent, VerbInstance question) {
		List<VerbInstance> retval = new ArrayList<>();
		Introspect introspect = new Introspect(agent);
		TextUi.println("storyLine.QuestionAnswering.answerQuestion: only trivial question");
		return trivialAnswer(agent, introspect, question);
		// return retval;
	}

	/**
	 * Returns a trivial answer by checking the question against the current
	 * story line
	 * 
	 * @return
	 */
	public static List<VerbInstance> trivialAnswer(Agent agent, Introspect introspect, VerbInstance question) {
		List<VerbInstance> retval = new ArrayList<>();
		StoryLine line = introspect.currentStoryLine();
		List<VerbInstance> vis = new ArrayList<>(line.getVis());
		Collections.reverse(vis);
		for(VerbInstance cand: vis) {
			if (QuestionAnswerPairing.isPair(question, cand, agent, true)) {
				retval.add(cand);
				break;
			}
		}
		return retval;
	}

}

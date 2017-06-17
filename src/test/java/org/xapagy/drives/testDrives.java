package org.xapagy.drives;

import org.junit.Test;
import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;
import org.xapagy.instances.VerbInstance;
import org.xapagy.ui.TextUi;

public class testDrives {

	/**
	 * This is a development-type test
	 */
	@Test
	public void develTest() {
		String description = "Test how is the drives stuff is supposed to work";
		TestHelper.testStart(description);
		Runner r = ArtificialDomain.runnerArtificialAutobiography();
		r.exec("$CreateScene #one CloseOthers With Instances w_c_bai20 'Hector', w_c_bai21 'Achilles'");
		// this is how I set the target value and equilibrium value of a drive
		r.exec("!!agent.getDrives().setTargetValue('Drive_Pleasure_Seeking',2.0);");
		r.exec("!!agent.getDrives().setEquilibriumValue('Drive_Pleasure_Seeking',2.0);");
		// this is how I set the self
		r.exec("!!cself = ref.InstanceByRef('\"Achilles\"');");
		r.exec("!!agent.getDrives().setSelf(cself);");
		TextUi.println(r.agent.getDrives());
		//
		// Setting from Xapi the impact of verb v_av40 on the pleasure seeking
		// drive of the subject
		//
		r.exec("!!v = agent.getVerbDB().getConcept('v_av40');");
		r.exec("!!print(v);");
		r.exec("!!v.setDriveImpactOnSubject('Drive_Pleasure_Seeking',1.0);");
		//
		// Setting of the impact of time -- on v_does_nothing on tiredness
		//
		r.exec("!!v = agent.getVerbDB().getConcept('v_does_nothing');");
		r.exec("!!print(v);");
		r.exec("!!v.setDriveImpactOnSubject('Drive_Tiredness',1.0);");

		//
		// Now let us see whether it works
		//
		VerbInstance vi = r.exac("'Achilles'/ wa_v_av40 / 'Hector'.");
		TextUi.println(r.agent.getDrives());
		r.exec("-");
		TextUi.println(r.agent.getDrives());
		for (int i = 0; i < 100; i++) {
			r.exec("-");
			TextUi.println(r.agent.getDrives());
		}
	}

}

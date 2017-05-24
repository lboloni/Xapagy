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
        String description =
                "Test how is the drives stuff is supposed to work";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.exec("$CreateScene #one CloseOthers With Instances w_c_bai20 'Hector', w_c_bai21 'Achilles'");
        // this is how I set the target value and equilibrium value of a drive
        r.exec("!!agent.getDrives().setTargetValue('Drive_PleasureSeeking',2.0);");
        r.exec("!!agent.getDrives().setEquilibriumValue('Drive_PleasureSeeking',2.0);");
        // this is how I set the self
        r.exec("!!cself = ref.InstanceByRef('\"Achilles\"');");
        r.exec("!!agent.getDrives().setSelf(cself);");
        // now let us assume that the v_av40 increases the pleasure
        r.exec("!!v = agent.getVerbDB().getConcept('v_av40');");
        r.exec("!!print(v);");
        r.exec("!!v.setDriveImpactOnSubject('v_av40',1.0);");
        VerbInstance vi = r.exac("'Achilles'/ wa_v_av40 / 'Hector'.");
        TextUi.println(r.agent.getDrives());
 
	}
	

}

package org.xapagy.drives;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;
import org.xapagy.instances.VerbInstance;
import org.xapagy.ui.TextUi;
import org.xapagy.drives.Drives;

public class testDrives {

	/**
	 * Checks how the drives are changed with different actions
	 */
	@Test
	public void testDriveChanges() {
		String description = "the way various VIs change the drives";
		TestHelper.testStart(description);
		Runner r = commonDriveTests.createScenario();
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
		double tiredness = r.agent.getDrives().getCurrentValue(Drives.DRIVE_TIREDNESS);
		assertTrue(tiredness > 1.0);
		double pleasureSeeking = r.agent.getDrives().getCurrentValue(Drives.DRIVE_PLEASURE_SEEKING);
		assertTrue(pleasureSeeking > 1.0);
	}
}

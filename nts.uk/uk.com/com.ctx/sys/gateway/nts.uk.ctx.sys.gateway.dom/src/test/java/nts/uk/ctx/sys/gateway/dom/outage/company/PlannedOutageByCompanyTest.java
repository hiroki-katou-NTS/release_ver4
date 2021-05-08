package nts.uk.ctx.sys.gateway.dom.outage.company;

import org.junit.Test;

import mockit.Mocked;
import nts.arc.testing.assertion.NtsAssert;
import nts.uk.ctx.sys.gateway.dom.outage.PlannedOutageState;

public class PlannedOutageByCompanyTest {

	@Test
	public void getter() {
		NtsAssert.invokeGetters(new PlannedOutageByCompany(null, null));
	}

	@Test
	public void setter(@Mocked PlannedOutageState state) {
		new PlannedOutageByCompany(null, null).setState(state);
	}

	@Test
	public void setter_null() {
		NtsAssert.systemError(
				ex -> ex.getClass() == NullPointerException.class,
				() -> {
					new PlannedOutageByCompany(null, null).setState(null);
				});
	}

}

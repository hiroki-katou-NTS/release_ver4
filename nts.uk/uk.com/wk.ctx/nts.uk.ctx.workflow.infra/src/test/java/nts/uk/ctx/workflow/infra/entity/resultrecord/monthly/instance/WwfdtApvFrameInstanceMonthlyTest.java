package nts.uk.ctx.workflow.infra.entity.resultrecord.monthly.instance;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import lombok.val;
import nts.arc.testing.assertion.NtsAssert;
import nts.uk.ctx.workflow.dom.resultrecord.AppFrameInstance;

public class WwfdtApvFrameInstanceMonthlyTest {

	@Test
	public void test() {
		val data = create();
		String rootID = "ru-toID";
		Integer phaseOrder = 8;
		
		WwfdtApvFrameInstanceMonthly result = (WwfdtApvFrameInstanceMonthly)NtsAssert.Invoke.staticMethod(
				WwfdtApvFrameInstanceMonthly.class,
				"fromDomain",
				rootID,
				phaseOrder,
				data
		);
		
		assertThat("PK_ROOT_ID", result.getPk().getRootID(), is(rootID));
		assertThat("PK_PHASE_ORDER", result.getPk().getPhaseOrder(), is(phaseOrder));
		assertThat("PK_FRAME_ORDER", result.getPk().getFrameOrder(), is(data.getFrameOrder()));
		assertThat("CONFIRM_ATR", result.getConfirmAtr(), is(data.isConfirmAtr()?1:0));
		assertThat("List_Size", result.listWwfdtApvApproveInstanceMonthly.size(), is(data.getListApprover().size()));
	}

	private AppFrameInstance create(){
		List<String> approverList = new ArrayList<>();
		approverList.add("a");
		approverList.add("b");
		approverList.add("c");
		approverList.add("d");
		return new AppFrameInstance(
						99, 
						false, 
						approverList
					);
	}
}

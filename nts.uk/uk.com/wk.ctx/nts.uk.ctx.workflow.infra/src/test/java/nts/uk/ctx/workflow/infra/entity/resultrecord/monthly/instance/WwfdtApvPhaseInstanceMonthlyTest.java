package nts.uk.ctx.workflow.infra.entity.resultrecord.monthly.instance;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import lombok.val;
import nts.arc.testing.assertion.NtsAssert;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalForm;
import nts.uk.ctx.workflow.dom.resultrecord.AppFrameInstance;
import nts.uk.ctx.workflow.dom.resultrecord.AppPhaseInstance;
import nts.uk.ctx.workflow.infra.entity.resultrecord.monthly.instance.WwfdtApvPhaseInstanceMonthly;

public class WwfdtApvPhaseInstanceMonthlyTest {

	@Test
	public void test() {
		val data = create();
		
		String rootID = "rootID";
		
		WwfdtApvPhaseInstanceMonthly result = (WwfdtApvPhaseInstanceMonthly)NtsAssert.Invoke.staticMethod(
				WwfdtApvPhaseInstanceMonthly.class,
				"fromDomain",
				rootID,
				data
		);
		
		assertThat("ROOT_ID", result.getPk().getRootID(), is(rootID));
		assertThat("PHASE_ORDER", result.getPk().getPhaseOrder(), is(data.getPhaseOrder()));
		assertThat("APPROVAL_FORM", result.getApprovalForm(), is(data.getApprovalForm().value));
		assertThat("List_Size", result.listWwfdtApvFrameInstanceMonthly.size(), is(data.getListAppFrame().size()));
		
	}

	private AppPhaseInstance create() {
		List<AppFrameInstance> frameIns = new ArrayList<>();
		frameIns.add(new AppFrameInstance(0, true, new ArrayList<>()));
		frameIns.add(new AppFrameInstance(1, false, new ArrayList<>()));
		
		return new AppPhaseInstance(
				2, 
				ApprovalForm.SINGLE_APPROVED, 
				frameIns
				);
	}
}

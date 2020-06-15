package nts.uk.ctx.workflow.infra.entity.resultrecord.monthly.instance;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;

import nts.arc.testing.assertion.NtsAssert;
import nts.uk.ctx.workflow.infra.repository.resultrecord.JpaAppRootInstanceRepository;

public class WwfdtApvApproveInstanceMonthlyTest {

	@Test
	public void test() {
		String rootID = "ルーID";
		Integer phaseOrder = 4;
		Integer frameOrder = 64;
		String approverChildID = "ちる";
		
		WwfdtApvApproveInstanceMonthly result = (WwfdtApvApproveInstanceMonthly)NtsAssert.Invoke.staticMethod(
				WwfdtApvApproveInstanceMonthly.class,
				"fromDomain",
				rootID,
				phaseOrder,
				frameOrder,
				approverChildID
		);
		
		assertThat("PK_ROOT_ID", result.getPk().getRootID(), is(rootID));
		assertThat("PK_PHASE_ORDER", result.getPk().getPhaseOrder(), is(phaseOrder));
		assertThat("PK_FRAME_ORDER", result.getPk().getFrameOrder(), is(frameOrder));
		assertThat("PK_APPROVER_CHILD_ID", result.getPk().getApproverChildID(), is(approverChildID));
		
	}

}

package nts.uk.ctx.workflow.infra.entity.resultrecord.daily.instance;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;

import nts.arc.testing.assertion.NtsAssert;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.infra.repository.resultrecord.JpaAppRootInstanceRepository;

public class WwfdtApvApproveInstanceDailyTest {

	@Test
	public void test() {
		String rootID = "るるID";
		Integer phaseOrder = 46;
		Integer frameOrder = 51;
		String approverChildID = "ちるID"; 
		String companyID = "こんID";
		String employeeID = "えID"; 
		GeneralDate startDate = GeneralDate.ymd(4215, 11, 29);
		
		WwfdtApvApproveInstanceDaily result = (WwfdtApvApproveInstanceDaily)NtsAssert.Invoke.staticMethod(
				WwfdtApvApproveInstanceDaily.class,
				"fromDomain",
				rootID,
				phaseOrder,
				frameOrder,
				approverChildID,
				companyID,
				employeeID,
				startDate
		);
		
		assertThat("ROOT_ID", result.getPk().getRootID(), is(rootID));
		assertThat("PHASE_ORDER", result.getPk().getPhaseOrder(), is(phaseOrder));
		assertThat("FRAME_ORDER", result.getPk().getFrameOrder(), is(frameOrder));
		assertThat("APPROVER_CHILD_ID", result.getPk().getApproverChildID(), is(approverChildID));
		assertThat("CID", result.getCompanyID(), is(companyID));
		assertThat("EMPLOYEE_ID", result.getEmployeeID(), is(employeeID));
		assertThat("START_DATE", result.getStartDate(), is(startDate));
	}
	

}

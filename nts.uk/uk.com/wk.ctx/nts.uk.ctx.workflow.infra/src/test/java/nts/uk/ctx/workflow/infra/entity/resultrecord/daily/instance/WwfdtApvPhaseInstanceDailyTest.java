package nts.uk.ctx.workflow.infra.entity.resultrecord.daily.instance;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import lombok.val;
import nts.arc.testing.assertion.NtsAssert;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalForm;
import nts.uk.ctx.workflow.dom.resultrecord.AppFrameInstance;
import nts.uk.ctx.workflow.dom.resultrecord.AppPhaseInstance;

public class WwfdtApvPhaseInstanceDailyTest {

	@Test
	public void test() {
		val data = create();
		String rootID = "るーとID";
		String companyID = "かんぱID";
		String employeeID = "えんID";
		GeneralDate startDate = GeneralDate.ymd(2525, 2, 5);
		
		WwfdtApvPhaseInstanceDaily result = (WwfdtApvPhaseInstanceDaily)NtsAssert.Invoke.staticMethod(
				WwfdtApvPhaseInstanceDaily.class,
				"fromDomain",
				rootID,
				companyID,
				employeeID,
				startDate,
				data
		);
		
		assertThat("PK_ROOT_ID", result.getPk().getRootID(), is(rootID));
		assertThat("PK_PHASE_ORDER", result.getPk().getPhaseOrder(), is(data.getPhaseOrder()));
		assertThat("CID", result.getCompanyID(), is(companyID));
		assertThat("EMPLOYEE_ID", result.getEmployeeID(), is(employeeID));
		assertThat("START_DATE", result.getStartDate(), is(startDate));
		assertThat("APPROVAL_FORM", result.getApprovalForm(), is(data.getApprovalForm().value));
		assertThat("List_Size", result.listWwfdtApvFrameInstanceDaily.size(), is(data.getListAppFrame().size()));
	}

	private AppPhaseInstance create(){
		List<AppFrameInstance> frameIns = new ArrayList<>();
		frameIns.add(new AppFrameInstance(
				0, 
				true, 
				new ArrayList<>()));
		frameIns.add(new AppFrameInstance(
				1, 
				false, 
				new ArrayList<>()));
		return new AppPhaseInstance(
				15, 
				ApprovalForm.SINGLE_APPROVED, 
				frameIns
		);
	}
}

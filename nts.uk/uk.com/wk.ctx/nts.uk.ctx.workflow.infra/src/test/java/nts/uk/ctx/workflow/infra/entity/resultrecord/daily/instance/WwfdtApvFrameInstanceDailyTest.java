package nts.uk.ctx.workflow.infra.entity.resultrecord.daily.instance;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import lombok.val;
import nts.arc.testing.assertion.NtsAssert;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.resultrecord.AppFrameInstance;

public class WwfdtApvFrameInstanceDailyTest {

	@Test
	public void test() {
		val data = create();
		String rootID = "るーとID";
		String companyID = "かんぱID";
		String employeeID = "えんID";
		GeneralDate startDate = GeneralDate.ymd(2525, 2, 5);
		
		WwfdtApvFrameInstanceDaily result = (WwfdtApvFrameInstanceDaily)NtsAssert.Invoke.staticMethod(
				WwfdtApvFrameInstanceDaily.class,
				"fromDomain",
				rootID,
				99,
				companyID,
				employeeID,
				startDate,
				data
		);
		
		assertThat("PK_ROOT_ID", result.getPk().getRootID(), is(rootID));
		assertThat("PK_PHASE_ORDER", result.getPk().getPhaseOrder(), is(99));
		assertThat("PK_FRAME_ORDER", result.getPk().getPhaseOrder(), is(data.getFrameOrder()));
		assertThat("CID", result.getCompanyID(), is(companyID));
		assertThat("EMPLOYEE_ID", result.getEmployeeID(), is(employeeID));
		assertThat("START_DATE", result.getStartDate(), is(startDate));
		assertThat("List_Size", result.listWwfdtApvApproveInstanceDaily.size(), is(data.getListApprover().size()));
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

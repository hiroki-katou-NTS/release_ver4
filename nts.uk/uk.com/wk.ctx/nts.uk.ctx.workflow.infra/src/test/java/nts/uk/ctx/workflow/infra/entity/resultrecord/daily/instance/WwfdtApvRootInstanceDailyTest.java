package nts.uk.ctx.workflow.infra.entity.resultrecord.daily.instance;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import lombok.val;
import nts.arc.testing.assertion.NtsAssert;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalForm;
import nts.uk.ctx.workflow.dom.resultrecord.AppPhaseInstance;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootInstance;
import nts.uk.ctx.workflow.dom.resultrecord.RecordRootType;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public class WwfdtApvRootInstanceDailyTest {

	@Test
	public void test() {
		val data = create();
		WwfdtApvRootInstanceDaily result = (WwfdtApvRootInstanceDaily)NtsAssert.Invoke.staticMethod(
				WwfdtApvRootInstanceDaily.class,
				"fromDomain",
				data
		);
		
		assertThat("ROOT_ID", result.getRootID(), is(data.getRootID()));
		assertThat("CID", 	  result.getCompanyID(), is(data.getCompanyID()));
		assertThat("EMPLOYEE_ID", result.getEmployeeID(), is(data.getEmployeeID()));
		assertThat("START_DATE",  result.getStartDate(), is(data.getDatePeriod().start()));
		assertThat("END_DATE",    result.getEndDate(), is(data.getDatePeriod().end()));
		assertThat("ListSize",    result.listWwfdtApvPhaseInstanceDaily.size(), is(data.getListAppPhase().size()));
	}

	
	private AppRootInstance create() {
		List<AppPhaseInstance> phaseIns = new ArrayList<>();
		phaseIns.add(new AppPhaseInstance(
				1, 
				ApprovalForm.SINGLE_APPROVED, 
				new ArrayList<>())
				);
		phaseIns.add(new AppPhaseInstance(
				2, 
				ApprovalForm.EVERYONE_APPROVED, 
				new ArrayList<>())
				);
		
		return new AppRootInstance(
				"ルーティID", 
				"ケイシャID", 
				"エピピID", 
				new DatePeriod(GeneralDate.ymd(2000, 1, 1), GeneralDate.ymd(9999, 12, 31)), 
				RecordRootType.CONFIRM_WORK_BY_DAY, 
				phaseIns);
	}
	
}

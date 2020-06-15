package nts.uk.ctx.workflow.infra.entity.approverstatemanagement.application;

import static org.junit.Assert.*;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;

import lombok.val;
import nts.arc.testing.assertion.NtsAssert;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalRootState;

public class WwfdtAppApvRootStateTest {

	@Test
	public void test() {
		val data = create();
		
		WwfdtAppApvRootState result = (WwfdtAppApvRootState)NtsAssert.Invoke.staticMethod(
				WwfdtAppApvRootState.class,
				"fromDomain",
				data);
		
		assertThat("PK_APPID", result.wwfdpAppApvRootStatePK.appID , is(data.getRootStateID()));
		assertThat("CID", result.companyID , is(data.getCompanyID()));
		assertThat("EMPLOYEE_ID", result.employeeID , is(data.getEmployeeID()));
		assertThat("APP_DATE", result.appDate , is(data.getApprovalRecordDate()));
		assertThat("PHASESTATE_LISTSIZE", result.listWwfdtAppApvPhaseState.size(), is(data.getListApprovalPhaseState().size()));
	}

	private ApprovalRootState create() {
		return new ApprovalRootState(
				"コケ会社ＩＤ", 
				"ルーティＩＤ", 
				"平社員ＩＤ", 
				GeneralDate.ymd(2051, 7, 16), 
				new ArrayList<>());
	}

}

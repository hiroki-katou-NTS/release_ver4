package nts.uk.ctx.workflow.infra.entity.approverstatemanagement.application;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;

import lombok.val;
import nts.arc.testing.assertion.NtsAssert;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApproverState;

public class WwfdtAppApvApproverStateTest {

	@Test
	public void test() {
		val data = create();
		
		WwfdtAppApvApproverState result = (WwfdtAppApvApproverState)NtsAssert.Invoke.staticMethod(
				WwfdtAppApvApproverState.class,
				"fromDomain",
				"コンパニアイディ",
				"エンポロイアイディ",
				GeneralDate.ymd(2010, 2, 10),
				data
				);
		
		assertThat("PK_APPID", result.wwfdpAppApvApproverStatePK.appID, is(data.getRootStateID()));
		assertThat("PK_PHASEORDER", result.wwfdpAppApvApproverStatePK.phaseOrder, is(data.getPhaseOrder()));
		assertThat("PK_FRAMEORDER", result.wwfdpAppApvApproverStatePK.frameOrder, is(data.getFrameOrder()));
		assertThat("PK_CHILDAPPID", result.wwfdpAppApvApproverStatePK.approverID, is(data.getApproverID()));
		assertThat("CID"		, result.companyID, is("コンパニアイディ"));
		assertThat("EMPLOYEE_ID", result.employeeID, is("エンポロイアイディ"));
		assertThat("APP_DATE"	, result.appDate, is(GeneralDate.ymd(2010, 2, 10)));
	}
	
	private ApproverState create() {
		return new ApproverState(
				"ルードID", 
				59, 
				41, 
				"アペID", 
				"ｋォンＩＤ", 
				GeneralDate.ymd(2001, 3, 25));
	}
}

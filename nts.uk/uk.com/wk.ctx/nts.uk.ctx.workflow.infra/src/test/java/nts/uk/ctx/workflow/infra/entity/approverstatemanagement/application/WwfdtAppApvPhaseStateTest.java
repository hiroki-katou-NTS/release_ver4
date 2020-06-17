package nts.uk.ctx.workflow.infra.entity.approverstatemanagement.application;

import static org.junit.Assert.*;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;

import nts.arc.testing.assertion.NtsAssert;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalForm;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalBehaviorAtr;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalPhaseState;

public class WwfdtAppApvPhaseStateTest {

	@Test
	public void test() {
		
		ApprovalPhaseState phaseState = createPhaseState();
		WwfdtAppApvPhaseState result = (WwfdtAppApvPhaseState)NtsAssert.Invoke.staticMethod(
				WwfdtAppApvPhaseState.class, 
				"fromDomain", 
				"カンパニーアイディー",
				"エンプロイアイディー",
				GeneralDate.ymd(2000, 4, 21),
				phaseState
				);
		
		assertThat("PK_APPID", result.wwfdpAppApvPhaseStatePK.getAppID(), is(phaseState.getRootStateID()));
		assertThat("PK_ORDER", result.wwfdpAppApvPhaseStatePK.phaseOrder, is(phaseState.getPhaseOrder()));
		assertThat("CID"	 , result.companyID							, is("カンパニーアイディー"));
		assertThat("EMPLOYEE_ID", result.employeeID, is("エンプロイアイディー"));
		assertThat("APP_DATE"	, result.appDate   , is(GeneralDate.ymd(2000, 4, 21)));
		assertThat("APP_PHASE_ATR", result.approvalAtr,  is(phaseState.getApprovalAtr().value));
		assertThat("APPROVAL_FORM", result.approvalForm, is(phaseState.getApprovalForm().value));
		assertThat("FrameStateList",result.listWwfdtAppApvFrameState.size(),is(phaseState.getListApprovalFrame().size()));
	}
	
	private ApprovalPhaseState createPhaseState() {
		return new ApprovalPhaseState(
				"ルートアイディー", 
				Integer.valueOf(5284), 
				ApprovalBehaviorAtr.REMAND, 
				ApprovalForm.SINGLE_APPROVED, 
				new ArrayList<>()
				);
	}
}

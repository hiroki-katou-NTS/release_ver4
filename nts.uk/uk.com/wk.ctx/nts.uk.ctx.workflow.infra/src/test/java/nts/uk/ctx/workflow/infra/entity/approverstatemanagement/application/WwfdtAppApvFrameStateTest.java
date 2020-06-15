package nts.uk.ctx.workflow.infra.entity.approverstatemanagement.application;

import static org.junit.Assert.*;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;

import lombok.val;
import nts.arc.testing.assertion.NtsAssert;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ConfirmPerson;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalBehaviorAtr;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalFrame;

public class WwfdtAppApvFrameStateTest {

	@Test
	public void test() {
		val data = create();
		
		WwfdtAppApvFrameState result = (WwfdtAppApvFrameState)NtsAssert.Invoke.staticMethod(
				WwfdtAppApvFrameState.class, 
				"fromDomain", 
				"カカカカカンパニーID",
				"エエエエエンプロイID",
				GeneralDate.ymd(1999, 12, 31),
				data
				);
		
		assertThat("PK_APPID", result.wwfdpAppApvFrameStatePK.appID, is(data.getRootStateID()));
		assertThat("PK_PHASE_ORDER", result.wwfdpAppApvFrameStatePK.phaseOrder, is(data.getPhaseOrder()));
		assertThat("PK_FRAME_ORDER", result.wwfdpAppApvFrameStatePK.frameOrder, is(data.getFrameOrder()));
		assertThat("CID", result.companyID, is("カカカカカンパニーID"));
		assertThat("EMPLOYEE_ID", result.employeeID, is("エエエエエンプロイID"));
		assertThat("APP_DATE", result.appDate, is(GeneralDate.ymd(1999, 12, 31)));
		assertThat("APP_FRAME_ATR", result.approvalAtr, is(data.getApprovalAtr().value));
		assertThat("CONFIRM_ATR", result.confirmAtr, is(data.getConfirmAtr().value));
		assertThat("APPROVER_ID", result.approverID, is("アアアアプローバルID"));
		assertThat("REPRESENTER_ID", result.representerID, is("リリリリリプレゼントID"));
		assertThat("APPROVAL_DATE", result.approvalDate, is(GeneralDate.ymd(2014, 11, 2)));
		assertThat("APPROVAL_REASON", result.approvalReason, is("リリリリリリリリ理由"));
		assertThat("ApprovalFrame_ListSize", result.listWwfdtAppApvApproverState.size(), is(data.getListApproverState().size()));
	}
	
	private ApprovalFrame create() {
		return new ApprovalFrame(
				"ルルルルルートID", 
				1192, 
				893, 
				ApprovalBehaviorAtr.UNAPPROVED, 
				ConfirmPerson.CONFIRM, 
				"アアアアプローバルID", 
				"リリリリリプレゼントID", 
				GeneralDate.ymd(2014, 11, 2), 
				"リリリリリリリリ理由",
				new ArrayList<>()
				);
	}

}

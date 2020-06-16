package nts.uk.ctx.workflow.infra.repository.approverstatemanagement;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import nts.arc.testing.assertion.NtsAssert;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalFrame;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalPhaseState;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalRootState;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApproverState;
import nts.uk.ctx.workflow.infra.entity.approverstatemanagement.application.FullJoinAppApvState;


public class JpaApprovalRootStateRepositoryTest {
	
	@Test
	public void toDomainRootTest() {
		String appId = "APPID";
		Integer i;
		List<FullJoinAppApvState> fullRootLst = new ArrayList<>();
		for(i = 0; i <= 10; i++) {
			fullRootLst.add(FullJoinAppApvState.builder()
				.companyID("0000000000-0765-1")
				.appID("APPID")
				.employeeID("EMPID")
				.appDate(GeneralDate.ymd(2020, 04, 01))
				.phaseOrder(1)
				.appPhaseAtr(1)
				.approvalForm(1)
				.frameOrder(1)
				.appFrameAtr(1)
				.confirmAtr(1)
				.approverID("APVID")
				.representerID("RPSID")
				.approvalDate(GeneralDate.ymd(2020, 05, 01))
				.approvalReason("気に入らないから")
				.approverChildID("APVID" + i.toString())
				.build()
			);
		}
		
		ApprovalRootState result = (ApprovalRootState)NtsAssert.Invoke.staticMethod(
				JpaApprovalRootStateRepository.class, 
				"toDomainRoot", 
				appId, fullRootLst);
		
		assertThat(result.getCompanyID(), is("0000000000-0765-1"));
		assertThat(result.getRootStateID(), is("APPID"));
		assertThat(result.getEmployeeID(), is("EMPID"));
		assertThat(result.getApprovalRecordDate(), is(GeneralDate.ymd(2020, 04, 01)));
		assertThat(result.getListApprovalPhaseState().get(0).getPhaseOrder(), is(1));
		assertThat(result.getListApprovalPhaseState().get(0).getApprovalAtr().value, is(1));
		assertThat(result.getListApprovalPhaseState().get(0).getApprovalForm().value, is(1));
		assertThat(result.getListApprovalPhaseState().get(0).getListApprovalFrame().get(0).getRootStateID(), is("APPID"));
		assertThat(result.getListApprovalPhaseState().get(0).getListApprovalFrame().get(0).getPhaseOrder(), is(1));
		assertThat(result.getListApprovalPhaseState().get(0).getListApprovalFrame().get(0).getFrameOrder(), is(1));
		assertThat(result.getListApprovalPhaseState().get(0).getListApprovalFrame().get(0).getApprovalAtr().value, is(1));
		assertThat(result.getListApprovalPhaseState().get(0).getListApprovalFrame().get(0).getConfirmAtr().value, is(1));
		assertThat(result.getListApprovalPhaseState().get(0).getListApprovalFrame().get(0).getApproverID(), is("APVID"));
		assertThat(result.getListApprovalPhaseState().get(0).getListApprovalFrame().get(0).getRepresenterID(), is("RPSID"));
		assertThat(result.getListApprovalPhaseState().get(0).getListApprovalFrame().get(0).getApprovalDate(), is(GeneralDate.ymd(2020, 05, 01)));
		assertThat(result.getListApprovalPhaseState().get(0).getListApprovalFrame().get(0).getApprovalReason(), is("気に入らないから"));
		assertThat(result.getListApprovalPhaseState().get(0).getListApprovalFrame().get(0).getListApproverState().get(0).getApproverID(), is("APVID0"));
	}
	
	@Test
	public void toDomainPhaseTest() {
		String appId = "APPID";
		Integer phaseOrder = 1;
		Integer i;
		List<FullJoinAppApvState> fullPhaseLst = new ArrayList<>();
		for(i = 0; i <= 10; i++) {
			fullPhaseLst.add(FullJoinAppApvState.builder()
				.companyID("0000000000-0765-1")
				.appID("APPID")
				.employeeID("EMPID")
				.appDate(GeneralDate.ymd(2020, 04, 01))
				.phaseOrder(1)
				.appPhaseAtr(1)
				.approvalForm(1)
				.frameOrder(1)
				.appFrameAtr(1)
				.confirmAtr(1)
				.approverID("APVID")
				.representerID("RPSID")
				.approvalDate(GeneralDate.ymd(2020, 05, 01))
				.approvalReason("気に入らないから")
				.approverChildID("APVID" + i.toString())
				.build()
			);
		}
		
		ApprovalPhaseState result = (ApprovalPhaseState)NtsAssert.Invoke.staticMethod(
				JpaApprovalRootStateRepository.class, 
				"toDomainPhase", 
				appId, phaseOrder, fullPhaseLst);
		
		assertThat(result.getRootStateID(), is("APPID"));
		assertThat(result.getPhaseOrder(), is(1));
		assertThat(result.getApprovalAtr().value, is(1));
		assertThat(result.getApprovalForm().value, is(1));
		assertThat(result.getListApprovalFrame().get(0).getRootStateID(), is("APPID"));
		assertThat(result.getListApprovalFrame().get(0).getPhaseOrder(), is(1));
		assertThat(result.getListApprovalFrame().get(0).getFrameOrder(), is(1));
		assertThat(result.getListApprovalFrame().get(0).getApprovalAtr().value, is(1));
		assertThat(result.getListApprovalFrame().get(0).getConfirmAtr().value, is(1));
		assertThat(result.getListApprovalFrame().get(0).getApproverID(), is("APVID"));
		assertThat(result.getListApprovalFrame().get(0).getRepresenterID(), is("RPSID"));
		assertThat(result.getListApprovalFrame().get(0).getApprovalDate(), is(GeneralDate.ymd(2020, 05, 01)));
		assertThat(result.getListApprovalFrame().get(0).getApprovalReason(), is("気に入らないから"));
		assertThat(result.getListApprovalFrame().get(0).getListApproverState().get(0).getApproverID(), is("APVID0"));
	}
	
	@Test
	public void toDomainFrameTest() {
		String appId = "APPID";
		Integer phaseOrder = 1;
		Integer frameOrder = 1;
		Integer i;
		List<FullJoinAppApvState> fullFrameLst = new ArrayList<>();
		for(i = 0; i <= 10; i++) {
			fullFrameLst.add(FullJoinAppApvState.builder()
				.companyID("0000000000-0765-1")
				.appID("APPID")
				.employeeID("EMPID")
				.appDate(GeneralDate.ymd(2020, 04, 01))
				.phaseOrder(1)
				.appPhaseAtr(1)
				.approvalForm(1)
				.frameOrder(1)
				.appFrameAtr(1)
				.confirmAtr(1)
				.approverID("APVID")
				.representerID("RPSID")
				.approvalDate(GeneralDate.ymd(2020, 05, 01))
				.approvalReason("気に入らないから")
				.approverChildID("APVID" + i.toString())
				.build()
			);
		}
		
		ApprovalFrame result = (ApprovalFrame)NtsAssert.Invoke.staticMethod(
				JpaApprovalRootStateRepository.class, 
				"toDomainFrame", 
				appId, phaseOrder, frameOrder, fullFrameLst);
		
		assertThat(result.getRootStateID(), is("APPID"));
		assertThat(result.getPhaseOrder(), is(1));
		assertThat(result.getFrameOrder(), is(1));
		assertThat(result.getApprovalAtr().value, is(1));
		assertThat(result.getConfirmAtr().value, is(1));
		assertThat(result.getApproverID(), is("APVID"));
		assertThat(result.getRepresenterID(), is("RPSID"));
		assertThat(result.getApprovalDate(), is(GeneralDate.ymd(2020, 05, 01)));
		assertThat(result.getApprovalReason(), is("気に入らないから"));
		assertThat(result.getListApproverState().get(0).getApproverID(), is("APVID0"));
	}
	
	@Test
	public void toDomainApproverTest() {
		String appId = "APPID";
		Integer phaseOrder = 1;
		Integer frameOrder = 1;
		Integer i;
		List<FullJoinAppApvState> fullFrameLst = new ArrayList<>();
		for(i = 0; i <= 10; i++) {
			fullFrameLst.add(FullJoinAppApvState.builder()
				.companyID("0000000000-0765-1")
				.appID("APPID")
				.employeeID("EMPID")
				.appDate(GeneralDate.ymd(2020, 04, 01))
				.phaseOrder(1)
				.appPhaseAtr(1)
				.approvalForm(1)
				.frameOrder(1)
				.appFrameAtr(1)
				.confirmAtr(1)
				.approverID("APVID")
				.representerID("RPSID")
				.approvalDate(GeneralDate.ymd(2020, 05, 01))
				.approvalReason("気に入らないから")
				.approverChildID("APVID" + i.toString())
				.build()
			);
		}
		
		ApproverState result = (ApproverState)NtsAssert.Invoke.staticMethod(
				JpaApprovalRootStateRepository.class, 
				"toDomainApprover", 
				appId, phaseOrder, frameOrder, fullFrameLst);
		
		assertThat(result.getRootStateID(), is("APPID"));
		assertThat(result.getPhaseOrder(), is(1));
		assertThat(result.getFrameOrder(), is(1));
		assertThat(result.getApproverID(), is("APVID"));
		assertThat(result.getCompanyID(), is("0000000000-0765-1"));
		assertThat(result.getDate(), is(GeneralDate.ymd(2020, 04, 01)));
	}

}

package nts.uk.ctx.workflow.infra.repository.resultrecord;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import nts.arc.testing.assertion.NtsAssert;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.resultrecord.AppFrameInstance;
import nts.uk.ctx.workflow.dom.resultrecord.AppPhaseInstance;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootInstance;
import nts.uk.ctx.workflow.infra.entity.resultrecord.FullJoinAppRootInstance;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public class JpaAppRootInstanceRepositoryTest {
	
	@Test
	public void toDomainRootTest() {
		String appId = "APPID";
		Integer i;
		List<FullJoinAppRootInstance> fullRootLst = new ArrayList<>();
		List<String> approvers = new ArrayList<>();
		for(i = 0; i <= 10; i++) {
			fullRootLst.add(FullJoinAppRootInstance.builder()
				.companyID("0000000000-0765-1")
				.rootID("ROOTID")
				.employeeID("EMPID")
				.startDate(GeneralDate.ymd(2010, 04, 01))
				.endDate(GeneralDate.ymd(2020, 03, 31))
				.rootType(1)
				.phaseOrder(1)
				.approvalForm(1)
				.frameOrder(1)
				.confirmAtr(1)
				.approverChildID("APVID" + i.toString())
				.build()
			);
			approvers.add("APVID" + i.toString());
		}
		
		AppRootInstance result = (AppRootInstance)NtsAssert.Invoke.staticMethod(
				JpaAppRootInstanceRepository.class, 
				"toDomainRoot", 
				appId, fullRootLst);
		assertThat(result.getCompanyID(), is("0000000000-0765-1"));
		assertThat(result.getRootID(), is("APPID"));
		assertThat(result.getEmployeeID(), is("EMPID"));
		assertThat(result.getDatePeriod(), is(new DatePeriod(GeneralDate.ymd(2010, 04, 01), GeneralDate.ymd(2020, 03, 31))));
		assertThat(result.getRootType().value, is(1));
		assertThat(result.getListAppPhase().get(0).getPhaseOrder(), is(1));
		assertThat(result.getListAppPhase().get(0).getApprovalForm().value, is(1));
		assertThat(result.getListAppPhase().get(0).getListAppFrame().get(0).getFrameOrder(), is(1));
		assertThat(result.getListAppPhase().get(0).getListAppFrame().get(0).isConfirmAtr(), is(true));
		assertThat(result.getListAppPhase().get(0).getListAppFrame().get(0).getListApprover(), is(approvers));
	}
	
	@Test
	public void toDomainPhaseTest() {
		String appId = "APPID";
		Integer phaseOrder = 1;
		Integer i;
		List<FullJoinAppRootInstance> fullPhaseLst = new ArrayList<>();
		List<String> approvers = new ArrayList<>();
		for(i = 0; i <= 10; i++) {
			fullPhaseLst.add(FullJoinAppRootInstance.builder()
				.companyID("0000000000-0765-1")
				.rootID("ROOTID")
				.employeeID("EMPID")
				.startDate(GeneralDate.ymd(2010, 04, 01))
				.endDate(GeneralDate.ymd(2020, 03, 31))
				.rootType(1)
				.phaseOrder(1)
				.approvalForm(1)
				.frameOrder(1)
				.confirmAtr(1)
				.approverChildID("APVID" + i.toString())
				.build()
			);
			approvers.add("APVID" + i.toString());
		}
		
		AppPhaseInstance result = (AppPhaseInstance)NtsAssert.Invoke.staticMethod(
				JpaAppRootInstanceRepository.class, 
				"toDomainPhase",
				appId, phaseOrder, fullPhaseLst);
		assertThat(result.getPhaseOrder(), is(1));
		assertThat(result.getApprovalForm().value, is(1));
		assertThat(result.getListAppFrame().get(0).getFrameOrder(), is(1));
		assertThat(result.getListAppFrame().get(0).isConfirmAtr(), is(true));
		assertThat(result.getListAppFrame().get(0).getListApprover(), is(approvers));
	}
	
	@Test
	public void toDomainFrameTest() {
		String appId = "APPID";
		Integer phaseOrder = 1;
		Integer frameOrder = 1;
		Integer i;
		List<FullJoinAppRootInstance> fullFrameLst = new ArrayList<>();
		List<String> approvers = new ArrayList<>();
		for(i = 0; i <= 10; i++) {
			fullFrameLst.add(FullJoinAppRootInstance.builder()
				.companyID("0000000000-0765-1")
				.rootID("ROOTID")
				.employeeID("EMPID")
				.startDate(GeneralDate.ymd(2010, 04, 01))
				.endDate(GeneralDate.ymd(2020, 03, 31))
				.rootType(1)
				.phaseOrder(1)
				.approvalForm(1)
				.frameOrder(1)
				.confirmAtr(1)
				.approverChildID("APVID" + i.toString())
				.build()
			);
			approvers.add("APVID" + i.toString());
		}
		
		AppFrameInstance result = (AppFrameInstance)NtsAssert.Invoke.staticMethod(
				JpaAppRootInstanceRepository.class, 
				"toDomainFrame", 
				appId, phaseOrder, frameOrder, fullFrameLst);
		assertThat(result.getFrameOrder(), is(1));
		assertThat(result.isConfirmAtr(), is(true));
		assertThat(result.getListApprover(), is(approvers));
	}

}

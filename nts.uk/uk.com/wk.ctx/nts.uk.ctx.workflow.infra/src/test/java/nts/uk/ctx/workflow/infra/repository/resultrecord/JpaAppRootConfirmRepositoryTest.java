package nts.uk.ctx.workflow.infra.repository.resultrecord;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import nts.arc.testing.assertion.NtsAssert;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootConfirm;
import nts.uk.ctx.workflow.infra.entity.resultrecord.FullJoinAppRootConfirm;

public class JpaAppRootConfirmRepositoryTest {
	
	@Test
	public void toDomainRootTestDaily() {
		String appId = "APPID";
		Integer i;
		List<FullJoinAppRootConfirm> fullRootLst = new ArrayList<>();
		List<String> approvers = new ArrayList<>();
		for(i = 0; i <= 10; i++) {
			fullRootLst.add(FullJoinAppRootConfirm.builder()
				.companyID("0000000000-0765-1")
				.rootID("ROOTID")
				.employeeID("EMPID")
				.recordDate(GeneralDate.ymd(2020, 04, 01))
				.rootType(1)
				.yearMonth(null)
				.closureID(null)
				.closureDay(null)
				.lastDayFlg(null)
				.phaseOrder(1)
				.appPhaseAtr(1)
				.frameOrder(1)
				.approverID("APVID")
				.representerID("RPSID")
				.approvalDate(GeneralDate.ymd(2020, 05, 01))
				.build()
			);
			approvers.add("APVID" + i.toString());
		}
		
		AppRootConfirm result = (AppRootConfirm)NtsAssert.Invoke.staticMethod(
				JpaAppRootConfirmRepository.class, 
				"toDomainRoot", 
				appId, fullRootLst);
		assertThat(result.getCompanyID(), is("0000000000-0765-1"));
		assertThat(result.getRootID(), is("ROOTID"));
		assertThat(result.getEmployeeID(), is("EMPID"));
		assertThat(result.getRecordDate(), is(GeneralDate.ymd(2020, 04, 01)));
		assertThat(result.getRootType().value, is(1));
		assertThat(result.getListAppPhase().get(0).getPhaseOrder(), is(1));
		assertThat(result.getListAppPhase().get(0).getAppPhaseAtr().value, is(1));
		assertThat(result.getListAppPhase().get(0).getListAppFrame().get(0).getFrameOrder(), is(1));
		assertThat(result.getListAppPhase().get(0).getListAppFrame().get(0).getApproverID().get(), is("APVID"));
		assertThat(result.getListAppPhase().get(0).getListAppFrame().get(0).getRepresenterID().get(), is("RPSID"));
		assertThat(result.getListAppPhase().get(0).getListAppFrame().get(0).getApprovalDate(), is(GeneralDate.ymd(2020, 05, 01)));
		assertThat(!result.getYearMonth().isPresent(), is(true));
		assertThat(!result.getClosureID().isPresent(), is(true));
		assertThat(!result.getClosureDate().isPresent(), is(true));
	}
	
	@Test
	public void toDomainRootTestMonthly() {
		String appId = "APPID";
		Integer i;
		List<FullJoinAppRootConfirm> fullRootLst = new ArrayList<>();
		List<String> approvers = new ArrayList<>();
		for(i = 0; i <= 10; i++) {
			fullRootLst.add(FullJoinAppRootConfirm.builder()
				.companyID("0000000000-0765-1")
				.rootID("ROOTID")
				.employeeID("EMPID")
				.recordDate(GeneralDate.ymd(2020, 04, 01))
				.rootType(2)
				.yearMonth(202004)
				.closureID(1)
				.closureDay(25)
				.lastDayFlg(0)
				.phaseOrder(1)
				.appPhaseAtr(1)
				.frameOrder(1)
				.approverID("APVID")
				.representerID("RPSID")
				.approvalDate(GeneralDate.ymd(2020, 05, 01))
				.build()
			);
			approvers.add("APVID" + i.toString());
		}
		
		AppRootConfirm result = (AppRootConfirm)NtsAssert.Invoke.staticMethod(
				JpaAppRootConfirmRepository.class, 
				"toDomainRoot", 
				appId, fullRootLst);
		assertThat(result.getCompanyID(), is("0000000000-0765-1"));
		assertThat(result.getRootID(), is("ROOTID"));
		assertThat(result.getEmployeeID(), is("EMPID"));
		assertThat(result.getRecordDate(), is(GeneralDate.ymd(2020, 04, 01)));
		assertThat(result.getRootType().value, is(2));
		assertThat(result.getListAppPhase().get(0).getPhaseOrder(), is(1));
		assertThat(result.getListAppPhase().get(0).getAppPhaseAtr().value, is(1));
		assertThat(result.getListAppPhase().get(0).getListAppFrame().get(0).getFrameOrder(), is(1));
		assertThat(result.getListAppPhase().get(0).getListAppFrame().get(0).getApproverID().get(), is("APVID"));
		assertThat(result.getListAppPhase().get(0).getListAppFrame().get(0).getRepresenterID().get(), is("RPSID"));
		assertThat(result.getListAppPhase().get(0).getListAppFrame().get(0).getApprovalDate(), is(GeneralDate.ymd(2020, 05, 01)));
		assertThat(result.getYearMonth().get().v(), is(202004));
		assertThat(result.getClosureID().get(), is(1));
		assertThat(result.getClosureDate().get().getClosureDay().v(), is(25));
		assertThat(result.getClosureDate().get().getLastDayOfMonth(), is(false));
	}
	

}

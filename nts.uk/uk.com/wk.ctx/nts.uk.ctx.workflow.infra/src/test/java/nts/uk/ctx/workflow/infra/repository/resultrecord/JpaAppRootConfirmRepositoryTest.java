package nts.uk.ctx.workflow.infra.repository.resultrecord;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import lombok.val;
import nts.arc.layer.infra.data.jdbc.JdbcProxy;
import nts.arc.testing.assertion.NtsAssert;
import nts.arc.testing.jdbc.JdbcProxyPeeker;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootConfirm;
import nts.uk.ctx.workflow.infra.entity.resultrecord.FullJoinAppRootConfirm;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

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
	
	@Test
	public void internalQueryDaily_roots() {
		
		val employeeIds = Arrays.asList("e1", "e2");
		val period = new DatePeriod(GeneralDate.ymd(2020, 4, 1), GeneralDate.ymd(2020, 4, 30));
		
		String actual = JdbcProxyPeeker.peekSql(() -> {
			val proxy = new JdbcProxy(null);
			val target = new JpaAppRootConfirmRepository.InternalQueryDaily(proxy, employeeIds, period);
			NtsAssert.Invoke.privateMethod(target, "roots");
		});
		
		String expected = "select ROOT_ID, CID, EMPLOYEE_ID, RECORD_DATE"
				+ " from WWFDT_DAY_APV_RT_CONFIRM"
				+ " where EMPLOYEE_ID in [e1, e2]"
				+ " and RECORD_DATE between 2020-04-01 and 2020-04-30";
		assertThat(actual, is(expected));
	}
	
	@Test
	public void internalQueryDaily_phases() {
		
		val employeeIds = Arrays.asList("e1", "e2");
		val period = new DatePeriod(GeneralDate.ymd(2020, 4, 1), GeneralDate.ymd(2020, 4, 30));
		
		String actual = JdbcProxyPeeker.peekSql(() -> {
			val proxy = new JdbcProxy(null);
			val target = new JpaAppRootConfirmRepository.InternalQueryDaily(proxy, employeeIds, period);
			NtsAssert.Invoke.privateMethod(target, "phases");
		});
		
		String expected = "select ROOT_ID, PHASE_ORDER, APP_PHASE_ATR"
				+ " from WWFDT_DAY_APV_PH_CONFIRM"
				+ " where EMPLOYEE_ID in [e1, e2]"
				+ " and RECORD_DATE between 2020-04-01 and 2020-04-30";
		assertThat(actual, is(expected));
	}
	
	@Test
	public void internalQueryDaily_frames() {
		
		val employeeIds = Arrays.asList("e1", "e2");
		val period = new DatePeriod(GeneralDate.ymd(2020, 4, 1), GeneralDate.ymd(2020, 4, 30));
		
		String actual = JdbcProxyPeeker.peekSql(() -> {
			val proxy = new JdbcProxy(null);
			val target = new JpaAppRootConfirmRepository.InternalQueryDaily(proxy, employeeIds, period);
			NtsAssert.Invoke.privateMethod(target, "frames");
		});
		
		String expected = "select ROOT_ID, PHASE_ORDER, FRAME_ORDER, APPROVER_ID, REPRESENTER_ID, APPROVAL_DATE"
				+ " from WWFDT_DAY_APV_FR_CONFIRM"
				+ " where EMPLOYEE_ID in [e1, e2]"
				+ " and RECORD_DATE between 2020-04-01 and 2020-04-30";
		assertThat(actual, is(expected));
	}
	
	@Test
	public void internalQueryDaily_fullJoin() {
		
		val rootsList = Arrays.asList(
				Arrays.asList("root1", "comp", "emp1", GeneralDate.ymd(2020, 4, 1)),
				Arrays.asList("root2", "comp", "emp1", GeneralDate.ymd(2020, 4, 2))
				);
		
		val phasesMap = new HashMap<String, List<List<Object>>>();
		phasesMap.put("root1", Arrays.asList(
				Arrays.asList("root1", 1, 1),
				Arrays.asList("root1", 2, 1)
				));
		
		val framesMap = new HashMap<String, List<List<Object>>>();
		framesMap.put("root1", Arrays.asList(
				Arrays.asList("root1", 1, 1, "approver1", null, GeneralDate.ymd(2020, 4, 15)),
				Arrays.asList("root1", 1, 2, null, "representer1", GeneralDate.ymd(2020, 4, 16))
				));
		
		@SuppressWarnings("unchecked")
		val actual = (List<FullJoinAppRootConfirm>) NtsAssert.Invoke.staticMethod(
				JpaAppRootConfirmRepository.InternalQueryDaily.class,
				"fullJoin",
				rootsList, phasesMap, framesMap);
		
		assertThat(actual.size(), is(4));

		val actual0 = actual.get(0);
		assertThat(actual0.getRootID(), is("root1"));
		assertThat(actual0.getPhaseOrder(), is(1));
		assertThat(actual0.getFrameOrder(), is(1));
		assertThat(actual0.getApproverID(), is("approver1"));
		assertThat(actual0.getRepresenterID(), is(CoreMatchers.nullValue()));
		
		val actual1 = actual.get(1);
		assertThat(actual1.getRootID(), is("root1"));
		assertThat(actual1.getPhaseOrder(), is(1));
		assertThat(actual1.getFrameOrder(), is(2));
		assertThat(actual1.getApproverID(), is(CoreMatchers.nullValue()));
		assertThat(actual1.getRepresenterID(), is("representer1"));

		val actual2 = actual.get(2);
		assertThat(actual2.getRootID(), is("root1"));
		assertThat(actual2.getPhaseOrder(), is(2));
		assertThat(actual2.getFrameOrder(), is(CoreMatchers.nullValue()));
		assertThat(actual2.getApproverID(), is(CoreMatchers.nullValue()));
		assertThat(actual2.getRepresenterID(), is(CoreMatchers.nullValue()));

		val actual3 = actual.get(3);
		assertThat(actual3.getRootID(), is("root2"));
		assertThat(actual3.getPhaseOrder(), is(CoreMatchers.nullValue()));
		assertThat(actual3.getFrameOrder(), is(CoreMatchers.nullValue()));
		assertThat(actual3.getApproverID(), is(CoreMatchers.nullValue()));
		assertThat(actual3.getRepresenterID(), is(CoreMatchers.nullValue()));
	}
}

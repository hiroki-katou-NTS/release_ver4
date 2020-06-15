package nts.uk.ctx.workflow.infra.entity.resultrecord.monthly.confirm;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.Test;

import nts.arc.testing.assertion.NtsAssert;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalBehaviorAtr;
import nts.uk.ctx.workflow.dom.resultrecord.AppFrameConfirm;
import nts.uk.ctx.workflow.dom.resultrecord.AppPhaseConfirm;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootConfirm;
import nts.uk.ctx.workflow.dom.resultrecord.RecordRootType;
import nts.uk.shr.com.time.calendar.date.ClosureDate;

public class WwfdtApvFrameConfirmMonthlyTest {

	@Test
	public void test() {
		AppRootConfirm rootData = createRoot();
		AppPhaseConfirm phaseData = createPhase();
		AppFrameConfirm frameData = createFrame();
		
		WwfdtApvFrameConfirmMonthly result = (WwfdtApvFrameConfirmMonthly)NtsAssert.Invoke.staticMethod(
				WwfdtApvFrameConfirmMonthly.class,
				"fromDomain",
				rootData,
				phaseData,
				frameData
		);
		assertThat("PK_ROOT_ID", result.getPk().getRootID(), is(rootData.getRootID()));
		assertThat("PK_PHASE_ORDER", result.getPk().getPhaseOrder(), is(phaseData.getPhaseOrder()));
		assertThat("PK_FRAME_ORDER", result.getPk().getFrameOrder(), is(frameData.getFrameOrder()));
		assertThat("CID", result.getCompanyID(), is(rootData.getCompanyID()));
		assertThat("EMPLOYEE_ID", result.getEmployeeID(), is(rootData.getEmployeeID()));
		assertThat("YEARMONTH", result.getYearMonth().intValue(), is(rootData.getYearMonth().get().v().intValue()));
		assertThat("CLOSURE_ID", result.getClosureID(), is(rootData.getClosureID().get()));
		assertThat("CLOSURE_DAY", result.getClosureDay().intValue(), is(rootData.getClosureDate().get().getClosureDay().v().intValue()));
		assertThat("LAST_DAY_FLG", result.getLastDayFlg(), is(rootData.getClosureDate().get().getLastDayOfMonth()==false?0:1));
		assertThat("APPROVER_ID", result.getApproverID(), is(frameData.getApproverID().get()));
		assertThat("REPRESENTER_ID", result.getRepresenterID(), is(frameData.getRepresenterID().get()));
		assertThat("APPROVAL_DATE", result.getApprovalDate(), is(frameData.getApprovalDate()));
	}

	private AppFrameConfirm createFrame() {
		return new AppFrameConfirm(
				500, 
				Optional.of("ロバID"), 
				Optional.of("ДID"), 
				GeneralDate.ymd(2222, 2, 26));
	}
	private AppPhaseConfirm createPhase() {
		return new AppPhaseConfirm(
				0, 
				ApprovalBehaviorAtr.ORIGINAL_REMAND, 
				new ArrayList<>());
	}

	private AppRootConfirm createRoot() {
		return new AppRootConfirm(
				"ルーティID", 
				"ケイシャID", 
				"エピピID", 
				GeneralDate.ymd(2007, 4, 1), 
				RecordRootType.CONFIRM_WORK_BY_MONTH, 
				new ArrayList<>(), 
				Optional.of(new YearMonth(201010)), 
				Optional.of(21), 
				Optional.of(new ClosureDate(22, Boolean.FALSE)));
	}
	
}

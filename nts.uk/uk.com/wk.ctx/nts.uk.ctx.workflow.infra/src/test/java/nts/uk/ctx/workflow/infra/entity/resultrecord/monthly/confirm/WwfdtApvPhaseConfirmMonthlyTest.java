package nts.uk.ctx.workflow.infra.entity.resultrecord.monthly.confirm;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;

import nts.arc.testing.assertion.NtsAssert;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalBehaviorAtr;
import nts.uk.ctx.workflow.dom.resultrecord.AppPhaseConfirm;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootConfirm;
import nts.uk.ctx.workflow.dom.resultrecord.RecordRootType;
import nts.uk.shr.com.time.calendar.date.ClosureDate;

public class WwfdtApvPhaseConfirmMonthlyTest {

	@Test
	public void test() {
		AppRootConfirm rootData = createRoot();
		AppPhaseConfirm phaseData = createPhase();
		
		WwfdtApvPhaseConfirmMonthly result = (WwfdtApvPhaseConfirmMonthly)NtsAssert.Invoke.staticMethod(
				WwfdtApvPhaseConfirmMonthly.class,
				"fromDomain",
				rootData,
				phaseData
		);
		
		assertThat("PK_ROOT_ID", result.getPk().getRootID(), is(rootData.getRootID()));
		assertThat("PK_PHASE_ORDER", result.getPk().getPhaseOrder(), is(phaseData.getPhaseOrder()));
		assertThat("CID", result.getCompanyID(), is(rootData.getCompanyID()));
		assertThat("EMPLOYEE_ID", result.getEmployeeID(), is(rootData.getEmployeeID()));
		assertThat("YEARMONTH", result.getYearMonth().intValue(), is(rootData.getYearMonth().get().v().intValue()));
		assertThat("CLOSURE_ID", result.getClosureID(), is(rootData.getClosureID().get()));
		assertThat("CLOSURE_DAY", result.getClosureDay().intValue(), is(rootData.getClosureDate().get().getClosureDay().v().intValue()));
		assertThat("LAST_DAY_FLG", result.getLastDayFlg(), is(rootData.getClosureDate().get().getLastDayOfMonth() == false ? 0:1));
		assertThat("APP_PHASE_ATR", result.getAppPhaseAtr(), is(phaseData.getAppPhaseAtr().value));
		assertThat("frameConfirmList", result.listWwfdtApvFrameConfirmMonthly.size(), is(phaseData.getListAppFrame().size()));
	}

	private AppPhaseConfirm createPhase() {
		return new AppPhaseConfirm(
				0, 
				ApprovalBehaviorAtr.DENIAL, 
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

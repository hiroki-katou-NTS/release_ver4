package nts.uk.ctx.workflow.infra.entity.resultrecord.daily.confirm;

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

public class WwfdtApvPhaseConfirmDailyTest {

	@Test
	public void test() {
		AppRootConfirm rootData = createRoot();
		AppPhaseConfirm phaseData = createPhase();
		
		WwfdtApvPhaseConfirmDaily result = (WwfdtApvPhaseConfirmDaily)NtsAssert.Invoke.staticMethod(
				WwfdtApvPhaseConfirmDaily.class,
				"fromDomain",
				rootData,
				phaseData
		);
		
		assertThat("PK_ROOT_ID", result.getPk().getRootID() , is(rootData.getRootID()));
		assertThat("PK_PAHSE_ORDER", result.getPk().getPhaseOrder() , is(phaseData.getPhaseOrder()));
		assertThat("CID", result.getCompanyID() , is(rootData.getCompanyID()));
		assertThat("EMPLOYEE_ID", result.getEmployeeID() , is(rootData.getEmployeeID()));
		assertThat("RECORD_DATE", result.getRecordDate() , is(rootData.getRecordDate()));
		assertThat("APP_PHASE_ATR", result.getAppPhaseAtr() , is(phaseData.getAppPhaseAtr().value));
	}

	private AppPhaseConfirm createPhase() {
		return new AppPhaseConfirm(
				0, 
				ApprovalBehaviorAtr.UNAPPROVED, 
				new ArrayList<>());
	}

	private AppRootConfirm createRoot() {
		return new AppRootConfirm(
				"ルーティID", 
				"ケイシャID", 
				"エピピID", 
				GeneralDate.ymd(2007, 4, 1), 
				RecordRootType.CONFIRM_WORK_BY_DAY, 
				new ArrayList<>(), 
				Optional.of(new YearMonth(201010)), 
				Optional.of(21), 
				Optional.of(new ClosureDate(22, Boolean.FALSE)));
	}

}

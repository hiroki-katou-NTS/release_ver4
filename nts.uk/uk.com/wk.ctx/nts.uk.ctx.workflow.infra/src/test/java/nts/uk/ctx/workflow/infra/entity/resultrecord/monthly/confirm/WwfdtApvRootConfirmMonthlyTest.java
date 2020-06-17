package nts.uk.ctx.workflow.infra.entity.resultrecord.monthly.confirm;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;

import nts.arc.testing.assertion.NtsAssert;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootConfirm;
import nts.uk.ctx.workflow.dom.resultrecord.RecordRootType;
import nts.uk.shr.com.time.calendar.date.ClosureDate;

public class WwfdtApvRootConfirmMonthlyTest {

	@Test
	public void test() {
		AppRootConfirm data = create();
		WwfdtApvRootConfirmMonthly result = (WwfdtApvRootConfirmMonthly)NtsAssert.Invoke.staticMethod(
				WwfdtApvRootConfirmMonthly.class,
				"fromDomain",
				data
		);
		assertThat("ROOT_ID", result.getRootID(), is(data.getRootID()));
		assertThat("CID", result.getCompanyID(), is(data.getCompanyID()));
		assertThat("EMPLOYEE_ID", result.getEmployeeID(), is(data.getEmployeeID()));
		assertThat("YEARMONTH", result.getYearMonth().intValue(), is(data.getYearMonth().get().v().intValue()));
		assertThat("CLOSURE_ID", result.getClosureID(), is(data.getClosureID().get()));
		assertThat("CLOSURE_DAY", result.getClosureDay(), is(data.getClosureDate().get().getClosureDay().v()));
		assertThat("LAST_DAY_FLG", result.getLastDayFlg(), is(data.getClosureDate().get().getLastDayOfMonth() == false?0:1));
	}

	
	private AppRootConfirm create() {
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

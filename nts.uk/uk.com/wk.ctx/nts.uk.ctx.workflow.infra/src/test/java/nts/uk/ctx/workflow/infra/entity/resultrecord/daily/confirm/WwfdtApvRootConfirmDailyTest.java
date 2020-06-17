package nts.uk.ctx.workflow.infra.entity.resultrecord.daily.confirm;

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

public class WwfdtApvRootConfirmDailyTest {

	@Test
	public void test() {
		AppRootConfirm data = create();
		WwfdtApvRootConfirmDaily result = (WwfdtApvRootConfirmDaily)NtsAssert.Invoke.staticMethod(
			WwfdtApvRootConfirmDaily.class,
			"fromDomain",
			data
		);
		
		assertThat("ROOT_ID", result.getRootID() , is(data.getRootID()));
		assertThat("CID", result.getCompanyID(), is(data.getCompanyID()));
		assertThat("EMPLOYEE_ID", result.getEmployeeID(), is(data.getEmployeeID()));
		assertThat("RECORD_DATE", result.getRecordDate(), is(data.getRecordDate()));
	}

	private AppRootConfirm create() {
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

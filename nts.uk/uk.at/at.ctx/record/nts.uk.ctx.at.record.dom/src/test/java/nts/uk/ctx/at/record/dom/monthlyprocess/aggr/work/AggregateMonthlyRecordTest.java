package nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import javax.ejb.Stateless;

import org.junit.Test;
import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.AggregateMonthlyRecord;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 月別実績を集計する　（テスト）
 * @author shuichi_ishida
 */
@Stateless
public class AggregateMonthlyRecordTest {

	private AggregateMonthlyRecord aggregateMonthlyRecord;
	
	/**
	 * アルゴリズム　（テスト）
	 */
	@Test
	public void aggregateTest() {
		// setup
		this.aggregateMonthlyRecord = new AggregateMonthlyRecord();
		String targetCmp = "TESTCMP";
		String targetEmp = "TESTEMP";
		val targetPeriod = new DatePeriod(GeneralDate.ymd(2017, 10, 1), GeneralDate.ymd(2017, 10, 31));
		
		/* 期間受け取りテスト　2017/10/31 */
		// exercise
		AggregateMonthlyRecordValue value = this.aggregateMonthlyRecord.aggregate(targetCmp, targetEmp, targetPeriod);
		String actual = "EMPTY";
		val actualList = value.getAttendanceTimes();
		if (!actualList.isEmpty()) {
			//actual = actualList.get(0).getDatePeriod().start().toString();
			actual = actualList.get(0).getDatePeriod().end().toString();
		}
		// verify
		assertThat(actual, is("2001/04/01"));

		/* 計算値受け取りテスト　2017/10/31
		// exercise
		target.algorithm();
		val actualList = target.getAttendanceTimes();
		int actual = 0;
		if (!actualList.isEmpty()) {
			actual = actualList.get(0).getMonthlyCalculation().getHolidayUseTime().getAnnualLeave().getAnnualLeaveUseTime().valueAsMinutes();
		}
		// verify
		assertThat(actual, is(100));
		*/
	}
}

package nts.uk.ctx.at.shared.dom.holidaymanagement.treatmentholiday;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import mockit.Injectable;
import nts.arc.testing.assertion.NtsAssert;
import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.shared.dom.common.days.FourWeekDays;
import nts.uk.ctx.at.shared.dom.common.days.WeeklyDays;
import nts.uk.shr.com.time.calendar.MonthDay;

public class HolidayAcqManageByMDTest {
	
	@Injectable
	private HolidayAcquisitionManagement.Require require;
	

	@Test
	public void testGetter() {
		HolidayAcqManageByMD holidayAcqManageByMD = new HolidayAcqManageByMD(new MonthDay(12, 31), new FourWeekDays(4.0), new WeeklyDays(1.0));
		NtsAssert.invokeGetters(holidayAcqManageByMD);
	}
	
	@Test
	public void test_getUnitManagementPeriod() {
		HolidayAcqManageByMD holidayAcqManageByMD = new HolidayAcqManageByMD(new MonthDay(12, 31), new FourWeekDays(4.0), new WeeklyDays(1.0));
		HolidayCheckUnit holidayCheckUnit = holidayAcqManageByMD.getUnitManagementPeriod();
		assertThat(holidayCheckUnit).isEqualTo(HolidayCheckUnit.FOUR_WEEK);
	}
	
	@Test
	public void test_make4Weeks() {
		HolidayAcqManageByMD holidayAcqManageByMD = new HolidayAcqManageByMD(new MonthDay(12, 31), new FourWeekDays(4.0), new WeeklyDays(1.0));
		DatePeriod datePeriod = holidayAcqManageByMD.make4Weeks(GeneralDate.ymd(2020, 11, 11), GeneralDate.ymd(2020, 11, 12));
		assertThat(datePeriod).isEqualTo(new DatePeriod(GeneralDate.ymd(2020, 11, 11), GeneralDate.ymd(2020, 12, 8)));
	}
	
	//if $対象月日 < @起算月日 
	// $対象月日.month == $起算月日 .month,$対象月日.day < $起算月日 .day,
	@Test
	public void test_getManagementPeriod_1() {
		HolidayAcqManageByMD holidayAcqManageByMD = new HolidayAcqManageByMD(new MonthDay(1, 15), new FourWeekDays(4.0), new WeeklyDays(1.0));
		HolidayAcqManaPeriod result = holidayAcqManageByMD.getManagementPeriod(require, GeneralDate.ymd(2020, 1, 1));
		
		assertThat(result.getHolidayDays().v()).isEqualTo(5.0);
		assertThat(result.getPeriod()).isEqualTo(new DatePeriod(GeneralDate.ymd(2019, 12, 17), GeneralDate.ymd(2020, 1, 14)));
	}
	
	//if $対象月日 < @起算月日 
	// $対象月日.month < $起算月日 .month
	@Test
	public void test_getManagementPeriod_1_1() {
		HolidayAcqManageByMD holidayAcqManageByMD = new HolidayAcqManageByMD(new MonthDay(1, 1), new FourWeekDays(4.0),
				new WeeklyDays(1.0));
		HolidayAcqManaPeriod result = holidayAcqManageByMD.getManagementPeriod(require, GeneralDate.ymd(2020, 2, 15));

		assertThat(result.getHolidayDays().v()).isEqualTo(4.0);
		assertThat(result.getPeriod())
				.isEqualTo(new DatePeriod(GeneralDate.ymd(2020, 1, 29), GeneralDate.ymd(2020, 2, 25)));
	}

	// if $対象月日 > @起算月日
	// $対象月日.month > $起算月日 .month
	@Test
	public void test_getManagementPeriod_1_2() {
		HolidayAcqManageByMD holidayAcqManageByMD = new HolidayAcqManageByMD(new MonthDay(2, 1), new FourWeekDays(4.0),
				new WeeklyDays(1.0));
		HolidayAcqManaPeriod result = holidayAcqManageByMD.getManagementPeriod(require, GeneralDate.ymd(2020, 1, 15));

		assertThat(result.getHolidayDays().v()).isEqualTo(5.0);
		assertThat(result.getPeriod())
				.isEqualTo(new DatePeriod(GeneralDate.ymd(2020, 1, 3), GeneralDate.ymd(2020, 1, 31)));
	}
		
	//if $対象月日 = @起算月日
	//$対象月日.month == $起算月日 .month,$対象月日.day == $起算月日 .day,

	@Test
	public void test_getManagementPeriod_2() {
		HolidayAcqManageByMD holidayAcqManageByMD = new HolidayAcqManageByMD(new MonthDay(1, 1), new FourWeekDays(4.0), new WeeklyDays(1.0));
		HolidayAcqManaPeriod result = holidayAcqManageByMD.getManagementPeriod(require, GeneralDate.ymd(2020, 1, 1));
		
		assertThat(result.getHolidayDays().v()).isEqualTo(4.0);
		assertThat(result.getPeriod()).isEqualTo(new DatePeriod(GeneralDate.ymd(2020, 1, 1), GeneralDate.ymd(2020, 1, 28)));
	}
	//if $週間数 > 13 
	@Test
	public void test_getManagementPeriod_3() {
		HolidayAcqManageByMD holidayAcqManageByMD = new HolidayAcqManageByMD(new MonthDay(1, 1), new FourWeekDays(28.0), new WeeklyDays(7.0));
		HolidayAcqManaPeriod result = holidayAcqManageByMD.getManagementPeriod(require, GeneralDate.ymd(2020, 12, 1));
		
		assertThat(result.getHolidayDays().v()).isEqualTo(28.0);
		assertThat(result.getPeriod()).isEqualTo(new DatePeriod(GeneralDate.ymd(2020, 12, 02), GeneralDate.ymd(2020, 12, 31)));
	}
	
	@Test
	public void test_getStartDateType() {
		HolidayAcqManageByMD holidayAcqManageByMD = new HolidayAcqManageByMD(new MonthDay(1, 1), new FourWeekDays(4.0), new WeeklyDays(1.0));
		StartDateClassification result = holidayAcqManageByMD.getStartDateType();
		
		assertThat(result).isEqualTo(StartDateClassification.SPECIFY_MD);
	}

}
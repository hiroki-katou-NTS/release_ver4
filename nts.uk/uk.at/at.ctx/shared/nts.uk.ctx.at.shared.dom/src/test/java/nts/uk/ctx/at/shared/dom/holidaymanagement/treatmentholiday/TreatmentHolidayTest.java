package nts.uk.ctx.at.shared.dom.holidaymanagement.treatmentholiday;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;

import lombok.val;
import mockit.Expectations;
import mockit.Injectable;
import mockit.integration.junit4.JMockit;
import nts.arc.testing.assertion.NtsAssert;
import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.DayOfWeek;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.shared.dom.common.days.FourWeekDays;
import nts.uk.ctx.at.shared.dom.common.days.WeeklyDays;
import nts.uk.ctx.at.shared.dom.workrule.weekmanage.WeekRuleManagement;
import nts.uk.shr.com.enumcommon.NotUseAtr;
import nts.uk.shr.com.time.calendar.MonthDay;

@RunWith(JMockit.class)
public class TreatmentHolidayTest {
	
	@Injectable
	private TreatmentHoliday.Require require;
	
	@Injectable
	private HolidayAcquisitionManagement.Require holidayRequire;

	@Test
	public void testGetter() {
		WeeklyHolidayAcqMana weeklyHolidayAcqMana = new WeeklyHolidayAcqMana(new WeeklyDays(1.0));
		TreatmentHoliday treatmentHoliday = new TreatmentHoliday("companyId", NotUseAtr.NOT_USE, weeklyHolidayAcqMana);
		NtsAssert.invokeGetters(treatmentHoliday);
	}
	
	/**
	 * if 休日取得管理  == 1週間単位の休日取得管理 
	 */
	@Test
	public void test_getNumberHoliday_1() {
		WeeklyHolidayAcqMana weeklyHolidayAcqMana = new WeeklyHolidayAcqMana(new WeeklyDays(1.0));
		TreatmentHoliday treatmentHoliday = new TreatmentHoliday("companyId", NotUseAtr.NOT_USE, weeklyHolidayAcqMana);
		
		WeekRuleManagement weekRuleManagement = WeekRuleManagement.of("companyId", DayOfWeek.MONDAY,true);
		new Expectations() {{
			require.find();
			result = weekRuleManagement;
		}};
		
		HolidayNumberManagement result = treatmentHoliday.getNumberHoliday(require, GeneralDate.ymd(2020, 11, 25));
		assertThat( result.getAddNonstatutoryHolidays()).isEqualTo(treatmentHoliday.getAddNonstatutoryHolidays());
		assertThat( result.getPeriod()).isEqualTo(new DatePeriod(GeneralDate.ymd(2020, 11, 23), GeneralDate.ymd(2020, 11, 29))); 
		assertThat( result.getHolidayDays().v()).isEqualTo(weeklyHolidayAcqMana.getWeeklyDays().v());
	}
	/**
	 * if 休日取得管理  == 月日起算の休日取得管理
	 */
	@Test
	public void test_getNumberHoliday_2() {
		HolidayAcqManageByMD holidayAcqManageByMD = new HolidayAcqManageByMD(new MonthDay(12, 31), new FourWeekDays(4.0), new WeeklyDays(1.0));
		TreatmentHoliday treatmentHoliday = new TreatmentHoliday("companyId", NotUseAtr.NOT_USE, holidayAcqManageByMD);
		
		HolidayNumberManagement result = treatmentHoliday.getNumberHoliday(require, GeneralDate.ymd(2020, 11, 11));
		assertThat( result.getAddNonstatutoryHolidays()).isEqualTo(treatmentHoliday.getAddNonstatutoryHolidays());
		assertThat( result.getPeriod()).isEqualTo(new DatePeriod(GeneralDate.ymd(2020, 11, 03), GeneralDate.ymd(2020, 11, 30))); 
		assertThat( result.getHolidayDays().v()).isEqualTo(4.0);
	}
	
	/**
	 * if 休日取得管理  == 年月日起算の休日取得管理
	 */
	@Test
	public void test_getNumberHoliday_3() {
		HolidayAcqManageByYMD holidayAcqManageByYMD = new HolidayAcqManageByYMD(GeneralDate.ymd(2020, 12, 1), new FourWeekDays(4.0));
		TreatmentHoliday treatmentHoliday = new TreatmentHoliday("companyId", NotUseAtr.USE, holidayAcqManageByYMD);
		
		HolidayNumberManagement result = treatmentHoliday.getNumberHoliday(require, GeneralDate.ymd(2020, 12, 31));
		assertThat( result.getAddNonstatutoryHolidays()).isEqualTo(treatmentHoliday.getAddNonstatutoryHolidays());
		assertThat( result.getPeriod()).isEqualTo(new DatePeriod(GeneralDate.ymd(2020, 12, 29), GeneralDate.ymd(2021, 1, 25))); 
		assertThat( result.getHolidayDays()).isEqualTo(holidayAcqManageByYMD.getFourWeekHoliday());
	}
	
	/**
	 * if 休日取得管理  == 1週間単位の休日取得管理 
	 */
	@Test
	public void test_get28Days_1week() {
		val weeklyHolidayAcqMana = new WeeklyHolidayAcqMana(new WeeklyDays(1.0));
		val treatmentHoliday = new TreatmentHoliday("companyId", NotUseAtr.NOT_USE, weeklyHolidayAcqMana);
		val weekRuleMana = WeekRuleManagement.of("companyId", DayOfWeek.MONDAY,true);
		
		new Expectations() {{
			require.find();
			result = weekRuleMana;
		}};
		
		val result = treatmentHoliday.get28Days(require, GeneralDate.ymd(2021, 02, 10));
		assertThat( result.start()).isEqualTo(GeneralDate.ymd(2021, 02, 8));
		assertThat( result.end()).isEqualTo(GeneralDate.ymd(2021, 03, 7)); 
	}
	
	/**
	 * if 休日取得管理  == 月日起算の休日取得管理
	 */
	@Test
	public void test_get28Days_monthDay() {
		val holidayAcqManageByMD = new HolidayAcqManageByMD(new MonthDay(12, 31), new FourWeekDays(4.0), new WeeklyDays(1.0));
		val treatmentHoliday = new TreatmentHoliday("companyId", NotUseAtr.NOT_USE, holidayAcqManageByMD);
		
		val result = treatmentHoliday.get28Days(require, GeneralDate.ymd(2021, 02, 10));
		assertThat( result.start()).isEqualTo(GeneralDate.ymd(2021, 1, 28));
		assertThat( result.end()).isEqualTo(GeneralDate.ymd(2021, 02, 24)); 
	}
	
	/**
	 * if 休日取得管理  == 年月日起算の休日取得管理
	 */
	@Test
	public void test_get28Days_ymd() {
		HolidayAcqManageByYMD holidayAcqManageByYMD = new HolidayAcqManageByYMD(GeneralDate.ymd(2021, 1, 1), new FourWeekDays(4.0));
		TreatmentHoliday treatmentHoliday = new TreatmentHoliday("companyId", NotUseAtr.USE, holidayAcqManageByYMD);
		
		val result = treatmentHoliday.get28Days(require, GeneralDate.ymd(2021, 02, 10));
		assertThat( result.start()).isEqualTo(GeneralDate.ymd(2021, 1, 29));
		assertThat( result.end()).isEqualTo(GeneralDate.ymd(2021, 02, 25)); 
	}
}

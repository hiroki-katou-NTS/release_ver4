package nts.uk.ctx.at.aggregation.dom.schedulecounter.aggregationprocess.personcounter;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;

import lombok.val;
import mockit.Expectations;
import mockit.Injectable;
import mockit.integration.junit4.JMockit;
import nts.arc.testing.assertion.NtsAssert;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.WorkInformation;
import nts.uk.ctx.at.shared.dom.common.EmployeeId;
import nts.uk.ctx.at.shared.dom.holidaymanagement.publicholiday.configuration.DayOfWeek;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.affiliationinfor.AffiliationInforOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.breakouting.breaking.BreakTimeOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.calcategory.CalAttrOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.dailyattendancework.IntegrationOfDaily;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.workinfomation.CalculationState;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.workinfomation.NotUseAttribute;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.workinfomation.WorkInfoOfDailyAttendance;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktype.DailyWork;
import nts.uk.ctx.at.shared.dom.worktype.HalfDayWorkTypeClassification;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeAbbreviationName;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeClassification;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeMemo;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeName;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeSymbolicName;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeUnit;

/**
 * UT?????????
 * ?????????????????????????????????????????????????????????
 * @author lan_lt
 */
@RunWith(JMockit.class)
public class WorkdayHolidayCounterServiceTest {

	@Injectable
	WorkdayHolidayCounterService.Require require;

	/**
	 * input:	???????????????= ONEDAY
	 * 			??????????????????????????????????????????????????? = WORKING
	 * output:	WORKING = 1
	 * 			HOLIDAY = 0
	 */
	@Test
	public void getValueOfTargetAggregation_ONEDAY_WORKING(
				@Injectable WorkInformation workInfo
			,	@Injectable WorkType workType
			,	@Injectable DailyWork dailyWork) {

		val targetCounts = new ArrayList<>(Arrays.asList(WorkClassificationAsAggregationTarget.values()));

		new Expectations() {
			{
				require.getWorkType((WorkTypeCode) any);
				result = Optional.of(workType);

				dailyWork.getHalfDayWorkTypeClassification();
				result = HalfDayWorkTypeClassification.createByWholeDay(WorkTypeClassification.Attendance);
			}
		};

		Map<WorkClassificationAsAggregationTarget, BigDecimal>  result = NtsAssert.Invoke.staticMethod(WorkdayHolidayCounterService.class, "getValueOfTargetAggregation", require, targetCounts, workInfo);

		//HOLIDAY
		{
			val numberHoliday = result.get(WorkClassificationAsAggregationTarget.HOLIDAY);
			assertThat(numberHoliday).isEqualByComparingTo(BigDecimal.ZERO);

		}

		//WORKING
		{
			val numberHoliday = result.get(WorkClassificationAsAggregationTarget.WORKING);
			assertThat(numberHoliday).isEqualByComparingTo(BigDecimal.valueOf(1));

		}
	}

	/**
	 * input:	???????????????= ONEDAY
	 * 			??????????????????????????????????????????????????? = HOLIDAY
	 * output:	WORKING = 0
	 * 			HOLIDAY = 1
	 */
	@Test
	public void getValueOfTargetAggregation_ONEDAY_HOLIDAY(
				@Injectable WorkInformation workInfo
			,	@Injectable WorkType workType
			,	@Injectable DailyWork dailyWork) {

		val targetCounts = new ArrayList<>(Arrays.asList(WorkClassificationAsAggregationTarget.values()));

		new Expectations() {
			{
				require.getWorkType((WorkTypeCode) any);
				result = Optional.of(workType);

				dailyWork.getHalfDayWorkTypeClassification();
				result = HalfDayWorkTypeClassification.createByWholeDay(WorkTypeClassification.Holiday);
			}
		};

		Map<WorkClassificationAsAggregationTarget, BigDecimal>  result = NtsAssert.Invoke.staticMethod(WorkdayHolidayCounterService.class, "getValueOfTargetAggregation", require, targetCounts, workInfo);

		//HOLIDAY
		{
			val numberHoliday = result.get(WorkClassificationAsAggregationTarget.HOLIDAY);
			assertThat(numberHoliday).isEqualByComparingTo(BigDecimal.valueOf(1));

		}

		//WORKING
		{
			val numberHoliday = result.get(WorkClassificationAsAggregationTarget.WORKING);
			assertThat(numberHoliday).isEqualByComparingTo(BigDecimal.ZERO);

		}
	}

	/**
	 * input:	???????????????= HALFDAY
	 * 			??????????????????????????????????????????????????? : ??????= WORKING, ?????? = HOLIDAY
	 * output:	WORKING = 	0.5
	 * 			HOLIDAY =	0.5
	 */
	@Test
	public void getValueOfTargetAggregation_HALFDAY(
				@Injectable WorkInformation workInfo
			,	@Injectable WorkType workType
			,	@Injectable DailyWork dailyWork) {

		val targetCounts = new ArrayList<>(Arrays.asList(WorkClassificationAsAggregationTarget.values()));

		new Expectations() {
			{
				require.getWorkType((WorkTypeCode) any);
				result = Optional.of(workType);

				dailyWork.getHalfDayWorkTypeClassification();
				result = HalfDayWorkTypeClassification.createByAmAndPm(WorkTypeClassification.Attendance, WorkTypeClassification.Holiday);
			}
		};

		Map<WorkClassificationAsAggregationTarget, BigDecimal>  result = NtsAssert.Invoke.staticMethod(WorkdayHolidayCounterService.class, "getValueOfTargetAggregation", require, targetCounts, workInfo);

		//HOLIDAY
		{
			val numberHoliday = result.get(WorkClassificationAsAggregationTarget.HOLIDAY);
			assertThat(numberHoliday).isEqualByComparingTo(BigDecimal.valueOf(0.5));

		}

		//WORKING
		{
			val numberHoliday = result.get(WorkClassificationAsAggregationTarget.WORKING);
			assertThat(numberHoliday).isEqualByComparingTo(BigDecimal.valueOf(0.5));

		}
	}

	/**
	 * method??????????????????
	 * ?????????[input]??????????????????????????????ID(distinct)  = [output].keys, ??????????????????????????????
	 */
	@Test
	public void test_Count() {
		//?????????????????????????????????????????????
		WorkTimeCode wkTimeCd = new WorkTimeCode("999");

		//????????????????????????????????????
		WorkTypeCode attWktpCd = new WorkTypeCode("001");
		//????????????????????????????????????
		WorkTypeCode holidayWktpCd = new WorkTypeCode("002");
		//?????? ??????????????????????????????????????????
		WorkTypeCode amWktpCd = new WorkTypeCode("003");
		//???????????????????????????????????????????????????
		WorkTypeCode pmWktpCd = new WorkTypeCode("004");

		//????????????????????????????????????
		WorkType attWkType = Helper.createOneDayByWorkTypeCode(attWktpCd, WorkTypeClassification.Attendance);
		//????????????????????????????????????
		WorkType holidayWkType = Helper.createOneDayByWorkTypeCode(holidayWktpCd, WorkTypeClassification.Holiday);
		//?????? ?????????????????????????????????
		WorkType amWkType = Helper.createHalfDayByWorkTypeCode(amWktpCd, WorkTypeClassification.Attendance, WorkTypeClassification.Holiday);
		//??????????????????????????????????????????
		WorkType pmWktp = Helper.createHalfDayByWorkTypeCode(pmWktpCd, WorkTypeClassification.Holiday, WorkTypeClassification.Attendance);
		val dailyWorks = Arrays.asList(
					Helper.createDailyWorks("sid1",		new  WorkInformation(attWktpCd, wkTimeCd))		//????????????
				,	Helper.createDailyWorks("sid1",		new  WorkInformation(holidayWktpCd, wkTimeCd))	//????????????
				,	Helper.createDailyWorks("sid2",		new  WorkInformation(attWktpCd, wkTimeCd))		//????????????
				,	Helper.createDailyWorks("sid2",		new  WorkInformation(amWktpCd, wkTimeCd))		//???????????????????????????
				,	Helper.createDailyWorks("sid3",		new  WorkInformation(holidayWktpCd, wkTimeCd))	//????????????
				,	Helper.createDailyWorks("sid3",		new  WorkInformation(pmWktpCd, wkTimeCd))		//???????????????????????????
				,	Helper.createDailyWorks("sid4",		new  WorkInformation(amWktpCd, wkTimeCd))		//???????????????????????????
				,	Helper.createDailyWorks("sid4",		new  WorkInformation(pmWktpCd, wkTimeCd)));		//???????????????????????????

		new Expectations() {
			{
				require.getWorkType(attWktpCd);
				result = Optional.of(attWkType);

				require.getWorkType(holidayWktpCd);
				result = Optional.of(holidayWkType);

				require.getWorkType(amWktpCd);
				result = Optional.of(amWkType);

				require.getWorkType(pmWktpCd);
				result = Optional.of(pmWktp);
			}
		};

		Map<EmployeeId, Map<WorkClassificationAsAggregationTarget, BigDecimal>> results = WorkdayHolidayCounterService.count(require, dailyWorks);

		assertThat(results.keySet()).containsOnlyElementsOf(Arrays.asList(
					new EmployeeId("sid1")
				, 	new EmployeeId("sid2")
				, 	new EmployeeId("sid3")
				, 	new EmployeeId("sid4")));

		{
			val actual = results.get(new EmployeeId("sid1"));
			//1????????? + 1?????????
			assertThat(actual.get(WorkClassificationAsAggregationTarget.WORKING)).isEqualTo(BigDecimal.valueOf(1.0));
			assertThat(actual.get(WorkClassificationAsAggregationTarget.HOLIDAY)).isEqualTo(BigDecimal.valueOf(1.0));
		}

		{
			val actual = results.get(new EmployeeId("sid2"));
			//???????????? + ????????????
			assertThat(actual.get(WorkClassificationAsAggregationTarget.WORKING)).isEqualTo(BigDecimal.valueOf(1.5));
			assertThat(actual.get(WorkClassificationAsAggregationTarget.HOLIDAY)).isEqualTo(BigDecimal.valueOf(0.5));
		}

		{
			val actual = results.get(new EmployeeId("sid3"));
			//???????????? + ????????????
			assertThat(actual.get(WorkClassificationAsAggregationTarget.WORKING)).isEqualTo(BigDecimal.valueOf(0.5));
			assertThat(actual.get(WorkClassificationAsAggregationTarget.HOLIDAY)).isEqualTo(BigDecimal.valueOf(1.5));
		}

		{
			val actual = results.get(new EmployeeId("sid4"));
			//???????????? + ????????????
			assertThat(actual.get(WorkClassificationAsAggregationTarget.WORKING)).isEqualTo(BigDecimal.valueOf(1.0));
			assertThat(actual.get(WorkClassificationAsAggregationTarget.HOLIDAY)).isEqualTo(BigDecimal.valueOf(1.0));
		}

	}

	public static class Helper{
		@Injectable
		private static AffiliationInforOfDailyAttd affiliationInfor;
		/**
		 * ????????????(Work)	?????????
		 * @param sid ??????ID
		 * @param workInformation ????????????
		 * @return
		 */
		public static IntegrationOfDaily createDailyWorks(String sid, WorkInformation workInfo ) {
			val workInfoDailyAtt =  new WorkInfoOfDailyAttendance(workInfo
					,	CalculationState.No_Calculated
					,	NotUseAttribute.Use
					,	NotUseAttribute.Use
					,	DayOfWeek.MONDAY
					,	Collections.emptyList()
					,	Optional.empty()
				);


			return new IntegrationOfDaily(
					sid, GeneralDate.ymd(2021, 4, 1), workInfoDailyAtt
					, CalAttrOfDailyAttd.defaultData()
					, affiliationInfor
					, Optional.empty()
					, Collections.emptyList()
					, Optional.empty()
					, new BreakTimeOfDailyAttd(Collections.emptyList())
					, Optional.empty()
					, Optional.empty()
					, Optional.empty()
					, Optional.empty()
					, Optional.empty()
					, Optional.empty()
					, Collections.emptyList()
					, Optional.empty()
					, Collections.emptyList()
					, Collections.emptyList()
					, Collections.emptyList()
					, Optional.empty());
		}

		/**
		 * ????????????????????????????????????
		 * @param wkTypeCode ?????????????????????
		 * @param workTypeCls ????????????
		 * @return
		 */
		public static WorkType createOneDayByWorkTypeCode(WorkTypeCode wkTypeCode, WorkTypeClassification workTypeCls) {
			return new WorkType(
						wkTypeCode
					,	new WorkTypeSymbolicName("symbolicName")
					,	new WorkTypeName("workTypeName")
					,	new WorkTypeAbbreviationName("abbName")
					,	new WorkTypeMemo("memo")
					,	new DailyWork(WorkTypeUnit.OneDay, workTypeCls
							,	WorkTypeClassification.Holiday
							,	WorkTypeClassification.Holiday));
		}
		/**
		 * ?????????????????????????????????????????????
		 * @param wkTypeCode ?????????????????????
		 * @param morning ??????
		 * @param afternoon ??????
		 * @return
		 */
		public static WorkType createHalfDayByWorkTypeCode(WorkTypeCode wkTypeCode
				,	WorkTypeClassification morning
				,	WorkTypeClassification afternoon) {
			return new WorkType(
						wkTypeCode
					,	new WorkTypeSymbolicName("symbolicName")
					,	new WorkTypeName("workTypeName")
					,	new WorkTypeAbbreviationName("abbName")
					,	new WorkTypeMemo("memo")
					,	new DailyWork(WorkTypeUnit.MonringAndAfternoon, WorkTypeClassification.Absence
							,	morning
							,	afternoon));
		}
	}
}

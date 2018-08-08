package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import nts.arc.layer.dom.AggregateRoot;
import nts.gul.util.value.Finally;
import nts.uk.ctx.at.record.dom.daily.TimevacationUseTimeOfDaily;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.withinstatutory.WithinWorkTimeFrame;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.withinstatutory.WithinWorkTimeSheet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.HolidayAddtionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.HourlyPaymentAdditionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.WorkDeformedLaborAdditionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.WorkFlexAdditionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.WorkRegularAdditionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.kmk013_splitdomain.HolidayCalcMethodSet;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.statutory.worktime.sharedNew.DailyUnit;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.overtime.overtimeframe.OverTimeFrameNo;
import nts.uk.ctx.at.shared.dom.workrule.statutoryworktime.DailyCalculationPersonalInformation;
import nts.uk.ctx.at.shared.dom.worktime.common.EmTimeFrameNo;
import nts.uk.ctx.at.shared.dom.worktime.common.TimeZoneRounding;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneOtherSubHolTimeSet;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixRestTimezoneSet;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixedWorkCalcSetting;
import nts.uk.ctx.at.shared.dom.worktime.flexset.CoreTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.flexset.FlexCalcSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 時間帯作成、時間計算で再取得が必要になっているクラスたちの管理クラス
 * @author keisuke_hoshina
 *
 */
@Getter
public class ManageReGetClass {
	
	//1日の範囲
	private CalculationRangeOfOneDay calculationRangeOfOneDay;
	
	//日別実績(WORK)
	@Setter
	private IntegrationOfDaily integrationOfDaily;
	
	//就業時間帯設定
	private Optional<WorkTimeSetting> workTimeSetting;
	
	//勤務種類
	private Optional<WorkType> workType;
	
	//就業時間帯別代休時間設定
	private List<WorkTimezoneOtherSubHolTimeSet> subHolTransferSetList;
	
	//労働制
	private DailyCalculationPersonalInformation personalInfo;
	
	//法定労働時間(日単位)
	private DailyUnit dailyUnit;
	
	//大塚用　固定勤務の休憩時間帯保持
	private Optional<FixRestTimezoneSet> fixRestTimeSetting;
	
	//大塚要ケインで使用する固定計算設定クラス
	private Optional<FixedWorkCalcSetting> ootsukaFixedWorkSet;
	
	//休暇の計算方法の設定
	private HolidayCalcMethodSet holidayCalcMethodSet;
	
	//計算処理に入ることができるかフラグ
	//(造語)
	private Boolean calculatable;
	
	//休憩回数
	int breakCount;
	
	//フレックス勤務設定
	private Optional<CoreTimeSetting> coreTimeSetting;

	//各種加算設定
	private WorkRegularAdditionSet workRegularAdditionSet;
	private WorkFlexAdditionSet workFlexAdditionSet;
	private HourlyPaymentAdditionSet hourlyPaymentAdditionSet;
	private WorkDeformedLaborAdditionSet workDeformedLaborAdditionSet;
	private Optional<HolidayAddtionSet> holidayAddtionSet;
	
	//就業時間帯の共通設定
	private Optional<nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneCommonSet> WorkTimezoneCommonSet;

	//法て内残業枠ＮＯリスト
	private List<OverTimeFrameNo> statutoryFrameNoList;	
	
	//フレックス計算設定
	private Optional<FlexCalcSetting> flexCalcSetting;
	
	
	
//	/**
//	 * Constructor 
//	 */
//	private ManageReGetClass(CalculationRangeOfOneDay calculationRangeOfOneDay, IntegrationOfDaily integrationOfDaily,
//			Optional<WorkTimeSetting> workTimeSetting, Optional<WorkType> workType,
//			List<WorkTimezoneOtherSubHolTimeSet> subHolTransferSetList,
//			DailyCalculationPersonalInformation personalInfo, DailyUnit dailyUnit,
//			Optional<FixRestTimezoneSet> fixRestTimeSeting,
//			Optional<FixedWorkCalcSetting> ootsukaFixedWorkSet,
//			Boolean calculatable,
//			int breakCount,
//			Optional<CoreTimeSetting> coreTimeSetting,
//			Optional<WorkTimezoneCommonSet> WorkTimezoneCommonSet,
//			List<OverTimeFrameNo> statutoryFrameNoList) {
//		super();
//		this.calculationRangeOfOneDay = calculationRangeOfOneDay;
//		this.integrationOfDaily = integrationOfDaily;
//		this.workTimeSetting = workTimeSetting;
//		this.workType = workType;
//		this.subHolTransferSetList = subHolTransferSetList;
//		this.personalInfo = personalInfo;
//		this.fixRestTimeSetting = fixRestTimeSeting;
//		this.dailyUnit = dailyUnit;
//		this.ootsukaFixedWorkSet = ootsukaFixedWorkSet;
//		this.calculatable = calculatable;
//		this.breakCount = breakCount;
//		this.coreTimeSetting = coreTimeSetting;
//		this.WorkTimezoneCommonSet = WorkTimezoneCommonSet;
//		this.statutoryFrameNoList = statutoryFrameNoList;
//	}

	public ManageReGetClass(CalculationRangeOfOneDay calculationRangeOfOneDay, IntegrationOfDaily integrationOfDaily,
			Optional<WorkTimeSetting> workTimeSetting, Optional<WorkType> workType,
			List<WorkTimezoneOtherSubHolTimeSet> subHolTransferSetList,
			DailyCalculationPersonalInformation personalInfo, DailyUnit dailyUnit,
			Optional<FixRestTimezoneSet> fixRestTimeSetting, 
			Optional<FixedWorkCalcSetting> ootsukaFixedWorkSet,
			HolidayCalcMethodSet holidayCalcMethodSet, 
			Boolean calculatable, 
			int breakCount,
			Optional<CoreTimeSetting> coreTimeSetting, 
			WorkRegularAdditionSet workRegularAdditionSet,
			WorkFlexAdditionSet workFlexAdditionSet, 
			HourlyPaymentAdditionSet hourlyPaymentAdditionSet,
			WorkDeformedLaborAdditionSet workDeformedLaborAdditionSet, 
			Optional<nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneCommonSet> workTimezoneCommonSet,
			List<OverTimeFrameNo> statutoryFrameNoList,
			Optional<FlexCalcSetting> flexCalcSetting) {
		super();
		this.calculationRangeOfOneDay = calculationRangeOfOneDay;
		this.integrationOfDaily = integrationOfDaily;
		this.workTimeSetting = workTimeSetting;
		this.workType = workType;
		this.subHolTransferSetList = subHolTransferSetList;
		this.personalInfo = personalInfo;
		this.dailyUnit = dailyUnit;
		this.fixRestTimeSetting = fixRestTimeSetting;
		this.ootsukaFixedWorkSet = ootsukaFixedWorkSet;
		this.holidayCalcMethodSet = holidayCalcMethodSet;
		this.calculatable = calculatable;
		this.breakCount = breakCount;
		this.coreTimeSetting = coreTimeSetting;
		this.workRegularAdditionSet = workRegularAdditionSet;
		this.workFlexAdditionSet = workFlexAdditionSet;
		this.hourlyPaymentAdditionSet = hourlyPaymentAdditionSet;
		this.workDeformedLaborAdditionSet = workDeformedLaborAdditionSet;
		this.WorkTimezoneCommonSet = workTimezoneCommonSet;
		this.statutoryFrameNoList = statutoryFrameNoList;
		this.flexCalcSetting = flexCalcSetting;
	}
	/**
	 * 計算処理に入ることができないと判断できた時Factory Method
	 * @param personalInfo2 
	 */
	public static ManageReGetClass cantCalc(Optional<WorkType> workType,IntegrationOfDaily integration, DailyCalculationPersonalInformation personalInfo) {
		return new ManageReGetClass(new CalculationRangeOfOneDay(Finally.of(new FlexWithinWorkTimeSheet(Arrays.asList(new WithinWorkTimeFrame(new EmTimeFrameNo(5), 
																																			  new TimeZoneRounding(new TimeWithDayAttr(0), new TimeWithDayAttr(0), null), 
																																			  new TimeSpanForCalc(new TimeWithDayAttr(0), new TimeWithDayAttr(0)), 
																																			  Collections.emptyList(), 
																																			  Collections.emptyList(), 
																																			  Collections.emptyList(), 
																																			  Optional.empty(), 
																																			  Collections.emptyList(), 
																																			  Optional.empty(), 
																																			  Optional.empty())), 
																										Optional.empty())),
																 Finally.of(new OutsideWorkTimeSheet(Optional.empty(),Optional.empty())), 
																 null, 
																 integration.getAttendanceLeave().orElse(null), 
																 null, 
																 Finally.of(new TimevacationUseTimeOfDaily(new AttendanceTime(0), new AttendanceTime(0), new AttendanceTime(0), new AttendanceTime(0))), 
																 integration.getWorkInformation(),
																 Optional.empty()), 
									integration,
									Optional.empty(),
									workType, 
									Collections.emptyList(), 
									personalInfo,
									null,
									Optional.empty(),
									Optional.empty(),
									null,
									false,
									0,
									Optional.empty(),
									null,
									null,
									null,
									null,
									Optional.empty(),
									Collections.emptyList(),
									Optional.empty());
				
	}
	
	/**
	 * 計算処理に入ることができないと判断できた時Factory Method
	 * @param personalInfo2 
	 */
	public static ManageReGetClass cantCalc2(Optional<WorkType> workType,
											 IntegrationOfDaily integration,
											 DailyCalculationPersonalInformation personalInfo,
											 HolidayCalcMethodSet holidayCalcMethodSet, 
											 WorkRegularAdditionSet workRegularAdditionSet,
											 WorkFlexAdditionSet workFlexAdditionSet, 
											 HourlyPaymentAdditionSet hourlyPaymentAdditionSet,
											 WorkDeformedLaborAdditionSet workDeformedLaborAdditionSet
			) {
		return new ManageReGetClass(new CalculationRangeOfOneDay(Finally.of(new FlexWithinWorkTimeSheet(Arrays.asList(new WithinWorkTimeFrame(new EmTimeFrameNo(5), 
																																			  new TimeZoneRounding(new TimeWithDayAttr(0), new TimeWithDayAttr(0), null), 
																																			  new TimeSpanForCalc(new TimeWithDayAttr(0), new TimeWithDayAttr(0)), 
																																			  Collections.emptyList(), 
																																			  Collections.emptyList(), 
																																			  Collections.emptyList(), 
																																			  Optional.empty(), 
																																			  Collections.emptyList(), 
																																			  Optional.empty(), 
																																			  Optional.empty())), 
																										Optional.empty())),
																 Finally.of(new OutsideWorkTimeSheet(Optional.empty(),Optional.empty())), 
																 null, 
																 integration.getAttendanceLeave().orElse(null), 
																 null, 
																 Finally.of(new TimevacationUseTimeOfDaily(new AttendanceTime(0), new AttendanceTime(0), new AttendanceTime(0), new AttendanceTime(0))), 
																 integration.getWorkInformation(),
																 Optional.empty()), 
									integration,
									Optional.empty(),
									workType, 
									Collections.emptyList(), 
									personalInfo,
									null,
									Optional.empty(),
									Optional.empty(),
									holidayCalcMethodSet,
									false,
									0,
									Optional.empty(),
									workRegularAdditionSet,
									workFlexAdditionSet,
									hourlyPaymentAdditionSet,
									workDeformedLaborAdditionSet,
									Optional.empty(),
									Collections.emptyList(),
									Optional.empty());
				
	}
	
		
	/**
	 * 計算処理に入ることができると判断できた時Factory Method
	 */
	public static ManageReGetClass canCalc(CalculationRangeOfOneDay calculationRangeOfOneDay, IntegrationOfDaily integrationOfDaily,
											Optional<WorkTimeSetting> workTimeSetting, Optional<WorkType> workType,
											List<WorkTimezoneOtherSubHolTimeSet> subHolTransferSetList,
											DailyCalculationPersonalInformation personalInfo, DailyUnit dailyUnit,
											Optional<FixRestTimezoneSet> fixRestTimeSetting, 
											Optional<FixedWorkCalcSetting> ootsukaFixedWorkSet,
											HolidayCalcMethodSet holidayCalcMethodSet, 
											int breakCount,
											Optional<CoreTimeSetting> coreTimeSetting, 
											WorkRegularAdditionSet workRegularAdditionSet,
											WorkFlexAdditionSet workFlexAdditionSet, 
											HourlyPaymentAdditionSet hourlyPaymentAdditionSet,
											WorkDeformedLaborAdditionSet workDeformedLaborAdditionSet,
											Optional<nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneCommonSet> workTimezoneCommonSet,
											List<OverTimeFrameNo> statutoryFrameNoList,
											Optional<FlexCalcSetting> flexCalcSetting) {
		
		
		
		
		return new ManageReGetClass(calculationRangeOfOneDay,
									integrationOfDaily,
									workTimeSetting,
									workType,
									subHolTransferSetList,
									personalInfo,
									dailyUnit,
									fixRestTimeSetting,
									ootsukaFixedWorkSet,
									holidayCalcMethodSet,
									true,
									breakCount,
									coreTimeSetting,
									workRegularAdditionSet,
									workFlexAdditionSet,
									hourlyPaymentAdditionSet,
									workDeformedLaborAdditionSet,
									workTimezoneCommonSet,
									statutoryFrameNoList,
									flexCalcSetting);
	
	}
	
	/**
	 * 会社共通で使いまわしをする設定をこのクラスにも設定する
	 */
	public void setCompanyCommonSetting(ManagePerCompanySet managePerCompany) {
		this.holidayAddtionSet = managePerCompany.getHolidayAdditionPerCompany();
		this.dailyUnit = managePerCompany.dailyUnit;
	}

}

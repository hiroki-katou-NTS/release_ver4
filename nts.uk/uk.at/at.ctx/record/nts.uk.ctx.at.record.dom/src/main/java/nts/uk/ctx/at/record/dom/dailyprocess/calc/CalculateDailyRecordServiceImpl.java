package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.eclipse.persistence.internal.oxm.schema.model.Content;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.gul.util.value.Finally;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.bonuspay.autocalc.BonusPayAutoCalcSet;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeSheet;
import nts.uk.ctx.at.record.dom.breakorgoout.OutingTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.breakorgoout.OutingTimeSheet;
import nts.uk.ctx.at.record.dom.breakorgoout.enums.BreakType;
import nts.uk.ctx.at.record.dom.breakorgoout.enums.GoingOutReason;
import nts.uk.ctx.at.record.dom.breakorgoout.primitivevalue.BreakFrameNo;
import nts.uk.ctx.at.record.dom.breakorgoout.primitivevalue.OutingFrameNo;
import nts.uk.ctx.at.record.dom.calculationattribute.AutoCalHolidaySetting;
import nts.uk.ctx.at.record.dom.calculationattribute.AutoCalOfLeaveEarlySetting;
import nts.uk.ctx.at.record.dom.calculationattribute.AutoCalOfOverTime;
import nts.uk.ctx.at.record.dom.calculationattribute.AutoCalRaisingSalarySetting;
import nts.uk.ctx.at.record.dom.calculationattribute.AutoCalcSetOfDivergenceTime;
import nts.uk.ctx.at.record.dom.calculationattribute.AutoCalculationSetting;
import nts.uk.ctx.at.record.dom.calculationattribute.CalAttrOfDailyPerformance;
import nts.uk.ctx.at.record.dom.calculationattribute.enums.AutoCalOverTimeAttr;
import nts.uk.ctx.at.record.dom.calculationattribute.enums.DivergenceTimeAttr;
import nts.uk.ctx.at.record.dom.calculationattribute.enums.LeaveAttr;
import nts.uk.ctx.at.record.dom.calculationattribute.enums.SalaryCalAttr;
import nts.uk.ctx.at.record.dom.calculationattribute.enums.SpecificSalaryCalAttr;
import nts.uk.ctx.at.record.dom.daily.DeductionTotalTime;
import nts.uk.ctx.at.record.dom.daily.LateTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.LeaveEarlyTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.record.dom.daily.TimevacationUseTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.breaktimegoout.BreakTimeGoOutTimes;
import nts.uk.ctx.at.record.dom.daily.breaktimegoout.BreakTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkFrameTimeSheet;
import nts.uk.ctx.at.record.dom.daily.latetime.IntervalExemptionTime;
import nts.uk.ctx.at.record.dom.daily.midnight.MidNightTimeSheet;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.HolidayOfDaily;
import nts.uk.ctx.at.record.dom.raborstandardact.flex.SettingOfFlexWork;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.converter.DailyRecordToAttendanceItemConverter;
import nts.uk.ctx.at.record.dom.editstate.EditStateOfDailyPerformance;
import nts.uk.ctx.at.record.dom.editstate.enums.EditStateSetting;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.record.dom.worklocation.WorkLocationCD;
import nts.uk.ctx.at.record.dom.worktime.TimeActualStamp;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingWork;
import nts.uk.ctx.at.record.dom.worktime.WorkStamp;
import nts.uk.ctx.at.record.dom.worktime.enums.StampSourceInfo;
import nts.uk.ctx.at.record.dom.worktime.primitivevalue.WorkNo;
import nts.uk.ctx.at.record.dom.worktime.primitivevalue.WorkTimes;
import nts.uk.ctx.at.record.dom.worktime.repository.TimeLeavingOfDailyPerformanceRepository;
import nts.uk.ctx.at.shared.dom.bonuspay.enums.HolidayTimesheetCalculationSetting;
import nts.uk.ctx.at.shared.dom.bonuspay.enums.OvertimeTimesheetCalculationSetting;
import nts.uk.ctx.at.shared.dom.bonuspay.enums.WorkingTimesheetCalculationSetting;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ItemValue;
import nts.uk.ctx.at.shared.dom.bonuspay.setting.BonusPaySetting;
import nts.uk.ctx.at.shared.dom.common.CompanyId;
import nts.uk.ctx.at.shared.dom.common.DailyTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.employment.statutory.worktime.employment.EmploymentContractHistory;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalAtrOvertime;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalOvertimeSetting;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalSetting;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.TimeLimitUpperLimitSetting;
import nts.uk.ctx.at.shared.dom.ot.frame.NotUseAtr;
import nts.uk.ctx.at.shared.dom.personallaborcondition.UseAtr;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.AddSettingOfFlexWork;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.AddSettingOfIrregularWork;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.AddSettingOfRegularWork;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.CalculationByActualTimeAtr;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.HolidayCalcMethodSet;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.IncludeHolidaysPremiumCalcDetailSet;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.IncludeHolidaysWorkCalcDetailSet;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.PremiumCalcMethodDetailOfHoliday;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.PremiumCalcMethodOfHoliday;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.WorkTimeCalcMethodDetailOfHoliday;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.WorkTimeCalcMethodOfHoliday;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.ctx.at.shared.dom.workrule.addsettingofworktime.AddVacationSet;
import nts.uk.ctx.at.shared.dom.workrule.addsettingofworktime.VacationAddTimeSet;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.AutoCalcSet;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.OverDayEndAggregateFrameSet;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.OverDayEndCalcSet;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.OverDayEndCalcSetOfExcessHoliday;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.OverDayEndCalcSetOfExcessSpecialHoliday;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.OverDayEndCalcSetOfStatutoryHoliday;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.OverDayEndCalcSetOfWeekDay;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.RaisingSalaryCalcAtr;
import nts.uk.ctx.at.shared.dom.workrule.overtime.StatutoryPrioritySet;
import nts.uk.ctx.at.shared.dom.workrule.statutoryworktime.DailyCalculationPersonalInformation;
import nts.uk.ctx.at.shared.dom.workrule.statutoryworktime.GetOfStatutoryWorkTime;
import nts.uk.ctx.at.shared.dom.workrule.waytowork.PersonalLaborCondition;
import nts.uk.ctx.at.shared.dom.worktime.common.CommonRestSetting;
import nts.uk.ctx.at.shared.dom.worktime.common.FixedRestCalculateMethod;
import nts.uk.ctx.at.shared.dom.worktime.common.LegalOTSetting;
import nts.uk.ctx.at.shared.dom.worktime.common.OverTimeOfTimeZoneSet;
import nts.uk.ctx.at.shared.dom.worktime.common.RestClockManageAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.RestTimeOfficeWorkCalcMethod;
import nts.uk.ctx.at.shared.dom.worktime.common.TimezoneOfFixedRestTimeSet;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneCommonSet;
import nts.uk.ctx.at.shared.dom.worktime.difftimeset.DiffTimeWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixOffdayWorkTimezone;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixedWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixedWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.flexset.FlexWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowFixedRestCalcMethod;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowFixedRestSet;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowRestCalcMethod;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowRestSet;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowRestSetting;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowRestTimezone;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkRestTimezone;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.predset.BreakDownTimeDay;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetermineTime;
import nts.uk.ctx.at.shared.dom.worktime.predset.PrescribedTimezoneSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeDailyAtr;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeDivision;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeMethodSet;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktype.AttendanceHolidayAttr;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.TimeWithDayAttr;

@Stateless
public class CalculateDailyRecordServiceImpl implements CalculateDailyRecordService{
	
	@Inject
	private WorkTypeRepository workTypeRepository;
	
	//@Inject
	//private EmploymentContractHistoryAdopter employmentContractHistoryAdopter;
	
	@Inject
	private PredetemineTimeSettingRepository predetemineTimeSetRepository;

	@Inject
	private WorkTimeSettingRepository workTimeSettingRepository;
	
	@Inject
	private GetOfStatutoryWorkTime getOfStatutoryWorkTime;
	
	@Inject
	private WorkInformationRepository workInformationRepository;
	
	@Inject
	private FixedWorkSettingRepository fixedWorkSettingRepository;
	
	@Inject
	private FlowWorkSettingRepository flowWorkSettingRepository;
	
	@Inject
	private DiffTimeWorkSettingRepository diffTimeWorkSettingRepository;
	
	@Inject
	private FlexWorkSettingRepository flexWorkSettingRepository;

	@Inject
	private DailyRecordToAttendanceItemConverter dailyRecordToAttendanceItemConverter;
	
	
	/**
	 * 勤務情報を取得して計算
	 * @param placeId 職場ID
	 * @param employmentCd 雇用コード
	 * @param employeeId 社員ID
	 * @param targetDate 対象年月日
	 * @param integrationOfDaily 日別実績(Work)
	 * @return 日別実績(Work)
	 */
	@Override
	public IntegrationOfDaily calculate(IntegrationOfDaily integrationOfDaily) {
//		/*日別実績(Work)の退避*/
//		val copyIntegrationOfDaily = integrationOfDaily;
		if (integrationOfDaily.getAffiliationInfor() == null) return integrationOfDaily;
		// 実績データの計算
		val test = this.calcDailyAttendancePerformance(integrationOfDaily); 
		return test;
	}

	
	private IntegrationOfDaily calcDailyAttendancePerformance(IntegrationOfDaily integrationOfDaily) {
		val record = createRecord(integrationOfDaily);
		return calcRecord(record,integrationOfDaily);
	}
	
	/**
	 * 実績データから時間帯の作成
	 * @param companyId 会社ID
	 * @param employeeId 社員コード
	 * @param targetDate 対象日
	 * @param integrationOfDaily 
	 */
	private CalculationRangeOfOneDay createRecord(IntegrationOfDaily integrationOfDaily) {
		String companyId = AppContexts.user().companyId();
		String placeId = integrationOfDaily.getAffiliationInfor().getWplID();
		String employmentCd = integrationOfDaily.getAffiliationInfor().getEmploymentCode().toString();
		String employeeId = integrationOfDaily.getAffiliationInfor().getEmployeeId();
		GeneralDate targetDate = integrationOfDaily.getAffiliationInfor().getYmd(); 
			
		/*1日の計算範囲クラスを作成*/
		val oneRange = createOneDayRange(integrationOfDaily);
		/*勤務種類の取得*/
		val workInfo = integrationOfDaily.getWorkInformation();
		//val workType = this.workTypeRepository.findByPK(companyId, "001").get();
		val workType = this.workTypeRepository.findByPK(companyId,workInfo.getRecordWorkInformation().getWorkTypeCode().v()); // 要確認：勤務種類マスタが削除されている場合は考慮しない？
		if(!workType.isPresent()) return oneRange;
		
		/*就業時間帯勤務区分*/
		//Optional<WorkTimeSetting> workTime = workTimeSettingRepository.findByCode(companyId,//"901"); 
		if(workInfo == null || workInfo.getRecordWorkInformation() == null || workInfo.getRecordWorkInformation().getWorkTimeCode() == null)
			return oneRange;
		Optional<WorkTimeSetting> workTime = workTimeSettingRepository.findByCode(companyId,workInfo.getRecordWorkInformation().getWorkTimeCode().toString());
		if(!workTime.isPresent()) return oneRange;
		/*労働制*/
		DailyCalculationPersonalInformation personalInfo = getPersonInfomation(integrationOfDaily);
		
		/**
		 * 勤務種類が休日系なら、所定時間の時間を変更する
		 */
		if(workType.get().getAttendanceHolidayAttr().equals(AttendanceHolidayAttr.HOLIDAY)) {
			oneRange.getPredetermineTimeSetForCalc().endTimeSetStartTime();
		}
		
		//---------------------------------Repositoryが整理されるまでの一時的な作成-------------------------------------------
		//休憩時間帯(BreakManagement)
		List<BreakTimeSheet> breakTimeSheet = new ArrayList<>();
//		breakTimeSheet.add(new BreakTimeSheet(new BreakFrameNo(1),
//											  new TimeWithDayAttr(720),
//											  new TimeWithDayAttr(780),
//											  new AttendanceTime(0)));
		if(!integrationOfDaily.getBreakTime().isEmpty()) {
			if(!integrationOfDaily.getBreakTime().get(0).getBreakTimeSheets().isEmpty()) {
				breakTimeSheet.addAll(integrationOfDaily.getBreakTime().get(0).getBreakTimeSheets());
			}
		}
		
		List<BreakTimeOfDailyPerformance> breakTimeOfDailyList = new ArrayList<>();
		breakTimeOfDailyList.add(new BreakTimeOfDailyPerformance(employeeId, 
																 BreakType.REFER_WORK_TIME, 
																 breakTimeSheet, 
																 targetDate));

		//外出時間帯
		WorkStamp goOut = new WorkStamp(new TimeWithDayAttr(780),new TimeWithDayAttr(780),new WorkLocationCD("01"), StampSourceInfo.CORRECTION_RECORD_SET);
		WorkStamp back  = new WorkStamp(new TimeWithDayAttr(840),new TimeWithDayAttr(840),new WorkLocationCD("01"), StampSourceInfo.CORRECTION_RECORD_SET);
		List<OutingTimeSheet> outingTimeSheets = new ArrayList<>();
		outingTimeSheets.add(new OutingTimeSheet(new OutingFrameNo(1),
												  Optional.of(new TimeActualStamp(goOut,goOut,1)),
												  new AttendanceTime(0),
												  new AttendanceTime(60),
												  GoingOutReason.PUBLIC,
												  Optional.of(new TimeActualStamp(back, back, 1))
												 ));
		OutingTimeOfDailyPerformance goOutTimeSheetList = new OutingTimeOfDailyPerformance(employeeId,targetDate,outingTimeSheets);
		
		MidNightTimeSheet midNightTimeSheet = new MidNightTimeSheet(companyId, 
																	new TimeWithDayAttr(1320),
																	new TimeWithDayAttr(1740));
		
		//流動勤務の休憩時間帯
		FlowWorkRestTimezone fluRestTime = new FlowWorkRestTimezone(
												  true,
												  new TimezoneOfFixedRestTimeSet(Collections.emptyList()),
												  new FlowRestTimezone(Collections.emptyList(),false,new FlowRestSetting(new AttendanceTime(0), new AttendanceTime(0)))
												  );
		
		//流動固定休憩設定
		FlowFixedRestSet fluidPrefixBreakTimeSet = new FlowFixedRestSet(false,false,false,FlowFixedRestCalcMethod.REFER_MASTER);
		
		//0時跨ぎ計算設定
		OverDayEndCalcSet overDayEndCalcSet = new OverDayEndCalcSet(companyId,
																	UseAtr.NOTUSE,
																	new OverDayEndAggregateFrameSet(Collections.emptyList(),Collections.emptyList(),Collections.emptyList()),
																	new OverDayEndCalcSetOfStatutoryHoliday(UseAtr.NOTUSE,UseAtr.NOTUSE,UseAtr.NOTUSE),
																	new OverDayEndCalcSetOfExcessHoliday(UseAtr.NOTUSE,UseAtr.NOTUSE,UseAtr.NOTUSE),
																	new OverDayEndCalcSetOfExcessSpecialHoliday(UseAtr.NOTUSE,UseAtr.NOTUSE,UseAtr.NOTUSE),
																	new OverDayEndCalcSetOfWeekDay(UseAtr.NOTUSE,UseAtr.NOTUSE,UseAtr.NOTUSE));
		//残業時間の自動計算設定
		AutoCalSetting autoCalSetting = new AutoCalSetting(TimeLimitUpperLimitSetting.NOUPPERLIMIT,AutoCalAtrOvertime.CALCULATEMBOSS);
		AutoCalOvertimeSetting autoCalcOverTimeWork = new AutoCalOvertimeSetting(autoCalSetting, 
																				 autoCalSetting, 
																				 autoCalSetting, 
																				 autoCalSetting, 
																				 autoCalSetting, 
																				 autoCalSetting);
		/*前日の勤務情報取得  */
		WorkInfoOfDailyPerformance yestarDayWorkInfo = workInformationRepository.find(employeeId, targetDate.addDays(-1)).orElse(workInfo);
		val yesterDay = this.workTypeRepository.findByPK(companyId, yestarDayWorkInfo.getRecordWorkInformation().getWorkTypeCode().v()).orElse(workType.get());
		//val yesterDay = this.workTypeRepository.findByPK(companyId, "001");//yestarDayWorkInfo.getRecordWorkInformation().getWorkTypeCode().v());
		/*翌日の勤務情報取得 */
		WorkInfoOfDailyPerformance tomorrowDayWorkInfo = workInformationRepository.find(employeeId, targetDate.addDays(1)).orElse(workInfo);
		val tomorrow = this.workTypeRepository.findByPK(companyId, tomorrowDayWorkInfo.getRecordWorkInformation().getWorkTypeCode().v()).orElse(workType.get());
		//val tomorrow = this.workTypeRepository.findByPK(companyId, "001");//tomorrowDayWorkInfo.getRecordWorkInformation().getWorkTypeCode().v());
		
		Optional<SettingOfFlexWork> flexCalcMethod = Optional.empty();
		
		//---------------------------------Repositoryが整理されるまでの一時的な作成-------------------------------------------
		if (workTime.get().getWorkTimeDivision().getWorkTimeDailyAtr().isFlex()) {
			/* フレックス勤務 */
			val flexWorkSetOpt = flexWorkSettingRepository.find(companyId,workInfo.getRecordWorkInformation().getWorkTimeCode().v());
				oneRange.createTimeSheetAsFlex(personalInfo.getWorkingSystem(),oneRange.getPredetermineTimeSetForCalc(),
												BonusPaySetting.createFromJavaType(companyId,
														"01"/*ここは聞く*/,
														"テスト加給設定"/*ここは聞く*/,
														Collections.emptyList(),
														Collections.emptyList()
														),
												flexWorkSetOpt.get().getOffdayWorkTime().getLstWorkTimezone(),
											   flexWorkSetOpt.get().getLstHalfDayWorkTimezone().get(0).getWorkTimezone().getLstOTTimezone(),
											   /*休出時間帯リスト*/Collections.emptyList(),overDayEndCalcSet, yesterDay, workType.get(),tomorrow,
											   new BreakDownTimeDay(new AttendanceTime(4),new AttendanceTime(4),new AttendanceTime(8)),
												personalInfo.getStatutoryWorkTime(),autoCalcOverTimeWork,LegalOTSetting.LEGAL_INTERNAL_TIME,StatutoryPrioritySet.priorityNormalOverTimeWork,
												workTime.get(),flexWorkSetOpt.get(),goOutTimeSheetList,oneRange.getOneDayOfRange(),oneRange.getAttendanceLeavingWork(),
												workTime.get().getWorkTimeDivision(),breakTimeOfDailyList,midNightTimeSheet,personalInfo
											   );
		} else {
			switch (workTime.get().getWorkTimeDivision().getWorkTimeMethodSet()) {
			case FIXED_WORK:
				/* 固定 */
				val fixedWorkSetting = fixedWorkSettingRepository.findByKey(companyId, workInfo.getRecordWorkInformation().getWorkTimeCode().v());
				oneRange.createWithinWorkTimeSheet(personalInfo.getWorkingSystem(), workTime.get().getWorkTimeDivision().getWorkTimeMethodSet(),
						RestClockManageAtr.IS_CLOCK_MANAGE, goOutTimeSheetList,
						new CommonRestSetting(RestTimeOfficeWorkCalcMethod.OFFICE_WORK_APPROP_ALL),
						Optional.of(FixedRestCalculateMethod.MASTER_REF), workTime.get().getWorkTimeDivision(),
						oneRange.getPredetermineTimeSetForCalc(), fixedWorkSetting.get(), 
						BonusPaySetting.createFromJavaType(companyId,
						"01"/*ここは聞く*/,
						"テスト加給設定"/*ここは聞く*/,
						Collections.emptyList(),
						Collections.emptyList()
						)
						, fixedWorkSetting.get().getLstHalfDayWorkTimezone().get(0).getWorkTimezone().getLstOTTimezone(),
						fixedWorkSetting.get().getOffdayWorkTimezone().getLstWorkTimezone(), 
						overDayEndCalcSet, 
						Collections.emptyList(), 
						yesterDay, 
						workType.get(),
						tomorrow, 
						new BreakDownTimeDay(new AttendanceTime(4),new AttendanceTime(4),new AttendanceTime(8)),
						personalInfo.getStatutoryWorkTime(), 
						autoCalcOverTimeWork, 
						fixedWorkSetting.get().getLegalOTSetting(), 
						StatutoryPrioritySet.priorityNormalOverTimeWork, 
						workTime.get(),
						breakTimeOfDailyList,
						midNightTimeSheet,
						personalInfo);
				break;
			case FLOW_WORK:
				/* 流動勤務 */
				val flowWorkSetOpt = flowWorkSettingRepository.find(companyId,workInfo.getRecordWorkInformation().getWorkTimeCode().v());
				break;
			case DIFFTIME_WORK:
				/* 時差勤務 */
				val diffWorkSetOpt = diffTimeWorkSettingRepository.find(companyId,workInfo.getRecordWorkInformation().getWorkTimeCode().v());
//			case Enum_Overtime_Work:
				break;
			default:
				throw new RuntimeException("unknown workTimeMethodSet" + workTime.get().getWorkTimeDivision().getWorkTimeMethodSet());
			}
		}
		return oneRange;
	}
	
	/**
	 * 作成した時間帯から時間を計算する
	 * @param integrationOfDaily 日別実績(WORK)
	 * @return 日別実績(WORK)
	 */
	private IntegrationOfDaily calcRecord(CalculationRangeOfOneDay oneRange, IntegrationOfDaily integrationOfDaily) {
		String companyId = AppContexts.user().companyId();
		String placeId = integrationOfDaily.getAffiliationInfor().getWplID();
		String employmentCd = integrationOfDaily.getAffiliationInfor().getEmploymentCode().toString();
		String employeeId = integrationOfDaily.getAffiliationInfor().getEmployeeId();
		GeneralDate targetDate = integrationOfDaily.getAffiliationInfor().getYmd(); 
		
		/*日別実績(Work)の退避*/
		val copyIntegrationOfDaily = dailyRecordToAttendanceItemConverter.setData(integrationOfDaily).toDomain();
		
		//残業の自動計算設定
		val overTimeSet = new AutoCalcSet(AutoCalAtrOvertime.CALCULATEMBOSS);
		AutoCalculationSetting aCalcSet = new AutoCalculationSetting(AutoCalAtrOvertime.CALCULATEMBOSS, TimeLimitUpperLimitSetting.NOUPPERLIMIT);
		AutoCalOfOverTime oTimeSet = new AutoCalOfOverTime(aCalcSet,aCalcSet,aCalcSet,aCalcSet,aCalcSet,aCalcSet); 
		
		//残業時間の自動計算設定
		AutoCalSetting autoCalSetting = new AutoCalSetting(TimeLimitUpperLimitSetting.NOUPPERLIMIT,AutoCalAtrOvertime.CALCULATEMBOSS);
		AutoCalOvertimeSetting autoCalcOverTimeWork = new AutoCalOvertimeSetting(autoCalSetting, 
																				 autoCalSetting, 
																				 autoCalSetting, 
																				 autoCalSetting, 
																				 autoCalSetting, 
																				 autoCalSetting);
		
		//休出の自動計算設定
		AutoCalSetting holidayAutoCalcSetting = new AutoCalSetting(TimeLimitUpperLimitSetting.NOUPPERLIMIT, AutoCalAtrOvertime.CALCULATEMBOSS);
		AutoCalHolidaySetting autoHolidaySet = new AutoCalHolidaySetting(aCalcSet, aCalcSet);
		//遅刻早退の自動計算設定
		AutoCalOfLeaveEarlySetting lateLeave = new AutoCalOfLeaveEarlySetting(LeaveAttr.NOT_USE, LeaveAttr.NOT_USE);
		//加給の自動計算設定
		RaisingSalaryCalcAtr raisingAutoCalcSet = new RaisingSalaryCalcAtr(true,true);
		AutoCalRaisingSalarySetting autoRaisingSet = new  AutoCalRaisingSalarySetting(SalaryCalAttr.USE,SpecificSalaryCalAttr.USE);
		//加給時間計算設定
		BonusPayAutoCalcSet bonusPayAutoCalcSet = new BonusPayAutoCalcSet(new CompanyId(companyId),
																		   1,
																		   WorkingTimesheetCalculationSetting.CalculateAutomatic,
																		   OvertimeTimesheetCalculationSetting.CalculateAutomatic,
																		   HolidayTimesheetCalculationSetting.CalculateAutomatical);
		
		CalAttrOfDailyPerformance calcAtrOfDaily = new CalAttrOfDailyPerformance(employeeId, 
																				 targetDate,
																				 new AutoCalculationSetting(AutoCalAtrOvertime.CALCULATEMBOSS,
																						 					TimeLimitUpperLimitSetting.NOUPPERLIMIT), 
																				 autoRaisingSet, 
																				 autoHolidaySet, 
																				 oTimeSet, 
																				 lateLeave, 
																				 new AutoCalcSetOfDivergenceTime(DivergenceTimeAttr.USE));
		
		//変形労働勤務の加算設定
		WorkTimeCalcMethodDetailOfHoliday workDetailSet = new WorkTimeCalcMethodDetailOfHoliday(nts.uk.ctx.at.shared.dom.workrule.addsettingofworktime.NotUseAtr.To,
																							new IncludeHolidaysWorkCalcDetailSet(nts.uk.ctx.at.shared.dom.workrule.addsettingofworktime.NotUseAtr.To));
		WorkTimeCalcMethodOfHoliday workTimeClacMethodOfHoliday = new WorkTimeCalcMethodOfHoliday(CalculationByActualTimeAtr.CalculationByActualTime,workDetailSet);
		PremiumCalcMethodDetailOfHoliday preDetailSet = new PremiumCalcMethodDetailOfHoliday(new IncludeHolidaysPremiumCalcDetailSet(nts.uk.ctx.at.shared.dom.workrule.addsettingofworktime.NotUseAtr.To));
		PremiumCalcMethodOfHoliday premiumCalcMethodOfHoliday = new PremiumCalcMethodOfHoliday(preDetailSet,CalculationByActualTimeAtr.CalculationByActualTime);
		HolidayCalcMethodSet holidaycalcMethodSet = new HolidayCalcMethodSet(workTimeClacMethodOfHoliday,premiumCalcMethodOfHoliday);
		val illegularAddSetting = new AddSettingOfIrregularWork(new CompanyId(companyId),holidaycalcMethodSet);
		//フレックス勤務の加算設定
		AddSettingOfFlexWork flexAddSetting = new AddSettingOfFlexWork(new CompanyId(companyId),holidaycalcMethodSet);
		//固定勤務の加算設定
		AddSettingOfRegularWork regularAddSetting = new AddSettingOfRegularWork(new CompanyId(companyId),holidaycalcMethodSet);

		
		//遅刻時間帯
		LateTimeSheet lateTimeSheet = new LateTimeSheet(Optional.empty(),
														Optional.empty(),
														1,
														Optional.empty(),
														Optional.empty());
		//日別実績の遅刻時間帯
		LateTimeOfDaily lateTimeOfDaily = new LateTimeOfDaily(TimeWithCalculation.sameTime(new AttendanceTime(0)), 
															  TimeWithCalculation.sameTime(new AttendanceTime(0)), 
															  new nts.uk.ctx.at.shared.dom.worktime.common.WorkNo(1), 
															  new TimevacationUseTimeOfDaily(new AttendanceTime(0), 
																	  						 new AttendanceTime(0), 
																	  						 new AttendanceTime(0), 
																	  						 new AttendanceTime(0)), 
															  new IntervalExemptionTime(new AttendanceTime(0),
																	  					new AttendanceTime(0),
																	  					new AttendanceTime(0))
															  );
		//早退時間帯
		LeaveEarlyTimeSheet leaveEarlyTimeSheet= new LeaveEarlyTimeSheet(Optional.empty(),
																		 Optional.empty()); 
		//日別実績の早退時間帯
		LeaveEarlyTimeOfDaily leaveEarlyTimeOfDaily = new LeaveEarlyTimeOfDaily(TimeWithCalculation.sameTime(new AttendanceTime(0)), 
				  											                    TimeWithCalculation.sameTime(new AttendanceTime(0)), 
				  											                    new nts.uk.ctx.at.shared.dom.worktime.common.WorkNo(1), 
				  											                    new TimevacationUseTimeOfDaily(new AttendanceTime(0), 
				  											                    		new AttendanceTime(0), 
				  											                    		new AttendanceTime(0), 
				  											                    		new AttendanceTime(0)), 
				  											                    new IntervalExemptionTime(new AttendanceTime(0),
				  											                    		new AttendanceTime(0),
				  											                    		new AttendanceTime(0))
				  																);
		//個人労働条件
		PersonalLaborCondition personalLabor = new PersonalLaborCondition(oneRange.getPredetermineTimeSetForCalc().getAdditionSet());
		//労働制
		DailyCalculationPersonalInformation personalInfo = getPersonInfomation(integrationOfDaily);
		//休暇クラス
		VacationClass vacation = new VacationClass(new HolidayOfDaily());
		
		Optional<SettingOfFlexWork> flexCalcMethod = Optional.empty();
 
		//-------------------------計算用一時的クラス作成----------------------------
		
		Optional<WorkTimeDailyAtr> workTime = Optional.empty();
		if(integrationOfDaily.getWorkInformation().getRecordWorkInformation().getWorkTimeCode() != null) {
			val workTimeSetting = workTimeSettingRepository.findByCode(companyId,integrationOfDaily.getWorkInformation().getRecordWorkInformation().getWorkTimeCode().toString());
			workTime = workTimeSetting.isPresent()?Optional.of(workTimeSetting.get().getWorkTimeDivision().getWorkTimeDailyAtr()):Optional.empty();
		}
		
		if(integrationOfDaily.getWorkInformation() == null || integrationOfDaily.getWorkInformation().getRecordWorkInformation() == null || integrationOfDaily.getWorkInformation().getRecordWorkInformation().getWorkTypeCode() == null)
			return integrationOfDaily;
		
		val workType = this.workTypeRepository.findByPK(companyId,integrationOfDaily.getWorkInformation().getRecordWorkInformation().getWorkTypeCode().v()); // 要確認：勤務種類マスタが削除されている場合は考慮しない？
		if(!workType.isPresent()) return integrationOfDaily;
		//休暇加算時間設定
		VacationAddTimeSet vacationAddSetting = new VacationAddTimeSet(new BreakDownTimeDay(oneRange.getPredetermineTimeSetForCalc().getAdditionSet().getPredTime().getOneDay(), 
																							oneRange.getPredetermineTimeSetForCalc().getAdditionSet().getPredTime().getMorning(),
																							oneRange.getPredetermineTimeSetForCalc().getAdditionSet().getPredTime().getAfternoon()),
																	   new AddVacationSet(nts.uk.ctx.at.shared.dom.workrule.addsettingofworktime.NotUseAtr.To,
																			   			  nts.uk.ctx.at.shared.dom.workrule.addsettingofworktime.NotUseAtr.To,
																			   			  nts.uk.ctx.at.shared.dom.workrule.addsettingofworktime.NotUseAtr.To));
		
		/*時間の計算*/
		integrationOfDaily = AttendanceTimeOfDailyPerformance.calcTimeResult(oneRange,integrationOfDaily,autoCalcOverTimeWork,holidayAutoCalcSetting,
					Optional.empty(),//Optional.of(personalLabor),
				    vacation,
				    workType.get(),
				    lateTimeSheet,
				    leaveEarlyTimeSheet,
				    lateTimeOfDaily,
				    leaveEarlyTimeOfDaily,
				    lateLeave.getLeaveLate().isUse(),  //日別実績の計算区分.遅刻早退の自動計算設定.遅刻
				    lateLeave.getLeaveEarly().isUse(),  //日別実績の計算区分.遅刻早退の自動計算設定.早退
				    personalInfo.getWorkingSystem(),
				    illegularAddSetting,
				    flexAddSetting,
				    regularAddSetting,
				    vacationAddSetting,
				    AutoCalOverTimeAttr.CALCULATION_FROM_STAMP,
				    workTime.get(),
				    flexCalcMethod,
				    holidaycalcMethodSet,
					raisingAutoCalcSet,
					bonusPayAutoCalcSet,
					calcAtrOfDaily,
					Collections.emptyList(),
					Collections.emptyList());
	
		

//		// 編集状態を取得（日別実績の編集状態が持つ勤怠項目IDのみのList作成）
		List<Integer> attendanceItemIdList = integrationOfDaily.getEditState().stream().filter(editState -> editState.getEmployeeId()==copyIntegrationOfDaily.getAffiliationInfor().getEmployeeId()
				   && editState.getYmd() == copyIntegrationOfDaily.getAffiliationInfor().getYmd())
        .map(editState -> editState.getAttendanceItemId())
        .distinct()
        .collect(Collectors.toList());

		IntegrationOfDaily calcResultIntegrationOfDaily = integrationOfDaily;
		
		if(!attendanceItemIdList.isEmpty()) {
			DailyRecordToAttendanceItemConverter beforDailyRecordDto = this.dailyRecordToAttendanceItemConverter.setData(copyIntegrationOfDaily);	
			List<ItemValue> itemValueList = beforDailyRecordDto.convert(attendanceItemIdList);		
			DailyRecordToAttendanceItemConverter afterDailyRecordDto = this.dailyRecordToAttendanceItemConverter.setData(integrationOfDaily);	
			afterDailyRecordDto.merge(itemValueList);
			//手修正された項目の値を計算前に戻す 		
			calcResultIntegrationOfDaily = afterDailyRecordDto.toDomain();
		}
			
		/*日別実績への項目移送*/
		//eturn integrationOfDaily;
		return calcResultIntegrationOfDaily;
	}
	
	
	/**
	 * １日の範囲クラス作成
	 * @param companyId 会社コード
	 * @param employeeId 社員ID
	 * @param targetDate 対象日
	 * @param integrationOfDaily 日別実績(Work)
	 * @return 1日の計算範囲
	 */
	private CalculationRangeOfOneDay createOneDayRange(IntegrationOfDaily integrationOfDaily) {
		String companyId = AppContexts.user().companyId();
		String placeId = integrationOfDaily.getAffiliationInfor().getWplID();
		String employmentCd = integrationOfDaily.getAffiliationInfor().getEmploymentCode().toString();
		String employeeId = integrationOfDaily.getAffiliationInfor().getEmployeeId();
		GeneralDate targetDate = integrationOfDaily.getAffiliationInfor().getYmd(); 
		/*所定時間設定取得*/
		//val predetermineTimeSet = predetemineTimeSetRepository.findByWorkTimeCode(companyId,"901");// integrationOfDaily.getWorkInformation().getRecordWorkInformation().getWorkTimeCode().toString());
		Optional<PredetemineTimeSetting> predetermineTimeSet = Optional.empty();
		String workTimeCode = null;
		if(integrationOfDaily.getWorkInformation().getRecordWorkInformation().getWorkTimeCode() != null) {
			predetermineTimeSet = predetemineTimeSetRepository.findByWorkTimeCode(companyId,integrationOfDaily.getWorkInformation().getRecordWorkInformation().getWorkTimeCode().toString());
			workTimeCode = integrationOfDaily.getWorkInformation().getRecordWorkInformation().getWorkTimeCode().toString();
		}
		
		if(!predetermineTimeSet.isPresent()) {
			predetermineTimeSet = Optional.of(new PredetemineTimeSetting(companyId,
															new AttendanceTime(0),
															new WorkTimeCode(workTimeCode),
															new PredetermineTime(new BreakDownTimeDay(new AttendanceTime(0),new AttendanceTime(0),new AttendanceTime(0)),
																	  			 new BreakDownTimeDay(new AttendanceTime(0),new AttendanceTime(0),new AttendanceTime(0))),
															false,
															new PrescribedTimezoneSetting(new TimeWithDayAttr(0),new TimeWithDayAttr(0),Collections.emptyList()),
															new TimeWithDayAttr(0),
															false));
					
		}
		/*1日の計算範囲取得*/
		val calcRangeOfOneDay = new TimeSpanForCalc(predetermineTimeSet.get().getStartDateClock()
												   ,predetermineTimeSet.get().getStartDateClock().forwardByMinutes(predetermineTimeSet.get().getRangeTimeDay().valueAsMinutes()));
		
		WorkInfoOfDailyPerformance toDayWorkInfo = integrationOfDaily.getWorkInformation();
		
		/*ジャストタイムの判断するための設定取得*/
//		boolean justLate        = /*就業時間帯から固定・流動・フレックスの設定を取得してくるロジック*/;
//		boolean justEarlyLeave  = /*就業時間帯から固定・流動・フレックスの設定を取得してくるロジック*/;
//		/*日別実績の出退勤時刻セット*/
		Optional<TimeLeavingOfDailyPerformance> timeLeavingOfDailyPerformance = integrationOfDaily.getAttendanceLeave();
		if(!timeLeavingOfDailyPerformance.isPresent()) {
			//TimeLeavingOfDailyPerformance attendanceLeavingOfDaily = timeLeavingOfDailyPerformanceRepository.
			WorkStamp attendance = new WorkStamp(new TimeWithDayAttr(0),new TimeWithDayAttr(0), new WorkLocationCD("01"), StampSourceInfo.CORRECTION_RECORD_SET );
			WorkStamp leaving = new WorkStamp(new TimeWithDayAttr(0),new TimeWithDayAttr(0), new WorkLocationCD("01"), StampSourceInfo.CORRECTION_RECORD_SET );
			TimeActualStamp stamp = new TimeActualStamp(attendance,leaving,1);
			TimeLeavingWork timeLeavingWork = new TimeLeavingWork(new nts.uk.ctx.at.shared.dom.worktime.common.WorkNo(1),Optional.of(stamp),Optional.of(stamp));
			List<TimeLeavingWork> timeLeavingWorkList = new ArrayList<>();
			timeLeavingWorkList.add(timeLeavingWork);
			timeLeavingOfDailyPerformance = Optional.of(new TimeLeavingOfDailyPerformance(employeeId,new WorkTimes(1),timeLeavingWorkList,targetDate)); 
		}
		return new CalculationRangeOfOneDay(Finally.empty(),  
											Finally.empty(),
											calcRangeOfOneDay,
											timeLeavingOfDailyPerformance.get(),/*出退勤*/
											PredetermineTimeSetForCalc.convertMastarToCalc(predetermineTimeSet.get())/*所定時間帯(計算用)*/,
											Finally.of(new TimevacationUseTimeOfDaily(new AttendanceTime(0),new AttendanceTime(0),new AttendanceTime(0),new AttendanceTime(0))),
											toDayWorkInfo);
	}
	
	/**
	 * 労働制を取得する
	 * @return 日別計算用の個人情報
	 */
	private DailyCalculationPersonalInformation getPersonInfomation(IntegrationOfDaily integrationOfDaily) {
		String companyId = AppContexts.user().companyId();
		String placeId = integrationOfDaily.getAffiliationInfor().getWplID();
		String employmentCd = integrationOfDaily.getAffiliationInfor().getEmploymentCode().toString();
		String employeeId = integrationOfDaily.getAffiliationInfor().getEmployeeId();
		GeneralDate targetDate = integrationOfDaily.getAffiliationInfor().getYmd(); 
		//Optional<EmploymentContractHistory> employmentContractHistory = this.employmentContractHistoryAdopter.findByEmployeeIdAndBaseDate(employeeId, targetDate);
		Optional<EmploymentContractHistory> employmentContractHistory = Optional.of(new EmploymentContractHistory(employeeId,WorkingSystem.REGULAR_WORK));
		if(!employmentContractHistory.isPresent()) {
			throw new RuntimeException("Can't get WorkingSystem");
		}
		// 労働制
		return getOfStatutoryWorkTime.getDailyTimeFromStaturoyWorkTime(employmentContractHistory.get().getWorkingSystem()
																	, companyId
																	, placeId
																	, employmentCd
																	, employeeId
																	, targetDate);
	}
}

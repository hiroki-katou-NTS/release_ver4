package nts.uk.ctx.at.record.dom.actualworkinghours;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.actualworkinghours.daily.workschedule.WorkScheduleTimeOfDaily;
import nts.uk.ctx.at.record.dom.adapter.personnelcostsetting.PersonnelCostSettingImport;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeSheet;
import nts.uk.ctx.at.record.dom.calculationattribute.BonusPayAutoCalcSet;
import nts.uk.ctx.at.record.dom.calculationattribute.CalAttrOfDailyPerformance;
import nts.uk.ctx.at.record.dom.daily.breaktimegoout.BreakTimeOfDaily;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.CheckExcessAtr;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.ManageReGetClass;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.OverTimeFrameTime;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.PredetermineTimeSetForCalc;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.VacationClass;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.converter.DailyRecordToAttendanceItemConverter;
import nts.uk.ctx.at.record.dom.divergence.time.DivergenceTime;
import nts.uk.ctx.at.record.dom.divergencetime.DiverdenceReasonCode;
import nts.uk.ctx.at.record.dom.divergencetime.DivergenceReasonContent;
import nts.uk.ctx.at.record.dom.divergencetimeofdaily.DivergenceTimeOfDaily;
import nts.uk.ctx.at.record.dom.premiumtime.PremiumTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.raborstandardact.flex.SettingOfFlexWork;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.SystemFixedErrorAlarm;
import nts.uk.ctx.at.shared.dom.calculation.holiday.kmk013_splitdomain.DeductLeaveEarly;
import nts.uk.ctx.at.shared.dom.calculation.holiday.time.OverTimeFrame;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensatoryOccurrenceSetting;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.worktime.common.DeductionTime;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixRestTimezoneSet;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeDailyAtr;
import nts.uk.ctx.at.shared.dom.worktype.AttendanceHolidayAttr;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeClassification;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 
 * @author nampt 日別実績の勤務実績時間
 *
 */
@Getter
public class ActualWorkingTimeOfDaily {

	// 割増時間
	private PremiumTimeOfDailyPerformance premiumTimeOfDailyPerformance;

	// 拘束差異時間
	private AttendanceTime constraintDifferenceTime;

	// 拘束時間
	private ConstraintTime constraintTime;

	// 時差勤務時間
	private AttendanceTime timeDifferenceWorkingHours;

	// 総労働時間
	private TotalWorkingTime totalWorkingTime;

	// //代休発生情報
	// private SubHolOccurrenceInfo subHolOccurrenceInfo;

	// 乖離時間
	@Setter
	private DivergenceTimeOfDaily divTime;

	/**
	 * Constructor
	 */
	private ActualWorkingTimeOfDaily(AttendanceTime constraintDiffTime, ConstraintTime constraintTime,
			AttendanceTime timeDiff, TotalWorkingTime totalWorkingTime, DivergenceTimeOfDaily divTime,
			PremiumTimeOfDailyPerformance premiumTime) {
		this.premiumTimeOfDailyPerformance = premiumTime;
		this.constraintDifferenceTime = constraintDiffTime;
		this.constraintTime = constraintTime;
		this.timeDifferenceWorkingHours = timeDiff;
		this.totalWorkingTime = totalWorkingTime;
		this.divTime = divTime;
	}

	public static ActualWorkingTimeOfDaily of(TotalWorkingTime totalWorkTime, int midBind, int totalBind, int bindDiff,
			int diffTimeWork) {
		return new ActualWorkingTimeOfDaily(new AttendanceTime(bindDiff),
				new ConstraintTime(new AttendanceTime(midBind), new AttendanceTime(totalBind)),
				new AttendanceTime(diffTimeWork), totalWorkTime, new DivergenceTimeOfDaily(),
				new PremiumTimeOfDailyPerformance());
	}

    public static ActualWorkingTimeOfDaily of(TotalWorkingTime totalWorkTime, int midBind, int totalBind, int bindDiff,
            int diffTimeWork, DivergenceTimeOfDaily divTime) {
        return new ActualWorkingTimeOfDaily(new AttendanceTime(bindDiff),
                new ConstraintTime(new AttendanceTime(midBind), new AttendanceTime(totalBind)),
                new AttendanceTime(diffTimeWork), totalWorkTime, divTime, new PremiumTimeOfDailyPerformance());
    }

    public static ActualWorkingTimeOfDaily of(TotalWorkingTime totalWorkTime, int midBind, int totalBind, int bindDiff,
            int diffTimeWork, DivergenceTimeOfDaily divTime, PremiumTimeOfDailyPerformance premiumTime) {
        return new ActualWorkingTimeOfDaily(new AttendanceTime(bindDiff),
                new ConstraintTime(new AttendanceTime(midBind), new AttendanceTime(totalBind)),
                new AttendanceTime(diffTimeWork), totalWorkTime, divTime, premiumTime);
    }
    
    public static ActualWorkingTimeOfDaily of(AttendanceTime constraintDiffTime, ConstraintTime constraintTime,
			AttendanceTime timeDiff, TotalWorkingTime totalWorkingTime, DivergenceTimeOfDaily divTime,
			PremiumTimeOfDailyPerformance premiumTime) {
    	return new ActualWorkingTimeOfDaily(constraintDiffTime,constraintTime,
    										timeDiff,totalWorkingTime,divTime,premiumTime);
    }
    
    public ActualWorkingTimeOfDaily inssertTotalWorkingTime(TotalWorkingTime time) {
    	return new ActualWorkingTimeOfDaily(this.constraintDifferenceTime,this.constraintTime,this.timeDifferenceWorkingHours,time,this.divTime,this.premiumTimeOfDailyPerformance);
    }
    
	/**
	 * 日別実績の実働時間の計算
	 * @param workScheduleTime 
	 * @param breakTimeCount 
	 * @param schePreTimeSet 
	 * @param schePreTimeSet 
	 * @param ootsukaFixedCalcSet 
	 * @param integrationOfDaily 
	 * @param dailyUnit 
	 * @param workScheduleTime 
	 * @param flexSetting 
	 */
	public static ActualWorkingTimeOfDaily calcRecordTime(ManageReGetClass recordClass,
			   VacationClass vacationClass,
			   WorkType workType,
		       Optional<WorkTimeDailyAtr> workTimeDailyAtr,
			   Optional<SettingOfFlexWork> flexCalcMethod,
			   BonusPayAutoCalcSet bonusPayAutoCalcSet,
			   List<CompensatoryOccurrenceSetting> eachCompanyTimeSet,
			   DailyRecordToAttendanceItemConverter forCalcDivergenceDto,
			   List<DivergenceTime> divergenceTimeList, 
			   WorkingConditionItem conditionItem,
			   Optional<PredetermineTimeSetForCalc> predetermineTimeSetByPersonInfo,
			   DeductLeaveEarly leaveLateSet, WorkScheduleTimeOfDaily workScheduleTime) {

		
		/* 総労働時間の計算 */
		val totalWorkingTime = TotalWorkingTime.calcAllDailyRecord(recordClass,
				    vacationClass,
				    workType,
				    workTimeDailyAtr,
				    flexCalcMethod,
					bonusPayAutoCalcSet,
					eachCompanyTimeSet,
					conditionItem,
					predetermineTimeSetByPersonInfo,
					leaveLateSet
					);
		/*ログ差し込み*/
		org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ActualWorkingTimeOfDaily.class);
		log.info("計算後、大塚処理前の残業値を出力します。");
		if(totalWorkingTime.getExcessOfStatutoryTimeOfDaily().getOverTimeWork() != null
		&& totalWorkingTime.getExcessOfStatutoryTimeOfDaily().getOverTimeWork().isPresent()) {
			for(OverTimeFrameTime otFrame : totalWorkingTime.getExcessOfStatutoryTimeOfDaily().getOverTimeWork().get().getOverTimeWorkFrameTime()) {
				log.info("枠Ｎｏ："+otFrame.getOverWorkFrameNo());
				log.info("残業時間："+otFrame.getOverTimeWork().getTime());
				log.info("計算残業時間："+otFrame.getOverTimeWork().getCalcTime());
			}
		}
		/*ログ差し込み*/
		
		
		TotalWorkingTime calcResultOotsuka;
		if(workType.getDailyWork().decisionMatchWorkType(WorkTypeClassification.SpecialHoliday).isFullTime()) {
			//大塚モード(特休時計算)
			calcResultOotsuka = totalWorkingTime.SpecialHolidayCalculationForOotsuka(recordClass,
				    vacationClass,
				    workType,
				    workTimeDailyAtr,
				    flexCalcMethod,
					bonusPayAutoCalcSet,
					eachCompanyTimeSet,
					conditionItem,
					predetermineTimeSetByPersonInfo,
					leaveLateSet);
		}
		else {
			/*大塚残業*/
			calcResultOotsuka = calcOotsuka(recordClass,
											totalWorkingTime,
											workType,
											workScheduleTime.getRecordPrescribedLaborTime());
		}
		

		
		/*大塚モードの計算（欠勤控除時間）*/
		//1日出勤系の場合は処理を呼ばないように作成が必要
		if(workType.getDailyWork().decisionNeedPredTime() != AttendanceHolidayAttr.FULL_TIME && recordClass.getCalculatable()) {
			//大塚モード休憩未取得
			calcResultOotsuka = calcResultOotsuka.reCalcLateLeave(recordClass.getWorkTimezoneCommonSet(),
					  recordClass.getFixRestTimeSetting(),
					  recordClass.getFixWoSetting(),
					  recordClass.getIntegrationOfDaily().getAttendanceLeave());	

		}
		
		/*拘束差異時間*/
		val constraintDifferenceTime = new AttendanceTime(0);
		/*拘異時間*/
		val constraintTime = new ConstraintTime(new AttendanceTime(0), new AttendanceTime(0));
		/* 時差勤務時間*/
		val timeDifferenceWorkingHours = new AttendanceTime(0);
		
		/* 割増時間の計算 */
		val premiumTime = new PremiumTimeOfDailyPerformance(Collections.emptyList());
		/* 乖離時間の計算 */
//		val divergenceTimeOfDaily = createDivergenceTimeOfDaily(
//													   forCalcDivergenceDto,
//													   divergenceTimeList,
//													   recordClass.getIntegrationOfDaily().getCalAttr(),
//													   recordClass.getFixRestTimeSetting(),
//													   calcResultOotsuka
//													   );
//		DivergenceTimeOfDaily divergenceTimeOfDaily = new DivergenceTimeOfDaily(Collections.emptyList());
		
		/*返値*/
		return new ActualWorkingTimeOfDaily(
				constraintDifferenceTime,
				constraintTime,
				timeDifferenceWorkingHours,
				calcResultOotsuka,
				new DivergenceTimeOfDaily(new ArrayList<>()),
				premiumTime
				);
		
	}
	
	
	public static PremiumTimeOfDailyPerformance createPremiumTimeOfDailyPerformance(List<PersonnelCostSettingImport> personnelCostSettingImport,
	 																				Optional<DailyRecordToAttendanceItemConverter> dailyRecordDto) {
		return PremiumTimeOfDailyPerformance.calcPremiumTime(personnelCostSettingImport, dailyRecordDto);
	}
	
	public static DivergenceTimeOfDaily createDivergenceTimeOfDaily(
			DailyRecordToAttendanceItemConverter forCalcDivergenceDto,
			List<DivergenceTime> divergenceTimeList,CalAttrOfDailyPerformance calcAtrOfDaily,
			Optional<FixRestTimezoneSet> breakTimeSheets, TotalWorkingTime calcResultOotsuka) {
		
		val returnList = calcDivergenceTime(forCalcDivergenceDto, divergenceTimeList,calcAtrOfDaily,breakTimeSheets,calcResultOotsuka);
		//returnする
		return new DivergenceTimeOfDaily(returnList);
	}

	/**
	 * 乖離時間の計算 
	 * @param calcAtrOfDaily
	 * @param calcResultOotsuka 
	 * @param optional 就業時間帯(マスタ)側の休憩時間帯 
	 * @return
	 */
	private static List<nts.uk.ctx.at.record.dom.divergencetimeofdaily.DivergenceTime>   calcDivergenceTime(DailyRecordToAttendanceItemConverter forCalcDivergenceDto,List<DivergenceTime> divergenceTimeList,
			 																								CalAttrOfDailyPerformance calcAtrOfDaily,Optional<FixRestTimezoneSet> breakTimeSheets, TotalWorkingTime calcResultOotsuka) {
		val integrationOfDailyInDto = forCalcDivergenceDto.toDomain();
		if(integrationOfDailyInDto == null
			|| integrationOfDailyInDto.getAttendanceTimeOfDailyPerformance() == null
			|| !integrationOfDailyInDto.getAttendanceTimeOfDailyPerformance().isPresent()
			|| integrationOfDailyInDto.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily() == null)
			return Collections.emptyList();
		
		List<nts.uk.ctx.at.record.dom.divergencetimeofdaily.DivergenceTime> divergenceTime = new ArrayList<>();
		
		DivergenceTimeOfDaily div_time = integrationOfDailyInDto.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getDivTime();
		for(int i=0 ; i<10 ;i++) {
			String reasonContent = null;
			String reasonCode = null;
			
			int div_index = i+1;
			if(div_time != null) {
				List<nts.uk.ctx.at.record.dom.divergencetimeofdaily.DivergenceTime> obj
					= div_time.getDivergenceTime().stream().filter(c->c.getDivTimeId()==div_index).collect(Collectors.toList());
				
				if(!obj.isEmpty()) {
					if(obj.get(0).getDivReason().isPresent()) reasonContent = obj.get(0).getDivReason().get().v();
					if(obj.get(0).getDivResonCode().isPresent()) reasonCode = obj.get(0).getDivResonCode().get().v();
				}
					
			}
			
			nts.uk.ctx.at.record.dom.divergencetimeofdaily.DivergenceTime obj = new nts.uk.ctx.at.record.dom.divergencetimeofdaily.DivergenceTime(
					new AttendanceTime(0),
					new AttendanceTime(0),
					new AttendanceTime(0),
					div_index,
					reasonContent == null ? null : new DivergenceReasonContent(reasonContent),
					reasonCode == null ? null : new DiverdenceReasonCode(reasonCode));
			
			divergenceTime.add(obj);
		}
		//自動計算設定で使用しないであれば空を戻す
		if(!calcAtrOfDaily.getDivergenceTime().getDivergenceTime().isUse())
			return divergenceTime;
		
		val divergenceTimeInIntegrationOfDaily = new DivergenceTimeOfDaily(divergenceTime);
		val returnList = new ArrayList<nts.uk.ctx.at.record.dom.divergencetimeofdaily.DivergenceTime>(); 
		//乖離時間算出のアルゴリズム実装
		for(DivergenceTime divergenceTimeClass : divergenceTimeList) {
			if(divergenceTimeClass.getDivTimeUseSet().isUse()) {
				divergenceTimeInIntegrationOfDaily.getDivergenceTime().stream()
										.filter(tc -> tc.getDivTimeId() == divergenceTimeClass.getDivergenceTimeNo())
										.findFirst().ifPresent(tdi -> {
											int totalTime = 0;
											int deductionTime = (tdi.getDeductionTime() == null ?  0 : tdi.getDeductionTime().valueAsMinutes());
											if(tdi.getDivTimeId() <=7) {
												totalTime = divergenceTimeClass.totalDivergenceTimeWithAttendanceItemId(forCalcDivergenceDto);
											}
											//大塚ｶｽﾀﾏｲｽﾞ(乖離No8～10は別の処理をさせる
											else {
												totalTime = calcDivergenceNo8910(tdi,integrationOfDailyInDto,breakTimeSheets,calcResultOotsuka);
											}
											returnList.add(new nts.uk.ctx.at.record.dom.divergencetimeofdaily.DivergenceTime(new AttendanceTime(totalTime - deductionTime), 
													tdi.getDeductionTime(), 
													new AttendanceTime(totalTime), 
													tdi.getDivTimeId(), 
											 		tdi.getDivReason(), 
											 		tdi.getDivResonCode()));
										});
			}
		}
		return (returnList.size()>0)?returnList:divergenceTimeInIntegrationOfDaily.getDivergenceTime();
	}

	/**
	 * エラーチェックの指示メソッド 
	 * @param attendanceItemConverter 
	 * @return 社員のエラーチェック一覧
	 */
	public List<EmployeeDailyPerError> requestCheckError(String employeeId,GeneralDate targetDate,
			   											 SystemFixedErrorAlarm fixedErrorAlarmCode,
			   											 CheckExcessAtr checkAtr) {
		return this.getTotalWorkingTime().checkOverTimeExcess(employeeId, targetDate, fixedErrorAlarmCode, checkAtr);
	}
	/////****************************************************:大塚専用:********************************************************/////
	/**
	 * 大塚モードの計算(休憩未取得)
	 * @param workingSystem 
	 * @param totalWorkingTime
	 * @param fixRestTimeSetting 固定休憩時間の時間帯設定
	 * @param predetermineTime 所定時間
	 * @param dailyUnit 
	 * @param timeLeavingOfDailyPerformance 
	 * @param workType 
	 * @param attendanceTime 
	 * @return
	 */
	private static TotalWorkingTime calcOotsuka(ManageReGetClass recordClass, 
									TotalWorkingTime totalWorkingTime,
									WorkType workType, 
									AttendanceTime acutualPredTime
									) {
		if(!recordClass.getCalculatable() || recordClass.getIntegrationOfDaily().getAttendanceLeave() == null || !recordClass.getIntegrationOfDaily().getAttendanceLeave().isPresent()) return totalWorkingTime;
		if((recordClass.getPersonalInfo().getWorkingSystem().isRegularWork() || recordClass.getPersonalInfo().getWorkingSystem().isVariableWorkingTimeWork())&&recordClass.getOotsukaFixedWorkSet().isPresent()&& !workType.getDailyWork().isHolidayWork()) {
			//休憩未取得時間の計算
			AttendanceTime unUseBreakTime =
					recordClass.getPersonalInfo().getWorkingSystem().isRegularWork() ?
							totalWorkingTime.getBreakTimeOfDaily().calcUnUseBrekeTime(
									recordClass.getFixRestTimeSetting().get(),
									recordClass.getFixWoSetting(),
									recordClass.getIntegrationOfDaily().getAttendanceLeave().get()) 
							: new AttendanceTime(0);
			unUseBreakTime = unUseBreakTime.greaterThan(0)?unUseBreakTime:new AttendanceTime(0);
			//所てない休憩未取得時間を算出する際に使用する所定時間に加算する時間(就業時間帯マスタに設定されている所定内の休憩時間の合計)
			int withinBreakTime = 0;
			//就業時間帯に設定されている休憩のループ
			for(DeductionTime breakTImeSheet : recordClass.getFixRestTimeSetting().get().getLstTimezone()) {
				//就業時間帯に設定されている勤務時間帯のstream
				withinBreakTime += recordClass.getFixWoSetting().stream().filter(tc -> tc.getTimezone().isOverlap(breakTImeSheet))
													  .map(tt -> tt.getTimezone().getDuplicatedWith(breakTImeSheet.timeSpan()).get().lengthAsMinutes())
													  .collect(Collectors.summingInt(ts -> ts));
					
				
			}
			//所定内休憩未取得時間の計算
			AttendanceTime unUseWithinBreakTime = totalWorkingTime.getWithinStatutoryTimeOfDaily().calcUnUseWithinBreakTime(unUseBreakTime,acutualPredTime,new AttendanceTime(withinBreakTime));
			//所定外休憩未取得時間
			AttendanceTime unUseExcessBreakTime = unUseBreakTime.minusMinutes(unUseWithinBreakTime.valueAsMinutes());
			
									
			
			//日別実績の総労働からとってくる
			AttendanceTime vacationAddTime = totalWorkingTime.getVacationAddTime();
			//残業時間
			if(totalWorkingTime.getExcessOfStatutoryTimeOfDaily().getOverTimeWork().isPresent()) {
				//休憩未取得時間から残業時間計算
				totalWorkingTime.getExcessOfStatutoryTimeOfDaily().getOverTimeWork().get().calcOotsukaOverTime(
						totalWorkingTime.getWithinStatutoryTimeOfDaily().getActualWorkTime(),
						unUseExcessBreakTime,
						vacationAddTime,/*休暇加算時間*/
						recordClass.getCalculationRangeOfOneDay().getPredetermineTimeSetForCalc().getpredetermineTime(workType.getDailyWork()),
						recordClass.getOotsukaFixedWorkSet(),
						recordClass.getIntegrationOfDaily().getCalAttr().getOvertimeSetting(),
						recordClass.getDailyUnit(),
						recordClass.getFixRestTimeSetting(),
						recordClass.getCalculationRangeOfOneDay().getWithinWorkingTimeSheet(),
						recordClass.getCalculationRangeOfOneDay().getPredetermineTimeSetForCalc()
						);
				
			}
			
			//就業時間から休憩未取得時間を減算(休憩未取得を残業時間として計算する　であれば差し引く)
			if(recordClass.getOotsukaFixedWorkSet() != null
			   && recordClass.getOotsukaFixedWorkSet().isPresent()
			   && recordClass.getOotsukaFixedWorkSet().get().getOverTimeCalcNoBreak() != null
			   && recordClass.getOotsukaFixedWorkSet().get().getOverTimeCalcNoBreak().getCalcMethod() != null
			   && !recordClass.getOotsukaFixedWorkSet().get().getOverTimeCalcNoBreak().getCalcMethod().isCalcAsWorking() ) {
				//totalWorkingTime.getWithinStatutoryTimeOfDaily().workTimeMinusUnUseBreakTimeForOotsuka(unUseBreakTime);
				totalWorkingTime.getWithinStatutoryTimeOfDaily().workTimeMinusUnUseBreakTimeForOotsuka(unUseExcessBreakTime);
			}
			
			//休暇加算を残業として計算する場合、ロジックの関係上、就業時間計算時に休暇加算が合算されてしまう
			//ここでは、合算されてしまっている休暇加算を差し引いている
			if(recordClass.getOotsukaFixedWorkSet() != null && recordClass.getOotsukaFixedWorkSet().isPresent() ) {
				if(recordClass.getOotsukaFixedWorkSet().get().getExceededPredAddVacationCalc().getCalcMethod() != null
					 && recordClass.getOotsukaFixedWorkSet().get().getExceededPredAddVacationCalc().getCalcMethod().isCalcAsOverTime()
					 && totalWorkingTime.getWithinStatutoryTimeOfDaily().getWorkTime().greaterThan(recordClass.getCalculationRangeOfOneDay().getPredetermineTimeSetForCalc().getAdditionSet().getPredTime().getOneDay().valueAsMinutes())) {
					totalWorkingTime.setWithinWorkTime(recordClass.getCalculationRangeOfOneDay().getPredetermineTimeSetForCalc().getAdditionSet().getPredTime().getOneDay());
				}
			}

		}
		return totalWorkingTime;
	}

	/**
	 * 大塚カスタマイズ　乖離No 8、9、１０のみ別のチェックをさせる
	 * @param integrationOfDailyInDto 日別実績(WORK)実績計算済み 
	 * @param tdi  
	 * @param calcResultOotsuka 
	 * @param breakList 就業時間帯側の休憩リスト
	 * @param breakOfDaily 
	 */
	public static int calcDivergenceNo8910(nts.uk.ctx.at.record.dom.divergencetimeofdaily.DivergenceTime tdi, IntegrationOfDaily integrationOfDailyInDto,Optional<FixRestTimezoneSet> masterBreakList, 
										   TotalWorkingTime calcResultOotsuka) {
		//実績がそもそも存在しない(不正)の場合
		if(!integrationOfDailyInDto.getAttendanceTimeOfDailyPerformance().isPresent()
		 ||!masterBreakList.isPresent()) 
			return 0;
		val breakOfDaily = calcResultOotsuka.getBreakTimeOfDaily();
		//就業時間帯側から実績の休憩時間帯への変換
		val breakList = BreakTimeSheet.covertFromFixRestTimezoneSet(masterBreakList.get().getLstTimezone());
		
		switch(tdi.getDivTimeId()) {
		case 8:
			return processNumberEight(integrationOfDailyInDto, breakList, breakOfDaily);
		case 9:
			return processNumberNight(integrationOfDailyInDto, breakList, breakOfDaily,calcResultOotsuka);
		case 10:
			return processNumberTen(integrationOfDailyInDto, breakList, breakOfDaily);
		default:
			throw new RuntimeException("exception divergence No:"+tdi.getDivTimeId());
		}
	}
	/**
	 * 乖離No８に対する処理
	 */
	public static int processNumberEight(IntegrationOfDaily integrationOfDailyInDto,List<BreakTimeSheet> breakList,BreakTimeOfDaily breakOfDaily) {
		//休憩枠No1取得
		Optional<BreakTimeSheet> breakTimeSheet = breakList.stream().filter(tc -> tc.getBreakFrameNo().v() == 1).findFirst();
		if(!breakTimeSheet.isPresent()) return 0;
		//出退勤取得
		TimeSpanForCalc attendanceLeave = new TimeSpanForCalc(new TimeWithDayAttr(0), new TimeWithDayAttr(0));
		if(integrationOfDailyInDto.getAttendanceLeave().isPresent()) {
			val attendanceTimeByWorkNo = integrationOfDailyInDto.getAttendanceLeave().get().getAttendanceLeavingWork(1);
			if(attendanceTimeByWorkNo.isPresent()) {
				attendanceLeave = attendanceTimeByWorkNo.get().getTimespan();
			}
		}
		//出退勤が休憩No1を含んでいるか
		if(attendanceLeave.contains(new TimeSpanForCalc(breakTimeSheet.get().getStartTime(),breakTimeSheet.get().getEndTime()))) {
			val calcValue = breakOfDaily.getToRecordTotalTime().getWithinStatutoryTotalTime().getCalcTime().minusMinutes(new TimeSpanForCalc(breakTimeSheet.get().getStartTime(),breakTimeSheet.get().getEndTime()).lengthAsMinutes());
			return calcValue.greaterThan(0)?calcValue.valueAsMinutes():0;
		}
		//含んでいない
		else {
			//乖離時間0
			return 0;
		}
	}
	public static int processNumberNight(IntegrationOfDaily integrationOfDailyInDto,List<BreakTimeSheet> breakList,BreakTimeOfDaily breakOfDaily, TotalWorkingTime calcResultOotsuka) {
		//休憩枠No2取得
		Optional<BreakTimeSheet> breakTimeSheet = breakList.stream().filter(tc -> tc.getBreakFrameNo().v() == 2).findFirst();
		if(!breakTimeSheet.isPresent()) return 0;
		//出退勤取得
		//TimeSpanForCalc attendanceLeave = new TimeSpanForCalc(new TimeWithDayAttr(0), new TimeWithDayAttr(0));
		if(integrationOfDailyInDto.getAttendanceLeave().isPresent()) {
			val attendanceTimeByWorkNo = integrationOfDailyInDto.getAttendanceLeave().get().getAttendanceLeavingWork(1);
			if(attendanceTimeByWorkNo.isPresent()) {
			//	attendanceLeave = attendanceTimeByWorkNo.get().getTimespan();
			}
		}
		//実働時間 > 8:00 && 残業合計(振替残業含む)>0
		if(calcResultOotsuka.getActualTime().greaterThan(480) && calcResultOotsuka.getExcessOfStatutoryTimeOfDaily().calcOverTime().greaterThan(0)) {
			//出退勤が休憩No2を含んでいるか
			val calcValue = new AttendanceTime(breakTimeSheet.get().getEndTime().valueAsMinutes() - breakTimeSheet.get().getStartTime().valueAsMinutes())
										.minusMinutes(breakOfDaily.getToRecordTotalTime().getExcessOfStatutoryTotalTime().getCalcTime().valueAsMinutes());
			return calcValue.greaterThan(0)?calcValue.valueAsMinutes():0;
		}
		//含んでいない
		else {
		//乖離時間0
		return 0;
		}
	}
	
	public static int processNumberTen(IntegrationOfDaily integrationOfDailyInDto,List<BreakTimeSheet> breakList,BreakTimeOfDaily breakOfDaily) {
		//休憩枠No2取得
		Optional<BreakTimeSheet> breakTimeSheet = breakList.stream().filter(tc -> tc.getBreakFrameNo().v() == 2).findFirst();
		if(!breakTimeSheet.isPresent()) return 0;
		//出退勤取得
		TimeSpanForCalc attendanceLeave = new TimeSpanForCalc(new TimeWithDayAttr(0), new TimeWithDayAttr(0));
		if(integrationOfDailyInDto.getAttendanceLeave().isPresent()) {
			val attendanceTimeByWorkNo = integrationOfDailyInDto.getAttendanceLeave().get().getAttendanceLeavingWork(1);
			if(attendanceTimeByWorkNo.isPresent()) {
				attendanceLeave = attendanceTimeByWorkNo.get().getTimespan();
			}
		}
		//休憩終了 <= 退勤
		if(attendanceLeave.getEnd().greaterThan(breakTimeSheet.get().getEndTime())) {
			Optional<BreakTimeSheet> equalTimeSheet = Optional.empty();
			for(BreakTimeOfDailyPerformance masterBreakTimeSheet: integrationOfDailyInDto.getBreakTime()) {
				if(masterBreakTimeSheet.getBreakType().isReferWorkTime()) {
					equalTimeSheet = masterBreakTimeSheet.getBreakTimeSheets().stream()
																			  .filter(ts -> ts.getStartTime() != null && ts.getEndTime() != null)
																			  .filter(tc -> new TimeSpanForCalc(tc.getStartTime(),tc.getEndTime())
																					  					.contains(new TimeSpanForCalc(breakTimeSheet.get().getStartTime(),breakTimeSheet.get().getEndTime()))).findFirst();
				}
			}
			if(equalTimeSheet.isPresent()) {
				//取得した休憩と予定休憩の時間帯が同じ
				return 0;
			}
			//該当した物が一つもない
			else {
				val difference = breakTimeSheet.get().getEndTime().valueAsMinutes() - breakTimeSheet.get().getStartTime().valueAsMinutes();
				return difference>0?difference:0;
			}
		}
		//含んでいない
		else {
			//乖離時間0
			return 0;
		}
	}
	
	/////****************************************************:大塚専用:********************************************************/////
	public static ActualWorkingTimeOfDaily defaultValue(){
		return new ActualWorkingTimeOfDaily(AttendanceTime.ZERO, ConstraintTime.defaultValue(), AttendanceTime.ZERO, 
											TotalWorkingTime.createAllZEROInstance(), 
											new DivergenceTimeOfDaily(new ArrayList<>()), 
											new PremiumTimeOfDailyPerformance(new ArrayList<>()));
	}
}

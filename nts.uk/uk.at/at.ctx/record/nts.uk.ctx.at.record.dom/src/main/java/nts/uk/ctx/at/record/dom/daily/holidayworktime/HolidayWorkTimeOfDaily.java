package nts.uk.ctx.at.record.dom.daily.holidayworktime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import nts.arc.time.GeneralDate;
import nts.gul.util.value.Finally;
import nts.uk.ctx.at.record.dom.calculationattribute.CalAttrOfDailyPerformance;
import nts.uk.ctx.at.record.dom.daily.TimeDivergenceWithCalculation;
import nts.uk.ctx.at.record.dom.daily.bonuspaytime.BonusPayTime;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.AttendanceItemDictionaryForCalc;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.BonusPayAtr;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.DeductionAtr;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.HolidayWorkFrameTimeSheetForCalc;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.HolidayWorkTimeSheet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.ErrorAlarmWorkRecordCode;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalSetting;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.TimeLimitUpperLimitSetting;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensatoryOccurrenceSetting;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.AutoCalRaisingSalarySetting;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.holidaywork.HolidayWorkFrameNo;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.holidaywork.StaturoryAtrOfHolidayWork;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneOtherSubHolTimeSet;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.shr.com.context.AppContexts;

/**
 * 日別実績の休出時間
 * @author keisuke_hoshina
 *
 */
@Getter
@Setter
public class HolidayWorkTimeOfDaily {
	//休出枠時間帯
	private List<HolidayWorkFrameTimeSheet> holidayWorkFrameTimeSheet;
	//休出枠時間
	private List<HolidayWorkFrameTime> holidayWorkFrameTime;
	//休出深夜
	private Finally<HolidayMidnightWork> holidayMidNightWork;
	//休出拘束時間
	private AttendanceTime holidayTimeSpentAtWork;
	
	/**
	 * Constructor 
	 */
	public HolidayWorkTimeOfDaily(List<HolidayWorkFrameTimeSheet> holidayWorkFrameTimeSheet,List<HolidayWorkFrameTime> holidayWorkFrameTime,
								   Finally<HolidayMidnightWork> holidayMidNightWork, AttendanceTime holidayTimeSpentAtWork) {
		this.holidayWorkFrameTimeSheet = holidayWorkFrameTimeSheet;
		this.holidayWorkFrameTime = holidayWorkFrameTime;
		this.holidayMidNightWork = holidayMidNightWork;
		this.holidayTimeSpentAtWork = holidayTimeSpentAtWork;
	}
	

	/**
	 * メンバー変数の時間計算を指示するクラス
	 * @param integrationOfDaily 
	 * @param holidayTimeSheet
	 * @return
	 */
	public static HolidayWorkTimeOfDaily calculationTime(HolidayWorkTimeSheet holidayWorkTimeSheet,
														 AutoCalSetting holidayAutoCalcSetting,
														 WorkType workType,
														 Optional<WorkTimezoneOtherSubHolTimeSet> eachWorkTimeSet,
														 Optional<CompensatoryOccurrenceSetting> eachCompanyTimeSet,
														 IntegrationOfDaily integrationOfDaily,
														 AttendanceTime beforeApplicationTime,
														 AutoCalSetting holidayLateNightAutoCalSetting) {
		//時間帯
		val holidayWorkFrameTimeSheet = holidayWorkTimeSheet.changeHolidayWorkTimeFrameTimeSheet();
		//枠時間
		val holidayWorkFrameTime = holidayWorkTimeSheet.collectHolidayWorkTime(holidayAutoCalcSetting,
				 															   workType,
				 															   eachWorkTimeSet,
				 															   eachCompanyTimeSet,
				 															   integrationOfDaily);
		//深夜
		//holMidNightTime.add(new HolidayWorkMidNightTime(TimeWithCalculation.sameTime(new AttendanceTime(0)), StaturoryAtrOfHolidayWork.WithinPrescribedHolidayWork));
		val holidayMidnightWork = Finally.of(calcMidNightTimeIncludeHolidayWorkTime(holidayWorkTimeSheet,beforeApplicationTime,holidayLateNightAutoCalSetting));
		//使用時間
		val holidayTimeSpentTime = new AttendanceTime(0);
		return new HolidayWorkTimeOfDaily(holidayWorkFrameTimeSheet,
										  holidayWorkFrameTime,
										  holidayMidnightWork,
										  holidayTimeSpentTime);
	}
	
	/**
	 * 休出時間に含まれている加給時間帯を計算する
	 * @return　加給時間クラス
	 */
	public List<BonusPayTime> calcBonusPay(AutoCalRaisingSalarySetting bonusPayAutoCalcSet,BonusPayAtr bonusPayAtr,CalAttrOfDailyPerformance calcAtrOfDaily){
		List<BonusPayTime> bonusPayList = new ArrayList<>();
//		for(HolidayWorkFrameTimeSheet frameTimeSheet: holidayWorkFrameTimeSheet) {
//			//bonusPayList.addAll(frameTimeSheet.calcBonusPay(ActualWorkTimeSheetAtr.HolidayWork,bonusPayAutoCalcSet,calcAtrOfDaily));
//		}
		return bonusPayList;
	}
	
	/**
	 * 休出時間に含まれている特定日加給時間帯を計算する
	 * @return　加給時間クラス
	 */
	public List<BonusPayTime> calcSpecifiedBonusPay(AutoCalRaisingSalarySetting bonusPayAutoCalcSet,BonusPayAtr bonusPayAtr,CalAttrOfDailyPerformance calcAtrOfDaily){
		List<BonusPayTime> bonusPayList = new ArrayList<>();
//		for(HolidayWorkFrameTimeSheet frameTimeSheet: holidayWorkFrameTimeSheet) {
//			//bonusPayList.addAll(frameTimeSheet.calcSpacifiedBonusPay(ActualWorkTimeSheetAtr.HolidayWork,bonusPayAutoCalcSet,calcAtrOfDaily));
//		}
		return bonusPayList;
	}
	/**
	 * 休出時間が含んでいる深夜時間の算出
	 * @return
	 */
	public static HolidayMidnightWork calcMidNightTimeIncludeHolidayWorkTime(HolidayWorkTimeSheet holidayWorkTimeSheet,
																			 AttendanceTime beforeApplicationTime,
																			 AutoCalSetting holidayLateNightAutoCalSetting) {
		EachStatutoryHolidayWorkTime eachTime = new EachStatutoryHolidayWorkTime();
		for(HolidayWorkFrameTimeSheetForCalc  frameTime : holidayWorkTimeSheet.getWorkHolidayTime()) {
			if(frameTime.getMidNightTimeSheet().isPresent()) {
				eachTime.addTime(frameTime.getStatutoryAtr().get(), holidayLateNightAutoCalSetting.getCalAtr().isCalculateEmbossing()?frameTime.getMidNightTimeSheet().get().calcTotalTime(DeductionAtr.Appropriate):new AttendanceTime(0));
			}
		}
		List<HolidayWorkMidNightTime> holidayWorkList = new ArrayList<>();
		holidayWorkList.add(new HolidayWorkMidNightTime(TimeDivergenceWithCalculation.sameTime(eachTime.getStatutory()),StaturoryAtrOfHolidayWork.WithinPrescribedHolidayWork));
		holidayWorkList.add(new HolidayWorkMidNightTime(TimeDivergenceWithCalculation.sameTime(eachTime.getExcess()),StaturoryAtrOfHolidayWork.ExcessOfStatutoryHolidayWork));
		holidayWorkList.add(new HolidayWorkMidNightTime(TimeDivergenceWithCalculation.sameTime(eachTime.getPublicholiday()),StaturoryAtrOfHolidayWork.PublicHolidayWork));
		
		//事前制御の設定を確認
		if(holidayLateNightAutoCalSetting.getUpLimitORtSet()==TimeLimitUpperLimitSetting.LIMITNUMBERAPPLICATION){
			//事前申請制御
			for(HolidayWorkMidNightTime holidayWorkMidNightTime:holidayWorkList) {
				if(holidayWorkMidNightTime.getTime().getTime().greaterThanOrEqualTo(beforeApplicationTime.valueAsMinutes())) {
					TimeDivergenceWithCalculation time = TimeDivergenceWithCalculation.createTimeWithCalculation(beforeApplicationTime, holidayWorkMidNightTime.getTime().getCalcTime());
					holidayWorkMidNightTime.reCreate(time);
				}
			}
		}	
		return new HolidayMidnightWork(holidayWorkList);
	}
	
	/**
	 * 全枠の休出時間の合計の算出
	 * @return　休出時間
	 */
	public int calcTotalFrameTime() {
		int totalTime = 0;
		for(HolidayWorkFrameTime holidayWorkFrameTime : holidayWorkFrameTime) {
			totalTime += holidayWorkFrameTime.getHolidayWorkTime().get().getTime().valueAsMinutes();
		}
		return totalTime;
	}
	
	/**
	 * 全枠の振替休出時間の合計の算出
	 * @return　休出時間
	 */
	public int calcTransTotalFrameTime() {
		int transTotalTime = 0;
		for(HolidayWorkFrameTime holidayWorkFrameTime : holidayWorkFrameTime) {
			transTotalTime += holidayWorkFrameTime.getTransferTime().get().getTime().valueAsMinutes();
		}
		return transTotalTime ;
	}
	

	/**
	 * 休出時間 実績超過チェック
	 * @return　社員のエラー一覧
	 */
	public List<EmployeeDailyPerError> checkHolidayWorkExcess(String employeeId,
															  GeneralDate targetDate,
															  AttendanceItemDictionaryForCalc attendanceItemDictionary,
															  ErrorAlarmWorkRecordCode errorCode) {
		List<EmployeeDailyPerError> returnErrorList = new ArrayList<>();
		for(HolidayWorkFrameTime frameTime:this.getHolidayWorkFrameTime()) {
			if(frameTime.isOverLimitDivergenceTime()) {
				//休出時間
				attendanceItemDictionary.findId("休出時間"+frameTime.getHolidayFrameNo().v()).ifPresent( itemId -> 
						returnErrorList.add(new EmployeeDailyPerError(AppContexts.user().companyCode(), employeeId, targetDate, errorCode, itemId))
				);
//				//振替時間
//				attendanceItemDictionary.findId("振替時間"+frameTime.getHolidayFrameNo().v()).ifPresent( itemId -> 
//						returnErrorList.add(new EmployeeDailyPerError(AppContexts.user().companyCode(), employeeId, targetDate, errorCode, itemId))
//				);
			}
		}
		return returnErrorList;
	}
	
	/**
	 * 休出事前申請のチェック
	 * @return　社員のエラー一覧
	 */
	public List<EmployeeDailyPerError> checkPreHolidayWorkExcess(String employeeId,
			  												  GeneralDate targetDate,
															  AttendanceItemDictionaryForCalc attendanceItemDictionary,
			  												  ErrorAlarmWorkRecordCode errorCode) {
		List<EmployeeDailyPerError> returnErrorList = new ArrayList<>();
		for(HolidayWorkFrameTime frameTime:this.getHolidayWorkFrameTime()) {
			if(frameTime.isPreOverLimitDivergenceTime()) {
				//休出時間
				attendanceItemDictionary.findId("休出時間"+frameTime.getHolidayFrameNo().v()).ifPresent( itemId -> 
						returnErrorList.add(new EmployeeDailyPerError(AppContexts.user().companyCode(), employeeId, targetDate, errorCode, itemId))
				);
//				//振替時間
//				attendanceItemDictionary.findId("振替時間"+frameTime.getHolidayFrameNo().v()).ifPresent( itemId -> 
//						returnErrorList.add(new EmployeeDailyPerError(AppContexts.user().companyCode(), employeeId, targetDate, errorCode, itemId))
//				);
			}
		}
		return returnErrorList;
	}
	
	/**
	 *　実績の休出深夜超過
	 * @return
	 */
	public List<EmployeeDailyPerError> checkNightTimeExcess(String employeeId,
														   GeneralDate targetDate,
														   AttendanceItemDictionaryForCalc attendanceItemDictionary,
														   ErrorAlarmWorkRecordCode errorCode) {
		List<EmployeeDailyPerError> returnErrorList = new ArrayList<>();
		if(this.getHolidayMidNightWork().isPresent()) {
			returnErrorList.addAll(this.getHolidayMidNightWork().get().getErrorList(employeeId, targetDate, attendanceItemDictionary, errorCode));
		}
		return returnErrorList;
	}
	
	
	/**
	 * 自身の休出枠時間の乖離時間を計算
	 * @return
	 */
	public HolidayWorkTimeOfDaily calcDiverGenceTime() {
		List<HolidayWorkFrameTime> list = new ArrayList<>();
		for(HolidayWorkFrameTime holidayworkFrameTime:this.holidayWorkFrameTime) {
			holidayworkFrameTime.calcDiverGenceTime();
			list.add(holidayworkFrameTime);
		}
		Finally<HolidayMidnightWork> holidayMidnight = this.holidayMidNightWork.isPresent()?Finally.of(this.holidayMidNightWork.get().calcDiverGenceTime()):this.holidayMidNightWork;
		return new HolidayWorkTimeOfDaily(this.holidayWorkFrameTimeSheet,list,holidayMidnight,this.holidayTimeSpentAtWork);
	}
	
	//PCログインログオフから計算した計算時間を入れる(大塚モードのみ)
	public void setPCLogOnValue(Map<HolidayWorkFrameNo, HolidayWorkFrameTime> map) {
		Map<HolidayWorkFrameNo,HolidayWorkFrameTime> changeList = convertHolMap(this.getHolidayWorkFrameTime());
		
		for(int frameNo = 1 ; frameNo<=10 ; frameNo++) {
			//値更新
			if(changeList.containsKey(new HolidayWorkFrameNo(frameNo))) {
				val getframe = changeList.get(new HolidayWorkFrameNo(frameNo)); 
				if(map.containsKey(new HolidayWorkFrameNo(frameNo))) {
					//残業時間の置き換え
					getframe.getHolidayWorkTime().get().replaceTimeAndCalcDiv(map.get(new HolidayWorkFrameNo(frameNo)).getHolidayWorkTime().get().getCalcTime());
					//振替時間の置き換え
					getframe.getTransferTime().get().replaceTimeAndCalcDiv(map.get(new HolidayWorkFrameNo(frameNo)).getTransferTime().get().getCalcTime());
				}
				else {
					//残業時間の置き換え
					getframe.getHolidayWorkTime().get().replaceTimeAndCalcDiv(new AttendanceTime(0));
					//振替時間の置き換え
					getframe.getTransferTime().get().replaceTimeAndCalcDiv(new AttendanceTime(0));
				}
				changeList.remove(new HolidayWorkFrameNo(frameNo));
				changeList.put(new HolidayWorkFrameNo(frameNo), getframe);
			}
			//リストへ追加
			else {
				if(map.containsKey(new HolidayWorkFrameNo(frameNo))) {
					changeList.put(new HolidayWorkFrameNo(frameNo),
							   	   new HolidayWorkFrameTime(new HolidayWorkFrameNo(frameNo),
							   			   				 Finally.of(TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(0),map.get(new HolidayWorkFrameNo(frameNo)).getHolidayWorkTime().get().getCalcTime())),
							   			   				 Finally.of(TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(0),map.get(new HolidayWorkFrameNo(frameNo)).getTransferTime().get().getCalcTime())),
							   			   				 Finally.of(new AttendanceTime(0))));
				}
			}
		}
		
		this.holidayWorkFrameTime = new ArrayList<>(changeList.values());
	}
	
	private Map<HolidayWorkFrameNo,HolidayWorkFrameTime> convertHolMap(List<HolidayWorkFrameTime> holidayWorkFrameTime) {
		Map<HolidayWorkFrameNo,HolidayWorkFrameTime> map= new HashMap<>();
		for(HolidayWorkFrameTime hol : holidayWorkFrameTime) {
			map.put(hol.getHolidayFrameNo(), hol);
		}
		return map;
	}
}

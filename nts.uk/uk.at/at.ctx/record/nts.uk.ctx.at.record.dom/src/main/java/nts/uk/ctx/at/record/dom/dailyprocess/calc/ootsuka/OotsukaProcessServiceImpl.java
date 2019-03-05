package nts.uk.ctx.at.record.dom.dailyprocess.calc.ootsuka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import lombok.Getter;
import lombok.val;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkFrameTime;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkMidNightTime;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.OverTimeFrameTime;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeOfExistMinus;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.holidaywork.HolidayWorkFrameNo;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.overtime.overtimeframe.OverTimeFrameNo;
import nts.uk.ctx.at.shared.dom.worktime.common.HolidayCalculation;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixedWorkCalcSetting;
import nts.uk.ctx.at.shared.dom.worktype.CloseAtr;
import nts.uk.ctx.at.shared.dom.worktype.DailyWork;
import nts.uk.ctx.at.shared.dom.worktype.HolidayAtr;
import nts.uk.ctx.at.shared.dom.worktype.WorkAtr;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeClassification;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeSet;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeSetCheck;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeUnit;

/**
 * 大塚専用処理(実装)
 * @author keisuke_hoshina
 *
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class OotsukaProcessServiceImpl implements OotsukaProcessService{

	private boolean workTypeChangedFromSpecialHoliday = false;
	
	@Override
	public WorkType getOotsukaWorkType(WorkType workType,
									   Optional<FixedWorkCalcSetting> calcMethodOfFixWork,
									   TimeLeavingOfDailyPerformance attendanceLeaving,
									   HolidayCalculation holidayCalculation) {
		if(decisionOotsukaMode(workType,calcMethodOfFixWork,attendanceLeaving,holidayCalculation)) {
			return createOotsukaWorkType(workType);
		}
		else {
			return workType;
		}
	}
	
	/**
	 * 大塚用勤務種類の作成
	 * @param workType マスタから取得した勤務種類
	 * @return 作成した勤務種類
	 */
	private WorkType createOotsukaWorkType(WorkType workType) {
		
		changeWorkTypeFlag(workType);
		
		if(workType.getDailyWork().getWorkTypeUnit().isOneDay()) {			
			val workTypeSet = workType.getWorkTypeSetByAtr(WorkAtr.OneDay);
		
			val dailyWork = new DailyWork(WorkTypeUnit.OneDay, 
									  WorkTypeClassification.Attendance, 
									  workType.getDailyWork().getMorning(), 
									  workType.getDailyWork().getAfternoon());
		
		
			val createWorkType = new WorkType(workType.getCompanyId(), 
									workType.getWorkTypeCode(), 
									workType.getSymbolicName(), 
									workType.getName(), 
									workType.getAbbreviationName(), 
									workType.getMemo(), 
									dailyWork, 
									workType.getDeprecate(), 
									workType.getCalculateMethod());
			WorkTypeSet createWorkTypeSet = new WorkTypeSet(workTypeSet.isPresent()?workTypeSet.get().getCompanyId():workType.getCompanyId(), 
															workTypeSet.isPresent()?workTypeSet.get().getWorkTypeCd():workType.getWorkTypeCode(), 
															workTypeSet.isPresent()?workTypeSet.get().getWorkAtr():WorkAtr.OneDay, 
															workTypeSet.isPresent()?workTypeSet.get().getDigestPublicHd():WorkTypeSetCheck.NO_CHECK, 
															workTypeSet.isPresent()?workTypeSet.get().getHolidayAtr():HolidayAtr.NON_STATUTORY_HOLIDAYS, 
															workTypeSet.isPresent()?workTypeSet.get().getCountHodiday():WorkTypeSetCheck.NO_CHECK, 
															workTypeSet.isPresent()?workTypeSet.get().getCloseAtr():CloseAtr.PRENATAL, 
															workTypeSet.isPresent()?workTypeSet.get().getSumAbsenseNo():0, 
															workTypeSet.isPresent()?workTypeSet.get().getSumSpHodidayNo():0, 
															WorkTypeSetCheck.NO_CHECK, 
															WorkTypeSetCheck.NO_CHECK, 
															workTypeSet.isPresent()?workTypeSet.get().getGenSubHodiday():WorkTypeSetCheck.NO_CHECK,
															WorkTypeSetCheck.NO_CHECK
														);
			createWorkType.addWorkTypeSet(createWorkTypeSet);
			return createWorkType;
		}
		else {
			val workTypeSetMorning = workType.getWorkTypeSetByAtr(WorkAtr.Monring);
			val workTypeSetAfternoon = workType.getWorkTypeSetByAtr(WorkAtr.Afternoon);
			
			val dailyWork = new DailyWork(WorkTypeUnit.MonringAndAfternoon, 
										  workType.getDailyWork().getOneDay(), 
										  WorkTypeClassification.Attendance, 
										  WorkTypeClassification.Attendance);


			val createWorkType = new WorkType(workType.getCompanyId(), 
											  workType.getWorkTypeCode(), 
											  workType.getSymbolicName(), 
											  workType.getName(), 
											  workType.getAbbreviationName(), 
											  workType.getMemo(), 
											  dailyWork, 
											  workType.getDeprecate(), 
											  workType.getCalculateMethod());
			WorkTypeSet createWorkTypeSetMorning = new WorkTypeSet(workTypeSetMorning.isPresent()?workTypeSetMorning.get().getCompanyId():workType.getCompanyId(), 
																   workTypeSetMorning.isPresent()?workTypeSetMorning.get().getWorkTypeCd():workType.getWorkTypeCode(), 
																   workTypeSetMorning.isPresent()?workTypeSetMorning.get().getWorkAtr():WorkAtr.OneDay, 
																   workTypeSetMorning.isPresent()?workTypeSetMorning.get().getDigestPublicHd():WorkTypeSetCheck.NO_CHECK, 
																   workTypeSetMorning.isPresent()?workTypeSetMorning.get().getHolidayAtr():HolidayAtr.NON_STATUTORY_HOLIDAYS, 
																   workTypeSetMorning.isPresent()?workTypeSetMorning.get().getCountHodiday():WorkTypeSetCheck.NO_CHECK, 
																   workTypeSetMorning.isPresent()?workTypeSetMorning.get().getCloseAtr():CloseAtr.PRENATAL, 
																   workTypeSetMorning.isPresent()?workTypeSetMorning.get().getSumAbsenseNo():0, 
																   workTypeSetMorning.isPresent()?workTypeSetMorning.get().getSumSpHodidayNo():0, 
																   WorkTypeSetCheck.NO_CHECK, 
																   WorkTypeSetCheck.NO_CHECK, 
															       workTypeSetMorning.isPresent()?workTypeSetMorning.get().getGenSubHodiday():WorkTypeSetCheck.NO_CHECK,
															       WorkTypeSetCheck.NO_CHECK
				);		
			
			WorkTypeSet createWorkTypeSetAfternoon = new WorkTypeSet(workTypeSetAfternoon.isPresent()?workTypeSetAfternoon.get().getCompanyId():workType.getCompanyId(), 
					   												 workTypeSetAfternoon.isPresent()?workTypeSetAfternoon.get().getWorkTypeCd():workType.getWorkTypeCode(), 
					   												 workTypeSetAfternoon.isPresent()?workTypeSetAfternoon.get().getWorkAtr():WorkAtr.OneDay, 
					   												 workTypeSetAfternoon.isPresent()?workTypeSetAfternoon.get().getDigestPublicHd():WorkTypeSetCheck.NO_CHECK, 
					   												 workTypeSetAfternoon.isPresent()?workTypeSetAfternoon.get().getHolidayAtr():HolidayAtr.NON_STATUTORY_HOLIDAYS, 
					   												 workTypeSetAfternoon.isPresent()?workTypeSetAfternoon.get().getCountHodiday():WorkTypeSetCheck.NO_CHECK, 
					   												 workTypeSetAfternoon.isPresent()?workTypeSetAfternoon.get().getCloseAtr():CloseAtr.PRENATAL, 
					   												 workTypeSetAfternoon.isPresent()?workTypeSetAfternoon.get().getSumAbsenseNo():0, 
					   												 workTypeSetAfternoon.isPresent()?workTypeSetAfternoon.get().getSumSpHodidayNo():0, 
					   												 WorkTypeSetCheck.NO_CHECK, 
					   												 WorkTypeSetCheck.NO_CHECK, 
					   												 workTypeSetAfternoon.isPresent()?workTypeSetAfternoon.get().getGenSubHodiday():WorkTypeSetCheck.NO_CHECK,
					   												 WorkTypeSetCheck.NO_CHECK
				);
			createWorkType.addWorkTypeSet(createWorkTypeSetMorning);
			createWorkType.addWorkTypeSet(createWorkTypeSetAfternoon);
			return createWorkType;
		}
	}

	/**
	 * 内部で保持している変更前の勤務種類が何だったかフラグの変更
	 * @param workType
	 */
	private void changeWorkTypeFlag(WorkType workType) {
		if(workType.getDailyWork().isOneOrHalfDaySpecHoliday())
			workTypeChangedFromSpecialHoliday = true;
	}

	/**
	 * 大塚モード判断処理
	 * @param holidayCalculation 
	 * @return
	 */
	@Override
	public boolean decisionOotsukaMode(WorkType workType,
										Optional<FixedWorkCalcSetting> calcMethodOfFixWork,
										TimeLeavingOfDailyPerformance attendanceLeaving, HolidayCalculation holidayCalculation) {
		//勤務計算をする　＆＆　打刻漏れをしていない
		if(decisionAbleCalc(workType,calcMethodOfFixWork,holidayCalculation) && !attendanceLeaving.isLeakageStamp()) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * 日勤務で計算するかどうか判断
	 * @param workType 勤務種類
	 * @param holidayCalculation 
	 * @param isCalcInVacation 休暇時の計算
	 * @return
	 */
	private boolean decisionAbleCalc(WorkType workType,Optional<FixedWorkCalcSetting> calcMethodOfFixWork, HolidayCalculation holidayCalculation) {
		//休暇時の計算を取得
		if(workType != null && holidayCalculation.getIsCalculate().isUse()) {//calcMethodOfFixWork.isPresent()) {
//			return workType.getDailyWork().isOneOrHalfAnnualHoliday()
//					|| workType.getDailyWork().isOneOrHalfDaySpecHoliday()
//					|| workType.getDailyWork().isOneOrHalfDayYearlyReserved();
			return workType.getDailyWork().decisionNeedPredTime().isHoliday();
		}
		//しない
		else {
			return false;
		}
	}
	
	/**
	 * 大塚モード(PCログオン時間で計算した値の埋め込み)
	 */
	@Override
	public IntegrationOfDaily integrationConverter(IntegrationOfDaily fromStamp, IntegrationOfDaily fromPcLogInfo) {
		if(fromPcLogInfo.getAttendanceTimeOfDailyPerformance().isPresent()
		 &&fromPcLogInfo.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily() != null
		 &&fromPcLogInfo.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime() != null) {

			AttendanceTime withinMidValue = new AttendanceTime(0);//所定内深夜
			//所定内深夜時間え
			if(fromPcLogInfo.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getWithinStatutoryTimeOfDaily() != null) {
				withinMidValue = fromPcLogInfo.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getWithinStatutoryTimeOfDaily().getWithinStatutoryMidNightTime().getTime().getCalcTime();
			}
			//所定内深夜時間置き換え
			fromStamp.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getWithinStatutoryTimeOfDaily().getWithinStatutoryMidNightTime().getTime().replaceTimeAndCalcDiv(withinMidValue);
			//残業、残業振替
			Map<OverTimeFrameNo,OverTimeFrameTime> overmap = new HashMap<>();
			//所定外深夜
			AttendanceTime excessMidNight = new AttendanceTime(0);
			//所定外残業深夜
			AttendanceTime excessOverMidNight = new AttendanceTime(0);
			//フレックス時間
			AttendanceTimeOfExistMinus flexTime = new AttendanceTimeOfExistMinus(0);
			//休出、休出振替
			Map<HolidayWorkFrameNo,HolidayWorkFrameTime> holWorkMap = new HashMap<>();
			Map<String,HolidayWorkMidNightTime> holWorkMid = new HashMap<>();
			
			
			if(fromPcLogInfo.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily() != null) {
				excessMidNight = fromPcLogInfo.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().getExcessOfStatutoryMidNightTime().getTime().getCalcTime();
				//残業時間、振替時間、所定外残業深夜時間、フレックスの置き換え
				if(fromPcLogInfo.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().getOverTimeWork().isPresent()) {
					//残業、振替
					overmap = convertOverMap(fromPcLogInfo.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().getOverTimeWork().get().getOverTimeWorkFrameTime());

					//所定外残業深夜
					if(fromStamp.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().getOverTimeWork().get().getExcessOverTimeWorkMidNightTime().isPresent()) {
						excessOverMidNight = fromPcLogInfo.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().getOverTimeWork().get().getExcessOverTimeWorkMidNightTime().get().getTime().getCalcTime();

					}
					//フレックス時間
					flexTime = fromPcLogInfo.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().getOverTimeWork().get().getFlexTime().getFlexTime().getCalcTime();
				}
				if(fromPcLogInfo.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().getWorkHolidayTime().isPresent()) {
					//休出、振替
					holWorkMap = convertHolMap(fromPcLogInfo.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().getWorkHolidayTime().get().getHolidayWorkFrameTime());
					//休出深夜時間
					if(fromStamp.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().getWorkHolidayTime().get().getHolidayMidNightWork().isPresent()) {
						holWorkMid = convertHolMidMap(fromPcLogInfo.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().getWorkHolidayTime().get().getHolidayMidNightWork().get().getHolidayWorkMidNightTime());
					
					}
				}
			}
			//残業、残業振替
			fromStamp.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().getOverTimeWork().get()
	         .setPCLogOnValue(overmap);
			//所定外深夜
			fromStamp.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().getExcessOfStatutoryMidNightTime().getTime()
			 .replaceTimeAndCalcDiv(excessMidNight);
			//所定外残業深夜
			fromStamp.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().getOverTimeWork().get().getExcessOverTimeWorkMidNightTime().get().getTime()
	         .replaceTimeAndCalcDiv(excessOverMidNight);
			//フレックス時間の置き換え
			fromStamp.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().getOverTimeWork().get().getFlexTime().getFlexTime()
			 .replaceTimeAndCalcDiv(flexTime);
			//休出、休出振替
			fromStamp.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().getWorkHolidayTime().get()
			 .setPCLogOnValue(holWorkMap);
			//休出深夜
			fromStamp.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().getWorkHolidayTime().get().getHolidayMidNightWork().get()
			 .replaceValueBypcLogInfo(holWorkMid);
			
			//ここのみ、現在乖離時間が存在しない 2018.07.05
			if(fromPcLogInfo.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getRaiseSalaryTimeOfDailyPerfor() != null) {
				//加給、所定内加給、所定外加給、特定日加給、所定内特定日加給、所定外特定日加給の置き換え
				fromStamp.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getRaiseSalaryTimeOfDailyPerfor()
						 .replaceValueByPCLogInfo(fromPcLogInfo.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getRaiseSalaryTimeOfDailyPerfor());
			}
		}
		return fromStamp;
	}

	private Map<String, HolidayWorkMidNightTime> convertHolMidMap(List<HolidayWorkMidNightTime> holidayWorkMidNightTime) {
		Map<String, HolidayWorkMidNightTime> map = new HashMap<>();
		for(HolidayWorkMidNightTime hol : holidayWorkMidNightTime) {
			map.put(hol.getStatutoryAtr().toString(), hol);
		}
		return map;
	}

	private Map<HolidayWorkFrameNo,HolidayWorkFrameTime> convertHolMap(List<HolidayWorkFrameTime> holidayWorkFrameTime) {
		Map<HolidayWorkFrameNo,HolidayWorkFrameTime> map= new HashMap<>();
		for(HolidayWorkFrameTime hol : holidayWorkFrameTime) {
			map.put(hol.getHolidayFrameNo(), hol);
		}
		return map;
	}

	private Map<OverTimeFrameNo,OverTimeFrameTime> convertOverMap(List<OverTimeFrameTime> overTimeWorkFrameTime) {
		Map<OverTimeFrameNo,OverTimeFrameTime> map = new HashMap<>();
		for(OverTimeFrameTime ot : overTimeWorkFrameTime) {
			map.put(ot.getOverWorkFrameNo(), ot);
		}
		return map;
	}

	@Override
	public boolean getWorkTypeChangedFromSpecialHoliday() {
		return this.workTypeChangedFromSpecialHoliday;
	}
}

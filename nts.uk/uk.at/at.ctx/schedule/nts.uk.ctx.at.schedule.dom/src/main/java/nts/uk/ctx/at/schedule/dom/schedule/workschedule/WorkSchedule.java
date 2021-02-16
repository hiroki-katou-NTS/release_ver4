package nts.uk.ctx.at.schedule.dom.schedule.workschedule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import nts.arc.error.BusinessException;
import nts.arc.layer.dom.objecttype.DomainAggregate;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.WorkInformation;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.holidaymanagement.publicholiday.configuration.DayOfWeek;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.TimezoneToUseHourlyHoliday;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.affiliationinfor.AffiliationInforOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.attendancetime.TimeLeavingOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.attendancetime.WorkNo;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.breakgoout.BreakFrameNo;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.breakgoout.OutingTimeOfDaily;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.breakouting.OutingTimeOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.breakouting.breaking.BreakTimeOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.breakouting.breaking.BreakTimeSheet;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.earlyleavetime.LeaveEarlyTimeOfDaily;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.editstate.EditStateOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.latetime.LateTimeOfDaily;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.shortworktime.ShortTimeOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.workinfomation.CalculationState;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.workinfomation.NotUseAttribute;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.workinfomation.WorkInfoOfDailyAttendance;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.worktime.AttendanceTimeOfDailyAttendance;
import nts.uk.ctx.at.shared.dom.workrule.goingout.GoingOutReason;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 勤務予定 root
 * UKDesign.ドメインモデル."NittsuSystem.UniversalK".就業.contexts.勤務予定.勤務予定.勤務予定
 * @author HieuLT
 *
 */
@Getter
@Setter
@AllArgsConstructor
public class WorkSchedule implements DomainAggregate {

	/** 社員ID(employeeID) */
	private final String employeeID;

	/** 社員ID(年月日(YMD) */
	private final GeneralDate ymd;

	/** 確定区分 */
	private ConfirmedATR confirmedATR;

	/** 勤務情報 */
	private WorkInfoOfDailyAttendance workInfo;

	/** 所属情報 **/
	private AffiliationInforOfDailyAttd affInfo;
	
	/** 休憩時間帯**/
	@Getter
	private BreakTimeOfDailyAttd lstBreakTime;
	
	/** 編集状態 **/
	private List<EditStateOfDailyAttd> lstEditState;

	/** 出退勤 */
	private Optional<TimeLeavingOfDailyAttd> optTimeLeaving;

	/** 勤怠時間 */
	private Optional<AttendanceTimeOfDailyAttendance> optAttendanceTime;

	/** 短時間勤務 */
	private Optional<ShortTimeOfDailyAttd> optSortTimeWork;

	/** 外出時間帯 */
	private Optional<OutingTimeOfDailyAttd> outingTime;

	/**
	 * TODO 勤務予定に外出時間帯を追加、あとで直す！！
	 * 外出時間帯を追加したことによってコンパイルエラーが発生するため、
	 * 一旦仮で外出時間帯以外を受け付けるコンストラクタを用意。
	 */
//	public WorkSchedule(String sid, GeneralDate date, ConfirmedATR confirmedAtr
//			, WorkInfoOfDailyAttendance workInfo, AffiliationInforOfDailyAttd affInfo
//			, Optional<BreakTimeOfDailyAttd> breakTime, List<EditStateOfDailyAttd> editState
//			, Optional<TimeLeavingOfDailyAttd> timeLeaving, Optional<AttendanceTimeOfDailyAttendance> attendanceTime
//			, Optional<ShortTimeOfDailyAttd> sortTimeWork) {
//
//		this.employeeID = sid;
//		this.ymd = date;
//		this.confirmedATR = confirmedAtr;
//		this.workInfo = workInfo;
//		this.affInfo = affInfo;
//		this.lstBreakTime = breakTime;
//		this.lstEditState = editState;
//		this.optTimeLeaving = timeLeaving;
//		this.optAttendanceTime = attendanceTime;
//		this.optSortTimeWork = sortTimeWork;
//		this.outingTime = Optional.empty();
//	}
	/**
	 * 作る
	 * @param require
	 * @param employeeId 社員ID
	 * @param date 年月日
	 * @param workinformation 勤務情報
	 * @return
	 */
	public static WorkSchedule create(
			Require require,
			String employeeId,
			GeneralDate date,
			WorkInformation workInformation
			){
		
		if (! workInformation.checkNormalCondition(require) ) {
			throw new BusinessException("Msg_430");
		}
			
		return new WorkSchedule(
				employeeId, 
				date, 
				ConfirmedATR.UNSETTLED, 
				WorkInfoOfDailyAttendance.create(
						require, 
						workInformation, 
						CalculationState.No_Calculated, 
						NotUseAttribute.Not_use, 
						NotUseAttribute.Not_use, 
						DayOfWeek.convertFromCommonClass(date.dayOfWeekEnum())), 
				AffiliationInforOfDailyAttd.create(require, employeeId, date), 
				new BreakTimeOfDailyAttd(),
				new ArrayList<>(), 
				Optional.of(TimeLeavingOfDailyAttd.createByPredetermineZone(
						require, 
						workInformation)), 
				Optional.empty(), 
				Optional.empty(), 
				Optional.empty());
	}
	
	/**
	 * 勤務情報を指定して手修正で作る
	 * @param require
	 * @param employeeId 社員ID
	 * @param date 年月日
	 * @param workInformation 勤務情報
	 * @return
	 */
	public static WorkSchedule createByHandCorrectionWithWorkInformation(
			Require require,
			String employeeId,
			GeneralDate date,
			WorkInformation workInformation
			) {
		WorkSchedule workSchedule = WorkSchedule.create(require, employeeId, date, workInformation);
		
		List<EditStateOfDailyAttd> editStateOfDailyAttdList = 
				Arrays.asList(
					WS_AttendanceItem.WorkType,
					WS_AttendanceItem.WorkTime,
					WS_AttendanceItem.StartTime1,
					WS_AttendanceItem.EndTime1,
					WS_AttendanceItem.StartTime2,
					WS_AttendanceItem.EndTime2)
				.stream()
				.map( item -> EditStateOfDailyAttd.createByHandCorrection(require, item.ID, employeeId))
				.collect(Collectors.toList());
		
		workSchedule.setLstEditState(editStateOfDailyAttdList);
		
		return workSchedule;
	}
	
	/**
	 * 出退勤時刻の値を手修正で変更する
	 * 勤怠項目IDを指定して手修正で変更する
	 * @param require
	 * @param updateInfoMap 変更する情報Map
	 */
	public <T> void changeAttendanceItemValueByHandCorrection (
			Require require,
			Map<Integer, T > updateInfoMap) {
		
		updateInfoMap.forEach( (key, value) -> this.updateValueByHandCorrection(require, key, value));
	}
	
	/**
	 * 値の変更
	 * @param updateAttendanceItemID 勤怠項目ID
	 * @param value 値
	 * @return
	 */
	private <T> T updateValue(int updateAttendanceItemID, T value) {
		
		val updateAttendanceItem = WS_AttendanceItem.valueOf(updateAttendanceItemID);
		
		switch (updateAttendanceItem) {
			case StartTime1 :
				this.optTimeLeaving.get()
					.getAttendanceLeavingWork( 1 ).get()
					.getAttendanceStamp().get()
					.getStamp().get()
					.getTimeDay()
					.setTimeWithDay( Optional.of( (TimeWithDayAttr) value) );
				break;

			case EndTime1:
				this.optTimeLeaving.get()
					.getAttendanceLeavingWork( 1 ).get()
					.getLeaveStamp().get()
					.getStamp().get()
					.getTimeDay()
					.setTimeWithDay( Optional.of( (TimeWithDayAttr) value) );
				break;
			case StartTime2:
				this.optTimeLeaving.get()
					.getAttendanceLeavingWork( 2 ).get()
					.getAttendanceStamp().get()
					.getStamp().get()
					.getTimeDay()
					.setTimeWithDay( Optional.of( (TimeWithDayAttr) value) );
				break;

			case EndTime2:
				this.optTimeLeaving.get()
					.getAttendanceLeavingWork( 2 ).get()
					.getLeaveStamp().get()
					.getStamp().get()
					.getTimeDay()
					.setTimeWithDay( Optional.of( (TimeWithDayAttr) value) );
				break;
			case GoStraight:
				this.workInfo.setGoStraightAtr( (NotUseAttribute) value );
				break;
			case BackStraight:
				this.workInfo.setBackStraightAtr( (NotUseAttribute) value );
				break;
			default:
				break;
		}
		
		return value;
	}
	
	/**
	 * 勤怠項目IDに対応する値
	 * @param updateAttendanceItemID 勤怠項目ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <T> T getAttendanceItemValue(int updateAttendanceItemID) {
		
		val updateAttendanceItem = WS_AttendanceItem.valueOf(updateAttendanceItemID);
		
		switch (updateAttendanceItem) {
			case StartTime1:
				return (T) this.optTimeLeaving.get()
						.getAttendanceLeavingWork(1).get()
						.getAttendanceStamp().get()
						.getStamp().get()
						.getTimeDay().getTimeWithDay().get();
			case EndTime1:
				return (T) this.optTimeLeaving.get()
						.getAttendanceLeavingWork(1).get()
						.getLeaveStamp().get()
						.getStamp().get()
						.getTimeDay().getTimeWithDay().get();
			case StartTime2:
				return (T) this.optTimeLeaving.get()
						.getAttendanceLeavingWork(2).get()
						.getAttendanceStamp().get()
						.getStamp().get()
						.getTimeDay().getTimeWithDay().get();
			case EndTime2:
				return (T) this.optTimeLeaving.get()
						.getAttendanceLeavingWork(2).get()
						.getLeaveStamp().get()
						.getStamp().get()
						.getTimeDay().getTimeWithDay().get();
			case GoStraight:
				return (T) this.workInfo.getGoStraightAtr();
			case BackStraight:
				return (T) this.workInfo.getBackStraightAtr();
			default:
				return null;
		}

	}
	/**
	 * 値を手修正で変更
	 * @param require
	 * @param updateAttendanceItemID 
	 * @param value Optional<T> 値
	 * @return
	 * 
	 * note: 勤怠項目が勤務種類と就業時間帯を含まない this attendance item doesn't accept work-type or work-time
	 */
	private <T> T updateValueByHandCorrection(Require require, int updateAttendanceItemID, T value) {
		
		T existValue = this.getAttendanceItemValue(updateAttendanceItemID);
		
		if ( existValue.equals(value) ) {
			return existValue;
		}
		
		lstEditState.removeIf( editState -> editState.getAttendanceItemId() == updateAttendanceItemID);
		lstEditState.add(EditStateOfDailyAttd.createByHandCorrection(require, updateAttendanceItemID, employeeID));
		
		return this.updateValue(updateAttendanceItemID, value);
		
	} 
	
	/**
	 * 確定する
	 */
	public void confirm() {
		this.confirmedATR = ConfirmedATR.CONFIRMED;
	}
	
	/**
	 * 確定を解除する
	 */
	public void removeConfirm() {
		this.confirmedATR = ConfirmedATR.UNSETTLED;
	}
	
	/**
	 * 手修正を解除する
	 */
	public void removeHandCorrections() {
		this.lstEditState.removeIf( editState -> editState.isHandCorrect() );
	}

	/**
	 * 時間休暇を取得する
	 * @return
	 */
	public Map<TimezoneToUseHourlyHoliday, TimeVacation> getTimeVacation() {
		
		/** 日別勤怠の遅刻時間を取得 */
		val lateTimeMap = this.getLateTimes();
		
		/** 日別勤怠の早退時間を取得*/
		val earlyTimeMap = this.getEarlyTimes();
		
		/** 日別勤怠の外出時間を取得 **/
		val outingTimeMap = this.getOutingTimes();
		
		Map<TimezoneToUseHourlyHoliday, TimeVacation> result = new HashMap<>();
		result.putAll( lateTimeMap );
		result.putAll( earlyTimeMap );
		result.putAll( outingTimeMap );
		
		return result;
	}
	
	/**
	 * ［時間休暇を取得する］の［日別勤怠の遅刻時間を取得］
	 * @return
	 */
	private Map<TimezoneToUseHourlyHoliday, TimeVacation> getLateTimes() {
		
		// 勤怠時間 or 出退勤 empty
		if ( !this.optAttendanceTime.isPresent() || !this.optTimeLeaving.isPresent() ) {
			return Collections.emptyMap();
		}
		
		// 遅刻時間を取得する
		val lateTimes = this.optAttendanceTime.get().getLateTimeOfDaily();

		Map<TimezoneToUseHourlyHoliday, TimeVacation> result = new HashMap<>();
		
		for (LateTimeOfDaily lateTime : lateTimes) {
			
			// 出勤の勤務NOを指定
			val lateType = TimezoneToUseHourlyHoliday.getBeforeWorking(WorkNo.converFromOtherWorkNo(lateTime.getWorkNo()));

			// @出退勤.勤務開始の休暇時間帯を取得する
			Optional<TimeSpanForCalc> leavingTimeSpan = this.optTimeLeaving.get().getStartTimeVacations(lateTime.getWorkNo());
			if ( !leavingTimeSpan.isPresent() ) {
				continue;
			}
			
			//時間休暇
			val timeVacation = new TimeVacation( new ArrayList<>( Arrays.asList(leavingTimeSpan.get()) ), lateTime.getTimePaidUseTime() );

			result.put(lateType, timeVacation);
		}
		
		return result;
	}
	
	/**
	 * ［時間休暇を取得する］の［日別勤怠の早退時間を取得］
	 * @return
	 */
	private Map<TimezoneToUseHourlyHoliday, TimeVacation> getEarlyTimes() {
		
		if ( !this.optAttendanceTime.isPresent() || !this.optTimeLeaving.isPresent() ) {
			return Collections.emptyMap();
		}
		
		/** 日別勤怠の早退時間を取得*/
		List<LeaveEarlyTimeOfDaily> earlyTimes = this.optAttendanceTime.get().getLeaveEarlyTimeOfDaily();
		
		Map<TimezoneToUseHourlyHoliday, TimeVacation> result = new HashMap<>();
		
		for(LeaveEarlyTimeOfDaily earlyTime : earlyTimes) {
			
			// 退勤の勤務NOを指定
			val earlyType = TimezoneToUseHourlyHoliday.getAfterWorking(WorkNo.converFromOtherWorkNo(earlyTime.getWorkNo()));
			
			// @出退勤.勤務終了の休暇時間帯を取得する ($.勤務NO)
			Optional<TimeSpanForCalc> leavingTimeSpan = this.optTimeLeaving.get().getEndTimeVacations(earlyTime.getWorkNo());
			if ( !leavingTimeSpan.isPresent() ) {
				continue;
			}
			
			//時間休暇
			val timeVacation = new TimeVacation( new ArrayList<>( Arrays.asList(leavingTimeSpan.get())), earlyTime.getTimePaidUseTime() );
			
			result.put(earlyType, timeVacation);
		}
		
		return result;
		
	}
	
	/**
	 * ［時間休暇を取得する］の［【日別勤怠の外出時間を取得］
	 * @return
	 */
	private Map<TimezoneToUseHourlyHoliday, TimeVacation> getOutingTimes() {
		
		if ( !this.optAttendanceTime.isPresent() || !this.outingTime.isPresent() ) {
			return Collections.emptyMap();
		}
		
		//$外出時間 = @勤怠時間.外出時間を取得する () : filter $.外出理由 in (私用, 組合)
		List<OutingTimeOfDaily> outingTimes = this.optAttendanceTime.get().getOutingTimeOfDaily().stream()
				.filter(c -> c.getReason() == GoingOutReason.PRIVATE || c.getReason() == GoingOutReason.UNION)
				.collect(Collectors.toList());
		
		Map<TimezoneToUseHourlyHoliday, TimeVacation> result = new HashMap<>();
		
		for(OutingTimeOfDaily outingTime : outingTimes) {
			
			val type = TimezoneToUseHourlyHoliday.getDuringWorking(outingTime.getReason());
			
			// 外出理由を指定して時間帯を取得する
			val timeZones = this.outingTime.get().getTimeZoneByGoOutReason(outingTime.getReason());
			val timeLeave = new TimeVacation( timeZones, outingTime.getTimeVacationUseOfDaily() );
			
			result.put(type, timeLeave);
		}
		
		return result;
	}
	
	/**
	 * 休憩時間帯を手修正する
	 * @param require
	 * @param newBreakTimeList 時間帯リスト
	 */
	public void handCorrectBreakTimeList(Require require, List<TimeSpanForCalc> newBreakTimeList) {
		
		List<TimeSpanForCalc> sortedBreakTimeList = newBreakTimeList.stream()
				.sorted(Comparator.comparingInt(TimeSpanForCalc::start))
				.collect(Collectors.toList());;
				
		List<BreakTimeSheet> newBreakTimeSheets = 
			IntStream.range(0, sortedBreakTimeList.size()).boxed()
				.map( index ->
						new BreakTimeSheet(
							new BreakFrameNo(index + 1),
							sortedBreakTimeList.get(index).getStart(),
							sortedBreakTimeList.get(index).getEnd() ))
				.collect(Collectors.toList());
		
		// update value of BreakTime
		this.lstBreakTime = new BreakTimeOfDailyAttd(newBreakTimeSheets);
		
		// update EditState of BreakTime(1...size)
		this.lstEditState.removeIf( editState -> WS_AttendanceItem.isBreakTime( editState.getAttendanceItemId() ) );
		List<WS_AttendanceItem> updatedAttendanceItemList = WS_AttendanceItem.getBreakTimeItemWithSize( newBreakTimeList.size() );
		updatedAttendanceItemList.forEach( item -> this.lstEditState.add(
				EditStateOfDailyAttd.createByHandCorrection(require, item.ID, this.employeeID)));
		// update EditState of BreakTime 休憩時間
		this.lstEditState.add(EditStateOfDailyAttd.createByHandCorrection(require, WS_AttendanceItem.BreakTime.ID, this.employeeID));
	}
	
	public static interface Require extends 
		WorkInfoOfDailyAttendance.Require, 
		AffiliationInforOfDailyAttd.Require,
		TimeLeavingOfDailyAttd.Require, 
		EditStateOfDailyAttd.Require {
	
	}

}

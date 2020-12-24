package nts.uk.ctx.at.schedule.dom.schedule.workschedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import nts.arc.error.BusinessException;
import nts.arc.i18n.I18NText;
import nts.arc.task.tran.AtomTask;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.WorkInformation;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.temporarytime.WorkNo;
import nts.uk.ctx.at.shared.dom.worktime.ChangeableWorkingTimeZonePerNo.ClockAreaAtr;
import nts.uk.ctx.at.shared.dom.worktime.ChangeableWorkingTimeZonePerNo.ContainsResult;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 勤務予定を作る
 * UKDesign.ドメインモデル.NittsuSystem.UniversalK.就業.contexts.勤務予定.勤務予定.勤務予定.勤務予定を作る
 * @author dan_pv
 */
public class CreateWorkSchedule {
	
	/**
	 * 作る
	 * @param require
	 * @param employeeId 社員ID
	 * @param date 年月日
	 * @param workInformation 勤務情報
	 * @param updateInfoMap 変更する情報Map
	 * @return
	 */
	public static <T> ResultOfRegisteringWorkSchedule create(
			Require require, 
			String employeeId, 
			GeneralDate date, 
			WorkInformation workInformation,
			List<TimeSpanForCalc> breakTimeList,
			Map<Integer, T> updateInfoMap) {
		
		Optional<WorkSchedule> registedWorkSchedule = require.getWorkSchedule(employeeId, date);
		boolean isNewRegister = !registedWorkSchedule.isPresent();
		
		WorkSchedule workSchedule;
		if ( isNewRegister || ! registedWorkSchedule.get().getWorkInfo().getRecordInfo().isSame(workInformation) ) {
			try {
				workSchedule = WorkSchedule.createByHandCorrectionWithWorkInformation(require, employeeId, date, workInformation);
			} catch (BusinessException e) {
				if (e.getMessageId().equals("Msg_430")) {
					String message = I18NText.main(e.getMessageId()).build().buildMessage();
					return ResultOfRegisteringWorkSchedule.createWithError(employeeId, date, message);
				}
				
				throw e; // else
			}
		} else {
			workSchedule = registedWorkSchedule.get();
		}
		
		if ( updateInfoMap.containsKey( WS_AttendanceItem.StartTime1.ID ) || 
				updateInfoMap.containsKey( WS_AttendanceItem.EndTime1.ID ) || 
				updateInfoMap.containsKey( WS_AttendanceItem.StartTime2.ID ) || 
				updateInfoMap.containsKey( WS_AttendanceItem.EndTime2.ID) ) {
			
			List<ErrorInfoOfWorkSchedule> errorList = 
					CreateWorkSchedule.checkTimeSpan(require, employeeId, date, workInformation, updateInfoMap);
			
			if ( !errorList.isEmpty() ) {
				return ResultOfRegisteringWorkSchedule.createWithErrorList(errorList);
			}
		}
		
		workSchedule.changeAttendanceItemValueByHandCorrection(require, updateInfoMap);
		if ( !breakTimeList.isEmpty() ) {
			workSchedule.handCorrectBreakTimeList(require, breakTimeList);
		}
		
		WorkSchedule correctedResult = require.correctWorkSchedule(workSchedule);
		
		// TODO		
		// if $補正処理結果.エラーメッセージID.isPresent()												
		//		return 勤務予定の登録処理結果#エラーありで作る (社員ID, 年月日, $補正処理結果.エラーメッセージID)

		
		AtomTask atomTask = AtomTask.of( () -> {
			if ( isNewRegister ) {
				require.insertWorkSchedule(correctedResult);
			} else {
				require.updateWorkSchedule(correctedResult);
			}
			require.registerTemporaryData(employeeId, date);
		});
		
		return ResultOfRegisteringWorkSchedule.create(atomTask);
		
	}
	
	/**
	 * 時間帯のチェック
	 * @param require
	 * @param employeeId 社員ID
	 * @param date 年月日
	 * @param workInformation 勤務情報
	 * @param updateInfoMap 変更する情報Map
	 * @return
	 */
	private static <T> List<ErrorInfoOfWorkSchedule> checkTimeSpan(
			Require require,
			String employeeId,
			GeneralDate date,
			WorkInformation workInformation,
			Map<Integer, T> updateInfoMap
			) {
		
		// 開始時刻１
		Optional<ErrorInfoOfWorkSchedule> errorOfStartTime1 = 
				CreateWorkSchedule.getErrorInfo(require, employeeId, date, workInformation, WorkTimeZone.START_TIME_1, updateInfoMap);
		// 終了時刻１
		Optional<ErrorInfoOfWorkSchedule> errorOfEndTime1 = 
				CreateWorkSchedule.getErrorInfo(require, employeeId, date, workInformation, WorkTimeZone.END_TIME_1, updateInfoMap);
		// 開始時刻２
		Optional<ErrorInfoOfWorkSchedule> errorOfStartTime2 = 
				CreateWorkSchedule.getErrorInfo(require, employeeId, date, workInformation, WorkTimeZone.START_TIME_2, updateInfoMap);
		// 終了時刻２
		Optional<ErrorInfoOfWorkSchedule> errorOfEndTime2 = 
				CreateWorkSchedule.getErrorInfo(require, employeeId, date, workInformation, WorkTimeZone.END_TIME_2, updateInfoMap);
		
		List<ErrorInfoOfWorkSchedule> errorInfoList = new ArrayList<>();
		errorOfStartTime1.ifPresent(errorInfoList::add);
		errorOfEndTime1.ifPresent(errorInfoList::add);
		errorOfStartTime2.ifPresent(errorInfoList::add);
		errorOfEndTime2.ifPresent(errorInfoList::add);
		return errorInfoList;
	}
	
	/**
	 * 勤務予定のエラー情報を取る
	 * @param require
	 * @param employeeId 社員ID
	 * @param date 年月日
	 * @param workInformation 勤務情報
	 * @param workTimeZone 勤務時間帯
	 * @param updateInfoMap 変更する情報Map
	 * @return
	 */
	private static <T> Optional<ErrorInfoOfWorkSchedule> getErrorInfo(
			Require require,
			String employeeId, 
			GeneralDate date, 
			WorkInformation workInformation, 
			WorkTimeZone workTimeZone,
			Map<Integer, T> updateInfoMap
			) {
		
		if ( !updateInfoMap.containsKey(workTimeZone.attendanceItemId) ) {
			return Optional.empty();
		}
		
		T time  = updateInfoMap.get( workTimeZone.attendanceItemId );
		ContainsResult stateOfTime = 
				workInformation.containsOnChangeableWorkingTime(require, workTimeZone.clockArea , workTimeZone.workNo, (TimeWithDayAttr) time);
		if ( stateOfTime.isContains() ) {
			return Optional.empty();
		}
		
		String errorMessage = I18NText.main("Msg_1781").addIds(
				stateOfTime.getTimeSpan().get().getStart().getInDayTimeWithFormat(), 
				stateOfTime.getTimeSpan().get().getEnd().getInDayTimeWithFormat())
			.build().buildMessage();
		
		return Optional.of(
				ErrorInfoOfWorkSchedule.attendanceItemError(employeeId, date, workTimeZone.attendanceItemId, errorMessage));
	}
	
	public static interface Require extends WorkSchedule.Require{
		
		/**
		 * 勤務予定を取得する
		 * @param employeeId 社員ID
		 * @param date 年月日
		 * @return
		 */
		Optional<WorkSchedule> getWorkSchedule(String employeeId, GeneralDate date);
		
		/**
		 * 勤務予定を補正する
		 * @param workSchedule 勤務予定
		 */
		WorkSchedule correctWorkSchedule(WorkSchedule workSchedule);
		
		/**
		 * 勤務予定を新規登録する
		 * @param workSchedule 勤務予定
		 */
		void insertWorkSchedule(WorkSchedule workSchedule);
		
		/**
		 * 勤務予定を更新する
		 * @param workSchedule 勤務予定
		 */
		void updateWorkSchedule(WorkSchedule workSchedule);
		
		/**
		 * 暫定データを登録する
		 * @param employeeId 社員ID
		 * @param date 年月日
		 */
		void registerTemporaryData(String employeeId, GeneralDate date);
	}
	
	@RequiredArgsConstructor
	static enum WorkTimeZone {
		
		// 開始時刻１
		START_TIME_1( WS_AttendanceItem.StartTime1.ID, ClockAreaAtr.START, new WorkNo(1)),

		// 終了時刻１
		END_TIME_1( WS_AttendanceItem.EndTime1.ID, ClockAreaAtr.END, new WorkNo(1) ),
		
		// 開始時刻２ 
		START_TIME_2 ( WS_AttendanceItem.StartTime2.ID, ClockAreaAtr.START, new WorkNo(2) ),
		
		// 終了時刻２
		END_TIME_2( WS_AttendanceItem.EndTime2.ID, ClockAreaAtr.END, new WorkNo(2) );
		
		public final int attendanceItemId;
		
		public final ClockAreaAtr clockArea;
		
		public final WorkNo workNo;

	}

}

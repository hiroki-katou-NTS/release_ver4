package nts.uk.ctx.at.schedule.dom.schedule.createworkschedule.createschedulecommon.correctworkschedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.schedule.dom.schedule.workschedule.WorkSchedule;
import nts.uk.ctx.at.shared.dom.dailyprocess.calc.CalculateDailyRecordServiceCenterNew;
import nts.uk.ctx.at.shared.dom.dailyprocess.calc.CalculateOption;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.attendancetime.TimeLeavingWork;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.calcategory.CalAttrOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.dailyattendancework.IntegrationOfDaily;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.function.algorithm.ChangeDailyAttendance;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.function.algorithm.ICorrectionAttendanceRule;
import nts.uk.ctx.at.shared.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionType;

/**
 * 勤務予定を補正する
 * UKDesign.ドメインモデル.NittsuSystem.UniversalK.就業.contexts.勤務予定.勤務予定処理.作成処理.アルゴリズム.勤務予定処理.勤務予定作成する.勤務予定作成共通処理.勤務予定を補正する
 * @author tutk
 *
 */
@Stateless
public class CorrectWorkSchedule {
	
	@Inject
	private CalculateDailyRecordServiceCenterNew centerNew;
	
	@Inject
	private ICorrectionAttendanceRule rule;
	
	/**
	 * 勤務予定を補正する method
	 * @param workSchedule
	 * @param employeeId
	 * @param targetDate
	 * @return
	 */
	public WorkSchedule correctWorkSchedule(WorkSchedule workSchedule,String employeeId,GeneralDate targetDate) {
		//勤務予定から日別勤怠（Work）に変換する
		//(tạo 1 biến class IntegrationOfDaily , biến nào k có thì để empty) (TKT-TQP)
		
		CalAttrOfDailyAttd calAttrOfDailyAttd = CalAttrOfDailyAttd.createAllCalculate();
		IntegrationOfDaily integrationOfDaily = new IntegrationOfDaily(employeeId, targetDate, workSchedule.getWorkInfo(), calAttrOfDailyAttd, workSchedule.getAffInfo(), 
				Optional.empty(), new ArrayList<>(), Optional.empty(), workSchedule.getLstBreakTime(), workSchedule.getOptAttendanceTime(), 
				workSchedule.getOptTimeLeaving(), workSchedule.getOptSortTimeWork(), Optional.empty(), Optional.empty(), 
				Optional.empty(), workSchedule.getLstEditState(), Optional.empty(), new ArrayList<>(), Optional.empty());
		//勤怠ルールの補正処理 
		
		ChangeDailyAttendance changeAtt = new ChangeDailyAttendance(true, false, false, false, true);
		integrationOfDaily = rule.process(integrationOfDaily, changeAtt);
		
		//勤務予定の出退勤を補正する
		correctStampOfWorkSchedule(integrationOfDaily);
		
		//勤務予定情報を計算する
		integrationOfDaily = this.calcWorkScheduleInfo(integrationOfDaily, employeeId, targetDate).get(0);
		
		WorkSchedule workSchedules = new WorkSchedule(integrationOfDaily.getEmployeeId(),
				integrationOfDaily.getYmd(), workSchedule.getConfirmedATR(), integrationOfDaily.getWorkInformation(),
				integrationOfDaily.getAffiliationInfor(), integrationOfDaily.getBreakTime(),
				integrationOfDaily.getEditState(), integrationOfDaily.getAttendanceLeave(),
				integrationOfDaily.getAttendanceTimeOfDailyPerformance(), integrationOfDaily.getShortTime());
		
		return workSchedules;
		
	}
	
	/**
	 * Call to 勤務予定情報を計算する
	 * @param integrationOfDaily
	 * @param employeeId
	 * @param targetDate
	 * @return
	 */
	private List<IntegrationOfDaily> calcWorkScheduleInfo(IntegrationOfDaily integrationOfDaily, String employeeId,GeneralDate targetDate) {
		List<IntegrationOfDaily> lstInteOfDaily = new ArrayList<>();
		integrationOfDaily.setEmployeeId(employeeId);
		integrationOfDaily.setYmd(targetDate);
		lstInteOfDaily.add(integrationOfDaily);
		CalculateOption calculateOption = new CalculateOption(false, true);
		lstInteOfDaily = centerNew.calculatePassCompanySetting(calculateOption, lstInteOfDaily, EnumAdaptor.valueOf(ExecutionType.NORMAL_EXECUTION.value, ExecutionType.class));
		
		return lstInteOfDaily;
	}

	/**
	 * alg: 勤務予定の出退勤を補正する 
	 */
	private void correctStampOfWorkSchedule(IntegrationOfDaily integrationOfDaily){
		
		// 出退勤があるかを確認する
		if(!integrationOfDaily.getAttendanceLeave().isPresent()){
			return;
		}
		
		// 出退勤一覧をループする
		List<TimeLeavingWork> timeLeavingWorks = new ArrayList<>(integrationOfDaily.getAttendanceLeave().get().getTimeLeavingWorks());
		for (TimeLeavingWork timeLeavingWork : timeLeavingWorks) {
			// 出退勤を削除すべきかを確認する
			// True　←　出退勤。出勤。打刻　＝　EMPTY　AND　出退勤。退勤。打刻　＝　EMPTY
			// False　←　以外

			boolean attendanceStampIsEmpty = (!timeLeavingWork.getAttendanceStamp().isPresent())
										  || (timeLeavingWork.getAttendanceStamp().isPresent() && !timeLeavingWork.getAttendanceStamp().get().getStamp().isPresent());
			
			boolean leaveStampIsEmpty = (!timeLeavingWork.getLeaveStamp().isPresent())
					  				 || (timeLeavingWork.getLeaveStamp().isPresent() && !timeLeavingWork.getLeaveStamp().get().getStamp().isPresent());	
			if (attendanceStampIsEmpty == true && leaveStampIsEmpty == true) {
				// 処理中の出退勤を削除する
				integrationOfDaily.getAttendanceLeave().get().getTimeLeavingWorks().remove(timeLeavingWork);
			}
		}
		
		// 出退勤一覧のsizeを確認する
		if(integrationOfDaily.getAttendanceLeave().get().getTimeLeavingWorks().size() == 0){
			integrationOfDaily.setAttendanceLeave(Optional.empty()); 
		}
	}
}

package nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.ErrorAlarmWorkRecordCode;
import nts.uk.ctx.at.record.dom.worktime.TemporaryTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingWork;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkNo;

/*
 * 臨時系二重打刻をチェックする
 */
@Stateless
public class TemporaryDoubleStampChecking {

	public EmployeeDailyPerError temporaryDoubleStampChecking(String companyID, String employeeID, GeneralDate processingDate, TemporaryTimeOfDailyPerformance temporaryTimeOfDailyPerformance){
		
		EmployeeDailyPerError employeeDailyPerError = null;
		
		List<Integer> attendanceItemIDList = new ArrayList<>();
		
//		Optional<TemporaryTimeOfDailyPerformance> temporaryTimeOfDailyPerformance = this.temporaryTimeOfDailyPerformanceRepository
//				.findByKey(employeeID, processingDate);
		
		if (temporaryTimeOfDailyPerformance != null && !temporaryTimeOfDailyPerformance.getTimeLeavingWorks().isEmpty()) {
			List<TimeLeavingWork> timeLeavingWorks = temporaryTimeOfDailyPerformance.getTimeLeavingWorks();
			for (TimeLeavingWork timeLeavingWork : timeLeavingWorks){
				if(timeLeavingWork.getAttendanceStamp() != null && timeLeavingWork.getAttendanceStamp().isPresent()){
					if(timeLeavingWork.getAttendanceStamp().get().getNumberOfReflectionStamp() >= 2){
						if (timeLeavingWork.getWorkNo().equals(new WorkNo((1)))) {
							attendanceItemIDList.add(51);
						} else if (timeLeavingWork.getWorkNo().equals(new WorkNo((2)))) {
							attendanceItemIDList.add(59);
						} else if (timeLeavingWork.getWorkNo().equals(new WorkNo((3)))) {
							attendanceItemIDList.add(67);
						}
					}				
				}
				if(timeLeavingWork.getLeaveStamp() != null && timeLeavingWork.getLeaveStamp().isPresent()){
					if (timeLeavingWork.getLeaveStamp().get().getNumberOfReflectionStamp() >= 2) {
						if (timeLeavingWork.getWorkNo().equals(new WorkNo((1)))) {
							attendanceItemIDList.add(53);
						} else if (timeLeavingWork.getWorkNo().equals(new WorkNo((2)))) {
							attendanceItemIDList.add(61);
						} else if (timeLeavingWork.getWorkNo().equals(new WorkNo((3)))) {
							attendanceItemIDList.add(69);
						}
					}
				}				
			}
			if(!attendanceItemIDList.isEmpty()){
				employeeDailyPerError = new EmployeeDailyPerError(companyID,
						employeeID, processingDate, new ErrorAlarmWorkRecordCode("S006"),
						attendanceItemIDList);
			}
//			if (!attendanceItemIDList.isEmpty()) {
//				this.createEmployeeDailyPerError.createEmployeeDailyPerError(companyID, employeeID, processingDate, new ErrorAlarmWorkRecordCode("S006"), attendanceItemIDList);
//			}
		}
		return employeeDailyPerError;
	}
}

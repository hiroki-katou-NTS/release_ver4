package nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.ErrorAlarmWorkRecordCode;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingWork;
import nts.uk.ctx.at.record.dom.worktime.WorkStamp;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.BasicScheduleService;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.WorkStyle;
import nts.uk.shr.com.time.TimeWithDayAttr;

/*
 * 打刻漏れ
 */
@Stateless
public class LackOfStampingAlgorithm {

	@Inject
	private BasicScheduleService basicScheduleService;

	public EmployeeDailyPerError lackOfStamping(String companyID, String employeeID, GeneralDate processingDate,
			WorkInfoOfDailyPerformance workInfoOfDailyPerformance,
			TimeLeavingOfDailyPerformance timeLeavingOfDailyPerformance) {

		EmployeeDailyPerError employeeDailyPerError = null;

		// WorkInfoOfDailyPerformance workInfoOfDailyPerformance =
		// workInformationRepository
		// .find(employeeID, processingDate).get();

		WorkStyle workStyle = basicScheduleService
				.checkWorkDay(workInfoOfDailyPerformance.getRecordInfo().getWorkTypeCode().v());

		if (workStyle != WorkStyle.ONE_DAY_REST) {

			// TimeLeavingOfDailyPerformance timeLeavingOfDailyPerformance =
			// timeLeavingOfDailyPerformanceRepository.findByKey(employeeID,
			// processingDate).get();

			if (timeLeavingOfDailyPerformance != null
					&& !timeLeavingOfDailyPerformance.getTimeLeavingWorks().isEmpty()) {
				List<TimeLeavingWork> timeLeavingWorkList = timeLeavingOfDailyPerformance.getTimeLeavingWorks();
				List<Integer> attendanceItemIDList = new ArrayList<>();
				for (TimeLeavingWork timeLeavingWork : timeLeavingWorkList) {
					if (timeLeavingWork.getLeaveStamp() != null 
							&& timeLeavingWork.getLeaveStamp().isPresent()
							&& timeLeavingWork.getAttendanceStamp() != null
							&& timeLeavingWork.getAttendanceStamp().isPresent()) {
						Optional<WorkStamp> leavingStamp = timeLeavingWork.getLeaveStamp().get().getStamp();
						Optional<WorkStamp> attendanceStamp = timeLeavingWork.getAttendanceStamp().get().getStamp();
						if (leavingStamp.isPresent() && !attendanceStamp.isPresent()) {
							if (timeLeavingWork.getWorkNo().v().intValue() == 1) {
								attendanceItemIDList.add(31);
							} else if (timeLeavingWork.getWorkNo().v().intValue() == 2) {
								attendanceItemIDList.add(41);
							}
						} else if (!leavingStamp.isPresent() && attendanceStamp.isPresent()) {
							if (timeLeavingWork.getWorkNo().v().intValue() == 1) {
								attendanceItemIDList.add(34);
							} else if (timeLeavingWork.getWorkNo().v().intValue() == 2) {
								attendanceItemIDList.add(44);
							}
						} else if (!leavingStamp.isPresent() && !attendanceStamp.isPresent()) {
							if (timeLeavingWork.getWorkNo().v().intValue() == 1) {
								attendanceItemIDList.add(31);
								attendanceItemIDList.add(34);
							} else if (timeLeavingWork.getWorkNo().v().intValue() == 2) {
								attendanceItemIDList.add(41);
								attendanceItemIDList.add(44);
							}
						} else if (leavingStamp.isPresent() && attendanceStamp.isPresent()){
							TimeWithDayAttr leavingTimeWithDay = leavingStamp.get().getTimeWithDay();
							TimeWithDayAttr attendanceTimeWithDay = attendanceStamp.get().getTimeWithDay();
							if (leavingTimeWithDay != null && attendanceTimeWithDay == null) {
								if (timeLeavingWork.getWorkNo().v().intValue() == 1) {
									attendanceItemIDList.add(31);
								} else if (timeLeavingWork.getWorkNo().v().intValue() == 2) {
									attendanceItemIDList.add(41);
								}
							} else if (leavingTimeWithDay == null && attendanceTimeWithDay != null) {
								if (timeLeavingWork.getWorkNo().v().intValue() == 1) {
									attendanceItemIDList.add(34);
								} else if (timeLeavingWork.getWorkNo().v().intValue() == 2) {
									attendanceItemIDList.add(44);
								}
							} else if (leavingTimeWithDay == null && attendanceTimeWithDay == null) {
								if (timeLeavingWork.getWorkNo().v().intValue() == 1) {
									attendanceItemIDList.add(31);
									attendanceItemIDList.add(34);
								} else if (timeLeavingWork.getWorkNo().v().intValue() == 2) {
									attendanceItemIDList.add(41);
									attendanceItemIDList.add(44);
								}
							}
						}
					}
				}
				if (!attendanceItemIDList.isEmpty()) {
					employeeDailyPerError = new EmployeeDailyPerError(companyID, employeeID, processingDate,
							new ErrorAlarmWorkRecordCode("S001"), attendanceItemIDList);
				}
			}else {
				List<Integer> attendanceItemIDList = new ArrayList<>();
				attendanceItemIDList.add(31);
				attendanceItemIDList.add(34);
				employeeDailyPerError = new EmployeeDailyPerError(companyID, employeeID, processingDate,
						new ErrorAlarmWorkRecordCode("S001"), attendanceItemIDList);
			}
		}

		return employeeDailyPerError;
	}

}

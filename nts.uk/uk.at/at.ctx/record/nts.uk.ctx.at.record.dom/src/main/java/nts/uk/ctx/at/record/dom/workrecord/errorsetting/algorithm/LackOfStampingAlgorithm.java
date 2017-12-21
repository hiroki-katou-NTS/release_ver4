package nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.algorithm.CreateEmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.ErrorAlarmWorkRecordCode;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.OutPutProcess;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.SystemFixedErrorAlarm;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingWork;
import nts.uk.ctx.at.record.dom.worktime.WorkStamp;
import nts.uk.ctx.at.record.dom.worktime.repository.TimeLeavingOfDailyPerformanceRepository;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.BasicScheduleService;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.WorkStyle;

/*
 * 打刻漏れ
 */
@Stateless
public class LackOfStampingAlgorithm {

	@Inject
	private BasicScheduleService basicScheduleService;

	@Inject
	private WorkInformationRepository workInformationRepository;

	@Inject
	private TimeLeavingOfDailyPerformanceRepository timeLeavingOfDailyPerformanceRepository;

	@Inject
	private CreateEmployeeDailyPerError createEmployeeDailyPerError;

	public OutPutProcess lackOfStamping(String companyID, String employeeID, GeneralDate processingDate) {

		OutPutProcess outPutProcess = OutPutProcess.NO_ERROR;

		WorkInfoOfDailyPerformance workInfoOfDailyPerformance = workInformationRepository
				.find(employeeID, processingDate).get();

		WorkStyle workStyle = basicScheduleService
				.checkWorkDay(workInfoOfDailyPerformance.getRecordWorkInformation().getWorkTypeCode().v());

		if (workStyle == WorkStyle.ONE_DAY_REST) {

			TimeLeavingOfDailyPerformance timeLeavingOfDailyPerformance = timeLeavingOfDailyPerformanceRepository
					.findByKey(employeeID, processingDate).get();

			List<TimeLeavingWork> timeLeavingWorkList = timeLeavingOfDailyPerformance.getTimeLeavingWorks();
			List<Integer> attendanceItemIDList = new ArrayList<>();
			for (TimeLeavingWork timeLeavingWork : timeLeavingWorkList) {
				WorkStamp leavingStamp = timeLeavingWork.getLeaveStamp().getStamp().get();
				WorkStamp attendanceStamp = timeLeavingWork.getAttendanceStamp().getStamp().get();
				if (leavingStamp != null && attendanceStamp == null) {
					if (timeLeavingWork.getWorkNo().v().intValue() == 1) {
						attendanceItemIDList.add(31);
					} else if (timeLeavingWork.getWorkNo().v().intValue() == 2) {
						attendanceItemIDList.add(41);
					}
				} else if (leavingStamp == null && attendanceStamp != null) {
					if (timeLeavingWork.getWorkNo().v().intValue() == 1) {
						attendanceItemIDList.add(34);
					} else if (timeLeavingWork.getWorkNo().v().intValue() == 2) {
						attendanceItemIDList.add(44);
					}
				} else if (leavingStamp == null && attendanceStamp == null) {
					if (timeLeavingWork.getWorkNo().v().intValue() == 1) {
						attendanceItemIDList.add(31);
						attendanceItemIDList.add(34);
					} else if (timeLeavingWork.getWorkNo().v().intValue() == 2) {
						attendanceItemIDList.add(41);
						attendanceItemIDList.add(44);
					}
				}
				outPutProcess = createEmployeeDailyPerError.createEmployeeDailyPerError(companyID, employeeID,
						processingDate, new ErrorAlarmWorkRecordCode("S001"),
						attendanceItemIDList);
			}

		}

		return outPutProcess;
	}

}

package nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.algorithm.CreateEmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.ErrorAlarmWorkRecordCode;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.OutPutProcess;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.SystemFixedErrorAlarm;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingWork;
import nts.uk.ctx.at.record.dom.worktime.repository.TimeLeavingOfDailyPerformanceRepository;
import nts.uk.ctx.at.shared.dom.worktime.algorithm.rangeofdaytimezone.DuplicateStateAtr;
import nts.uk.ctx.at.shared.dom.worktime.algorithm.rangeofdaytimezone.DuplicationStatusOfTimeZone;
import nts.uk.ctx.at.shared.dom.worktime.algorithm.rangeofdaytimezone.RangeOfDayTimeZoneService;
import nts.uk.ctx.at.shared.dom.worktime.algorithm.rangeofdaytimezone.TimeSpanForCalc;
import nts.uk.shr.com.time.TimeWithDayAttr;

/*
 * 打刻順序不正
 */
@Stateless
public class StampIncorrectOrderAlgorithm {

	@Inject
	private TimeLeavingOfDailyPerformanceRepository timeLeavingOfDailyPerformanceRepository;

	@Inject
	private CreateEmployeeDailyPerError createEmployeeDailyPerError;

	@Inject
	private RangeOfDayTimeZoneService rangeOfDayTimeZoneService;

	public void stampIncorrectOrder(String companyID, String employeeID, GeneralDate processingDate,
			TimeLeavingOfDailyPerformance timeLeavingOfDailyPerformance) {
		
		if(timeLeavingOfDailyPerformance != null && !timeLeavingOfDailyPerformance.getTimeLeavingWorks().isEmpty() && timeLeavingOfDailyPerformance.getTimeLeavingWorks().size() >= 2){

			List<Integer> attendanceItemIds = new ArrayList<>();
	
			// TimeLeavingOfDailyPerformance timeLeavingOfDailyPerformance =
			// timeLeavingOfDailyPerformanceRepository
			// .findByKey(employeeID, processingDate).get();
	
			if (timeLeavingOfDailyPerformance != null && !timeLeavingOfDailyPerformance.getTimeLeavingWorks().isEmpty()) {
	
				// ペアの逆転がないか確認する
				List<OutPutProcess> pairOutPutList = checkPairReversed(timeLeavingOfDailyPerformance.getTimeLeavingWorks());
	
				if (pairOutPutList.stream().anyMatch(item -> item == OutPutProcess.HAS_ERROR)) {
					if (timeLeavingOfDailyPerformance.getTimeLeavingWorks().get(0).getWorkNo()
							.greaterThan(timeLeavingOfDailyPerformance.getTimeLeavingWorks().get(1).getWorkNo())) {
						this.createEmployeeDailyPerError.createEmployeeDailyPerError(companyID, employeeID, processingDate,
								new ErrorAlarmWorkRecordCode("S004"), attendanceItemIds);
					} else {
						if(timeLeavingOfDailyPerformance.getTimeLeavingWorks().get(0).getAttendanceStamp() != null 
								&& timeLeavingOfDailyPerformance.getTimeLeavingWorks().get(0).getAttendanceStamp().isPresent()
								&& timeLeavingOfDailyPerformance.getTimeLeavingWorks().get(0).getAttendanceStamp().get().getStamp() != null
								&& timeLeavingOfDailyPerformance.getTimeLeavingWorks().get(0).getAttendanceStamp().get().getStamp().isPresent()
								&& timeLeavingOfDailyPerformance.getTimeLeavingWorks().get(0).getLeaveStamp() != null
								&& timeLeavingOfDailyPerformance.getTimeLeavingWorks().get(0).getLeaveStamp().isPresent()
								&& timeLeavingOfDailyPerformance.getTimeLeavingWorks().get(0).getLeaveStamp().get().getStamp() != null
								&& timeLeavingOfDailyPerformance.getTimeLeavingWorks().get(0).getLeaveStamp().get().getStamp().isPresent()){
							// 重複の判断処理
							TimeWithDayAttr stampStartTimeFirstTime = timeLeavingOfDailyPerformance.getTimeLeavingWorks().get(0)
									.getAttendanceStamp().get().getStamp().get().getTimeWithDay();
							TimeWithDayAttr endStartTimeFirstTime = timeLeavingOfDailyPerformance.getTimeLeavingWorks().get(0)
									.getLeaveStamp().get().getStamp().get().getTimeWithDay();
							TimeSpanForCalc timeSpanFirstTime = new TimeSpanForCalc(stampStartTimeFirstTime,
									endStartTimeFirstTime);
	
							if (timeLeavingOfDailyPerformance.getTimeLeavingWorks().get(1).getAttendanceStamp() != null 
								&& timeLeavingOfDailyPerformance.getTimeLeavingWorks().get(1).getAttendanceStamp().isPresent()
								&& timeLeavingOfDailyPerformance.getTimeLeavingWorks().get(1).getAttendanceStamp().get().getStamp() != null
								&& timeLeavingOfDailyPerformance.getTimeLeavingWorks().get(1).getAttendanceStamp().get().getStamp().isPresent()
								&& timeLeavingOfDailyPerformance.getTimeLeavingWorks().get(1).getLeaveStamp() != null
								&& timeLeavingOfDailyPerformance.getTimeLeavingWorks().get(1).getLeaveStamp().isPresent()
								&& timeLeavingOfDailyPerformance.getTimeLeavingWorks().get(1).getLeaveStamp().get().getStamp() != null
								&& timeLeavingOfDailyPerformance.getTimeLeavingWorks().get(1).getLeaveStamp().get().getStamp().isPresent()) {
								TimeWithDayAttr stampStartTimeSecondTime = timeLeavingOfDailyPerformance.getTimeLeavingWorks()
										.get(1).getAttendanceStamp().get().getStamp().get().getTimeWithDay();
								TimeWithDayAttr endStartTimeSecondTime = timeLeavingOfDailyPerformance.getTimeLeavingWorks().get(1)
										.getLeaveStamp().get().getStamp().get().getTimeWithDay();
								TimeSpanForCalc timeSpanSecondTime = new TimeSpanForCalc(stampStartTimeSecondTime,
										endStartTimeSecondTime);
	
								DuplicateStateAtr duplicateStateAtr = this.rangeOfDayTimeZoneService
										.checkPeriodDuplication(timeSpanFirstTime, timeSpanSecondTime);
								DuplicationStatusOfTimeZone duplicationStatusOfTimeZone = this.rangeOfDayTimeZoneService
										.checkStateAtr(duplicateStateAtr);
	
								if (duplicationStatusOfTimeZone != DuplicationStatusOfTimeZone.NON_OVERLAPPING) {
									this.createEmployeeDailyPerError.createEmployeeDailyPerError(companyID, employeeID,
											processingDate, new ErrorAlarmWorkRecordCode("S004"), attendanceItemIds);
								}
							}
						}	
					}
				} else {
					this.createEmployeeDailyPerError.createEmployeeDailyPerError(companyID, employeeID, processingDate,
							new ErrorAlarmWorkRecordCode(SystemFixedErrorAlarm.INCORRECT_STAMP.name()), attendanceItemIds);
				}
			}
		}
	}

	private List<OutPutProcess> checkPairReversed(List<TimeLeavingWork> timeLeavingWorks) {
		List<OutPutProcess> outPutProcessList = new ArrayList<>();
		OutPutProcess pairOutPut = OutPutProcess.HAS_ERROR;

		for (TimeLeavingWork timeLeavingWorking : timeLeavingWorks) {
			if (timeLeavingWorking.getLeaveStamp() != null
					&& timeLeavingWorking.getLeaveStamp().isPresent()
					&& timeLeavingWorking.getLeaveStamp().get().getStamp() != null
					&& timeLeavingWorking.getLeaveStamp().get().getStamp().isPresent()
					&& timeLeavingWorking.getAttendanceStamp() != null 
					&& timeLeavingWorking.getAttendanceStamp().isPresent()
					&& timeLeavingWorking.getAttendanceStamp().get().getStamp() != null
					&& timeLeavingWorking.getAttendanceStamp().get().getStamp().isPresent()) {
				if (timeLeavingWorking.getLeaveStamp().get().getStamp().get().getAfterRoundingTime()
						.greaterThanOrEqualTo(timeLeavingWorking.getAttendanceStamp().get().getStamp().get().getAfterRoundingTime())) {
					pairOutPut = OutPutProcess.NO_ERROR;
				}
			}
			outPutProcessList.add(pairOutPut);
		}

		return outPutProcessList;
	}
}

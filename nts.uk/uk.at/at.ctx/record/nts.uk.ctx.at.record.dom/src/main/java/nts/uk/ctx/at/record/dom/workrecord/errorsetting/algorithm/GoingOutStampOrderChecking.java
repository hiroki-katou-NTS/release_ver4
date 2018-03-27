package nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.breakorgoout.OutingTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.breakorgoout.OutingTimeSheet;
import nts.uk.ctx.at.record.dom.breakorgoout.enums.GoingOutReason;
import nts.uk.ctx.at.record.dom.breakorgoout.primitivevalue.OutingFrameNo;
import nts.uk.ctx.at.record.dom.breakorgoout.repository.OutingTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.algorithm.CreateEmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.ErrorAlarmWorkRecordCode;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.CheckState;
import nts.uk.ctx.at.record.dom.worktime.TemporaryTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeActualStamp;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingWork;
import nts.uk.ctx.at.record.dom.worktime.repository.TemporaryTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.worktime.repository.TimeLeavingOfDailyPerformanceRepository;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.worktime.algorithm.rangeofdaytimezone.DuplicateStateAtr;
import nts.uk.ctx.at.shared.dom.worktime.algorithm.rangeofdaytimezone.DuplicationStatusOfTimeZone;
import nts.uk.ctx.at.shared.dom.worktime.algorithm.rangeofdaytimezone.RangeOfDayTimeZoneService;
import nts.uk.ctx.at.shared.dom.worktime.algorithm.rangeofdaytimezone.TimeSpanForCalc;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 外出系打刻順序不正をチェックする
 * @author nampt
 *
 */
@Stateless
public class GoingOutStampOrderChecking {

	@Inject
	private OutingTimeOfDailyPerformanceRepository outingTimeOfDailyPerformanceRepository;

	@Inject
	private RangeOfDayTimeZoneService rangeOfDayTimeZoneService;

	@Inject
	private CreateEmployeeDailyPerError createEmployeeDailyPerError;

	@Inject
	private TimeLeavingOfDailyPerformanceRepository timeLeavingOfDailyPerformanceRepository;

	@Inject
	private TemporaryTimeOfDailyPerformanceRepository temporaryTimeOfDailyPerformanceRepository;

	public void goingOutStampOrderChecking(String companyId, String employeeId, GeneralDate processingDate,
			OutingTimeOfDailyPerformance outingTimeOfDailyPerformance, TimeLeavingOfDailyPerformance timeLeavingOfDailyPerformance, 
			TemporaryTimeOfDailyPerformance temporaryTimeOfDailyPerformance) {
		// Optional<OutingTimeOfDailyPerformance> outingTimeOfDailyPerformance =
		// this.outingTimeOfDailyPerformanceRepository
		// .findByEmployeeIdAndDate(employeeId, processingDate);
		if (outingTimeOfDailyPerformance != null && !outingTimeOfDailyPerformance.getOutingTimeSheets().isEmpty()) {

			List<OutingTimeSheet> outingTimeSheets = outingTimeOfDailyPerformance.getOutingTimeSheets();
			
			outingTimeSheets.sort((e1, e2) -> e1.getGoOut().get().getStamp().get().getTimeWithDay().v()
					.compareTo(e2.getGoOut().get().getStamp().get().getTimeWithDay().v()));

			int outingFrameNo = 1;
			for (OutingTimeSheet item : outingTimeSheets) {
				Optional<TimeActualStamp> goOut = item.getGoOut();
				AttendanceTime outingTimeCalculation = item.getOutingTimeCalculation();
				AttendanceTime outingTime = item.getOutingTime();
				GoingOutReason reasonForGoOut = item.getReasonForGoOut();
				Optional<TimeActualStamp> comeBack = item.getComeBack();
				item = new OutingTimeSheet(new OutingFrameNo(outingFrameNo), goOut, outingTimeCalculation, outingTime,
						reasonForGoOut, comeBack);
				outingFrameNo++;
			}

			List<OutingTimeSheet> outingTimeSheetsTemp = new ArrayList<>();

			for (OutingTimeSheet outingTimeSheet : outingTimeSheets) {
				if (outingTimeSheet.getComeBack() != null && outingTimeSheet.getComeBack().isPresent()
						&& outingTimeSheet.getComeBack().get().getStamp() != null && outingTimeSheet.getComeBack().get().getStamp().isPresent()
						&& outingTimeSheet.getGoOut().get().getStamp() != null && outingTimeSheet.getGoOut().get().getStamp().isPresent()) {
					if (outingTimeSheet.getGoOut().get().getStamp().get().getTimeWithDay()
							.lessThanOrEqualTo(outingTimeSheet.getComeBack().get().getStamp().get().getTimeWithDay())) {

						List<Integer> attendanceItemIDList = new ArrayList<>();

						if (outingTimeSheet.getOutingFrameNo().equals(new OutingFrameNo(1))) {
							attendanceItemIDList.add(88);
							attendanceItemIDList.add(91);
						} else if (outingTimeSheet.getOutingFrameNo().equals(new OutingFrameNo(2))) {
							attendanceItemIDList.add(95);
							attendanceItemIDList.add(98);
						} else if (outingTimeSheet.getOutingFrameNo().equals(new OutingFrameNo(3))) {
							attendanceItemIDList.add(102);
							attendanceItemIDList.add(105);
						} else if (outingTimeSheet.getOutingFrameNo().equals(new OutingFrameNo(4))) {
							attendanceItemIDList.add(109);
							attendanceItemIDList.add(112);
						} else if (outingTimeSheet.getOutingFrameNo().equals(new OutingFrameNo(5))) {
							attendanceItemIDList.add(116);
							attendanceItemIDList.add(119);
						} else if (outingTimeSheet.getOutingFrameNo().equals(new OutingFrameNo(6))) {
							attendanceItemIDList.add(123);
							attendanceItemIDList.add(126);
						} else if (outingTimeSheet.getOutingFrameNo().equals(new OutingFrameNo(7))) {
							attendanceItemIDList.add(130);
							attendanceItemIDList.add(133);
						} else if (outingTimeSheet.getOutingFrameNo().equals(new OutingFrameNo(8))) {
							attendanceItemIDList.add(137);
							attendanceItemIDList.add(140);
						} else if (outingTimeSheet.getOutingFrameNo().equals(new OutingFrameNo(9))) {
							attendanceItemIDList.add(144);
							attendanceItemIDList.add(147);
						} else if (outingTimeSheet.getOutingFrameNo().equals(new OutingFrameNo(10))) {
							attendanceItemIDList.add(151);
							attendanceItemIDList.add(154);
						}

						// list contain different
						outingTimeSheetsTemp = outingTimeSheets.stream()
								.filter(item -> !item.getOutingFrameNo().equals(outingTimeSheet.getOutingFrameNo().v()))
								.collect(Collectors.toList());

						TimeWithDayAttr stampStartTimeFirstTime = outingTimeSheet.getGoOut().get().getStamp().get()
								.getTimeWithDay();
						TimeWithDayAttr endStartTimeFirstTime = outingTimeSheet.getComeBack().get().getStamp().get()
								.getTimeWithDay();
						TimeSpanForCalc timeSpanFirstTime = new TimeSpanForCalc(stampStartTimeFirstTime,
								endStartTimeFirstTime);

						List<DuplicationStatusOfTimeZone> newList = new ArrayList<>();

						// 他の時間帯との時間帯重複を確認する
						// check with another outingFrameNo
						for (OutingTimeSheet timeSheet : outingTimeSheetsTemp) {
							TimeWithDayAttr stampStartTimeSecondTime = timeSheet.getGoOut().get().getStamp().get()
									.getTimeWithDay();
							TimeWithDayAttr endStartTimesecondTime = timeSheet.getComeBack().get().getStamp().get()
									.getTimeWithDay();
							TimeSpanForCalc timeSpanSecondTime = new TimeSpanForCalc(stampStartTimeSecondTime,
									endStartTimesecondTime);

							DuplicateStateAtr duplicateStateAtr = this.rangeOfDayTimeZoneService
									.checkPeriodDuplication(timeSpanFirstTime, timeSpanSecondTime);
							DuplicationStatusOfTimeZone duplicationStatusOfTimeZone = this.rangeOfDayTimeZoneService
									.checkStateAtr(duplicateStateAtr);
							newList.add(duplicationStatusOfTimeZone);
						}

						if (newList.stream().allMatch(item -> item == DuplicationStatusOfTimeZone.NON_OVERLAPPING)) {
							createEmployeeDailyPerError.createEmployeeDailyPerError(companyId, employeeId,
									processingDate, new ErrorAlarmWorkRecordCode("S004"), attendanceItemIDList);
						} else {
							// 出退勤時間帯に包含されているか確認する
							CheckState checkState = checkConjugation(companyId, employeeId, processingDate,
									outingTimeSheet, timeLeavingOfDailyPerformance, temporaryTimeOfDailyPerformance);
							if (checkState == CheckState.NON_INCLUSION) {
								createEmployeeDailyPerError.createEmployeeDailyPerError(companyId, employeeId,
										processingDate, new ErrorAlarmWorkRecordCode("S004"), attendanceItemIDList);
							}
						}
					}
				}
			}
		}
	}

	private CheckState checkConjugation(String comanyID, String employeeID, GeneralDate processingDate,
			OutingTimeSheet outingTimeSheet, TimeLeavingOfDailyPerformance timeLeavingOfDailyPerformance, 
			TemporaryTimeOfDailyPerformance temporaryTimeOfDailyPerformance) {
		CheckState state = CheckState.INCLUSION;

//		Optional<TimeLeavingOfDailyPerformance> timeLeavingOfDailyPerformance = this.timeLeavingOfDailyPerformanceRepository
//				.findByKey(employeeID, processingDate);

		TimeSpanForCalc timeSpanFirstTime = null;
		if (outingTimeSheet.getGoOut() != null && outingTimeSheet.getGoOut().isPresent() 
				&& outingTimeSheet.getGoOut().get().getStamp() != null && outingTimeSheet.getGoOut().get().getStamp().isPresent() 
				&& outingTimeSheet.getComeBack() != null && outingTimeSheet.getComeBack().isPresent() 
				&& outingTimeSheet.getComeBack().get().getStamp() != null && outingTimeSheet.getComeBack().get().getStamp().isPresent()) {
			TimeWithDayAttr stampStartTimeFirstTime = outingTimeSheet.getGoOut().get().getStamp().get().getTimeWithDay();
			TimeWithDayAttr endStartTimeFirstTime = outingTimeSheet.getComeBack().get().getStamp().get().getTimeWithDay();
			timeSpanFirstTime = new TimeSpanForCalc(stampStartTimeFirstTime, endStartTimeFirstTime);
		}
		
		if (timeLeavingOfDailyPerformance != null && !timeLeavingOfDailyPerformance.getTimeLeavingWorks().isEmpty() && timeSpanFirstTime != null) {
			List<TimeLeavingWork> timeLeavingWorks = timeLeavingOfDailyPerformance.getTimeLeavingWorks();
			for (TimeLeavingWork timeLeavingWork : timeLeavingWorks) {
				// if(timeLeavingWork.getAttendanceStamp().getStamp() != null){
				if (timeLeavingWork.getAttendanceStamp() != null && timeLeavingWork.getAttendanceStamp().isPresent() 
						&& timeLeavingWork.getAttendanceStamp().get().getStamp() != null && timeLeavingWork.getAttendanceStamp().get().getStamp().isPresent()
						&& timeLeavingWork.getLeaveStamp() != null && timeLeavingWork.getLeaveStamp().isPresent()
						&& timeLeavingWork.getLeaveStamp().get().getStamp() != null && timeLeavingWork.getLeaveStamp().get().getStamp().isPresent()) {
					TimeWithDayAttr stampStartTimeSecondTime = timeLeavingWork.getAttendanceStamp().get().getStamp().get()
							.getTimeWithDay();
					TimeWithDayAttr endStartTimesecondTime = timeLeavingWork.getLeaveStamp().get().getStamp().get()
							.getTimeWithDay();
					TimeSpanForCalc timeSpanSecondTime = new TimeSpanForCalc(stampStartTimeSecondTime,
							endStartTimesecondTime);
					DuplicateStateAtr duplicateStateAtr = this.rangeOfDayTimeZoneService
							.checkPeriodDuplication(timeSpanFirstTime, timeSpanSecondTime);
					DuplicationStatusOfTimeZone duplicationStatusOfTimeZone = this.rangeOfDayTimeZoneService
							.checkStateAtr(duplicateStateAtr);
					if (duplicationStatusOfTimeZone == DuplicationStatusOfTimeZone.SAME_WORK_TIME
							|| duplicationStatusOfTimeZone == DuplicationStatusOfTimeZone.INCLUSION_OUTSIDE) {
						state = CheckState.INCLUSION;
					} else {
//						Optional<TemporaryTimeOfDailyPerformance> temporaryTimeOfDailyPerformance = this.temporaryTimeOfDailyPerformanceRepository
//								.findByKey(employeeID, processingDate);
						if (temporaryTimeOfDailyPerformance != null && !temporaryTimeOfDailyPerformance.getTimeLeavingWorks().isEmpty()) {
							List<TimeLeavingWork> leavingWorks = temporaryTimeOfDailyPerformance.getTimeLeavingWorks();
							for (TimeLeavingWork leavingWork : leavingWorks) {
								if(leavingWork.getAttendanceStamp() != null && leavingWork.getAttendanceStamp().isPresent()
										&& leavingWork.getAttendanceStamp().get().getStamp() != null && leavingWork.getAttendanceStamp().get().getStamp().isPresent()
										&& leavingWork.getLeaveStamp() != null && leavingWork.getLeaveStamp().isPresent()
										&& leavingWork.getLeaveStamp().get().getStamp() != null && leavingWork.getLeaveStamp().get().getStamp().isPresent()){
									TimeWithDayAttr newStampStartTimeSecondTime = leavingWork.getAttendanceStamp().get().getStamp()
											.get().getTimeWithDay();
									TimeWithDayAttr newEndStartTimesecondTime = leavingWork.getLeaveStamp().get().getStamp().get()
											.getTimeWithDay();
									TimeSpanForCalc newTimeSpanSecondTime = new TimeSpanForCalc(newStampStartTimeSecondTime,
											newEndStartTimesecondTime);
									DuplicateStateAtr newDuplicateStateAtr = this.rangeOfDayTimeZoneService
											.checkPeriodDuplication(timeSpanFirstTime, newTimeSpanSecondTime);
									DuplicationStatusOfTimeZone newDuplicationStatusOfTimeZone = this.rangeOfDayTimeZoneService
											.checkStateAtr(newDuplicateStateAtr);
									if (newDuplicationStatusOfTimeZone == DuplicationStatusOfTimeZone.SAME_WORK_TIME
											|| newDuplicationStatusOfTimeZone == DuplicationStatusOfTimeZone.INCLUSION_OUTSIDE) {
										state = CheckState.INCLUSION;
									} else {
										state = CheckState.NON_INCLUSION;
									}
								}								
							}
						}
					}
				}
				// }
			}
		}

		return state;
	}
}

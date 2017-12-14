package nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeSheet;
import nts.uk.ctx.at.record.dom.breakorgoout.repository.BreakTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.algorithm.CreateEmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.ErrorAlarmWorkRecordCode;
import nts.uk.ctx.at.record.dom.worktime.primitivevalue.WorkNo;

/*
 * 休憩系打刻漏れをチェックする
 */
@Stateless
public class BreakTimeStampLeakageChecking {

	@Inject
	private BreakTimeOfDailyPerformanceRepository breakTimeOfDailyPerformanceRepository;

	@Inject
	private CreateEmployeeDailyPerError createEmployeeDailyPerError;

	public void breakTimeStampLeakageChecking(String companyID, String employeeID, GeneralDate processingDate) {
		List<BreakTimeOfDailyPerformance> breakTimeOfDailyPerformances = breakTimeOfDailyPerformanceRepository
				.findByKey(employeeID, processingDate);
		if (!breakTimeOfDailyPerformances.isEmpty()) {
			for (BreakTimeOfDailyPerformance breakTimeOfDailyPerformance : breakTimeOfDailyPerformances) {
				List<BreakTimeSheet> breakTimeSheets = breakTimeOfDailyPerformance.getBreakTimeSheets();

				for (BreakTimeSheet breakTimeSheet : breakTimeSheets) {
					if (breakTimeSheet.getStartTime().getTimeWithDay() == null) {

						List<Integer> attendanceItemIDList = new ArrayList<>();

						if (breakTimeSheet.getBreakFrameNo().equals(new WorkNo(new BigDecimal(1)))) {
							attendanceItemIDList.add(157);
						} else if (breakTimeSheet.getBreakFrameNo().equals(new WorkNo(new BigDecimal(2)))) {
							attendanceItemIDList.add(163);
						} else if (breakTimeSheet.getBreakFrameNo().equals(new WorkNo(new BigDecimal(3)))) {
							attendanceItemIDList.add(169);
						} else if (breakTimeSheet.getBreakFrameNo().equals(new WorkNo(new BigDecimal(4)))) {
							attendanceItemIDList.add(175);
						} else if (breakTimeSheet.getBreakFrameNo().equals(new WorkNo(new BigDecimal(5)))) {
							attendanceItemIDList.add(181);
						} else if (breakTimeSheet.getBreakFrameNo().equals(new WorkNo(new BigDecimal(6)))) {
							attendanceItemIDList.add(187);
						} else if (breakTimeSheet.getBreakFrameNo().equals(new WorkNo(new BigDecimal(7)))) {
							attendanceItemIDList.add(193);
						} else if (breakTimeSheet.getBreakFrameNo().equals(new WorkNo(new BigDecimal(8)))) {
							attendanceItemIDList.add(199);
						} else if (breakTimeSheet.getBreakFrameNo().equals(new WorkNo(new BigDecimal(9)))) {
							attendanceItemIDList.add(205);
						} else if (breakTimeSheet.getBreakFrameNo().equals(new WorkNo(new BigDecimal(10)))) {
							attendanceItemIDList.add(211);
						}

						createEmployeeDailyPerError.createEmployeeDailyPerError(companyID, employeeID, processingDate,
								new ErrorAlarmWorkRecordCode("S001"), attendanceItemIDList);
					}

					if (breakTimeSheet.getEndTime().getTimeWithDay() == null) {
						List<Integer> attendanceItemIDList = new ArrayList<>();

						if (breakTimeSheet.getBreakFrameNo().equals(new WorkNo(new BigDecimal(1)))) {
							attendanceItemIDList.add(159);
						} else if (breakTimeSheet.getBreakFrameNo().equals(new WorkNo(new BigDecimal(2)))) {
							attendanceItemIDList.add(165);
						} else if (breakTimeSheet.getBreakFrameNo().equals(new WorkNo(new BigDecimal(3)))) {
							attendanceItemIDList.add(171);
						} else if (breakTimeSheet.getBreakFrameNo().equals(new WorkNo(new BigDecimal(4)))) {
							attendanceItemIDList.add(177);
						} else if (breakTimeSheet.getBreakFrameNo().equals(new WorkNo(new BigDecimal(5)))) {
							attendanceItemIDList.add(183);
						} else if (breakTimeSheet.getBreakFrameNo().equals(new WorkNo(new BigDecimal(6)))) {
							attendanceItemIDList.add(189);
						} else if (breakTimeSheet.getBreakFrameNo().equals(new WorkNo(new BigDecimal(7)))) {
							attendanceItemIDList.add(195);
						} else if (breakTimeSheet.getBreakFrameNo().equals(new WorkNo(new BigDecimal(8)))) {
							attendanceItemIDList.add(201);
						} else if (breakTimeSheet.getBreakFrameNo().equals(new WorkNo(new BigDecimal(9)))) {
							attendanceItemIDList.add(207);
						} else if (breakTimeSheet.getBreakFrameNo().equals(new WorkNo(new BigDecimal(10)))) {
							attendanceItemIDList.add(213);
						}

						createEmployeeDailyPerError.createEmployeeDailyPerError(companyID, employeeID, processingDate,
								new ErrorAlarmWorkRecordCode("S001"), attendanceItemIDList);
					}
				}

			}
		}
	}

}

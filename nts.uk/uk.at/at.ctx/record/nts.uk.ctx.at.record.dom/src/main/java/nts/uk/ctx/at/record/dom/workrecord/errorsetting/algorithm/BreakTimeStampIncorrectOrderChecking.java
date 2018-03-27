package nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeSheet;
import nts.uk.ctx.at.record.dom.breakorgoout.primitivevalue.BreakFrameNo;
import nts.uk.ctx.at.record.dom.breakorgoout.repository.BreakTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.algorithm.CreateEmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.ErrorAlarmWorkRecordCode;

/*
 * 休憩系打刻順序不正をチェックする
 */
@Stateless
public class BreakTimeStampIncorrectOrderChecking {

	@Inject
	private BreakTimeOfDailyPerformanceRepository breakTimeOfDailyPerformanceRepository;

	@Inject
	private CreateEmployeeDailyPerError createEmployeeDailyPerError;

	public void breakTimeStampIncorrectOrderChecking(String companyId, String employeeId, GeneralDate processingDate, BreakTimeOfDailyPerformance breakTimeOfDailyPerformance) {
//		List<BreakTimeOfDailyPerformance> breakTimeOfDailyPerformances = breakTimeOfDailyPerformanceRepository
//				.findByKey(employeeId, processingDate);
		if (breakTimeOfDailyPerformance != null && !breakTimeOfDailyPerformance.getBreakTimeSheets().isEmpty()) {

			List<BreakTimeSheet> breakTimeSheets = breakTimeOfDailyPerformance.getBreakTimeSheets();

			breakTimeSheets.sort((e1, e2) -> e1.getStartTime().v()
					.compareTo(e2.getStartTime().v()));

			int breakFrameNo = 1;
			for (BreakTimeSheet item : breakTimeSheets) {
				item = new BreakTimeSheet(new BreakFrameNo(breakFrameNo), item.getStartTime(), item.getEndTime(), item.getBreakTime());
				breakFrameNo++;
			}

			for (BreakTimeSheet breakTimeSheet : breakTimeSheets) {
				
				List<Integer> attendanceItemIDList = new ArrayList<>();

				if (breakTimeSheet.getBreakFrameNo().equals(new BreakFrameNo(1))) {
					attendanceItemIDList.add(157);
					attendanceItemIDList.add(159);
				} else if (breakTimeSheet.getBreakFrameNo().equals(new BreakFrameNo(2))) {
					attendanceItemIDList.add(163);
					attendanceItemIDList.add(165);
				} else if (breakTimeSheet.getBreakFrameNo().equals(new BreakFrameNo(3))) {
					attendanceItemIDList.add(169);
					attendanceItemIDList.add(171);
				} else if (breakTimeSheet.getBreakFrameNo().equals(new BreakFrameNo(4))) {
					attendanceItemIDList.add(175);
					attendanceItemIDList.add(177);
				} else if (breakTimeSheet.getBreakFrameNo().equals(new BreakFrameNo(5))) {
					attendanceItemIDList.add(181);
					attendanceItemIDList.add(183);
				} else if (breakTimeSheet.getBreakFrameNo().equals(new BreakFrameNo(6))) {
					attendanceItemIDList.add(187);
					attendanceItemIDList.add(189);
				} else if (breakTimeSheet.getBreakFrameNo().equals(new BreakFrameNo(7))) {
					attendanceItemIDList.add(193);
					attendanceItemIDList.add(195);
				} else if (breakTimeSheet.getBreakFrameNo().equals(new BreakFrameNo(8))) {
					attendanceItemIDList.add(199);
					attendanceItemIDList.add(201);
				} else if (breakTimeSheet.getBreakFrameNo().equals(new BreakFrameNo(9))) {
					attendanceItemIDList.add(205);
					attendanceItemIDList.add(207);
				} else if (breakTimeSheet.getBreakFrameNo().equals(new BreakFrameNo(10))) {
					attendanceItemIDList.add(211);
					attendanceItemIDList.add(213);
				}
				
				if(breakTimeSheet.getStartTime().greaterThan(breakTimeSheet.getEndTime())){
					createEmployeeDailyPerError.createEmployeeDailyPerError(companyId, employeeId, processingDate,
							new ErrorAlarmWorkRecordCode("S004"), attendanceItemIDList);
				}
			}
		}
	}
}

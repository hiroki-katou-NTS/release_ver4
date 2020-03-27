package nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.domainservice;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.record.dom.worktime.TimeActualStamp;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingWork;

/**
 * DS : タイムカードを取得する
 * UKDesign.ドメインモデル.NittsuSystem.UniversalK.就業.contexts.勤務実績.勤務実績.打刻管理.打刻.タイムカードを取得する
 * @author tutk
 *
 */
public class GetTimeCardService {

	public static TimeCard getTimeCard(Require required, String employeeId, YearMonth yearMonth) {
		DatePeriod datePeriod = calculatePeriod(yearMonth);

		List<TimeLeavingOfDailyPerformance> listTimeLeavingOfDailyPer = required.findbyPeriodOrderByYmd(employeeId,
				datePeriod);

		List<AttendanceOneDay> listAttendanceOneDay = getListAttendanceOneDay(listTimeLeavingOfDailyPer);

		return new TimeCard(employeeId, listAttendanceOneDay);
	}

	// [prv-1] 実績を取得する期間を求める
	private static DatePeriod calculatePeriod(YearMonth yearMonth) {
		return new DatePeriod(GeneralDate.ymd(yearMonth.year(), yearMonth.month(), 1), yearMonth.lastGeneralDate());
	}

	// [prv-2] 日々の実績を作成する
	private static List<AttendanceOneDay> getListAttendanceOneDay(
			List<TimeLeavingOfDailyPerformance> listTimeLeavingOfDailyPer) {

		return listTimeLeavingOfDailyPer.stream().map(c -> createAttendanceOneDay(c.getYmd(),c.getTimeLeavingWorks()))
				.collect(Collectors.toList());
	}

	// [prv-3] 1日の出退勤を作成する
	private static AttendanceOneDay createAttendanceOneDay(GeneralDate ymd , List<TimeLeavingWork> timeLeavingWorks) {
		Optional<TimeActualStamp> attendance1 = Optional.empty();
		Optional<TimeActualStamp> leavingStamp1 = Optional.empty();
		Optional<TimeActualStamp> attendance2 = Optional.empty();
		Optional<TimeActualStamp> leavingStamp2 = Optional.empty();

		for (TimeLeavingWork timeLeavingWork : timeLeavingWorks) {
			if (timeLeavingWork.getWorkNo().v() == 1) {
				attendance1 = timeLeavingWork.getAttendanceStamp();
				leavingStamp1 = timeLeavingWork.getLeaveStamp();
			} else if (timeLeavingWork.getWorkNo().v() == 2) {
				attendance2 = timeLeavingWork.getAttendanceStamp();
				leavingStamp2 = timeLeavingWork.getLeaveStamp();
			}
		}

		return new AttendanceOneDay(ymd, attendance1, leavingStamp1, attendance2, leavingStamp2);
	}

	public static interface Require {

		/**
		 * [R-1] 日別実績の出退勤を取得する class : TimeLeavingOfDailyPerformanceRepository
		 * 
		 * @param employeeId
		 * @param datePeriod
		 * @return
		 */

		List<TimeLeavingOfDailyPerformance> findbyPeriodOrderByYmd(String employeeId, DatePeriod datePeriod);
	}
}

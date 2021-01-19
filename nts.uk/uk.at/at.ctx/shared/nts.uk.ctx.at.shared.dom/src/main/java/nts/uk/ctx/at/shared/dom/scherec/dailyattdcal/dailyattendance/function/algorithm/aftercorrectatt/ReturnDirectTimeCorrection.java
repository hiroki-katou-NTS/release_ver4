package nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.function.algorithm.aftercorrectatt;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.attendancetime.TimeLeavingOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.attendancetime.TimeLeavingWork;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.breakouting.OutingTimeOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.breakouting.OutingTimeSheet;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.common.TimeActualStamp;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.common.timestamp.ReasonTimeChange;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.common.timestamp.TimeChangeMeans;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.common.timestamp.WorkStamp;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.common.timestamp.WorkTimeInformation;

/**
 * @author ThanhNX
 *
 *         直行直帰による、戻り時刻補正
 */
public class ReturnDirectTimeCorrection {

	// 直行直帰による、戻り時刻補正
	public static Optional<OutingTimeOfDailyAttd> process(String comapnyId, TimeLeavingOfDailyAttd attendanceLeave,
			Optional<OutingTimeOfDailyAttd> outingTime) {
		// 退勤が直行直帰の出退勤を取得する
		// domain not map
		List<TimeLeavingWork> lstTimeLeaving = attendanceLeave.getTimeLeavingWorks().stream().filter(x -> {
			return hasGoOutBack(x);
		}).collect(Collectors.toList());

		if (!outingTime.isPresent())
			return outingTime;

		List<OutingTimeSheet> outingTimeSheets = outingTime.get().getOutingTimeSheets();
		for (TimeLeavingWork leavWork : lstTimeLeaving) {
			Integer timeAttendance = leavWork.getAttendanceStamp().get().getStamp().get().getTimeDay()
					.getTimeWithDay().get().v();
			Integer timeLeave = leavWork.getLeaveStamp().get().getStamp().get().getTimeDay().getTimeWithDay()
					.get().v();

			// 出勤～退勤の中で一番最後の外出時間帯を取得
			// 補正対象かどうか判断
			// 戻り時刻を退勤時刻で補正する
			Optional<OutingTimeSheet> outingTimeSheet = outingTime.get().getOutingTimeSheets().stream()
					.filter(x -> (!x.getComeBack().isPresent() || !x.getComeBack().isPresent()) && x.getGoOut().isPresent()
							&& x.getGoOut().get().getTimeDay().getTimeWithDay().isPresent()
							&& inRange(
									x.getGoOut().get().getTimeDay().getTimeWithDay().get().v(),
									timeAttendance, timeLeave))
					.sorted((x, y) -> compareTime(x, y)).findFirst();

			outingTimeSheet.ifPresent(x -> {
				//outingTimeSheets.set(index, setOutTimeSheet(x, leavWork));
				//#111712
				//戻り.打刻.時刻.時刻←退勤.打刻.時刻.時刻
				//戻り.打刻.時刻.時刻変更理由.時刻変更手段←”自動セット”
				WorkStamp stampLeav= leavWork.getLeaveStamp().map(y -> y.getStamp().orElse(null)).orElse(null);
				x.setComeBack(Optional.of(
						new WorkStamp(new WorkTimeInformation(new ReasonTimeChange(TimeChangeMeans.AUTOMATIC_SET, Optional.empty()),
										stampLeav == null ? null
												: stampLeav.getTimeDay().getTimeWithDay().orElse(null)),
								Optional.empty())
						));
			});
		}
		return Optional.ofNullable(outingTimeSheets.isEmpty() ? null : new OutingTimeOfDailyAttd(outingTimeSheets));
	}

	private static boolean inRange(Integer check, Integer min, Integer max) {
		return check >= min && check <= max;
	}

	private static Integer compareTime(OutingTimeSheet goOut1, OutingTimeSheet goOut2) {
		return goOut2.getGoOut().get().getTimeDay().getTimeWithDay().get().v()
				- goOut1.getGoOut().get().getTimeDay().getTimeWithDay().get().v();
	}

	private static boolean hasActualStamp(TimeLeavingWork timeLeav) {
		return timeLeav.getLeaveStamp().isPresent() && timeLeav.getLeaveStamp().get().getStamp().isPresent()
				&& timeLeav.getAttendanceStamp().isPresent()
				&& timeLeav.getAttendanceStamp().get().getStamp().isPresent();
	}

	private static boolean hasGoOutBack(TimeLeavingWork timeLeav) {
		if (!hasActualStamp(timeLeav))
			return false;
		WorkTimeInformation workInfoLeav = timeLeav.getLeaveStamp().get().getStamp().get().getTimeDay();
		WorkTimeInformation workInfoAtt = timeLeav.getAttendanceStamp().get().getStamp().get().getTimeDay();
		return (workInfoLeav.getReasonTimeChange().getTimeChangeMeans() == TimeChangeMeans.DIRECT_BOUNCE
				|| workInfoLeav.getReasonTimeChange().getTimeChangeMeans() == TimeChangeMeans.DIRECT_BOUNCE_APPLICATION)
				&& workInfoLeav.getTimeWithDay().isPresent() && workInfoAtt.getTimeWithDay().isPresent();

	}
}

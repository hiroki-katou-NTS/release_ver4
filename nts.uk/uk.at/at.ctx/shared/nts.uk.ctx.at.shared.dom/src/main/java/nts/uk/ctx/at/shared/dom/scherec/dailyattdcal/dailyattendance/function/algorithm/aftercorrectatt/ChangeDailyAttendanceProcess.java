package nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.function.algorithm.aftercorrectatt;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.attendancetime.TimeLeavingWork;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.function.timestamp.ChangeAttendanceTimeStamp;

/**
 * @author thanh_nx
 *
 *         日別実績の出退勤を変更する
 */
@Stateless
public class ChangeDailyAttendanceProcess {

	@Inject
	private ChangeAttendanceTimeStamp changeAttendanceTimeStamp;

	public List<TimeLeavingWork> changeDaily(String companyId, List<TimeLeavingWork> lstTimeLeavNew,
			List<TimeLeavingWork> lstTimeLeav) {

		//「日別実績の出退勤．出退勤」の時刻をemptyにする
		lstTimeLeav.removeIf(x -> {
			return !lstTimeLeavNew.stream().filter(y -> y.getWorkNo().v() == x.getWorkNo().v()).findFirst().isPresent();
		});
		
		// INPUT．「出退勤（List）」をループする
		for (TimeLeavingWork newData : lstTimeLeavNew) {

			// INPUT．「日別実績の出退勤．出退勤．出勤．打刻」を確認する
			Optional<TimeLeavingWork> timeLeavOpt = lstTimeLeav.stream()
					.filter(x -> x.getWorkNo().v() == newData.getWorkNo().v()).findFirst();
			if (!timeLeavOpt.isPresent()) {
				//insert：  出退勤が存在する && 日別実績の出退勤．出退勤．勤務NO = 処理中の勤務NOが存在しない
				// 「日別実績の出退勤．出退勤」に処理中の「出退勤」を追加する
				lstTimeLeav.add(newData);
				continue;
			}

			//update：出退勤が存在する && 日別実績の出退勤．出退勤．勤務NO = 処理中の勤務NOが存在する
			// INPUT．「日別実績の出退勤．出退勤．出勤．打刻」を確認する
			if (checkHasAtt(timeLeavOpt.get())) {
				// 勤怠打刻を変更する
				changeAttendanceTimeStamp.change(companyId,
						timeLeavOpt.get().getAttendanceStamp().get().getStamp().get(),
						newData.getAttendanceStamp().get().getStamp().get());
			} else {
				// 処理中の「出退勤．出勤．打刻」 をセットする
				if (timeLeavOpt.get().getAttendanceStamp().isPresent() && newData.getAttendanceStamp().isPresent()) {
					timeLeavOpt.get().getAttendanceStamp().get()
							.setStamp(newData.getAttendanceStamp().get().getStamp());
				}
			}

			if (checkHasLeav(timeLeavOpt.get())) {
				// 勤怠打刻を変更する
				changeAttendanceTimeStamp.change(companyId, timeLeavOpt.get().getLeaveStamp().get().getStamp().get(),
						newData.getLeaveStamp().get().getStamp().get());
			} else {
				// 処理中の「出退勤．出勤．打刻」 をセットする
				if (timeLeavOpt.get().getLeaveStamp().isPresent() && newData.getLeaveStamp().isPresent()) {
					timeLeavOpt.get().getLeaveStamp().get().setStamp(newData.getLeaveStamp().get().getStamp());
				}
			}
		}

		return lstTimeLeav;
	}

	private boolean checkHasAtt(TimeLeavingWork timeLeav) {
		return timeLeav.getAttendanceStamp().isPresent() && timeLeav.getAttendanceStamp().get().getStamp().isPresent();
	}

	private boolean checkHasLeav(TimeLeavingWork timeLeav) {
		return timeLeav.getLeaveStamp().isPresent() && timeLeav.getLeaveStamp().get().getStamp().isPresent();
	}
}

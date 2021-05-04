package nts.uk.screen.at.app.kdw013.a;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.record.dom.daily.ouen.SupportFrameNo;
import nts.uk.ctx.at.record.dom.daily.ouen.TimeZone;
import nts.uk.ctx.at.record.dom.daily.ouen.WorkDetailsParam;
import nts.uk.ctx.at.shared.dom.dailyattdcal.dailyattendance.IntegrationOfDailyGetter;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.attendancetime.TimeLeavingWork;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.dailyattendancework.IntegrationOfDaily;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.timesheet.ouen.OuenWorkTimeOfDailyAttendance;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.timesheet.ouen.OuenWorkTimeSheetOfDailyAttendance;

/**
 * UKDesign.UniversalK.就業.KDW_日別実績.KDW013_工数入力.A:工数入力.メニュー別OCD.日別実績データを取得する
 * 
 * @author tutt
 *
 */
@Stateless
public class GetDailyPerformanceData {

	@Inject
	private IntegrationOfDailyGetter getter;

	/**
	 * 
	 * @param sId    社員ID
	 * @param period
	 * @return
	 */
	public List<WorkRecordDetail> get(String sId, DatePeriod period) {
		List<WorkRecordDetail> lstWorkRecordDetail = new ArrayList<>();

		// 1: get(社員ID,期間)
		List<IntegrationOfDaily> lstIntegrationOfDaily = getter.getIntegrationOfDaily(sId, period);

		for (IntegrationOfDaily i : lstIntegrationOfDaily) {
			List<TimeLeavingWork> timeLeavingWorks = i.getAttendanceLeave().get().getTimeLeavingWorks();

			for (TimeLeavingWork time : timeLeavingWorks) {

				// 作業実績詳細
				WorkRecordDetail detail = new WorkRecordDetail();

				// 年月日
				detail.setDate(i.getYmd());
				// 社員ID
				detail.setSId(i.getEmployeeId());

				// 作業詳細リスト
				List<WorkDetailsParam> lstworkDetailsParam = new ArrayList<>();

				for (OuenWorkTimeSheetOfDailyAttendance a : i.getOuenTimeSheet()) {
					for (OuenWorkTimeOfDailyAttendance b : i.getOuenTime()) {
						WorkDetailsParam workDetailsParam = new WorkDetailsParam(
								new SupportFrameNo(a.getTimeSheet().getStart().get().getTimeWithDay().get().v()),
								new TimeZone(a.getTimeSheet().getStart().get(), a.getTimeSheet().getEnd().get(),
										Optional.ofNullable(b.getWorkTime().getTotalTime())),
								a.getWorkContent().getWork(), a.getWorkContent().getWorkRemarks(),
								a.getWorkContent().getWorkplace().getWorkLocationCD());
						lstworkDetailsParam.add(workDetailsParam);
					}
				}

				// 実績内容
				ActualContent actualContent = new ActualContent();

				// 休憩リスト
				actualContent.setBreakTimeSheets(i.getBreakTime().getBreakTimeSheets());
				// 休憩時間
				actualContent.setBreakHours(Optional.of(i.getAttendanceTimeOfDailyPerformance().get()
						.getActualWorkingTimeOfDaily().getTotalWorkingTime().getBreakTimeOfDaily().getWorkTime()));
				// 総労働時間
				actualContent.setTotalWorkingHours(Optional.of(i.getAttendanceTimeOfDailyPerformance().get()
						.getActualWorkingTimeOfDaily().getTotalWorkingTime().getTotalTime()));

				if (time.getWorkNo().v() == 1) {
					// 開始時刻
					actualContent
							.setStart(Optional.of(time.getAttendanceStamp().get().getActualStamp().get().getTimeDay()));

					// 終了時刻
					actualContent.setEnd(Optional.of(time.getLeaveStamp().get().getActualStamp().get().getTimeDay()));
				}

				// 実績内容
				detail.setActualContent(Optional.of(actualContent));

				// 作業詳細リスト
				detail.setLstworkDetailsParam(Optional.of(lstworkDetailsParam));

				lstWorkRecordDetail.add(detail);
			}
		}

		return lstWorkRecordDetail;
	}

}

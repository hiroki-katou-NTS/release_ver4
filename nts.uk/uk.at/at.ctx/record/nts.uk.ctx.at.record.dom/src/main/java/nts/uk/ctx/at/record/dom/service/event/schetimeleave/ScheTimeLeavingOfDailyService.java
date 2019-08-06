package nts.uk.ctx.at.record.dom.service.event.schetimeleave;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.editstate.EditStateOfDailyPerformance;
import nts.uk.ctx.at.record.dom.editstate.enums.EditStateSetting;
import nts.uk.ctx.at.record.dom.workinformation.ScheduleTimeSheet;
import nts.uk.ctx.at.shared.dom.worktime.predset.TimezoneUse;
import nts.uk.ctx.at.shared.dom.worktime.predset.UseSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingService;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.internal.PredetermineTimeSetForCalc;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
@Stateless
public class ScheTimeLeavingOfDailyService {
	@Inject
	private WorkTypeRepository worktypeRepo;
	@Inject
	private WorkTimeSettingService workTimeService;
	/**
	 * 予定出退勤時刻を反映する
	 * @param cid
	 * @param dailyInfor
	 * @return
	 */
	public void correct(String cid, String workTypeCd, Optional<String> workTimeCd,
			Optional<Integer> startTime, Optional<Integer> endTime,	IntegrationOfDaily dailyInfor) {
		if(dailyInfor.getWorkInformation().getScheduleInfo() == null
				|| dailyInfor.getWorkInformation().getScheduleInfo().getWorkTimeCode() == null
				|| dailyInfor.getWorkInformation().getScheduleInfo().getWorkTypeCode() == null) {
			if(dailyInfor.getWorkInformation().getScheduleInfo() != null 
					&& dailyInfor.getWorkInformation().getScheduleInfo().getWorkTimeCode() == null) {
				List<ScheduleTimeSheet> lstScheduleTimeSheet = dailyInfor.getWorkInformation().getScheduleTimeSheets();
				List<ScheduleTimeSheet> lstScheduleTimeSheetNo1 = lstScheduleTimeSheet.stream()
						.filter(x -> x.getWorkNo().v() == 1).collect(Collectors.toList());
				if(!lstScheduleTimeSheetNo1.isEmpty()) {
					lstScheduleTimeSheet.remove(0);
					dailyInfor.getWorkInformation().setScheduleTimeSheets(lstScheduleTimeSheet);
				}
			}
			return;
		}
		//勤務種類を取得する
		Optional<WorkType> workTypeInfor = worktypeRepo.findByPK(cid, workTypeCd);
		//キャッシュ「日別実績（Edit）．日別実績（Work)．編集状態」をチェックする
		List<EditStateOfDailyPerformance> lstScheTime = dailyInfor.getEditState().stream().filter(x -> (x.getAttendanceItemId() == 3 || x.getAttendanceItemId() == 4) 
				&& x.getEditStateSetting() != EditStateSetting.REFLECT_APPLICATION).collect(Collectors.toList());
		if(!lstScheTime.isEmpty()) {
			return;
		}
		WorkType workType = workTypeInfor.get();
		//取得したドメインモデル「勤務種類．一日の勤務．勤務区分」をチェックする
		if((workType.getDailyWork().isOneDay()
				&& workType.getDailyWork().IsLeaveForADay()) //取得したドメインモデル「勤務種類．一日の勤務．一日」をチェックする
				|| (!workType.getDailyWork().isOneDay() //取得したドメインモデル「勤務種類．一日の勤務．午前」と「勤務種類．一日の勤務．午後」をチェックする
						&& workType.getDailyWork().IsLeaveForMorning() && workType.getDailyWork().IsLeaveForAfternoon())) {
				return;
		}
		//INPUT．「開始時刻」とINPUT．「終了時刻」をチェックする
		if(startTime.isPresent() || endTime.isPresent()) {
			return;
		}
		//所定時間帯を取得する
		PredetermineTimeSetForCalc getTimezone = workTimeService.getPredeterminedTimezone(cid, 
				dailyInfor.getWorkInformation().getScheduleInfo().getWorkTimeCode().v(),
				dailyInfor.getWorkInformation().getScheduleInfo().getWorkTypeCode().v(),
				1);
		
		List<TimezoneUse> lstTimeZone = getTimezone.getTimezones().stream()
				.filter(x -> x.getUseAtr() == UseSetting.USE && x.getWorkNo() == 1)
				.collect(Collectors.toList());
		if(lstTimeZone.isEmpty()) {
			return;
		}
		TimezoneUse timeZone = lstTimeZone.get(0);
		List<ScheduleTimeSheet> scheduleTimeSheets = dailyInfor.getWorkInformation().getScheduleTimeSheets().stream().map(y -> {
			//パラメータ「補正後日別実績．日別実績(Work)．勤務情報．勤務予定の勤務情報．勤務予定時間帯．出勤」　←　取得した「時間帯(使用区分付き)．開始」
			return new ScheduleTimeSheet(y.getWorkNo().v(), timeZone.getStart().v(), timeZone.getEnd().v());
		}).collect(Collectors.toList());
		if(scheduleTimeSheets.isEmpty()) {
			ScheduleTimeSheet scheTime = new ScheduleTimeSheet(1, timeZone.getStart().v(), timeZone.getEnd().v());
			dailyInfor.getWorkInformation().getScheduleTimeSheets().add(scheTime);
		} else {
			dailyInfor.getWorkInformation().setScheduleTimeSheets(scheduleTimeSheets);	
		}
		//マージした出退勤時刻の「編集状態」を更新するする
		List<EditStateOfDailyPerformance> lstEditScheStartTime = dailyInfor.getEditState().stream()
				.filter(x -> x.getAttendanceItemId() == 3)
				.collect(Collectors.toList());
		if(lstEditScheStartTime.isEmpty()) {
			EditStateOfDailyPerformance editScheStartTime = new EditStateOfDailyPerformance(dailyInfor.getWorkInformation().getEmployeeId(),
					3, dailyInfor.getWorkInformation().getYmd(),
					EditStateSetting.REFLECT_APPLICATION);
			dailyInfor.getEditState().add(editScheStartTime);
		}
		List<EditStateOfDailyPerformance> lstEditScheEndTime = dailyInfor.getEditState().stream()
				.filter(x -> x.getAttendanceItemId() == 4)
				.collect(Collectors.toList());
		if(lstEditScheEndTime.isEmpty()) {
			EditStateOfDailyPerformance editScheEndTime = new EditStateOfDailyPerformance(dailyInfor.getWorkInformation().getEmployeeId(),
					4, dailyInfor.getWorkInformation().getYmd(),
					EditStateSetting.REFLECT_APPLICATION);
			dailyInfor.getEditState().add(editScheEndTime);
		}
	}
}

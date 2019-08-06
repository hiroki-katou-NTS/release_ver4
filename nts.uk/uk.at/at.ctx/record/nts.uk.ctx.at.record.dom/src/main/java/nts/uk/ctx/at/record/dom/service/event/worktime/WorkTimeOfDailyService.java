package nts.uk.ctx.at.record.dom.service.event.worktime;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.editstate.EditStateOfDailyPerformance;
import nts.uk.ctx.at.record.dom.editstate.enums.EditStateSetting;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.BasicScheduleService;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.SetupType;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
@Stateless
public class WorkTimeOfDailyService {
	@Inject
	private BasicScheduleService basicService;
	@Inject
	private WorkingConditionItemRepository workingCondition;
	public IntegrationOfDaily correct(String companyId, IntegrationOfDaily working){
		if(working.getWorkInformation().getRecordInfo() == null
				|| working.getWorkInformation().getRecordInfo().getWorkTypeCode() == null) {
			return working;
		}
		String workType = working.getWorkInformation().getRecordInfo().getWorkTypeCode().v();
		String sid = working.getWorkInformation().getEmployeeId();
		GeneralDate ymd = working.getWorkInformation().getYmd();
		//就業時間帯の必須チェック
		SetupType checkNeededOfWorkTime = basicService.checkNeededOfWorkTimeSetting(workType);
		if(checkNeededOfWorkTime == SetupType.NOT_REQUIRED) {
			working.getWorkInformation().getRecordInfo().setSiftCode(null);
		} else if(checkNeededOfWorkTime == SetupType.REQUIRED) {
			if(working.getWorkInformation().getRecordInfo().getWorkTimeCode() == null) {
				List<EditStateOfDailyPerformance> lstEditWorktime = working.getEditState().stream().filter(x -> x.getAttendanceItemId() == 29)
						.collect(Collectors.toList());
				List<EditStateOfDailyPerformance> lstEditScheWorktime = working.getEditState().stream().filter(x -> x.getAttendanceItemId() == 2)
						.collect(Collectors.toList());
				//社員の労働条件を取得する
				Optional<WorkingConditionItem> optWorkingCondition = workingCondition.getBySidAndPeriodOrderByStrD(sid,
						new DatePeriod(ymd,ymd)).stream().findFirst();
				optWorkingCondition.ifPresent(x -> {
					x.getWorkCategory().getWeekdayTime().getWorkTimeCode().ifPresent(y -> {
						working.getWorkInformation().getRecordInfo().setSiftCode(y);
						lstEditWorktime.stream().forEach(a -> a.setEditStateSetting(EditStateSetting.REFLECT_APPLICATION));
						if(lstEditWorktime.isEmpty()) {
							working.getEditState().add(new EditStateOfDailyPerformance(sid, 29, ymd, EditStateSetting.REFLECT_APPLICATION));
						}
						if(working.getWorkInformation().getScheduleInfo() != null
								&& working.getWorkInformation().getScheduleInfo().getWorkTimeCode() == null) {
							working.getWorkInformation().getScheduleInfo().setSiftCode(y);
							lstEditScheWorktime.stream().forEach(b -> b.setEditStateSetting(EditStateSetting.REFLECT_APPLICATION));
							if(lstEditScheWorktime.isEmpty()) {
								working.getEditState().add(new EditStateOfDailyPerformance(sid, 2, ymd, EditStateSetting.REFLECT_APPLICATION));
							}
						}
					});
				});
			}
		}
		return working;
	}
}

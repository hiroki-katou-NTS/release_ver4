package nts.uk.ctx.at.record.app.command.dailyperform;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.app.command.dailyperform.breaktime.UpdateBreakTimeByTimeLeaveChangeCommand;
import nts.uk.ctx.at.record.app.command.dailyperform.breaktime.UpdateBreakTimeByTimeLeaveChangeHandler;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.BreakTimeOfDailyPerformanceCorrectService;
import nts.uk.ctx.at.record.dom.editstate.EditStateOfDailyPerformance;
import nts.uk.ctx.at.record.dom.service.event.common.EventHandleResult;
import nts.uk.ctx.at.record.dom.service.event.common.EventHandleResult.EventHandleAction;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;

@Stateless
public class BreakTimeOfDailyPerformanceCorrectServiceImpl implements BreakTimeOfDailyPerformanceCorrectService {

	@Inject
	private UpdateBreakTimeByTimeLeaveChangeHandler breakTimeCorrectHandler;
	
	@Override
	public Optional<BreakTimeOfDailyPerformance> correct(String cid, WorkInfoOfDailyPerformance workInfo,
			List<EditStateOfDailyPerformance> editState, Optional<TimeLeavingOfDailyPerformance> timeLeave,
			Optional<BreakTimeOfDailyPerformance> oldRecordBreak, Optional<WorkType> workType) {
		
		UpdateBreakTimeByTimeLeaveChangeCommand breakTimeEvent = (UpdateBreakTimeByTimeLeaveChangeCommand) UpdateBreakTimeByTimeLeaveChangeCommand
				.builder()
				.cachedBreackTime(oldRecordBreak.orElse(null))
				.employeeId(workInfo.getEmployeeId())
				.targetDate(workInfo.getYmd())
				.companyId(cid)
				.cachedEditState(editState)
				.cachedWorkInfo(workInfo)
				.actionOnCache(true)
				.cachedTimeLeave(timeLeave.orElse(null))
				.cachedWorkType(workType.orElse(null))
				.isTriggerRelatedEvent(false)
				.build();
		
		EventHandleResult<BreakTimeOfDailyPerformance>  breakTimeCorrected = breakTimeCorrectHandler.handle(breakTimeEvent);
		
		if (breakTimeCorrected.getAction() == EventHandleAction.ABORT || breakTimeCorrected.getAction() == EventHandleAction.DELETE)
			return Optional.empty();
		
		return Optional.of(breakTimeCorrected.getData());
	}

}

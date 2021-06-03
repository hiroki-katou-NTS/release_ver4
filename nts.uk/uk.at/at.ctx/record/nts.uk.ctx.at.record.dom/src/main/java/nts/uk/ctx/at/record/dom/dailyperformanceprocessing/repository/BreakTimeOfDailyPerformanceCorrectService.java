package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository;

import java.util.List;
import java.util.Optional;

import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.editstate.EditStateOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;

public interface BreakTimeOfDailyPerformanceCorrectService {

	Optional<BreakTimeOfDailyPerformance> correct(String cid, WorkInfoOfDailyPerformance workInfo, 
			List<EditStateOfDailyPerformance> editState, Optional<TimeLeavingOfDailyPerformance> timeLeave, 
			Optional<BreakTimeOfDailyPerformance> oldRecordBreak, Optional<WorkType> workType);
}

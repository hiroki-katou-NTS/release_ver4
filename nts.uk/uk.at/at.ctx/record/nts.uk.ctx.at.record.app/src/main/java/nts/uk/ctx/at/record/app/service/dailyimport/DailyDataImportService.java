package nts.uk.ctx.at.record.app.service.dailyimport;

import java.util.List;
import java.util.function.Supplier;

import nts.arc.task.data.TaskDataSetter;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.editstate.EditStateOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface DailyDataImportService {

	List<RecordImportError> importAt(DatePeriod period, TaskDataSetter dataSetter, Supplier<Boolean> isCanceled);
	
	void removePreData(DatePeriod period, List<String> empIds);
	
	void finishUpdate(List<WorkInfoOfDailyPerformance> cWorkInfo, List<TimeLeavingOfDailyPerformance> cTimeLeave,
			List<BreakTimeOfDailyPerformance> cBreak, List<EditStateOfDailyPerformance> edits);
}

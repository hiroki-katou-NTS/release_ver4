package nts.uk.ctx.at.function.dom.adapter.executionlog;

import java.util.List;

import nts.arc.time.GeneralDate;

public interface ScheduleErrorLogAdapter {
	public List<ScheduleErrorLogImport> findByExecutionId(String executionId);

	public Boolean checkExistErrorByKey(String executionId, String employeeId, GeneralDate baseDate);

	public void add(ScheduleErrorLogImport domain);
}

package nts.uk.ctx.at.schedule.pub.executionlog;

import java.util.List;

import nts.arc.time.GeneralDate;

public interface ScheduleErrorLogPub {
	public List<ScheduleErrorLogEx> findByExecutionId(String executionId);
	
	public Boolean checkExistErrorByKey(String executionId, String employeeId, GeneralDate baseDate); 
	
	public void add(ScheduleErrorLogEx domain);
}

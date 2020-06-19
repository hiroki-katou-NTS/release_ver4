package nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDateTime;

public interface ExecutionLogRepository {

	Optional<ExecutionLog> getByExecutionContent(String empCalAndSumExecLogID, int executionContent);

	void addExecutionLog(ExecutionLog executionLog);

	void addAllExecutionLog(List<ExecutionLog> listExecutionLog);

	void updateLogInfo(String empCalAndSumExecLogID, int executionContent, int processStatus);

	List<ExecutionLog> getExecutionLogs(String empCalAndSumExecLogID);

	void updateExecutionDate(String empCalAndSumExecLogID, GeneralDateTime executionStartDate,
			GeneralDateTime executionEndDate, GeneralDateTime dailyCreateStartTime, GeneralDateTime dailyCreateEndTime,
			GeneralDateTime dailyCalculateStartTime, GeneralDateTime dailyCalculateEndTime,
			GeneralDateTime reflectApprovalStartTime, GeneralDateTime reflectApprovalEndTime,
			GeneralDateTime monthlyAggregateStartTime, GeneralDateTime monthlyAggregateEndTime,
			int stopped);
}

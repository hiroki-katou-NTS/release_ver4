package nts.uk.ctx.at.record.infra.repository.log;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ExecutionLog;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ExecutionLogRepository;
import nts.uk.ctx.at.record.infra.entity.log.KrcdtExecutionLog;

@Stateless
public class JpaExecutionLogRepository extends JpaRepository implements ExecutionLogRepository {

	private final String SELECT_BY_CAL_AND_SUM_EXE_ID = "SELECT el FROM KrcdtExecutionLog el "
			+ " WHERE el.krcdtExecutionLogPK.empCalAndSumExecLogID = :empCalAndSumExecLogID";

	private final String SELECT_BY_EXECUTION_LOG = "SELECT el FROM KrcdtExecutionLog el "
			+ " WHERE el.krcdtExecutionLogPK.empCalAndSumExecLogID = :empCalAndSumExecLogID AND el.krcdtExecutionLogPK.executionContent = :executionContent";

	@Override
	public Optional<ExecutionLog> getByExecutionContent(String empCalAndSumExecLogID, int executionContent) {
		Optional<ExecutionLog> optional = this.queryProxy().query(SELECT_BY_EXECUTION_LOG, KrcdtExecutionLog.class)
				.setParameter("empCalAndSumExecLogID", empCalAndSumExecLogID)
				.setParameter("executionContent", executionContent).getSingle(f -> f.toDomain());

		return optional;
	}

	@Override
	public void updateLogInfo(String empCalAndSumExecLogID, int executionContent, int processStatus) {
		Optional<KrcdtExecutionLog> krcdtExecutionLog = this.queryProxy()
				.query(SELECT_BY_EXECUTION_LOG, KrcdtExecutionLog.class)
				.setParameter("empCalAndSumExecLogID", empCalAndSumExecLogID)
				.setParameter("executionContent", executionContent).getSingle();
		if (krcdtExecutionLog.isPresent()) {
			krcdtExecutionLog.get().processStatus = processStatus;

			this.commandProxy().update(krcdtExecutionLog.get());
			this.getEntityManager().flush();

		}
	}

	@Override
	public List<ExecutionLog> getExecutionLogs(String empCalAndSumExecLogID) {
		return this.queryProxy().query(SELECT_BY_CAL_AND_SUM_EXE_ID, KrcdtExecutionLog.class)
				.setParameter("empCalAndSumExecLogID", empCalAndSumExecLogID).getList(c -> c.toDomain());

	}

	@Override
	public void addExecutionLog(ExecutionLog executionLog) {
		this.commandProxy().insert(KrcdtExecutionLog.toEntity(executionLog));
	}

	@Override
	public void addAllExecutionLog(List<ExecutionLog> listExecutionLog) {
		List<KrcdtExecutionLog> lstKrcdtExecutionLog = listExecutionLog.stream()
		.map(c -> KrcdtExecutionLog.toEntity(c)).collect(Collectors.toList());
		this.commandProxy().insertAll(lstKrcdtExecutionLog);
	}

}

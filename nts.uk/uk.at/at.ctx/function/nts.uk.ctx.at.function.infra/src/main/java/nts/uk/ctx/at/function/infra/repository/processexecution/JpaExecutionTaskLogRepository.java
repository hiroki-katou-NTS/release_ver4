package nts.uk.ctx.at.function.infra.repository.processexecution;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ExecutionTaskLog;
import nts.uk.ctx.at.function.dom.processexecution.repository.ExecutionTaskLogRepository;
import nts.uk.ctx.at.function.infra.entity.processexecution.KfnmtExecutionTaskLog;
import nts.uk.ctx.at.function.infra.entity.processexecution.KfnmtExecutionTaskLogPK;
//import nts.uk.ctx.at.function.infra.entity.processexecution.KfnmtProcessExecutionLog;

@Stateless
public class JpaExecutionTaskLogRepository extends JpaRepository
		implements ExecutionTaskLogRepository {
	/**
	 * Query strings
	 */
	private static final String SELECT_ALL = "SELECT etl FROM KfnmtExecutionTaskLog etl ";
	private static final String SELECT_LIST = SELECT_ALL
			+ "WHERE etl.kfnmtExecTaskLogPK.companyId = :companyId "
			+ "AND etl.kfnmtExecTaskLogPK.execItemCd = :execItemCd "
			+ "AND etl.kfnmtExecTaskLogPK.execId = :execId ";
	@Override
	public List<ExecutionTaskLog> getAllByCidExecCdExecId(String companyId, String execItemCd, String execId) {
		return this.queryProxy().query(SELECT_LIST, KfnmtExecutionTaskLog.class)
				.setParameter("companyId", companyId)
				.setParameter("execItemCd", execItemCd)
				.setParameter("execId", execId).getList(c -> c.toNewDomain());
	}
	
	/**
	 * insert task log list
	 */
	@Override
	public void insertAll(String companyId, String execItemCd, String execId, List<ExecutionTaskLog> taskLogList) {
		List<KfnmtExecutionTaskLog> entityList =
				taskLogList
					.stream()
						.map(task -> KfnmtExecutionTaskLog.toEntity(companyId,
																	execItemCd,
																	execId,
																	task)).collect(Collectors.toList());
		this.commandProxy().insertAll(entityList);
	}

	/**
	 * update task log list
	 */
	@Override
	public void updateAll(String companyId, String execItemCd, String execId, List<ExecutionTaskLog> taskLogList) {
		List<KfnmtExecutionTaskLog> entityList =
				taskLogList
					.stream()
						.map(task -> {
							KfnmtExecutionTaskLogPK pk = new KfnmtExecutionTaskLogPK(companyId, execItemCd, execId, task.getProcExecTask().value);
							Optional<KfnmtExecutionTaskLog> entityOpt = this.queryProxy().find(pk, KfnmtExecutionTaskLog.class);
							if (entityOpt.isPresent()) {
								KfnmtExecutionTaskLog entity = entityOpt.get();
								entity.status = (task.getStatus()!=null && task.getStatus().isPresent())?task.getStatus().get().value:null;
								return entity;
							}
							return null;
							
						}).collect(Collectors.toList());
																	
		this.commandProxy().updateAll(entityList);
	}
}

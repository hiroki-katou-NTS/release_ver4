package nts.uk.ctx.sys.assist.infra.repository.datarestoration;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.sys.assist.dom.datarestoration.DataRecoveryLog;
import nts.uk.ctx.sys.assist.dom.datarestoration.DataRecoveryLogRepository;
import nts.uk.ctx.sys.assist.infra.entity.datarestoration.SspdtRecoverLog;
import nts.uk.ctx.sys.assist.infra.entity.datarestoration.SspdtRecoverLogPk;

@Stateless
public class JpaDataRecoverLogRepository extends JpaRepository implements DataRecoveryLogRepository  {
	
	private static final String SELECT_ALL_QUERY_STRING = "SELECT f FROM SspdtRecoverLog f";
	private static final String SELECT_BY_KEY_STRING = SELECT_ALL_QUERY_STRING
			+ " WHERE  f.dataRecoverLogPk.recoveryProcessId = :recoveryProcessId ";
	
	private static final String GET_MAX = "SELECT MAX(f.dataRecoverLogPk.logSequenceNumber) "
			+ " FROM SspdtRecoverLog f WHERE f.dataRecoverLogPk.recoveryProcessId = :recoveryProcessId ";
	 
	@Override
	public int getMaxSeqId(String recoveryProcessId) {
		 Object max = this.queryProxy().query(GET_MAX, Object.class)
					.setParameter("recoveryProcessId", recoveryProcessId).getSingleOrNull();
		 if(max == null) {
			 return 0;
		 }else {
			 return (int)max;
		 }
	 }

	@Override
	public List<DataRecoveryLog> getAllResultLogDeletion() {
		return this.queryProxy().query(SELECT_ALL_QUERY_STRING, SspdtRecoverLog.class)
				.getList(item -> item.toDomain());
	}

	@Override
	public Optional<DataRecoveryLog> getResultLogDeletionById(String recoveryProcessId) {
		return this.queryProxy().query(SELECT_BY_KEY_STRING, SspdtRecoverLog.class)
				.setParameter("recoveryProcessId", recoveryProcessId).getSingle(c -> c.toDomain());
	}

	@Override
	public void add(DataRecoveryLog data) {
		SspdtRecoverLog entity = toEntity(data);
		this.commandProxy().insert(entity);
		this.getEntityManager().flush();
	}
	
	private static SspdtRecoverLog toEntity(DataRecoveryLog domain){
		val entity = new SspdtRecoverLog();
		val key = new SspdtRecoverLogPk();
		key.recoveryProcessId = domain.getRecoveryProcessId();
		key.logSequenceNumber = domain.getLogSequenceNumber();
		entity.dataRecoverLogPk = key;
		entity.contentSql = domain.getContentSql() == null ? "" : domain.getContentSql().v();
		entity.errorContent = domain.getErrorContent() == null ? "" : domain.getErrorContent().v();
		entity.processingContent = domain.getProcessingContent() == null ? "" : domain.getProcessingContent().v();
		entity.target = domain.getTarget();
		entity.targetDate = domain.getTargetDate();
		return entity;
	}
	
}

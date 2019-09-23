package nts.uk.ctx.at.function.infra.repository.processexecution;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionLog;
//import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionLogManage;
import nts.uk.ctx.at.function.dom.processexecution.repository.ProcessExecutionLogRepository;
import nts.uk.ctx.at.function.infra.entity.processexecution.KfnmtExecutionTaskLog;
import nts.uk.ctx.at.function.infra.entity.processexecution.KfnmtExecutionTaskLogPK;
import nts.uk.ctx.at.function.infra.entity.processexecution.KfnmtProcessExecutionLog;
//import nts.uk.ctx.at.function.infra.entity.processexecution.KfnmtProcessExecutionLogManage;
//import nts.uk.ctx.at.function.infra.entity.processexecution.KfnmtProcessExecutionLogPK;

@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Stateless
public class JpaProcessExecutionLogRepository extends JpaRepository
		implements ProcessExecutionLogRepository {
	/**
	 * Query strings
	 */
	private static final String SELECT_ALL = "SELECT pel FROM KfnmtProcessExecutionLog pel ";
	private static final String SELECT_All_BY_CID = SELECT_ALL
			+ "WHERE pel.kfnmtProcExecLogPK.companyId = :companyId ORDER BY pel.kfnmtProcExecLogPK.execItemCd ASC ";
	
	private static final String SELECT_BY_PK = SELECT_ALL
			+ "WHERE pel.kfnmtProcExecLogPK.companyId = :companyId "
			+ "AND pel.kfnmtProcExecLogPK.execItemCd = :execItemCd "
			+ "AND pel.kfnmtProcExecLogPK.execId = :execId ";
	
	private static final String SELECT_BY_KEY = SELECT_ALL 
			+ "WHERE pel.kfnmtProcExecLogPK.companyId = :companyId "
			+ "AND pel.kfnmtProcExecLogPK.execItemCd = :execItemCd ";
	
	private static final String SELECT_TASK_LOG = "SELECT k FROM KfnmtExecutionTaskLog k"+ 
	" WHERE k.kfnmtExecTaskLogPK.companyId = :companyId " + " AND k.kfnmtExecTaskLogPK.execItemCd= :execItemCd ";
	
	
	private static final String SELECT_BY_CID_AND_EXEC_CD = SELECT_ALL
			+ "WHERE pel.kfnmtProcExecLogPK.companyId = :companyId "
			+ "AND pel.kfnmtProcExecLogPK.execItemCd = :execItemCd ";
	
	private static final String SELECT_BY_KEY_NATIVE = "SELECT * FROM KFNMT_PROC_EXEC_LOG as pel WITH (READUNCOMMITTED)"
			+ "WHERE pel.CID = ? "
			+ "AND pel.EXEC_ITEM_CD = ? ";
	private static final String DELETE_BY_EXEC_CD = " DELETE FROM KfnmtProcessExecutionLog c "
			+ "WHERE c.kfnmtProcExecLogPK.companyId = :companyId "
			+ "AND c.kfnmtProcExecLogPK.execItemCd = :execItemCd ";
	private static final String SELECT_TASK_LOG_BY_JDBC = "SELECT * FROM KFNMT_EXEC_TASK_LOG "
			+ "WHERE CID = ? "
			+ "AND EXEC_ITEM_CD = ? ";
	@Override
	public List<ProcessExecutionLog> getProcessExecutionLogByCompanyId(String companyId) {
		return this.queryProxy().query(SELECT_All_BY_CID, KfnmtProcessExecutionLog.class)
				.setParameter("companyId", companyId).getList(c -> c.toDomain());
	}
	
	@Override
	public Optional<ProcessExecutionLog> getLogByCIdAndExecCd(String companyId, String execItemCd, String execId) {
		return this.queryProxy().query(SELECT_BY_PK, KfnmtProcessExecutionLog.class)
				.setParameter("companyId", companyId)
				.setParameter("execItemCd", execItemCd)
				.setParameter("execId", execId).getSingle(c -> c.toDomain());
	}
	
	@Override
	public void insert(ProcessExecutionLog domain) {
		this.commandProxy().insert(KfnmtProcessExecutionLog.toEntity(domain));
	}
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Override
	public void update(ProcessExecutionLog domain) {
		KfnmtProcessExecutionLog updateData = KfnmtProcessExecutionLog.toEntity(domain);
		Optional<KfnmtProcessExecutionLog> oldDataOtp = this.queryProxy().find(updateData.kfnmtProcExecLogPK, KfnmtProcessExecutionLog.class);
		if(!oldDataOtp.isPresent())
			return;
		KfnmtProcessExecutionLog oldData = oldDataOtp.get();
		oldData.schCreateStart = updateData.schCreateStart;
		oldData.schCreateEnd = updateData.schCreateEnd;
		oldData.dailyCreateStart = updateData.dailyCreateStart;
		oldData.dailyCreateEnd = updateData.dailyCreateEnd;
		oldData.dailyCalcStart = updateData.dailyCalcStart;
		oldData.dailyCalcEnd = updateData.dailyCalcEnd;
		oldData.reflectApprovalResultStart = updateData.reflectApprovalResultStart;
		oldData.reflectApprovalResultEnd = updateData.reflectApprovalResultEnd;
		oldData.taskLogList = updateData.taskLogList;
		this.commandProxy().update(oldData);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Override
	public void remove(String companyId, String execItemCd, String execId) {
		this.getEntityManager().createQuery(DELETE_BY_EXEC_CD, KfnmtProcessExecutionLog.class)
		.setParameter("companyId", companyId)
		.setParameter("execItemCd", execItemCd)
		.executeUpdate();
	}

	@Override
	public void removeList(String companyId, String execItemCd) {
		List<KfnmtProcessExecutionLog> logList = this.queryProxy().query(SELECT_BY_CID_AND_EXEC_CD, KfnmtProcessExecutionLog.class)
														.setParameter("companyId", companyId)
														.setParameter("execItemCd", execItemCd).getList();
		if (!CollectionUtil.isEmpty(logList)) {
			this.commandProxy().removeAll(logList);
		}
	}

	@Override
	public Optional<ProcessExecutionLog> getLog(String companyId, String execItemCd) {
//		 List<KfnmtExecutionTaskLog> list = this.queryProxy().query(SELECT_TASK_LOG, KfnmtExecutionTaskLog.class)
//				.setParameter("companyId", companyId).setParameter("execItemCd", execItemCd).getList();
		List<KfnmtExecutionTaskLog> list = new ArrayList<>();
		try (PreparedStatement statement1 = this.connection().prepareStatement(SELECT_TASK_LOG_BY_JDBC)) {
			statement1.setString(1, companyId);
			statement1.setString(2, execItemCd);
			list.addAll(new NtsResultSet(statement1.executeQuery()).getList(rec -> {
				KfnmtExecutionTaskLogPK pk = new KfnmtExecutionTaskLogPK();
						pk.setCompanyId(rec.getString("CID"));
						pk.setExecItemCd(rec.getString("EXEC_ITEM_CD"));
						pk.setExecId(rec.getString("EXEC_ID"));
						pk.setTaskId(rec.getInt("TASK_ID"));
						KfnmtExecutionTaskLog entity = new KfnmtExecutionTaskLog();
						entity.setKfnmtExecTaskLogPK(pk);
						entity.setStatus(rec.getInt("STATUS"));
						entity.setLastExecDateTime(rec.getGeneralDateTime("LAST_EXEC_DATETIME"));
						entity.setLastEndExecDateTime(rec.getGeneralDateTime("LAST_END_EXEC_DATETIME"));
						entity.setErrorBusiness(rec.getInt("ERROR_BUSINESS"));
						entity.setErrorSystem(rec.getInt("ERROR_SYSTEM"));
						entity.setUpdDate(rec.getGeneralDateTime("UPD_DATE"));
						return entity;
					}));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		if(!list.isEmpty()){
			 List<KfnmtProcessExecutionLog> listKfnmtProcessExecutionLog= this.queryProxy().query(SELECT_BY_KEY, KfnmtProcessExecutionLog.class)
						.setParameter("companyId", companyId).setParameter("execItemCd", execItemCd).getList();
			 if(!listKfnmtProcessExecutionLog.isEmpty()){
				 KfnmtProcessExecutionLog kfnmtProcessExecutionLog = listKfnmtProcessExecutionLog.get(0);
				 return Optional.ofNullable(kfnmtProcessExecutionLog.toDomainMaxDate(list));
			 }else{
				 return Optional.empty();
			 }
		}else{
			 List<ProcessExecutionLog> lstProcessExecutionLog = this.queryProxy().query(SELECT_BY_KEY, KfnmtProcessExecutionLog.class)
						.setParameter("companyId", companyId).setParameter("execItemCd", execItemCd).getList(c -> c.toDomainMaxDate());
				 if(lstProcessExecutionLog!=null && !lstProcessExecutionLog.isEmpty()){
					return Optional.ofNullable(lstProcessExecutionLog.get(0));
				 }
				 return Optional.empty();
		}
	}
	
	@Override
	public Optional<ProcessExecutionLog> getLogReadUncommit(String companyId, String execItemCd){
		Query query = this.getEntityManager().createNativeQuery(SELECT_BY_KEY_NATIVE, KfnmtProcessExecutionLog.class)
				.setParameter(1, companyId).setParameter(2, execItemCd);
		@SuppressWarnings("unchecked")
		List<KfnmtProcessExecutionLog> resultList = query.getResultList();
		List<ProcessExecutionLog> lstProcessExecutionLog = new ArrayList<ProcessExecutionLog>();
		resultList.forEach(x->{
			lstProcessExecutionLog.add(x.toDomainMaxDate());
		});
		 if(lstProcessExecutionLog!=null && !lstProcessExecutionLog.isEmpty()){
				return Optional.ofNullable(lstProcessExecutionLog.get(0));
			 }
		 return Optional.empty();
	}
	
}

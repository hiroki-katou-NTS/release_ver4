package nts.uk.ctx.at.function.infra.repository.processexecution;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDateTime;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionLogHistory;
import nts.uk.ctx.at.function.dom.processexecution.repository.ProcessExecutionLogHistRepository;
import nts.uk.ctx.at.function.infra.entity.processexecution.KfnmtProcessExecutionLogHistory;

@Stateless
public class JpaProcessExecutionLogHistRepository extends JpaRepository
		implements ProcessExecutionLogHistRepository {
	/**
	 * Query strings
	 */
	private static final String SELECT_ALL = "SELECT pelh FROM KfnmtProcessExecutionLogHistory pelh ";
	private static final String SELECT_All_BY_CID_EXECCD_EXECID = SELECT_ALL
			+ "WHERE pelh.kfnmtProcExecLogHstPK.companyId = :companyId "
			+ "AND pelh.kfnmtProcExecLogHstPK.execItemCd = :execItemCd "
			+ "AND pelh.kfnmtProcExecLogHstPK.execId = :execId ";
	private static final String SELECT_All_BY_CID_EXECCD_DATE = SELECT_ALL
			+ "WHERE pelh.kfnmtProcExecLogHstPK.companyId = :companyId "
			+ "AND pelh.kfnmtProcExecLogHstPK.execItemCd = :execItemCd "
			+ "AND pelh.prevExecDateTime >= :prevExecDateTime AND pelh.prevExecDateTime< :nextExecDateTime ORDER BY pelh.prevExecDateTime DESC";
	private static final String SELECT_All_BY_CID_EXECCD_DATE_RANGE = SELECT_ALL
			+ "WHERE pelh.kfnmtProcExecLogHstPK.companyId = :companyId "
			+ "AND pelh.kfnmtProcExecLogHstPK.execItemCd = :execItemCd "
			+ "AND pelh.prevExecDateTime >= :startDate AND pelh.prevExecDateTime < :endDate ORDER BY pelh.prevExecDateTime DESC";
	private static final String SELECT_All_BY_CID_EXECCD = SELECT_ALL
			+ "WHERE pelh.kfnmtProcExecLogHstPK.companyId = :companyId "
			+ "AND pelh.kfnmtProcExecLogHstPK.execItemCd = :execItemCd ";
	@Override
	public Optional<ProcessExecutionLogHistory> getByExecId(String companyId, String execItemCd, String execId) {
		return this.queryProxy().query(SELECT_All_BY_CID_EXECCD_EXECID, KfnmtProcessExecutionLogHistory.class)
				.setParameter("companyId", companyId)
				.setParameter("execItemCd", execItemCd)
				.setParameter("execId", execId).getSingle(c -> c.toDomain());
	}
	
	
	@Override
	public void insert(ProcessExecutionLogHistory domain) {
		Optional<KfnmtProcessExecutionLogHistory> data = this.queryProxy().query(SELECT_All_BY_CID_EXECCD_EXECID, KfnmtProcessExecutionLogHistory.class)
			.setParameter("companyId", domain.getCompanyId())
			.setParameter("execItemCd", domain.getExecItemCd())
			.setParameter("execId", domain.getExecId()).getSingle();
		if(data.isPresent()) {
			this.commandProxy().remove(data.get());
			this.getEntityManager().flush();
		}
		this.commandProxy().insert(KfnmtProcessExecutionLogHistory.toEntity(domain));
		this.getEntityManager().flush();
	}
	
	@Override
	public void update(ProcessExecutionLogHistory domain) {
		KfnmtProcessExecutionLogHistory newEntity = KfnmtProcessExecutionLogHistory.toEntity2(domain);
		
		KfnmtProcessExecutionLogHistory updateEntity = this.queryProxy().query(SELECT_All_BY_CID_EXECCD_EXECID, KfnmtProcessExecutionLogHistory.class)
		.setParameter("companyId", domain.getCompanyId())
		.setParameter("execItemCd", domain.getExecItemCd())
		.setParameter("execId", domain.getExecId()).getSingle().get();
		updateEntity.overallStatus = newEntity.overallStatus;
		updateEntity.errorDetail = newEntity.errorDetail;
		updateEntity.prevExecDateTime = newEntity.prevExecDateTime;
		updateEntity.schCreateStart = newEntity.schCreateStart;
		updateEntity.schCreateEnd = newEntity.schCreateEnd;
		updateEntity.dailyCreateStart = newEntity.dailyCreateStart;
		updateEntity.dailyCreateEnd = newEntity.dailyCreateEnd;
		updateEntity.dailyCalcStart = newEntity.dailyCalcStart;
		updateEntity.dailyCalcEnd = newEntity.dailyCalcEnd;
		updateEntity.reflectApprovalResultStart = newEntity.reflectApprovalResultStart;
		updateEntity.reflectApprovalResultEnd = newEntity.reflectApprovalResultEnd;
		updateEntity.taskLogList = newEntity.taskLogList;
		updateEntity.lastEndExecDateTime = newEntity.lastEndExecDateTime;
		updateEntity.errorSystem = newEntity.errorSystem;
		updateEntity.errorBusiness = newEntity.errorBusiness;
		updateEntity.taskLogList = newEntity.taskLogList;
		this.commandProxy().update(updateEntity);		
		this.getEntityManager().flush();
	}
	
	@Override
	public void remove(String companyId, String execItemCd) {
		 List<KfnmtProcessExecutionLogHistory> list = this.queryProxy().query(SELECT_All_BY_CID_EXECCD, KfnmtProcessExecutionLogHistory.class)
				.setParameter("companyId", companyId)
				.setParameter("execItemCd", execItemCd).getList();
		 if (!CollectionUtil.isEmpty(list)) {
				this.commandProxy().removeAll(list);
			}
	}
	
	


	@Override
	public List<ProcessExecutionLogHistory> getByDate(String companyId, String execItemCd,
			GeneralDateTime prevExecDateTime) {
		GeneralDateTime nextExecDateTime = prevExecDateTime.addDays(1);
		
		return this.queryProxy().query(SELECT_All_BY_CID_EXECCD_DATE, KfnmtProcessExecutionLogHistory.class)
				.setParameter("companyId", companyId)
				.setParameter("execItemCd", execItemCd)
				.setParameter("prevExecDateTime", prevExecDateTime)
				.setParameter("nextExecDateTime", nextExecDateTime).getList(c -> c.toDomain());
		
	}


	@Override
	public List<ProcessExecutionLogHistory> getByDateRange(String companyId, String execItemCd,
			GeneralDateTime startDate, GeneralDateTime endDate) {
		GeneralDateTime endDate1 = endDate.addDays(1);
		
		return this.queryProxy().query(SELECT_All_BY_CID_EXECCD_DATE_RANGE, KfnmtProcessExecutionLogHistory.class)
				.setParameter("companyId", companyId)
				.setParameter("execItemCd", execItemCd)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate1).getList(c -> c.toDomain());
	}

}

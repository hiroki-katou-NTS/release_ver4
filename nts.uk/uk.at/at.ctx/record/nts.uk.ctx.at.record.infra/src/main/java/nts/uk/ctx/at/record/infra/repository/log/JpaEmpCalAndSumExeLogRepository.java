package nts.uk.ctx.at.record.infra.repository.log;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.EmpCalAndSumExeLog;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.EmpCalAndSumExeLogRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ExecutionLog;
import nts.uk.ctx.at.record.infra.entity.log.KrcdtEmpExecutionLog;
import nts.uk.ctx.at.record.infra.entity.log.KrcdtExecutionLog;

@Stateless
public class JpaEmpCalAndSumExeLogRepository extends JpaRepository implements EmpCalAndSumExeLogRepository {

	private final String SELECT_FROM_LOG = "SELECT c FROM KrcdtEmpExecutionLog c ";

	// Get all log by companyID and EmployeeID and empCalAndSumExecLogID DESC
	private final String SELECT_All_LOG_BY_EMPLOYEEID = SELECT_FROM_LOG + " WHERE c.companyID = :companyID "
			+ " AND c.employeeID = :employeeID ORDER BY c.krcdtEmpExecutionLogPK.empCalAndSumExecLogID DESC";

	private final String SELECT_All_LOG = SELECT_FROM_LOG + " WHERE c.companyID = :companyID ";

	private final String SELECT_BY_EXECUTION_LOG = "SELECT el FROM KrcdtExecutionLog el "
			+ " WHERE el.krcdtExecutionLogPK.empCalAndSumExecLogID = :empCalAndSumExecLogID AND el.krcdtExecutionLogPK.executionContent = :executionContent";

	private final String UPDATE_LOG_INFO = "UPDATE KrcdtExecutionLog a" 
			+ " SET a.processStatus = :processStatus"
			+ " WHERE a.krcdtExecutionLogPK.empCalAndSumExecLogID = :empCalAndSumExecLogID"
			+ " AND a.krcdtExecutionLogPK.executionContent = :executionContent ";

	private final String SELECT_BY_LOG_ID = "SELECT c FROM KrcdtEmpExecutionLog c "
			+ " WHERE c.krcdtEmpExecutionLogPK.empCalAndSumExecLogID = :empCalAndSumExecLogID ";

	private final String SELECT_LOG_BY_DATE = SELECT_All_LOG + " AND c.executedDate >= :startDate"
			+ " AND c.executedDate <= :endDate";

	@Override
	public Optional<EmpCalAndSumExeLog> getEmpCalAndSumExeLogMaxByEmp(String companyID, String employeeID) {
		List<EmpCalAndSumExeLog> data = this.queryProxy()
				.query(SELECT_All_LOG_BY_EMPLOYEEID, KrcdtEmpExecutionLog.class).setParameter("companyID", companyID)
				.setParameter("employeeID", employeeID).getList(c -> c.toDomain());
		return !data.isEmpty() ? Optional.of(data.get(0)) : Optional.empty();
	}

	@Override
	public List<EmpCalAndSumExeLog> getAllEmpCalAndSumExeLog(String companyID) {
		List<EmpCalAndSumExeLog> data = this.queryProxy().query(SELECT_All_LOG, KrcdtEmpExecutionLog.class)
				.setParameter("companyID", companyID).getList(c -> c.toDomain());
		return data;
	}

	@Override
	public Optional<ExecutionLog> getByExecutionContent(String empCalAndSumExecLogID, int executionContent) {
		Optional<ExecutionLog> optional = this.queryProxy()
				.query(SELECT_BY_EXECUTION_LOG, KrcdtExecutionLog.class)
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
		if(krcdtExecutionLog.isPresent()){
			krcdtExecutionLog.get().processStatus = processStatus;
			
			this.commandProxy().update(krcdtExecutionLog.get());
			this.getEntityManager().flush();
			
		}
	}

	@Override
	public Optional<EmpCalAndSumExeLog> getByEmpCalAndSumExecLogID(String empCalAndSumExecLogID) {
		Optional<EmpCalAndSumExeLog> data = this.queryProxy().query(SELECT_BY_LOG_ID, KrcdtEmpExecutionLog.class)
				.setParameter("empCalAndSumExecLogID", empCalAndSumExecLogID).getSingle(c -> c.toDomain());
		return data;
	}

	@Override
	public List<EmpCalAndSumExeLog> getAllEmpCalAndSumExeLogByDate(String companyID, GeneralDate startDate,
			GeneralDate endDate) {
		List<EmpCalAndSumExeLog> data = this.queryProxy().query(SELECT_LOG_BY_DATE, KrcdtEmpExecutionLog.class)
				.setParameter("companyID", companyID).setParameter("startDate", startDate)
				.setParameter("endDate", endDate).getList(c -> c.toDomain());
		return data;
	}

	@Override
	public void add(EmpCalAndSumExeLog empCalAndSumExeLog) {
		this.commandProxy().insert(KrcdtEmpExecutionLog.toEntity(empCalAndSumExeLog));
		this.getEntityManager().flush();
	}
	
	@Override
	public void updateStatus(String empCalAndSumExecLogID, int executionStatus) {
		KrcdtEmpExecutionLog krcdtEmpExecutionLog = this.queryProxy()
				.query(SELECT_BY_LOG_ID, KrcdtEmpExecutionLog.class)
				.setParameter("empCalAndSumExecLogID", empCalAndSumExecLogID).getSingle().get();
		krcdtEmpExecutionLog.executedStatus = executionStatus;
		this.commandProxy().update(krcdtEmpExecutionLog);
	}
}

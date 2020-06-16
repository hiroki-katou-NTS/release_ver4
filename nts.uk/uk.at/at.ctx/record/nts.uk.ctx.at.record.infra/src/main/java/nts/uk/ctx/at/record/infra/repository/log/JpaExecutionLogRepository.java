package nts.uk.ctx.at.record.infra.repository.log;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ExecutionLog;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ExecutionLogRepository;
import nts.uk.ctx.at.record.infra.entity.log.KrcdtExecutionLog;

@Stateless
public class JpaExecutionLogRepository extends JpaRepository implements ExecutionLogRepository {

	private static final String SELECT_BY_CAL_AND_SUM_EXE_ID = "SELECT el FROM KrcdtExecutionLog el "
			+ " WHERE el.krcdtExecutionLogPK.empCalAndSumExecLogID = :empCalAndSumExecLogID";

	private static final String SELECT_BY_EXECUTION_LOG = "SELECT el FROM KrcdtExecutionLog el "
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
		List<KrcdtExecutionLog> lstKrcdtExecutionLog = listExecutionLog.stream().map(c -> KrcdtExecutionLog.toEntity(c))
				.collect(Collectors.toList());
		this.commandProxy().insertAll(lstKrcdtExecutionLog);
	}

	//type = 4:UI, type =0:server-daily,type =1:server-calculate,type =2:server-reflect,type =3:server-monthly
	@Override
	public void updateExecutionDate(String empCalAndSumExecLogID, GeneralDateTime executionStartDate,
			GeneralDateTime executionEndDate, GeneralDateTime dailyCreateStartTime, GeneralDateTime dailyCreateEndTime,
			GeneralDateTime dailyCalculateStartTime, GeneralDateTime dailyCalculateEndTime,
			GeneralDateTime reflectApprovalStartTime, GeneralDateTime reflectApprovalEndTime,
			GeneralDateTime monthlyAggregateStartTime, GeneralDateTime monthlyAggregateEndTime , int stopped, int type) {
		
		List<KrcdtExecutionLog> krcdtExecutionLogs = this.queryProxy().query(SELECT_BY_CAL_AND_SUM_EXE_ID, KrcdtExecutionLog.class)
		.setParameter("empCalAndSumExecLogID", empCalAndSumExecLogID).getList();
		// fix bug 110491 ↓
		Optional<KrcdtExecutionLog> createLog = Optional.empty();
		Optional<KrcdtExecutionLog> calculateLog = Optional.empty();
		Optional<KrcdtExecutionLog> log = Optional.empty();
		Optional<KrcdtExecutionLog> monthlyLog = Optional.empty();
		
		if(type == 4) {
		createLog = krcdtExecutionLogs.stream().filter(item -> item.krcdtExecutionLogPK.executionContent == 0).findFirst();
		calculateLog = krcdtExecutionLogs.stream().filter(item -> item.krcdtExecutionLogPK.executionContent == 1).findFirst();
		log = krcdtExecutionLogs.stream().filter(item -> item.krcdtExecutionLogPK.executionContent == 2).findFirst();
		monthlyLog = krcdtExecutionLogs.stream().filter(item -> item.krcdtExecutionLogPK.executionContent == 3).findFirst();
		}
		if(type == 0){
			createLog = krcdtExecutionLogs.stream().filter(item -> item.krcdtExecutionLogPK.executionContent == 0).findFirst();
		}
		if(type == 1){
			calculateLog = krcdtExecutionLogs.stream().filter(item -> item.krcdtExecutionLogPK.executionContent == 1).findFirst();
		}
		if(type == 2){
			log = krcdtExecutionLogs.stream().filter(item -> item.krcdtExecutionLogPK.executionContent == 2).findFirst();
		}
		if(type == 3){
			monthlyLog = krcdtExecutionLogs.stream().filter(item -> item.krcdtExecutionLogPK.executionContent == 3).findFirst();
		}
		if (createLog.isPresent() && dailyCreateEndTime != null) {
			if (dailyCreateStartTime != null) {
				createLog.get().executionStartDate = dailyCreateStartTime;				
			} else {
				createLog.get().executionStartDate = executionEndDate;
			}

			createLog.get().executionEndDate = dailyCreateEndTime;
			
//			if (stopped == 1) {
//				createLog.get().executionEndDate = executionEndDate;
//				dailyCreateEndTime = executionEndDate;
//			}
			
			this.commandProxy().update(createLog.get());
			this.getEntityManager().flush();
		}
		
		if (calculateLog.isPresent() && dailyCalculateEndTime != null) {
			
			if (dailyCalculateStartTime != null) {
				calculateLog.get().executionStartDate = dailyCalculateStartTime;
			} else {
				calculateLog.get().executionStartDate = executionEndDate;
			}
			

			calculateLog.get().executionEndDate = dailyCalculateEndTime;

			
//			if (stopped == 1 && dailyCalculateStartTime == null) {
//				calculateLog.get().executionStartDate = dailyCreateEndTime;
//				calculateLog.get().executionEndDate = dailyCreateEndTime;
//			}
			
			this.commandProxy().update(calculateLog.get());
			this.getEntityManager().flush();
		}

		if (log.isPresent() && reflectApprovalEndTime != null) {
			if (reflectApprovalStartTime != null) {
				log.get().executionStartDate = reflectApprovalStartTime;
			} else {
				log.get().executionStartDate = executionEndDate;
			}
			
			log.get().executionEndDate = reflectApprovalEndTime;

			
//			if (stopped == 1 &&  reflectApprovalStartTime == null) {
//				if (dailyCalculateEndTime != null) {
//					log.get().executionStartDate = dailyCalculateEndTime;
//					log.get().executionEndDate = dailyCalculateEndTime;
//				} else if (!calculateLog.isPresent() && createLog.isPresent()) {
//					log.get().executionStartDate = dailyCreateEndTime;
//					log.get().executionEndDate = dailyCreateEndTime;
//				}
//			}			
			
			this.commandProxy().update(log.get());
			this.getEntityManager().flush();
		}
		
		if (monthlyLog.isPresent() && monthlyAggregateEndTime != null) {
			if (monthlyAggregateStartTime != null) {
				monthlyLog.get().executionStartDate = monthlyAggregateStartTime;
			} else {
				monthlyLog.get().executionStartDate = executionEndDate;
			}
			
			monthlyLog.get().executionEndDate = monthlyAggregateEndTime;

			
//			if (stopped == 1 && monthlyAggregateStartTime == null) {
//				if (reflectApprovalEndTime != null) {
//					monthlyLog.get().executionStartDate = reflectApprovalEndTime;
//					monthlyLog.get().executionEndDate = reflectApprovalEndTime;
//				} else if(reflectApprovalEndTime == null && dailyCalculateEndTime != null){
//					monthlyLog.get().executionStartDate = dailyCalculateEndTime;
//					monthlyLog.get().executionEndDate = dailyCalculateEndTime;
//				} else if(reflectApprovalEndTime == null && dailyCalculateEndTime == null){
//					monthlyLog.get().executionStartDate = dailyCreateEndTime;
//					monthlyLog.get().executionEndDate = dailyCreateEndTime;
//				}
//			}
			
			this.commandProxy().update(monthlyLog.get());
			this.getEntityManager().flush();
		}
	}

}

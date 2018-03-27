package nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;

/**
 * 
 * @author tutk
 *
 */
public interface EmpCalAndSumExeLogRepository {
	/**
	 * get all  EmpCalAndSumExeLog by companyID, employeeID
	 * @param companyID
	 * @param employeeID
	 * @return
	 */
	Optional<EmpCalAndSumExeLog> getEmpCalAndSumExeLogMaxByEmp(String companyID, String employeeID);
	
	/**
	 * get all  EmpCalAndSumExeLog
	 * @param companyID
	 * @return
	 */
	List<EmpCalAndSumExeLog> getAllEmpCalAndSumExeLog(String companyID);

	
	
	/**
	 * get EmpCalAndSumExeLog by empCalAndSumExecLogID
	 * @param empCalAndSumExecLogID
	 * @return
	 */
	Optional<EmpCalAndSumExeLog> getByEmpCalAndSumExecLogID (String empCalAndSumExecLogID);

	/**
	 * KIF 001 3 日別実績の作成処理
	 * 
	 * @param empCalAndSumExecLogID
	 * @param executionContent
	 * @return
	 */
	Optional<ExecutionLog> getByExecutionContent(String empCalAndSumExecLogID, int executionContent);

	/**
	 * KIF 001 4 ログ情報（実行ログ）
	 * 
	 * @param empCalAndSumExecLogID
	 * 
	 */
	void updateLogInfo(String empCalAndSumExecLogID,int executionContent, int processStatus);

	/**
	 * get all EmpCalAndSumExeLog by startDate and endDate
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<EmpCalAndSumExeLog> getAllEmpCalAndSumExeLogByDate(String companyID, GeneralDate startDate,
			GeneralDate endDate);

	void add(EmpCalAndSumExeLog empCalAndSumExeLog);
	
	void updateStatus(String empCalAndSumExecLogID, int executionStatus);
}

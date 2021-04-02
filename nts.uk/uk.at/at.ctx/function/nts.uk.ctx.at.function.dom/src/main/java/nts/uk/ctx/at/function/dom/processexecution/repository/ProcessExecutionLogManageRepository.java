package nts.uk.ctx.at.function.dom.processexecution.repository;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionLogManage;

public interface ProcessExecutionLogManageRepository {
	public List<ProcessExecutionLogManage> getProcessExecutionLogByCompanyId(String companyId);
	
	public List<ProcessExecutionLogManage> getProcessExecutionLogByCompanyIdAndExecItemCd(String companyId, List<String> execItemCds);
	
	public List<ProcessExecutionLogManage> getProcessExecutionReadUncommit(String companyId);
	public Optional<ProcessExecutionLogManage> getLogByCIdAndExecCd(String companyId, String execItemCd);
	public void insert(ProcessExecutionLogManage domain);
	public void update(ProcessExecutionLogManage domain);
	public void remove(String companyId, String execItemCd);
	public Optional<ProcessExecutionLogManage> getLogByCIdAndExecCdAndDateTiem(String companyId, String execItemCd,GeneralDateTime dateTime);
	public void updateByDatetime(ProcessExecutionLogManage domain,GeneralDateTime dateTime);
	
	/**
	 * エラーがある自動実行項目を取得する(会社ID)
	 * @param 会社ID companyId
	 * @return List<ProcessExecutionLogManage>
	 */
	public List<ProcessExecutionLogManage> getAutorunItemsWithErrors(String companyId);
}
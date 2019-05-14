package nts.uk.ctx.at.function.app.find.processexecution;

//import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
//import javax.ejb.TransactionAttribute;
//import javax.ejb.TransactionAttributeType;
//import javax.ejb.TransactionManagement;
import javax.inject.Inject;
//import javax.transaction.Transactional;
//import javax.transaction.Transactional.TxType;
//
//import org.apache.commons.lang3.RandomUtils;
//
//import nts.arc.task.tran.TransactionService;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.at.function.app.find.processexecution.dto.ProcessExecutionLogDto;
import nts.uk.ctx.at.function.dom.processexecution.ExecutionCode;
import nts.uk.ctx.at.function.dom.processexecution.ProcessExecution;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionLog;
//import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionLogManage;
import nts.uk.ctx.at.function.dom.processexecution.repository.ExecutionTaskSettingRepository;
import nts.uk.ctx.at.function.dom.processexecution.repository.ProcessExecutionLogManageRepository;
import nts.uk.ctx.at.function.dom.processexecution.repository.ProcessExecutionLogRepository;
import nts.uk.ctx.at.function.dom.processexecution.repository.ProcessExecutionRepository;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.ExecutionTaskSetting;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.i18n.TextResource;
/**
 * 起動時処理 - KBT002	
 * @author tutk
 *
 */
@Stateless
public class ProcessExecutionLogFinder {

	@Inject
	private ProcessExecutionLogRepository procExecLogRepo;
	
	@Inject
	private ExecutionTaskSettingRepository execSettingRepo;
	
	@Inject
	private ProcessExecutionRepository procExecRepo;
	
	@Inject
	private ProcessExecutionLogManageRepository processExecLogManRepo;
	

	
	public List<ProcessExecutionLogDto> findAll() {
		String companyId = AppContexts.user().companyId();
		String KBT002_165 = TextResource.localize("KBT002_165");
			return this.processExecLogManRepo.getProcessExecutionLogByCompanyId(companyId).stream().map(a -> {
				ProcessExecutionLogDto dto = null;
				//ドメインモデル「更新処理自動実行ログ」を取得する
				Optional<ProcessExecutionLog> processExecutionLogOpt = this.procExecLogRepo.getLog(companyId, a.getExecItemCd().v());
				if(processExecutionLogOpt!=null && processExecutionLogOpt.isPresent()){
					dto = ProcessExecutionLogDto.fromDomain(processExecutionLogOpt.get(),a);
				}else{
					dto = ProcessExecutionLogDto.fromDomain(new ProcessExecutionLog(new ExecutionCode(a.getExecItemCd().v()),companyId,IdentifierUtil.randomUniqueId()),a);
				}
				// ドメインモデル「実行タスク設定」を取得する	
				Optional<ExecutionTaskSetting> taskSettingOpt = this.execSettingRepo.getByCidAndExecCd(companyId, a.getExecItemCd().v());
				if (taskSettingOpt.isPresent()) {
					ExecutionTaskSetting setting = Optional.of(taskSettingOpt.get()).get();
					dto.setNextExecDateTime(
							(setting.getNextExecDateTime() == null || !setting.getNextExecDateTime().isPresent()) ? 
									KBT002_165 : setting.getNextExecDateTime().get().toString("yyyy/MM/dd HH:mm:ss"));
				}else{
					dto.setNextExecDateTime(KBT002_165);
				}
				Optional<ProcessExecution> procExecOpt = this.procExecRepo.getProcessExecutionByCidAndExecCd(companyId, a.getExecItemCd().v());
				if (procExecOpt.isPresent()) {
					dto.setExecItemName(procExecOpt.get().getExecItemName().v());
				}
				return dto;
			}).collect(Collectors.toList());
	}
	
}
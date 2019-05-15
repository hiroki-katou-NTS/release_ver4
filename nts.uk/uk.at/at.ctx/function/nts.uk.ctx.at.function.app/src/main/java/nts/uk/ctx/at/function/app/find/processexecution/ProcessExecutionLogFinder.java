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

import nts.arc.time.GeneralDateTime;
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
import nts.uk.shr.com.task.schedule.UkJobScheduler;
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
	
	@Inject
	private UkJobScheduler scheduler;
	
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
				GeneralDateTime nextFireTime = null;
				// ドメインモデル「実行タスク設定」を取得する	
				Optional<ExecutionTaskSetting> taskSettingOpt = this.execSettingRepo.getByCidAndExecCd(companyId, a.getExecItemCd().v());
				//次回実行日時作成処理
				if(taskSettingOpt.isPresent()) {
					try {
						if(taskSettingOpt.get().isEnabledSetting()) {
							nextFireTime = taskSettingOpt
									.map(e -> e.getScheduleId())
									.flatMap(id -> this.scheduler.getNextFireTime(id))
									.orElse(null);
						}
					} catch (Exception e2) {
						nextFireTime = null;
					}
					
				}
				if (taskSettingOpt.isPresent()) {
					ExecutionTaskSetting setting = Optional.of(taskSettingOpt.get()).get();
					dto.setNextExecDateTime(
							(nextFireTime == null || !setting.getNextExecDateTime().isPresent()) ? 
									KBT002_165 : nextFireTime.toString("yyyy/MM/dd HH:mm:ss"));
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
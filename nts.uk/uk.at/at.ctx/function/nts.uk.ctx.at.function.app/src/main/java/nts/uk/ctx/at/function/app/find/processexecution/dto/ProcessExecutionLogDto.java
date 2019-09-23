package nts.uk.ctx.at.function.app.find.processexecution.dto;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;
import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.CurrentExecutionStatus;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.EndStatus;
//import nts.uk.ctx.at.function.dom.processexecution.executionlog.ExecutionTaskLog;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.OverallErrorDetail;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionLog;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionLogManage;

@Data
public class ProcessExecutionLogDto {
	
	private static final String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
	
	/* コード */
	private String execItemCd;
	
	private String execItemName;
	
	/* 会社ID */
	private String companyId;
	
	/* 現在の実行状態 */
	public Integer currentStatusCd;
	
	/* 現在の実行状態 */
	public String currentStatus;
	
	/* 全体の終了状態 */
	public Integer overallStatusCd;
	
	/* 全体の終了状態 */
	public String overallStatus;
	
	/* 全体のエラー詳細 */
	public String overallError;
	
	/* 前回実行日時 */
	public String lastExecDateTime;
	
	/* スケジュール作成の期間 */
	public GeneralDate schCreateStart;
	
	/* スケジュール作成の期間 */
	public GeneralDate schCreateEnd;
	
	/* 日別作成の期間 */
	public GeneralDate dailyCreateStart;
	
	/* 日別作成の期間 */
	public GeneralDate dailyCreateEnd;
	
	/* 日別計算の期間 */
	public GeneralDate dailyCalcStart;
	
	/* 日別計算の期間 */
	public GeneralDate dailyCalcEnd;
	
	/* 実行ID */
	public String execId;
	
	/* 次回実行日時 */
	private String nextExecDateTime;
	
	private String taskLogExecId;
	
    private List<ProcessExecutionTaskLogDto> taskLogList;
    
	/* 前回終了日時*/
	private String lastEndExecDateTime;
	
	/* 全体のシステムエラー状態*/
	private Boolean errorSystem;
	
	/* 全体の業務エラー状態*/
	private Boolean errorBusiness;
	
	private String errorSystemText;
	
	private String errorBusinessText;
	
	private String rangeDateTime = "";
	
	public ProcessExecutionLogDto() {
		super();
	}

	public ProcessExecutionLogDto(String execItemCd, String companyId, Integer currentStatusCd,
			String currentStatus, Integer overallStatusCd, String overallStatus, String overallError,
			String lastExecDateTime, GeneralDate schCreateStart, GeneralDate schCreateEnd, GeneralDate dailyCreateStart,
			GeneralDate dailyCreateEnd, GeneralDate dailyCalcStart, GeneralDate dailyCalcEnd, String execId,
			List<ProcessExecutionTaskLogDto> taskLogList,String taskLogExecId,String lastEndExecDateTime,Boolean errorSystem,Boolean errorBusiness) {
		super();
		this.execItemCd = execItemCd;
		this.companyId = companyId;
		this.currentStatusCd = currentStatusCd;
		this.currentStatus = currentStatus;
		this.overallStatusCd = overallStatusCd;
		this.overallStatus = overallStatus;
		this.overallError = overallError;
		this.lastExecDateTime = lastExecDateTime;
		this.schCreateStart = schCreateStart;
		this.schCreateEnd = schCreateEnd;
		this.dailyCreateStart = dailyCreateStart;
		this.dailyCreateEnd = dailyCreateEnd;
		this.dailyCalcStart = dailyCalcStart;
		this.dailyCalcEnd = dailyCalcEnd;
		this.execId = execId;
		this.taskLogList = taskLogList;
		this.taskLogExecId = taskLogExecId;
		this.lastEndExecDateTime = lastEndExecDateTime;
		this.errorSystem = errorSystem;
		this.errorBusiness = errorBusiness;
		if(errorSystem != null) {
			if(errorSystem.booleanValue()) {
				this.errorSystemText = "あり";
			}else {
				this.errorSystemText = "なし";
			}
		}else {
			this.errorSystemText = null;
		}
		if(errorBusiness != null) {
			if(errorBusiness.booleanValue()) {
				this.errorBusinessText = "あり";
			}else {
				this.errorBusinessText = "なし";
			}
		}else {
			this.errorBusinessText = null;
		}
	}
	
	public static ProcessExecutionLogDto fromDomain(ProcessExecutionLog procExecLog, ProcessExecutionLogManage procExecLogMan ) {
		List<ProcessExecutionTaskLogDto> taskLogList = procExecLog.getTaskLogList().stream().map(x -> ProcessExecutionTaskLogDto.fromDomain(x)).collect(Collectors.toList());
		Collections.sort(taskLogList, new Comparator<ProcessExecutionTaskLogDto>() {
			@Override
			public int compare(ProcessExecutionTaskLogDto dto1, ProcessExecutionTaskLogDto dto2) {
				return dto1.getTaskId() - dto2.getTaskId();
			}
		});
		String taskLogExecId = "";
		if(!procExecLog.getTaskLogList().isEmpty()){
			taskLogExecId = procExecLog.getTaskLogList().get(0).getExecId();
		}
		
		GeneralDate schCreateStart = null;
		GeneralDate schCreateEnd = null;
		if (procExecLog.getEachProcPeriod() != null && procExecLog.getEachProcPeriod().isPresent()
				&& procExecLog.getEachProcPeriod().get().getScheduleCreationPeriod() != null
				&& procExecLog.getEachProcPeriod().get().getScheduleCreationPeriod().isPresent()) {
			schCreateStart = procExecLog.getEachProcPeriod().get().getScheduleCreationPeriod().get().start();
			schCreateEnd = procExecLog.getEachProcPeriod().get().getScheduleCreationPeriod().get().end();
		}
		GeneralDate dailyCreateStart = null;
		GeneralDate dailyCreateEnd = null;
		if (procExecLog.getEachProcPeriod() != null && procExecLog.getEachProcPeriod().isPresent()
				&& procExecLog.getEachProcPeriod().get().getDailyCreationPeriod() != null
				&& procExecLog.getEachProcPeriod().get().getDailyCreationPeriod().isPresent()) {
			dailyCreateStart = procExecLog.getEachProcPeriod().get().getDailyCreationPeriod().get().start();
			dailyCreateEnd = procExecLog.getEachProcPeriod().get().getDailyCreationPeriod().get().end();
		}
		GeneralDate dailyCalcStart = null;
		GeneralDate dailyCalcEnd = null;
		if (procExecLog.getEachProcPeriod() != null && procExecLog.getEachProcPeriod().isPresent()
				&& procExecLog.getEachProcPeriod().get().getDailyCalcPeriod() != null
				&& procExecLog.getEachProcPeriod().get().getDailyCalcPeriod().isPresent()) {
			dailyCalcStart = procExecLog.getEachProcPeriod().get().getDailyCalcPeriod().get().start();
			dailyCalcEnd = procExecLog.getEachProcPeriod().get().getDailyCalcPeriod().get().end();
		}
//		GeneralDate reflectApprovalResultStart = null;
//		GeneralDate reflectApprovalResultEnd = null;
		if (procExecLog.getEachProcPeriod() != null && procExecLog.getEachProcPeriod().isPresent()
				&& procExecLog.getEachProcPeriod().get().getReflectApprovalResult() != null
				&& procExecLog.getEachProcPeriod().get().getReflectApprovalResult().isPresent()) {
//			reflectApprovalResultStart = procExecLog.getEachProcPeriod().get().getReflectApprovalResult().get().start();
//			reflectApprovalResultEnd = procExecLog.getEachProcPeriod().get().getReflectApprovalResult().get().end();
		}
		return new ProcessExecutionLogDto(
				procExecLog.getExecItemCd().v(),
				procExecLog.getCompanyId(),
				procExecLogMan.getCurrentStatus() == null ? null : procExecLogMan.getCurrentStatus().value,
				procExecLogMan.getCurrentStatus() == null ? "" : EnumAdaptor.valueOf(procExecLogMan.getCurrentStatus().value, CurrentExecutionStatus.class).name,
				(procExecLogMan.getOverallStatus() == null || !procExecLogMan.getOverallStatus().isPresent()) ? null : procExecLogMan.getOverallStatus().get().value,
				(procExecLogMan.getOverallStatus() == null || !procExecLogMan.getOverallStatus().isPresent()) ? "" : EnumAdaptor.valueOf(procExecLogMan.getOverallStatus().get().value, EndStatus.class).name,
				procExecLogMan.getOverallError() == null ? "" : EnumAdaptor.valueOf(procExecLogMan.getOverallError().value, OverallErrorDetail.class).name,
				procExecLogMan.getLastExecDateTime() == null ? "" : procExecLogMan.getLastExecDateTime().toString(DATE_FORMAT),
				schCreateStart,
				schCreateEnd,
				dailyCreateStart,
				dailyCreateEnd,
				dailyCalcStart,
				dailyCalcEnd,
				procExecLog.getExecId(), 
				taskLogList,taskLogExecId,
				procExecLogMan.getLastEndExecDateTime() == null ? "" : procExecLogMan.getLastEndExecDateTime().toString(DATE_FORMAT),
				procExecLogMan.getErrorSystem(),
				procExecLogMan.getErrorBusiness()
				);
	}
}

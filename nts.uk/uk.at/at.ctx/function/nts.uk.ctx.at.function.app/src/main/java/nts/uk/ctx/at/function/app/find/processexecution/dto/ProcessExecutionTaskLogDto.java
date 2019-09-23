package nts.uk.ctx.at.function.app.find.processexecution.dto;

import lombok.Data;
import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.function.dom.processexecution.createlogfileexecution.CalTimeRangeDateTimeToString;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.EndStatus;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ExecutionTaskLog;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionTask;

@Data
public class ProcessExecutionTaskLogDto {
	private static final String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
	
	/* 更新処理 */
	public int taskId;
	
	public String taskName;
	
	/* 終了状態 */
	public Integer statusCd;
	
	/* 終了状態 */
	public String status;
	
	/* 前回実行日時 */
	private String lastExecDateTime;
	
	/* 前回終了日時*/
	private String lastEndExecDateTime;
	
	/* 全体のシステムエラー状態*/
	private Boolean errorSystem;
	
	/* 全体の業務エラー状態*/
	private Boolean errorBusiness;
	
	private String rangeDateTime = "";
	
	private String errorSystemText;
	
	private String errorBusinessText;
	
	public ProcessExecutionTaskLogDto() {
		super();
	}

	public ProcessExecutionTaskLogDto(int taskId, String taskName, Integer statusCd, String status,
			String lastExecDateTime, String lastEndExecDateTime, Boolean errorSystem, Boolean errorBusiness) {
		super();
		this.taskId = taskId;
		this.taskName = taskName;
		this.statusCd = statusCd;
		this.status = status;
		this.lastExecDateTime = lastExecDateTime;
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
	//procExecLogMan.getLastExecDateTime() == null ? "" : procExecLogMan.getLastExecDateTime().toString(DATE_FORMAT)
	
	public static ProcessExecutionTaskLogDto fromDomain(ExecutionTaskLog domain) {
		String rangeDateTime = CalTimeRangeDateTimeToString.calTimeExec(domain.getLastExecDateTime(), domain.getLastEndExecDateTime());
		
		ProcessExecutionTaskLogDto dto =  new ProcessExecutionTaskLogDto(
				domain.getProcExecTask().value,
				EnumAdaptor.valueOf(domain.getProcExecTask().value, ProcessExecutionTask.class).name,
				(domain.getStatus()!=null && domain.getStatus().isPresent())?domain.getStatus().get().value:null ,
				(domain.getStatus()!=null && domain.getStatus().isPresent())?EnumAdaptor.valueOf(domain.getStatus().get().value , EndStatus.class).name:null,
				domain.getLastExecDateTime() == null ? "" : domain.getLastExecDateTime().toString(DATE_FORMAT),		
				domain.getLastEndExecDateTime() == null ? "" : domain.getLastEndExecDateTime().toString(DATE_FORMAT),
				domain.getErrorSystem() == null ?null: domain.getErrorSystem(),
				domain.getErrorBusiness() == null ?null: domain.getErrorBusiness()
				);
		dto.setRangeDateTime(rangeDateTime);
		return dto;
				
	}







}

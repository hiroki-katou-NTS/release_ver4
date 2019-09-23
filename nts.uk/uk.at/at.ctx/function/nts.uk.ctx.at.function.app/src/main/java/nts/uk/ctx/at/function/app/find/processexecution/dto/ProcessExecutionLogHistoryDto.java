package nts.uk.ctx.at.function.app.find.processexecution.dto;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;
import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.function.dom.processexecution.createlogfileexecution.CalTimeRangeDateTimeToString;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.EndStatus;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.OverallErrorDetail;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionLogHistory;

@Data
public class ProcessExecutionLogHistoryDto {
	
	private static final String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
	
	/* コード */
	private String execItemCd;
	
	private String execItemName;
	
	/* 会社ID */
	private String companyId;
	
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
	
    private List<ProcessExecutionTaskLogDto> taskLogList;
	
    /* 前回終了日時*/
	private String lastEndExecDateTime;
	
	/* 全体のシステムエラー状態*/
	private Boolean errorSystem;
	
	/* 全体の業務エラー状態*/
	private Boolean errorBusiness;
	
	private String rangeDateTime = "";
	
	private String errorSystemText;
	
	private String errorBusinessText;
	
	public ProcessExecutionLogHistoryDto() {
		super();
	}

	public ProcessExecutionLogHistoryDto(String execItemCd, String companyId,
			String overallStatus, String overallError, String lastExecDateTime, GeneralDate schCreateStart,
			GeneralDate schCreateEnd, GeneralDate dailyCreateStart, GeneralDate dailyCreateEnd,
			GeneralDate dailyCalcStart, GeneralDate dailyCalcEnd, String execId,/* String prevExecDateTimeEx,*/
			List<ProcessExecutionTaskLogDto> taskLogList,String lastEndExecDateTime,Boolean errorSystem,Boolean errorBusiness,String rangeDateTime) {
		super();
		this.execItemCd = execItemCd;
		this.companyId = companyId;
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
		this.lastEndExecDateTime = lastEndExecDateTime;
		this.errorSystem = errorSystem;
		this.errorBusiness = errorBusiness;
		this.rangeDateTime = rangeDateTime;
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
	
	public static ProcessExecutionLogHistoryDto fromDomain(ProcessExecutionLogHistory domain) {
		List<ProcessExecutionTaskLogDto> taskLogList = domain.getTaskLogList().stream().map(x -> ProcessExecutionTaskLogDto.fromDomain(x)).collect(Collectors.toList());
		GeneralDate schCreateStart = null;
		GeneralDate schCreateEnd = null;
		if (domain.getEachProcPeriod() != null
				&& domain.getEachProcPeriod().getScheduleCreationPeriod() != null
				&& domain.getEachProcPeriod().getScheduleCreationPeriod().isPresent()) {
			schCreateStart = domain.getEachProcPeriod().getScheduleCreationPeriod().get().start();
			schCreateEnd = domain.getEachProcPeriod().getScheduleCreationPeriod().get().end();
		}
		GeneralDate dailyCreateStart = null;
		GeneralDate dailyCreateEnd = null;
		if (domain.getEachProcPeriod() != null 
				&& domain.getEachProcPeriod().getDailyCreationPeriod() != null
				&& domain.getEachProcPeriod().getDailyCreationPeriod().isPresent()) {
			dailyCreateStart = domain.getEachProcPeriod().getDailyCreationPeriod().get().start();
			dailyCreateEnd = domain.getEachProcPeriod().getDailyCreationPeriod().get().end();
		}
		GeneralDate dailyCalcStart = null;
		GeneralDate dailyCalcEnd = null;
		if (domain.getEachProcPeriod() != null
				&& domain.getEachProcPeriod().getDailyCalcPeriod() != null
				&& domain.getEachProcPeriod().getDailyCalcPeriod().isPresent()) {
			dailyCalcStart = domain.getEachProcPeriod().getDailyCalcPeriod().get().start();
			dailyCalcEnd = domain.getEachProcPeriod().getDailyCalcPeriod().get().end();
		}
//		GeneralDate reflectApprovalResultStart = null;
//		GeneralDate reflectApprovalResultEnd = null;
		if (domain.getEachProcPeriod() != null
				&& domain.getEachProcPeriod().getReflectApprovalResult() != null
				&& domain.getEachProcPeriod().getReflectApprovalResult().isPresent()) {
//			reflectApprovalResultStart = domain.getEachProcPeriod().getReflectApprovalResult().get().start();
//			reflectApprovalResultEnd = domain.getEachProcPeriod().getReflectApprovalResult().get().end();
		}
		String rangeDateTime = CalTimeRangeDateTimeToString.calTimeExec(domain.getLastExecDateTime(), domain.getLastEndExecDateTime());
		return new ProcessExecutionLogHistoryDto(
				domain.getExecItemCd().v(),
				domain.getCompanyId(),
				(domain.getOverallStatus()!=null && domain.getOverallStatus().isPresent())? EnumAdaptor.valueOf(domain.getOverallStatus().get().value, EndStatus.class).name:" ",
				(domain.getOverallError()!=null && domain.getOverallError().isPresent())?EnumAdaptor.valueOf(domain.getOverallError().get().value, OverallErrorDetail.class).name: " ",
				domain.getLastExecDateTime().toString(DATE_FORMAT),
				schCreateStart,
				schCreateEnd,
				dailyCreateStart,
				dailyCreateEnd,
				dailyCalcStart,
				dailyCalcEnd,
				domain.getExecId(), 
				taskLogList,
				domain.getLastEndExecDateTime()== null?null: domain.getLastEndExecDateTime().toString(DATE_FORMAT),
				domain.getErrorSystem(),
				domain.getErrorBusiness(),
				rangeDateTime);
	}
}

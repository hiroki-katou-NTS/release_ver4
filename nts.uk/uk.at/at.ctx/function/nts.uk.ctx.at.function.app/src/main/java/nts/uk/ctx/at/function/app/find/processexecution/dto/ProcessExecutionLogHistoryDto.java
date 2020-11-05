package nts.uk.ctx.at.function.app.find.processexecution.dto;

import lombok.Builder;
import lombok.Data;
import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Dto UKDesign.ドメインモデル.NittsuSystem.UniversalK.就業.contexts.就業機能.更新処理自動実行.更新処理自動実行ログ.更新処理自動実行ログ履歴
 */
@Data
@Builder
public class ProcessExecutionLogHistoryDto implements ProcessExecutionLogHistory.MementoSetter, ProcessExecutionLogHistory.MementoGetter {

    /* 実行ID */
    public String execId;
    /* 全体の終了状態 */
    public Integer overallStatus;
    /* 前回実行日時 */
    public GeneralDateTime lastExecDateTime;
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
    /* 承認結果反映 */
    public GeneralDate reflectApprovalResultStart;
    /* 承認結果反映 */
    public GeneralDate reflectApprovalResultEnd;
    /* 全体のエラー詳細 */
    public Integer overallError;
    /* コード */
    private String execItemCd;
    /* 会社ID */
    private String companyId;
    /* 全体のシステムエラー状態*/
    private Boolean errorSystem;
    /* 全体の業務エラー状態*/
    private Boolean errorBusiness;
    /* 前回終了日時*/
    private GeneralDateTime lastEndExecDateTime;
    /* 各処理の終了状態 */
    private List<ProcessExecutionTaskLogDto> taskLogList;

//	private String rangeDateTime = "";
//
//	private String errorSystemText;
//
//	private String errorBusinessText;

//		if(errorSystem != null) {
//		if(errorSystem.booleanValue()) {
//			this.errorSystemText = "あり";
//		}else {
//			this.errorSystemText = "なし";
//		}
//	}else {
//		this.errorSystemText = null;
//	}
//		if(errorBusiness != null) {
//		if(errorBusiness.booleanValue()) {
//			this.errorBusinessText = "あり";
//		}else {
//			this.errorBusinessText = "なし";
//		}
//	}else {
//		this.errorBusinessText = null;
//	}

    @Override
    public EachProcessPeriod getEachProcPeriod() {
        return new EachProcessPeriod(
                new DatePeriod(this.schCreateStart, this.schCreateEnd),
                new DatePeriod(this.dailyCreateStart, this.dailyCreateEnd),
                new DatePeriod(this.dailyCalcStart, this.dailyCalcEnd),
                new DatePeriod(this.reflectApprovalResultStart, this.reflectApprovalResultEnd)
        );
    }

    @Override
    public void setEachProcPeriod(EachProcessPeriod eachProcPeriod) {
        Optional<DatePeriod> scheduleCreationPeriod = eachProcPeriod.getScheduleCreationPeriod();
        Optional<DatePeriod> dailyCreationPeriod = eachProcPeriod.getDailyCreationPeriod();
        Optional<DatePeriod> dailyCalcPeriod = eachProcPeriod.getDailyCalcPeriod();
        Optional<DatePeriod> reflectApprovalResult = eachProcPeriod.getReflectApprovalResult();

        this.schCreateStart = scheduleCreationPeriod.map(DatePeriod::start).orElse(null);
        this.schCreateEnd = scheduleCreationPeriod.map(DatePeriod::end).orElse(null);

        this.dailyCreateStart = dailyCreationPeriod.map(DatePeriod::start).orElse(null);
        this.dailyCreateEnd = dailyCreationPeriod.map(DatePeriod::end).orElse(null);

        this.dailyCalcStart = dailyCalcPeriod.map(DatePeriod::start).orElse(null);
        this.dailyCalcEnd = dailyCalcPeriod.map(DatePeriod::end).orElse(null);

        this.reflectApprovalResultStart = reflectApprovalResult.map(DatePeriod::start).orElse(null);
        this.reflectApprovalResultEnd = reflectApprovalResult.map(DatePeriod::end).orElse(null);
    }

    @Override
    public List<ExecutionTaskLog> getTaskLogList() {
        //TODO
        return taskLogList.stream()
                .map(item -> ExecutionTaskLog.builder()
                        .procExecTask(EnumAdaptor.valueOf(item.getTaskId(), ProcessExecutionTask.class))
                        .status(Optional.ofNullable(EnumAdaptor.valueOf(item.getStatusCd(), EndStatus.class)))
                        .lastExecDateTime(Optional.ofNullable(item.getLastExecDateTime()))
                        .lastEndExecDateTime(Optional.ofNullable(item.getLastEndExecDateTime()))
                        .errorSystem(Optional.of(item.getErrorSystem()))
                        .errorBusiness(Optional.of(item.getErrorBusiness()))
                        .systemErrorDetails(Optional.ofNullable(item.getErrorSystemText()))
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void setTaskLogList(List<ExecutionTaskLog> taskLogList) {
        this.taskLogList = taskLogList.stream()
                .map(item -> ProcessExecutionTaskLogDto.builder()
                        .taskId(item.getProcExecTask().value)
                        .statusCd(item.getStatus().map(e -> e.value).orElse(null))
                        .status(item.getStatus().map(e -> e.name).orElse(null))
                        .lastExecDateTime(item.getLastExecDateTime().orElse(null))
                        .lastEndExecDateTime(item.getLastEndExecDateTime().orElse(null))
                        .errorSystem(item.getErrorSystem().orElse(null))
                        .errorBusiness(item.getErrorBusiness().orElse(null))
                        .build())
                .collect(Collectors.toList());
    }
}

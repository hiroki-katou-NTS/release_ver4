package nts.uk.ctx.at.function.app.find.processexecution.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.ctx.at.function.dom.adapter.employeebasic.EmployeeInfoImport;
import nts.uk.ctx.at.function.dom.processexecution.ProcessExecution;

import java.util.List;

/** 更新処理自動実行データ */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProcessAutoRunDataDto {

    /** 更新処理自動実行一覧 */
    private List<ProcessExecution> lstProcessExecution;

    /** 実行ログ詳細 */
    private List<ExecutionLogDetailDto> lstExecLogDetail;

    /** 社員情報 */
    private List<EmployeeInfoImport> lstEmployeeSearch;

    /** 社員名を出力するか */
    private boolean isExportEmpName;

}

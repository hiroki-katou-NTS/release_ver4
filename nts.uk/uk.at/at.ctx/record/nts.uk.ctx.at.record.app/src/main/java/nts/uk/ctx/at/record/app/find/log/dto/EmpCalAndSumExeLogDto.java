package nts.uk.ctx.at.record.app.find.log.dto;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.EmpCalAndSumExeLog;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ExecutionLog;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmpCalAndSumExeLogDto {
	
	
	/**
	 * 就業計算と集計実行ログID
	 */
	private String empCalAndSumExecLogID;
	
	/**
	 * 会社ID
	 */

	private String companyID;
	
	/**
	 * 処理月
	 */
	private Integer processingMonth;
	
	/**
	 * 実行したメニュー
	 * ・選択して実行 ・ケース別実行
	 */
	private int executedMenu;

	/**
	 * 実行日
	 */

	private GeneralDate executionDate;
	
	/**
	 * 実行状況
	 */
	private int executionStatus;
	
	/**
	 * 実行状況 Name Japan
	 */
	private String executionStatusName;
	
	/**
	 * 実行社員ID
	 */

	private String employeeID;
	
	/**
	 * 締めID
	 */
	private int closureID;
	

	/**
	 * 運用ケース
	 */
	private String caseSpecExeContentID;	
	
	


	
	
	
	/**
	 * 実行ログ
	 * 1->4 elements
	 */
	private List<ExecutionLogDto> executionLogs;
	
	public static EmpCalAndSumExeLogDto fromDomain(EmpCalAndSumExeLog domain, List<ExecutionLog> lstExecutionLog) {
		return new EmpCalAndSumExeLogDto(
				domain.getEmpCalAndSumExecLogID(), 
				domain.getCompanyID(), 
				domain.getProcessingMonth().v(), 
				domain.getExecutedMenu().value, 
				domain.getExecutionDate(), 
				domain.getExecutionStatus().value,
				domain.getExecutionStatus().nameId,
				domain.getEmployeeID(),
				domain.getClosureID(),
				domain.getCaseSpecExeContentID(),
				lstExecutionLog.stream().map(c->ExecutionLogDto.fromDomain(c)).collect(Collectors.toList())
				);
	}

}

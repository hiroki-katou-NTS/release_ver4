
package nts.uk.ctx.at.record.dom.workrecord.log;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.workrecord.log.enums.ErrorPresent;
import nts.uk.ctx.at.record.dom.workrecord.log.enums.ExeStateOfCalAndSum;
import nts.uk.ctx.at.record.dom.workrecord.log.enums.ExecutedMenu;
import nts.uk.ctx.at.record.dom.workrecord.log.enums.ExecutionContent;
import nts.uk.ctx.at.record.dom.workrecord.log.enums.ExecutionStatus;


/**
 * 就業計算と集計実行ログ
 * @author tutk
 *
 */
@Getter 
public class EmpCalAndSumExeLog extends AggregateRoot {

	/**就業計算と集計実行ログID */
	private String empCalAndSumExecLogID;
	
	/** 会社ID */
	private String companyID;
	
	/**
	 * 処理月
	 */
	private YearMonth processingMonth;
	
	/**
	 * 実行したメニュー
	 * ・選択して実行 ・ケース別実行
	 */
	private ExecutedMenu executedMenu;
	
	/**
	 * 実行日
	 */

	private GeneralDate executionDate;
	
	/**
	 * 実行状況
	 */
	private ExeStateOfCalAndSum executionStatus;
	
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
	@Setter
	private List<ExecutionLog> executionLogs;
	public void addExecutionLog(ExecutionLog executionLog) {
		this.executionLogs.add(executionLog);
	}
	
	public EmpCalAndSumExeLog(String empCalAndSumExecLogID, String companyID, YearMonth processingMonth,
			ExecutedMenu executedMenu, GeneralDate executionDate, ExeStateOfCalAndSum executionStatus,
			String employeeID, int closureID, String caseSpecExeContentID, List<ExecutionLog> executionLogs) {
		super();
		this.empCalAndSumExecLogID = empCalAndSumExecLogID;
		this.companyID = companyID;
		this.processingMonth = processingMonth;
		this.executedMenu = executedMenu;
		this.executionDate = executionDate;
		this.executionStatus = executionStatus;
		this.employeeID = employeeID;
		this.closureID = closureID;
		this.caseSpecExeContentID = caseSpecExeContentID;
		this.executionLogs = executionLogs;
	}
	
	public static EmpCalAndSumExeLog createFromJavaType(
			String empCalAndSumExecLogID,
			String companyID,
			YearMonth processingMonth,
			int executedMenu,
			GeneralDate executionDate,
			int executionStatus,
			String employeeID,
			int closureID,
			String caseSpecExeContentID,
			List<ExecutionLog> executionLogs) {
		return new EmpCalAndSumExeLog(
				empCalAndSumExecLogID,
				companyID,
				processingMonth,
				EnumAdaptor.valueOf(executedMenu,ExecutedMenu.class),
				executionDate,
				EnumAdaptor.valueOf(executionStatus,ExeStateOfCalAndSum.class),
				employeeID,
				closureID,
				caseSpecExeContentID,
				executionLogs);
	}
	
}

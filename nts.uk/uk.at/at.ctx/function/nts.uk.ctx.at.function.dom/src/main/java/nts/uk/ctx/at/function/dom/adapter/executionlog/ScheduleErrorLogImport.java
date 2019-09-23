package nts.uk.ctx.at.function.dom.adapter.executionlog;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.time.GeneralDate;

@Getter
@Setter
@NoArgsConstructor
public class ScheduleErrorLogImport {
	/** The error content. */
	// エラー内容
	private String errorContent;

	/** The execution id. */
	// 実行ID
	private String executionId;

	/** The date. */
	// 年月日
	private GeneralDate date;

	/** The employee id. */
	// 社員ID
	private String employeeId;

	public ScheduleErrorLogImport(String errorContent, String executionId, GeneralDate date, String employeeId) {
		super();
		this.errorContent = errorContent;
		this.executionId = executionId;
		this.date = date;
		this.employeeId = employeeId;
	}
}

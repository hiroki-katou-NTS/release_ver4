package nts.uk.ctx.at.record.dom.organization;

import lombok.Getter;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Getter
public class EmploymentHistoryImported {

	/** The employee id. */
	// 社員ID
	private String employeeId;

	/** The job title code. */
	// 雇用コード
	private String employmentCode;

	/** The job title name. */
	// 雇用名称
	private String employmentName;

	/** The period. */
	// 配属期間
	private DatePeriod period;

	public EmploymentHistoryImported(String employeeId, String employmentCode, String employmentName,
			DatePeriod period) {
		super();
		this.employeeId = employeeId;
		this.employmentCode = employmentCode;
		this.employmentName = employmentName;
		this.period = period;
	}

}

package nts.uk.ctx.sys.gateway.dom.adapter.syjobtitle;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.arc.time.GeneralDate;

@Data
@AllArgsConstructor
public class EmployeeJobHistImport {

	/** The employee id. */
	// 社員ID
	private String employeeId;

	/** The job title id. */
	// 職位ID
	private String jobTitleID;

	/** The job title name. */
	// 職位名称
	private String jobTitleName;

	/** The start date. */
	// 配属期間 start
	private GeneralDate startDate;

	/** The end date. */
	// 配属期間 end
	private GeneralDate endDate;
}

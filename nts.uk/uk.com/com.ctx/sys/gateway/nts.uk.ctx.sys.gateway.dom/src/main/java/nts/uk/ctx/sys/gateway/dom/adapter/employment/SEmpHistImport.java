/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.dom.adapter.employment;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Class SEmpHistImport.
 */

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Data
@AllArgsConstructor
public class SEmpHistImport {

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
}

/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.at.app.export.monthlyschedule;

import java.util.List;

import lombok.Data;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.file.at.app.export.dailyschedule.FileOutputType;

/**
 * The Class MonthlyWorkScheduleQuery.
 */

@Data
public class MonthlyWorkScheduleQuery {
	
	/** The start date. */
	private YearMonth startYearMonth;
	
	/** The end date. */
	private YearMonth endYearMonth;
	
	/** The workplace ids. */
	private List<String> employeeId;
	
	/** The condition. */
	private MonthlyWorkScheduleCondition condition;
	
	/** The condition code. */
	private String code;
	
	/** The file type. */
	private FileOutputType fileType;
	
	private GeneralDate baseDate;
	
	private Integer closureId;
}

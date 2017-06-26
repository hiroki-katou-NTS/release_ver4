/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.pr.app.export.denominationtable.data;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;




/**
 * The Class DenominationTableData.
 */
/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Builder

/**
 * Gets the salary chart item data.
 *
 * @return the salary chart item data
 */

/**
 * Gets the employee list.
 *
 * @return the employee list */

/**
 * Gets the employee list.
 *
 * @return the employee list
 */
@Getter

/**
 * Sets the salary chart item data.
 *
 * @param salaryChartItemData the new salary chart item data
 */

/**
 * Sets the employee list.
 *
 * @param employeeList the new employee list
 */

/**
 * Sets the employee list.
 *
 * @param employeeList the new employee list
 */
@Setter
public class DenominationTableData {
	
	/** The salary chart header. */
	private DenoTableHeaderData salaryChartHeader;
	
	/** The employee list. */
	private List<EmployeeData> employeeList;
}

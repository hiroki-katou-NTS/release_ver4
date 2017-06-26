/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.pr.app.export.denominationtable.data;

import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Builder

/**
 * Gets the dep path.
 *
 * @return the dep path
 */
@Getter

/**
 * Sets the dep path.
 *
 * @param depPath the new dep path
 */
@Setter
public class EmployeeData {

	/** The emp code. */
	private String empCode;
	
	/** The emp name. */
	private String empName;	
	
	/** The payment amount. */
	private Double paymentAmount;	
	
	/** The denomination. */
	private Map<Denomination, Long> denomination;
	
	private DepartmentData departmentData;

	
}

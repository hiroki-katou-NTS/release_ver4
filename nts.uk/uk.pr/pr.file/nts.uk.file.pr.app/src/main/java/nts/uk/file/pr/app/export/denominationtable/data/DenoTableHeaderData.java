/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.pr.app.export.denominationtable.data;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


/**
 * The Class DenoTableHeaderData.
 */
/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Builder

/**
 * Gets the emp type.
 *
 * @return the emp type
 */

/**
 * Gets the category info.
 *
 * @return the category info
 */
@Getter

/**
 * Sets the emp type.
 *
 * @param empType the new emp type
 */

/**
 * Sets the category info.
 *
 * @param categoryInfo the new category info
 */
@Setter
public class DenoTableHeaderData {
	
	/** The target year month. */
	private String targetYearMonth;
	
	/** The department info. */
	private String departmentInfo;
	
	/** The position info. */
	private String positionInfo;
	
	/** The category info. */
	private String categoryInfo;
	

}

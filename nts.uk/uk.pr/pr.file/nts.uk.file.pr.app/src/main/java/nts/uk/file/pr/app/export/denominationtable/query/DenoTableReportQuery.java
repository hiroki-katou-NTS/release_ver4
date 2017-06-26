/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.pr.app.export.denominationtable.query;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class DenoTableReportQuery.
 */

/**
 * Instantiates a new salary chart report query.
 */

/**
 * Instantiates a new salary chart report query.
 */

/**
 * Instantiates a new salary chart report query.
 */

/**
 * Instantiates a new salary chart report query.
 */

/**
 * Instantiates a new salary chart report query.
 */

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */

/**
 * Gets the checks if is break page by accumulated.
 *
 * @return the checks if is break page by accumulated
 */
@Getter

/**
 * Sets the checks if is break page by accumulated.
 *
 * @param isBreakPageByAccumulated the new checks if is break page by accumulated
 */

/**
 * Sets the checks if is break page by accumulated.
 *
 * @param isBreakPageByAccumulated the new checks if is break page by accumulated
 */
@Setter
public class DenoTableReportQuery {

	/** The year month. */
	private Integer yearMonth;
	
	/** The p id list. */
	private List<String> pIdList;
	
	/** The selected division. */
	private Integer selectedDivision;	
	
	/** The is print detail item. */
	private Boolean isPrintDetailItem;
	
	/** The is print total of department. */
	private Boolean isPrintTotalOfDepartment;
	
	/** The is print dep hierarchy. */
	private Boolean isPrintDepHierarchy;
	
	/** The selected levels. */ 
	private List<Integer> selectedLevels;
	
	/** The is calculate total. */
	private Boolean isCalculateTotal;
	
	/** The selected break page code. */
	private Integer selectedBreakPageCode;

	/** The selected use 2000 yen. */
	private Integer selectedUse2000yen; 
	
	/** The selected break page hierarchy code. */
	private Integer selectedBreakPageHierarchyCode;
	
	/** The is break page by accumulated. */
	private Boolean isBreakPageByAccumulated;
}

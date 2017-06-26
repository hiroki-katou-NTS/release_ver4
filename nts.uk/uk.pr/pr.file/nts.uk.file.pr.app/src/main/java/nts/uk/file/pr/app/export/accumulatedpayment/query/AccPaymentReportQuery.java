/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.pr.app.export.accumulatedpayment.query;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * The Class AccPaymentReportQuery.
 */

/**
 * Instantiates a new acc payment report query.
 */
@Data
public class AccPaymentReportQuery {

	/** The target year. */
	private Integer targetYear;

	/** The emp id list. */
	private List<String> pIdList;
	
	/** The is lower limit. */
	private Boolean isLowerLimit;
	
	/** The is upper limit. */
	private Boolean isUpperLimit;

	/** The lower limit value. */
	private BigDecimal lowerLimitValue;

	/** The upper limit value. */
	private BigDecimal upperLimitValue;
	
	/** The base date. */
	private Date baseDate;
}

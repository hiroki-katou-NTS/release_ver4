/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.pr.app.export.accumulatedpayment;

import java.util.List;

import nts.uk.file.pr.app.export.accumulatedpayment.data.AccPaymentItemData;
import nts.uk.file.pr.app.export.accumulatedpayment.query.AccPaymentReportQuery;



/**
 * The Interface AccPaymentRepository.
 */
public interface AccPaymentRepository {
	/**
	 * Gets the items.
	 *
	 * @param companyCode the company code
	 * @param query the query
	 * @return the items
	 */
	List<AccPaymentItemData> getItems(String companyCode, AccPaymentReportQuery query);
}

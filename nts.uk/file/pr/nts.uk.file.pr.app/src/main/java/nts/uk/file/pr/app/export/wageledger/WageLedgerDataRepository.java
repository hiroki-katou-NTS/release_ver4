/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.pr.app.export.wageledger;

import java.util.List;

import nts.uk.file.pr.app.export.wageledger.data.WLOldLayoutReportData;

/**
 * The Interface WageLedgerDataRepository.
 */
public interface WageLedgerDataRepository {
	
	/**
	 * Find report data.
	 *
	 * @param query the query
	 * @return the wage ledger report data
	 */
	List<WLOldLayoutReportData> findReportDatas(String companyCode, WageLedgerReportQuery query);
	
	/**
	 * Check has report data.
	 *
	 * @param companyCode the company code
	 * @param query the query
	 * @return true, if successful
	 */
	boolean hasReportData(String companyCode, WageLedgerReportQuery query);
}

/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.pr.app.export.denominationtable;

import java.util.List;

import nts.uk.file.pr.app.export.denominationtable.data.EmployeeData;
import nts.uk.file.pr.app.export.denominationtable.query.DenoTableReportQuery;


/**
 * The Interface DenoTableRepository.
 */
public interface DenoTableRepository {

	/**
	 * Gets the items.
	 *
	 * @param companyCode the company code
	 * @param query the query
	 * @return the items
	 */
	List<EmployeeData> getItems(String companyCode, DenoTableReportQuery query);
	
	
}

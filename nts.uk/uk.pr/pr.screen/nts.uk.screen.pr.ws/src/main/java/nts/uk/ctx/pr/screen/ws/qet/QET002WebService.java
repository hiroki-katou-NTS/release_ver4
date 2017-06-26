/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.screen.ws.qet;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.app.file.export.ExportServiceResult;
import nts.arc.layer.ws.WebService;
import nts.uk.file.pr.app.export.accumulatedpayment.AccPaymentReportService;
import nts.uk.file.pr.app.export.accumulatedpayment.query.AccPaymentReportQuery;


/**
 * The Class QET002WebService.
 */
@Path("/screen/pr/qet002")
@Produces("application/json")
public class QET002WebService extends WebService {

	/** The export service. */
	@Inject
	private AccPaymentReportService exportService;
	
	/**
	 * Generate.
	 *
	 * @param query the query
	 * @return the export service result
	 */
	@POST
	@Path("generate")
	public ExportServiceResult generate(AccPaymentReportQuery query) {
	// TODO: validate print dto.\	
		// Validate
//				if (query.getTargetYear() == 0) {
//					throw new RuntimeException("Target Year is Empty");
//				}
//				if ((query.isLowerLimit() == true) && (query.getLowerLimitValue() == 0)) {			
//					throw new RuntimeException("Lower Limit Value is Not Entered.");			
//				}
//				if ((query.isUpperLimit() == true) && (query.getUpperLimitValue() == 0)) {			
//					throw new RuntimeException("Upper Limit Value is Not Entered.");			
//				}
//				if ((query.isLowerLimit() == true) && (query.isUpperLimit() == true)) {
//					if (query.getLowerLimitValue() > query.getUpperLimitValue()) {
//						throw new RuntimeException("Lower Limit Value is greater than Upper Limit Value.");
//					}
//				}
		return this.exportService.start(query);
	}
}

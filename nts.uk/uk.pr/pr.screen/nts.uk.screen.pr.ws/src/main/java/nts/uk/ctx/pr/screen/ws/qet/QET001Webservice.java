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
import nts.uk.file.pr.app.export.wageledger.WageLedgerReportQuery;
import nts.uk.file.pr.app.export.wageledger.WageLedgerReportSevice;

/**
 * The Class QET001Webservice.
 */
@Path("/screen/pr/qet001")
@Produces("application/json")
public class QET001Webservice extends WebService{
	
	/** The report service. */
	@Inject
	private WageLedgerReportSevice reportService;
	
	/**
	 * Prints the report.
	 *
	 * @param printDto the print dto
	 * @return the export service result
	 */
	@POST
	@Path("print")
	public ExportServiceResult printReport(WageLedgerReportQuery query) {
		return this.reportService.start(query);
	}
}

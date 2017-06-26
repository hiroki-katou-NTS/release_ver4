/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
/**
 * 
 */
package nts.uk.ctx.pr.screen.ws.qpp;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.app.file.export.ExportServiceResult;
import nts.arc.layer.ws.WebService;
import nts.uk.file.pr.app.export.detailpayment.PaymentSalaryReportService;
import nts.uk.file.pr.app.export.detailpayment.query.PaymentSalaryQuery;

/**
 * The Class QPP007WebService.
 *
 */
@Path("/screen/pr/QPP007")
@Produces("application/json")
public class QPP007WebService extends WebService {

    /** The report service. */
    @Inject
    PaymentSalaryReportService reportService;
    
    /**
     * Export data to pdf.
     *
     * @param query the query
     * @return the export service result
     */
    @POST
    @Path("saveAsPdf")
    public ExportServiceResult exportDataToPdf(PaymentSalaryQuery query) {
         return reportService.start(query);
    }
}

package nts.uk.ctx.pr.screen.ws.qrm;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.app.file.export.ExportServiceResult;
import nts.arc.layer.ws.WebService;
import nts.uk.file.pr.app.export.retirementpayment.RetirementPaymentExportService;
import nts.uk.file.pr.app.export.retirementpayment.RetirementPaymentQuery;

/**
 * The Class QRM009WebService.
 */
@Path("/screen/pr/qrm009")
@Produces("application/json")
public class QRM009WebService extends WebService{
	/** The report service. */
	@Inject
	private RetirementPaymentExportService retirementPaymentExportService;
	
	/**
     * Export data to pdf.
     *
     * @param query the query
     * @return the export service result
     */
    @POST
    @Path("saveAsPdf")
    public ExportServiceResult exportDataToPdf(RetirementPaymentQuery query) {
        return retirementPaymentExportService.start(query);
   }
}

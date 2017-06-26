package nts.uk.ctx.pr.screen.ws.qpp;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.app.file.export.ExportServiceResult;
import nts.arc.layer.ws.WebService;
import nts.uk.file.pr.app.export.residentialtax.InhabitantTaxChecklistQuery;
import nts.uk.file.pr.app.export.residentialtax.InhabitantTaxChecklistReportBService;
import nts.uk.file.pr.app.export.residentialtax.InhabitantTaxChecklistReportCService;
import nts.uk.file.pr.app.export.residentialtax.OutputPaymentDataReportService;
import nts.uk.file.pr.app.export.residentialtax.ResidentialTaxQuery;
import nts.uk.file.pr.app.export.residentialtax.ResidentialTaxReportService;

@Path("/screen/pr/QPP011")
@Produces("application/json")
public class QPP011WebService extends WebService {

	@Inject
	ResidentialTaxReportService reportService;
	
	@Inject
	InhabitantTaxChecklistReportBService reportServiceB;
	
	@Inject
	InhabitantTaxChecklistReportCService reportServiceC;
	
	@Inject
	private OutputPaymentDataReportService reportPaymentDataService;

	@POST
	@Path("saveAsPdf")
	public ExportServiceResult exportDataToPdf(ResidentialTaxQuery query) {
		return reportService.start(query);
	}
	
	@POST
	@Path("saveAsPdfB")
	public ExportServiceResult exportDataB(InhabitantTaxChecklistQuery query) {
		return reportServiceB.start(query);
	}
	
	@POST
	@Path("saveAsPdfC")
	public ExportServiceResult exportDataC(InhabitantTaxChecklistQuery query) {
		return reportServiceC.start(query);
	}
	
	@POST
	@Path("savePaymentData")
	public ExportServiceResult exportPaymentData(ResidentialTaxQuery query) {
		return reportPaymentDataService.start(query);
	}
	
	

}

package nts.uk.ctx.pr.screen.ws.qpp;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.app.file.export.ExportServiceResult;
import nts.arc.layer.ws.WebService;
import nts.uk.file.pr.app.export.banktransfer.BankTransferReportAService;
import nts.uk.file.pr.app.export.banktransfer.BankTransferReportBService;
import nts.uk.file.pr.app.export.banktransfer.query.BankTransferReportQuery;

@Path("/screen/pr/QPP014")
@Produces("application/json")
public class QPP014WebService extends WebService {

	@Inject
	private BankTransferReportAService reportAService;
	
	@Inject
	private BankTransferReportBService reportBService;


	@POST
	@Path("saveAsPdfA")
	public ExportServiceResult exportDataToPdf(BankTransferReportQuery query) {
		return reportAService.start(query);
	}
	
	@POST
	@Path("saveAsPdfB")
	public ExportServiceResult exportDataToPdfB(BankTransferReportQuery query) {
		return reportBService.start(query);
	}

}

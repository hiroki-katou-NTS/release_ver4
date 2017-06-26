package nts.uk.pr.file.ws.paymentdata;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.infra.file.export.FileGeneratorContext;
import nts.arc.layer.ws.WebService;
import nts.uk.pr.file.infra.paymentdata.PaymentDataPrintFileGenerator;
import nts.uk.pr.file.infra.paymentdata.PaymentDataQuery;
import nts.uk.pr.file.infra.paymentdata.PrintPaymentDataQueryProcessor;
import nts.uk.pr.file.infra.paymentdata.result.PaymentDataResult;

@Path("/file/paymentdata")
@Produces("application/json")
public class PaymentDataPrintWebservice extends WebService {

	@Inject
	private PaymentDataPrintFileGenerator paymentDataPrint;
	@Inject
	private PrintPaymentDataQueryProcessor getPaymentDataQueryProcessor;

	@POST
	@Path("print")
	public FileGeneratorContext print(List<PaymentDataQuery> query) {
		List<PaymentDataResult> results = new ArrayList<>();
		for (int i = 0; i < query.size(); i++) {
			PaymentDataResult result = this.getPaymentDataQueryProcessor.find(query.get(i));
			if (result != null) {
				results.add(result);
			}
		}
		//return this.paymentDataPrint.start(results);
		return null;
	}

}

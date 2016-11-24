package nts.uk.ctx.pr.screen.ws.qpp;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.uk.ctx.pr.screen.app.query.qpp005.GetPaymentDataQueryProcessor;
import nts.uk.ctx.pr.screen.app.query.qpp005.PaymentDataQuery;

@Path("/screen/pr/qpp005/paymentdata")
@Produces("application/json")
public class QPP005WebService {
	@Inject
	private GetPaymentDataQueryProcessor getPaymentDataQueryProcessor;

	@POST
	@Path("find")
	public void find(PaymentDataQuery query) {
		this.getPaymentDataQueryProcessor.find(query);
	}
}

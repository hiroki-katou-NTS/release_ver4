package employment.procesing.yearmonth;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.shr.find.employment.processing.yearmonth.IPaydayProcessingFinder;
import nts.uk.shr.find.employment.processing.yearmonth.PaydayProcessingDto;

@Path("pr/core")
@Produces("application/json")
public class PaydayProcessingWebservice extends WebService {

	@Inject
	private IPaydayProcessingFinder processing;

	@POST
	@Path("paydayrocessing/getbyccd")
	public List<PaydayProcessingDto> getPaydayProcessing() {
		return this.processing.getPaydayProcessing("");
	}
}

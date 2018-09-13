package nts.uk.ctx.core.ws.system.welfarepensioninsurance;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.uk.ctx.core.app.find.socialinsurance.welfarepensioninsurance.WelfarePensionInsuranceFinder;
import nts.uk.ctx.core.app.find.socialinsurance.welfarepensioninsurance.dto.WelfarePensionInsuranceRateHistoryDto;

@Path("ctx/core/socialinsurance/welfarepensioninsurance")
@Produces("application/json")
public class SocialInsuranceOfficeWebservice {
	
	@Inject
	private WelfarePensionInsuranceFinder welfarePensionInsuranceFinder;
	
	@POST
	@Path("/getByCompanyId")
	public List<WelfarePensionInsuranceRateHistoryDto> getOfficeByCompanyId() {
		return welfarePensionInsuranceFinder.findOfficeByCompanyId();
	}
}

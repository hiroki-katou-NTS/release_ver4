package nts.uk.ctx.pr.core.ws.rule.law.tax.residential.output;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.core.app.find.rule.law.tax.residential.output.RegalDocCompanyFinder;
import nts.uk.ctx.pr.core.app.find.rule.law.tax.residential.output.dto.RegalDocCompanyDto;

@Path("pr/core/rule/law/tax/residential/output")
@Produces("application/json")
public class RegalDocCompanyWebService extends WebService {

	@Inject
	private RegalDocCompanyFinder finder;
	
	/**
	 * Find all Regal Doc Company
	 * @return
	 */
	@Path("findallRegalDoc")
	@POST
	public List<RegalDocCompanyDto> findAll() {
		return this.finder.findAll();
	}
}

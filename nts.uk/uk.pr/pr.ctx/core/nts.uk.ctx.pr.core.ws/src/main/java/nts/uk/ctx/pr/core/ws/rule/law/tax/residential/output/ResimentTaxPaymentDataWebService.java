package nts.uk.ctx.pr.core.ws.rule.law.tax.residential.output;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.core.app.command.rule.law.tax.residential.output.AddResimentTaxPaymentDataCommand;
import nts.uk.ctx.pr.core.app.command.rule.law.tax.residential.output.AddResimentTaxPaymentDataCommandHandler;
import nts.uk.ctx.pr.core.app.command.rule.law.tax.residential.output.UpdateResimentTaxPaymentDataCommand;
import nts.uk.ctx.pr.core.app.command.rule.law.tax.residential.output.UpdateResimentTaxPaymentDataCommandHandler;
import nts.uk.ctx.pr.core.app.find.rule.law.tax.residential.output.ResimentTaxPaymentDataFinder;
import nts.uk.ctx.pr.core.app.find.rule.law.tax.residential.output.dto.ResimentTaxPaymentDataDto;

@Path("pr/core/rule/law/tax/residential/output")
@Produces("application/json")
public class ResimentTaxPaymentDataWebService extends WebService {
	@Inject
	private ResimentTaxPaymentDataFinder finder;
	@Inject
	private AddResimentTaxPaymentDataCommandHandler addHandler;
	@Inject
	private UpdateResimentTaxPaymentDataCommandHandler updateHandler;

	/**
	 * Find Resiment Tax Payment by ResimentTaxCode and YearMonth
	 * @param resimentTaxCode
	 * @param yearMonth
	 * @return
	 */
	@Path("find/{resimentTaxCode}/{yearMonth}")
	@POST
	public ResimentTaxPaymentDataDto find(@PathParam("resimentTaxCode") String resimentTaxCode,
			@PathParam("yearMonth") int yearMonth) {
		return this.finder.find(resimentTaxCode, yearMonth);
	}

	/**
	 * Add Resiment Tax Payment
	 * @param command
	 */
	@Path("add")
	@POST
	public void add(AddResimentTaxPaymentDataCommand command) {
		this.addHandler.handle(command);
	}

	/**
	 * Update Resiment Tax Payment
	 * @param command
	 */
	@Path("update")
	@POST
	public void update(UpdateResimentTaxPaymentDataCommand command) {
		this.updateHandler.handle(command);
	}
}

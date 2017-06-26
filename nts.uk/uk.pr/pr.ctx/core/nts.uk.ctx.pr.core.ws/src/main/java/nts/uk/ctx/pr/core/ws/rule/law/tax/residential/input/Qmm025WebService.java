package nts.uk.ctx.pr.core.ws.rule.law.tax.residential.input;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.core.app.command.rule.law.tax.residential.input.RemovePersonResiTaxCommand;
import nts.uk.ctx.pr.core.app.command.rule.law.tax.residential.input.RemovePersonResiTaxCommandHandler;
import nts.uk.ctx.pr.core.app.command.rule.law.tax.residential.input.UpdatePersonResiTaxCommand;
import nts.uk.ctx.pr.core.app.command.rule.law.tax.residential.input.UpdatePersonResiTaxCommandHandler;
import nts.uk.ctx.pr.core.app.find.rule.law.tax.residential.input.PersonResiTaxDto;
import nts.uk.ctx.pr.core.app.find.rule.law.tax.residential.input.PersonResiTaxFinder;

@Path("pr/core/rule/law/tax/residential/input")
@Produces("application/json")
public class Qmm025WebService extends WebService {
	@Inject
	private PersonResiTaxFinder personResiTaxFinder;

	@Inject
	private RemovePersonResiTaxCommandHandler removePersonResiTaxCommandHandler;

	@Inject
	private UpdatePersonResiTaxCommandHandler updatePersonResiTaxCommandHandler;

	@POST
	@Path("findAll/{yearKey}")
	public List<PersonResiTaxDto> findAll(@PathParam("yearKey") int yearKey) {
		return this.personResiTaxFinder.findAll(yearKey);
	}

	@POST
	@Path("findAll/{yearKey}/{residenceCode}")
	public List<String> findByResidenceCode(@PathParam("yearKey") int yearKey,
			@PathParam("residenceCode") String residenceCode) {
		return this.personResiTaxFinder.findByResidenceCode(residenceCode, yearKey);
	}

	@POST
	@Path("remove")
	public void remove(RemovePersonResiTaxCommand command) {
		this.removePersonResiTaxCommandHandler.handle(command);
	}

	@POST
	@Path("update")
	public void update(UpdatePersonResiTaxCommand command) {
		this.updatePersonResiTaxCommandHandler.handle(command);
	}

}

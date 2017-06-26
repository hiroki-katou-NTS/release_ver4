package nts.uk.ctx.basic.ws.system.bank;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.basic.app.find.system.bank.dto.PersonBankAccountDto;
import nts.uk.ctx.basic.app.find.system.bank.personaccount.PersonBankAccountFinder;

@Path("basic/system/bank/person")
@Produces("application/json")
public class PersonBankAccountWebService extends WebService {

	@Inject
	private PersonBankAccountFinder finder;
	
	/**
	 * find a person bank
	 */
	@POST
	@Path("find/{personId}")
	public PersonBankAccountDto find(@PathParam("personId") String personId) {
		return this.finder.find(personId);
	}
	
}

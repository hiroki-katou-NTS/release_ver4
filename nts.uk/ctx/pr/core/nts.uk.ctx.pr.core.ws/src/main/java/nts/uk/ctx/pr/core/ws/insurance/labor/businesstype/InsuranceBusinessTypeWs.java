/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.ws.insurance.labor.businesstype;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.uk.ctx.pr.core.app.insurance.labor.businesstype.command.InsuranceBusinessTypeUpdateCommand;
import nts.uk.ctx.pr.core.app.insurance.labor.businesstype.command.InsuranceBusinessTypeUpdateCommandHandler;
import nts.uk.ctx.pr.core.app.insurance.labor.businesstype.find.InsuranceBusinessTypeFinder;
import nts.uk.ctx.pr.core.app.insurance.labor.businesstype.find.dto.InsuranceBusinessTypeFindOutDto;

@Path("pr/insurance/labor/businesstype")
@Produces("application/json")
public class InsuranceBusinessTypeWs {

	/** The update. */
	@Inject
	InsuranceBusinessTypeUpdateCommandHandler update;

	/** The find. */
	@Inject
	InsuranceBusinessTypeFinder find;

	/**
	 * Update.
	 *
	 * @param command
	 *            the command
	 */
	@POST
	@Path("update")
	public void update(InsuranceBusinessTypeUpdateCommand command) {
		this.update.handle(command);
	}

	@POST
	@Path("findall")
	public InsuranceBusinessTypeFindOutDto findAll() {
		return this.find.findAll();
	}
}

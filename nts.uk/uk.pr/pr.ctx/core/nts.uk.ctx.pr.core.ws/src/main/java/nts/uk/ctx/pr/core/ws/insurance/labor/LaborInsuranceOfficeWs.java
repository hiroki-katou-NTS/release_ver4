/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.ws.insurance.labor;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.core.app.insurance.labor.command.LaborInsuranceOfficeAddCommand;
import nts.uk.ctx.pr.core.app.insurance.labor.command.LaborInsuranceOfficeAddCommandHandler;
import nts.uk.ctx.pr.core.app.insurance.labor.command.LaborInsuranceOfficeDeleteCommand;
import nts.uk.ctx.pr.core.app.insurance.labor.command.LaborInsuranceOfficeDeleteCommandHandler;
import nts.uk.ctx.pr.core.app.insurance.labor.command.LaborInsuranceOfficeUpdateCommand;
import nts.uk.ctx.pr.core.app.insurance.labor.command.LaborInsuranceOfficeUpdateCommandHandler;
import nts.uk.ctx.pr.core.app.insurance.labor.find.LaborInsuranceOfficeFinder;
import nts.uk.ctx.pr.core.app.insurance.labor.find.dto.LaborInsuranceOfficeFindDto;
import nts.uk.ctx.pr.core.app.insurance.labor.find.dto.LaborInsuranceOfficeFindOutDto;

/**
 * The Class LaborInsuranceOfficeWs.
 */
@Path("ctx/pr/core/insurance/labor")
@Produces("application/json")
public class LaborInsuranceOfficeWs extends WebService {

	/** The find. */
	@Inject
	private LaborInsuranceOfficeFinder find;

	/** The add. */
	@Inject
	private LaborInsuranceOfficeAddCommandHandler add;

	/** The update. */
	@Inject
	private LaborInsuranceOfficeUpdateCommandHandler update;

	/** The delete. */
	@Inject
	private LaborInsuranceOfficeDeleteCommandHandler delete;

	/**
	 * Find all.
	 *
	 * @return the list
	 */
	@POST
	@Path("findall")
	public List<LaborInsuranceOfficeFindOutDto> findAll() {
		return find.findAll();
	}

	/**
	 * Find by code.
	 *
	 * @param officeCode
	 *            the office code
	 * @return the labor insurance office find dto
	 */
	@POST
	@Path("findLaborInsuranceOffice/{officeCode}")
	public LaborInsuranceOfficeFindDto findByCode(@PathParam("officeCode") String officeCode) {
		return find.findById(officeCode);
	}

	/**
	 * Adds the.
	 *
	 * @param command
	 *            the command
	 */
	@POST
	@Path("add")
	public void add(LaborInsuranceOfficeAddCommand command) {
		this.add.handle(command);
	}

	/**
	 * Update.
	 *
	 * @param command
	 *            the command
	 */
	@POST
	@Path("update")
	public void update(LaborInsuranceOfficeUpdateCommand command) {
		this.update.handle(command);
	}

	/**
	 * Delete.
	 *
	 * @param command
	 *            the command
	 */
	@POST
	@Path("delete")
	public void delete(LaborInsuranceOfficeDeleteCommand command) {
		this.delete.handle(command);
	}
}

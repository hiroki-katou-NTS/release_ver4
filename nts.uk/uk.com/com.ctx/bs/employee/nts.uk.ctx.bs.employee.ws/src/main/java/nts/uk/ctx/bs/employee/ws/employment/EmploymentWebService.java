/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.ws.employment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.bs.employee.app.command.employment.EmpRemoveCommand;
import nts.uk.ctx.bs.employee.app.command.employment.EmpRemoveCommandHandler;
import nts.uk.ctx.bs.employee.app.command.employment.EmpSaveCommand;
import nts.uk.ctx.bs.employee.app.command.employment.EmpSaveCommandHandler;
import nts.uk.ctx.bs.employee.app.find.employment.EmploymentFinder;
import nts.uk.ctx.bs.employee.app.find.employment.dto.EmploymentDto;

/**
 * The Class EmploymentWebService.
 */
@Path("bs/employee/employment")
@Produces(MediaType.APPLICATION_JSON)
public class EmploymentWebService extends WebService {
	
	/** The finder. */
	@Inject
	private EmploymentFinder finder;
	
	/** The save handler. */
	@Inject
	private EmpSaveCommandHandler saveHandler;
	
	/** The remove handler. */
	@Inject
	private EmpRemoveCommandHandler removeHandler;
	
	/**
	 * Find all.
	 *
	 * @return the list
	 */
	@POST
	@Path("findAll")
	public List<EmploymentDto> findAll() {
		return this.finder.findAll();
	}
	
	
	/**
	 * Find by id.
	 *
	 * @param employmentCode the employment code
	 * @return the employment dto
	 */
	@POST
	@Path("findByCode/{employmentCode}")
	public EmploymentDto findByCode(@PathParam("employmentCode") String employmentCode) {
		return this.finder.findByCode(employmentCode);
	}
	
	/**
	 * Removes the.
	 *
	 * @param command the command
	 */
	@POST
	@Path("remove")
	public void remove(EmpRemoveCommand command) {
		this.removeHandler.handle(command);
	}

	/**
	 * Save.
	 *
	 * @param command the command
	 */
	@POST
	@Path("save")
	public void save(EmpSaveCommand command) {
		this.saveHandler.handle(command);
	}
	
	@POST
	@Path("findByCodes")
	public List<EmploymentDto> findByCodes(List<String> employmentCodes) {
		return this.finder.findByCodes(employmentCodes);
	}
	
	@POST
	@Path("findNamesByCodes")
	public List<String> findNamesByCodes(List<String> employmentCodes) {
		List<String> names = new ArrayList<>();
		if (employmentCodes == null || employmentCodes.isEmpty()) return names;
		names = this.finder.findByCodes(employmentCodes).stream().map(item -> item.getName()).collect(Collectors.toList());
		return names;
	}
	
	@POST
	@Path("findNamesByCodesWithNull")
	public List<String> findNamesByCodesWithNull(List<String> employmentCodes) {
		List<String> names = new ArrayList<>();
		if (employmentCodes == null || employmentCodes.isEmpty()) return names;
		names = this.finder.findByCodesWithNull(employmentCodes).stream().map(item -> item.getName()).collect(Collectors.toList());
		return names;
	}
}

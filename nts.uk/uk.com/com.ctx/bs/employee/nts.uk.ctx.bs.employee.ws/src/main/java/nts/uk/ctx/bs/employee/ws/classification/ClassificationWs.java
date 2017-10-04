/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.ws.classification;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.bs.employee.app.command.classification.ClfRemoveCommand;
import nts.uk.ctx.bs.employee.app.command.classification.ClfRemoveCommandHandler;
import nts.uk.ctx.bs.employee.app.command.classification.ClfSaveCommand;
import nts.uk.ctx.bs.employee.app.command.classification.ClfSaveCommandHandler;
import nts.uk.ctx.bs.employee.app.find.classification.ClassificationFinder;
import nts.uk.ctx.bs.employee.app.find.classification.dto.ClassificationFindDto;

/**
 * The Class ManagementCategoryWs.
 */
@Path("basic/company/organization/classification")
@Produces(MediaType.APPLICATION_JSON)
public class ClassificationWs extends WebService{

	/** The finder. */
	@Inject
	private ClassificationFinder finder;
	
	/** The save handler. */
	@Inject
	private ClfSaveCommandHandler saveHandler;
	
	/** The remove handler. */
	@Inject
	private ClfRemoveCommandHandler removeHandler;
	
	/**
	 * Find all.
	 *
	 * @return the list
	 */
	@Path("findAll")
	@POST
	public List<ClassificationFindDto> findAll() {
		return this.finder.findAll();
	}
	
	/**
	 * Save.
	 *
	 * @param command the command
	 */
	@Path("save")
	@POST
	public void save(ClfSaveCommand command) {
		this.saveHandler.handle(command);
	}
	
	/**
	 * Removes the.
	 *
	 * @param command the command
	 */
	@Path("remove")
	@POST
	public void remove(ClfRemoveCommand command) {
		this.removeHandler.handle(command);
	}
}

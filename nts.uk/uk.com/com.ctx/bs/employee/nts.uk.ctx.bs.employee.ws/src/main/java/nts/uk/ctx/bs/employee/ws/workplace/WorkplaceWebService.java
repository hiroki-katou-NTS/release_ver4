/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.ws.workplace;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.bs.employee.app.find.workplace.WorkplaceConfigFinder;
import nts.uk.ctx.bs.employee.app.find.workplace.dto.WorkplaceCommandDto;
import nts.uk.ctx.bs.employee.app.find.workplace.dto.WorkplaceConfigDto;

@Path("bs/employee/workplace")
@Produces(MediaType.APPLICATION_JSON)
public class WorkplaceWebService extends WebService {

	/** The wkp config finder. */
	@Inject
	private WorkplaceConfigFinder wkpConfigFinder;

	/**
	 * Find last config.
	 *
	 * @param dto the dto
	 * @return the workplace config dto
	 */
	@Path("findLastConfig")
	@POST
	public WorkplaceConfigDto findLastConfig(WorkplaceCommandDto dto) {
		return this.wkpConfigFinder.findLastestByCompanyId();
	}
}

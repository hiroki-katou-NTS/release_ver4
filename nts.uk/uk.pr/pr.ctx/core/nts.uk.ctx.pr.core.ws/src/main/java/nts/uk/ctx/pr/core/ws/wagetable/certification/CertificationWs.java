/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.ws.wagetable.certification;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.core.app.wagetable.certification.find.CertificationFinder;
import nts.uk.ctx.pr.core.app.wagetable.certification.find.dto.CertificationFindInDto;

/**
 * The Class CertificationWs.
 */
@Path("pr/wagetable/certification")
@Produces("application/json")
public class CertificationWs extends WebService {

	/** The find. */
	@Inject
	private CertificationFinder find;

	/**
	 * Find all.
	 *
	 * @return the list
	 */
	@POST
	@Path("findall")
	public List<CertificationFindInDto> findAll() {
		return this.find.findAll();
	}
}

/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.ws.itemmaster;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.report.app.itemmaster.query.ItemMaterRepository;
import nts.uk.ctx.pr.report.app.itemmaster.query.MasterItemDto;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class ItemMasterWebservice.
 */
@Path("ctx/pr/report/masteritem")
@Produces("application/json")
public class ItemMasterWebservice extends WebService {

	/** The item mater finder. */
	@Inject
	private ItemMaterRepository itemMaterFinder;

	/**
	 * Find all.
	 *
	 * @return the list
	 */
	@POST
	@Path("findAll")
	public List<MasterItemDto> findAll() {
		String companyCode = AppContexts.user().companyCode();
		return this.itemMaterFinder.findAll(companyCode);
	}
}

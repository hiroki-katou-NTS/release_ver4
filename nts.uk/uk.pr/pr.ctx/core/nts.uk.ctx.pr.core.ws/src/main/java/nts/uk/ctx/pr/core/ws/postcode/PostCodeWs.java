/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.ws.postcode;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.core.app.postcode.find.PostCode;
import nts.uk.ctx.pr.core.app.postcode.find.PostCodeFinder;

/**
 * The Class AddressWs.
 */
@Path("ctx/pr/core/postcode")
@Produces("application/json")
public class PostCodeWs extends WebService {

	/** The finder. */
	@Inject
	private PostCodeFinder finder;

	/**
	 * Find all.
	 *
	 * @param zipCode
	 *            the zip code
	 * @return the list
	 */
	@POST
	@Path("find/{zipCode}")
	public List<PostCode> findAll(@PathParam("zipCode") String zipCode) {
		return this.finder.findPostCodeList(zipCode);
	}

}

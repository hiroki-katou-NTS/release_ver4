package nts.uk.ctx.basic.ws.postcode;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.shr.find.postcode.IPostCodeFinder;
import nts.uk.shr.find.postcode.PostCode;

/**
 * The Class AddressWs.
 */
@Path("ctx/basic/postcode")
@Produces("application/json")
public class PostCodeWs extends WebService {
	@Inject
	private IPostCodeFinder finder;

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

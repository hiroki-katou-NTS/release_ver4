package nts.uk.ctx.bs.person.ws.roles;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import find.roles.auth.item.PersonInfoItemAuthFinder;
import find.roles.auth.item.PersonInfoItemDetailDto;
import nts.arc.layer.ws.WebService;

@Path("ctx/bs/person/roles/auth/item")
@Produces("application/json")
public class PersonInfoItemAuthWebservice extends WebService {
	@Inject
	PersonInfoItemAuthFinder personInfoItemAuthFinder;

	@POST
	@Path("findAllItem/{personInfoCategoryAuthId}")
	public List<PersonInfoItemDetailDto> getAllItemDetail(
			@PathParam("personInfoCategoryAuthId") String personInfoCategoryAuthId) {
		return personInfoItemAuthFinder.getAllItemDetail(personInfoCategoryAuthId);

	}

}

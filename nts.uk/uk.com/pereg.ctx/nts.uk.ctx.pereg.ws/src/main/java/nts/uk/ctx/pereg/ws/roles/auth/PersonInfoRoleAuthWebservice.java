package nts.uk.ctx.pereg.ws.roles.auth;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pereg.app.command.roles.auth.SavePersonInfoRoleAuthCommand;
import nts.uk.ctx.pereg.app.command.roles.auth.SavePersonInfoRoleAuthCommandHandler;
import nts.uk.ctx.pereg.app.command.roles.auth.UpdatePersonInfoRoleAuthCommand;
import nts.uk.ctx.pereg.app.command.roles.auth.UpdatePersonInfoRoleAuthCommandHandler;
import nts.uk.ctx.pereg.app.find.roles.auth.PersonInfoRoleAuthDto;
import nts.uk.ctx.pereg.app.find.roles.auth.PersonInfoRoleAuthFinder;
import nts.uk.shr.com.context.AppContexts;

@Path("ctx/pereg/roles/auth")
@Produces("application/json")
public class PersonInfoRoleAuthWebservice extends WebService {
	@Inject
	PersonInfoRoleAuthFinder personInfoRoleAuthFinder;

	@Inject
	UpdatePersonInfoRoleAuthCommandHandler update;

	@Inject
	SavePersonInfoRoleAuthCommandHandler save;

	@POST
	@Path("findAll")
	public List<PersonInfoRoleAuthDto> getAllPersonInfoRoleAuth() {
		return personInfoRoleAuthFinder.getAllPersonInfoRoleAuth();

	}

	@POST
	@Path("find/{roleId}")
	public PersonInfoRoleAuthDto getDetailPersonRoleAuth(@PathParam("roleId") String roleId) {
		return personInfoRoleAuthFinder.getDetailPersonRoleAuth(roleId)
				.orElse(new PersonInfoRoleAuthDto("", 0, 0, 0, 0, 0, 0));

	}

	@POST
	@Path("getSelfAuth")
	public PersonInfoRoleAuthDto getSelfAuth() {

		String roleId = AppContexts.user().roles().forCompanyAdmin();
		return personInfoRoleAuthFinder.getDetailPersonRoleAuth(roleId)
				.orElse(new PersonInfoRoleAuthDto("", 0, 0, 0, 0, 0, 0));

	}

	@POST
	@Path("update")
	public void update(UpdatePersonInfoRoleAuthCommand command) {
		this.update.handle(command);
	}

	@POST
	@Path("save")
	public void save(SavePersonInfoRoleAuthCommand command) {
		this.save.handle(command);
	}

}

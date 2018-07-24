package nts.uk.ctx.sys.auth.ws.registration.user;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.sys.auth.app.command.registration.user.AddRegistrationUserCommand;
import nts.uk.ctx.sys.auth.app.command.registration.user.AddRegistrationUserCommandHandler;
import nts.uk.ctx.sys.auth.app.command.registration.user.DeleteRegistrationUserCommand;
import nts.uk.ctx.sys.auth.app.command.registration.user.DeleteRegistrationUserCommandHandler;
import nts.uk.ctx.sys.auth.app.find.registration.user.CompanyImportDto;
import nts.uk.ctx.sys.auth.app.find.registration.user.RegistrationUserFinder;
import nts.uk.ctx.sys.auth.app.find.registration.user.UserDto;

@Path("ctx/sys/auth/regis/user")
@Produces("application/json")
public class RegistrationUserWS extends WebService {
	
	@Inject
	private RegistrationUserFinder registrationUserFinder;
	
	@Inject
	private AddRegistrationUserCommandHandler saveRegistrationUserCommandHandler;
	
	@Inject
	private DeleteRegistrationUserCommandHandler deleteRegistrationUserCommandHandler;
	
	@POST
	@Path("getAllCom")
	public List<CompanyImportDto> findCompanyImportList() {
		return this.registrationUserFinder.getCompanyImportList();
	}
	
	@POST
	@Path("getlistUser")
	public List<UserDto> getListUser(String cid) {
		return this.registrationUserFinder.getLoginUserListByCurrentCID(cid);
	}
	
	@POST
	@Path("getAllUser")
	public List<UserDto> getAllUser() {
		return this.registrationUserFinder.getLoginUserListByContractCode();
	}
	
	@POST
	@Path("findUser")
	public UserDto findUserByUserId(String userId) {
		return this.registrationUserFinder.getUserByUserId(userId);
	}
	
	@POST
	@Path("register")
	public String registerUser(AddRegistrationUserCommand command) {
		return this.saveRegistrationUserCommandHandler.handle(command);
	}
	
	@POST
	@Path("delete")
	public String delete(DeleteRegistrationUserCommand command) {
		return this.deleteRegistrationUserCommandHandler.handle(command);
	}

}

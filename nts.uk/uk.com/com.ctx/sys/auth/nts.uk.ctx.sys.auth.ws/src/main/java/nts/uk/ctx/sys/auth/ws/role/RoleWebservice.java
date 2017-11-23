package nts.uk.ctx.sys.auth.ws.role;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.enums.EnumAdaptor;
import nts.arc.enums.EnumConstant;
import nts.arc.layer.ws.WebService;
import nts.uk.ctx.sys.auth.app.command.person.role.RemovePersonRoleCommand;
import nts.uk.ctx.sys.auth.app.command.person.role.RemovePersonRoleCommandHandler;
import nts.uk.ctx.sys.auth.app.command.person.role.SavePersonRoleCommand;
import nts.uk.ctx.sys.auth.app.command.person.role.SavePersonRoleCommandHandler;
import nts.uk.ctx.sys.auth.app.find.person.role.PersonInformationRole;
import nts.uk.ctx.sys.auth.app.find.person.role.PersonInformationRoleFinder;
import nts.uk.ctx.sys.auth.app.find.person.role.dto.RoleDto;
import nts.uk.ctx.sys.auth.dom.role.EmployeeReferenceRange;
import nts.uk.ctx.sys.auth.dom.role.personrole.PersonRole;
import nts.uk.shr.infra.i18n.resource.I18NResourcesForUK;

@Path("ctx/sys/auth/role")
@Produces("application/json")
public class RoleWebservice extends WebService {
	@Inject
	private PersonInformationRoleFinder personInforRoleFinder;
	@Inject
	private SavePersonRoleCommandHandler savePersonRoleHandler;
	@Inject
	private RemovePersonRoleCommandHandler removePersonRoleHandler; 
	@Inject
	private I18NResourcesForUK i18n;
	
	@POST
	@Path("getlistrolebytype/{roleType}")
	public List<RoleDto> getListRoleByRoleType(@PathParam("roleType") int roleType) {
		return this.personInforRoleFinder.getListRoleByRoleType(roleType);
	}

	@POST
	@Path("save/person/infor")
	public void savePersonInfo(SavePersonRoleCommand command){
		savePersonRoleHandler.handle(command);
	}
	
	@POST
	@Path("remove/person/infor")
	public void removePersonInfo(RemovePersonRoleCommand command){
		removePersonRoleHandler.handle(command);
	}
	
	@POST
	@Path("find/person/infor")
	public List<PersonInformationRole> find(){
		return personInforRoleFinder.find();
	}
	@POST
	@Path("get/enum/reference/range")
	public List<EnumConstant> getEnumReferenceRange(){
		return EnumAdaptor.convertToValueNameList(EmployeeReferenceRange.class, i18n);
	}
	
	@POST
	@Path("find/person/role")
	public List<PersonRole> find(List<String> roleIds){
		return personInforRoleFinder.findByListRoleIds(roleIds);
	}
}

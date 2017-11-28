package nts.uk.ctx.sys.auth.ws.roleset;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.app.command.JavaTypeResult;
import nts.arc.layer.ws.WebService;
import nts.uk.ctx.sys.auth.app.command.roleset.AddOrUpdateDefaultRoleSetCommandHandler;
import nts.uk.ctx.sys.auth.app.command.roleset.AddRoleSetCommandHandler;
import nts.uk.ctx.sys.auth.app.command.roleset.DefaultRoleSetCommand;
import nts.uk.ctx.sys.auth.app.command.roleset.DeleteRoleSetCommand;
import nts.uk.ctx.sys.auth.app.command.roleset.DeleteRoleSetCommandHandler;
import nts.uk.ctx.sys.auth.app.command.roleset.RoleSetCommand;
import nts.uk.ctx.sys.auth.app.command.roleset.UpdateRoleSetCommandHandler;
import nts.uk.ctx.sys.auth.app.find.roleset.DefaultRoleSetDto;
import nts.uk.ctx.sys.auth.app.find.roleset.DefaultRoleSetFinder;
import nts.uk.ctx.sys.auth.app.find.roleset.RoleSetDto;
import nts.uk.ctx.sys.auth.app.find.roleset.RoleSetFinder;
import nts.uk.ctx.sys.auth.dom.roleset.webmenu.WebMenuImport;
import nts.uk.ctx.sys.auth.dom.roleset.webmenu.WebMenuAdapter;
import nts.uk.ctx.sys.auth.dom.roleset.webmenu.webmenulinking.RoleSetLinkWebMenuImport;
import nts.uk.ctx.sys.auth.dom.roleset.webmenu.webmenulinking.RoleSetLinkWebMenuAdapter;
import nts.uk.shr.com.context.AppContexts;


@Path("ctx/sys/auth/roleset")
@Produces("application/json")
public class RoleSetWebservice extends WebService {
	@Inject
	private RoleSetFinder roleSetFinder;

	@Inject
	private AddRoleSetCommandHandler addRoleSetCommandHandler;

	@Inject
	private UpdateRoleSetCommandHandler updateRoleSetCommandHandler;

	@Inject
	private DeleteRoleSetCommandHandler deleteRoleSetCommandHandler;

	// Default RoleSet:
	@Inject
	private DefaultRoleSetFinder defaultRoleSetFinder;
	
	@Inject
	private AddOrUpdateDefaultRoleSetCommandHandler addOrUpdateDefaultRoleSetCommandHandler;

	// Web menu
	@Inject
	private WebMenuAdapter webMenuAdapter;
	
	// Role Set and Web menu link
	
	@Inject
	private RoleSetLinkWebMenuAdapter roleSetLinkWebMenuAdapter;
	
	@POST
	@Path("findallroleset")
	public List<RoleSetDto> getAllRolSet() {
		return this.roleSetFinder.findAll();
	}

	@POST
	@Path("findroleset/{rolesetcd}")
	public RoleSetDto getRoleSet(@PathParam("rolesetcd") String roleSetCd) {
		return this.roleSetFinder.find(roleSetCd);
	}

	@POST
	@Path("addroleset")
	public JavaTypeResult<String> addRoleSet(RoleSetCommand command) {
		return new JavaTypeResult<String>(this.addRoleSetCommandHandler.handle(command));
	}

	@POST
	@Path("updateroleset")
	public void updateRoleSet(RoleSetCommand command) {
		this.updateRoleSetCommandHandler.handle(command);
	}

	@POST
	@Path("deleteroleset")
	public void removeSelectionItem(DeleteRoleSetCommand command) {
		this.deleteRoleSetCommandHandler.handle(command);
	}

	// Default Role Set
	@POST
	@Path("finddefaultroleset")
	public DefaultRoleSetDto getCurrentDefaultRoleSet() {
		return this.defaultRoleSetFinder.findByCompanyId();
	}
	
	@POST
	@Path("adddefaultroleset")
	public JavaTypeResult<String> addDefaultRoleSet(DefaultRoleSetCommand command) {
		return new JavaTypeResult<String>(this.addOrUpdateDefaultRoleSetCommandHandler.handle(command));
	}
	
	// Web menu
	@POST
	@Path("findallwebmenu")
	public List<WebMenuImport> getAllWebMenu() {
		return this.webMenuAdapter.findByCompanyId();
	}
	
	// Role Set link Web menu
	@POST
	@Path("findallrolesetwebmenu/{rolesetcd}")
	public List<RoleSetLinkWebMenuImport> getAllRoleSetWebMenu(@PathParam("roleSetCd") String roleSetCd) {
		return this.roleSetLinkWebMenuAdapter.findAllWebMenuByRoleSetCd(roleSetCd);
	}
	
	// Get companyId of the login user
	@POST
	@Path("companyidofloginuser")
	public JavaTypeResult<String> getCompanyIdOfLoginUser() {
		return new JavaTypeResult<String> (AppContexts.user().companyId());
	}
	
	
}

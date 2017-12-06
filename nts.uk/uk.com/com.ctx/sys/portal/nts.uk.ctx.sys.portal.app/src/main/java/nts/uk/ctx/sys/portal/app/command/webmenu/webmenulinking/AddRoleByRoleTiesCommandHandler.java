package nts.uk.ctx.sys.portal.app.command.webmenu.webmenulinking;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.sys.portal.dom.webmenu.WebMenuCode;
import nts.uk.ctx.sys.portal.dom.webmenu.webmenulinking.RoleByRoleTies;
import nts.uk.ctx.sys.portal.dom.webmenu.webmenulinking.RoleByRoleTiesRepository;

@Stateless
public class AddRoleByRoleTiesCommandHandler extends CommandHandler<RoleByRoleTiesCommand> {

	@Inject
	private RoleByRoleTiesRepository repo;
	
	@Override
	protected void handle(CommandHandlerContext<RoleByRoleTiesCommand> context) {
		RoleByRoleTiesCommand role = context.getCommand();
		
		RoleByRoleTies newRole = new RoleByRoleTies(new WebMenuCode( role.getWebMenuCd()),role.getRoleId());
		Optional<RoleByRoleTies> checkData = repo.getRoleByRoleTiesById(newRole.getRoleId());
		
		if(checkData.isPresent()) {
			throw new BusinessException("Msg_3");
		} else {
			repo.insertRoleByRoleTies(newRole);
		}
		
		
	}

}

/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.ws.singlesignon;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.sys.gateway.app.command.singlesignon.RemoveWindowAccountCommand;
import nts.uk.ctx.sys.gateway.app.command.singlesignon.RemoveWindowAccountCommandHandler;
import nts.uk.ctx.sys.gateway.app.command.singlesignon.SaveWindowAccountCommand;
import nts.uk.ctx.sys.gateway.app.command.singlesignon.SaveWindowAccountCommandHandler;
import nts.uk.ctx.sys.gateway.app.find.singlesignon.UserDto;
import nts.uk.ctx.sys.gateway.app.find.singlesignon.UserInfo;
import nts.uk.ctx.sys.gateway.app.find.singlesignon.UserInfoFinder;
import nts.uk.ctx.sys.gateway.app.find.singlesignon.WindowAccountFinder;
import nts.uk.ctx.sys.gateway.app.find.singlesignon.WindownAccountFinderDto;

/**
 * The Class WindowAccountWs.
 */
@Path("ctx/sys/gateway/single/signon")
@Produces("application/json")
@Stateless
public class WindowAccountWs extends WebService{
	
	/** The user finder. */
	@Inject
	private UserInfoFinder userFinder;
	
	/** The window account finder. */
	@Inject
	private WindowAccountFinder windowAccountFinder;
	
	/** The save window account command handler. */
	@Inject
	private SaveWindowAccountCommandHandler saveWindowAccountCommandHandler;
	
	/** The remove window account command handler. */
	@Inject
	private RemoveWindowAccountCommandHandler removeWindowAccountCommandHandler;
	
	/**
	 * Find list user info.
	 *
	 * @param baseDate the base date
	 * @return the list
	 */
	@POST
	@Path("find/userInfo")
	public List<UserDto> findListUserInfo(UserInfo object) {
		return this.userFinder.findListUserInfo(object.getBaseDate(),object.getIsScreenC());
	}
	
	
	/**
	 * Find by user id and use atr.
	 *
	 * @param userId the user id
	 * @return the list
	 */
	@POST
	@Path("find/window/account")
	public List<WindownAccountFinderDto> findListWindowAccByUserIdAndUseAtr(WindownAccountFinderDto windownAccountFinderDto) {
		return this.windowAccountFinder.findWindowAccountByUserId(windownAccountFinderDto.getUserId());
	}
	
	/**
	 * Save window account.
	 *
	 * @param listCommand the list command
	 */
	@Path("save/windowAcc")
	@POST
	public void saveWindowAccount(SaveWindowAccountCommand saveWindowAccountCommand) {
		this.saveWindowAccountCommandHandler.handle(saveWindowAccountCommand);
	}
	
	/**
	 * Removes the window account.
	 *
	 * @param removeCommand the remove command
	 */
	@Path("remove/windowAcc")
	@POST
	public void removeWindowAccount(RemoveWindowAccountCommand removeCommand) {
		removeWindowAccountCommandHandler.handle(removeCommand);
	}		
	
}

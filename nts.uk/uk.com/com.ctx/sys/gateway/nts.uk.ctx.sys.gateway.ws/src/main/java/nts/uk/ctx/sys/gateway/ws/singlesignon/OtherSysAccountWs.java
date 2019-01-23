/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.ws.singlesignon;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.sys.gateway.app.command.singlesignon.RemoveOtherSysAccountCommand;
import nts.uk.ctx.sys.gateway.app.command.singlesignon.RemoveOtherSysAccountCommandHandler;
import nts.uk.ctx.sys.gateway.app.command.singlesignon.SaveOtherSysAccountCommand;
import nts.uk.ctx.sys.gateway.app.command.singlesignon.SaveOtherSysAccountCommandHandler;
import nts.uk.ctx.sys.gateway.app.find.singlesignon.OtherSysAccFinder;
import nts.uk.ctx.sys.gateway.app.find.singlesignon.OtherSysAccFinderDto;

/**
 * The Class OtherSysAccountWs.
 */
@Path("ctx/sys/gateway/single/signon")
@Produces("application/json")
public class OtherSysAccountWs extends WebService{

	/** The save other sys account command handler. */
	@Inject
	private SaveOtherSysAccountCommandHandler saveOtherSysAccountCommandHandler;
	
	
	/** The remove other sys account command handler. */
	@Inject
	private RemoveOtherSysAccountCommandHandler removeOtherSysAccountCommandHandler;
	
	/** The other sys acc finder. */
	@Inject
	private OtherSysAccFinder otherSysAccFinder;
	
	/**
	 * Save other sys account.
	 *
	 * @param command the command
	 */
	@Path("save/otherSysAcc")
	@POST
	public void saveOtherSysAccount(SaveOtherSysAccountCommand command) {
		this.saveOtherSysAccountCommandHandler.handle(command);
	}
	
	/**
	 * Removes the other sys account.
	 *
	 * @param command the command
	 */
	@Path("remove/otherSysAcc")
	@POST
	public void removeOtherSysAccount(RemoveOtherSysAccountCommand command) {
		this.removeOtherSysAccountCommandHandler.handle(command);
	}
	
	/**
	 * Find other sys acc by user ID.
	 *
	 * @param otherSysAcc the other sys acc
	 * @return the other sys acc finder dto
	 */
	@Path("find/otherSysAcc")
	@POST
	public OtherSysAccFinderDto findOtherSysAccByUserID(OtherSysAccFinderDto otherSysAcc) {
		return this.otherSysAccFinder.findByEmployeeId(otherSysAcc.getEmployeeId());
	}

	/**
	 * Find already setting.
	 *
	 * @param userIds the user ids
	 * @return the list
	 */
	@POST
	@Path("find/otheracc/alreadysetting")
	public List<String> findAlreadySetting(List<String> userIds) {
		return this.otherSysAccFinder.findAlreadySetting(userIds);
	}
	
}

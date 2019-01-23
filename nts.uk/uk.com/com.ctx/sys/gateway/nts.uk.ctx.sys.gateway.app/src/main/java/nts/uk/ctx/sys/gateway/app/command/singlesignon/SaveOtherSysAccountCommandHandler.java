/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.app.command.singlesignon;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import nts.arc.error.BundledBusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.sys.gateway.dom.singlesignon.OtherSysAccount;
import nts.uk.ctx.sys.gateway.dom.singlesignon.OtherSysAccountRepository;
import nts.uk.ctx.sys.gateway.dom.singlesignon.UseAtr;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class SaveOtherSysAccountCommandHandler.
 */
@Stateless
public class SaveOtherSysAccountCommandHandler extends CommandHandler<SaveOtherSysAccountCommand> {
	
	/** The other sys account repository. */
	@Inject
	private OtherSysAccountRepository otherSysAccountRepository;

	/* (non-Javadoc)
	 * @see nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command.CommandHandlerContext)
	 */
	@Override
	protected void handle(CommandHandlerContext<SaveOtherSysAccountCommand> context) {
		String cid = AppContexts.user().companyId();
		// Get command
		SaveOtherSysAccountCommand command = context.getCommand();
		command.setCompanyId(cid);
		Optional<OtherSysAccount> opOtherSysAcc = otherSysAccountRepository.findByEmployeeId(cid,command.getEmployeeId());

		if (opOtherSysAcc.isPresent()) {
			this.validate(command);				
			// update
			this.otherSysAccountRepository.update(new OtherSysAccount(command) , opOtherSysAcc.get());
		}else{
			this.validate(command);	
		// save domain
		this.otherSysAccountRepository.add(new OtherSysAccount(command));
		}
	}
			
	/**
	 * Validate.
	 *
	 * @param dto the dto
	 */
	private void validate(SaveOtherSysAccountCommand dto) {
		if (dto.getUseAtr() == UseAtr.NotUse) {
			return;
		}
		
		// check error domain
		boolean isError = false;
		BundledBusinessException exceptions = BundledBusinessException.newInstance();

		// check only company code and user name
		if (!StringUtils.isEmpty(dto.getCompanyCode().v()) && !StringUtils.isEmpty(dto.getUserName().v())) {
			List<OtherSysAccount> opOtherSysAccount = otherSysAccountRepository
					.findByCompanyCodeAndUserName(dto.getCompanyCode().v(), dto.getUserName().v()).stream()
					.filter(item -> !item.getEmployeeId().equals(dto.getEmployeeId())&&item.getAccountInfo().getUseAtr().equals(UseAtr.Use)).collect(Collectors.toList());

			// Check condition
			if (!opOtherSysAccount.isEmpty() && !opOtherSysAccount.get(0).getEmployeeId().equals(dto.getEmployeeId())) {
				// Has error, throws message
				isError = true;
				exceptions.addMessage("Msg_616");
			}

			if (isError) {
				// show error list
				exceptions.throwExceptions();
			}
		}
	}
	
}

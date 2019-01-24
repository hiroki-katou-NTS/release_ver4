/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.app.command.singlesignon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import nts.arc.error.BundledBusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.sys.gateway.dom.singlesignon.UseAtr;
import nts.uk.ctx.sys.gateway.dom.singlesignon.WindowsAccount;
import nts.uk.ctx.sys.gateway.dom.singlesignon.WindowsAccountGetMemento;
import nts.uk.ctx.sys.gateway.dom.singlesignon.WindowsAccountInfo;
import nts.uk.ctx.sys.gateway.dom.singlesignon.WindowsAccountRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class SaveWindowAccountCommandHandler.
 */
@Stateless
public class SaveWindowAccountCommandHandler extends CommandHandler<SaveWindowAccountCommand> {

	/** The window account repository. */
	@Inject
	private WindowsAccountRepository windowAccountRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command
	 * .CommandHandlerContext)
	 */
	@Override
	protected void handle(CommandHandlerContext<SaveWindowAccountCommand> context) {
		String companyId = AppContexts.user().companyId();
		// Get command
		SaveWindowAccountCommand command = context.getCommand();

			List<WindowAccountDto> listWinAccDto = new ArrayList<>();
						
			if (command.getWinAcc1() != null) {
					this.validate(command.getWinAcc1());
				
				listWinAccDto.add(command.getWinAcc1());
			}
			if (command.getWinAcc2() != null) {
					this.validate(command.getWinAcc2());

				listWinAccDto.add(command.getWinAcc2());
			}
			if (command.getWinAcc3() != null) {
					this.validate(command.getWinAcc3());

				listWinAccDto.add(command.getWinAcc3());
			}
			if (command.getWinAcc4() != null) {
					this.validate(command.getWinAcc4());

				listWinAccDto.add(command.getWinAcc4());
			}
			if (command.getWinAcc5() != null) {
					this.validate(command.getWinAcc5());

				listWinAccDto.add(command.getWinAcc5());
			}
			
			// remove old domain
			Optional<WindowsAccount> optWindowAcc = windowAccountRepository.findByEmployeeId(companyId,command.getEmployeeId());
		
			// add and update data to db
			this.save(companyId,optWindowAcc,listWinAccDto );

		}
	
	private void save(String cid,Optional<WindowsAccount> optWindowAccDB, List<WindowAccountDto> listWinAccDto) {
		WindowsAccount windAccCommand = this.toWindowsAccountDomain(cid,listWinAccDto);
		windAccCommand.validate();
		
		Map<Integer, WindowsAccountInfo> mapWinAcc = new HashMap<Integer, WindowsAccountInfo>();
		
		if(optWindowAccDB.isPresent()) {
			mapWinAcc = optWindowAccDB.get().getAccountInfos().stream()
					.collect(Collectors.toMap(WindowsAccountInfo::getNo, Function.identity()));
		}
		
		List<Integer> lstWinAccSaved = new ArrayList<>();
		
		for (WindowsAccountInfo domain : windAccCommand.getAccountInfos()) {
			
			lstWinAccSaved.add(domain.getNo());
			
			WindowsAccountInfo winAccDb = mapWinAcc.get(domain.getNo());
			
			// not existed, insert DB
			if (winAccDb == null) {
				this.windowAccountRepository.add(windAccCommand.getCompanyId(), windAccCommand.getEmployeeId(), domain);
			} else {
				this.windowAccountRepository.update(windAccCommand.getCompanyId(), windAccCommand.getEmployeeId(), domain, winAccDb);
			}
		}
		
		// remove item not setting
		if(optWindowAccDB.isPresent()) {
			optWindowAccDB.get().getAccountInfos().stream().filter(
					domain -> domain.getNo() != null && !lstWinAccSaved.contains(domain.getNo()))
					.forEach(domain -> {
						this.windowAccountRepository.remove(windAccCommand.getCompanyId(),windAccCommand.getEmployeeId(), domain.getNo());
					});
		}
	}
	
	/**
	 * Validate.
	 *
	 * @param dto
	 *            the dto
	 */
	private void validate(WindowAccountDto dto) {
		if (dto.getUseAtr() == UseAtr.NotUse) {
			return;
		}
		// check error domain
		boolean isError = false;
		BundledBusinessException exceptions = BundledBusinessException.newInstance();

		if (!StringUtils.isEmpty(dto.getHostName().v()) && !StringUtils.isEmpty(dto.getUserName().v())) {
			Optional<WindowsAccount> opWindowAccount = windowAccountRepository
					.findbyUserNameAndHostNameAndIsUsed(dto.getUserName().v(), dto.getHostName().v());

			// Check condition
			if (opWindowAccount.isPresent() && !opWindowAccount.get().getEmployeeId().equals(dto.getEmployeeId())) {
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
		
	/**
	 * To domain.
	 *
	 * @return the total condition
	 */
	public WindowsAccount toWindowsAccountDomain(String cid, List<WindowAccountDto> windowAccountDtos) {
		return new WindowsAccount(new WindowsAccountDtoGetMemento(cid,windowAccountDtos));
	}

	/**
	 * The Class DtoGetMemento.
	 */
	private class WindowsAccountDtoGetMemento implements WindowsAccountGetMemento {

		/** The command. */
		private List<WindowAccountDto> windowAccountDtos;
		
		/** The cid. */
		private String cid;

		/**
		 * Instantiates a new dto get memento.
		 *
		 * @param command
		 *            the command
		 */
		public WindowsAccountDtoGetMemento(String cid, List<WindowAccountDto> windowAccountDtos) {
			this.cid = cid;
			this.windowAccountDtos = windowAccountDtos;
		}


		@Override
		public String getEmployeeId() {
			if (CollectionUtil.isEmpty(this.windowAccountDtos)) {
				return null;
			}
			return this.windowAccountDtos.stream().findFirst().get().getEmployeeId();
		}

		@Override
		public List<WindowsAccountInfo> getAccountInfos() {
			// Check empty
			if (CollectionUtil.isEmpty(this.windowAccountDtos)) {
				return Collections.emptyList();
			}
			
			return this.windowAccountDtos.stream().map(item -> new WindowsAccountInfo(item))
					.collect(Collectors.toList());
		}


		@Override
		public String getCompanyId() {
			return this.cid;
		}

	}
}

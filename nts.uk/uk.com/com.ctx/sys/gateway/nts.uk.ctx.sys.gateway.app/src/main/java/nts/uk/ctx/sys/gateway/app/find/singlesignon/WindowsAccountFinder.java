/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.app.find.singlesignon;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.sys.gateway.dom.singlesignon.WindowsAccount;
import nts.uk.ctx.sys.gateway.dom.singlesignon.WindowsAccountRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class WindowAccountFinder.
 */
@Stateless
public class WindowsAccountFinder {

	/** The window account repository. */
	@Inject
	private WindowsAccountRepository windowAccountRepository;

	/**
	 * Find window account by employee id.
	 *
	 * @param employeeId the employee id
	 * @return the list
	 */
	public List<WindowsAccountFinderDto> findWindowAccountByEmployeeId(String employeeId) {

		String companyId = AppContexts.user().companyId();
		Optional<WindowsAccount> optWindowAccount = this.windowAccountRepository
				.findListWindowAccountByEmployeeId(companyId,employeeId);

		// Check exist
		if (optWindowAccount.isPresent()) {
			WindowsAccount windowsAccount = optWindowAccount.get();
			return windowsAccount.getAccountInfos().stream()
					.map(accInfo -> new WindowsAccountFinderDto(employeeId, accInfo.getHostName().v(),
							accInfo.getUserName().v(), accInfo.getNo(), accInfo.getUseAtr().value))
					.collect(Collectors.toList());
		}

		// Return
		return Collections.emptyList();

	}

	/**
	 * Find already setting.
	 *
	 * @param userIds the user ids
	 * @return the list
	 */
	public List<String> findAlreadySetting(List<String> employeeIds) {
		return windowAccountRepository.findByListEmployeeId(employeeIds).stream().map(WindowsAccount::getEmployeeId)
				.collect(Collectors.toList());
	}

}

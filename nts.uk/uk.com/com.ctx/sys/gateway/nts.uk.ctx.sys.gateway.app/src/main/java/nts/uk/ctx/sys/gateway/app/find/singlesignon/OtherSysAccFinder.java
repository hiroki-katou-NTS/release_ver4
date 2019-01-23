/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.app.find.singlesignon;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.sys.gateway.dom.singlesignon.OtherSysAccount;
import nts.uk.ctx.sys.gateway.dom.singlesignon.OtherSysAccountRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class OtherSysAccFinder.
 */
@Stateless
public class OtherSysAccFinder {

	/** The other sys account repository. */
	@Inject
	private OtherSysAccountRepository otherSysAccountRepository;

	/**
	 * Find by user id.
	 *
	 * @param userId
	 *            the user id
	 * @return the other sys acc finder dto
	 */
	public OtherSysAccFinderDto findByEmployeeId(String employeeId) {
		
		OtherSysAccFinderDto otherSysAccFinderDto = new OtherSysAccFinderDto();
		String cid = AppContexts.user().companyId();
		Optional<OtherSysAccount> opOtherSysAcc = otherSysAccountRepository.findByEmployeeId(cid,employeeId);
		if (opOtherSysAcc.isPresent()) {
			opOtherSysAcc.get().saveToMemento(otherSysAccFinderDto);
		}
		
		return otherSysAccFinderDto;
	}

	/**
	 * Find already setting.
	 *
	 * @param userIds the user ids
	 * @return the list
	 */
	public List<String> findAlreadySetting(List<String> userIds) {
		return otherSysAccountRepository.findAllOtherSysAccount(userIds).stream().map(OtherSysAccount::getEmployeeId)
				.collect(Collectors.toList());
	}
	
}

/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.wagetable.certification.find;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.app.wagetable.certification.find.dto.CertificationFindInDto;
import nts.uk.ctx.pr.core.dom.wagetable.certification.Certification;
import nts.uk.ctx.pr.core.dom.wagetable.certification.CertificationRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

/**
 * The Class CertificationFinder.
 */
@Stateless
public class CertificationFinder {

	/** The find. */
	@Inject
	private CertificationRepository repository;

	/**
	 * Find all.
	 *
	 * @return the list
	 */
	public List<CertificationFindInDto> findAll() {

		// get info login
		LoginUserContext loginUserContext = AppContexts.user();
		
		// call findAll None Group
		List<Certification> data = this.repository.findAllNoneOfGroup(loginUserContext.companyCode());

		// to data
		return data.stream().map(certification -> {
			CertificationFindInDto dto = new CertificationFindInDto();
			certification.saveToMemento(dto);
			return dto;
		}).collect(Collectors.toList());
	}

}

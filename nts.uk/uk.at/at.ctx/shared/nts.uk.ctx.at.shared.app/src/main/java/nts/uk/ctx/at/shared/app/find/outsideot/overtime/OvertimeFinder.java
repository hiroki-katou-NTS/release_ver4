package nts.uk.ctx.at.shared.app.find.outsideot.overtime;

/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.shared.app.find.outsideot.dto.OvertimeDto;
import nts.uk.ctx.at.shared.dom.outsideot.overtime.Overtime;
import nts.uk.ctx.at.shared.dom.outsideot.overtime.OvertimeRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

/**
 * The Class OvertimeFinder.
 */
@Stateless
public class OvertimeFinder {
	
	/** The repository. */
	@Inject
	private OvertimeRepository repository;
	
	/**
	 * Find by id.
	 *
	 * @return the list
	 */
	public List<OvertimeDto> findAll() {

		// get login user
		LoginUserContext loginUserContext = AppContexts.user();

		// get company id
		String companyId = loginUserContext.companyId();

		// call repository find data
		List<Overtime> overtimes = this.repository.findAll(companyId);

		return overtimes.stream().map(domain -> {
			OvertimeDto dto = new OvertimeDto();
			domain.saveToMemento(dto);
			return dto;
		}).collect(Collectors.toList());

	}
	
	
}


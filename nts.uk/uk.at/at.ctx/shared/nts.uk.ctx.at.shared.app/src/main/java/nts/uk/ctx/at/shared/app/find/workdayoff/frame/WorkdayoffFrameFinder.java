/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.app.find.workdayoff.frame;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.shared.dom.workdayoff.frame.WorkdayoffFrame;
import nts.uk.ctx.at.shared.dom.workdayoff.frame.WorkdayoffFrameRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

/**
 * The Class WorkdayoffFrameFinder.
 */
@Stateless
public class WorkdayoffFrameFinder {
	
	/** The repository. */
	@Inject WorkdayoffFrameRepository repository;
	
	/**
	 * Find all.
	 *
	 * @return the list
	 */
	public List<WorkdayoffFrameFindDto> findAll(){
		
		// get login info
		LoginUserContext loginUserContext = AppContexts.user();
		
		// get company id
		String companyId = loginUserContext.companyId();

		// get all
		List<WorkdayoffFrame> allWorkdayoffFrame = this.repository
			.getAllWorkdayoffFrame(companyId);
		
		// to domain
		return allWorkdayoffFrame.stream().map(category -> {
			WorkdayoffFrameFindDto dto = new WorkdayoffFrameFindDto();
			category.saveToMemento(dto);
			return dto;
		}).collect(Collectors.toList());
	}
}

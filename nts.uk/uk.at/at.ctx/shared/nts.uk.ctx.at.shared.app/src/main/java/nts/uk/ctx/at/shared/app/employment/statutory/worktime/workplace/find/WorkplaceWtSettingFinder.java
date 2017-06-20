/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.app.employment.statutory.worktime.workplace.find;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.shared.dom.employment.statutory.worktime.workplace.WorkPlaceWtSetting;
import nts.uk.ctx.at.shared.dom.employment.statutory.worktime.workplace.WorkPlaceWtSettingRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class WorkplaceWtSettingFinder.
 */
@Stateless
public class WorkplaceWtSettingFinder {

	/** The repository. */
	@Inject
	private WorkPlaceWtSettingRepository repository;

	/** The company id. */
	String companyId = AppContexts.user().companyId();

	/**
	 * Find.
	 *
	 * @param request the request
	 * @return the workplace wt setting dto
	 */
	public WorkplaceWtSettingDto find(WorkplaceWtSettingRequest request) {
		Optional<WorkPlaceWtSetting> optWorkplaceWtSetting = this.repository.find(companyId, request.getYear(),
				request.getWorkplaceId());
		if (optWorkplaceWtSetting.isPresent()) {
			return WorkplaceWtSettingDto.fromDomain(optWorkplaceWtSetting.get());
		}
		// TODO ko co du lieu thi vao new mode.
		return null;
	}

	/**
	 * Find all.
	 *
	 * @param year the year
	 * @return the list
	 */
	public List<String> findAll(int year) {
		return this.repository.findAll(companyId, year);
	}
}

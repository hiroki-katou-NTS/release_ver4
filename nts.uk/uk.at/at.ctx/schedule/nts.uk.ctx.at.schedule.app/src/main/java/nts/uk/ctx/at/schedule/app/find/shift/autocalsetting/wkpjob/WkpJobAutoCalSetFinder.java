/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.app.find.shift.autocalsetting.wkpjob;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.schedule.dom.shift.autocalsetting.WkpJobAutoCalSetting;
import nts.uk.ctx.at.schedule.dom.shift.autocalsetting.WkpJobAutoCalSettingRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class WkpJobAutoCalSetFinder.
 */
@Stateless
public class WkpJobAutoCalSetFinder {

	/** The wkp job auto cal setting repository. */
	@Inject
	private WkpJobAutoCalSettingRepository wkpJobAutoCalSettingRepository;

	/**
	 * Gets the wkp job auto cal setting.
	 *
	 * @param wkpId the wkp id
	 * @param jobId the job id
	 * @return the wkp job auto cal setting
	 */
	public WkpJobAutoCalSettingDto getWkpJobAutoCalSetting(String wkpId, String jobId) {
		String companyId = AppContexts.user().companyId();

		Optional<WkpJobAutoCalSetting> opt = this.wkpJobAutoCalSettingRepository.getAllWkpJobAutoCalSetting(companyId,
				wkpId, jobId);

		if (!opt.isPresent()) {
			return null;
		}

		WkpJobAutoCalSettingDto dto = new WkpJobAutoCalSettingDto();

		opt.get().saveToMemento(dto);

		return dto;
	}

	/**
	 * Delete by code.
	 *
	 * @param wkpId the wkp id
	 * @param jobId the job id
	 */
	public void deleteByCode(String wkpId, String jobId) {
		String companyId = AppContexts.user().companyId();
		wkpJobAutoCalSettingRepository.delete(companyId, wkpId, jobId);
	}

}

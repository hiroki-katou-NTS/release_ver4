/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.app.find.shift.autocalsetting;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.schedule.dom.shift.autocalsetting.ComAutoCalSetting;
import nts.uk.ctx.at.schedule.dom.shift.autocalsetting.ComAutoCalSettingRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class ComAutoCalSetFinder.
 */
@Stateless
public class ComAutoCalSetFinder {

	/** The com auto cal setting repository. */
	@Inject
	private ComAutoCalSettingRepository comAutoCalSettingRepository;
	
	/**
	 * Gets the total times details.
	 *
	 * @param totalCountNo the total count no
	 * @return the total times details
	 */
	public ComAutoCalSettingDto getComAutoCalSetting() {
		String companyId = AppContexts.user().companyId();

		Optional<ComAutoCalSetting> optTotalTimes = this.comAutoCalSettingRepository.getAllComAutoCalSetting(companyId);

		if (!optTotalTimes.isPresent()) {
			return null;
		}

		ComAutoCalSettingDto dto = new ComAutoCalSettingDto();

		optTotalTimes.get().saveToMemento(dto);

		return dto;
	}
	
	
}

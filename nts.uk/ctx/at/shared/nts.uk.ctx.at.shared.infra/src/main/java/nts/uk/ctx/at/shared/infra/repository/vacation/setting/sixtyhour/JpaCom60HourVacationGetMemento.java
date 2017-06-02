/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.vacation.setting.sixtyhour;

import nts.uk.ctx.at.shared.dom.vacation.setting.sixtyhours.Com60HourVacationGetMemento;
import nts.uk.ctx.at.shared.dom.vacation.setting.sixtyhours.SixtyHourVacationSetting;
import nts.uk.ctx.at.shared.infra.entity.vacation.setting.sixtyhours.KshstCom60hVacation;
import nts.uk.ctx.at.shared.infra.entity.vacation.setting.sixtyhours.KshstSixtyHourVacationSetting;
import nts.uk.ctx.at.shared.infra.entity.vacation.setting.subst.KsvstComSubstVacation;

/**
 * The Class JpaComSubstVacationGetMemento.
 */
public class JpaCom60HourVacationGetMemento implements Com60HourVacationGetMemento {

	/** The type value. */
	private KshstCom60hVacation typeValue;

	/**
	 * Instantiates a new jpa com subst vacation get memento.
	 *
	 * @param typeValue
	 *            the type value
	 */
	public JpaCom60HourVacationGetMemento(KshstCom60hVacation typeValue) {
		this.typeValue = typeValue;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.vacation.setting.sixtyhours.Com60HourVacationGetMemento#getCompanyId()
	 */
	@Override
	public String getCompanyId() {
		return this.typeValue.getCid();
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.vacation.setting.sixtyhours.Com60HourVacationGetMemento#getSetting()
	 */
	@Override
	public SixtyHourVacationSetting getSetting() {
		return new SixtyHourVacationSetting(new Jpa60HourVacationSettingGetMemento<KshstSixtyHourVacationSetting>(this.typeValue));
	}

}

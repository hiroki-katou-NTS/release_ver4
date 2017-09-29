/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.infra.repository.automaticcalculation;

import nts.uk.ctx.at.schedule.dom.shift.autocalsetting.AutoCalAtrOvertime;
import nts.uk.ctx.at.schedule.dom.shift.autocalsetting.AutoCalOvertimeSettingGetMemento;
import nts.uk.ctx.at.schedule.dom.shift.autocalsetting.AutoCalSetting;
import nts.uk.ctx.at.schedule.dom.shift.autocalsetting.TimeLimitUpperLimitSetting;
import nts.uk.ctx.at.schedule.infra.entity.shift.autocalsetting.KshmtAutoCalSet;

/**
 * The Class JpaAutoCalOvertimeSettingGetMemento.
 */
public class JpaAutoCalOvertimeSettingGetMemento implements AutoCalOvertimeSettingGetMemento {

	/** The entity. */
	private KshmtAutoCalSet entity;

	/**
	 * Instantiates a new jpa auto cal overtime setting get memento.
	 *
	 * @param entity the entity
	 */
	public JpaAutoCalOvertimeSettingGetMemento(KshmtAutoCalSet entity) {
		this.entity = entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.autocalsetting.
	 * AutoCalOvertimeSettingGetMemento#getEarlyOtTime()
	 */
	@Override
	public AutoCalSetting getEarlyOtTime() {
		return new AutoCalSetting(TimeLimitUpperLimitSetting.valueOf(this.entity.getEarlyOtTimeLimit()),
				AutoCalAtrOvertime.valueOf(this.entity.getEarlyOtTimeAtr()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.autocalsetting.
	 * AutoCalOvertimeSettingGetMemento#getEarlyMidOtTime()
	 */
	@Override
	public AutoCalSetting getEarlyMidOtTime() {
		return new AutoCalSetting(TimeLimitUpperLimitSetting.valueOf(this.entity.getEarlyMidOtTimeLimit()),
				AutoCalAtrOvertime.valueOf(this.entity.getEarlyMidOtTimeAtr()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.autocalsetting.
	 * AutoCalOvertimeSettingGetMemento#getNormalOtTime()
	 */
	@Override
	public AutoCalSetting getNormalOtTime() {
		return new AutoCalSetting(TimeLimitUpperLimitSetting.valueOf(this.entity.getNormalOtTimeLimit()),
				AutoCalAtrOvertime.valueOf(this.entity.getNormalOtTimeAtr()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.autocalsetting.
	 * AutoCalOvertimeSettingGetMemento#getNormalMidOtTime()
	 */
	@Override
	public AutoCalSetting getNormalMidOtTime() {
		return new AutoCalSetting(TimeLimitUpperLimitSetting.valueOf(this.entity.getNormalMidOtTimeLimit()),
				AutoCalAtrOvertime.valueOf(this.entity.getNormalMidOtTimeAtr()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.autocalsetting.
	 * AutoCalOvertimeSettingGetMemento#getLegalOtTime()
	 */
	@Override
	public AutoCalSetting getLegalOtTime() {
		return new AutoCalSetting(TimeLimitUpperLimitSetting.valueOf(this.entity.getLegalOtTimeLimit()),
				AutoCalAtrOvertime.valueOf(this.entity.getLegalOtTimeAtr()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.autocalsetting.
	 * AutoCalOvertimeSettingGetMemento#getLegalMidOtTime()
	 */
	@Override
	public AutoCalSetting getLegalMidOtTime() {
		return new AutoCalSetting(TimeLimitUpperLimitSetting.valueOf(this.entity.getLegalMidOtTimeLimit()),
				AutoCalAtrOvertime.valueOf(this.entity.getLegalMidOtTimeAtr()));
	}

}

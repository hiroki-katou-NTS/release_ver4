/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.ot.autocalsetting.com;

import nts.uk.ctx.at.shared.dom.common.CompanyId;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalFlexOvertimeSetting;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalOvertimeSetting;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalRestTimeSetting;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.com.ComAutoCalSettingGetMemento;
import nts.uk.ctx.at.shared.infra.entity.ot.autocalsetting.com.KshmtAutoComCalSet;
import nts.uk.ctx.at.shared.infra.repository.ot.autocalsetting.JpaAutoCalFlexOvertimeSettingGetMemento;
import nts.uk.ctx.at.shared.infra.repository.ot.autocalsetting.JpaAutoCalOvertimeSettingGetMemento;
import nts.uk.ctx.at.shared.infra.repository.ot.autocalsetting.JpaAutoCalRestTimeSettingGetMemento;

/**
 * The Class JpaComAutoCalSettingGetMemento.
 */
public class JpaComAutoCalSettingGetMemento implements ComAutoCalSettingGetMemento {

	/** The entity. */
	private KshmtAutoComCalSet entity;

	/**
	 * Instantiates a new jpa com auto cal setting get memento.
	 *
	 * @param entity
	 *            the entity
	 */
	public JpaComAutoCalSettingGetMemento(KshmtAutoComCalSet entity) {
		this.entity = entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.autocalsetting.
	 * ComAutoCalSettingGetMemento#getCompanyId()
	 */
	@Override
	public CompanyId getCompanyId() {
		return new CompanyId(this.entity.getCid());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.autocalsetting.
	 * ComAutoCalSettingGetMemento#getNormalOTTime()
	 */
	@Override
	public AutoCalOvertimeSetting getNormalOTTime() {
		return new AutoCalOvertimeSetting(new JpaAutoCalOvertimeSettingGetMemento(this.entity));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.autocalsetting.
	 * ComAutoCalSettingGetMemento#getFlexOTTime()
	 */
	@Override
	public AutoCalFlexOvertimeSetting getFlexOTTime() {
		return new AutoCalFlexOvertimeSetting(new JpaAutoCalFlexOvertimeSettingGetMemento(this.entity));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.autocalsetting.
	 * ComAutoCalSettingGetMemento#getRestTime()
	 */
	@Override
	public AutoCalRestTimeSetting getRestTime() {
		return new AutoCalRestTimeSetting(new JpaAutoCalRestTimeSettingGetMemento(this.entity));
	}

}

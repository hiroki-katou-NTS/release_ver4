/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.ot.autocalsetting.job;

import nts.uk.ctx.at.shared.dom.common.CompanyId;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalFlexOvertimeSetting;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalOvertimeSetting;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalRestTimeSetting;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.PositionId;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.job.JobAutoCalSettingGetMemento;
import nts.uk.ctx.at.shared.infra.entity.ot.autocalsetting.job.KshmtAutoJobCalSet;
import nts.uk.ctx.at.shared.infra.repository.ot.autocalsetting.JpaAutoCalFlexOvertimeSettingGetMemento;
import nts.uk.ctx.at.shared.infra.repository.ot.autocalsetting.JpaAutoCalOvertimeSettingGetMemento;
import nts.uk.ctx.at.shared.infra.repository.ot.autocalsetting.JpaAutoCalRestTimeSettingGetMemento;

/**
 * The Class JpaJobAutoCalSettingGetMemento.
 */
public class JpaJobAutoCalSettingGetMemento implements JobAutoCalSettingGetMemento {

	/** The entity. */
	private KshmtAutoJobCalSet entity;

	/**
	 * Instantiates a new jpa job auto cal setting get memento.
	 *
	 * @param entity the entity
	 */
	public JpaJobAutoCalSettingGetMemento(KshmtAutoJobCalSet entity) {
		this.entity = entity;
	}
	
	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.schedule.dom.shift.autocalsetting.JobAutoCalSettingGetMemento#getCompanyId()
	 */
	@Override
	public CompanyId getCompanyId() {
		return new CompanyId(this.entity.getKshmtAutoJobCalSetPK().getCid());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.schedule.dom.shift.autocalsetting.JobAutoCalSettingGetMemento#getPositionId()
	 */
	@Override
	public PositionId getPositionId() {
		return new PositionId(this.entity.getKshmtAutoJobCalSetPK().getJobid());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.schedule.dom.shift.autocalsetting.JobAutoCalSettingGetMemento#getNormalOTTime()
	 */
	@Override
	public AutoCalOvertimeSetting getNormalOTTime() {
		return new AutoCalOvertimeSetting(new JpaAutoCalOvertimeSettingGetMemento(this.entity));
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.schedule.dom.shift.autocalsetting.WkpAutoCalSettingGetMemento#getFlexOTTime()
	 */
	@Override
	public AutoCalFlexOvertimeSetting getFlexOTTime() {
		return new AutoCalFlexOvertimeSetting(new JpaAutoCalFlexOvertimeSettingGetMemento(this.entity));
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.schedule.dom.shift.autocalsetting.WkpAutoCalSettingGetMemento#getRestTime()
	 */
	@Override
	public AutoCalRestTimeSetting getRestTime() {
		return new AutoCalRestTimeSetting(new JpaAutoCalRestTimeSettingGetMemento(this.entity));
	}

}

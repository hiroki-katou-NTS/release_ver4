/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.worktime.common;

import nts.uk.ctx.at.shared.dom.worktime.common.BooleanGetAtr;
import nts.uk.ctx.at.shared.dom.worktime.flowset.ScheduleBreakCalculationGetMemento;
import nts.uk.ctx.at.shared.infra.entity.worktime.flexset.KshmtWtFleBrFl;
import nts.uk.ctx.at.shared.infra.entity.worktime.flowset.KshmtWtFloBrFlAll;
import nts.uk.shr.infra.data.entity.ContractUkJpaEntity;

/**
 * The Class JpaScheduleBreakCalculationGetMemento.
 *
 * @param <T>
 *            the generic type
 */
public class JpaScheduleBreakCalculationGetMemento<T extends ContractUkJpaEntity>
		implements ScheduleBreakCalculationGetMemento {

	/** The entity. */
	private T entity;

	/**
	 * Instantiates a new jpa schedule break calculation get memento.
	 *
	 * @param entity
	 *            the entity
	 */
	public JpaScheduleBreakCalculationGetMemento(T entity) {
		this.entity = entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.flowset.
	 * ScheduleBreakCalculationGetMemento#getIsReferRestTime()
	 */
	@Override
	public boolean getIsReferRestTime() {
		if (this.entity instanceof KshmtWtFloBrFlAll) {
			return BooleanGetAtr.getAtrByInteger(((KshmtWtFloBrFlAll) this.entity).getIsReferRestTime());
		}
		if (this.entity instanceof KshmtWtFleBrFl) {
			return BooleanGetAtr.getAtrByInteger(((KshmtWtFleBrFl) this.entity).getIsReferRestTime());
		}
		throw new IllegalStateException("entity type is not valid");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.flowset.
	 * ScheduleBreakCalculationGetMemento#getIsCalcFromSchedule()
	 */
	@Override
	public boolean getIsCalcFromSchedule() {
		if (this.entity instanceof KshmtWtFloBrFlAll) {
			return BooleanGetAtr.getAtrByInteger(((KshmtWtFloBrFlAll) this.entity).getIsCalcFromSchedule());
		}
		if (this.entity instanceof KshmtWtFleBrFl) {
			return BooleanGetAtr.getAtrByInteger(((KshmtWtFleBrFl) this.entity).getIsCalcFromSchedule());
		}
		throw new IllegalStateException("entity type is not valid");
	}

}

/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.worktime.common;

import nts.uk.ctx.at.shared.dom.worktime.common.CalcMethodExceededPredAddVacation;
import nts.uk.ctx.at.shared.dom.worktime.common.OTFrameNo;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.ExceededPredAddVacationCalcSetMemento;
import nts.uk.ctx.at.shared.infra.entity.worktime.difftimeset.KshmtWtDif;
import nts.uk.ctx.at.shared.infra.entity.worktime.fixedset.KshmtWtFix;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class JpaExceededPredAddVacationCalcSetMemento.
 */
public class JpaExceededPredAddVacationCalcSetMemento<T extends UkJpaEntity>
		implements ExceededPredAddVacationCalcSetMemento {

	/** The entity. */
	private T entity;

	/**
	 * Instantiates a new jpa exceeded pred add vacation calc set memento.
	 *
	 * @param entity
	 *            the entity
	 */
	public JpaExceededPredAddVacationCalcSetMemento(T entity) {
		super();
		this.entity = entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.fixedset.
	 * ExceededPredAddVacationCalcSetMemento#setCalcMethod(nts.uk.ctx.at.shared.
	 * dom.worktime.common.CalcMethodExceededPredAddVacation)
	 */
	@Override
	public void setCalcMethod(CalcMethodExceededPredAddVacation calcMethod) {
		if (this.entity instanceof KshmtWtFix) {
			if (calcMethod != null) {
				((KshmtWtFix) this.entity).setExceededPredCalcMethod(calcMethod.value);
			}
		}
		if (this.entity instanceof KshmtWtDif) {
			if (calcMethod != null) {
				((KshmtWtDif) this.entity).setExceededPredCalcMethod(calcMethod.value);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.fixedset.
	 * ExceededPredAddVacationCalcSetMemento#setOtFrameNo(nts.uk.ctx.at.shared.
	 * dom.workrule.outsideworktime.overtime.overtimeframe.OverTimeFrameNo)
	 */
	@Override
	public void setOtFrameNo(OTFrameNo otFrameNo) {
		if (this.entity instanceof KshmtWtFix) {
			if (otFrameNo != null) {
				((KshmtWtFix) this.entity).setExceededPredOtFrameNo(otFrameNo.v());
			}
		}
		if (this.entity instanceof KshmtWtDif) {
			if (otFrameNo != null) {
				((KshmtWtDif) this.entity).setExceededPredOtFrameNo(otFrameNo.v());
			}
		}
	}
}

/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.infra.repository.budget.external.actualresult;

import nts.uk.ctx.at.schedule.dom.budget.external.actualresult.ExtBudgTimeZoneValSetMemento;
import nts.uk.ctx.at.schedule.dom.budget.external.actualresult.ExternalBudgetVal;
import nts.uk.ctx.at.schedule.infra.entity.budget.external.actualresult.KscdtExtBudgetTimeVal;

/**
 * The Class JpaExtBudgTimeZoneValSetMemento.
 *
 * @param <T>
 *            the generic type
 */
public class JpaExtBudgTimeZoneValSetMemento<T> implements ExtBudgTimeZoneValSetMemento<T> {

    /** The entity. */
    private KscdtExtBudgetTimeVal entity;

    /**
     * Instantiates a new jpa ext budg time zone val set memento.
     *
     * @param entity
     *            the entity
     */
    public JpaExtBudgTimeZoneValSetMemento(KscdtExtBudgetTimeVal entity) {
        this.entity = entity;
    }

    /*
     * (non-Javadoc)
     * 
     * @see nts.uk.ctx.at.schedule.dom.budget.external.actualresult.
     * ExtBudgTimeZoneValSetMemento#setTimePeriod(int)
     */
    @Override
    public void setTimePeriod(int timePeriod) {
        this.entity.setPeriodTimeNo(timePeriod);
    }

    /*
     * (non-Javadoc)
     * 
     * @see nts.uk.ctx.at.schedule.dom.budget.external.actualresult.
     * ExtBudgTimeZoneValSetMemento#setActualValue(nts.uk.ctx.at.schedule.dom.
     * budget.external.actualresult.ExternalBudgetVal)
     */
    @Override
    public void setActualValue(ExternalBudgetVal<T> actualValue) {
        this.entity.setActualVal(actualValue.getRawValue());
    }

}

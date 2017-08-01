/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.infra.repository.budget.external.actualresult;

import nts.uk.ctx.at.schedule.dom.budget.external.ExtBudgetWorkplaceCode;
import nts.uk.ctx.at.schedule.dom.budget.external.actualresult.ExtBudgetAccDate;
import nts.uk.ctx.at.schedule.dom.budget.external.actualresult.ExtBudgetActualValue;
import nts.uk.ctx.at.schedule.dom.budget.external.actualresult.ExtBudgetErrorContent;
import nts.uk.ctx.at.schedule.dom.budget.external.actualresult.ExternalBudgetErrorSetMemento;
import nts.uk.ctx.at.schedule.infra.entity.budget.external.actualresult.KscdtExtBudgetError;

/**
 * The Class JpaExternalBudgetErrorSetMemento.
 */
public class JpaExternalBudgetErrorSetMemento implements ExternalBudgetErrorSetMemento {

    /** The entity. */
    private KscdtExtBudgetError entity;

    /**
     * Instantiates a new jpa external budget error set memento.
     *
     * @param entity
     *            the entity
     */
    public JpaExternalBudgetErrorSetMemento(KscdtExtBudgetError entity) {
        this.entity = entity;
    }

    /**
     * Sets the error content.
     *
     * @param errorContent
     *            the new error content
     */
    @Override
    public void setErrorContent(ExtBudgetErrorContent errorContent) {
        this.entity.setErrContent(errorContent.v());
    }

    /**
     * Sets the number column.
     *
     * @param numberColumn
     *            the new number column
     */
    @Override
    public void setNumberColumn(int numberColumn) {
        this.entity.getKscdtExtBudgetErrorPK().setColumnNo(numberColumn);
    }

    /**
     * Sets the actual value.
     *
     * @param actualValue
     *            the new actual value
     */
    @Override
    public void setActualValue(ExtBudgetActualValue actualValue) {
        this.entity.setAcceptedVal(actualValue.v());
    }

    /**
     * Sets the accepted date.
     *
     * @param acceptedDate
     *            the new accepted date
     */
    @Override
    public void setAcceptedDate(ExtBudgetAccDate acceptedDate) {
        this.entity.setAcceptedD(acceptedDate.v());
    }

    /**
     * Sets the work place code.
     *
     * @param workPlaceCode
     *            the new work place code
     */
    @Override
    public void setWorkPlaceCode(ExtBudgetWorkplaceCode workPlaceCode) {
        this.entity.setAcceptedWkpcd(workPlaceCode.v());
    }

    /**
     * Sets the execution id.
     *
     * @param executionId
     *            the new execution id
     */
    @Override
    public void setExecutionId(String executionId) {
        this.entity.getKscdtExtBudgetErrorPK().setExeId(executionId);
    }

    /**
     * Sets the number line.
     *
     * @param numberLine
     *            the new number line
     */
    @Override
    public void setNumberLine(int numberLine) {
        this.entity.getKscdtExtBudgetErrorPK().setLineNo(numberLine);
    }

}

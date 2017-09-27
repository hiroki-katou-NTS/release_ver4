/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.infra.repository.workplace;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import nts.uk.ctx.bs.employee.dom.workplace.Period;
import nts.uk.ctx.bs.employee.dom.workplace.WorkplaceHistory;
import nts.uk.ctx.bs.employee.dom.workplace.WorkplaceId;
import nts.uk.ctx.bs.employee.dom.workplace.WorkplaceSetMemento;
import nts.uk.ctx.bs.employee.infra.entity.workplace.BsymtWorkplaceHist;
import nts.uk.ctx.bs.employee.infra.entity.workplace.BsymtWorkplaceHistPK;

/**
 * The Class JpaWorkplaceSetMemento.
 */
public class JpaWorkplaceSetMemento implements WorkplaceSetMemento {

    /** The lst entity. */
    private List<BsymtWorkplaceHist> lstEntity;

    /**
     * Instantiates a new jpa workplace set memento.
     *
     * @param lstEntity
     *            the lst entity
     */
    public JpaWorkplaceSetMemento(List<BsymtWorkplaceHist> lstEntity) {
        this.beforeInitial(lstEntity);
        this.lstEntity = lstEntity;
    }

    /**
     * Before initial.
     *
     * @param lstEntity
     *            the lst entity
     */
    private void beforeInitial(List<BsymtWorkplaceHist> lstEntity) {
        lstEntity.forEach(entity -> {
            if (entity != null && entity.getBsymtWorkplaceHistPK() == null) {
                entity.setBsymtWorkplaceHistPK(new BsymtWorkplaceHistPK());
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * nts.uk.ctx.bs.employee.dom.workplace.WorkplaceSetMemento#setCompanyId(
     * java.lang.String)
     */
    @Override
    public void setCompanyId(String companyId) {
        this.lstEntity.forEach(entity -> {
            entity.getBsymtWorkplaceHistPK().setCid(companyId);
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * nts.uk.ctx.bs.employee.dom.workplace.WorkplaceSetMemento#setWorkplaceId(
     * nts.uk.ctx.bs.employee.dom.workplace.WorkplaceId)
     */
    @Override
    public void setWorkplaceId(WorkplaceId workplaceId) {
        this.lstEntity.forEach(entity -> {
            entity.getBsymtWorkplaceHistPK().setWkpid(workplaceId.v());
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see nts.uk.ctx.bs.employee.dom.workplace.WorkplaceSetMemento#
     * setWorkplaceHistory(java.util.List)
     */
    @Override
    public void setWorkplaceHistory(List<WorkplaceHistory> lstWkpHistory) {
        // convert list workplace history to map by key historyId
        Map<String, Period> mapWkpHist = lstWkpHistory.stream().collect(Collectors.toMap(
                item -> ((WorkplaceHistory) item).getHistoryId().v(), item -> ((WorkplaceHistory) item).getPeriod()));

        // set period
        this.lstEntity.forEach(entity -> {
            Period period = mapWkpHist.get(entity.getBsymtWorkplaceHistPK().getHistoryId());
            entity.setStrD(period.getStartDate());
            entity.setEndD(period.getEndDate());
        });
    }

}

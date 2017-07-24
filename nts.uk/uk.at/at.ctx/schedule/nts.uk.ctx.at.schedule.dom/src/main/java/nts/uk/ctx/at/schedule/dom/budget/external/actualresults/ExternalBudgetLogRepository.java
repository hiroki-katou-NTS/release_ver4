/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.dom.budget.external.actualresults;

/**
 * The Interface ExternalBudgetLogRepository.
 */
public interface ExternalBudgetLogRepository {
    
    /**
     * Adds the.
     *
     * @param domain the domain
     */
    void add(ExternalBudgetLog domain);
    
    /**
     * Update.
     *
     * @param domain the domain
     */
    void update(ExternalBudgetLog domain);
}

package nts.uk.ctx.at.schedule.dom.budget.external;

import java.util.Date;
import java.util.List;

/**
 * The Interface ExtBudgTimeZoneGetMemento.
 *
 * @param <T> the generic type
 */
public interface ExtBudgTimeZoneGetMemento<T> {

    /**
     * Gets the company id.
     *
     * @return the company id
     */
    String getCompanyId();

    /**
     * Gets the actual values.
     *
     * @param <T> the generic type
     * @return the actual values
     */
    List<ExternalBudgetTimeZoneVal<T>> getActualValues();

    /**
     * Gets the ext budget code.
     *
     * @return the ext budget code
     */
    ExternalBudgetCd getExtBudgetCode();

    /**
     * Gets the process date.
     *
     * @return the process date
     */
    Date getProcessDate();

    /**
     * Gets the workplace id.
     *
     * @return the workplace id
     */
    String getWorkplaceId();
}

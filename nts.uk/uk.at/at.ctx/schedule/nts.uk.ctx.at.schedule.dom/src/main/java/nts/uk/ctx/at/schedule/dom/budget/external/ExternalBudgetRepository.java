package nts.uk.ctx.at.schedule.dom.budget.external;

import java.util.List;
import java.util.Optional;

public interface ExternalBudgetRepository {
	/*
	 * get all Item of external budget
	 */
	List<ExternalBudget> findAll(String companyId);

	/*
	 * get Item of external budget by Budget Code
	 */
	Optional<ExternalBudget> find(String companyId, String externalBudgetCode);

	/*
	 * Insert
	 */
	void insert(ExternalBudget externalBudgetResult);

	/*
	 * Update
	 */
	void update(ExternalBudget externalBudgetResult);

	/*
	 * Delete
	 */
	void delete(String companyId, String externalBudgetCode);
}

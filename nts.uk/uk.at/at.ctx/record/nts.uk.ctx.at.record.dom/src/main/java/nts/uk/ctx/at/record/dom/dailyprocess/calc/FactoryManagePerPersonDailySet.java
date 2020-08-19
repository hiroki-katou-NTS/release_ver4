package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.util.Optional;

import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;

/**
 * @author kazuki_watanabe
 *
 */
public interface FactoryManagePerPersonDailySet {
	Optional<ManagePerPersonDailySet> create(String companyId, ManagePerCompanySet companySetting, IntegrationOfDaily daily, WorkingConditionItem conditionItem);
}

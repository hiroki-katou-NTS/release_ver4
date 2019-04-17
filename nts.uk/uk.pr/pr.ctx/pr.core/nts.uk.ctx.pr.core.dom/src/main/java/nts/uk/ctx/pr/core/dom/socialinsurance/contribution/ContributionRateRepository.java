package nts.uk.ctx.pr.core.dom.socialinsurance.contribution;

import nts.uk.shr.com.history.YearMonthHistoryItem;

import java.util.List;
import java.util.Optional;

public interface ContributionRateRepository {

	Optional<ContributionRateHistory> getContributionRateHistoryByOfficeCode(String officeCode);

	Optional<ContributionRate> getContributionRateByHistoryId(String historyId);

	Optional<ContributionRate> getContributionRateByDate(String cid, String officeCode, int yearMonth);

	void deleteByHistoryIds(List<String> historyIds,String officeCode);

	void add(ContributionRate domain, String officeCode, YearMonthHistoryItem yearMonth);

	void update(ContributionRate domain, String officeCode, YearMonthHistoryItem yearMonth);

	void updateContributionByGrade(ContributionRate domain);
	
	void insertContributionByGrade(ContributionRate domain);

	void deleteContributionByGradeByHistoryId(List<String> historyIds);

	void updateHistoryItem (String officeCode, YearMonthHistoryItem yearMonth);
}

package nts.uk.ctx.at.record.dom.dailyperformanceformat.repository;

import java.util.List;
import java.util.Optional;

import nts.uk.ctx.at.record.dom.dailyperformanceformat.BusinessTypeSorted;

public interface BusinessFormatSortedRepository {
	
	void add(List<BusinessTypeSorted> businessTypeSorteds);
	
	void update(BusinessTypeSorted businessTypeSorted);
	
	Optional<BusinessTypeSorted> find(String companyId, int attendanceItemId);
	
	List<BusinessTypeSorted> findAll(String companyId);

}

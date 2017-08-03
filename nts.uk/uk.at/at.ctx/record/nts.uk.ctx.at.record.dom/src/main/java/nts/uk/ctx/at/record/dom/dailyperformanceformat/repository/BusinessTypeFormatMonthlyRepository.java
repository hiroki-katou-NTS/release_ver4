package nts.uk.ctx.at.record.dom.dailyperformanceformat.repository;

import java.math.BigDecimal;
import java.util.List;

import nts.uk.ctx.at.record.dom.dailyperformanceformat.BusinessTypeFormatMonthly;

public interface BusinessTypeFormatMonthlyRepository {
	
	List<BusinessTypeFormatMonthly> getMonthlyDetail(String companyId, String workTypeCode);
	
	void update(BusinessTypeFormatMonthly businessTypeFormatMonthly);
	
	void deleteExistData(List<BigDecimal> attendanceItemIds);
	
	void add(List<BusinessTypeFormatMonthly> businessTypeFormatMonthlyAdds);

}

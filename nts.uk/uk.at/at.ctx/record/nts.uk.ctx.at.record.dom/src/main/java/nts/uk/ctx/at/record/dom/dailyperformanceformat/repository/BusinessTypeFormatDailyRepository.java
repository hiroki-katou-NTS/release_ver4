package nts.uk.ctx.at.record.dom.dailyperformanceformat.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import nts.uk.ctx.at.record.dom.dailyperformanceformat.BusinessTypeFormatDaily;

public interface BusinessTypeFormatDailyRepository {
	
	List<BusinessTypeFormatDaily> getBusinessTypeFormat(String companyId, String businessTypeCode);
	
	List<BusinessTypeFormatDaily> getBusinessTypeFormatDailyDetail(String companyId, String businessTypeCode, BigDecimal sheetNo);

	void deleteExistData(List<Integer> attendanceItemIds);
	
	void update(BusinessTypeFormatDaily businessTypeFormatDaily);
	
	void add(List<BusinessTypeFormatDaily> businessTypeFormatDailies);
	
	// Set header width
	void updateColumnsWidth(Map<Integer, Integer> lstHeader);
}

package nts.uk.ctx.at.function.dom.monthlycorrection.fixedformatmonthly;

import java.util.List;
import java.util.Optional;

public interface MonthlyRecordWorkTypeRepository {
	List<MonthlyRecordWorkType> getAllMonthlyRecordWorkType(String companyID);
	
	Optional<MonthlyRecordWorkType> getMonthlyRecordWorkTypeByCode(String companyID,String businessTypeCode);
	
	void addMonthlyRecordWorkType(MonthlyRecordWorkType monthlyRecordWorkType);
	
	void updateMonthlyRecordWorkType(MonthlyRecordWorkType monthlyRecordWorkType);
}

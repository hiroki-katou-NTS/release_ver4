package nts.uk.shr.find.employment.processing.yearmonth;

import java.util.List;

public interface IPaydayProcessingFinder {
	List<PaydayProcessingDto> getPaydayProcessing(String companyCode); 
	
	List<PaydayProcessingDto> getPaydayProcessing();
}

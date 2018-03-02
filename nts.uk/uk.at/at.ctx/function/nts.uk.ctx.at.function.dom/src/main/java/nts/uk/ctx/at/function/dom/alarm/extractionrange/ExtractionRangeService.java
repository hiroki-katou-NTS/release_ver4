package nts.uk.ctx.at.function.dom.alarm.extractionrange;

import java.util.List;

public interface ExtractionRangeService {
	
	/**
	 * @param alarmCode
	 * @param companyId
	 * @param closureId
	 * @param processingYm
	 * 
	 * @return period by category 
	 */
	List<CheckConditionTimeDto> getPeriodByCategory(String alarmCode,String companyId, int closureId, Integer processingYm );
	
}

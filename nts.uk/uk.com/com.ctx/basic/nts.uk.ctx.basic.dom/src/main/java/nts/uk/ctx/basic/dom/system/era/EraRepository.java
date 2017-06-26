package nts.uk.ctx.basic.dom.system.era;


import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;

public interface EraRepository {
	/**
	 * 
	 * @param eraName
	 * @return
	 */
	List<Era>getEras();
	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	Optional<Era>getEraDetail(String eraHist);
	
	/**
	 * get the latest era which is has end_date = 9999/12/31
	 * @return
	 */
	Optional<Era> getLatestEra();
	/**
	 * 
	 * @param era
	 */
	List<Era> getStartDateEraMaster(GeneralDate startDate);
	
	Optional<Era> getHistIdUpdate(String eraHist);
	Optional<Era> getEndDateBefore(GeneralDate endDate);
	
	void add(Era era);
	/**
	 * 
	 * @param era
	 */
	void update(Era era);
	/**
	 * 
	 * @param era
	 */
	void delete(String eraHist);
	Optional<Era> getCurrentEndDate(GeneralDate endDate);
		
	
	GeneralDate checkStartDate();
}

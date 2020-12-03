package nts.uk.ctx.at.record.dom.divergence.time;

import java.util.List;
import java.util.Optional;

/**
 * The Interface DivergenceReasonInputMethodRepository.
 */
public interface DivergenceReasonInputMethodRepository {

	/**
	 * get all divergence time.
	 *
	 * @param companyId the company id
	 * @return the all div time
	 */
	List<DivergenceReasonInputMethod> getAllDivTime(String companyId);

	/**
	 * Gets the div time info.
	 *
	 * @param companyId the company id
	 * @param divTimeNo the div time no
	 * @return the div time info
	 */
	Optional<DivergenceReasonInputMethod> getDivTimeInfo(String companyId, int divTimeNo);

	/**
	 * Update.
	 *
	 * @param domain the domain
	 */
	void update(DivergenceReasonInputMethod domain);
	
	List<DivergenceReasonInputMethod> getByCidAndLstTimeInfo(String companyId, List<Integer> divTimeNos, int useClassification); 
}

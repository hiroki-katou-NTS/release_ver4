package nts.uk.ctx.at.record.dom.divergencetime;

import java.util.List;
import java.util.Optional;

public interface DivergenceTimeRepository {

	/**
	 * get all divergence time
	 * @param companyId
	 * @return
	 */
	List<DivergenceTime> getAllDivTime(String companyId);
	/**
	 * get all divergence reason
	 * @param companyId
	 * @param divTimeId
	 * @return
	 */
	List<DivergenceReason> getDivReasonByCode(String companyId, int divTimeId);
	/**
	 * get all attendance item
	 * @param companyId
	 * @param divTimeId
	 * @return
	 */
	List<DivergenceItemSet> getallItembyCode(String companyId, int divTimeId);
	/**
	 * update divergence time
	 * @param divTime
	 */
	void updateDivTime(DivergenceTime divTime);
	/**
	 * add divergence reason
	 * @param divReason
	 */
	void addDivReason(DivergenceReason divReason);
	/**
	 * update divergence reason
	 * @param divReason
	 */
	void updateDivReason(DivergenceReason divReason);
	/**
	 * delete divergence reason
	 * @param companyId
	 * @param divTimeId
	 * @param divReasonCode
	 */
	void deleteDivReason(String companyId, int divTimeId, String divReasonCode);
	/**
	 * get divergence time
	 * @param companyId
	 * @param divTimeId
	 * @param divReasonCode
	 * @return
	 */
	Optional<DivergenceReason> getDivReason(String companyId, int divTimeId, String divReasonCode);
	/**
	 * add list item id new
	 * @param lstItemId
	 */
	void addItemId(List<DivergenceItemSet> lstItemId);
	/**
	 * delete list item id old
	 * @param companyId
	 * @param divTimeId
	 */
	void deleteItemId(String companyId, int divTimeId);
}

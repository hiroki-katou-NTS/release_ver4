/**
 * 
 */
package nts.uk.ctx.bs.employee.dom.classification.affiliate;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * @author danpv
 * @author hop.nt
 *
 */
public interface AffClassHistoryRepository {
	
	/**
	 * return historyDomain with one period
	 * @param historyId
	 * @return
	 */
	Optional<DateHistoryItem> getByHistoryId(String historyId);
	
	/**
	 * get with employeeId and standardDate
	 * @param employeeId
	 * @param standardDate
	 * @return
	 */
	Optional<DateHistoryItem> getByEmpIdAndStandardDate(String employeeId, GeneralDate standardDate);
	
	/**
	 * return historyDomain with periods
	 * @param employeeId
	 * @return
	 */
	Optional<AffClassHistory> getByEmployeeId(String cid, String employeeId);
	
	/**
	 * return historyDomain with periods descending
	 * @param cid
	 * @param employeeId
	 * @return
	 */
	Optional<AffClassHistory> getByEmployeeIdDesc(String cid, String employeeId);
	
	/**
	 * request-list 398
	 * @param employeeIds
	 * @param period
	 * @return
	 */
	List<AffClassHistory> getByEmployeeListWithPeriod(List<String> employeeIds, DatePeriod period);
	
	/**
	 * add domain history
	 * Merge BSYMT_AFF_CLASS_HISTORY To BSYMT_AFF_CLASS_HIS_ITEM  because response
	 * new Insert Method ↓
	 *        ClassName  : JpaAffClassHistoryRepository
	 *        MethodName : addToMerge
	 */
//	void add(String cid, String sid, DateHistoryItem itemToBeAdded);
	void addToMerge(String cid, String sid, DateHistoryItem itemToBeAdded, String classificationCode);
	
	/**
	 * update domain history
	 * @param history
	 * @author hop.nt
	 */
	void update(DateHistoryItem item);
	
	/**
	 * delete domain history
	 * @param histId
	 * @author hop.nt
	 */
	void delete(String histId);

}

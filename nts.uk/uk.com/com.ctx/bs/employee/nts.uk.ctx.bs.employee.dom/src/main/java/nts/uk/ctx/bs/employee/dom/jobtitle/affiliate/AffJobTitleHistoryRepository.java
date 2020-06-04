package nts.uk.ctx.bs.employee.dom.jobtitle.affiliate;

import java.util.List;
import java.util.Optional;

import lombok.Value;
import nts.arc.time.GeneralDate;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface AffJobTitleHistoryRepository {
	
	/**
	 * get with primary key
	 * @param historyId
	 * @return
	 */
	Optional<AffJobTitleHistory> getByHistoryId(String historyId);
	
	/**
	 * get with employeeId and standard date
	 * @param employeeId
	 * @param standardDate
	 * @return
	 */
	Optional<AffJobTitleHistory> getByEmpIdAndStandardDate(String employeeId, GeneralDate standardDate);
	
	// get listbypid
	Optional<AffJobTitleHistory> getListBySid(String cid, String sid);
	
	Optional<AffJobTitleHistory> getListBySidDesc(String cid, String sid);
	

	/**
	 * ドメインモッ�「�務�位」を新規登録する
	 * 
	 * Merge BSYMT_AFF_JOB_HIST To BSYMT_AFF_JOB_HIST_ITEM  because response
			new Insert Method ↓
	         Class      : here
	         MethodName : addToMerge
	 * 
	 * @param item
	 * @param sid
	 * @param cid
	 */
//	void add(String cid, String sid, DateHistoryItem item);
	void addToMerge(String cid, String sid, DateHistoryItem item, String jobTitleId, String note);

	/**
	 * 取得した「�務�位」を更新する
	 * 
	 * @param item
	 */
	void update(DateHistoryItem item);

	/**
	 * ドメインモッ�「�務�位」を削除する
	 * 
	 * @param histId
	 */
	void delete(String histId);
	
	List<AffJobTitleHistory> getAllBySid(String sid);
	
	Optional<AffJobTitleHistory> getListByHidSid(String hid, String sid);
	
	List<AffJobTitleHistory> getListByListHidSid(List<String> hid, List<String> sid);
	
	List<AffJobTitleHistory> getListByListHidSid(List<String> sid, GeneralDate targetDate);
	
	/**
	 * Search job title history.
	 *
	 * @param baseDate the base date
	 * @param employeeId the employee id
	 * @return the list
	 */
	List<AffJobTitleHistory> searchJobTitleHistory(GeneralDate baseDate, List<String> employeeId, List<String> jobTitleIds);
	
	 /**
 	 * Find all job title history.
 	 *
 	 * @param baseDate the base date
 	 * @param employeeIds the employee ids
 	 * @return the list
 	 */
 	List<AffJobTitleHistory> findAllJobTitleHistory(GeneralDate baseDate, List<String> employeeIds) ;
 	
 	/**
	 * request-list 398
	 * @param employeeIds
	 * @param period
	 * @return
	 */
	List<AffJobTitleHistory> getByEmployeeListPeriod(List<String> employeeIds, DatePeriod period);
	
	/**
	 * request list 515
	 * @param employeeIds
	 * @param period
	 * @return
	 * @author yennth
	 */
	Optional<AffJobTitleHistory> getListEmployee(GeneralDate baseDate, String cid);
	
	Optional<SingleHistoryItem> getSingleHistoryItem(String employeeId, GeneralDate baseDate);
	
	@Value
	public static class SingleHistoryItem {
		
		private final String employeeId;
		private final String historyId;
		private final DatePeriod period;
		private final String jobTitleId;
		private final String note;
	}
}

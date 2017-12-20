package nts.uk.ctx.bs.employee.dom.jobtitle.affiliate;

import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.shr.com.history.DateHistoryItem;

public interface AffJobTitleHistoryRepository_ver1 {
	
	/**
	 * get with primary key
	 * @param historyId
	 * @return
	 */
	Optional<AffJobTitleHistory_ver1> getByHistoryId(String historyId);
	
	/**
	 * get with employeeId and standard date
	 * @param employeeId
	 * @param standardDate
	 * @return
	 */
	Optional<AffJobTitleHistory_ver1> getByEmpIdAndStandardDate(String employeeId, GeneralDate standardDate);
	
	// get listbypid
	Optional<AffJobTitleHistory_ver1> getListBySid(String cid, String sid);

	/**
	 * ドメインモデル「職務職位」を新規登録する
	 * 
	 * @param item
	 * @param sid
	 * @param cid
	 */
	void add(String cid, String sid, DateHistoryItem item);

	/**
	 * 取得した「職務職位」を更新する
	 * 
	 * @param item
	 */
	void update(DateHistoryItem item);

	/**
	 * ドメインモデル「職務職位」を削除する
	 * 
	 * @param histId
	 */
	void delete(String histId);
}

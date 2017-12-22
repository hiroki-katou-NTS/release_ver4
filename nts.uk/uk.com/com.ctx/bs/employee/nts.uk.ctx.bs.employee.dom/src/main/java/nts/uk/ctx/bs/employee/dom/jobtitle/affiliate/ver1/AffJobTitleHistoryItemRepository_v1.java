package nts.uk.ctx.bs.employee.dom.jobtitle.affiliate.ver1;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;

public interface AffJobTitleHistoryItemRepository_v1 {
	
	/**
	 * @param employeeId
	 * @param referDate
	 * @return
	 */
	Optional<AffJobTitleHistoryItem> getByEmpIdAndReferDate(String employeeId, GeneralDate referDate);
	
	/**
	 * find with primary key
	 * @param historyId
	 * @return
	 */
	Optional<AffJobTitleHistoryItem> findByHitoryId(String historyId);
	
	/**
	 * ドメインモデル「職務職位」を新規登録する
	 * 
	 * @param domain
	 */
	void add(AffJobTitleHistoryItem domain);

	/**
	 * 取得した「職務職位」を更新する
	 * 
	 * @param domain
	 */
	void update(AffJobTitleHistoryItem domain);

	/**
	 * ドメインモデル「職務職位」を削除する
	 * 
	 * @param jobTitleMainId
	 */
	void delete(String jobTitleMainId);
	
	List<AffJobTitleHistoryItem> getByJobIdAndReferDate(String jobId, GeneralDate referDate);
	
	List<AffJobTitleHistoryItem> getAllBySid(String sid);
	
	List<AffJobTitleHistoryItem> getAllByListSidDate(List<String> lstSid, GeneralDate referDate);
}

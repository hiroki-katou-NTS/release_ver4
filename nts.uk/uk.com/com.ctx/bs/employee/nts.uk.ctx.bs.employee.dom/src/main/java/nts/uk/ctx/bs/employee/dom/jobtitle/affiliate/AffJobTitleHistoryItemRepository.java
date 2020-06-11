package nts.uk.ctx.bs.employee.dom.jobtitle.affiliate;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;

public interface AffJobTitleHistoryItemRepository {
	
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
//	 Merge BSYMT_AFF_JOB_HIST To BSYMT_AFF_JOB_HIST_ITEM  because response
//	 new Insert Method ↓
//	         ClassName  : JpaAffJobTitleHistoryRepository
//	         MethodName : addToMerge
	 */
//	void add(AffJobTitleHistoryItem domain);

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
	
	/**
	 * get by historyId list
	 * @param historyIds
	 * @return
	 */
	List<AffJobTitleHistoryItem> findByHitoryIds(List<String> historyIds);
	
	// request list 551
	List<AffJobTitleHistoryItem> findHistJob(String companyId, GeneralDate baseDate, List<String> jobIds);
}

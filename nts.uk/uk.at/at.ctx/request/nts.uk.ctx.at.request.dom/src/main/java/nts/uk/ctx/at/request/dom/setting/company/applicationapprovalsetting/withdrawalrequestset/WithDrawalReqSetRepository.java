package nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.withdrawalrequestset;

import java.util.Optional;

/**
 * TanLV
 *
 */
public interface WithDrawalReqSetRepository {
	/**
	 * Find
	 * @param companyId
	 * @return
	 */
	Optional<WithDrawalReqSet> getWithDrawalReqSet();
	
	/**
	 * Add
	 * @param withDrawalReqSet
	 */
	void addWithDrawalReqSet(WithDrawalReqSet withDrawalReqSet);
	
	/**
	 * Update
	 * @param withDrawalReqSet
	 */
	void updateWithDrawalReqSet(WithDrawalReqSet withDrawalReqSet);
	
	Optional<WorkUse> getDeferredWorkTimeSelect(String Cid);
}

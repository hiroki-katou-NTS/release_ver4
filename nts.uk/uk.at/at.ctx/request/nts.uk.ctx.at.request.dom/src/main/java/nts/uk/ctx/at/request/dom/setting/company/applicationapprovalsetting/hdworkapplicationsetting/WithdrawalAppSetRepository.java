package nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.hdworkapplicationsetting;

import java.util.Optional;

public interface WithdrawalAppSetRepository {
	/**
	 * get with drawal app set
	 * @return
	 * @author yennth
	 */
	Optional<WithdrawalAppSet> getWithDraw();
	/** 
	 * update with drawal app set
	 * @param with
	 * @author yennth
	 */
	void update(WithdrawalAppSet with);
	/**
	 * insert With drawal App Set
	 * @param with
	 * @author yennth
	 */
	void insert(WithdrawalAppSet with);
	
	Optional<WithdrawalAppSet> getByCid(String cid);
}

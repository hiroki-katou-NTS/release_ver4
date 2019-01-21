package nts.uk.ctx.sys.auth.pub.user;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.sys.auth.pub.user.UserInforEx;

public interface UserPublisher {

	Optional<UserDto> getUserInfo(String userId);
	
	/** Requestlist 222
	 * 契約コードとログインIDからユーザを取得する
	 */
	Optional<UserExport> getUserByContractAndLoginId(String contractCode, String loginId);
	
	/** Requestlist 220
	 * 紐付け先個人IDからユーザを取得する
	 */

	Optional<UserExport> getUserByAssociateId(String associatePersonId);
	
	List<UserExport> getListUserByListAsId(List<String> listAssociatePersonId);
	
	Optional<UserExport> getByUserId(String userId);
	
	Optional<UserExport> getBySid(String sid);
	/** Requestlist 313
	 * [No.313]社員IDからユーザを取得する
	 */
	Optional<UserInforEx> getByEmpID(String empID);
	
	Optional<UserExport> getByUserIDandDate(String userId , GeneralDate systemDate);
}

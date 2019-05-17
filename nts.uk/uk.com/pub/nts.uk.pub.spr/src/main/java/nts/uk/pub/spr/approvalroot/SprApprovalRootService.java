package nts.uk.pub.spr.approvalroot;

import java.util.List;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.pub.spr.export.PersonInfoSprExport;
import nts.uk.pub.spr.approvalroot.output.ApprovalRootSpr;

/**
 * 04 承認ルート
 * @author Doan Duy Hung
 *
 */
public interface SprApprovalRootService {
	
	/**
	 * 04 承認ルート
	 * @param employeeCD
	 * @param date
	 */
	public List<ApprovalRootSpr> getApprovalRoot(String employeeCD, String date);
	
	/**
	 * パラメータチェック
	 * @param employeeCD ログイン社員コード
	 * @param date 対象日
	 */
	public void checkParam(String employeeCD, String date);
	
	/**
	 * 承認すべき対象者
	 * @param companyID 会社ID
	 * @param approverID 承認社員ID
	 * @param date 対象日
	 */
	public List<ApprovalRootSpr> getApproverStatus(String companyID, String approverID, GeneralDate date);
	
	/**
	 * 申請済み検索
	 * @param companyID
	 * @param employeeID
	 * @param date
	 * @param rootType
	 * @return
	 */
	public List<PersonInfoSprExport> getAppRootStateIDByType(String companyID, String employeeID, GeneralDate date, Integer rootType);
	
}

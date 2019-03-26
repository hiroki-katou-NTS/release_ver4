package nts.uk.ctx.at.request.dom.application.common.service.newscreen.before;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.PrePostAtr;

/**
 * 
 * 2-1.新規画面登録前の処理
 *
 */
public interface NewBeforeRegister_New {
	/**
	 * 2-1.新規画面登録前の処理
	 * @param companyID
	 * @param employeeID
	 * @param startDate
	 * @param endDate
	 * @param postAtr
	 * @param routeAtr
	 * @param targetApp
	 * @param overTimeAtr, If APP_TYPE = 0 thì mới có overTime Atr, còn các loại đơn khác thì truyền 0 vào
	 */
	public void processBeforeRegister(Application_New application,int overTimeAtr, boolean checkOver1Year);
	
	/**
	 * 1.入社前退職チェック
	 * @param companyID 会社ID
	 * @param employeeID 社員ID（申請本人の社員ID）
	 * @param date 申請する開始日
	 */
	public void retirementCheckBeforeJoinCompany(String companyID, String employeeID, GeneralDate date);
	
	/**
	 * 2.申請の締め切り期限のチェック
	 * @param companyID
	 * @param appID
	 * @param appStartDate
	 * @param appEndDate
	 * @param startDate
	 * @param endDate
	 */
	public void deadlineApplicationCheck(String companyID, Integer closureID, String employeeID,
			GeneralDate deadlineStartDate, GeneralDate deadlineEndDate, GeneralDate appStartDate, GeneralDate appEndDate);
	
	/**
	 * 5.申請の受付制限をチェック
	 * @param postAtr 事前事後区分
	 * @param startDate 申請する開始日
	 * @param endDate 申請する終了日
	 */
	public void applicationAcceptanceRestrictionsCheck(String companyID, ApplicationType appType, PrePostAtr postAtr, GeneralDate startDate, GeneralDate endDate,int overTimeAtr);
	
	/**
	 * 3.確定チェック
	 * @param companyID 会社ID
	 * @param employeeID 社員ID（申請本人の社員ID）
	 * @param appDate 申請対象日
	 */
	public void confirmationCheck(String companyID, String employeeID, GeneralDate appDate);
	
	public void confirmCheckOvertime(String companyID, String employeeID, GeneralDate appDate);
}

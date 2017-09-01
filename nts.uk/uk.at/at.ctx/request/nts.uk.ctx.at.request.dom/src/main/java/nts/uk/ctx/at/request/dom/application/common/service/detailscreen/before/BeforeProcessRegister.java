package nts.uk.ctx.at.request.dom.application.common.service.detailscreen.before;

import nts.arc.time.GeneralDate;

/**
 * 
 * 4-1.詳細画面登録前の処理
 *
 */
public interface BeforeProcessRegister {
	/**
	 * 4-1.詳細画面登録前の処理
	 * @param companyID 会社ID 
	 * @param employeeID 社員ID（申請本人の社員ID）
	 * @param appDate 申請対象日
	 * @param employeeRouteAtr 就業ルート区分
	 * @param targetApp 対象申請
	 * @param postAtr 事前事後区分
	 */
	public void processBeforeDetailScreenRegistration(String companyID, String employeeID, GeneralDate appDate, int employeeRouteAtr, String targetApp, int postAtr);
	
	/**
	 * 1.排他チェック
	 */
	public void exclusiveCheck();
}

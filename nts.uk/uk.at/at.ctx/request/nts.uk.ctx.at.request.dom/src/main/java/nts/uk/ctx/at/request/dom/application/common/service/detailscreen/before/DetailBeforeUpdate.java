package nts.uk.ctx.at.request.dom.application.common.service.detailscreen.before;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.PrePostAtr;

/**
 * 
 * 4-1.詳細画面登録前の処理
 *
 */
public interface DetailBeforeUpdate {
	/**
	 * 4-1.詳細画面登録前の処理
	 * @param companyID
	 * @param employeeID
	 * @param appDate
	 * @param employeeRouteAtr
	 * @param appID
	 * @param postAtr
	 * @param version
	 * @param wkTypeCD
	 * @param wkTimeCd
	 */
	public void processBeforeDetailScreenRegistration(String companyID, String employeeID, GeneralDate appDate, int employeeRouteAtr, String appID, 
			PrePostAtr postAtr, Long version,String wkTypeCD, String wkTimeCd);
	
	/**
	 * 1.排他チェック
	 */
	public void exclusiveCheck(String companyID, String appID, Long version);
	/**
	 * 4-1.詳細画面登録前の処理 (CMM045)
	 * @param companyID 会社ID 
	 * @param employeeID 社員ID（申請本人の社員ID）
	 * @param appDate 申請対象日
	 * @param employeeRouteAtr 就業ルート区分
	 * @param targetApp 対象申請
	 * @param postAtr 事前事後区分
	 */
	public boolean processBefDetailScreenReg(String companyID, String employeeID, GeneralDate appDate, int employeeRouteAtr, String appID, 
			PrePostAtr postAtr, Long version);
	/**
	 * 1.排他チェック (CMM045)
	 * @param companyID
	 * @param appID
	 * @param version
	 * @return
	 */
	public boolean exclusiveCheckErr(String companyID, String appID, Long version);

	/**
	 * 勤務種類、就業時間帯チェックのメッセージを表示
	 * 
	 * @param companyID
	 * @param wkTypeCode
	 * @param wkTimeCode
	 */
	
	void displayWorkingHourCheck(String companyID, String wkTypeCode, String wkTimeCode);
}


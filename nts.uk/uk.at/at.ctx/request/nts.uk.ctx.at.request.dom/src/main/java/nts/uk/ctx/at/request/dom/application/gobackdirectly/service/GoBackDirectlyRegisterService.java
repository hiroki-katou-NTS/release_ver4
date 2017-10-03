package nts.uk.ctx.at.request.dom.application.gobackdirectly.service;

import nts.uk.ctx.at.request.dom.application.common.Application;
import nts.uk.ctx.at.request.dom.application.gobackdirectly.GoBackDirectly;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.GoBackDirectlyCommonSetting;

public interface GoBackDirectlyRegisterService {
	/**
	 * 直行直帰登録
	 * 
	 * @param employeeID
	 * @param application
	 * @param goBackDirectly
	 */
	public void register(GoBackDirectly goBackDirectly, Application application);

	/**
	 * アルゴリズム「直行直帰するチェック」を実行する
	 * 
	 * @param goBackDirectly
	 * @param goBackAtr
	 */
	public GoBackDirectAtr goBackDirectCheck(GoBackDirectly goBackDirectly);

	/**
	 * 直行直帰遅刻早退のチェック
	 * 
	 * @param goBackDirectly
	 * @return
	 */
	public GoBackDirectLateEarlyOuput goBackDirectLateEarlyCheck(GoBackDirectly goBackDirectly);

	/**
	 * Check Validity
	 * 
	 */
	public CheckValidOutput goBackLateEarlyCheckValidity(GoBackDirectly goBackDirectly,
			GoBackDirectlyCommonSetting goBackCommonSet, int line);
}

package nts.uk.ctx.at.request.dom.application.gobackdirectly.service;

import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.gobackdirectly.GoBackDirectly;

public interface GoBackDirectlyUpdateService {
	
	/**
	 * アルゴリズム「直行直帰更新前チェック」を実行する
	 * @return
	 */
	public void checkErrorBeforeUpdate(GoBackDirectly goBackDirectly, String companyID, String appID, Long version);
	/**
	 * アルゴリズム「直行直帰更新」を実行する
	 * @param goBackDirectly
	 */
	public void updateGoBackDirectly(GoBackDirectly goBackDirectly, Application_New application, Long version);
}

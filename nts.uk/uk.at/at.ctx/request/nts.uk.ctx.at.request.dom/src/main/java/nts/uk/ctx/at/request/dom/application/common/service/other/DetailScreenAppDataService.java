package nts.uk.ctx.at.request.dom.application.common.service.other;

import nts.uk.ctx.at.request.dom.application.common.service.other.output.DetailScreenAppData;

public interface DetailScreenAppDataService {
	
	/**
	 * 15.詳細画面申請データを取得する
	 * @param appID
	 * @return
	 */
	public DetailScreenAppData getDetailScreenAppData(String appID);
	
}

package nts.uk.ctx.at.request.dom.setting.request.application.workchange.service;

/**
 * Common work change service
 *
 */
public interface IWorkChangeCommonService {
	/**
	 * アルゴリズム「勤務変更申請基本データ（新規）」を実行する
	 * @param 
	 * compayId: 会社ID
	 * sId: 申請者ID
	 * @return
	 * ・勤務変更申請共通設定
	 * ・社員.社員名
	 */
	WorkChangeBasicData getSettingData(String compayId, String sId);
}

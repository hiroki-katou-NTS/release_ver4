package nts.uk.ctx.at.request.dom.application.common.service.mail;

import java.util.List;
import java.util.Map;

import nts.arc.time.GeneralDateTime;

/**
 * @author hiep.ld
 *
 */
public interface RegisterEmbededURL {
	// 
	/**
	 * アルゴリズム「申請メール埋込URL取得」を実行する
	 * @param appId
	 * @param appType
	 * @param prePostAtr
	 * @param employeeId
	 * @return embeddedUrl
	 */
	public String obtainApplicationEmbeddedUrl( String appId, int appType, int prePostAtr, List<String> employeeId);
	/**
	 * アルゴリズム「埋込URL情報登録申請」を実行する
	 * @param appId
	 * @param appType
	 * @param prePostAtr
	 * @param loginId
	 * @param employeeId
	 * @return Embed URL
	 */
	public String registerEmbeddedForApp(String appId, int appType, int prePostAtr, String loginId, List<String> employeeId);

	/**
	 * アルゴリズム「埋込URL情報申請画面ID取得」を実行する
	 * @param appType
	 * @param prePostAtr
	 * @return プログラムID, 遷移先ID
	 */
	public EmbeddedUrlScreenID getEmbeddedUrlRequestScreenID(int appType, int prePostAtr); 
	/**
	 * アルゴリズム「埋込URL情報登録」を実行する
	 * @param programId
	 * @param screenId
	 * @param periodCls
	 * @param numOfPeriod
	 * @param employeeId
	 * @param loginId
	 * @param taskIncidental
	 * @return 埋込用URL
	 */
	public String embeddedUrlInfoRegis(String programId, String screenId, int periodCls,
			int numOfPeriod, String employeeId, String loginId, Map<String, String> taskIncidental);
	// アルゴリズム「埋込URL有効期限取得」を実行する
	/**
	 * アルゴリズム「埋込URL有効期限取得」を実行する
	 * @param startDate
	 * @param periodCls
	 * @param numOfPeriod
	 * @return 有効期間　＝　終了日時
	 */
	public GeneralDateTime getEmbeddedUrlExpriredDate (GeneralDateTime startDate, int periodCls, int numOfPeriod);
}

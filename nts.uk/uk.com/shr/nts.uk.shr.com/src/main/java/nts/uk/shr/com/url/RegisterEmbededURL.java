package nts.uk.shr.com.url;

import java.util.List;
import java.util.Optional;

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
	public String obtainApplicationEmbeddedUrl( String appId, int appType, int prePostAtr, String employeeId);
	/**
	 * アルゴリズム「埋込URL情報登録申請」を実行する
	 * @param appId
	 * @param appType
	 * @param prePostAtr
	 * @param loginId
	 * @param employeeId
	 * @return Embed URL
	 */
	public String registerEmbeddedForApp(String appId, int appType, int prePostAtr, String loginId, String employeeId);

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
	 * @param companyId 会社ID（Optional）
	 * @return 埋込用URL
	 */
	public String embeddedUrlInfoRegis(String programId, String screenId, Integer periodCls, Integer numOfPeriod, 
			String employeeId, String contractCD, String loginId, String employeeCD, Integer isCompanyNotLogin,
			List<UrlTaskIncre> taskIncidental, Optional<String> companyId);
	// アルゴリズム「埋込URL有効期限取得」を実行する
	/**
	 * アルゴリズム「埋込URL有効期限取得」を実行する
	 * @param startDate
	 * @param periodCls
	 * @param numOfPeriod
	 * @return 有効期間　＝　終了日時
	 */
	public GeneralDateTime getEmbeddedUrlExpriredDate (GeneralDateTime startDate, int periodCls, int numOfPeriod);

	/**
	 * アルゴリズム「埋込URLデータ更新」を実行する
	 * @return
	 */
	public UrlExecInfo updateEmbeddedUrl (String cid, String contractCd, String loginID, String sCD, String sID, String programID, String screenID, GeneralDateTime issueDate, GeneralDateTime expiredDate, List<UrlTaskIncre> taskIncre);
	
	/**
	 * パスワード再設定メール有効期限チェック(Check giới hạn active mail tái thiết lập pass)
	 * @param embeddedURL 埋込URLID
	 */
	public void checkPassLimitExpire(String embeddedURLID);
}

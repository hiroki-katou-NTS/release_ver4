package nts.uk.ctx.at.shared.dom.monthlyaggrmethod;

import java.util.Optional;

/**
 * リポジトリ：職場月別実績集計設定
 * @author shuichu_ishida
 */
public interface AggrSettingMonthlyForWorkplaceRepository {

	/**
	 * 検索
	 * @param companyId 会社ID
	 * @param workplaceId 職場ID
	 * @return 該当する職場月別実績集計設定
	 */
	Optional<AggrSettingMonthlyForWorkplace> find(String companyId, String workplaceId);
	
	/**
	 * 登録および更新
	 * @param aggrSettingMonthlyForWorkplace 職場月別実績集計設定
	 */
	void persistAndUpdate(AggrSettingMonthlyForWorkplace aggrSettingMonthlyForWorkplace);
	
	/**
	 * 削除
	 * @param companyId 会社ID
	 * @param workplaceId 職場ID
	 */
	void remove(String companyId, String workplaceId);
}

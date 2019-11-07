package nts.uk.ctx.bs.employee.dom.groupcommonmaster;

import java.util.List;
import java.util.Optional;

public interface GroupCommonMasterRepository {

	/**
	 * グループ会社共通マスタの追加
	 * 
	 * @param グループ会社共通マスタ
	 *            (List)
	 */
	void addListGroupCommonMaster(List<GroupCommonMaster> domains);

	/**
	 * ドメインモデル [グループ会社共通マスタ] を取得する
	 * 
	 * @param 契約コード
	 * 
	 * @return 共通マスタリスト
	 */

	List<GroupCommonMaster> getByContractCode(String contractCode);

	/**
	 * 
	 * @param 契約コード
	 * @param 共通マスタID
	 * @return グループ会社共通マスタ
	 */
	Optional<GroupCommonMaster> getByContractCodeAndId(String contractCode, String commonMasterId);

	/**
	 * グループ会社共通マスタ項目の使用設定を削除する
	 * 
	 * @param 契約コード
	 * @param 共通マスタID
	 * @param 会社ID
	 * @param 更新項目リスト
	 */
	void removeGroupCommonMasterUsage(String contractCode, String commonMasterId, String companyId,
			List<String> masterItemIds);

	/**
	 * グループ会社共通マスタ項目の使用設定を追加する
	 * 
	 * @param 契約コード
	 * @param 共通マスタID
	 * @param 会社ID
	 * @param 更新項目リスト
	 */
	void addGroupCommonMasterUsage(String contractCode, String commonMasterId, String companyId,
			List<String> masterItemIds);
}

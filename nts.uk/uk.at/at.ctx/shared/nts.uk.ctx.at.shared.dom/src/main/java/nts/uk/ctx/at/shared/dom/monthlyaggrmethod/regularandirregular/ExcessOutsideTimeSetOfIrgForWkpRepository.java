package nts.uk.ctx.at.shared.dom.monthlyaggrmethod.regularandirregular;

/**
 * リポジトリ：職場の変形労働時間勤務の時間外超過設定
 * @author shuichu_ishida
 */
public interface ExcessOutsideTimeSetOfIrgForWkpRepository {

	/**
	 * 更新
	 * @param companyId 会社ID
	 * @param workplaceId 職場ID
	 * @param excessOutsideTimeSet 時間外超過設定
	 */
	void update(String companyId, String workplaceId, ExcessOutsideTimeSet excessOutsideTimeSet);
}

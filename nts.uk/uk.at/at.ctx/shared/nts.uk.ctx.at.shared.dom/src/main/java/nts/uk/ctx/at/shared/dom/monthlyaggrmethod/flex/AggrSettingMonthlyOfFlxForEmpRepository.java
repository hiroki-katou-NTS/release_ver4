package nts.uk.ctx.at.shared.dom.monthlyaggrmethod.flex;

/**
 * リポジトリ：雇用のフレックス時間勤務の月の集計設定
 * @author shuichu_ishida
 */
public interface AggrSettingMonthlyOfFlxForEmpRepository {
	
	/**
	 * 更新
	 * @param companyId 会社ID
	 * @param employmentCd 雇用コード
	 * @param aggrMonthlySettingOfFlx フレックス時間勤務の月の集計設定
	 */
	void update(String companyId, String employmentCd, AggrSettingMonthlyOfFlx aggrMonthlySettingOfFlx);
}

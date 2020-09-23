package nts.uk.ctx.at.shared.dom.monthlyaggrmethod.regularandirregular;

/**
 * リポジトリ：社員の変形労働時間勤務の時間外超過設定
 * @author shuichu_ishida
 */
public interface ExcessOutsideTimeSetOfIrgForSyaRepository {

	/**
	 * 更新
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param excessOutsideTimeSet 時間外超過設定
	 */
	void update(String companyId, String employeeId, ExcessOutsideTimeSet excessOutsideTimeSet);
}

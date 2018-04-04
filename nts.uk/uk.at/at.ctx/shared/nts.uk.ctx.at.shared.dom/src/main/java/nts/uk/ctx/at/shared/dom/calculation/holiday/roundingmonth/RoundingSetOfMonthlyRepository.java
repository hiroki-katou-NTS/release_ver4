package nts.uk.ctx.at.shared.dom.calculation.holiday.roundingmonth;

import java.util.Optional;

import nts.uk.ctx.at.shared.dom.monthly.roundingset.TimeRoundingOfExcessOutsideTime;

/**
 * リポジトリ：月別実績の丸め設定
 * @author shuichu_ishida
 */
public interface RoundingSetOfMonthlyRepository {

	/**
	 * 検索
	 * @param companyId 会社ID
	 * @return 月別実績の丸め設定
	 */
	Optional<TimeRoundingOfExcessOutsideTime> find(String companyId);

	/**
	 * 登録および更新
	 * @param roundingSetOfMonthly 月別実績の丸め設定
	 */
	void persistAndUpdate(TimeRoundingOfExcessOutsideTime roundingSetOfMonthly);
	
	/**
	 * 削除
	 * @param companyId 会社ID
	 */
	void remove(String companyId);
}

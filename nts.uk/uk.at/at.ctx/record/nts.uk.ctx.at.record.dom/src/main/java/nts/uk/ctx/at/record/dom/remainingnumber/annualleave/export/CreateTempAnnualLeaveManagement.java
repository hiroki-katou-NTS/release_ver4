package nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export;

import java.util.List;
import java.util.Optional;

import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.MonAggrCompanySettings;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.MonthlyCalculatingDailys;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemain;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 暫定年休管理データを作成する
 * @author shuichu_ishida
 */
public interface CreateTempAnnualLeaveManagement {

	/**
	 * 暫定年休管理データを作成する
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param period 期間
	 * @param mode モード
	 * @return 暫定年休管理データリスト
	 */
	List<InterimRemain> algorithm(String companyId, String employeeId, DatePeriod period,
			InterimRemainMngMode mode);

	/**
	 * 暫定年休管理データを作成する　（月別集計用）
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param period 期間
	 * @param mode モード
	 * @param companySets 月別集計で必要な会社別設定
	 * @param monthlyCalcDailys 月の計算中の日別実績データ
	 * @return 暫定年休管理データリスト
	 */
	List<InterimRemain> algorithm(String companyId, String employeeId, DatePeriod period,
			InterimRemainMngMode mode,
			Optional<MonAggrCompanySettings> companySets, Optional<MonthlyCalculatingDailys> monthlyCalcDailys);

}

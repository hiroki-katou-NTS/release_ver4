package nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionType;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 集計期間を取得する
 * @author shuichi_ishida
 */
public interface GetClosurePeriod {

	/**
	 * 集計期間を取得する
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param criteriaDate 基準日
	 * @param yearMonthOpt 年月（度）
	 * @param closureIdOpt 締めID
	 * @param executionTypeOpt 実行区分（通常、再実行）
	 * @return 締め処理期間リスト
	 */
	List<ClosurePeriod> get(String companyId, String employeeId, GeneralDate criteriaDate,
			Optional<YearMonth> yearMonthOpt, Optional<ClosureId> closureIdOpt, Optional<ExecutionType> executionTypeOpt);
	
	/**
	 * 年月を指定して集計期間を求める
	 * @param employeeId 社員ID
	 * @param criteriaDate 基準日
	 * @param yearMonth 対象年月
	 * @return 締め処理期間リスト
	 */
	List<ClosurePeriod> fromYearMonth(String employeeId, GeneralDate criteriaDate, YearMonth yearMonth);
	
	/**
	 * 期間を指定して集計期間を求める
	 * @param employeeId 社員ID
	 * @param criteriaDate 基準日
	 * @param period 対象期間
	 * @return 締め処理期間リスト
	 */
	List<ClosurePeriod> fromPeriod(String employeeId, GeneralDate criteriaDate, DatePeriod period);
}

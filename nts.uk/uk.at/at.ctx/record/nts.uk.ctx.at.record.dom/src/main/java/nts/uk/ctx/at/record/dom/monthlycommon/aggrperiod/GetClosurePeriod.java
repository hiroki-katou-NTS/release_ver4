package nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionType;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;

/**
 * 集計期間を取得する
 * @author shuichu_ishida
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
	 * @return 集計期間
	 */
	List<ClosurePeriod> get(String companyId, String employeeId, GeneralDate criteriaDate,
			Optional<YearMonth> yearMonthOpt, Optional<ClosureId> closureIdOpt, Optional<ExecutionType> executionTypeOpt);
}

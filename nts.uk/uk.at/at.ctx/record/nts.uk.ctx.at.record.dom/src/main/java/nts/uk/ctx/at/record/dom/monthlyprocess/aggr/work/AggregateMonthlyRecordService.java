package nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.IntegrationOfMonthly;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.param.AggrResultOfAnnAndRsvLeave;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.export.query.AbsRecRemainMngOfInPeriod;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.BreakDayOffRemainMngOfInPeriod;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service.InPeriodOfSpecialLeaveResultInfor;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * ドメインサービス：月別実績を集計する
 * @author shuichi_ishida
 */
public interface AggregateMonthlyRecordService {

	/**
	 * 集計処理
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param yearMonth 年月
	 * @param closureId 締めID
	 * @param closureDate 締め日付
	 * @param datePeriod 期間
	 * @param prevAggrResult 前回集計結果　（年休積立年休の集計結果）
	 * @param prevAbsRecResultOpt 前回集計結果　（振休振出の集計結果）
	 * @param prevBreakDayOffResultOpt 前回集計結果　（代休の集計結果）
	 * @param prevSpecialLeaveResultMap 前回集計結果　（特別休暇の集計結果）
	 * @param companySets 月別集計で必要な会社別設定
	 * @param employeeSets 月別集計で必要な社員別設定
	 * @param dailyWorks 日別実績(WORK)List
	 * @param monthlyWork 月別実績(WORK)
	 * @return 集計結果
	 */
	AggregateMonthlyRecordValue aggregate(String companyId, String employeeId,
			YearMonth yearMonth, ClosureId closureId, ClosureDate closureDate, DatePeriod datePeriod,
			AggrResultOfAnnAndRsvLeave prevAggrResult,
			Optional<AbsRecRemainMngOfInPeriod> prevAbsRecResultOpt,
			Optional<BreakDayOffRemainMngOfInPeriod> prevBreakDayOffResultOpt,
			Map<Integer, InPeriodOfSpecialLeaveResultInfor> prevSpecialLeaveResultMap,
			MonAggrCompanySettings companySets, MonAggrEmployeeSettings employeeSets,
			Optional<List<IntegrationOfDaily>> dailyWorks, Optional<IntegrationOfMonthly> monthlyWork);
}

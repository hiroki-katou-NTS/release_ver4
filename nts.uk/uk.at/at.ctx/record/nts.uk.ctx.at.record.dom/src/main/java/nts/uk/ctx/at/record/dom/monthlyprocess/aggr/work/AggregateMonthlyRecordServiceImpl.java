package nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;

import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.monthly.performance.EditStateOfMonthlyPerRepository;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.IntegrationOfMonthly;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.export.pererror.CreatePerErrorsFromLeaveErrors;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.GetDaysForCalcAttdRate;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.CreateInterimAnnualMngData;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.GetAnnAndRsvRemNumWithinPeriod;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.param.AggrResultOfAnnAndRsvLeave;
import nts.uk.ctx.at.record.dom.service.RemainNumberCreateInformation;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.export.query.AbsRecRemainMngOfInPeriod;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.export.query.AbsenceReruitmentMngInPeriodQuery;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.InterimRemainOffMonthProcess;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.InterimRemainOffPeriodCreateData;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.BreakDayOffMngInPeriodQuery;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.BreakDayOffRemainMngOfInPeriod;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service.InPeriodOfSpecialLeaveResultInfor;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service.SpecialLeaveManagementService;
import nts.uk.ctx.at.shared.dom.specialholiday.SpecialHolidayRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * ドメインサービス：月別実績を集計する
 * @author shuichi_ishida
 */
@Stateless
public class AggregateMonthlyRecordServiceImpl implements AggregateMonthlyRecordService {

	/** 月別集計が必要とするリポジトリ */
	@Inject
	private RepositoriesRequiredByMonthlyAggr repositories;
	/** 月次処理用の暫定残数管理データを作成する */
	@Inject
	private InterimRemainOffMonthProcess interimRemOffMonth;
	/** 残数作成元情報を作成する */
	@Inject
	private RemainNumberCreateInformation remNumCreateInfo;
	/** 指定期間の暫定残数管理データを作成する */
	@Inject
	private InterimRemainOffPeriodCreateData periodCreateData;
	/** 暫定年休管理データを作成する */
	@Inject
	private CreateInterimAnnualMngData createInterimAnnual;
	/** 期間中の年休積休残数を取得 */
	@Inject
	private GetAnnAndRsvRemNumWithinPeriod getAnnAndRsvRemNumWithinPeriod;
	/** 期間内の振出振休残数を取得する */
	@Inject
	private AbsenceReruitmentMngInPeriodQuery absenceRecruitMng;
	/** 期間内の休出代休残数を取得する */
	@Inject
	private BreakDayOffMngInPeriodQuery breakDayoffMng;
	/** 出勤率計算用日数を取得する */
	@Inject
	private GetDaysForCalcAttdRate getDaysForCalcAttdRate;
	/** 特別休暇 */
	@Inject
	private SpecialHolidayRepository specialHolidayRepo;
	/** 期間内の特別休暇残を集計する */
	@Inject
	private SpecialLeaveManagementService specialLeaveMng;
	/** 休暇残数エラーから月別残数エラー一覧を作成する */
	@Inject
	private CreatePerErrorsFromLeaveErrors createPerErrorFromLeaveErrors;
	/** 月別実績の編集状態 */
	@Inject
	private EditStateOfMonthlyPerRepository editStateRepo;
	
	/** 並列処理用 */
	@Resource
	private ManagedExecutorService executorService;
	
	/** 集計処理　（アルゴリズム） */
	@Override
	public AggregateMonthlyRecordValue aggregate(
			String companyId, String employeeId, YearMonth yearMonth,
			ClosureId closureId, ClosureDate closureDate, DatePeriod datePeriod,
			AggrResultOfAnnAndRsvLeave prevAggrResult,
			Optional<AbsRecRemainMngOfInPeriod> prevAbsRecResultOpt,
			Optional<BreakDayOffRemainMngOfInPeriod> prevBreakDayOffResultOpt,
			Map<Integer, InPeriodOfSpecialLeaveResultInfor> prevSpecialLeaveResultMap,
			MonAggrCompanySettings companySets, MonAggrEmployeeSettings employeeSets,
			Optional<List<IntegrationOfDaily>> dailyWorks, Optional<IntegrationOfMonthly> monthlyWork) {
		
		AggregateMonthlyRecordServiceProc proc = new AggregateMonthlyRecordServiceProc(
				this.repositories,
				this.interimRemOffMonth,
				this.remNumCreateInfo,
				this.periodCreateData,
				this.createInterimAnnual,
				this.getAnnAndRsvRemNumWithinPeriod,
				this.absenceRecruitMng,
				this.breakDayoffMng,
				this.getDaysForCalcAttdRate,
				this.specialHolidayRepo,
				this.specialLeaveMng,
				this.createPerErrorFromLeaveErrors,
				this.editStateRepo,
				this.executorService);
		
		return proc.aggregate(companyId, employeeId, yearMonth, closureId, closureDate,
				datePeriod, prevAggrResult, prevAbsRecResultOpt, prevBreakDayOffResultOpt, prevSpecialLeaveResultMap,
				companySets, employeeSets, dailyWorks, monthlyWork);
	}
	
	/** 集計処理　（アルゴリズム） */
	@Override
	public AggregateMonthlyRecordValue aggregate(String companyId, String employeeId, YearMonth yearMonth,
			ClosureId closureId, ClosureDate closureDate, DatePeriod datePeriod,
			AggrResultOfAnnAndRsvLeave prevAggrResult, Optional<AbsRecRemainMngOfInPeriod> prevAbsRecResultOpt,
			Optional<BreakDayOffRemainMngOfInPeriod> prevBreakDayOffResultOpt,
			Map<Integer, InPeriodOfSpecialLeaveResultInfor> prevSpecialLeaveResultMap,
			MonAggrCompanySettings companySets, MonAggrEmployeeSettings employeeSets,
			Optional<List<IntegrationOfDaily>> dailyWorks, Optional<IntegrationOfMonthly> monthlyWork,
			Boolean remainingProcAtr) {
		
		AggregateMonthlyRecordServiceProc proc = new AggregateMonthlyRecordServiceProc(
				this.repositories,
				this.interimRemOffMonth,
				this.remNumCreateInfo,
				this.periodCreateData,
				this.createInterimAnnual,
				this.getAnnAndRsvRemNumWithinPeriod,
				this.absenceRecruitMng,
				this.breakDayoffMng,
				this.getDaysForCalcAttdRate,
				this.specialHolidayRepo,
				this.specialLeaveMng,
				this.createPerErrorFromLeaveErrors,
				this.editStateRepo,
				this.executorService);
		
		return proc.aggregate(companyId, employeeId, yearMonth, closureId, closureDate,
				datePeriod, prevAggrResult, prevAbsRecResultOpt, prevBreakDayOffResultOpt, prevSpecialLeaveResultMap,
				companySets, employeeSets, dailyWorks, monthlyWork, remainingProcAtr);
	}
}

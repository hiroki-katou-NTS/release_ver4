package nts.uk.ctx.at.record.dom.monthlyprocess.byperiod;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.uk.ctx.at.record.dom.byperiod.AgreementTimeByPeriod;
import nts.uk.ctx.at.record.dom.byperiod.AnyItemByPeriod;
import nts.uk.ctx.at.record.dom.byperiod.AttendanceTimeOfAnyPeriod;
import nts.uk.ctx.at.record.dom.byperiod.ExcessOutsideByPeriod;
import nts.uk.ctx.at.record.dom.byperiod.MonthlyCalculationByPeriod;
import nts.uk.ctx.at.record.dom.byperiod.anyaggrperiod.AnyAggrFrameCode;
import nts.uk.ctx.at.record.dom.monthly.totalcount.TotalCountByPeriod;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.VerticalTotalOfMonthly;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.MonAggrCompanySettings;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.MonAggrEmployeeSettings;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.MonthlyCalculatingDailys;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.RepositoriesRequiredByMonthlyAggr;
import nts.uk.ctx.at.record.dom.resultsperiod.optionalaggregationperiod.OptionalAggrPeriod;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageContent;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmployeeImport;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 任意期間集計Mgr　（アルゴリズム）
 * @author shuichu_ishida
 */
@Stateless
public class AggregateByPeriodRecordServiceImpl implements AggregateByPeriodRecordService {

	/** 月別集計が必要とするリポジトリ */
	@Inject
	private RepositoriesRequiredByMonthlyAggr repositories;
	
	/** アルゴリズム */
	@Override
	public AggregateByPeriodRecordValue algorithm(String companyId, String employeeId, DatePeriod period,
			OptionalAggrPeriod optionalPeriod,
			MonAggrCompanySettings companySets, MonAggrEmployeeSettings employeeSets) {
		
		AggregateByPeriodRecordValue result = new AggregateByPeriodRecordValue();
		
		// 社員を取得する
		EmployeeImport employee = employeeSets.getEmployee();
		
		// 入社前、退職後を期間から除く　→　集計期間
		val termInOffice = new DatePeriod(employee.getEntryDate(), employee.getRetiredDate());
		DatePeriod aggrPeriod = this.confirmProcPeriod(period, termInOffice);
		if (aggrPeriod == null) {
			// 処理期間全体が、入社前または退職後の時
			return result;
		}

		// 計算に必要なデータを準備する
		MonthlyCalculatingDailys calcDailys = MonthlyCalculatingDailys.loadData(
				employeeId, aggrPeriod, this.repositories, employeeSets);

		// 労働制を取得
		Optional<WorkingConditionItem> workingConditionItemOpt =
				this.repositories.getWorkingConditionItem().getBySidAndStandardDate(employeeId, aggrPeriod.end());
		if (!workingConditionItemOpt.isPresent()){
			result.addErrorInfos("001", new ErrMessageContent(TextResource.localize("Msg_430")));
			return result;
		}
		WorkingSystem workingSystem = workingConditionItemOpt.get().getLaborSystem();
		
		// 月の計算
		MonthlyCalculationByPeriod monthlyAggregation = new MonthlyCalculationByPeriod();
		monthlyAggregation.calculation(aggrPeriod, workingSystem, calcDailys, companySets, this.repositories);
		
		// 縦計
		VerticalTotalOfMonthly verticalTotal = new VerticalTotalOfMonthly();
		verticalTotal.verticalTotal(companyId, employeeId, aggrPeriod, workingSystem,
				companySets, employeeSets, calcDailys, this.repositories);
		
		// 時間外超過
		ExcessOutsideByPeriod excessOutside = new ExcessOutsideByPeriod();
		excessOutside.aggregate(monthlyAggregation, companySets);
		
		// 36協定時間の集計
		AgreementTimeByPeriod agreementTime = new AgreementTimeByPeriod();
		agreementTime.aggregate(excessOutside);
		
		// 回数集計
		TotalCountByPeriod totalCount = new TotalCountByPeriod();
		totalCount.totalize(companyId, employeeId, aggrPeriod, companySets, calcDailys, this.repositories);
		
		// 任意項目を集計する
		AnyItemByPeriod anyItem = new AnyItemByPeriod();
		anyItem.aggregate(aggrPeriod, calcDailys, companySets, employeeSets);
		
		// 任意期間別実績の勤怠時間を返す
		AttendanceTimeOfAnyPeriod attendanceTime = AttendanceTimeOfAnyPeriod.of(
				employeeId,
				new AnyAggrFrameCode(optionalPeriod.getAggrFrameCode().v()),
				monthlyAggregation,
				excessOutside,
				agreementTime,
				verticalTotal,
				totalCount,
				anyItem);
		result.setAttendanceTime(Optional.of(attendanceTime));
		
		return result;
	}
	
	/**
	 * 処理期間との重複を確認する　（重複期間を取り出す）
	 * @param target 処理期間
	 * @param comparison 比較対象期間
	 * @return 重複期間　（null = 重複なし）
	 */
	private DatePeriod confirmProcPeriod(DatePeriod target, DatePeriod comparison){

		DatePeriod overlap = null;		// 重複期間
		
		// 開始前
		if (target.isBefore(comparison)) return overlap;
		
		// 終了後
		if (target.isAfter(comparison)) return overlap;
		
		// 重複あり
		overlap = target;
		
		// 開始日より前を除外
		if (overlap.contains(comparison.start())){
			overlap = overlap.cutOffWithNewStart(comparison.start());
		}
		
		// 終了日より後を除外
		if (overlap.contains(comparison.end())){
			overlap = overlap.cutOffWithNewEnd(comparison.end());
		}

		return overlap;
	}
}

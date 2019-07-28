package nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.workrecord.closurestatus.ClosureStatusManagement;
import nts.uk.ctx.at.record.dom.workrecord.closurestatus.ClosureStatusManagementRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.shr.com.time.calendar.date.ClosureDate;

/**
 * 実装：締め処理すべき集計期間を計算
 * @author shuichi_ishida
 */
@Stateless
public class CalcPeriodForClosureProcessImpl implements CalcPeriodForClosureProcess {

	/** 締め */
	@Inject
	private ClosureRepository closureRepo;
	/** 締めサービス */
	@Inject
	private ClosureService closureService;
	/** 集計すべき期間を計算 */
	@Inject
	private CalcPeriodForAggregate calcPeriodForAggregate;
	/** 締め状態管理 */
	@Inject
	private ClosureStatusManagementRepository closureSttMngRepo;

	/** 締め処理すべき集計期間を計算 */
	@Override
	public CalcPeriodForClosureProcValue algorithm(String companyId, String employeeId, int closureId){
		
		CalcPeriodForClosureProcValue returnValue = new CalcPeriodForClosureProcValue();
		
		// 「締め」を取得
		val closureOpt = this.closureRepo.findById(companyId, closureId);
		if (!closureOpt.isPresent()) return returnValue;
		val closure = closureOpt.get();
		
		// 当月の期間を算出する
		val currentYm = closure.getClosureMonth().getProcessingYm();
		val currentPeriod = this.closureService.getClosurePeriod(closureId, currentYm);
		if (currentPeriod == null) return returnValue;
		
		// 当月の締め日を取得する
		val currentDateOpt = closure.getClosureDateOfCurrentMonth();
		if (!currentDateOpt.isPresent()) return returnValue;
		val currentDate = currentDateOpt.get();
		
		// 集計すべき期間を計算
		List<ClosurePeriod> periodForAggregateList =
				this.calcPeriodForAggregate.algorithm(employeeId, currentPeriod.end());

		// 締め処理すべき締め期間に絞り込む
		val closurePeriodOpt = this.refineClosurePeriod(closureId, currentDate, currentYm, periodForAggregateList);
		if (!closurePeriodOpt.isPresent()){			
			// 対象締め処理期間なし
			return returnValue;
		}
				
		// 「締め状態管理」を取得
		Optional<ClosureStatusManagement> sttMng = closureSttMngRepo.getLatestByEmpId(employeeId);
		if (!sttMng.isPresent()) {
			// 「締め処理期間」を返す
			returnValue.setState(CalcPeriodForClosureProcState.EXIST);
			returnValue.setClosurePeriod(closurePeriodOpt);
			return returnValue;
		}
		
		// provisional 締め処理済み年月日
		GeneralDate closureProcessedDate = sttMng.get().getPeriod().end();		
		
		val closurePeriod = closurePeriodOpt.get();
		
		// 指定した年月日までの期間を除外する
		closurePeriod.excludePeriodByYmd(closureProcessedDate);
		
		// 「締め処理期間．集計期間」の件数をチェック
		if (closurePeriod.getAggrPeriods().size() > 0) {
			
			// 「締め処理期間」を返す
			returnValue.setState(CalcPeriodForClosureProcState.EXIST);
			returnValue.setClosurePeriod(Optional.of(closurePeriod));
			return returnValue;
		}
		
		// 既に締め処理済み
		returnValue.setState(CalcPeriodForClosureProcState.PROCESSED);
		return returnValue;
	}
	
	/**
	 * 締め処理すべき締め処理期間に絞り込む
	 * @param closureId 締めID
	 * @param closureDate 締め日
	 * @param yearMonth 年月
	 * @param periodForAggregateList 集計すべき締め処理期間リスト
	 * @return 締め処理期間
	 */
	private Optional<ClosurePeriod> refineClosurePeriod(
			int closureId, ClosureDate closureDate, YearMonth yearMonth,
			List<ClosurePeriod> periodForAggregateList){

		// 結合候補実締め毎集計期間
		List<AggrPeriodEachActualClosure> candidateForCombine = new ArrayList<>();
		
		for (val periodForAggregate : periodForAggregateList){
			
			// 処理中の「締め処理期間」とパラメータを比較して一致チェック
			if (periodForAggregate.getClosureId().value == closureId &&
				periodForAggregate.getClosureDate().getClosureDay().equals(closureDate.getClosureDay()) &&
				periodForAggregate.getClosureDate().getLastDayOfMonth() == closureDate.getLastDayOfMonth() &&
				periodForAggregate.getYearMonth().equals(yearMonth)){
			
				// 処理中の「締め処理期間．集計期間」の先頭に「結合候補」を追加
				candidateForCombine.addAll(periodForAggregate.getAggrPeriods());
				periodForAggregate.setAggrPeriods(candidateForCombine);
				
				// 「締め処理期間」を返す
				return Optional.of(periodForAggregate);
			}
			
			// 「結合候補」に処理中の「実締め毎集計期間」を追加
			candidateForCombine.addAll(periodForAggregate.getAggrPeriods());
		}
		// 対象締め処理期間なし
		return Optional.empty();
	}
}

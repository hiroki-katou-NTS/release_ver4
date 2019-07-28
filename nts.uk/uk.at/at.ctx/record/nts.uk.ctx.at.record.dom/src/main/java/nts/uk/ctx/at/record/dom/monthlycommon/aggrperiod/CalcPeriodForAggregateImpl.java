package nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.adapter.employment.EmploymentHistAdapter;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.UseClassification;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureIdHistory;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 実装：集計すべき期間を計算
 * @author shuichi_ishida
 */
@Stateless
public class CalcPeriodForAggregateImpl implements CalcPeriodForAggregate {

	/** 所属雇用履歴の取得 */
	@Inject
	private EmploymentHistAdapter employmentHistAdapter;
	/** 雇用に紐づく就業締めの取得 */
	@Inject
	private ClosureEmploymentRepository closureEmploymentRepository;
	/** 締めの取得 */
	@Inject
	private ClosureRepository closureRepository;
	/** 当月の期間を算出する */
	@Inject
	private ClosureService closureService;
	
	/** 集計すべき期間を計算 */
	@Override
	public List<ClosurePeriod> algorithm(String employeeId, GeneralDate aggrEnd){
		
		List<ClosureIdHistory> closureIdHistories = new ArrayList<>();		// 締めID履歴リスト

		// 締められていない期間の締めIDを取得
		this.getClosureIdOfNotClosurePeriod(employeeId, closureIdHistories);

		// 締め履歴から集計期間を生成
		return this.fromClosureHistory(employeeId, aggrEnd, closureIdHistories);
	}

	/** 締め履歴から集計期間を生成 */
	@Override
	public List<ClosurePeriod> fromClosureHistory(String employeeId, GeneralDate aggrEnd,
			List<ClosureIdHistory> closureIdHistories) {

		List<AggrPeriodEachActualClosure> aggrPeriods = new ArrayList<>();	// 実締め毎集計期間リスト
		List<ClosurePeriod> closurePeriods = new ArrayList<>();				// 締め処理期間リスト
		
		// 締めID履歴の先頭の開始日と「集計終了日」を比較　→　検索終了日
		GeneralDate findEnd = aggrEnd;	// 検索終了日
		if (!closureIdHistories.isEmpty()){
			GeneralDate checkStart = closureIdHistories.get(0).getPeriod().start();
			if (checkStart.after(aggrEnd)) findEnd = checkStart;
		}
		
		for (val closureIdHistory: closureIdHistories){
			
			// 締めID履歴の期間内で締め処理期間を作成
			this.createAggrPeriodEachActualClosureWithinHistories(
					closureIdHistory, findEnd.addMonths(1), aggrPeriods);
		}
		
		for (val aggrPeriod : aggrPeriods){
			
			// 締め処理期間を作成
			this.createClosurePeriod(aggrPeriod, aggrEnd, closurePeriods);
		}
		
		if (!closurePeriods.isEmpty()){

			// 締め処理期間の先頭の年月を取得
			YearMonth firstYm = closurePeriods.get(0).getYearMonth();
			
			// 締め処理期間から、集計終了日以降の「締め処理期間」を削除
			ListIterator<ClosurePeriod> itrClosurePeriods = closurePeriods.listIterator();
			while (itrClosurePeriods.hasNext()){
				ClosurePeriod target = itrClosurePeriods.next();
				if (!target.getYearMonth().equals(firstYm)){
					if (target.getAggrPeriods().isEmpty()){
						itrClosurePeriods.remove();
					}
					else if (target.getAggrPeriods().get(0).getPeriod().start().after(aggrEnd)){
						itrClosurePeriods.remove();
					}
				}
			}
		}
		
		return closurePeriods;
	}
	
	/**
	 * 締められていない期間の締めIDを取得
	 * @param employeeId 社員ID
	 * @param closureIdHistories 締めID履歴リスト
	 */
	private void getClosureIdOfNotClosurePeriod(
			String employeeId,
			List<ClosureIdHistory> closureIdHistories){

		String companyId = AppContexts.user().companyId();		// 処理中の会社ID
		
		// 「所属雇用履歴」を取得する
		val employmentHists = this.employmentHistAdapter.findByEmployeeIdOrderByStartDate(employeeId);
		
		for (val employmentHist : employmentHists){
			val employmentCd = employmentHist.getEmploymentCode();
			
			// 締めIDを取得する
			val closureEmploymentOpt = this.closureEmploymentRepository.findByEmploymentCD(companyId, employmentCd);
			if (!closureEmploymentOpt.isPresent()) continue;
			val closureEmployment = closureEmploymentOpt.get();
			
			// 「締め」を取得
			val closureId = closureEmployment.getClosureId();
			val closureOpt = this.closureRepository.findById(companyId, closureId);
			if (!closureOpt.isPresent()) continue;
			val closure = closureOpt.get();
			if (closure.getUseClassification() != UseClassification.UseClass_Use) continue;
			
			// 当月の期間を算出する
			val processingYm = closure.getClosureMonth().getProcessingYm();
			val closurePeriod = this.closureService.getClosurePeriod(closureId, processingYm);
			
			// 「所属雇用履歴」が「締め期間」より前がチェック
			val employmentPeriod = employmentHist.getPeriod();
			if (employmentPeriod.end().before(closurePeriod.start())) continue;
			
			// 「締めID履歴リスト」の末尾と締めIDを比較
			if (closureIdHistories.size() > 0){
				val lastHistory = closureIdHistories.get(closureIdHistories.size() - 1);
				if (lastHistory.getClosureId().value == closureId.intValue()){
					
					// 「締めID履歴リスト」の末尾を更新
					lastHistory.setPeriod(new DatePeriod(
							lastHistory.getPeriod().start(), employmentPeriod.end()));
					continue;
				}
			}
			
			// 処理中の「所属雇用履歴」より前が締められたかチェックし、「締めID履歴」を作成
			if (employmentPeriod.start().after(closurePeriod.start())){
				closureIdHistories.add(ClosureIdHistory.of(closureId, employmentPeriod));
			}
			else {
				closureIdHistories.add(ClosureIdHistory.of(closureId,
						new DatePeriod(closurePeriod.start(), employmentPeriod.end())));
			}
		}
	}
	
	/**
	 * 締めID履歴の期間内で締め処理期間を作成
	 * @param closureIdHistory 締めID履歴
	 * @param aggrEnd 集計終了日
	 * @param aggrPeriods 実締め毎集計期間リスト
	 */
	private void createAggrPeriodEachActualClosureWithinHistories(
			ClosureIdHistory closureIdHistory,
			GeneralDate aggrEnd,
			List<AggrPeriodEachActualClosure> aggrPeriods){

		String companyId = AppContexts.user().companyId();		// 処理中の会社ID
	
		val closureId = closureIdHistory.getClosureId();
		val histPeriod = closureIdHistory.getPeriod();
		
		// 集計開始日を取得
		GeneralDate aggrStart = histPeriod.start();
		
		// 「締め」を取得
		val closureOpt = this.closureRepository.findById(companyId, closureId.value);
		if (!closureOpt.isPresent()) return;
		val closure = closureOpt.get();
		if (closure.getUseClassification() != UseClassification.UseClass_Use) return;
		
		while (true){
		
			// 指定した年月日時点の締め期間を取得する
			val closurePeriodOpt = closure.getClosurePeriodByYmd(aggrStart);
			if (!closurePeriodOpt.isPresent()) return;
			val closurePeriod = closurePeriodOpt.get();
			
			// 集計対象期間を作成する
			DatePeriod aggrTargetPeriod = new DatePeriod(aggrStart, closurePeriod.getPeriod().end());
			
			// 「締めID履歴．期間」と「集計対象期間」の重複を計算
			if (histPeriod.isBefore(aggrTargetPeriod)) break;
			if (histPeriod.isAfter(aggrTargetPeriod)) break;
			if (aggrTargetPeriod.contains(histPeriod.start())){
				aggrTargetPeriod = aggrTargetPeriod.cutOffWithNewStart(histPeriod.start());
			}
			if (aggrTargetPeriod.contains(histPeriod.end())){
				aggrTargetPeriod = aggrTargetPeriod.cutOffWithNewEnd(histPeriod.end());
			}
			
			// 「実締め毎集計期間」を作成
			aggrPeriods.add(AggrPeriodEachActualClosure.of(closureId, closurePeriod.getClosureDate(),
					closurePeriod.getYearMonth(), aggrTargetPeriod, closurePeriod.getPeriod()));
			
			// 「締めID履歴．終了日」と「対象集計期間．終了日」を比較
			if (histPeriod.end().beforeOrEquals(aggrTargetPeriod.end())) break;
			
			// 「集計終了日」と「対象集計期間．終了日」を比較
			if (aggrEnd.beforeOrEquals(aggrTargetPeriod.end())) break;
			
			// 集計開始日←「対象集計期間．終了日」の翌日
			aggrStart = aggrTargetPeriod.end().addDays(1);
		}
	}
	
	/**
	 * 締め処理期間を作成
	 * @param aggrPeriod 実締め毎集計期間
	 * @param aggrEnd 集計終了日
	 * @param closurePeriods 締め処理期間リスト
	 */
	private void createClosurePeriod(
			AggrPeriodEachActualClosure aggrPeriod,
			GeneralDate aggrEnd,
			List<ClosurePeriod> closurePeriods){
		
		// 「締め処理期間リスト」の末尾を確認
		if (closurePeriods.size() > 0) {
			val lastPeriod = closurePeriods.get(closurePeriods.size() - 1);
			
			// 既存の締め処理期間に追加するかチェック
			if (lastPeriod.getYearMonth().equals(aggrPeriod.getYearMonth()) &&
				lastPeriod.getClosureYmd().afterOrEquals(aggrPeriod.getOriginalClosurePeriod().end())){
				
				// 末尾の締め処理期間に実締め毎集計期間を追加
				lastPeriod.addAggrPeriodEachActualClosure(aggrPeriod);
				return;
			}
		}
		
		// 締め処理期間を新規作成し、リストに追加
		closurePeriods.add(ClosurePeriod.of(aggrPeriod));
	}
}

package nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.adapter.employment.EmploymentHistAdapter;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionType;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.UseClassification;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 集計期間を取得する
 * @author shuichu_ishida
 */
@Stateless
public class GetClosurePeriodImpl implements GetClosurePeriod {

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
	
	/** 締め処理期間リスト */
	private List<ClosurePeriod> closurePeriods;
	/** 締めID履歴リスト */
	private List<ClosureIdHistory> closureIdHistories;
	/** 実締め毎集計期間リスト */
	private List<AggrPeriodEachActualClosure> aggrPeriods;
	
	/** 集計期間を取得する */
	@Override
	public List<ClosurePeriod> get(String companyId, String employeeId, GeneralDate criteriaDate,
			Optional<YearMonth> yearMonthOpt, Optional<ClosureId> closureIdOpt, Optional<ExecutionType> executionTypeOpt) {
		
		this.closurePeriods = new ArrayList<>();
		this.closureIdHistories = new ArrayList<>();
		this.aggrPeriods = new ArrayList<>();

		// ※　Optinal引数の組み合わせ不足等エラーは、throw new RuntimeError...で。
		//    現時点では、引数の利用が未設計。（2018.3.15 shuichi_ishida）
		
		// 集計すべき期間を計算
		this.calculatePeriodForAggregate(companyId, employeeId, criteriaDate);

		return this.closurePeriods;
	}
	
	/**
	 * 集計すべき期間を計算
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param aggrEnd 集計終了日
	 */
	private void calculatePeriodForAggregate(String companyId, String employeeId, GeneralDate aggrEnd){
	
		// 締められていない期間の締めIDを取得
		this.getClosureIdOfNotClosurePeriod(companyId, employeeId);

		for (val closureIdHistory: this.closureIdHistories){
			
			// 締めID履歴の期間内で締め処理期間を作成
			this.createAggrPeriodEachActualClosureWithinHistories(companyId, closureIdHistory, aggrEnd);
		}
		
		for (val aggrPeriod : this.aggrPeriods){
			
			// 締め処理期間を作成
			this.createClosurePeriod(aggrPeriod, aggrEnd);
		}
	}
	
	/**
	 * 締められていない期間の締めIDを取得
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 */
	private void getClosureIdOfNotClosurePeriod(String companyId, String employeeId){
		
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
			
			// 処理中の「所属雇用履歴」より前が締められたかチェックし、「締めID履歴」を作成
			if (employmentPeriod.start().after(closurePeriod.start())){
				this.closureIdHistories.add(ClosureIdHistory.of(closureId, employmentPeriod));
			}
			else {
				this.closureIdHistories.add(ClosureIdHistory.of(closureId,
						new DatePeriod(closurePeriod.start(), employmentPeriod.end())));
			}
		}
	}
	
	/**
	 * 締めID履歴の期間内で締め処理期間を作成
	 * @param companyId 会社ID
	 * @param closureIdHistory 締めID履歴
	 * @param aggrEnd 集計終了日
	 */
	private void createAggrPeriodEachActualClosureWithinHistories(
			String companyId, ClosureIdHistory closureIdHistory, GeneralDate aggrEnd){
	
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
			this.aggrPeriods.add(AggrPeriodEachActualClosure.of(closureId, closurePeriod.getClosureDate(),
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
	 */
	private void createClosurePeriod(AggrPeriodEachActualClosure aggrPeriod, GeneralDate aggrEnd){
		
		// 「締め処理期間リスト」の末尾を確認
		if (this.closurePeriods.size() > 0) {
			val lastPeriod = this.closurePeriods.get(this.closurePeriods.size() - 1);
			
			// 末尾の「締め処理期間．締め年月日」と「実締め毎集計期間．本来の締め期間．終了日」を比較
			if (lastPeriod.getClosureYmd().afterOrEquals(aggrPeriod.getOriginalClosurePeriod().end())){
				
				// 末尾の締め処理期間に実締め毎集計期間を追加
				lastPeriod.addAggrPeriodEachActualClosure(aggrPeriod);
				return;
			}
		}
		
		// 集計終了日を過ぎた期間かチェック
		if (aggrPeriod.getPeriod().start().after(aggrEnd)) return;
		
		// 締め処理期間を新規作成し、リストに追加
		this.closurePeriods.add(ClosurePeriod.of(aggrPeriod));
	}
}

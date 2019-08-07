package nts.uk.ctx.at.shared.dom.workrule.closure.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.shared.dom.adapter.employment.AffPeriodEmpCodeImport;
import nts.uk.ctx.at.shared.dom.adapter.employment.ShareEmploymentAdapter;
import nts.uk.ctx.at.shared.dom.adapter.employment.SharedSidPeriodDateEmploymentImport;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmployment;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 実装：締め履歴を取得する
 * @author shuichi_ishida
 */
@Stateless
public class GetClosureIdHistoryImpl implements GetClosureIdHistory {

	/** 締めの取得 */
	@Inject
	private ClosureRepository closureRepository;
	/** 雇用履歴の取得 */
	@Inject
	private ShareEmploymentAdapter shareEmploymentAdapter;
	/** 雇用に紐づく就業締めの取得 */
	@Inject
	private ClosureEmploymentRepository closureEmploymentRepo;
	
	/** 指定した年月の社員の締め履歴を取得する */
	@Override
	public List<ClosureIdHistory> ofEmployeeFromYearMonth(String employeeId, YearMonth yearMonth) {

		List<ClosureIdHistory> results = new ArrayList<>();
		
		String companyId = AppContexts.user().companyId();		// 処理中の会社ID
		
		// 「締め」を取得する
		List<Closure> closureList = this.closureRepository.findAllUse(companyId);
		if (closureList.size() == 0) return results;
		
		List<DatePeriod> periodList = new ArrayList<>();		// 期間リスト
		for (Closure closure : closureList) {
			
			// 指定した年月の期間をすべて取得する
			periodList.addAll(closure.getPeriodByYearMonth(yearMonth));
		}
		if (periodList.size() == 0) return results;
		
		// 期間リストのMAX期間を求める
		GeneralDate maxStart = periodList.get(0).start();
		GeneralDate maxEnd = periodList.get(0).end();
		for (DatePeriod period : periodList) {
			if (maxStart.after(period.start())) maxStart = period.start();
			if (maxEnd.before(period.end())) maxEnd = period.end();
		}
		DatePeriod maxPeriod = new DatePeriod(maxStart, maxEnd);
		
		// 指定した期間の社員の締め履歴を取得する
		results.addAll(this.ofEmployeeFromPeriod(employeeId, maxPeriod));
		
		return results;
	}
	
	/** 指定した期間の社員の締め履歴を取得する */
	@Override
	public List<ClosureIdHistory> ofEmployeeFromPeriod(String employeeId, DatePeriod period) {
		
		List<ClosureIdHistory> results = new ArrayList<>();
		
		String companyId = AppContexts.user().companyId();		// 処理中の会社ID
		
		// 社員IDリストと指定期間から社員の雇用履歴を取得
		List<String> employeeIds = new ArrayList<>();
		employeeIds.add(employeeId);
		List<SharedSidPeriodDateEmploymentImport> employments =
				this.shareEmploymentAdapter.getEmpHistBySidAndPeriod(employeeIds, period);
		if (employments.size() == 0) return results;
		List<AffPeriodEmpCodeImport> periodEmpCodes = employments.get(0).getAffPeriodEmpCodeExports();
		if (periodEmpCodes.size() == 0) return results;
		
		// 雇用期間から対象期間外を除く
		for (AffPeriodEmpCodeImport periodEmpCode : periodEmpCodes) {
			GeneralDate startDate = periodEmpCode.getPeriod().start();
			GeneralDate endDate = periodEmpCode.getPeriod().end();
			if (startDate.before(period.start())) startDate = period.start();
			if (endDate.after(period.end())) endDate = period.end();
			periodEmpCode.setPeriod(new DatePeriod(startDate, endDate));
		}
		periodEmpCodes.sort((a, b) -> a.getPeriod().start().compareTo(b.getPeriod().start()));
		
		// 「雇用に紐づく就業締め」を取得する
		List<String> employmentCds = new ArrayList<>();
		for (AffPeriodEmpCodeImport periodEmpCode : periodEmpCodes) {
			if (!employmentCds.contains(periodEmpCode.getEmploymentCode())) {
				employmentCds.add(periodEmpCode.getEmploymentCode());
			}
		}
		List<ClosureEmployment> closureEmployments =
				this.closureEmploymentRepo.findListEmployment(companyId, employmentCds);
		Map<String, ClosureEmployment> closureEmploymentMap = new HashMap<>();
		for (ClosureEmployment closureEmployment : closureEmployments) {
			closureEmploymentMap.putIfAbsent(closureEmployment.getEmploymentCD(), closureEmployment);
		}
		
		// 取得した情報から「締めID履歴」リストを作成
		for (AffPeriodEmpCodeImport periodEmpCode : periodEmpCodes) {
			if (!closureEmploymentMap.containsKey(periodEmpCode.getEmploymentCode())) continue;
			Integer closureId = closureEmploymentMap.get(periodEmpCode.getEmploymentCode()).getClosureId();
			
			boolean isMerge = false;
			for (ClosureIdHistory result : results) {
				// 既にある結果データの内、締めIDが同じ かつ その終了日と確認中データの開始日が連続する場合、結果データの期間に期間を連結する
				if (result.getClosureId().value == closureId.intValue() &&
					result.getPeriod().end().compareTo(periodEmpCode.getPeriod().start().addDays(-1)) == 0) {
					DatePeriod newPeriod = new DatePeriod(result.getPeriod().start(), periodEmpCode.getPeriod().end());
					result.setPeriod(newPeriod);
					isMerge = true;
					break;
				}
			}
			if (isMerge == false) {
				results.add(ClosureIdHistory.of(closureId, periodEmpCode.getPeriod()));
			}
		}
		
		return results;
	}
}

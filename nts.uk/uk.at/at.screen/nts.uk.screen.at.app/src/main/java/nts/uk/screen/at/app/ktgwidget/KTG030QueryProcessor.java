package nts.uk.screen.at.app.ktgwidget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.ApprovalStatusAdapter;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.AppRootOfEmpMonthImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.AppRootSituationMonth;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.month.ConfirmationMonth;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.ConfirmationMonthRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.ctx.at.shared.pub.workrule.closure.PresentClosingPeriodExport;
import nts.uk.ctx.at.shared.pub.workrule.closure.ShClosurePub;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class KTG030QueryProcessor {

	@Inject
	private ShClosurePub shClosurePub;

	@Inject
	private ClosureService closureService;
	
	@Inject
	private ApprovalStatusAdapter approvalStatusAdapter;
	
	@Inject
	private ConfirmationMonthRepository confirmationMonthRepository;

	public boolean confirmMonthActual() {
		// アルゴリズム「雇用に基づく締めを取得する」をする
		String cid = AppContexts.user().companyId();
		String employeeID = AppContexts.user().employeeId();
//		Optional<BsEmploymentHistoryImport> empHistoryOpt = shareEmpAdapter.findEmploymentHistory(cid, employeeID, GeneralDate.today());
//		if (!empHistoryOpt.isPresent()) {
//			throw new RuntimeException("Not found Employment history by employeeId:" + employeeID);
//		}
//		BsEmploymentHistoryImport empHistory = empHistoryOpt.get();
//		Optional<ClosureEmployment> closureEmploymentOpt = closureEmploymentRepo.findByEmploymentCD(cid, empHistory.getEmploymentCode());
//		int closureID = 1;
//		if (closureEmploymentOpt.isPresent()) {
//			 closureID = closureEmploymentOpt.get().getClosureId();
//		}
		
		
		// アルゴリズム「社員に対応する処理締めを取得する」を実行する
		Closure closure = closureService.getClosureDataByEmployee(employeeID, GeneralDate.today());
		// 当月の締め日を取得する
		Optional<ClosureDate> closureDate = closure.getClosureDateOfCurrentMonth();
		//アルゴリズム「処理年月と締め期間を取得する」を実行する
		//(Thực hiện thuật toán 「Lấy processYM và closingPeriod」)
		Optional<PresentClosingPeriodExport> presentClosingPeriod = shClosurePub.find(cid, closure.getClosureId().value);
		if (!presentClosingPeriod.isPresent()) {
			throw new RuntimeException("Not found PresentClosingPeriodExport by closureID" + closure.getClosureId().value);
			
		}
		YearMonth yearMonth = presentClosingPeriod.get().getProcessingYm();
		GeneralDate closureStartDate = presentClosingPeriod.get().getClosureStartDate();
		GeneralDate closureEndDate = presentClosingPeriod.get().getClosureEndDate();

		// "Acquire 「日別実績確認有無取得」"
		/*
		 * input · Employee ID · Date (start date) <= Tightening start date ·
		 * 
		 * Date (end date) <= closing end date
		 * Route type <= Employment application
		 */

		// RootType(就業日別確認) = 2
		DatePeriod period = new DatePeriod(closureStartDate, closureEndDate);
		// 月別実績確認すべきデータ有無取得
		boolean checkMonthApproved = this.ObtAppliDataPreAbs(employeeID, period, yearMonth, closure.getClosureId().value, closureDate.get());
//		boolean checkMonthApproved = dailyPerformanceAdapter.dataMonth(employeeID, period, yearMonth);
		return checkMonthApproved;
	}

	/**
	 * 月別実績確認すべきデータ有無取得
	 * @author yennth
	 */
	public boolean ObtAppliDataPreAbs(String employeeID, DatePeriod period, YearMonth yearMonth, int closureId, ClosureDate closureDate){
		List<String> listEmp = new ArrayList<>();
		// list luu 年月日 tam thoi
		List<YearMonth> listEmpTemp = new ArrayList<>();
		List<AppRootSituationMonth> approvalRootSituations = new ArrayList<>();
		Map<String, List<YearMonth>> empDate = new HashMap<String, List<YearMonth>>();
		// [No.534](中間データ版)承認状況を取得する （月別）
		AppRootOfEmpMonthImport rootOfEmp = approvalStatusAdapter.getApprovalEmpStatusMonth(employeeID, yearMonth, closureId, closureDate, period.end());
		// 取得したデータから、承認すべき社員と年月日リストを抽出する
		approvalRootSituations.addAll(rootOfEmp.getApprovalRootSituations());
		for(AppRootSituationMonth item : approvalRootSituations){
			if(!empDate.containsKey(item.getTargetID())){
				listEmpTemp = new ArrayList<>();
				listEmpTemp.add(item.getYearMonth());
				empDate.put(item.getTargetID(), listEmpTemp);
			}else{
				List<YearMonth> variable = empDate.get(item.getTargetID());
				variable.add(item.getYearMonth());
			}
		}
		// 月の本人確認を取得する
		for (Map.Entry<String, List<YearMonth>> entry : empDate.entrySet()) {
			if(!listEmp.contains(entry.getKey().toString())){
				listEmp.add(entry.getKey().toString());
			}
        }
		List<ConfirmationMonth> listIdent = confirmationMonthRepository.findBySomeProperty(listEmp, yearMonth.v(), closureDate.getClosureDay().v(), 
												closureDate.getLastDayOfMonth().booleanValue(), closureId);
		if(!listIdent.isEmpty()){
			return true;
		}
		return false;
	}
}

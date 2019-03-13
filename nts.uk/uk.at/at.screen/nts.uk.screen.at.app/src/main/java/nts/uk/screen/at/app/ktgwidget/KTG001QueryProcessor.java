package nts.uk.screen.at.app.ktgwidget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.Identification;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.IdentificationRepository;
import nts.uk.ctx.at.shared.dom.adapter.dailyperformance.AppEmpStatusImport;
import nts.uk.ctx.at.shared.dom.adapter.dailyperformance.DailyPerformanceAdapter;
import nts.uk.ctx.at.shared.dom.adapter.dailyperformance.RouteSituationImport;
import nts.uk.ctx.at.shared.dom.adapter.employment.BsEmploymentHistoryImport;
import nts.uk.ctx.at.shared.dom.adapter.employment.ShareEmploymentAdapter;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmployment;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.ctx.at.shared.pub.workrule.closure.PresentClosingPeriodExport;
import nts.uk.ctx.at.shared.pub.workrule.closure.ShClosurePub;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class KTG001QueryProcessor {
	@Inject
	private ClosureEmploymentRepository closureEmploymentRepo;

	@Inject
	private ShClosurePub shClosurePub;

	@Inject
	private IdentificationRepository identificationRepository;
	/**
	 * 日別実績確認すべきデータ有無表示
	 * 
	 * @return
	 */
	@Inject
	private DailyPerformanceAdapter dailyPerformanceAdapter;

	@Inject
	private ShareEmploymentAdapter shareEmpAdapter;

	public boolean confirmDailyActual() {
		// アルゴリズム「雇用に基づく締めを取得する」をする
		String cid = AppContexts.user().companyId();
		String employeeID = AppContexts.user().employeeId();

		Optional<BsEmploymentHistoryImport> empHistoryOpt = shareEmpAdapter.findEmploymentHistory(cid, employeeID, GeneralDate.today());
		if (!empHistoryOpt.isPresent()) {
			throw new RuntimeException("Not found Employment history by employeeId:" + employeeID);
		}
		BsEmploymentHistoryImport empHistory = empHistoryOpt.get();
		Optional<ClosureEmployment> closureEmploymentOpt = closureEmploymentRepo.findByEmploymentCD(cid, empHistory.getEmploymentCode());
		if (!closureEmploymentOpt.isPresent()) {
			throw new RuntimeException("Not found Employment history by employeeCd:" + empHistory.getEmploymentCode());
		}

		int closureID = closureEmploymentOpt.get().getClosureId();

		// アルゴリズム「処理年月と締め期間を取得する」を実行する
		Optional<PresentClosingPeriodExport> presentClosingPeriod = shClosurePub.find(cid, closureID);
		if (!presentClosingPeriod.isPresent()) {
			throw new RuntimeException("Not found PresentClosingPeriodExport by closureID" + closureID );
			
		}
		GeneralDate closureStartDate = presentClosingPeriod.get().getClosureStartDate();
		GeneralDate closureEndDate = presentClosingPeriod.get().getClosureEndDate();

		// "Acquire 「日別実績確認有無取得」"	
		/*
		 * input · Employee ID · Date (start date) <= Tightening start date ·
		 * Date (end date) <= closing end date + 1 month · 
		 * Route type <= Employment application
		 */

		// RootType(就業日別確認) = 1
		DatePeriod period = new DatePeriod(closureStartDate, closureEndDate);
//		boolean checkDateApproved = dailyPerformanceAdapter.isDataExist(employeeID, period, 1);
		boolean checkDateApproved = this.ObtAppliDataPreAbs(employeeID, period);
		return checkDateApproved;
	}
	
	/**
	 * 承認すべき申請データ有無取得
	 * @author yennth
	 */
	public boolean ObtAppliDataPreAbs(String employeeID, DatePeriod period){
		List<String> listEmp = new ArrayList<>();
		// list luu 年月日 tam thoi
		List<GeneralDate> listEmpTemp = new ArrayList<>();
		List<RouteSituationImport> routeLst = new ArrayList<>();
		Map<String, List<GeneralDate>> empDate = new HashMap<String, List<GeneralDate>>();
		// [No.133](中間データ版)承認状況を取得する
		AppEmpStatusImport appEmpStatusImport = dailyPerformanceAdapter.appEmpStatusExport(employeeID, period, 1);
		// 取得したデータから、承認すべき社員と年月日リストを抽出する
		routeLst.addAll(appEmpStatusImport.getRouteSituationLst());
		for(RouteSituationImport item : routeLst){
			if(!empDate.containsKey(item.getEmployeeID())){
				listEmpTemp = new ArrayList<>();
				listEmpTemp.add(item.getDate());
				empDate.put(item.getEmployeeID(), listEmpTemp);
			}else{
				List<GeneralDate> variable = empDate.get(item.getEmployeeID());
				variable.add(item.getDate());
			}
		}
		// 日の本人確認を取得する
		List<Identification> listIdent = identificationRepository.findByListEmployeeID(listEmp, period.start(), period.end());
		// 社員IDと年月日が一致する「日の本人確認」があるかチェックする
		for(Identification obj : listIdent){
			if(empDate.containsKey(obj.getProcessingYmd())){
				List<GeneralDate> listValue = empDate.get(obj.getEmployeeId());
				if(listValue.contains(obj.getProcessingYmd())){
					return true;
				}
			}
		}
		return false;
	}
}

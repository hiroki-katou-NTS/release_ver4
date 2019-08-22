package nts.uk.ctx.at.request.dom.vacation.history.service;

/*import java.util.Collections;*/
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.EmployeeRequestAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.dto.SEmpHistImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.remainingnumber.AnnualHolidayPlanManaAdapter;
import nts.uk.ctx.at.request.dom.vacation.history.PlanVacationHistory;
import nts.uk.ctx.at.request.dom.vacation.history.VacationHistoryRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmployment;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class PlanVacationRuleExportImpl implements PlanVacationRuleExport{
	@Inject
	private VacationHistoryRepository vacationHisRepos;
	@Inject
	private AnnualHolidayPlanManaAdapter annualAdapter;
	@Inject
	private EmployeeRequestAdapter employmentAdapter;
	@Inject
	private ClosureEmploymentRepository closureEmploymentService;
	@Inject
	private ClosureRepository closureRepository;
	@Inject
	private ClosureService closureService;
	@Override
	public List<PlanVacationRuleError> checkMaximumOfPlan(String cid, String employeeId, String workTypeCode, DatePeriod dateData,
			List<GeneralDate> lstDateIsHoliday) {
		List<PlanVacationRuleError> lstOutput = new ArrayList<>();
		//指定する期間内の計画休暇のルールの履歴を取得する
		//ドメインモデル「計画休暇のルールの履歴」を取得する
		List<PlanVacationHistory> lstVactionPeriod = vacationHisRepos.findByWorkTypeAndPeriod(cid, workTypeCode, dateData);
		if(lstVactionPeriod.isEmpty()) {
			//ドメインモデル「計画休暇のルールの履歴」を全て取得する
			List<PlanVacationHistory> lstVacation = vacationHisRepos.findByWorkTypeCode(cid, workTypeCode);
			if(lstVacation.isEmpty()) {
				return lstOutput;
			} else {
				//「期間外の利用」を「計画年休の上限チェックの結果」．エラー情報に追加する
				lstOutput.add(PlanVacationRuleError.OUTSIDEPERIOD);
				return lstOutput;
			}
		} else {
			//申請期間が利用可能な期間外かチェックする
			boolean isOutThePeriod = this.checkOutThePeriod(lstVactionPeriod, dateData);
			if(isOutThePeriod) {
				//「期間外の利用」を「計画年休の上限チェックの結果」．エラー情報に追加する
				lstOutput.add(PlanVacationRuleError.OUTSIDEPERIOD);
				return lstOutput;
			}
			//取得したドメインモデル「計画休暇のルールの履歴」を先頭から最後までループする
			for (PlanVacationHistory data : lstVactionPeriod) {
				//指定期間の計画年休の上限チェック
				DatePeriod dateCheck = new DatePeriod(data.start(), data.end());
				CheckMaximumOfPlanParam planParam = new CheckMaximumOfPlanParam(cid, employeeId, workTypeCode, dateCheck, data.getMaxDay().v(), dateData);
				if(this.checkMaxPlanSpecification(planParam, lstDateIsHoliday)) {
					//「上限日数超過」を「計画年休の上限チェックの結果」．エラー情報に追加する
					lstOutput.add(PlanVacationRuleError.OVERLIMIT);
					return lstOutput;
				}
			}
			
			return lstOutput;
		}
		
	}
	@Override
	public boolean checkOutThePeriod(List<PlanVacationHistory> lstVactionPeriod, DatePeriod dateData) {
		boolean outData = true;
		for(int i = 0; dateData.start().daysTo(dateData.end()) - i >= 0; i++){
			GeneralDate loopDate = dateData.start().addDays(i);
			for (PlanVacationHistory planData : lstVactionPeriod) {
				//ループする日は範囲内かチェックする
				if(loopDate.afterOrEquals(planData.start())
						&& loopDate.beforeOrEquals(planData.end())) {
					outData = false;
					break;
				}
			}
			//期間外フラグをチェックする
			if(outData) {
				break;
			}
		}
		return outData;
	}
	@Override
	public boolean checkMaxPlanSpecification(CheckMaximumOfPlanParam planParam, List<GeneralDate> lstDateIsHoliday) {
		boolean outputData = false;
		//チェック期間による申請期間を編集する
		DatePeriod editDate = this.getEditDate(planParam.getDateCheck(), planParam.getAppDate());
		//取得期間の算出
		PeriodVactionCalInfor getCalByDate = this.calGetPeriod(planParam.getCid(), planParam.getEmployeeId(), planParam.getDateCheck());
		//指定する期間の計画年休使用明細を取得する
		List<GeneralDate> lstDateDetail = annualAdapter.lstDetailPeriod(planParam.getCid(), planParam.getEmployeeId(), planParam.getWorkTypeCode(), getCalByDate);
		//使用済の計画年休日数を取得する
		int useDay = this.getUseDays(lstDateDetail, editDate);
		GeneralDate sD = editDate.start();
		GeneralDate eD = editDate.end();
		Integer appDayCount = 0;
		for(int i = 0; sD.daysTo(eD) - i >= 0; i++){
			GeneralDate loopDate = sD.addDays(i);
			if(lstDateIsHoliday.contains(loopDate)) {
				continue;
			}
			//INPUT.申請開始日～INPUT.申請終了日の期間から、INPUT.休日の申請日Listに一致する日を除いた年月日List（Ａ）を作成する
			appDayCount += 1;
		}
		
		//(使用済の計画年休日数＋申請する計画年休日数)はINPUT．上限日数と比較する
		if(useDay + appDayCount > planParam.getMaxNumber()) {
			outputData = true;
		}
		return outputData;
	}
	
	@Override
	public DatePeriod getEditDate(DatePeriod checkData, DatePeriod appDate) {
		//申請開始日(編集後)=申請開始日、申請終了日(編集後)=申請終了日(初期化)
		GeneralDate startDate = appDate.start();
		GeneralDate endDate = appDate.end();
		//チェック期間、申請期間を比較する
		if(appDate.start().before(checkData.start())) {
			startDate = checkData.start();
		}
		if(appDate.end().after(checkData.end())) {
			endDate = checkData.end();
		}
		return new DatePeriod(startDate, endDate);
	}
	@Override
	public int getUseDays(List<GeneralDate> lstDate, DatePeriod dataDate) {
		int outputData = 0;
		if(lstDate.isEmpty()) {
			return outputData;
		}
		for (GeneralDate detailDate : lstDate) {
			//INPUT．申請開始日 <= ループ中の「使用一覧」．使用日 <= INPUT．申請終了日 false
			if(dataDate.start().after(detailDate)
					|| dataDate.end().before(detailDate)) {
				outputData += 1;
			}
		}
		return outputData;
	}
	@Override
	public PeriodVactionCalInfor calGetPeriod(String cid, String employeeId, DatePeriod dateCheck) {
		//社員所属雇用履歴を取得
		SEmpHistImport employmentBySid = employmentAdapter.getEmpHist(cid, employeeId, GeneralDate.today());
		if(employmentBySid == null) {
			return null;
		}
		//雇用に紐づく締めを取得する
		Optional<ClosureEmployment> optClosureEmplInfor = closureEmploymentService.findByEmploymentCD(cid, employmentBySid.getEmploymentCode());
		if(!optClosureEmplInfor.isPresent()) {
			return null;
		}
		ClosureEmployment closureEmploy = optClosureEmplInfor.get();
		//ドメインモデル「締め」を取得する
		Optional<Closure> optClosureInfor = closureRepository.findById(cid, closureEmploy.getClosureId());
		if(!optClosureInfor.isPresent()) {
			return null;
		}
		Closure closureInfor = optClosureInfor.get();
		//当月の期間を算出する
		DatePeriod currentDate = closureService.getClosurePeriod(closureInfor.getClosureId().value, closureInfor.getClosureMonth().getProcessingYm());
		if(currentDate == null) {
			return null;
		}
		//INPUT．チェック開始日、INPUT．チェック終了日は締め期間の開始年月日、締め期間の終了年月日を比較する
		if(dateCheck.start().afterOrEquals(currentDate.start())) {//INPUT．チェック開始日>=締め期間の開始年月日
			//日別実績取得するフラグ=false、暫定データ取得するフラグ=true
			//開始日（日別実績）=null、終了日（日別実績）=null
			//開始日（暫定）=INPUT．チェック開始日、終了日（暫定）=INPUT．チェック終了日
			return new PeriodVactionCalInfor(Optional.empty(),
					Optional.of(dateCheck),
					false,
					true);
		} else if (dateCheck.end().before(currentDate.start())) { //INPUT．チェック終了日<締め期間の開始年月日
			//日別実績取得するフラグ=true、暫定データ取得するフラグ=false
			//開始日（日別実績）=INPUT．チェック開始日、終了日（日別実績）=INPUT．チェック終了日
			//開始日（暫定）=null、終了日（暫定）=null
			return new PeriodVactionCalInfor(Optional.of(dateCheck),
					Optional.empty(),
					true, 
					false);
		} else {
			//日別実績取得するフラグ=true、暫定データ取得するフラグ=true
			//開始日（日別実績）=INPUT．チェック開始日、終了日（日別実績）=締め期間の開始年月日.AddDays(-1)
			DatePeriod recordDate = new DatePeriod(dateCheck.start(), currentDate.start().addDays(-1));
			//開始日（暫定）=締め期間の開始年月日、終了日（暫定）=INPUT．チェック終了日
			DatePeriod interimDate = new DatePeriod(currentDate.start(), dateCheck.end());
			return new PeriodVactionCalInfor(Optional.of(recordDate), 
					Optional.of(interimDate),
					true, 
					true);
		}
	}
	
	

}

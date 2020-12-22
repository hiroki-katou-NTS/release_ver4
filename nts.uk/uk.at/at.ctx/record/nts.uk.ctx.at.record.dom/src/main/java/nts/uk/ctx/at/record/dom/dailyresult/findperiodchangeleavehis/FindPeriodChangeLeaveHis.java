package nts.uk.ctx.at.record.dom.dailyresult.findperiodchangeleavehis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.affiliationinformation.WorkTypeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.shared.dom.dailyperformanceformat.businesstype.BusinessTypeOfEmpDto;
import nts.uk.ctx.at.shared.dom.dailyresult.findperiodchangeleavehis.TakeLeaveEmpAndPeriodShared;
import nts.uk.ctx.at.shared.dom.dailyresult.findperiodchangeleavehis.TempAbsenceHisItemShared;
import nts.uk.ctx.at.shared.dom.worktype.WorkAtr;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeClassification;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeSet;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeUnit;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 休職休業履歴変更期間を求める
 * UKDesign.ドメインモデル.NittsuSystem.UniversalK.就業.shared(勤務予定、勤務実績).日の勤怠計算.日別勤怠.勤務情報.アルゴリズム(日別実績の勤務情報).休職休業履歴変更期間を求める
 * @author tutk
 *
 */
@Stateless
public class FindPeriodChangeLeaveHis {
	
	@Inject
	private WorkTypeRepository workTypeRepository;
	
	@Inject
	private WorkInformationRepository workRepository;
	
	/**
	 * 休職休業履歴変更期間を求める
	 * @param employeeId 社員ID
	 * @param period 処理期間
	 * @param listTakeLeaveEmpAndPeriodShared List<休職休業履歴，休職休業履歴項目> (休職休業履歴 :  hisId and datePeriod)
	 * @param recreationOfLeave 休職・休業者再作成
	 * @return
	 */
	public List<DatePeriod> findPeriodChangeLeaveHis(String employeeId, DatePeriod period,
			List<TakeLeaveEmpAndPeriodShared> listTakeLeaveEmpAndPeriodShared, boolean recreationOfLeave) {
		
		String companyId = AppContexts.user().companyId();
		List<DatePeriod> listDatePeriod = new ArrayList<>();
		if(!recreationOfLeave) {
			//INPUT「休職・休業者再作成」　=　FALSE
			listDatePeriod.add(period);
			return listDatePeriod;
		}
		//ドメインモデル「日別実績の勤務情報」を取得する 
		List<WorkInfoOfDailyPerformance> listWorkInfor = workRepository.findByPeriodOrderByYmd(employeeId, period);
		
		//「休職休業履歴差異」を作成する
		boolean checkLeaveOfAbsence = false;
		DatePeriod datePeriod = new DatePeriod(null, null);
		//INPUT．「期間」をループする'
		for(GeneralDate date : period.datesBetween()) {
			Optional<WorkInfoOfDailyPerformance> optWorkInfoOfDailyPer =  listWorkInfor.stream().filter(c->c.getYmd().equals(date)).findFirst();
			if(!optWorkInfoOfDailyPer.isPresent()) continue;
			// ドメインモデル「勤務種類」を取得する
			Optional<WorkType> workTypeOpt = this.workTypeRepository.findByDeprecated(companyId,
					optWorkInfoOfDailyPer.get().getRecordInfo().getWorkTypeCode().v());
			if (!workTypeOpt.isPresent()) continue;
			boolean comparisonResult = false;
			
			Optional<TakeLeaveEmpAndPeriodShared> takeLeaveEmpAndPeriodShared = listTakeLeaveEmpAndPeriodShared.stream().filter(c->c.getPeriod().contains(date)).findFirst();
			if (!takeLeaveEmpAndPeriodShared.isPresent()) continue;
			TempAbsenceHisItemShared tempAbsenceHisItemShared = takeLeaveEmpAndPeriodShared.get().getTempAbsenceHisItemShared();
			// 取得したドメインモデル「勤務種類」とINPUT「休職休業履歴項目」を比較する
			if (workTypeOpt.get().getDailyWork().getWorkTypeUnit() == WorkTypeUnit.OneDay
					&& workTypeOpt.get().getDailyWork().getOneDay() == WorkTypeClassification.LeaveOfAbsence) {
				if(tempAbsenceHisItemShared.getTempAbsenceFrNo() != 1) {
					comparisonResult = true;
				}
			}else if (workTypeOpt.get().getDailyWork().getWorkTypeUnit() == WorkTypeUnit.OneDay
					&& workTypeOpt.get().getDailyWork().getOneDay() == WorkTypeClassification.Closure) {
				WorkTypeSet  workTypeSet = workTypeOpt.get().getWorkTypeSetList().stream().filter(c->c.getWorkAtr() ==WorkAtr.OneDay).findFirst().get();
				if(tempAbsenceHisItemShared.getTempAbsenceFrNo() != (workTypeSet.getCloseAtr().value+2)) {
					comparisonResult = true;
				}
			}
			//比較結果 == TRUE　&&　休職休業履歴差異 = FALSE
			if(comparisonResult && !checkLeaveOfAbsence) {
				//「期間」を作成する
				datePeriod = datePeriod.newSpan(date, null);
				//「休職休業履歴差異」を更新する
				checkLeaveOfAbsence = true;
			}else if (!comparisonResult && checkLeaveOfAbsence) {//比較結果 == FALSE　&&　職場履歴差異 = TRUE
				//「期間」の終了日を更新する
				datePeriod = datePeriod.newSpan(datePeriod.start(), date.addDays(-1));
				listDatePeriod.add(datePeriod);
				checkLeaveOfAbsence = false;
			}
			
		}
		for(DatePeriod p : listDatePeriod) {
			if(p.end() == null) {
				p = p.newSpan(p.start(), period.end());
			}
		}
		
		return listDatePeriod;
	}

}

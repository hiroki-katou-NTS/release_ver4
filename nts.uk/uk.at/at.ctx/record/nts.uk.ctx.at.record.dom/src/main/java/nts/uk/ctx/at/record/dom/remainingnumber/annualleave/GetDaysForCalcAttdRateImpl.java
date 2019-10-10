package nts.uk.ctx.at.record.dom.remainingnumber.annualleave;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import lombok.val;
import nts.uk.ctx.at.record.dom.monthly.WorkTypeDaysCountTable;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.VacationAddSet;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.MonAggrCompanySettings;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.MonthlyCalculatingDailys;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.RepositoriesRequiredByMonthlyAggr;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.param.CalYearOffWorkAttendRate;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 実装：出勤率計算用日数を取得する
 * @author shuichu_ishida
 */
@Stateless
public class GetDaysForCalcAttdRateImpl implements GetDaysForCalcAttdRate {

	/** 日別実績の勤務情報の取得 */
	@Inject
	private WorkInformationRepository workInformationOfDailyRepo;
	/** 勤務情報の取得 */
	@Inject
	private WorkTypeRepository workTypeRepo;

	private static Object NullObject = new Object();
	
	/** 日別実績から出勤率計算用日数を取得 */
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public CalYearOffWorkAttendRate algorithm(String companyId, String employeeId, DatePeriod period) {
		
		double prescribedDays = 0.0;
		double workingDays = 0.0;
		double deductedDays = 0.0;
		
		Map<String, Object> workTypes = new HashMap<>();
		
		// 「日別実績の勤務情報」を取得
		val workInfos = this.workInformationOfDailyRepo.findByPeriodOrderByYmd(employeeId, period);
		for (val workInfo : workInfos){
			if (workInfo.getRecordInfo() == null) continue;
			if (workInfo.getRecordInfo().getWorkTypeCode() == null) continue;
			val workTypeCode = workInfo.getRecordInfo().getWorkTypeCode().v();
		
			// 「勤務種類」を取得
			WorkType workType = null;
			if (workTypes.containsKey(workTypeCode)){
				if (workTypes.get(workTypeCode) != NullObject) workType = (WorkType)workTypes.get(workTypeCode);
			}
			else {
				val workTypeOpt = this.workTypeRepo.findByPK(companyId, workTypeCode);
				if (workTypeOpt.isPresent()){
					workType = workTypeOpt.get();
					workTypes.putIfAbsent(workTypeCode, workType);
				}
				else {
					workTypes.putIfAbsent(workTypeCode, NullObject);
				}
			}
			if (workType == null) continue;
			
			// 勤務種類を判断してカウント数を取得
			WorkTypeDaysCountTable daysCount = new WorkTypeDaysCountTable(
					workType, new VacationAddSet(), Optional.empty());
			
			// 所定日数に取得したカウント数を加算
			prescribedDays += daysCount.getPredetermineDays().v();
			
			// 出勤率の計算方法を取得
			switch (workType.getCalculateMethod()){
			case DO_NOT_GO_TO_WORK:
				break;
			case MAKE_ATTENDANCE_DAY:
				// 労働日数に1日加算
				workingDays += 1.0;
				break;
			case EXCLUDE_FROM_WORK_DAY:
				// 控除日数に1日加算
				deductedDays += 1.0;
				break;
			case TIME_DIGEST_VACATION:
				//*****（未）　設計保留中。2018.7.26
				break;
			}
		}
		
		return new CalYearOffWorkAttendRate(0.0, prescribedDays, workingDays, deductedDays);
	}
	
	/** 日別実績から出勤率計算用日数を取得　（月別集計用） */
	@Override
	public CalYearOffWorkAttendRate algorithm(String companyId, String employeeId, DatePeriod period,
			MonAggrCompanySettings companySets, MonthlyCalculatingDailys monthlyCalcDailys,
			RepositoriesRequiredByMonthlyAggr repositories) {
		
		double prescribedDays = 0.0;
		double workingDays = 0.0;
		double deductedDays = 0.0;
		
		// 「日別実績の勤務情報」を取得
		val workInfos = monthlyCalcDailys.getWorkInfoOfDailyMap();
		for (val workInfo : workInfos.values()){
			if (!period.contains(workInfo.getYmd())) continue;
			if (workInfo.getRecordInfo() == null) continue;
			if (workInfo.getRecordInfo().getWorkTypeCode() == null) continue;
			val workTypeCode = workInfo.getRecordInfo().getWorkTypeCode().v();
		
			// 「勤務種類」を取得
			WorkType workType = companySets.getWorkTypeMap(workTypeCode, repositories);
			if (workType == null) continue;
			
			// 勤務種類を判断してカウント数を取得
			WorkTypeDaysCountTable daysCount = new WorkTypeDaysCountTable(
					workType, new VacationAddSet(), Optional.empty());
			
			// 所定日数に取得したカウント数を加算
			prescribedDays += daysCount.getPredetermineDays().v();
			
			// 出勤率の計算方法を取得
			switch (workType.getCalculateMethod()){
			case DO_NOT_GO_TO_WORK:
				break;
			case MAKE_ATTENDANCE_DAY:
				// 労働日数に1日加算
				workingDays += 1.0;
				break;
			case EXCLUDE_FROM_WORK_DAY:
				// 控除日数に1日加算
				deductedDays += 1.0;
				break;
			case TIME_DIGEST_VACATION:
				//*****（未）　設計保留中。2018.7.26
				break;
			}
		}
		
		return new CalYearOffWorkAttendRate(0.0, prescribedDays, workingDays, deductedDays);
	}
}

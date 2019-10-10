package nts.uk.ctx.at.record.dom.statutoryworkinghours.monthly;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.adapter.workplace.affiliate.AffWorkplaceAdapter;
import nts.uk.ctx.at.record.dom.statutoryworkinghours.DailyStatutoryWorkingHours;
import nts.uk.ctx.at.shared.dom.common.MonthlyEstimateTime;
import nts.uk.ctx.at.shared.dom.statutory.worktime.UsageUnitSettingRepository;
import nts.uk.ctx.at.shared.dom.statutory.worktime.companyNew.ComFlexSettingRepository;
import nts.uk.ctx.at.shared.dom.statutory.worktime.employeeNew.ShainFlexSettingRepository;
import nts.uk.ctx.at.shared.dom.statutory.worktime.employmentNew.EmpFlexSettingRepository;
import nts.uk.ctx.at.shared.dom.statutory.worktime.sharedNew.MonthlyUnit;
import nts.uk.ctx.at.shared.dom.statutory.worktime.sharedNew.WorkingTimeSetting;
import nts.uk.ctx.at.shared.dom.statutory.worktime.workplaceNew.WkpFlexSettingRepository;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;

@Stateless
public class MonthlyStatutoryWorkingHoursImpl implements MonthlyStatutoryWorkingHours{
	
	@Inject
	private UsageUnitSettingRepository usageUnitSettingRepository;
	
	@Inject
	private DailyStatutoryWorkingHours dailyStatutoryWorkingHours;
	
	@Inject
	private GetShainMonthlyLaborTime getShainMonthlySetting;
	
	@Inject
	private GetWorkingPlaceMonthlyLaborTime getWorkingPlaceMonthlyLaborTime;
	
	@Inject
	private GetEmploymentMonthlyLaborTime getEmploymentMonthlyLaborTime;
	
	@Inject
	private GetCompanyMonthlyLaborTime getCompanyMonthlyLaborTime;
	
	@Inject
	private AffWorkplaceAdapter affWorkplaceAdapter;
	
	@Inject
	private ShainFlexSettingRepository shainFlexSettingRepository;
	
	@Inject
	private WkpFlexSettingRepository wkpFlexSettingRepository;
	
	@Inject
	private EmpFlexSettingRepository empFlexSettingRepository;
	
	@Inject
	private ComFlexSettingRepository comFlexSettingRepository;
	
	/**
	 * 週、月の法定労働時間を取得(通常、変形用)
	 * @return
	 */
	@Override
	public Optional<MonAndWeekStatutoryTime> getMonAndWeekStatutoryTime(String companyId,
			  												  String employmentCd,
			  												  String employeeId,
			  												  GeneralDate baseDate,
			  												  YearMonth yearMonth,
			  												  WorkingSystem workingSystem) {
		//enum<労働制> = フレックス勤務、計算対象外　の場合
		if (workingSystem.isFlexTimeWork() || workingSystem.isExcludedWorkingCalculate()) {
			return Optional.empty();
		}
		
		//週の時間を取得
		MonthlyEstimateTime weeklyTime = getWeeklylyEstimateTime(companyId,employmentCd,employeeId,baseDate,workingSystem);
		//月の時間を取得
		MonthlyEstimateTime monthlyTime = getMonthlyEstimateTime(companyId,employmentCd,employeeId,baseDate,yearMonth,workingSystem);
		
		return Optional.of(new MonAndWeekStatutoryTime(weeklyTime,monthlyTime));

	}
	
	/**
	 * 週、月の法定労働時間を取得(フレックス用)
	 * @return
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public MonthlyFlexStatutoryLaborTime getFlexMonAndWeekStatutoryTime(String companyId,
			  															String employmentCd,
			  															String employeeId,
			  															GeneralDate baseDate,
			  															YearMonth yearMonth
			  															) {
		//取得処理
		val list = getFlexMonthlyUnit(companyId,employmentCd,employeeId,baseDate,yearMonth);
		if(!list.isPresent()) {
			return MonthlyFlexStatutoryLaborTime.zeroMonthlyFlexStatutoryLaborTime();
		}
		MonthlyUnit statutorySetting = list.get().getStatutoryList().stream().filter(m -> m.getMonth().v() == yearMonth.month()).findFirst().orElse(null);
		MonthlyUnit specifiedSetting = list.get().getSpecifiedList().stream().filter(m -> m.getMonth().v() == yearMonth.month()).findFirst().orElse(null);
		MonthlyEstimateTime statutorytime = new MonthlyEstimateTime(0);
		MonthlyEstimateTime specified = new MonthlyEstimateTime(0);
		if(statutorySetting!=null) {
			statutorytime = statutorySetting.getMonthlyTime();
		}
		if(specifiedSetting!=null) {
			specified = specifiedSetting.getMonthlyTime();
		}
		return new MonthlyFlexStatutoryLaborTime(statutorytime,specified);
	}
	
	
	/**
	 * 週の時間を取得
	 * @return
	 */
	public MonthlyEstimateTime getWeeklylyEstimateTime(String companyId,
			  										   String employmentCd,
			  										   String employeeId,
			  										   GeneralDate baseDate,
			  										   WorkingSystem workingSystem
			  										   ) {
		Optional<WorkingTimeSetting> weeklyTime = dailyStatutoryWorkingHours.getWorkingTimeSetting(companyId, employmentCd, employeeId, baseDate, workingSystem);
		//取得に失敗した場合
		if(!weeklyTime.isPresent()) {
			return new MonthlyEstimateTime(0);
		}
		return new MonthlyEstimateTime(weeklyTime.get().getWeeklyTime().getTime().valueAsMinutes());
	}
	
	
	/**
	 * 月の時間を取得
	 * @return
	 */
	public MonthlyEstimateTime getMonthlyEstimateTime(String companyId,
			  										  String employmentCd,
			  										  String employeeId,
			  										  GeneralDate baseDate,
			  										  YearMonth yearMonth,
			  										  WorkingSystem workingSystem) {
		
		MonthlyUnit monthlyUnit = getMonthlyUnit(companyId,
												 employmentCd,
												 employeeId,
												 baseDate,
												 yearMonth,
												 workingSystem).stream()
															   .filter(m -> m.getMonth().v() == yearMonth.month())
															   .findFirst().orElse(null);
		//取得に失敗した場合
		if(monthlyUnit==null) {
			return new MonthlyEstimateTime(0);
		}																	
		return monthlyUnit.getMonthlyTime();
	}
	
	
	
	/**
	 * 取得する単位を取得
	 * @return
	 */
	public List<MonthlyUnit> getMonthlyUnit(String companyId,
											String employmentCd,
											String employeeId,
											GeneralDate baseDate,
											YearMonth yearMonth,
											WorkingSystem workingSystem){
		
		/*労働時間と日数の設定の利用単位の設定*/
		val usageUnitSetting = usageUnitSettingRepository.findByCompany(companyId);
		if (!usageUnitSetting.isPresent()) {
			return new ArrayList<>();
		}
	
		if(usageUnitSetting.get().isEmployee()) {//社員の労働時間を管理する場合
			val result = getShainMonthlySetting.getShainWorkingTimeSetting(companyId,employeeId,workingSystem,yearMonth);
			if(!result.isEmpty()) {
				return result;
			}
		}
		if(usageUnitSetting.get().isWorkPlace()) {//職場の労働時間を管理する場合
			//職場の設定を取得
			val result = getWorkingPlaceMonthlyLaborTime.getWkpWorkingTimeSetting(companyId,employeeId,baseDate,yearMonth,workingSystem);
			if(!result.isEmpty()) {
				return result;
			}
		}
		if(usageUnitSetting.get().isEmployment()) {//雇用の労働時間を管理する場合
			//雇用別設定の取得
			val result = getEmploymentMonthlyLaborTime.getEmpWorkingTimeSetting(companyId,employmentCd,yearMonth,workingSystem);
			if(!result.isEmpty()) {
				return result;
			}
		}
		//会社別設定の取得
		return getCompanyMonthlyLaborTime.getComWorkingTimeSetting(companyId,yearMonth,workingSystem);
	}
	
	/**
	 * 取得処理(フレックス)
	 * @return
	 */
	public Optional<MonthlyFlexStatutoryLaborTimeList> getFlexMonthlyUnit(String companyId,
																		  String employmentCd,
																		  String employeeId,
																		  GeneralDate baseDate,
																		  YearMonth yearMonth) {
		
		/*労働時間と日数の設定の利用単位の設定*/
		val usageUnitSetting = usageUnitSettingRepository.findByCompany(companyId);
		if (usageUnitSetting.isPresent()) {
			//取得処理
			if(usageUnitSetting.get().isEmployee()) {//社員の労働時間を管理する場合
				val result = shainFlexSettingRepository.find(companyId, employeeId, yearMonth.year());
				if(result.isPresent()) {
					return Optional.of(new MonthlyFlexStatutoryLaborTimeList(result.get().getSpecifiedSetting(),result.get().getStatutorySetting()));
				}
			}
			if(usageUnitSetting.get().isWorkPlace()) {//職場の労働時間を管理する場合
				//所属職場を含む上位階層の職場IDを取得
				List<String> workPlaceIdList = affWorkplaceAdapter.findAffiliatedWorkPlaceIdsToRoot(companyId, employeeId, baseDate);
				for(String workPlaceId:workPlaceIdList) {
					val result = wkpFlexSettingRepository.find(companyId, workPlaceId, yearMonth.year());
					if(result.isPresent()) {
						return Optional.of(new MonthlyFlexStatutoryLaborTimeList(result.get().getSpecifiedSetting(),result.get().getStatutorySetting()));
					}
				}
			}
			if(usageUnitSetting.get().isEmployment()) {//雇用の労働時間を管理する場合
				//雇用別設定の取得
				val result = empFlexSettingRepository.find(companyId, employmentCd, yearMonth.year());
				if(result.isPresent()) {
					return Optional.of(new MonthlyFlexStatutoryLaborTimeList(result.get().getSpecifiedSetting(),result.get().getStatutorySetting()));
				}
			}
			//会社別設定の取得
			val result = comFlexSettingRepository.find(companyId, yearMonth.year());
			if(result.isPresent()) {
				return Optional.of(new MonthlyFlexStatutoryLaborTimeList(result.get().getSpecifiedSetting(),result.get().getStatutorySetting()));
			}
		}	
		return Optional.empty();
	}
	
	
}

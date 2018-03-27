package nts.uk.ctx.at.shared.dom.workrule.statutoryworktime;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.common.DailyTime;
import nts.uk.ctx.at.shared.dom.statutory.worktime.UsageUnitSettingRepository;
import nts.uk.ctx.at.shared.dom.statutory.worktime.company.CompanyWtSetting;
import nts.uk.ctx.at.shared.dom.statutory.worktime.company.CompanyWtSettingRepository;
import nts.uk.ctx.at.shared.dom.statutory.worktime.employee.EmployeeWtSettingRepository;
import nts.uk.ctx.at.shared.dom.statutory.worktime.employment.EmploymentWtSetting;
import nts.uk.ctx.at.shared.dom.statutory.worktime.employment.EmploymentWtSettingRepository;
import nts.uk.ctx.at.shared.dom.statutory.worktime.workplace.WorkPlaceWtSetting;
import nts.uk.ctx.at.shared.dom.statutory.worktime.workplace.WorkPlaceWtSettingRepository;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.ctx.at.shared.dom.workrule.statutoryworktime.DailyCalculationPersonalInformation;
import nts.uk.ctx.at.shared.dom.workrule.statutoryworktime.StatutoryWorkTimeSet;

/**
 * 法定労働時間の取得
 * @author keisuke_hoshina
 *
 */
@Stateless
public class GetOfStatutoryWorkTime {
	
	@Inject
	private UsageUnitSettingRepository usageUnitSettingRepository;
	
	@Inject
	private CompanyWtSettingRepository companyWtSettingRepository;
	
	@Inject
	private WorkPlaceWtSettingRepository workPlaceWtSettingRepository;
	
	@Inject
	private EmploymentWtSettingRepository employmentWtSettingRepository;
	
	@Inject
	private EmployeeWtSettingRepository employeeWtSettingRepository;


	
	/**
	 * 法定労働時間の取得
	 * @return 日別計算用の社員情報
	 */
	public DailyCalculationPersonalInformation getDailyTimeFromStaturoyWorkTime(WorkingSystem workingSystem,String companyId,String placeId,String employmentCd,String employeerId,GeneralDate targetDate) {	
		try {
			return getStatutoryWorkTime(workingSystem,companyId,placeId,employmentCd,employeerId,targetDate);
		}
		catch(RuntimeException e){
			return new DailyCalculationPersonalInformation(Optional.of(new DailyTime(0)), new DailyTime(0), workingSystem);
		}
	}
	
	/**
	 * 取得処理
	 * @param 労働制
	 */
	private DailyCalculationPersonalInformation getStatutoryWorkTime(WorkingSystem workingSystem,String companyId,String placeId,String employmentCd,String employeerId,GeneralDate targetDate) {
		if(workingSystem.isFlexTimeWork()) {
			return TimeOfDailyForFixedAndFluid(workingSystem,companyId,placeId,employmentCd,employeerId,targetDate);
		}
		else {
			return TimeOfDailyForFlex(workingSystem,companyId,placeId,employmentCd,employeerId,targetDate);
		}
	}
	
	/**
	 * 1日の時間取得する(固定・変動用)
	 * @param workingSystem 労働制
	 * @param companyId 会社ID
	 * @param placeId 職場ID
	 * @param managementCd　雇用コード
	 * @param employeerId 社員ID
	 * @return
	 */
	private DailyCalculationPersonalInformation TimeOfDailyForFixedAndFluid(WorkingSystem workingSystem,String companyId,String placeId,String employmentCd,String employeerId,GeneralDate targetDate) {
		switch(workingSystem) {
		case REGULAR_WORK:
		case VARIABLE_WORKING_TIME_WORK:
			Optional<StatutoryWorkTimeSet> statutoryWorkTimeSet = statutoryWorkTimeSet(companyId,placeId,employmentCd,employeerId,targetDate);
			if(statutoryWorkTimeSet.isPresent()) {
				if(workingSystem.isRegularWork()) {
					return new DailyCalculationPersonalInformation(Optional.empty(),statutoryWorkTimeSet.get().getFixedSet().getStatutorySetting().getDaily(),workingSystem);
				}
				else {
					return new DailyCalculationPersonalInformation(Optional.empty(),statutoryWorkTimeSet.get().getFixedSet().getStatutorySetting().getDaily(),workingSystem);
				}
			}
			return new DailyCalculationPersonalInformation(Optional.of(new DailyTime(0)),new DailyTime(0),workingSystem);
		case EXCLUDED_WORKING_CALCULATE:
		case FLEX_TIME_WORK:
			return new DailyCalculationPersonalInformation(Optional.of(new DailyTime(0)),new DailyTime(0),workingSystem);
		default:
			throw new RuntimeException("unknown workingSystem" + workingSystem);	
		}
	}
	
	/**
	 * 1日の時間取得する(フレックス用)
	 * @param workingSystem 労働制
	 * @param companyId 会社ID
	 * @param placeId 職場ID
	 * @param managementCd　雇用コード
	 * @param employeerId 社員ID
	 * @return
	 */
	private DailyCalculationPersonalInformation TimeOfDailyForFlex(WorkingSystem workingSystem,String companyId,String placeId,String employmentCd,String employeerId,GeneralDate targetDate) {
		switch(workingSystem) {
		case REGULAR_WORK:
		case VARIABLE_WORKING_TIME_WORK:
		case EXCLUDED_WORKING_CALCULATE:
			return new DailyCalculationPersonalInformation(Optional.of(new DailyTime(0)),new DailyTime(0),workingSystem);
		case FLEX_TIME_WORK:
			Optional<StatutoryWorkTimeSet> statutoryWorkTimeSet = statutoryWorkTimeSet(companyId,placeId,employmentCd,employeerId,targetDate);
			if(statutoryWorkTimeSet.isPresent()) {
				return new DailyCalculationPersonalInformation(Optional.of(statutoryWorkTimeSet.get().getFlexSet().getSpecifiedSetting().getDaily()),
													    statutoryWorkTimeSet.get().getFlexSet().getStatutorySetting().getDaily()
													    ,workingSystem);
			}
			return new DailyCalculationPersonalInformation(Optional.of(new DailyTime(0)),new DailyTime(0),workingSystem);
			
		default:
			throw new RuntimeException("unknown workingSystem" + workingSystem);	
		}
	}

	/**
	 * 労働時間設定を見て、法定労働時間設定の取得
	 * @param companyId 会社ID
	 * @param workPlaceId 職場ID
	 * @param employmentCd 雇用コード
	 * @param employeerId 社員ID
	 * @param targetDate 対象日
	 * @return 法定労働時間設定
	 */
	private Optional<StatutoryWorkTimeSet> statutoryWorkTimeSet(String companyId,String workPlaceId,String employmentCd,String employeerId,GeneralDate targetDate){
		val setting = usageUnitSettingRepository.findByCompany(companyId);
		if(setting.get().isEmployee()) {
			return Optional.of(StatutoryWorkTimeSet.createFromemployee(employeeWtSettingRepository.find(companyId)));
		}
		else if(setting.get().isEmployment()) {
			Optional<EmploymentWtSetting> employeeMent= employmentWtSettingRepository.find(companyId, targetDate.year(), employmentCd);
			if(employeeMent.isPresent()) {
				return Optional.of(StatutoryWorkTimeSet.createFromemployment(employeeMent.get()));
			}
			return Optional.empty();
			
		}
		else if(setting.get().isWorkPlace()) {
			Optional<WorkPlaceWtSetting> workPlace = workPlaceWtSettingRepository.find(companyId, targetDate.year(), workPlaceId);
			if(workPlace.isPresent()) {
				return Optional.of(StatutoryWorkTimeSet.createFromWorkPlace(workPlace.get()));
			}
			return Optional.empty();
		}
		else {
			Optional<CompanyWtSetting> companyWtSetting = companyWtSettingRepository.find(companyId, targetDate.year());
			if(companyWtSetting.isPresent()) {
				return Optional.of(StatutoryWorkTimeSet.createFromCompany(companyWtSetting.get()));
			}
			return Optional.empty();
		} 
	}
}

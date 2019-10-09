package nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.processten;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

//import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.adapter.employment.BsEmploymentHistoryImport;
import nts.uk.ctx.at.shared.dom.adapter.employment.ShareEmploymentAdapter;
import nts.uk.ctx.at.shared.dom.vacation.setting.ManageDistinct;
import nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.AnnualPaidLeaveSetting;
import nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.AnnualPaidLeaveSettingRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.MaxDayReference;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensLeaveComSetRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensLeaveEmSetRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensatoryLeaveComSetting;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensatoryLeaveEmSetting;
import nts.uk.ctx.at.shared.dom.vacation.setting.retentionyearly.EmploymentSettingRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.retentionyearly.EmptYearlyRetentionSetting;
import nts.uk.ctx.at.shared.dom.vacation.setting.retentionyearly.RetentionYearlySetting;
import nts.uk.ctx.at.shared.dom.vacation.setting.retentionyearly.RetentionYearlySettingRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.ComSubstVacation;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.ComSubstVacationRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.EmpSubstVacation;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.EmpSubstVacationRepository;

@Stateless
public class AbsenceTenProcessImpl implements AbsenceTenProcess{
	@Inject
	private AnnualPaidLeaveSettingRepository annualPaidLeaveSettingRepository;
	@Inject
	private ShareEmploymentAdapter employeeAdaptor;
	@Inject
	private CompensLeaveEmSetRepository compensLeaveEmSetRepository;
	@Inject
	private CompensLeaveComSetRepository compensLeaveComSetRepository;
	@Inject
	private EmpSubstVacationRepository empSubsRepos;
	@Inject
	private ComSubstVacationRepository repoComSubVaca;
	@Inject
	private EmploymentSettingRepository repoEmpSet;
//	@Inject
//	private AnnualPaidLeaveSettingRepository repoAnnualSet;
	@Inject
	private RetentionYearlySettingRepository retentionYearlySetRepo;
	// 10-1.年休の設定を取得する
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public AnnualHolidaySetOutput getSettingForAnnualHoliday(String companyID){
		AnnualHolidaySetOutput annualHoliday = new AnnualHolidaySetOutput();
		// ドメインモデル「年休設定」を取得する(lấy dữ liệu domain 「年休設定」)
		AnnualPaidLeaveSetting annualPaidLeave = this.annualPaidLeaveSettingRepository.findByCompanyId(companyID);
		if(annualPaidLeave == null){
			return new AnnualHolidaySetOutput(false,false,0,false,false,0,0);
		}
		if(annualPaidLeave.getYearManageType() != null && annualPaidLeave.getYearManageType().equals(ManageDistinct.NO)){
			// ドメインモデル「年休設定」．年休管理区分 = 管理しない
						annualHoliday.setYearHolidayManagerFlg(false);
						annualHoliday.setHalfManageCategoryFlg(false);
						annualHoliday.setHalfHolidayLitmit(0);
						annualHoliday.setSuspensionTimeYearFlg(false);
						annualHoliday.setHoursHolidayType(false);
						annualHoliday.setHoursHolidayLimit(0);
						annualHoliday.setTimeYearRest(1);
						return annualHoliday;
		}
		if(annualPaidLeave.getYearManageType() != null && annualPaidLeave.getYearManageType().equals(ManageDistinct.YES)){
			// ドメインモデル「年休設定」．年休管理区分 = 管理する
			annualHoliday.setYearHolidayManagerFlg(true);
			// ドメインモデル「半日年休管理」．管理区分をチェックする(kiểm tra domain 「半日年休管理」．管理区分)
			if(annualPaidLeave.getManageAnnualSetting().getHalfDayManage().manageType.equals(ManageDistinct.YES)){
				annualHoliday.setHalfManageCategoryFlg(true);
				// ドメインモデル「半日年休管理」．参照先をチェックする(kiểm tra domain 「半日年休管理」．参照先)
				if(annualPaidLeave.getManageAnnualSetting().getHalfDayManage().reference.equals(MaxDayReference.CompanyUniform)){
					// ドメインモデル「半日年休管理」．参照先 = 会社一律
					if(annualPaidLeave.getManageAnnualSetting().getHalfDayManage().maxNumberUniformCompany != null){
						annualHoliday.setHalfHolidayLitmit(annualPaidLeave.getManageAnnualSetting().getHalfDayManage().maxNumberUniformCompany.v());
					}
				}else{
					// ドメインモデル「半日年休管理」．参照先 = 年休付与テーブルを参照 : TODO
				}
			}else{
				annualHoliday.setHalfManageCategoryFlg(false);
				annualHoliday.setHalfHolidayLitmit(0);
			}
			// ドメインモデル「時間年休管理設定」．時間年休管理区分をチェックする(kiểm tra domain「時間年休管理設定」．時間年休管理区分)
			if(annualPaidLeave.getTimeSetting().getTimeManageType().equals(ManageDistinct.NO)){
				// ドメインモデル「時間年休管理設定」．時間年休管理区分 = 管理しない
				annualHoliday.setSuspensionTimeYearFlg(false);
				annualHoliday.setHoursHolidayLimit(0);
				annualHoliday.setTimeYearRest(1);
			}else{
				// ドメインモデル「時間年休管理設定」．時間年休管理区分 = 管理する
				annualHoliday.setSuspensionTimeYearFlg(true);
				annualHoliday.setTimeYearRest(annualPaidLeave.getTimeSetting().getTimeUnit().value);
				// ドメインモデル「時間年休の上限日数」．参照先をチェックする(kiêm tra domain 「時間年休の上限日数」．参照先)
				if(annualPaidLeave.getTimeSetting().getMaxYearDayLeave().reference.equals(MaxDayReference.CompanyUniform)){
					// ドメインモデル「時間年休の上限日数」．参照先 = 会社一律
					annualHoliday.setHoursHolidayLimit(annualPaidLeave.getTimeSetting().getMaxYearDayLeave().maxNumberUniformCompany.v());
				}else{
					// ドメインモデル「時間年休の上限日数」．参照先 = 年休付与テーブルを参照: TODO
				}
				
			}
		}
		return annualHoliday;
	}
	@Override
	// 10.2
	public SubstitutionHolidayOutput getSettingForSubstituteHoliday(String companyID, String employeeID,
			GeneralDate baseDate) {
		SubstitutionHolidayOutput result = new SubstitutionHolidayOutput();
		// アルゴリズム「社員所属雇用履歴を取得」を実行する(thực hiện xử lý 「社員所属雇用履歴を取得」)
		Optional<BsEmploymentHistoryImport> empHistImport = employeeAdaptor.findEmploymentHistory(companyID, employeeID, baseDate);
		if(!empHistImport.isPresent() || empHistImport.get().getEmploymentCode()==null){
			return null;
		}
		int isManageByTime = 0;
		int digestiveUnit = 0;
		// ドメインモデル「雇用の代休管理設定」を取得する(lấy domain 「雇用の代休管理設定」)
		CompensatoryLeaveEmSetting compensatoryLeaveEmSet = this.compensLeaveEmSetRepository.find(companyID, empHistImport.get().getEmploymentCode());
		if(compensatoryLeaveEmSet != null){
			if (compensatoryLeaveEmSet.getIsManaged() != null) {
				// １件以上取得できた(lấy được 1 data trở lên)
				if (compensatoryLeaveEmSet.getIsManaged().equals(ManageDistinct.YES)) {
					// ドメインモデル「雇用の代休管理設定」．管理区分 = 管理する
					result.setSubstitutionFlg(true);
					result.setExpirationOfsubstiHoliday(
							compensatoryLeaveEmSet.getCompensatoryAcquisitionUse().getExpirationTime().value);
					isManageByTime = compensatoryLeaveEmSet.getCompensatoryDigestiveTimeUnit()
							.getIsManageByTime().value;
					digestiveUnit = compensatoryLeaveEmSet.getCompensatoryDigestiveTimeUnit().getDigestiveUnit().value;
				} else {
					// ドメインモデル「雇用の代休管理設定」．管理区分 = 管理しない
					result.setSubstitutionFlg(false);
					result.setTimeOfPeriodFlg(false);
				}
			}
		}else{
			// ０件(0 data), ドメインモデル「代休管理設定」を取得する(lấy dữ liệu domain 「代休管理設定」)
			CompensatoryLeaveComSetting compensatoryLeaveComSet = this.compensLeaveComSetRepository.find(companyID);
			if(compensatoryLeaveComSet != null){
				if (compensatoryLeaveComSet.isManaged()) {
					// ドメインモデル「代休管理設定」．管理区分 = 管理する
					result.setSubstitutionFlg(true);
					result.setExpirationOfsubstiHoliday(
							compensatoryLeaveComSet.getCompensatoryAcquisitionUse().getExpirationTime().value);
					isManageByTime = compensatoryLeaveComSet.getCompensatoryDigestiveTimeUnit()
							.getIsManageByTime().value;
					digestiveUnit = compensatoryLeaveComSet.getCompensatoryDigestiveTimeUnit().getDigestiveUnit().value;
				}
			}else{
				//ドメインモデル「代休管理設定」．管理区分 = 管理しない
				result.setSubstitutionFlg(false);
				result.setTimeOfPeriodFlg(false);
			}
		}
		// 代休管理区分をチェックする(kiểm tra 代休管理区分)
		if(result.isSubstitutionFlg()){
			// ドメインモデル「時間代休の消化単位」．管理区分をチェックする(kiểm tra domain 「時間代休の消化単位」．管理区分)
			if(isManageByTime == 1){
				result.setTimeOfPeriodFlg(true);
				result.setDigestiveUnit(digestiveUnit);
			}else{
				result.setTimeOfPeriodFlg(false);
			}
		}
		return result;
	}
	/**
	 * @author hoatt
	 * 10-3.振休の設定を取得する
	 * @param companyID
	 * @param employeeID
	 * @param baseDate
	 * @return
	 */
	@Override
	public LeaveSetOutput getSetForLeave(String companyID, String employeeID, GeneralDate baseDate) {
		boolean subManageFlag = false;
		int expirationOfLeave = 0;
		// アルゴリズム「社員所属雇用履歴を取得」を実行する
		Optional<BsEmploymentHistoryImport> empHistImport = employeeAdaptor.findEmploymentHistory(companyID, employeeID, baseDate);
		if(empHistImport.isPresent()){
			//ドメインモデル「雇用振休管理設定」を取得する(láy dữ liệu domain 「雇用振休管理設定」)
			Optional<EmpSubstVacation> optEmpSubData = empSubsRepos.findById(companyID, empHistImport.get().getEmploymentCode());
			if(optEmpSubData.isPresent()){//１件以上取得できた(1data trở lên)
				//ドメインモデル「雇用振休管理設定」．管理区分をチェックする(kiểm tra domain 「雇用振休管理設定」．管理区分)
				EmpSubstVacation empSubData = optEmpSubData.get();
				if(empSubData.getSetting().getIsManage().equals(ManageDistinct.YES)){
					subManageFlag = true;
					//振休使用期限=ドメインモデル「振休管理設定」．「振休取得・使用方法」．休暇使用期限
					expirationOfLeave = empSubData.getSetting().getExpirationDate().value;
				}
			}else{//０件(0 data)
				//ドメインモデル「振休管理設定」を取得する(lấy dữ liệu domain 「振休管理設定」)
				Optional<ComSubstVacation>  comSub = repoComSubVaca.findById(companyID);
				if(comSub.isPresent()){
					ComSubstVacation comSubSet = comSub.get();
					if(comSubSet.isManaged()){
						subManageFlag = true;
						//振休使用期限=ドメインモデル「振休管理設定」．「振休取得・使用方法」．休暇使用期限
						expirationOfLeave = comSubSet.getSetting().getExpirationDate().value;
					}
				}
			}
		}
		return new LeaveSetOutput(subManageFlag, expirationOfLeave);
	}
	/**
	 * @author hoatt
	 * 10-4.積立年休の設定を取得する
	 * @param companyID
	 * @param employeeID
	 * @param baseDate
	 * @return
	 */
	@Override
	public boolean getSetForYearlyReserved(String companyID, String employeeID, GeneralDate baseDate) {
		// アルゴリズム「社員所属雇用履歴を取得」を実行する
		Optional<BsEmploymentHistoryImport> empHist = employeeAdaptor.findEmploymentHistory(companyID, employeeID, baseDate);
		if(empHist.isPresent()){
			BsEmploymentHistoryImport emp = empHist.get();
			//ドメインモデル「雇用積立年休設定」を取得する(lấy dữ liệu domain 「雇用積立年休設定」)
			Optional<EmptYearlyRetentionSetting> empRet = repoEmpSet.find(companyID, emp.getEmploymentCode());
			if(empRet.isPresent()){//１件以上取得できた(dữ liệu lấy được từ 1 trở lên)
				//ドメインモデル「雇用積立年休設定」．管理区分をチェックする(check domain 「雇用積立年休設定」．管理区分)
				return empRet.get().getManagementCategory().equals(ManageDistinct.YES) ? true : false;
			}
			//０件(0 dữ liệu)
			//RedMine#98195　EA修正履歴No.2406
			//ドメインモデル「積立年休設定」を取得する(lấy domain 「積立年休設定」)
			Optional<RetentionYearlySetting> retYearSet = retentionYearlySetRepo.findByCompanyId(companyID);
			if(retYearSet.isPresent()){
				return retYearSet.get().getManagementCategory().value == 1 ? true : false;
			}
		}
		return false;
	}
}

package nts.uk.ctx.at.record.dom.standardtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.adapter.classification.affiliate.AffClassificationSidImport;
import nts.uk.ctx.at.record.dom.adapter.employment.SyEmploymentImport;
import nts.uk.ctx.at.record.dom.standardtime.enums.LaborSystemtAtr;
import nts.uk.ctx.at.record.dom.standardtime.enums.UseClassificationAtr;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.AlarmFourWeeks;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.AlarmOneMonth;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.AlarmOneYear;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.AlarmThreeMonths;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.AlarmTwoMonths;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.AlarmTwoWeeks;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.AlarmWeek;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.ErrorFourWeeks;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.ErrorOneMonth;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.ErrorOneYear;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.ErrorThreeMonths;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.ErrorTwoMonths;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.ErrorTwoWeeks;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.ErrorWeek;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.LimitFourWeeks;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.LimitOneMonth;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.LimitOneYear;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.LimitThreeMonths;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.LimitTwoMonths;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.LimitTwoWeeks;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.LimitWeek;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;

public class BasicAgreementSettingsGetter {
	
	private AgreementUnitSetting  unitSetting = null; 
	
	private List<AffClassificationSidImport> affClassifications = new ArrayList<>();
	private List<AgreementTimeOfClassification> agreeTimeClassifi = new ArrayList<>();
	
	private Map<GeneralDate, Map<String, List<String>>> empToWorkplaceId = new HashMap<>();
	private Map<String, AgreementTimeOfWorkPlace> agreeTimeWP = new HashMap<>();
	
	private Map<String, List<SyEmploymentImport>> employments = new HashMap<>();
	private Map<String, AgreementTimeOfEmployment> agreeTimeEmployment = new HashMap<>();
	
	private List<AgreementTimeOfCompany> agreeTimeCompany = new ArrayList<>();
	
	private Map<String, BasicAgreementSetting> basicAgreeSettings = new HashMap<>();
	
	public BasicAgreementSettingsGetter(AgreementUnitSetting unitSetting,
			List<AffClassificationSidImport> affClassifications, List<AgreementTimeOfClassification> agreeTimeClassifi,
			Map<GeneralDate, Map<String, List<String>>> empToWorkplaceId,
			Map<String, AgreementTimeOfWorkPlace> agreeTimeWP, Map<String, List<SyEmploymentImport>> employments,
			Map<String, AgreementTimeOfEmployment> agreeTimeEmployment, List<AgreementTimeOfCompany> agreeTimeCompany,
			Map<String, BasicAgreementSetting> basicAgreeSettings) {
		super();
		this.unitSetting = unitSetting;
		this.affClassifications = affClassifications;
		this.agreeTimeClassifi = agreeTimeClassifi;
		this.empToWorkplaceId = empToWorkplaceId;
		this.agreeTimeWP = agreeTimeWP;
		this.employments = employments;
		this.agreeTimeEmployment = agreeTimeEmployment;
		this.agreeTimeCompany = agreeTimeCompany;
		this.basicAgreeSettings = basicAgreeSettings;
	}

	public BasicAgreementSetting getBasicSet(String companyId, String employeeId, GeneralDate criteriaDate,
			WorkingSystem workingSystem) {
		LaborSystemtAtr laborSystemAtr = workingSystem == WorkingSystem.VARIABLE_WORKING_TIME_WORK 
				? LaborSystemtAtr.DEFORMATION_WORKING_TIME_SYSTEM : LaborSystemtAtr.GENERAL_LABOR_SYSTEM;

		if (this.unitSetting.getClassificationUseAtr() == UseClassificationAtr.USE) {
			// 分類36協定時間を取得する
			val afc = this.affClassifications.stream().filter(ac -> ac.getEmployeeId().equals(employeeId) 
					&& ac.getDateRange().contains(criteriaDate)).findFirst();
			if(afc.isPresent()){
				val atc = this.agreeTimeClassifi.stream().filter(atci -> atci.getClassificationCode().equals(afc.get().getClassificationCode()) 
						&& atci.getLaborSystemAtr() == laborSystemAtr).findFirst();
				if(atc.isPresent()){
					if(this.basicAgreeSettings.containsKey(atc.get().getBasicSettingId())) {
						return this.basicAgreeSettings.get(atc.get().getBasicSettingId());
					}
				}
			}
		}
		if (this.unitSetting.getWorkPlaceUseAtr() == UseClassificationAtr.USE) {
			Map<String, List<String>> empWp = this.empToWorkplaceId.get(criteriaDate);
			if(empWp != null && empWp.containsKey(employeeId)) {
				for(String workplaceId : empWp.get(employeeId)) {
					AgreementTimeOfWorkPlace atwp = this.agreeTimeWP.get(workplaceId);
					if(atwp != null){
						if(this.basicAgreeSettings.containsKey(atwp.getBasicSettingId())){
							return this.basicAgreeSettings.get(atwp.getBasicSettingId());
						}
					}
				};
			}
		}
		if (this.unitSetting.getEmploymentUseAtr() == UseClassificationAtr.USE){
			if(this.employments.containsKey(employeeId)) {
				val employment = this.employments.get(employeeId).stream().filter(e -> e.getPeriod().contains(criteriaDate)).findFirst();
				if(employment.isPresent() && this.agreeTimeEmployment.containsKey(employment.get().getEmploymentCode())){
					val atwp = this.agreeTimeEmployment.get(employment.get().getEmploymentCode());
					
					if(atwp != null && this.basicAgreeSettings.containsKey(atwp.getBasicSettingId())){
						return this.basicAgreeSettings.get(atwp.getBasicSettingId());
					}
				}
			}
		}
		// 会社36協定時間を取得する
		val agreementTimeOfCmpOpt = this.agreeTimeCompany.stream().filter(atc -> atc.getLaborSystemAtr() == laborSystemAtr).findFirst();
		if (agreementTimeOfCmpOpt.isPresent() && this.basicAgreeSettings.containsKey(agreementTimeOfCmpOpt.get().getBasicSettingId())){
			return this.basicAgreeSettings.get(agreementTimeOfCmpOpt.get().getBasicSettingId());
		}

		return this.getDefault();
	}
	
	private BasicAgreementSetting getDefault(){
		// 全ての値を0で返す
		return new BasicAgreementSetting(
				new String(),
				new AlarmWeek(0),
				new ErrorWeek(0),
				new LimitWeek(0),
				new AlarmTwoWeeks(0),
				new ErrorTwoWeeks(0),
				new LimitTwoWeeks(0),
				new AlarmFourWeeks(0),
				new ErrorFourWeeks(0),
				new LimitFourWeeks(0),
				new AlarmOneMonth(0),
				new ErrorOneMonth(0),
				new LimitOneMonth(0),
				new AlarmTwoMonths(0),
				new ErrorTwoMonths(0),
				new LimitTwoMonths(0),
				new AlarmThreeMonths(0),
				new ErrorThreeMonths(0),
				new LimitThreeMonths(0),
				new AlarmOneYear(0),
				new ErrorOneYear(0),
				new LimitOneYear(0));
	}
}

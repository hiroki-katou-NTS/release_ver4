package nts.uk.ctx.at.record.dom.standardtime.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.adapter.classification.affiliate.AffClassificationAdapter;
import nts.uk.ctx.at.record.dom.adapter.classification.affiliate.AffClassificationSidImport;
import nts.uk.ctx.at.record.dom.adapter.employment.SyEmploymentAdapter;
import nts.uk.ctx.at.record.dom.adapter.employment.SyEmploymentImport;
import nts.uk.ctx.at.record.dom.adapter.workplace.affiliate.AffWorkplaceAdapter;
import nts.uk.ctx.at.record.dom.standardtime.AgreementTimeOfClassification;
import nts.uk.ctx.at.record.dom.standardtime.AgreementTimeOfCompany;
import nts.uk.ctx.at.record.dom.standardtime.AgreementTimeOfEmployment;
import nts.uk.ctx.at.record.dom.standardtime.AgreementTimeOfWorkPlace;
import nts.uk.ctx.at.record.dom.standardtime.AgreementUnitSetting;
import nts.uk.ctx.at.record.dom.standardtime.BasicAgreementSetting;
import nts.uk.ctx.at.record.dom.standardtime.BasicAgreementSettingsGetter;
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
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * ドメインサービス実装：36協定
 * @author shuichu_ishida
 */
@Stateless
public class AgreementDomainServiceImpl implements AgreementDomainService {

	/** 36協定単位設定 */
	@Inject
	private AgreementUnitSettingRepository agreementUnitSetRepository;
	/** 分類36協定設定 */
	@Inject
	private AgreementTimeOfClassificationRepository agreementTimeClassRepository;
	/** 職場36協定設定 */
	@Inject
	private AgreementTimeOfWorkPlaceRepository agreementTimeWorkPlaceRepository;
	/** 雇用36協定設定 */
	@Inject
	private AgreementTimeOfEmploymentRepostitory agreementTimeEmploymentRepository;
	/** 会社36協定設定 */
	@Inject
	private AgreementTimeCompanyRepository agreementTimeCompanyRepository;
	/** 36協定基本設定 */
	@Inject
	private BasicAgreementSettingRepository basicAgreementSetRepository;
	
	/** 所属分類履歴 */
	@Inject
	private AffClassificationAdapter affClassficationAdapter;
	/** 所属職場履歴 */
	@Inject
	private AffWorkplaceAdapter affWorkplaceAdapter;
	/** 所属雇用履歴 */
	@Inject
	private SyEmploymentAdapter syEmploymentAdapter;
	
	/** 36協定基本設定を取得する */
	@Override
	public BasicAgreementSetting getBasicSet(String companyId, String employeeId, GeneralDate criteriaDate,
			WorkingSystem workingSystem) {
		
		// 「36協定単位設定」を取得する
		AgreementUnitSetting agreementUnitSet = new AgreementUnitSetting(companyId,
				UseClassificationAtr.NOT_USE, UseClassificationAtr.NOT_USE, UseClassificationAtr.NOT_USE);
		val agreementUnitSetOpt = this.agreementUnitSetRepository.find(companyId);
		if (agreementUnitSetOpt.isPresent()) agreementUnitSet = agreementUnitSetOpt.get();

		// 36協定労働制を確認する
		LaborSystemtAtr laborSystemAtr = LaborSystemtAtr.GENERAL_LABOR_SYSTEM;
		if (workingSystem == WorkingSystem.VARIABLE_WORKING_TIME_WORK){
			laborSystemAtr = LaborSystemtAtr.DEFORMATION_WORKING_TIME_SYSTEM;
		}
		
		if (agreementUnitSet.getClassificationUseAtr() == UseClassificationAtr.USE){
			
			// 分類36協定時間を取得する
			val affClassficationOpt = this.affClassficationAdapter.findByEmployeeId(companyId, employeeId, criteriaDate);
			if (affClassficationOpt.isPresent()){
				val classCd = affClassficationOpt.get().getClassificationCode();
				val basicSettingIdOpt = this.agreementTimeClassRepository.findEmploymentBasicSettingID(
						companyId, laborSystemAtr, classCd);
				if (basicSettingIdOpt.isPresent()){
					val basicAgreementSetOpt = this.basicAgreementSetRepository.find(basicSettingIdOpt.get());
					if (basicAgreementSetOpt.isPresent()) return basicAgreementSetOpt.get();
				}
			}
		}
		if (agreementUnitSet.getWorkPlaceUseAtr() == UseClassificationAtr.USE){
			
			// 職場36協定時間を取得する
			val workplaceIds = this.affWorkplaceAdapter.findAffiliatedWorkPlaceIdsToRoot(
					companyId, employeeId, criteriaDate);
			for (String workplaceId : workplaceIds){
				val basicSettingIdOpt = this.agreementTimeWorkPlaceRepository.find(workplaceId, laborSystemAtr);
				if (basicSettingIdOpt.isPresent()){
					val basicAgreementSetOpt = this.basicAgreementSetRepository.find(basicSettingIdOpt.get());
					if (basicAgreementSetOpt.isPresent()) return basicAgreementSetOpt.get();
				}
			}
		}
		if (agreementUnitSet.getEmploymentUseAtr() == UseClassificationAtr.USE){
			
			// 雇用36協定時間を取得する
			val syEmploymentOpt = this.syEmploymentAdapter.findByEmployeeId(companyId, employeeId, criteriaDate);
			if (syEmploymentOpt.isPresent()){
				val employmentCd = syEmploymentOpt.get().getEmploymentCode();
				val basicSettingIdOpt = this.agreementTimeEmploymentRepository.findEmploymentBasicSettingId(
						companyId, employmentCd, laborSystemAtr);
				if (basicSettingIdOpt.isPresent()){
					val basicAgreementSetOpt = this.basicAgreementSetRepository.find(basicSettingIdOpt.get());
					if (basicAgreementSetOpt.isPresent()) return basicAgreementSetOpt.get();
				}
			}
		}
		
		// 会社36協定時間を取得する
		val agreementTimeOfCmpOpt = this.agreementTimeCompanyRepository.find(companyId, laborSystemAtr);
		if (agreementTimeOfCmpOpt.isPresent()){
			val basicAgreementSetOpt = this.basicAgreementSetRepository.find(
					agreementTimeOfCmpOpt.get().getBasicSettingId());
			if (basicAgreementSetOpt.isPresent()) return basicAgreementSetOpt.get();
		}
		
		// 全ての値を0で返す
		return getDefault();
	}
	
	@Override
	public BasicAgreementSettingsGetter getBasicSet(String companyId, List<String> employeeIds, DatePeriod datePeriod) {
		// 「36協定単位設定」を取得する
		Map<String, BasicAgreementSetting> basicAgreeSettings = new HashMap<>();
		List<AffClassificationSidImport> affClassifications = new ArrayList<>();
		List<AgreementTimeOfClassification> agreeTimeClassifi = new ArrayList<>();
		Map<String, Map<GeneralDate, List<String>>> empToWorkplaceId = new HashMap<>();
		Map<String, AgreementTimeOfWorkPlace> agreeTimeWP = new HashMap<>();
		Map<String, List<SyEmploymentImport>> employments = new HashMap<>();
		Map<String, AgreementTimeOfEmployment> agreeTimeEmployment = new HashMap<>();
		List<AgreementTimeOfCompany> agreeTimeCompany = new ArrayList<>();
		
		AgreementUnitSetting agreementUnitSet = this.agreementUnitSetRepository.find(companyId).orElseGet(() -> new AgreementUnitSetting(companyId,
				UseClassificationAtr.NOT_USE, UseClassificationAtr.NOT_USE, UseClassificationAtr.NOT_USE));
		
		if (agreementUnitSet.getClassificationUseAtr() == UseClassificationAtr.USE){
			affClassifications = this.affClassficationAdapter.finds(companyId, employeeIds, datePeriod);
			
			agreeTimeClassifi = this.agreementTimeClassRepository.find(companyId, affClassifications.stream()
																.map(c -> c.getClassificationCode()).distinct().collect(Collectors.toList()));
			
			basicAgreeSettings.putAll(this.getBasicSetting(agreeTimeClassifi.stream().map(c -> c.getBasicSettingId()).distinct().collect(Collectors.toList())));
		}
		
		if(agreementUnitSet.getWorkPlaceUseAtr() == UseClassificationAtr.USE){
			employeeIds.stream().forEach(empId -> {
				Map<GeneralDate, List<String>> empDateToWpId = new HashMap<>();
				datePeriod.datesBetween().forEach(date -> {
					empDateToWpId.put(date, this.affWorkplaceAdapter.findAffiliatedWorkPlaceIdsToRoot(companyId, empId, date));
				});
				empToWorkplaceId.put(empId, empDateToWpId);
			});
			List<String> workplaceIds = empToWorkplaceId.entrySet().stream().map(c -> 
													c.getValue().values().stream().flatMap(List::stream).distinct().collect(Collectors.toList()))
											.flatMap(List::stream).distinct().collect(Collectors.toList());
			// 職場36協定時間を取得する
			agreeTimeWP = this.agreementTimeWorkPlaceRepository.findWorkPlaceSetting(workplaceIds)
											.stream().collect(Collectors.toMap(c -> c.getWorkplaceId(), c -> c));
			basicAgreeSettings.putAll(this.getBasicSetting(agreeTimeWP.values().stream()
																.map(c -> c.getBasicSettingId()).distinct().collect(Collectors.toList())));
		}
		
		if(agreementUnitSet.getEmploymentUseAtr() == UseClassificationAtr.USE){
			employments = this.syEmploymentAdapter.finds(employeeIds, datePeriod);
			List<String> employmentCodes = employments.entrySet().stream().map(c -> 
																c.getValue().stream().map(h -> h.getEmploymentCode()).collect(Collectors.toList()))
															.flatMap(List::stream).distinct().collect(Collectors.toList());
			
			agreeTimeEmployment = this.agreementTimeEmploymentRepository.findEmploymentSetting(companyId, employmentCodes)
															.stream().collect(Collectors.toMap(c -> c.getEmploymentCategoryCode(), c -> c));
			basicAgreeSettings.putAll(this.getBasicSetting(agreeTimeEmployment.values().stream()
																.map(c -> c.getBasicSettingId()).distinct().collect(Collectors.toList())));
		}
		
		agreeTimeCompany = this.agreementTimeCompanyRepository.find(companyId);
		basicAgreeSettings.putAll(this.getBasicSetting(agreeTimeCompany.stream()
																.map(c -> c.getBasicSettingId()).distinct().collect(Collectors.toList())));
		
		return new BasicAgreementSettingsGetter(agreementUnitSet, affClassifications, agreeTimeClassifi, empToWorkplaceId, 
				agreeTimeWP, employments, agreeTimeEmployment, agreeTimeCompany, basicAgreeSettings);
	}
	
	private Map<String, BasicAgreementSetting> getBasicSetting(List<String> setIds){
		return this.basicAgreementSetRepository.find(setIds).stream().collect(Collectors.toMap(c -> c.getBasicSettingId(), c -> c));
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

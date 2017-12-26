/**
 * 
 */
package nts.uk.ctx.pereg.app.find.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.enums.EnumConstant;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.repository.BusinessTypesRepository;
import nts.uk.ctx.at.schedule.dom.employeeinfo.TimeZoneScheduledMasterAtr;
import nts.uk.ctx.at.schedule.dom.employeeinfo.WorkScheduleBasicCreMethod;
import nts.uk.ctx.at.schedule.dom.employeeinfo.WorkScheduleMasterReferenceAtr;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.childcareschedule.ChildCareAtr;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.ctx.bs.employee.dom.classification.ClassificationRepository;
import nts.uk.ctx.bs.employee.dom.employment.Employment;
import nts.uk.ctx.bs.employee.dom.employment.EmploymentRepository;
import nts.uk.ctx.bs.employee.dom.employment.history.SalarySegment;
import nts.uk.ctx.bs.employee.dom.jobtitle.info.JobTitleInfoRepository;
import nts.uk.ctx.bs.employee.dom.temporaryabsence.frame.NotUseAtr;
import nts.uk.ctx.bs.employee.dom.temporaryabsence.frame.TempAbsenceRepositoryFrame;
import nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfoRepository;
import nts.uk.ctx.bs.person.dom.person.info.BloodType;
import nts.uk.ctx.bs.person.dom.person.info.GenderPerson;
import nts.uk.ctx.pereg.app.find.person.info.item.CodeNameRefTypeDto;
import nts.uk.ctx.pereg.app.find.person.info.item.EnumRefConditionDto;
import nts.uk.ctx.pereg.app.find.person.info.item.MasterRefConditionDto;
import nts.uk.ctx.pereg.app.find.person.info.item.SelectionItemDto;
import nts.uk.ctx.pereg.app.find.person.setting.init.item.SelectionInitDto;
import nts.uk.ctx.pereg.app.find.person.setting.selectionitem.selection.SelectionFinder;
import nts.uk.ctx.pereg.dom.person.info.selectionitem.ReferenceTypes;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.pereg.app.ComboBoxObject;

/**
 * @author danpv
 * @param <E>
 *
 */
@Stateless
public class ComboBoxRetrieveFactory {

	@Inject
	private SelectionFinder selectionFinder;

	@Inject
	private ClassificationRepository classificationRepo;

	@Inject
	private EmploymentRepository employmentRepo;

	@Inject
	private BusinessTypesRepository businessTypeRepo;

	@Inject
	private WorkTypeRepository workTypeRepo;

	@Inject
	private TempAbsenceRepositoryFrame tempAbsFrameRepo;

	@Inject
	private WorkplaceInfoRepository workPlaceRepo;

	@Inject
	private JobTitleInfoRepository jobTitleRepo;

	private static Map<String, Class<?>> enumMap;
	static {
		Map<String, Class<?>> aMap = new HashMap<>();
		// 性別
		aMap.put("E00001", GenderPerson.class);
		// 血液型
		aMap.put("E00002", BloodType.class);
		// 給与区分
		aMap.put("E00003", SalarySegment.class);
		// 育児介護区分
		aMap.put("E00004", ChildCareAtr.class);
		// 予定管理区分
		aMap.put("E00005", NotUseAtr.class);
		// 労働制
		aMap.put("E00006", WorkingSystem.class);
		// 勤務予定基本作成方法
		aMap.put("E00007", WorkScheduleBasicCreMethod.class);
		// 勤務予定作成マスタ参照区分
		aMap.put("E00008", WorkScheduleMasterReferenceAtr.class);
		// 勤務予定の時間帯マスタ参照区分
		aMap.put("E00009", TimeZoneScheduledMasterAtr.class);

		enumMap = Collections.unmodifiableMap(aMap);
	}

	private final String JP_SPACE = "　";

	@SuppressWarnings("unchecked")
	public <E extends Enum<?>> List<ComboBoxObject> getComboBox(SelectionItemDto selectionItemDto,
			GeneralDate standardDate, boolean cps001) {
		String companyId = AppContexts.user().companyId();
		switch (selectionItemDto.getReferenceType()) {
		case ENUM:
			EnumRefConditionDto enumTypeDto = (EnumRefConditionDto) selectionItemDto;
			Class<?> enumClass = enumMap.get(enumTypeDto.getEnumName());
			if (enumClass == null) {
				return new ArrayList<>();
			}
			List<EnumConstant> enumConstants = EnumAdaptor.convertToValueNameList((Class<E>) enumClass);
			return enumConstants.stream()
					.map(enumElement -> new ComboBoxObject(enumElement.getValue() + "", enumElement.getLocalizedName()))
					.collect(Collectors.toList());
		case CODE_NAME:
			CodeNameRefTypeDto codeNameTypeDto = (CodeNameRefTypeDto) selectionItemDto;
			List<SelectionInitDto> selectionList = selectionFinder
					.getAllSelectionByCompanyId(codeNameTypeDto.getTypeCode(), standardDate);
			List<ComboBoxObject> lstComboBoxValue = new ArrayList<>();
			for (SelectionInitDto selection : selectionList) {
				lstComboBoxValue.add(new ComboBoxObject(selection.getSelectionId(), selection.getSelectionName()));
			}
			return lstComboBoxValue;
		case DESIGNATED_MASTER:
			MasterRefConditionDto masterRefTypeDto = (MasterRefConditionDto) selectionItemDto;
			switch (masterRefTypeDto.getMasterType()) {

			case "M00001":
				// 部門マスタ
				break;
			case "M00002":
				// 職場マスタ
				if (cps001) {
					return workPlaceRepo.findAll(companyId, standardDate).stream()
							.map(workPlace -> new ComboBoxObject(workPlace.getWorkplaceId(),
									workPlace.getWorkplaceCode().v() + JP_SPACE + workPlace.getWorkplaceName().v()))
							.collect(Collectors.toList());

				} else {
					return workPlaceRepo.findAll(companyId, standardDate).stream()
							.map(workPlace -> new ComboBoxObject(workPlace.getWorkplaceId(),
									workPlace.getWorkplaceName().v()))
							.collect(Collectors.toList());

				}

			case "M00003":
				// 雇用マスタ
				return getEmploymentList(companyId);
			case "M00004":
				// 分類マスタ１
<<<<<<< HEAD
				if (cps001) {
					return classificationRepo
							.getAllManagementCategory(companyId).stream().map(
									classification -> new ComboBoxObject(classification.getClassificationCode().v(),
											classification.getClassificationCode().v() + JP_SPACE
													+ classification.getClassificationName().v()))
							.collect(Collectors.toList());
				} else {

					return classificationRepo.getAllManagementCategory(companyId).stream()
							.map(classification -> new ComboBoxObject(classification.getClassificationCode().v(),
									classification.getClassificationName().v()))
							.collect(Collectors.toList());
				}
=======
				return classificationRepo.getAllManagementCategory(companyId)
						.stream().map(
								classification -> new ComboBoxObject(classification.getClassificationCode().v(),
										classification.getClassificationCode().v() + JP_SPACE
												+ classification.getClassificationName().v()))
						.collect(Collectors.toList());
			case "M00005":
				// 職位マスタ
				return jobTitleRepo.findAll(companyId, standardDate).stream()
						.map(jobTitle -> new ComboBoxObject(jobTitle.getJobTitleId(),
								jobTitle.getJobTitleCode() + JP_SPACE + jobTitle.getJobTitleName().v()))
						.collect(Collectors.toList());
			case "M00006":
				// 休職休業マスタ
				return tempAbsFrameRepo.findByCid(companyId).stream()
						.filter(frame -> frame.getUseClassification() == NotUseAtr.USE)
						.map(frame -> new ComboBoxObject(frame.getTempAbsenceFrNo().v() + "",
								frame.getTempAbsenceFrName().v()))
						.collect(Collectors.toList());
			case "M00007":
				// 勤務種別マスタ
				return businessTypeRepo.findAll(companyId).stream().map(businessType -> new ComboBoxObject(
						businessType.getBusinessTypeCode().v(),
						businessType.getBusinessTypeCode().v() + JP_SPACE + businessType.getBusinessTypeName().v()))
						.collect(Collectors.toList());
			case "M00008":
				// 勤務種類マスタ
				return workTypeRepo.findByCompanyId(companyId).stream()
						.map(workType -> new ComboBoxObject(workType.getWorkTypeCode().v(),
								workType.getWorkTypeCode().v() + JP_SPACE + workType.getName().v()))
						.collect(Collectors.toList());
			case "M00009":
				// 就業時間帯マスタ
				return Arrays.asList(new ComboBoxObject("001", "固定名"));
			default:
				break;
			}
			return null;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public <E extends Enum<?>> List<ComboBoxObject> getComboBox(ReferenceTypes RefType, String RefCd,
			GeneralDate standardDate) {
		String companyId = AppContexts.user().companyId();
		switch (RefType) {
		case ENUM:
			Class<?> enumClass = enumMap.get(RefCd);
			if (enumClass == null) {
				return new ArrayList<>();
			}
			List<EnumConstant> enumConstants = EnumAdaptor.convertToValueNameList((Class<E>) enumClass);
			return enumConstants.stream()
					.map(enumElement -> new ComboBoxObject(enumElement.getValue() + "", enumElement.getLocalizedName()))
					.collect(Collectors.toList());
		case CODE_NAME:
			List<SelectionInitDto> selectionList = selectionFinder.getAllSelectionByCompanyId(RefCd, standardDate);
			List<ComboBoxObject> lstComboBoxValue = new ArrayList<>();
			for (SelectionInitDto selection : selectionList) {
				lstComboBoxValue.add(new ComboBoxObject(selection.getSelectionId(), selection.getSelectionName()));
			}
			return lstComboBoxValue;
		case DESIGNATED_MASTER:
			switch (RefCd) {
			case "M00001":
				// 部門マスタ
				break;
			case "M00002":
				// 職場マスタ
				return workPlaceRepo.findAll(companyId, standardDate).stream()
						.map(workPlace -> new ComboBoxObject(workPlace.getWorkplaceId(),
								workPlace.getWorkplaceCode().v() + JP_SPACE + workPlace.getWorkplaceName().v()))
						.collect(Collectors.toList());
			case "M00003":
				// 雇用マスタ
				return getEmploymentList(companyId);
			case "M00004":
				// 分類マスタ１
				return classificationRepo.getAllManagementCategory(companyId)
						.stream().map(
								classification -> new ComboBoxObject(classification.getClassificationCode().v(),
										classification.getClassificationCode().v() + JP_SPACE
												+ classification.getClassificationName().v()))
						.collect(Collectors.toList());
>>>>>>> c0624d91e74b62511d6c33cc4563901955292d38
			case "M00005":
				// 職位マスタ
				if (cps001) {
					return jobTitleRepo.findAll(companyId, standardDate).stream()
							.map(jobTitle -> new ComboBoxObject(jobTitle.getJobTitleId(),
									jobTitle.getJobTitleCode() + JP_SPACE + jobTitle.getJobTitleName().v()))
							.collect(Collectors.toList());
				} else {
					return jobTitleRepo.findAll(companyId, standardDate).stream().map(
							jobTitle -> new ComboBoxObject(jobTitle.getJobTitleId(), jobTitle.getJobTitleName().v()))
							.collect(Collectors.toList());

				}
			case "M00006":
				// 休職休業マスタ
				if (cps001) {
					return tempAbsFrameRepo.findByCid(companyId).stream()
							.filter(frame -> frame.getUseClassification() == NotUseAtr.USE)
							.map(frame -> new ComboBoxObject(frame.getTempAbsenceFrNo().v() + "",
									frame.getTempAbsenceFrName().v()))
							.collect(Collectors.toList());
				} else {
					return tempAbsFrameRepo.findByCid(companyId).stream()
							.filter(frame -> frame.getUseClassification() == NotUseAtr.USE)
							.map(frame -> new ComboBoxObject(frame.getTempAbsenceFrNo().v() + "",
									frame.getTempAbsenceFrName().v()))
							.collect(Collectors.toList());
				}
			case "M00007":
				// 勤務種別マスタ
				if (cps001) {
				return businessTypeRepo.findAll(companyId).stream().map(businessType -> new ComboBoxObject(
						businessType.getBusinessTypeCode().v(),
						businessType.getBusinessTypeCode().v() + JP_SPACE + businessType.getBusinessTypeName().v()))
						.collect(Collectors.toList());
				} else {
					return businessTypeRepo.findAll(companyId).stream().map(businessType -> new ComboBoxObject(
							businessType.getBusinessTypeCode().v(),
							businessType.getBusinessTypeName().v()))
							.collect(Collectors.toList());					
					
				}
			case "M00008":
				// 勤務種類マスタ
				if (cps001) {
				return workTypeRepo.findByCompanyId(companyId).stream()
						.map(workType -> new ComboBoxObject(workType.getWorkTypeCode().v(),
								workType.getWorkTypeCode().v() + JP_SPACE + workType.getName().v()))
						.collect(Collectors.toList());
				}else {
					return workTypeRepo.findByCompanyId(companyId).stream()
							.map(workType -> new ComboBoxObject(workType.getWorkTypeCode().v(),
									 workType.getName().v()))
							.collect(Collectors.toList());					
					
				}
			case "M00009":
				// 就業時間帯マスタ
				return Arrays.asList(new ComboBoxObject("001", "固定名"));
			default:
				break;
			}
			return null;
		}
		return null;
	}

	private List<ComboBoxObject> getEmploymentList(String companyId) {
		List<Employment> employments = employmentRepo.findAll(companyId);
		List<ComboBoxObject> comboBoxList = new ArrayList<>();
		for (Employment employment : employments) {
			comboBoxList.add(new ComboBoxObject(employment.getEmploymentCode().v(),
					employment.getEmploymentCode().v() + JP_SPACE + employment.getEmploymentName().v()));
		}
		return comboBoxList;
	}

}

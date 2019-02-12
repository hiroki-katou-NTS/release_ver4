package nts.uk.screen.at.app.dailyperformance.correction.datadialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumConstant;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.app.find.application.applicationlist.AppWithDetailExportDto;
import nts.uk.ctx.at.request.app.find.application.applicationlist.ApplicationListForScreen;
import nts.uk.screen.at.app.dailyperformance.correction.DailyPerformanceCorrectionProcessor;
import nts.uk.screen.at.app.dailyperformance.correction.DailyPerformanceScreenRepo;
import nts.uk.screen.at.app.dailyperformance.correction.dto.AffEmploymentHistoryDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.type.TypeLink;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.i18n.TextResource;

@Stateless
public class DataDialogWithTypeProcessor {

	@Inject
	private DailyPerformanceScreenRepo repo;
	
	@Inject
	private ApplicationListForScreen applicationListForScreen;

	// 勤務種類
	public CodeNameType getDutyType(String companyId, String workTypeCode, String employmentCode) {
		List<WorkTypeChangedDto> dtos = repo.findWorkTypeChanged(employmentCode, workTypeCode, companyId);
		Set<String> workTypeCodes = dtos.stream().map(x -> x.getTypeCode()).collect(Collectors.toSet());
//		if (!workTypeCode.equals("") && workTypeCodes.isEmpty())
//			return CodeNameType.create(TypeLink.DUTY.value, new ArrayList<>())
//					.createError(true);
		List<CodeName> codeNames = repo.findWorkType(companyId, workTypeCodes);
		return CodeNameType.create(TypeLink.DUTY.value, codeNames).createError(!workTypeCodes.isEmpty());
	}

	// 勤務種類
	public CodeNameType getDutyTypeAll(String companyId) {
		List<CodeName> codeNames = repo.findWorkType(companyId, new HashSet<>());
		return CodeNameType.create(TypeLink.DUTY.value, codeNames);
	}

	// 就業時間帯
	public CodeNameType getWorkHours(String companyId, String workplaceId) {
		List<WorkTimeWorkplaceDto> workTimeDtos = repo.findWorkHours(companyId, workplaceId);
		List<String> shiftCodes = workTimeDtos.isEmpty() ? Collections.emptyList()
				: workTimeDtos.stream().map(x -> x.getWorkTimeID()).collect(Collectors.toList());
		List<CodeName> codeNames = repo.findWorkTimeZone(companyId, shiftCodes);
		return CodeNameType.create(TypeLink.WORK_HOURS.value, codeNames);
	}

	// 就業時間帯
	public CodeNameType getWorkHoursAll(String companyId) {
		List<CodeName> codeNames = repo.findWorkTimeZone(companyId, new ArrayList<>());
		return CodeNameType.create(TypeLink.WORK_HOURS.value, codeNames);
	}

	// 勤務場所
	public CodeNameType getServicePlace(String companyId) {
		List<CodeName> codeNames = repo.findWorkplaceLocation(companyId);
		return CodeNameType.create(TypeLink.SERVICE_PLACE.value, codeNames);
	}

	// 乖離理由
	public CodeNameType getReason(String companyId) {
		List<CodeName> codeNames = repo.findReason(companyId);
		return CodeNameType.create(TypeLink.REASON.value, codeNames);
	}

	// 職場--
	public CodeNameType getWorkPlace(String companyId, GeneralDate date) {
		List<CodeName> codeNames = repo.findWorkplace(companyId, date);
		return CodeNameType.create(TypeLink.WORKPLACE.value, codeNames);
	}

	// 分類
	public CodeNameType getClassification(String companyId) {
		List<CodeName> codeNames = repo.findClassification(companyId);
		return CodeNameType.create(TypeLink.CLASSIFICATION.value, codeNames);
	}

	// 職位--
	public CodeNameType getPossition(String companyId, GeneralDate date) {
		List<CodeName> codeNames = repo.findJobInfo(companyId, date);
		return CodeNameType.create(TypeLink.POSSITION.value, codeNames);
	}

	// 雇用区分
	public CodeNameType getEmployment(String companyId) {
		List<CodeName> codeNames = repo.findEmployment(companyId);
		return CodeNameType.create(TypeLink.EMPLOYMENT.value, codeNames);
	}

	public CodeName getTypeDialog(int type, ParamDialog param) {
		String companyId = AppContexts.user().companyId();
		Optional<CodeName> codeName;
		switch (type) {
		case 1:
			// KDL002
			if (param.getItemId() != null && (param.getItemId() == 28 || param.getItemId() == 1)) {
				codeName = this.getDutyTypeAll(companyId).getCodeNames().stream()
						.filter(x -> x.getCode().equals(param.getSelectCode())).findFirst();
				if(!codeName.isPresent()) return new CodeName(param.getWorkTypeCode(), TextResource.localize("KDW003_81"), "")
						.createError(ErrorTypeWorkType.NO_GROUP.code);
				if(param.getItemId() == 1) return codeName.get().createError(ErrorTypeWorkType.MASTER.code);
				
				
				AffEmploymentHistoryDto aff = repo.getAffEmploymentHistory(companyId, param.getEmployeeId(), param.getDate());
				CodeNameType codeNameType = this.getDutyType(companyId, param.getValueOld(),
						aff == null ? "" : aff.getEmploymentCode());
				Optional<CodeName> codeNameG = codeNameType.getCodeNames().stream().filter(x -> x.getCode().equals(param.getSelectCode()))
						.findFirst();
				CodeName codeNameTemp = codeNameG.isPresent() ? codeNameG.get().createError(ErrorTypeWorkType.MASTER.code)
						: codeName.get()
								.createError(codeNameType.getError() ? ErrorTypeWorkType.GROUP.code: ErrorTypeWorkType.MASTER.code);
				return codeNameTemp;
			} else {
				codeName = this.getDutyTypeAll(companyId).getCodeNames().stream()
						.filter(x -> x.getCode().equals(param.getSelectCode())).findFirst();
				return codeName.isPresent() ? codeName.get() : null;
			}
		case 2:
			// KDL001
			codeName = this.getWorkHours(companyId, param.getWorkplaceId()).getCodeNames().stream()
					.filter(x -> x.getCode().equals(param.getSelectCode())).findFirst();
			return codeName.isPresent() ? codeName.get().createError(ErrorTypeWorkType.MASTER.code) :  new CodeName(param.getSelectCode(), TextResource.localize("KDW003_81"), "")
					.createError(ErrorTypeWorkType.NO_GROUP.code);
		case 3:
			// KDL010
			codeName = this.getServicePlace(companyId).getCodeNames().stream()
					.filter(x -> x.getCode().equals(param.getSelectCode())).findFirst();
			return codeName.isPresent() ? codeName.get().createError(ErrorTypeWorkType.MASTER.code) :  new CodeName(param.getSelectCode(), TextResource.localize("KDW003_81"), "")
					.createError(ErrorTypeWorkType.NO_GROUP.code);
		case 4:
			// KDL032
			codeName = this.getReason(companyId).getCodeNames().stream()
					.filter(x -> x.getCode().equals(param.getSelectCode()) && Integer.parseInt(x.getId()) == (DailyPerformanceCorrectionProcessor.DEVIATION_REASON_MAP.get(Integer.parseInt(param.getWorkTypeCode())))).findFirst();
			return codeName.isPresent() ? codeName.get().createError(ErrorTypeWorkType.MASTER.code) :  new CodeName(param.getSelectCode(), TextResource.localize("KDW003_81"), "")
					.createError(ErrorTypeWorkType.NO_GROUP.code);
		case 5:
			// CDL008
			codeName = this.getWorkPlace(companyId, param.getDate()).getCodeNames().stream()
					.filter(x -> x.getCode().equals(param.getSelectCode())).findFirst();
			return codeName.isPresent() ? codeName.get().createError(ErrorTypeWorkType.MASTER.code) :  new CodeName(param.getSelectCode(), TextResource.localize("KDW003_81"), "")
					.createError(ErrorTypeWorkType.NO_GROUP.code);
		case 6:
			// KCP002
			codeName = this.getClassification(companyId).getCodeNames().stream()
					.filter(x -> x.getCode().equals(param.getSelectCode())).findFirst();
			return codeName.isPresent() ? codeName.get().createError(ErrorTypeWorkType.MASTER.code) :  new CodeName(param.getSelectCode(), TextResource.localize("KDW003_81"), "")
					.createError(ErrorTypeWorkType.NO_GROUP.code);
		case 7:
			// KCP003
			codeName = this.getPossition(companyId, param.getDate()).getCodeNames().stream()
					.filter(x -> x.getCode().equals(param.getSelectCode())).findFirst();
			return codeName.isPresent() ? codeName.get().createError(ErrorTypeWorkType.MASTER.code) :  new CodeName(param.getSelectCode(), TextResource.localize("KDW003_81"), "")
					.createError(ErrorTypeWorkType.NO_GROUP.code);
		case 8:
			// KCP001
			codeName = this.getEmployment(companyId).getCodeNames().stream()
					.filter(x -> x.getCode().equals(param.getSelectCode())).findFirst();
			return codeName.isPresent() ? codeName.get().createError(ErrorTypeWorkType.MASTER.code) :  new CodeName(param.getSelectCode(), TextResource.localize("KDW003_81"), "")
					.createError(ErrorTypeWorkType.NO_GROUP.code);
		default:
			return null;
		}
	}

	public List<CodeName> getAllTypeDialog(int type, ParamDialog param) {
		String companyId = AppContexts.user().companyId();
		switch (type) {
		case 1:
			// KDL002
			if (param.getItemId() != null && param.getItemId() == 28 && !param.getSelectCode().equals("")) {
				AffEmploymentHistoryDto aff = repo.getAffEmploymentHistory(companyId, param.getEmployeeId(), param.getDate());
				return this.getDutyType(companyId, param.getSelectCode(), aff == null ? "" : aff.getEmploymentCode())
						.getCodeNames();
			} else {
				return this.getDutyTypeAll(companyId).getCodeNames();
			}
		case 2:
			// KDL001
			return this.getWorkHours(companyId, param.getWorkplaceId()).getCodeNames();
		case 3:
			// KDL010
			return this.getServicePlace(companyId).getCodeNames();
		case 4:
			// KDL032
			return this.getReason(companyId).getCodeNames();
		case 5:
			// CDL008
			return this.getWorkPlace(companyId, param.getDate()).getCodeNames();
		case 6:
			// KCP002
			return this.getClassification(companyId).getCodeNames();
		case 7:
			// KCP003
			return this.getPossition(companyId, param.getDate()).getCodeNames();
		case 8:
			// KCP001
			return this.getEmployment(companyId).getCodeNames();
		default:
			return null;
		}
	}

	public Map<Integer, Map<String, CodeName>> getAllCodeName(List<Integer> types, String companyId, GeneralDate date) {
		return types.stream().collect(Collectors.toMap(type -> type, type -> {
			switch (type) {
			case 1:
				// KDL002
				return toMap(this.getDutyTypeAll(companyId).getCodeNames());
			case 2:
				// KDL001
				return toMap(this.getWorkHoursAll(companyId).getCodeNames());
			case 3:
				// KDL010
				return toMap(this.getServicePlace(companyId).getCodeNames());
			case 4:
				// KDL032
				return new HashMap<>();
			case 5:
				// CDL008 WPL
				return toMapID(this.getWorkPlace(companyId, date).getCodeNames());
			case 6:
				// KCP002
				return toMap(this.getClassification(companyId).getCodeNames());
			case 7:
				// KCP003 POS
				return toMapID(this.getPossition(companyId, date).getCodeNames());
			case 8:
				// KCP001
				return toMap(this.getEmployment(companyId).getCodeNames());
			default:
				return new HashMap<>();
			}
		}));
	}

	private Map<String, CodeName> toMap(List<CodeName> codeNames) {
		return codeNames.stream().filter(distinctByKey(x -> x.getCode()))
				.collect(Collectors.toMap(x -> x.getCode(), x -> x));
	}

	private Map<String, CodeName> toMapID(List<CodeName> codeNames) {
		return codeNames.stream().filter(distinctByKey(x -> x.getId()))
				.collect(Collectors.toMap(x -> x.getId(), x -> x));
	}
	
	public Optional<CodeName> getCodeNameWithId(int type, GeneralDate date, String id) {
		String companyId = AppContexts.user().companyId();
		if (type == TypeLink.POSSITION.value) {
			return repo.findJobInfoId(companyId, date, id);
		} else if (type == TypeLink.WORKPLACE.value) {
			return repo.findWorkplaceId(companyId, date, id);
		}

		return null;
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		final Set<Object> seen = new HashSet<>();
		return t -> seen.add(keyExtractor.apply(t));
	}
	
	// get application NO19
	public List<EnumConstant> getNameAppliction(){
		String companyId = AppContexts.user().companyId();
		List<AppWithDetailExportDto> lstApp = applicationListForScreen.getAppWithOvertimeInfo(companyId);
		List<EnumConstant> result =  lstApp.stream().map(x -> {
			return new EnumConstant(convertTypeUi(x), x.getAppName(), x.getOvertimeAtr() == null ? "" : x.getOvertimeAtr().toString());
		}).collect(Collectors.toList());
		return result;
	}
	
	private int convertTypeUi(AppWithDetailExportDto dto) {
		switch (dto.getAppType()) {
		case 0:
			return dto.getOvertimeAtr() -1;
		case 7:
			return dto.getAppType() + dto.getStampAtr() + 1;
		case 6:
			return 7;
		case 1:
		case 2:
		case 4:
			return dto.getAppType() + 2;
		case 10:
			return 15;

		default:
			return dto.getAppType() + 999;
		}
	}
}

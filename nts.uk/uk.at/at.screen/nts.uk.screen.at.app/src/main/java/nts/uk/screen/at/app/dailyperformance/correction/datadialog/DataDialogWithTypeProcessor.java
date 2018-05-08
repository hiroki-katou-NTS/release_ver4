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
import nts.uk.screen.at.app.dailyperformance.correction.DailyPerformanceCorrectionProcessor;
import nts.uk.screen.at.app.dailyperformance.correction.DailyPerformanceScreenRepo;
import nts.uk.screen.at.app.dailyperformance.correction.dto.type.TypeLink;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class DataDialogWithTypeProcessor {

	@Inject
	private DailyPerformanceScreenRepo repo;

	// 勤務種類
	public CodeNameType getDutyType(String companyId, String workTypeCode, String employmentCode) {
		List<WorkTypeChangedDto> dtos = repo.findWorkTypeChanged(employmentCode, workTypeCode, companyId);
		Set<String> workTypeCodes = dtos.stream().map(x -> x.getTypeCode()).collect(Collectors.toSet());
		List<CodeName> codeNames = repo.findWorkType(companyId, workTypeCodes);
		return CodeNameType.create(TypeLink.DUTY.value, codeNames);
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
			codeName = this.getDutyType(companyId, param.getWorkTypeCode(), param.getEmploymentCode()).getCodeNames()
					.stream().filter(x -> x.getCode().equals(param.getSelectCode())).findFirst();
			return codeName.isPresent() ? codeName.get() : null;
		case 2:
			// KDL001
			codeName = this.getWorkHours(companyId, param.getWorkplaceId()).getCodeNames().stream()
					.filter(x -> x.getCode().equals(param.getSelectCode())).findFirst();
			return codeName.isPresent() ? codeName.get() : null;
		case 3:
			// KDL010
			codeName = this.getServicePlace(companyId).getCodeNames().stream()
					.filter(x -> x.getCode().equals(param.getSelectCode())).findFirst();
			return codeName.isPresent() ? codeName.get() : null;
		case 4:
			// KDL032
			codeName = this.getReason(companyId).getCodeNames().stream()
					.filter(x -> x.getCode().equals(param.getSelectCode()) && Integer.parseInt(x.getId()) == (DailyPerformanceCorrectionProcessor.DEVIATION_REASON_MAP.get(Integer.parseInt(param.getWorkTypeCode())))).findFirst();
			return codeName.isPresent() ? codeName.get() : null;
		case 5:
			// CDL008
			codeName = this.getWorkPlace(companyId, param.getDate()).getCodeNames().stream()
					.filter(x -> x.getCode().equals(param.getSelectCode())).findFirst();
			return codeName.isPresent() ? codeName.get() : null;
		case 6:
			// KCP002
			codeName = this.getClassification(companyId).getCodeNames().stream()
					.filter(x -> x.getCode().equals(param.getSelectCode())).findFirst();
			return codeName.isPresent() ? codeName.get() : null;
		case 7:
			// KCP003
			codeName = this.getPossition(companyId, param.getDate()).getCodeNames().stream()
					.filter(x -> x.getCode().equals(param.getSelectCode())).findFirst();
			return codeName.isPresent() ? codeName.get() : null;
		case 8:
			// KCP001
			codeName = this.getEmployment(companyId).getCodeNames().stream()
					.filter(x -> x.getCode().equals(param.getSelectCode())).findFirst();
			return codeName.isPresent() ? codeName.get() : null;
		default:
			return null;
		}
	}

	public List<CodeName> getAllTypeDialog(int type, ParamDialog param) {
		String companyId = AppContexts.user().companyId();
		switch (type) {
		case 1:
			// KDL002
			return this.getDutyType(companyId, param.getWorkTypeCode(), param.getEmploymentCode()).getCodeNames();
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

	public Map<Integer, Map<String, String>> getAllCodeName(List<Integer> types, String companyId) {
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
				// CDL008
				return new HashMap<>();
			case 6:
				// KCP002
				return toMap(this.getClassification(companyId).getCodeNames());
			case 7:
				// KCP003
				return new HashMap<>();
			case 8:
				// KCP001
				return toMap(this.getEmployment(companyId).getCodeNames());
			default:
				return new HashMap<>();
			}
		}));
	}

	private Map<String, String> toMap(List<CodeName> codeNames) {
		return codeNames.stream().filter(distinctByKey(x -> x.getCode()))
				.collect(Collectors.toMap(x -> x.getCode(), x -> x.getName()));
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
		return repo.findApplicationCall(companyId);
	}
}

package nts.uk.ctx.at.function.app.find.alarm;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.function.app.find.alarm.extractionrange.ExtractionPeriodDailyDto;
import nts.uk.ctx.at.function.app.find.alarm.extractionrange.ExtractionPeriodUnitDto;
import nts.uk.ctx.at.function.app.find.alarm.extractionrange.SpecifiedMonthDto;
import nts.uk.ctx.at.function.dom.adapter.role.RoleIdFromUserAdapter;
import nts.uk.ctx.at.function.dom.alarm.AlarmPatternSetting;
import nts.uk.ctx.at.function.dom.alarm.AlarmPatternSettingRepository;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.AlarmCheckConditionByCategory;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.AlarmCheckConditionByCategoryRepository;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.CheckCondition;
import nts.uk.ctx.at.function.dom.alarm.extractionrange.ExtractionRange;
import nts.uk.ctx.at.function.dom.alarm.extractionrange.daily.ExtractionPeriodDaily;
import nts.uk.ctx.at.function.dom.alarm.extractionrange.daily.SpecifiedMonth;
import nts.uk.ctx.at.function.dom.alarm.extractionrange.periodunit.ExtractionPeriodUnit;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class AlarmPatternSettingFinder {

	@Inject
	private AlarmPatternSettingRepository alarmPatternRepo;

	@Inject
	private AlarmCheckConditionByCategoryRepository alarmCategoryRepo;

	@Inject
	private RoleIdFromUserAdapter roleByUserFinder;

	public List<AlarmPatternSettingDto> findAllAlarmPattern() {
		String companyId = AppContexts.user().companyId();
		return alarmPatternRepo.findByCompanyId(companyId).stream().map(domain -> convertToAlarmPatternDto(domain))
				.collect(Collectors.toList());
	}

	public List<AlarmCheckConditonCodeDto> findAllAlarmCheckCondition() {
		String companyId = AppContexts.user().companyId();
		return alarmCategoryRepo.findAll(companyId).stream().map(domain -> this.convertToCheckConditionCode(domain))
				.collect(Collectors.toList());
	}

	public AlarmPatternSettingDto convertToAlarmPatternDto(AlarmPatternSetting domain) {

		List<CheckConditionDto> checkConditionDtos = domain.getCheckConList().stream()
				.map(c -> convertToCheckConditionDto(c)).collect(Collectors.toList());
		AlarmPermissionSettingDto alarmPerSet = new AlarmPermissionSettingDto(domain.getAlarmPerSet().isAuthSetting(),
				domain.getAlarmPerSet().getRoleIds());

		return new AlarmPatternSettingDto(domain.getAlarmPatternCD().v(), domain.getAlarmPatternName().v(), alarmPerSet,
				checkConditionDtos);
	}

	public AlarmCheckConditonCodeDto convertToCheckConditionCode(AlarmCheckConditionByCategory domain) {
		return new AlarmCheckConditonCodeDto(EnumAdaptor.convertToValueName(domain.getCategory()), domain.getCode().v(),
				domain.getName().v());
	}

	public List<SpecifiedMonthDto> getSpecifiedMonth() {
		List<SpecifiedMonthDto> monthDtos = new ArrayList<SpecifiedMonthDto>();
		for (SpecifiedMonth r : SpecifiedMonth.values()) {
			monthDtos.add(new SpecifiedMonthDto(r.value, r.nameId));
		}
		return monthDtos;
	}

	public CheckConditionDto convertToCheckConditionDto(CheckCondition domain) {
		ExtractionPeriodDailyDto extractionPeriodDailyDto = null;
		ExtractionPeriodUnitDto extractionUnit = null;

		if (domain.getExtractPeriod().getExtractionRange() == ExtractionRange.PERIOD) {
			extractionPeriodDailyDto = ExtractionPeriodDailyDto
					.fromDomain((ExtractionPeriodDaily) domain.getExtractPeriod());
		} else if (domain.getExtractPeriod().getExtractionRange() == ExtractionRange.WEEK) {
			extractionUnit = ExtractionPeriodUnitDto.fromDomain((ExtractionPeriodUnit) domain.getExtractPeriod());
		} else {
			// Monthly
		}

		return new CheckConditionDto(domain.getAlarmCategory().value, domain.getCheckConditionList(),
				extractionPeriodDailyDto, extractionUnit);

	}

	public List<CodeNameAlarmDto> getCodeNameAlarm() {
		
		String companyId = AppContexts.user().companyId();
		String userId = AppContexts.user().userId();
		List<String> roleIds = roleByUserFinder.getRoleIdFromUserId(userId);
		List<CodeNameAlarmDto> result = alarmPatternRepo.findByCompanyIdAndUser(companyId).stream()
				.filter(a -> !a.isAuthSetting() || a.isAuthSetting() && intersectTwoListRoleId(a.getRoleIds(), roleIds))
				.map(a -> new CodeNameAlarmDto(a.getAlarmCode(), a.getAlarmName())).collect(Collectors.toList());

		result.sort((a, b) -> a.getAlarmCode().compareTo(b.getAlarmCode()));
		return result;
	}

	private boolean intersectTwoListRoleId(List<String> listRole1, List<String> listRole2) {
		List<String> intersect = listRole1.stream().filter(listRole2::contains).collect(Collectors.toList());
		return !intersect.isEmpty();
	}
	
}

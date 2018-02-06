package nts.uk.ctx.at.function.app.find.alarm.checkcondition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.function.dom.adapter.ErrorAlarmWorkRecordAdapter;
import nts.uk.ctx.at.function.dom.adapter.FixedConWorkRecordAdapter;
import nts.uk.ctx.at.function.dom.adapter.FixedConWorkRecordAdapterDto;
import nts.uk.ctx.at.function.dom.adapter.FixedConditionDataAdapter;
import nts.uk.ctx.at.function.dom.adapter.FixedConditionDataAdapterDto;
import nts.uk.ctx.at.function.dom.adapter.WorkRecordExtraConAdapter;
import nts.uk.ctx.at.function.dom.adapter.WorkRecordExtraConAdapterDto;
import nts.uk.ctx.at.function.dom.alarm.AlarmCategory;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.AlarmCheckConditionByCategory;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.AlarmCheckConditionByCategoryRepository;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.daily.ConExtractedDaily;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.daily.DailyAlarmCondition;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.fourweekfourdayoff.AlarmCheckCondition4W4D;
import nts.uk.shr.com.context.AppContexts;

/**
 * 
 * @author HungTT
 *
 */
@Stateless
public class AlarmCheckConditionByCategoryFinder {

	@Inject
	private AlarmCheckConditionByCategoryRepository conditionRepo;

	@Inject
	private FixedConWorkRecordAdapter fixedConditionAdapter;

	@Inject
	private FixedConditionDataAdapter fixCondDataAdapter;

	@Inject
	private WorkRecordExtraConAdapter workRecordExtractConditionAdapter;

	@Inject
	private ErrorAlarmWorkRecordAdapter errorAlarmWkRcAdapter;

	public List<AlarmCheckConditionByCategoryDto> getAllData(int category) {
		String companyId = AppContexts.user().companyId();
		return conditionRepo.findByCategory(companyId, category).stream().map(item -> minValueFromDomain(item))
				.collect(Collectors.toList());
	}

	public AlarmCheckConditionByCategoryDto getDataByCode(int category, String code) {
		String companyId = AppContexts.user().companyId();
		Optional<AlarmCheckConditionByCategory> opt = conditionRepo.find(companyId, category, code);
		if (opt.isPresent()) {
			return fromDomain(opt.get());
		} else {
			throw new RuntimeException("Object not exist!");
		}
	}
	
	public List<DailyErrorAlarmCheckDto> getDailyErrorAlarmCheck() {
		return errorAlarmWkRcAdapter.getAllErrorAlarmWorkRecord(AppContexts.user().companyId()).stream()
				.map(item -> new DailyErrorAlarmCheckDto(item.getCode(), item.getName(), item.getTypeAtr(),
						item.getDisplayMessage()))
				.collect(Collectors.toList());
	}

	private AlarmCheckConditionByCategoryDto fromDomain(AlarmCheckConditionByCategory domain) {
		int schedule4WCondition = 0;
		DailyAlarmCondition dailyAlarmCondition = new DailyAlarmCondition("", ConExtractedDaily.ALL.value, false,
				Collections.emptyList(), Collections.emptyList());
		List<FixedConditionWorkRecordDto> listFixedConditionWkRecord = new ArrayList<>();
		List<WorkRecordExtraConAdapterDto> lstWorkRecordExtraCon = new ArrayList<>();
		if (domain.getCategory() == AlarmCategory.SCHEDULE_4WEEK && domain.getExtractionCondition() != null) {
			AlarmCheckCondition4W4D schedule4WeekCondition = (AlarmCheckCondition4W4D) domain.getExtractionCondition();
			schedule4WCondition = schedule4WeekCondition.getFourW4DCheckCond().value;
		}
		if (domain.getCategory() == AlarmCategory.DAILY && domain.getExtractionCondition() != null) {
			dailyAlarmCondition = (DailyAlarmCondition) domain.getExtractionCondition();
			String dailyID = dailyAlarmCondition.getDailyAlarmConID();
			List<FixedConWorkRecordAdapterDto> listFixedConditionWorkRecord = fixedConditionAdapter
					.getAllFixedConWorkRecordByID(dailyID);
			List<FixedConditionDataAdapterDto> listFixedConditionData = fixCondDataAdapter
					.getAllFixedConditionDataPub();
			for (FixedConditionDataAdapterDto i : listFixedConditionData) {
				boolean check = true;
				if (listFixedConditionWorkRecord != null && !listFixedConditionWorkRecord.isEmpty()) {
					for (FixedConWorkRecordAdapterDto e : listFixedConditionWorkRecord) {
						if (e.getFixConWorkRecordNo() == i.getFixConWorkRecordNo()) {
							FixedConditionWorkRecordDto dto = new FixedConditionWorkRecordDto(e.getDailyAlarmConID(),
									i.getFixConWorkRecordName(), i.getFixConWorkRecordNo(), e.getMessage(),
									e.isUseAtr());
							listFixedConditionWkRecord.add(dto);
							check = false;
							break;
						}
					}
				}
				if (check) {
					FixedConditionWorkRecordDto dto = new FixedConditionWorkRecordDto("", i.getFixConWorkRecordName(),
							i.getFixConWorkRecordNo(), i.getMessage(), false);
					listFixedConditionWkRecord.add(dto);
				}
			}
			lstWorkRecordExtraCon = workRecordExtractConditionAdapter
					.getAllWorkRecordExtraConByListID(dailyAlarmCondition.getExtractConditionWorkRecord());
		}

		return new AlarmCheckConditionByCategoryDto(domain.getCode().v(), domain.getName().v(),
				domain.getCategory().value,
				new AlarmCheckTargetConditionDto(domain.getExtractTargetCondition().isFilterByEmployment(),
						domain.getExtractTargetCondition().isFilterByClassification(),
						domain.getExtractTargetCondition().isFilterByJobTitle(),
						domain.getExtractTargetCondition().isFilterByBusinessType(),
						domain.getExtractTargetCondition().getLstEmploymentCode(),
						domain.getExtractTargetCondition().getLstClassificationCode(),
						domain.getExtractTargetCondition().getLstJobTitleId(),
						domain.getExtractTargetCondition().getLstBusinessTypeCode()),
				domain.getListRoleId(), schedule4WCondition,
				new DailyAlarmCheckConditionDto(dailyAlarmCondition.isAddApplication(),
						dailyAlarmCondition.getConExtractedDaily().value, dailyAlarmCondition.getErrorAlarmCode(),
						lstWorkRecordExtraCon, listFixedConditionWkRecord));
	}
	
	private AlarmCheckConditionByCategoryDto minValueFromDomain(AlarmCheckConditionByCategory domain) {
		return new AlarmCheckConditionByCategoryDto(domain.getCode().v(), domain.getName().v(),
				domain.getCategory().value, null, null, 0, null);
	}
}

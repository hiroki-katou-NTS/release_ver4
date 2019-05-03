package nts.uk.ctx.at.function.dom.alarm.alarmlist.aggregationprocess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.function.dom.adapter.workplace.SyWorkplaceAdapter;
import nts.uk.ctx.at.function.dom.adapter.workplace.WkpConfigAtTimeAdapterDto;
import nts.uk.ctx.at.function.dom.alarm.AlarmCategory;
import nts.uk.ctx.at.function.dom.alarm.AlarmPatternSetting;
import nts.uk.ctx.at.function.dom.alarm.AlarmPatternSettingRepository;
import nts.uk.ctx.at.function.dom.alarm.alarmdata.ValueExtractAlarm;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.AlarmExtraValueWkReDto;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.EmployeeSearchDto;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.PeriodByAlarmCategory;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.AlarmCheckConditionByCategory;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.CheckCondition;
import nts.uk.shr.com.i18n.TextResource;

@Stateless
public class AggregationProcessService {
	
	@Inject
	private AlarmPatternSettingRepository alPatternSettingRepo;
	
	@Inject
	private ExtractAlarmForEmployeeService extractService;

	@Inject
	private SyWorkplaceAdapter syWorkplaceAdapter;
		
	public List<AlarmExtraValueWkReDto> processAlarmListWorkRecord(GeneralDate baseDate, String companyID, List<EmployeeSearchDto> listEmployee, 
			String checkPatternCode, List<PeriodByAlarmCategory> periodByCategory) {
		List<AlarmExtraValueWkReDto> result = new ArrayList<>();
		
		// パラメータ．パターンコードをもとにドメインモデル「アラームリストパターン設定」を取得する
		// パラメータ．パターンコードから「アラームリストパターン設定」を取得する
		Optional<AlarmPatternSetting> alarmPatternSetting = this.alPatternSettingRepo.findByAlarmPatternCode(companyID, checkPatternCode);		
		if(!alarmPatternSetting.isPresent())
			throw new RuntimeException("「アラームリストパターン設定 」が見つかりません！");
		
		
		List<ValueExtractAlarm> valueList = new ArrayList<>();

		valueList.addAll(extractService.process(companyID, alarmPatternSetting.get().getCheckConList(), periodByCategory, listEmployee));

		
		// get list workplaceId and hierarchyCode 
		List<String> listWorkplaceId = listEmployee.stream().map(e -> e.getWorkplaceId()).filter(wp -> wp != null).distinct().collect(Collectors.toList());
		Map<String, WkpConfigAtTimeAdapterDto> hierarchyWPMap = syWorkplaceAdapter.findByWkpIdsAtTime(companyID, baseDate, listWorkplaceId)
																			.stream().collect(Collectors.toMap(WkpConfigAtTimeAdapterDto::getWorkplaceId, x->x));
		
		// Map employeeID to EmployeeSearchDto object
		Map<String, EmployeeSearchDto> mapEmployeeId = listEmployee.stream().collect(Collectors.toMap(EmployeeSearchDto::getId, x->x));
		
		// MAP Enum AlarmCategory 
		Map<String, AlarmCategory> mapTextResourceToEnum = this.mapTextResourceToEnum();
		
		
		//Convert from ValueExtractAlarm to AlarmExtraValueWkReDto
		for(ValueExtractAlarm value: valueList) {
			AlarmExtraValueWkReDto itemResult = new AlarmExtraValueWkReDto(value.getWorkplaceID().orElse(null),
					getWorkPlaceId(hierarchyWPMap, value.getWorkplaceID().orElse(null)),
					mapEmployeeId.get(value.getEmployeeID()).getWorkplaceName(), 
					value.getEmployeeID(),
					mapEmployeeId.get(value.getEmployeeID()).getCode(),
					mapEmployeeId.get(value.getEmployeeID()).getName(), 
					value.getAlarmValueDate(), 
					mapTextResourceToEnum.get(value.getClassification()).value,
					value.getClassification(),
					value.getAlarmItem(),
					value.getAlarmValueMessage(),
					value.getComment().orElse(null),null);
			result.add(itemResult);
		}
		return result;
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<AlarmExtraValueWkReDto> processAlarmListWorkRecordV2(GeneralDate baseDate, String companyID, List<EmployeeSearchDto> listEmployee, 
			List<PeriodByAlarmCategory> periodByCategory, List<AlarmCheckConditionByCategory> eralCate,
			List<CheckCondition> checkConList, Consumer<Integer> counter, Supplier<Boolean> shouldStop) {
		
		List<ValueExtractAlarm> valueList = new ArrayList<>();
		
		// get list workplaceId and hierarchyCode 
		List<String> listWorkplaceId = listEmployee.stream().map(e -> e.getWorkplaceId()).filter(wp -> wp != null).distinct().collect(Collectors.toList());
		Map<String, WkpConfigAtTimeAdapterDto> hierarchyWPMap = syWorkplaceAdapter.findByWkpIdsAtTime(companyID, baseDate, listWorkplaceId)
																			.stream().collect(Collectors.toMap(WkpConfigAtTimeAdapterDto::getWorkplaceId, x->x));
		
		// Map employeeID to EmployeeSearchDto object
		Map<String, EmployeeSearchDto> mapEmployeeId = listEmployee.stream().collect(Collectors.toMap(EmployeeSearchDto::getId, x->x));
		
		// MAP Enum AlarmCategory 
		Map<String, AlarmCategory> mapTextResourceToEnum = this.mapTextResourceToEnum();

		valueList.addAll(extractService.processV2(companyID, checkConList, periodByCategory, 
				listEmployee, eralCate, counter, shouldStop));
		
		//Convert from ValueExtractAlarm to AlarmExtraValueWkReDto
		return valueList.stream().map(value -> {
			return new AlarmExtraValueWkReDto(value.getWorkplaceID().orElse(null),
					getWorkPlaceId(hierarchyWPMap, value.getWorkplaceID().orElse(null)),
					mapEmployeeId.get(value.getEmployeeID()).getWorkplaceName(), 
					value.getEmployeeID(),
					mapEmployeeId.get(value.getEmployeeID()).getCode(),
					mapEmployeeId.get(value.getEmployeeID()).getName(), 
					value.getAlarmValueDate(), 
					mapTextResourceToEnum.get(value.getClassification()).value,
					value.getClassification(),
					value.getAlarmItem(),
					value.getAlarmValueMessage(),
					value.getComment().orElse(null),
					value.getCheckedValue().orElse(null)
					);
		}).collect(Collectors.toList());
	}

	private String getWorkPlaceId(Map<String, WkpConfigAtTimeAdapterDto> hierarchyWPMap, String workplaceID) {
		return workplaceID != null  && hierarchyWPMap.get(workplaceID) != null ? hierarchyWPMap.get(workplaceID).getHierarchyCd() : "";
	}
	
	private Map<String, AlarmCategory> mapTextResourceToEnum(){
		Map<String, AlarmCategory> map = new HashMap<String, AlarmCategory>();
		map.put(TextResource.localize("KAL010_1"), AlarmCategory.DAILY);
		map.put(TextResource.localize("KAL010_62"), AlarmCategory.SCHEDULE_4WEEK);
		map.put(TextResource.localize("KAL010_100"), AlarmCategory.MONTHLY);
		map.put(TextResource.localize("KAL010_208"), AlarmCategory.AGREEMENT);
		map.put(TextResource.localize("KAL010_250"), AlarmCategory.MULTIPLE_MONTH);
		return map;
	} 
	

}

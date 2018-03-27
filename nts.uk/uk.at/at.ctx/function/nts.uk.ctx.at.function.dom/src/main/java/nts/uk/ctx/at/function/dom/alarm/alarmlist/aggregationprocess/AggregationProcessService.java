package nts.uk.ctx.at.function.dom.alarm.alarmlist.aggregationprocess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
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
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.i18n.TextResource;

@Stateless
public class AggregationProcessService {
	
	@Inject
	private AlarmPatternSettingRepository alPatternSettingRepo;
	
	@Inject
	private ExtractAlarmForEmployeeService extractService;

	@Inject
	private SyWorkplaceAdapter syWorkplaceAdapter;
		
	public List<AlarmExtraValueWkReDto> processAlarmListWorkRecord(List<EmployeeSearchDto> listEmployee, String checkPatternCode, List<PeriodByAlarmCategory> periodByCategory) {
		List<AlarmExtraValueWkReDto> result = new ArrayList<>();

		String companyID = AppContexts.user().companyId();
		
		// パラメータ．パターンコードをもとにドメインモデル「アラームリストパターン設定」を取得する
		Optional<AlarmPatternSetting> alarmPatternSetting = this.alPatternSettingRepo.findByAlarmPatternCode(companyID, checkPatternCode);		
		if(!alarmPatternSetting.isPresent())
			throw new RuntimeException("「アラームリストパターン設定 」が見つかりません！");
		
		
		List<ValueExtractAlarm> valueList = new ArrayList<>();
		// 従業員ごと に行う(for list employee)
		for (EmployeeSearchDto employee : listEmployee) {
			 valueList.addAll(extractService.process(alarmPatternSetting.get().getCheckConList(), periodByCategory, employee));
		}
				
		
		// get list workplaceId and hierarchyCode 
		List<String> listWorkplaceId = listEmployee.stream().map(e -> e.getWorkplaceId()).distinct().collect(Collectors.toList());
		listWorkplaceId.removeIf( e->e==null);
		List<WkpConfigAtTimeAdapterDto> hierarchyWPList = syWorkplaceAdapter.findByWkpIdsAtTime(companyID, GeneralDate.today(), listWorkplaceId);
		Map<String, WkpConfigAtTimeAdapterDto> hierarchyWPMap = hierarchyWPList.stream().collect(Collectors.toMap(WkpConfigAtTimeAdapterDto::getWorkplaceId, x->x));
		
		// Map employeeID to EmployeeSearchDto object
		Map<String, EmployeeSearchDto> mapEmployeeId = listEmployee.stream().collect(Collectors.toMap(EmployeeSearchDto::getId, x->x));
		
		// MAP Enum AlarmCategory 
		Map<String, AlarmCategory> mapTextResourceToEnum = this.mapTextResourceToEnum();
		
		
		//Convert from ValueExtractAlarm to AlarmExtraValueWkReDto
		for(ValueExtractAlarm value: valueList) {
			AlarmExtraValueWkReDto itemResult = new AlarmExtraValueWkReDto(value.getWorkplaceID().orElse(null),
					value.getWorkplaceID().isPresent()  && hierarchyWPMap.get(value.getWorkplaceID().get()) !=null ? hierarchyWPMap.get(value.getWorkplaceID().get()).getHierarchyCd(): "",
					mapEmployeeId.get(value.getEmployeeID()).getWorkplaceName(), 
					value.getEmployeeID(),
					mapEmployeeId.get(value.getEmployeeID()).getCode(),
					mapEmployeeId.get(value.getEmployeeID()).getName(), 
					value.getAlarmValueDate(), mapTextResourceToEnum.get(value.getClassification()).value,
					value.getClassification(),
					value.getAlarmItem(),
					value.getAlarmValueMessage(),
					value.getComment().orElse(null));
			result.add(itemResult);
		}
		return result;
	}
	
	private Map<String, AlarmCategory> mapTextResourceToEnum(){
		Map<String, AlarmCategory> map = new HashMap<String, AlarmCategory>();
		map.put(TextResource.localize("KAL010_1"), AlarmCategory.DAILY);
		map.put(TextResource.localize("KAL010_62"), AlarmCategory.SCHEDULE_4WEEK);
		map.put(TextResource.localize("KAL010_100"), AlarmCategory.MONTHLY);
		return map;
	} 
	

}

package nts.uk.ctx.at.record.app.service.attendanceitem;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.app.find.dailyperform.DailyRecordDto;
import nts.uk.ctx.at.record.app.find.monthly.root.MonthlyRecordWorkDto;
import nts.uk.ctx.at.record.dom.attendanceitem.util.AttendanceItemService;
import nts.uk.ctx.at.record.dom.optitem.OptionalItem;
import nts.uk.ctx.at.record.dom.optitem.OptionalItemAtr;
import nts.uk.ctx.at.record.dom.optitem.OptionalItemRepository;
import nts.uk.ctx.at.record.dom.optitem.PerformanceAtr;
import nts.uk.ctx.at.shared.dom.attendance.util.AttendanceItemIdContainer;
import nts.uk.ctx.at.shared.dom.attendance.util.AttendanceItemUtil;
import nts.uk.ctx.at.shared.dom.attendance.util.AttendanceItemUtil.AttendanceItemType;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class AttendanceItemServiceCenter implements AttendanceItemService {
	
	@Inject
	private OptionalItemRepository optionalMaster;

	@Override
	public List<ItemValue> getItemBy(AttendanceItemType type, ValueType... types) {
		List<ValueType> limitted = Arrays.asList(types);
		
		if(type == AttendanceItemType.MONTHLY_ITEM){
			return limitItemBy(type, AttendanceItemUtil.toItemValues(new MonthlyRecordWorkDto()), limitted);
		}
		
		return limitItemBy(type, AttendanceItemUtil.toItemValues(new DailyRecordDto()), limitted);
	}

	private Map<Integer, OptionalItem> getOptionalMaster(AttendanceItemType type) {
		return optionalMaster.findByPerformanceAtr(AppContexts.user().companyId(), 
				type == AttendanceItemType.MONTHLY_ITEM ? PerformanceAtr.MONTHLY_PERFORMANCE : PerformanceAtr.DAILY_PERFORMANCE)
				.stream().collect(Collectors.toMap(c -> c.getOptionalItemNo().v(), c -> c));
	}

	@Override
	public List<ItemValue> getTimeAndCountItem(AttendanceItemType type) {
		return getItemBy(type, ValueType.TIME, ValueType.COUNT, ValueType.COUNT_WITH_DECIMAL);
	}
	
	private List<ItemValue> limitItemBy(AttendanceItemType type, List<ItemValue> source, List<ValueType> constraints){
		Map<Integer, OptionalItem> optionalMaster = getOptionalMaster(type);
		Map<Integer, Integer> optionalItems = AttendanceItemIdContainer.mapOptionalItemIdsToNos(type);
		
		return source.stream().filter(c -> {
			if(!constraints.contains(c.getValueType())){
				return false;
			}
			if(AttendanceItemIdContainer.isOptionalItem(c)){
				Integer itemNo = optionalItems.get(c.getItemId());
				OptionalItem optionalItem = optionalMaster.get(itemNo);
				if(optionalItem == null){
					return false;
				}
				if(optionalItem.getOptionalItemAtr() == OptionalItemAtr.NUMBER){
					c.valueType(ValueType.COUNT_WITH_DECIMAL);
				}
			}
			
			
			return true;
		}).collect(Collectors.toList());
	}

}

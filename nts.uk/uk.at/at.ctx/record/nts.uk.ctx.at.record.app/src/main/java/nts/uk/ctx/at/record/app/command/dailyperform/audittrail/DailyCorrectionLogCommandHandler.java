package nts.uk.ctx.at.record.app.command.dailyperform.audittrail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.tuple.Pair;

import lombok.val;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.command.dailyperform.DailyRecordWorkCommand;
import nts.uk.ctx.at.record.app.command.dailyperform.audittrail.DailyCorrectionLogParameter.DailyCorrectedItem;
import nts.uk.ctx.at.record.app.command.dailyperform.audittrail.DailyCorrectionLogParameter.DailyCorrectionTarget;
import nts.uk.ctx.at.record.dom.daily.itemvalue.DailyItemValue;
import nts.uk.ctx.at.shared.dom.scherec.attendanceitem.converter.util.AttendanceItemIdContainer;
import nts.uk.ctx.at.shared.dom.scherec.attendanceitem.converter.util.AttendanceItemUtil.AttendanceItemType;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.item.ItemValue;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.DailyAttendanceItem;
import nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.adapter.DailyAttendanceItemNameAdapter;
import nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.adapter.DailyAttendanceItemNameAdapterDto;
import nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.repository.DailyAttendanceItemRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.security.audittrail.correction.DataCorrectionContext;
import nts.uk.shr.com.security.audittrail.correction.content.CorrectionAttr;
import nts.uk.shr.com.security.audittrail.correction.content.DataValueAttribute;
import nts.uk.shr.com.security.audittrail.correction.processor.CorrectionProcessorId;

@Stateless
public class DailyCorrectionLogCommandHandler extends CommandHandler<DailyCorrectionLogCommand> {
	
	@Inject
	private DailyAttendanceItemNameAdapter dailyAttendanceItemNameAdapter;
	
	@Inject
	private DailyAttendanceItemRepository dailyAttendanceItemRepository;

    private final static List<Integer> ITEM_ID_ALL = AttendanceItemIdContainer.getIds(AttendanceItemType.DAILY_ITEM).stream().map(x -> x.getItemId()).collect(Collectors.toList());
	
    @Override
	protected void handle(CommandHandlerContext<DailyCorrectionLogCommand> context) {
    	List<DailyAttendanceItem> dailyItems = dailyAttendanceItemRepository.getListById(AppContexts.user().companyId(), ITEM_ID_ALL);
		List<DPAttendanceItemRC> itemTemp = dailyItems.stream()
				.map(x -> new DPAttendanceItemRC(x.getAttendanceItemId(), x.getAttendanceName().v(),
						x.getDisplayNumber(), true, x.getNameLineFeedPosition(), x.getDailyAttendanceAtr().value,
						x.getMasterType().isPresent() ? x.getMasterType().get().value : null,
						x.getPrimitiveValue().isPresent() ? x.getPrimitiveValue().get().value : null))
				.collect(Collectors.toList());
		
		Map<Integer, DPAttendanceItemRC> lstAttendanceItem = itemTemp.stream().collect(Collectors.toMap(x -> x.getId(), x -> x));
		DataCorrectionContext.transactional(CorrectionProcessorId.DAILY, () -> {
	        
			Map<Integer, String> itemNameMap = dailyAttendanceItemNameAdapter.getDailyAttendanceItemName(ITEM_ID_ALL)
					.stream().filter(x -> x.getAttendanceItemName() != null).collect(Collectors.toMap(DailyAttendanceItemNameAdapterDto::getAttendanceItemId,
							x -> x.getAttendanceItemName()));
			
			val correctionLogParameter = new DailyCorrectionLogParameter(
					mapToDailyCorrection(convertToItemValueFtomItems(context.getCommand().getDailyNew()),
							convertToItemValueFtomItems(context.getCommand().getDailyOld()), itemEditMap(context.getCommand().getCommandNew()), itemNameMap), lstAttendanceItem);
			DataCorrectionContext.setParameter(correctionLogParameter);
			AttendanceItemIdContainer.getIds(AttendanceItemType.DAILY_ITEM);
		});
	}
	
//	private Map<Pair<String, GeneralDate>, Map<Integer, ItemValue>> convertToItemValue(List<IntegrationOfDaily> domains){
//		Map<Pair<String, GeneralDate>, Map<Integer, ItemValue>> result = new HashMap<>();
//		for (IntegrationOfDaily daily : domains) {
//			List<ItemValue> values = DailyRecordToAttendanceItemConverterImpl.builder().setData(daily).convert(ITEM_ID_ALL);
//			 Map<Integer, ItemValue> map = values.stream().collect(Collectors.toMap(x -> x.getItemId(), x -> x));
//			 result.put(Pair.of(daily.getWorkInformation().getEmployeeId(), daily.getWorkInformation().getYmd()), map);
//		}
//		return result;
//	}
	
	private Map<Pair<String, GeneralDate>, Map<Integer, ItemValue>> convertToItemValueFtomItems(List<DailyItemValue> dailys){
		Map<Pair<String, GeneralDate>, Map<Integer, ItemValue>> result = new HashMap<>();
		dailys.get(0).getItems().sort((x,y) -> x.getItemId()-y.getItemId());
		for (DailyItemValue daily : dailys) {
			 Map<Integer, ItemValue> map = daily.getItems().stream().collect(Collectors.toMap(x -> x.getItemId(), x -> x));
			 result.put(Pair.of(daily.getEmployeeId(), daily.getDate()), map);
		}
		return result;
	}
	
	private Map<Pair<String, GeneralDate>, List<Integer>> itemEditMap(List<DailyRecordWorkCommand> commands){
		Map<Pair<String, GeneralDate>, List<Integer>> result = new HashMap<>();
		commands.stream().forEach(x ->{
			result.put(Pair.of(x.getEmployeeId(), x.getWorkDate()), x.itemValues().stream().map(y -> y.getItemId()).collect(Collectors.toList()));
		});
		return result;
	}
	
	private List<DailyCorrectionTarget> mapToDailyCorrection(
			Map<Pair<String, GeneralDate>, Map<Integer, ItemValue>> itemNewMap,
			Map<Pair<String, GeneralDate>, Map<Integer, ItemValue>> itemOldMap,
			Map<Pair<String, GeneralDate>, List<Integer>> itemEditMap, Map<Integer, String> itemNameMap) {
		List<DailyCorrectionTarget> targets = new ArrayList<>();
		itemNewMap.forEach((key, value) -> {
			val itemOldValueMap = itemOldMap.get(key);
			val daiTarget = new DailyCorrectionTarget(key.getLeft(), key.getRight());
			value.forEach((valueItemKey, valueItemNew) -> {
				val itemOld = itemOldValueMap.get(valueItemKey);
				List<Integer> itemEdits = itemEditMap.get(key);
				if (compareValue(valueItemNew, itemOld)) {
					DailyCorrectedItem item = new DailyCorrectedItem(itemNameMap.get(valueItemKey),
							valueItemNew.getItemId(), itemOld.getValue(), valueItemNew.getValue(),
							convertType(valueItemNew.getValueType()), itemEdits.contains(valueItemNew.getItemId())
									? CorrectionAttr.EDIT : CorrectionAttr.CALCULATE);
					daiTarget.getCorrectedItems().add(item);
				}

			});
			targets.add(daiTarget);
		});
		return targets;
	}

	private Integer convertType(ValueType valueType){
		switch (valueType) {
		case TIME:
		case TIME_WITH_DAY:
			return DataValueAttribute.TIME.value;
		case COUNT:
		case COUNT_WITH_DECIMAL:
			return DataValueAttribute.COUNT.value;
		case AMOUNT:
			return DataValueAttribute.MONEY.value;
			
		case CLOCK:
			return DataValueAttribute.CLOCK.value;	
			
		default:
			return DataValueAttribute.STRING.value;
		}
	}
	
	private boolean compareValue(ItemValue itemNew, ItemValue itemOld) {
			// TimeWithDay
			ValueType type = itemOld.getValueType();
			if(!type.isCompare()) return !itemNew.equals(itemOld);
			if(!itemNew.getValueType().equals(itemOld.getValueType())) return false;
			if (((itemNew.getValue() == null && itemOld.getValue() != null && Double.parseDouble(itemOld.getValue()) == 0)
					   || (itemOld.getValue() == null && itemNew.getValue() != null && Double.parseDouble(itemNew.getValue()) == 0) 
					   || (itemNew.getValue() != null && itemOld.getValue() != null && Double.parseDouble(itemOld.getValue()) == 0 && Double.parseDouble(itemNew.getValue()) == 0))) {
				itemNew.value(0);
				itemOld.value(0);
			}else if(itemOld.getValue() != null && itemNew.getValue() != null){
				itemNew.value(Double.parseDouble(itemNew.getValue()));
				itemOld.value(Double.parseDouble(itemOld.getValue()));
			}
			
            if(itemOld.getValue() != null && itemNew.getValue() != null && !itemOld.getValue().equals(itemNew.getValue())) {
            	return true;
            }else if (!itemOld.equals(itemNew)) {
				return true;
			}
		return false;
	}
}

package nts.uk.screen.at.app.monthlyperformance.audittrail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.record.app.find.monthly.root.MonthlyRecordWorkDto;
import nts.uk.ctx.at.shared.dom.adapter.attendanceitemname.AttendanceItemNameAdapter;
import nts.uk.ctx.at.shared.dom.adapter.attendanceitemname.MonthlyAttendanceItemNameDto;
import nts.uk.ctx.at.shared.dom.scherec.attendanceitem.converter.util.AttendanceItemIdContainer;
import nts.uk.ctx.at.shared.dom.scherec.attendanceitem.converter.util.AttendanceItemUtil;
import nts.uk.ctx.at.shared.dom.scherec.attendanceitem.converter.util.AttendanceItemUtil.AttendanceItemType;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.item.ItemValue;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.item.ValueType;
import nts.uk.screen.at.app.monthlyperformance.audittrail.MonthlyCorrectionLogParameter.MonthlyCorrectedItem;
import nts.uk.screen.at.app.monthlyperformance.audittrail.MonthlyCorrectionLogParameter.MonthlyCorrectionTarget;
import nts.uk.shr.com.security.audittrail.correction.DataCorrectionContext;
import nts.uk.shr.com.security.audittrail.correction.content.CorrectionAttr;
import nts.uk.shr.com.security.audittrail.correction.content.DataValueAttribute;
import nts.uk.shr.com.security.audittrail.correction.processor.CorrectionProcessorId;

@Stateless
public class MonthlyCorrectionLogCommandHandler extends CommandHandler<MonthlyCorrectionLogCommand> {

	@Inject
	private AttendanceItemNameAdapter attendanceItemNameAdapter;
	
	private final static List<Integer> ITEM_ID_ALL = AttendanceItemIdContainer.getIds(AttendanceItemType.MONTHLY_ITEM)
			.stream().map(x -> x.getItemId()).collect(Collectors.toList());

	@Override
	protected void handle(CommandHandlerContext<MonthlyCorrectionLogCommand> context) {
		DataCorrectionContext.transactional(CorrectionProcessorId.MONTHLY, () -> {
			
			List<MonthlyRecordWorkDto> oldDtos = context.getCommand().getMonthlyOld();
			List<MonthlyRecordWorkDto> newDtos = context.getCommand().getMonthlyNew();
	
			Map<Integer, String> itemNameMap = attendanceItemNameAdapter.getMonthlyAttendanceItemName(ITEM_ID_ALL).stream()
					.collect(Collectors.toMap(MonthlyAttendanceItemNameDto::getAttendanceItemId,
							x -> x.getAttendanceItemName()));
			Map<String, List<Integer>> editMap = context.getCommand().getQuery().stream()
					.collect(Collectors.toMap(x -> x.getEmployeeId(),
							x -> x.getItems().stream().map(y -> y.getItemId()).collect(Collectors.toList())));
			List<MonthlyCorrectionTarget> targets = new ArrayList<>();
			for (int i = 0; i < newDtos.size(); i++) {
				MonthlyRecordWorkDto oldDto = oldDtos.get(i);
				MonthlyRecordWorkDto newDto = newDtos.get(i);
				List<Integer> editItems = editMap.get(oldDto.getEmployeeId());
				Map<Integer, ItemValue> itemOldMap = AttendanceItemUtil
						.toItemValues(oldDto, ITEM_ID_ALL, AttendanceItemType.MONTHLY_ITEM).stream()
						.collect(Collectors.toMap(x -> x.getItemId(), x -> x));
				Map<Integer, ItemValue> itemNewMap = AttendanceItemUtil
						.toItemValues(newDto, ITEM_ID_ALL, AttendanceItemType.MONTHLY_ITEM).stream()
						.collect(Collectors.toMap(x -> x.getItemId(), x -> x));
				MonthlyCorrectionTarget monthTarget = new MonthlyCorrectionTarget(oldDto.getEmployeeId(), context.getCommand().getEndPeriod());
				itemNewMap.forEach((key, value) -> {
					ItemValue itemNew = value;
					ItemValue itemOld = itemOldMap.get(key);
					if(itemOld != null && itemNew != null && itemNew.getValue() != null && itemOld.getValue() != null && itemNew.getValueType().isDouble()){
						itemNew.value(String.valueOf(Double.valueOf(itemNew.getValue()))).valueType(ValueType.COUNT_WITH_DECIMAL);
						itemOld.value(String.valueOf(Double.valueOf(itemOld.getValue()))).valueType(ValueType.COUNT_WITH_DECIMAL);;
					}
					if (itemOld != null && itemNew != null && (itemNew.getItemId() == itemOld.getItemId())
							&& (itemNew.getValue() != null && itemOld.getValue() != null)
							&& !StringUtils.equals(itemNew.getValue(), itemOld.getValue())) {
						MonthlyCorrectedItem item = new MonthlyCorrectedItem(itemNameMap.get(key), key,
								itemOld != null ? itemOld.getValue() : null,
								itemNew != null ? itemNew.getValue() : null, convertType(itemNew.getValueType()),
								editItems.contains(key) ? CorrectionAttr.EDIT : CorrectionAttr.CALCULATE);
						monthTarget.getCorrectedItems().add(item);
					}
				});
				targets.add(monthTarget);
			}
			MonthlyCorrectionLogParameter correctionLogParameter = new MonthlyCorrectionLogParameter(targets);
			DataCorrectionContext.setParameter(correctionLogParameter);
			AttendanceItemIdContainer.getIds(AttendanceItemType.MONTHLY_ITEM);
		});
	}
	
	private Integer convertType(ValueType valueType) {
		switch (valueType.value) {

		case 1:
		case 2:
			return DataValueAttribute.TIME.value;

		case 13:
			return DataValueAttribute.MONEY.value;

		case 15:
			return DataValueAttribute.CLOCK.value;

		default:
			return DataValueAttribute.STRING.value;
		}
	}
}

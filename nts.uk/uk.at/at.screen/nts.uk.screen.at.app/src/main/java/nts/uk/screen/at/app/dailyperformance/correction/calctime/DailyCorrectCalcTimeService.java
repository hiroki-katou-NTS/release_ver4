package nts.uk.screen.at.app.dailyperformance.correction.calctime;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.command.dailyperform.DailyCorrectEventServiceCenter;
import nts.uk.ctx.at.record.app.command.dailyperform.checkdata.DailyModifyRCResult;
import nts.uk.ctx.at.record.app.command.dailyperform.correctevent.EventCorrectResult;
import nts.uk.ctx.at.record.app.find.dailyperform.DailyRecordDto;
import nts.uk.ctx.at.record.app.find.dailyperform.editstate.EditStateOfDailyPerformanceDto;
import nts.uk.ctx.at.record.dom.editstate.enums.EditStateSetting;
import nts.uk.ctx.at.record.dom.worktime.enums.StampSourceInfo;
import nts.uk.ctx.at.shared.dom.attendance.util.AttendanceItemUtil;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;
import nts.uk.screen.at.app.dailymodify.query.DailyModifyResult;
import nts.uk.screen.at.app.dailyperformance.correction.checkdata.ValidatorDataDailyRes;
import nts.uk.screen.at.app.dailyperformance.correction.datadialog.CodeName;
import nts.uk.screen.at.app.dailyperformance.correction.datadialog.DataDialogWithTypeProcessor;
import nts.uk.screen.at.app.dailyperformance.correction.datadialog.ParamDialog;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPItemValue;
import nts.uk.screen.at.app.dailyperformance.correction.dto.calctime.DCCalcTime;
import nts.uk.screen.at.app.dailyperformance.correction.dto.calctime.DCCellEdit;
import nts.uk.screen.at.app.dailyperformance.correction.dto.type.TypeLink;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class DailyCorrectCalcTimeService {

	public static final int MINUTES_OF_DAY = 24 * 60;

	private static final String FORMAT_HH_MM = "%d:%02d";

	@Inject
	private DailyCorrectEventServiceCenter dailyCorrectEventServiceCenter;
	
	@Inject
	private DataDialogWithTypeProcessor dataDialogWithTypeProcessor;
	
	@Inject
	private ValidatorDataDailyRes validatorDataDaily;
	

	public DCCalcTime calcTime(List<DailyRecordDto> dailyEdits, List<DPItemValue> itemEdits, Boolean changeSpr31, Boolean changeSpr34) {
		
	    DCCalcTime calcTime = new DCCalcTime();
        
		DPItemValue itemEditCalc = itemEdits.stream().filter(x -> !x.getColumnKey().equals("USE")).findFirst().get();
		getWplPosId(itemEdits);
		
		DailyRecordDto dtoEdit = dailyEdits.stream()
				.filter(x -> equalEmpAndDate(x.getEmployeeId(), x.getDate(), itemEditCalc)).findFirst().orElse(null);
		if((changeSpr31 != null || changeSpr34 != null) && dtoEdit.getTimeLeaving().isPresent() && !dtoEdit.getTimeLeaving().get().getWorkAndLeave().isEmpty()) {
				dtoEdit.getTimeLeaving().get().getWorkAndLeave().stream().filter(x -> x.getNo() == 1).forEach(x -> {
					if(changeSpr31 != null && changeSpr31.booleanValue() && x.getWorking() != null) x.getWorking().getTime().setStampSourceInfo(StampSourceInfo.SPR.value);
					if(changeSpr34 != null && changeSpr34.booleanValue() && x.getLeave() != null) x.getLeave().getTime().setStampSourceInfo(StampSourceInfo.SPR.value);
				});
		}

		val itemValues = itemEdits.stream().map(x -> new ItemValue(x.getValue(), x.getValueType() == null ? ValueType.UNKNOWN : ValueType.valueOf(x.getValueType()),
				x.getLayoutCode(), x.getItemId())).collect(Collectors.toList());
//		val itemBase =  new ItemValue(itemEditCalc.getValue(), ValueType.valueOf(itemEditCalc.getValueType()),
//				itemEditCalc.getLayoutCode(), itemEditCalc.getItemId());
		
		addEditState(dtoEdit, itemEdits);
		
		DailyModifyRCResult updated = DailyModifyRCResult.builder().employeeId(itemEditCalc.getEmployeeId())
				.workingDate(itemEditCalc.getDate()).items(itemValues).completed();

		checkInput28And1(dtoEdit, itemEdits);
		
		String companyId = AppContexts.user().companyId();
		
//		AttendanceItemUtil.fromItemValues(dtoEdit, Arrays.asList(itemBase));
		AttendanceItemUtil.fromItemValues(dtoEdit, itemValues);

		EventCorrectResult result = dailyCorrectEventServiceCenter.correctRunTime(dtoEdit, updated, companyId);
		List<ItemValue> items = result.getCorrectedItemsWithStrict();

		DailyRecordDto resultBaseDto = result.getCorrected().workingDate(itemEditCalc.getDate()).employeeId(itemEditCalc.getEmployeeId());

		val dailyEditsResult = dailyEdits.stream().map(x -> {
			if (equalEmpAndDate(x.getEmployeeId(), x.getDate(), itemEditCalc)) {
				return resultBaseDto;
			} else {
				return x;
			}
		}).collect(Collectors.toList());
		calcTime.setCellEdits(items.stream().map(x -> new DCCellEdit(itemEditCalc.getRowId(), "A" + x.getItemId(),
				convertData(x.getValueType().value, x.getValue()))).collect(Collectors.toList()));
		calcTime.setDailyEdits(dailyEditsResult);
		return calcTime;
	}
	
	private DailyRecordDto addEditState(DailyRecordDto dtoEdit, List<DPItemValue> itemEdits){
		val sidLogin = AppContexts.user().employeeId();
		val dtoEditState = itemEdits.stream().map(x -> EditStateOfDailyPerformanceDto.createWith(x.getEmployeeId(),
				x.getItemId(), x.getDate(),
				x.getEmployeeId().equals(sidLogin)
						? EditStateSetting.HAND_CORRECTION_MYSELF.value : EditStateSetting.HAND_CORRECTION_OTHER.value))
				.collect(Collectors.toList());

		dtoEdit.getEditStates().addAll(dtoEditState);
		return dtoEdit;
	}

	private Object convertData(int valueType, String value) {
		switch (valueType) {
		case 1:
		case 2:
		case 15:
			return value == null ? "" : converTime(valueType, value);
		default:
			return value == null ? "" : value;
		}
	}

	private String converTime(int valueType, String value) {
		int minute = 0;
		if (Integer.parseInt(value) >= 0) {
			minute = Integer.parseInt(value);
		} else {
			if (valueType == ValueType.TIME_WITH_DAY.value) {
				minute = 0 - ((Integer.parseInt(value)
						+ (1 + -Integer.parseInt(value) / MINUTES_OF_DAY) * MINUTES_OF_DAY));
			} else {
				minute = Integer.parseInt(value);
			}
		}
		int hours = minute / 60;
		int minutes = Math.abs(minute) % 60;
		String valueConvert = (minute < 0 && hours == 0) ? "-" + String.format(FORMAT_HH_MM, hours, minutes)
				: String.format(FORMAT_HH_MM, hours, minutes);
		return valueConvert;
	}

	private boolean equalEmpAndDate(String employee, GeneralDate date, DPItemValue itemEdit) {
		return employee.equals(itemEdit.getEmployeeId()) && date.equals(itemEdit.getDate());
	}
	
	private void getWplPosId(List<DPItemValue> itemEdits) {
		// map id -> code possition and workplace
		itemEdits.stream().map(itemEdit -> {
			if(itemEdit.getTypeGroup() == null) return itemEdit;
			if (itemEdit.getTypeGroup() == TypeLink.POSSITION.value) {
				CodeName codeName = dataDialogWithTypeProcessor.getTypeDialog(itemEdit.getTypeGroup(),
						new ParamDialog(itemEdit.getDate(), itemEdit.getValue()));
				itemEdit.setValue(codeName == null ? null : codeName.getId());
				return itemEdit;
			} else if (itemEdit.getTypeGroup() == TypeLink.WORKPLACE.value) {
				CodeName codeName = dataDialogWithTypeProcessor.getTypeDialog(itemEdit.getTypeGroup(),
						new ParamDialog(itemEdit.getDate(), itemEdit.getValue()));
				itemEdit.setValue(codeName == null ? null : codeName.getId());
				return itemEdit;
			}
			return itemEdit;
		}).collect(Collectors.toList());
	}
	
	private void checkInput28And1(DailyRecordDto dailyEdit, List<DPItemValue> itemEditCalc) {
		DailyModifyResult updated = DailyModifyResult.builder().employeeId(dailyEdit.getEmployeeId())
				.workingDate(dailyEdit.getDate()).items(AttendanceItemUtil.toItemValues(dailyEdit)).completed();
		List<DPItemValue> resultError = validatorDataDaily.checkInput28And1(itemEditCalc,
				Arrays.asList(updated));
		if (!resultError.isEmpty())
			throw new BusinessException(resultError.get(0).getMessage());
	}
}

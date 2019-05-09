package nts.uk.screen.at.app.dailyperformance.correction.calctime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.tuple.Pair;

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
import nts.uk.screen.at.app.dailymodify.command.DailyModifyResCommandFacade;
import nts.uk.screen.at.app.dailymodify.query.DailyModifyQuery;
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
	
	@Inject
	private DailyModifyResCommandFacade dailyModifyResFacade;

	public DCCalcTime calcTime(List<DailyRecordDto> dailyEdits, List<DPItemValue> itemEdits, Boolean changeSpr31,
			Boolean changeSpr34,  boolean notChangeCell) {

		String companyId = AppContexts.user().companyId();
		DCCalcTime calcTime = new DCCalcTime();

		DPItemValue itemEditCalc = itemEdits.stream().filter(x -> !x.getColumnKey().equals("USE")).findFirst().get();
		getWplPosId(itemEdits);

		DailyRecordDto dtoEdit = dailyEdits.stream()
				.filter(x -> equalEmpAndDate(x.getEmployeeId(), x.getDate(), itemEditCalc)).findFirst().orElse(null);
		
		//セットされている勤務種類、就業時間帯が「マスタ未登録」でないかチェックする
		val checkMaster = checkHasMasterWorkTypeTime(companyId, dtoEdit, itemEdits);
		if(!checkMaster.getLeft() || !checkMaster.getRight()) {
			calcTime.setCellEdits(new ArrayList<>());
			calcTime.setDailyEdits(dailyEdits);
			calcTime.setErrorFindMaster28(!checkMaster.getLeft());
			calcTime.setErrorFindMaster29(!checkMaster.getRight());
			return calcTime;
		};
		
		if ((changeSpr31 != null || changeSpr34 != null) && dtoEdit.getTimeLeaving().isPresent()
				&& !dtoEdit.getTimeLeaving().get().getWorkAndLeave().isEmpty()) {
			dtoEdit.getTimeLeaving().get().getWorkAndLeave().stream().filter(x -> x.getNo() == 1).forEach(x -> {
				if (changeSpr31 != null && changeSpr31.booleanValue() && x.getWorking() != null)
					x.getWorking().getTime().setStampSourceInfo(StampSourceInfo.SPR.value);
				if (changeSpr34 != null && changeSpr34.booleanValue() && x.getLeave() != null)
					x.getLeave().getTime().setStampSourceInfo(StampSourceInfo.SPR.value);
			});
		}

		if (changeSpr31 != null && changeSpr31.booleanValue()) {
			val item31 = itemEdits.stream().filter(x -> x.getItemId() == 31).findFirst().orElse(null);
			val val688 = updateItemHistNo31(dtoEdit, item31);
			if(item31 != null) addEditState(dtoEdit, Arrays.asList(val688));
		}

		if (changeSpr34 != null && changeSpr34.booleanValue()) {
			val item34 = itemEdits.stream().filter(x -> x.getItemId() == 34).findFirst().orElse(null);
			val val689 = updateItemHistNo34(dtoEdit, item34);
			if(item34 != null) addEditState(dtoEdit, Arrays.asList(val689));
		}
		
		val itemValues = itemEdits.stream()
				.map(x -> new ItemValue(x.getValue(),
						x.getValueType() == null ? ValueType.UNKNOWN : ValueType.valueOf(x.getValueType()),
						x.getLayoutCode(), x.getItemId()))
				.collect(Collectors.toList());
		// val itemBase = new ItemValue(itemEditCalc.getValue(),
		// ValueType.valueOf(itemEditCalc.getValueType()),
		// itemEditCalc.getLayoutCode(), itemEditCalc.getItemId());

		if(!notChangeCell) addEditState(dtoEdit, itemEdits);
		
		dailyModifyResFacade.createStampSourceInfo(dtoEdit, Arrays.asList(new DailyModifyQuery(dtoEdit.getEmployeeId(), dtoEdit.getDate(), itemValues)));

		DailyModifyRCResult updated = DailyModifyRCResult.builder().employeeId(itemEditCalc.getEmployeeId())
				.workingDate(itemEditCalc.getDate()).items(itemValues).completed();

		checkInput28And1(dtoEdit, itemEdits);

		// AttendanceItemUtil.fromItemValues(dtoEdit, Arrays.asList(itemBase));
		AttendanceItemUtil.fromItemValues(dtoEdit, itemValues);

		EventCorrectResult result = dailyCorrectEventServiceCenter.correctRunTime(dtoEdit, updated, companyId);
		List<ItemValue> items = result.getCorrectedItemsWithStrict();

		DailyRecordDto resultBaseDto = result.getCorrected().workingDate(itemEditCalc.getDate())
				.employeeId(itemEditCalc.getEmployeeId());

		val dailyEditsResult = dailyEdits.stream().map(x -> {
			if (equalEmpAndDate(x.getEmployeeId(), x.getDate(), itemEditCalc)) {
				resultBaseDto.getWorkInfo().setVersion(x.getWorkInfo().getVersion());
				return resultBaseDto;
			} else {
				return x;
			}
		}).collect(Collectors.toList());
		calcTime.setCellEdits(items.stream().map(x -> new DCCellEdit(itemEditCalc.getRowId(), "A" + x.getItemId(),
				convertData(x.getValueType().value, x.getValue()))).collect(Collectors.toList()));
		calcTime.setDailyEdits(dailyEditsResult);
		calcTime.setErrorFindMaster28(false);
		calcTime.setErrorFindMaster29(false);
		return calcTime;
	}

	private DailyRecordDto addEditState(DailyRecordDto dtoEdit, List<DPItemValue> itemEdits) {
		val sidLogin = AppContexts.user().employeeId();
		val dtoEditState = itemEdits.stream()
				.map(x -> EditStateOfDailyPerformanceDto.createWith(x.getEmployeeId(), x.getItemId(), x.getDate(),
						x.getEmployeeId().equals(sidLogin) ? EditStateSetting.HAND_CORRECTION_MYSELF.value
								: EditStateSetting.HAND_CORRECTION_OTHER.value))
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
			if (itemEdit.getTypeGroup() == null)
				return itemEdit;
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
		List<DPItemValue> resultError = validatorDataDaily.checkInput28And1(itemEditCalc, Arrays.asList(updated));
		if (!resultError.isEmpty())
			throw new BusinessException(resultError.get(0).getMessage());
	}

	private DPItemValue updateItemHistNo34(DailyRecordDto dailyEdit, DPItemValue item34) {
		if(item34 == null) return null;
		ItemValue itemValue689 = new ItemValue(item34.getValue(), ValueType.TIME, "M_A49_A", 689);
		AttendanceItemUtil.fromItemValues(dailyEdit, Arrays.asList(itemValue689));
		return new DPItemValue("", dailyEdit.getEmployeeId(), dailyEdit.getDate(), 689);
	}

	private DPItemValue updateItemHistNo31(DailyRecordDto dailyEdit, DPItemValue item31) {
		if(item31 == null) return null;
		ItemValue itemValue688 = new ItemValue(item31.getValue(), ValueType.TIME, "M_A48_A", 688);
		AttendanceItemUtil.fromItemValues(dailyEdit, Arrays.asList(itemValue688));
		return new DPItemValue("", dailyEdit.getEmployeeId(), dailyEdit.getDate(), 688);
	}
	
	//セットされている勤務種類、就業時間帯が「マスタ未登録」でないかチェックする
	private Pair<Boolean, Boolean> checkHasMasterWorkTypeTime(String companyId, DailyRecordDto dailyEdit,  List<DPItemValue> itemEdits) {
		boolean hasMaster28 = true, hasMaster29 = true;
		boolean contentItem28 = itemEdits.stream().filter(x -> x.getItemId() == 28).findFirst().isPresent() ? true
				: false;
		boolean contentItem29 = itemEdits.stream().filter(x -> x.getItemId() == 29).findFirst().isPresent() ? true
				: false;

		List<DailyModifyResult> resultValues = AttendanceItemUtil.toItemValues(Arrays.asList(dailyEdit)).entrySet()
				.stream().map(c -> DailyModifyResult.builder().items(c.getValue()).workingDate(c.getKey().workingDate())
						.employeeId(c.getKey().employeeId()).completed())
				.collect(Collectors.toList());
		if (resultValues.isEmpty())
			return Pair.of(false, false);
		DailyModifyResult resultValue = resultValues.get(0);
		if (!contentItem28) {
			String value28 = resultValue.getItems().stream().filter(x -> x.getItemId() == 28).findFirst().orElse(null)
					.getValue();
			if (value28 == null || value28.isEmpty()) {
				hasMaster28 = true;
			}else {
				Optional<CodeName> codeName28opt = dataDialogWithTypeProcessor.getDutyTypeAll(companyId).getCodeNames()
						.stream().filter(x -> x.getCode().equals(value28)).findFirst();

				if (!codeName28opt.isPresent())
					hasMaster28 = false;
			}
			
		}

		if (!contentItem29) {
			String value29 = resultValue.getItems().stream().filter(x -> x.getItemId() == 29).findFirst().orElse(null)
					.getValue();

			if (value29 == null || value29.isEmpty()) {
				hasMaster29 = true;

			}else {
				Optional<CodeName> codeName29opt = dataDialogWithTypeProcessor.getWorkHoursAll(companyId).getCodeNames()
						.stream().filter(x -> x.getCode().equals(value29)).findFirst();

				if (!codeName29opt.isPresent())
					hasMaster29 = false;
			}

		}
		
		return  Pair.of(hasMaster28, hasMaster29);
	}
}

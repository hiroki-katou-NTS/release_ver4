package nts.uk.screen.at.app.dailymodify.command.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.tuple.Pair;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.command.dailyperform.DailyRecordWorkCommand;
import nts.uk.ctx.at.record.app.command.dailyperform.DailyRecordWorkCommandHandler;
import nts.uk.ctx.at.record.app.command.dailyperform.checkdata.RCDailyCorrectionResult;
import nts.uk.ctx.at.record.app.command.dailyperform.month.UpdateMonthDailyParam;
import nts.uk.ctx.at.record.app.find.dailyperform.DailyRecordDto;
import nts.uk.ctx.at.record.dom.daily.itemvalue.DailyItemValue;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.AttendanceItemUtil;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.erroralarm.EmployeeMonthlyPerError;
import nts.uk.ctx.at.shared.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionType;
import nts.uk.screen.at.app.dailymodify.command.DailyModifyResCommandFacade;
import nts.uk.screen.at.app.dailymodify.query.DailyModifyQuery;
import nts.uk.screen.at.app.dailymodify.query.DailyModifyResult;
import nts.uk.screen.at.app.dailyperformance.correction.checkdata.ValidatorDataDailyRes;
import nts.uk.screen.at.app.dailyperformance.correction.checkdata.dto.ErrorAfterCalcDaily;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPItemValue;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DataResultAfterIU;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DateRange;
import nts.uk.screen.at.app.dailyperformance.correction.dto.ResultReturnDCUpdateData;
import nts.uk.screen.at.app.dailyperformance.correction.dto.TypeError;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class ProcessDailyCalc {

	@Inject
	private ValidatorDataDailyRes validatorDataDaily;

	@Inject
	private DailyModifyResCommandFacade resCommandFacade;

	@Inject
	private DailyRecordWorkCommandHandler handler;

	private Map<Pair<String, GeneralDate>, ResultReturnDCUpdateData> checkBeforeCalc(DailyCalcParam param,
			List<DailyRecordDto> dailyEdits) {

		Map<Pair<String, GeneralDate>, List<DPItemValue>> mapSidDateEdit = param.getMapSidDateItemEdit();
		List<DPItemValue> lstNotFoundWorkType = param.getLstNotFoundWorkType();

		List<DailyModifyResult> newResultBefore = AttendanceItemUtil.toItemValues(dailyEdits).entrySet().stream()
				.map(dto -> DailyModifyResult.builder().items(dto.getValue()).employeeId(dto.getKey().getEmployeeId())
						.workingDate(dto.getKey().getDate()).completed())
				.collect(Collectors.toList());

		Map<Pair<String, GeneralDate>, List<DailyModifyResult>> mapSidDateData = newResultBefore.stream()
				.collect(Collectors.groupingBy(x -> Pair.of(x.getEmployeeId(), x.getDate())));

		Map<Pair<String, GeneralDate>, ResultReturnDCUpdateData> lstResultReturnDailyError = new HashMap<>();
		Map<Pair<String, GeneralDate>, ResultReturnDCUpdateData> lstResultTemp = new HashMap<>();
		for (Entry<Pair<String, GeneralDate>, List<DPItemValue>> x : mapSidDateEdit.entrySet()) {
			// 計算前エラーチェック
			// check error care item
			List<DPItemValue> itemErrors = new ArrayList<>();
			List<DPItemValue> itemInputErors = new ArrayList<>();
			List<DPItemValue> itemInputError28 = new ArrayList<>();
			List<DPItemValue> itemInputWorkType = new ArrayList<>();
			Map<Integer, List<DPItemValue>> errorTemp = new HashMap<>();
			List<DPItemValue> itemCovert = x.getValue().stream().collect(Collectors.toList()).stream()
					.filter(ProcessCommonCalc.distinctByKey(p -> p.getItemId())).collect(Collectors.toList());
			List<DailyModifyResult> itemValues = itemCovert.isEmpty() ? Collections.emptyList()
					: mapSidDateData.get(Pair.of(itemCovert.get(0).getEmployeeId(), itemCovert.get(0).getDate()));
			List<DailyModifyResult> itemOlds = param.getResultOlds().stream()
					.filter(y -> x.getKey().equals(Pair.of(y.getEmployeeId(), y.getDate())))
					.collect(Collectors.toList());
			List<DPItemValue> items = validatorDataDaily.checkCareItemDuplicate(itemCovert, itemOlds);
			itemErrors.addAll(items);
			List<DPItemValue> itemInputs = validatorDataDaily.checkInputData(itemCovert, itemValues);
			itemInputErors.addAll(itemInputs);
			itemInputWorkType = lstNotFoundWorkType.stream().filter(
					wt -> wt.getEmployeeId().equals(x.getKey().getLeft()) && wt.getDate().equals(x.getKey().getRight()))
					.collect(Collectors.toList());
			List<DPItemValue> itemInputs28 = validatorDataDaily.checkInput28And1(itemCovert, itemValues);
			itemInputError28.addAll(itemInputs28);
			if (!itemErrors.isEmpty() || !itemInputErors.isEmpty() || !itemInputError28.isEmpty()
					|| !itemInputWorkType.isEmpty()) {
				// 発生しているエラーを「エラー参照ダイアログ」に表示する
				if (!itemErrors.isEmpty())
					errorTemp.put(TypeError.DUPLICATE.value, itemErrors);
				if (!itemInputErors.isEmpty())
					errorTemp.put(TypeError.COUPLE.value, itemInputErors);
				if (!itemInputError28.isEmpty())
					errorTemp.put(TypeError.ITEM28.value, itemInputError28);
				if (!itemInputWorkType.isEmpty())
					errorTemp.put(TypeError.NOT_FOUND_WORKTYPE.value, itemInputWorkType);
				lstResultTemp.put(x.getKey(),
						new ResultReturnDCUpdateData(x.getKey().getLeft(), x.getKey().getRight(), errorTemp));
			}
		}
		lstResultReturnDailyError.putAll(lstResultTemp);
		return lstResultReturnDailyError;
	}

	public ErrorAfterCalcDaily checkErrorAfterCalcDaily(RCDailyCorrectionResult resultIU,
			List<DailyModifyResult> resultOlds, DateRange range, List<DailyRecordDto> dailyDtoEditAll,
			List<DPItemValue> lstItemEdits) {
		Map<Pair<String, GeneralDate>, ResultReturnDCUpdateData> resultErrorDaily = new HashMap<>();
		Map<Integer, List<DPItemValue>> resultErrorMonth = new HashMap<>();
		boolean hasError = false;
		DataResultAfterIU dataResultAfterIU = new DataResultAfterIU();

		val errorDivergence = validatorDataDaily.errorCheckDivergence(resultIU.getLstDailyDomain(),
				resultIU.getLstMonthDomain());
		if (!errorDivergence.isEmpty()) {
			resultErrorDaily.putAll(errorDivergence);
			hasError = true;
		}

		// 残数系のエラーチェック（月次集計なし）
		val sidChange = ProcessCommonCalc.itemInGroupChange(resultIU.getLstDailyDomain(), resultOlds);
		val pairError = resCommandFacade.mapDomainMonthChange(sidChange, resultIU.getLstDailyDomain(),
				resultIU.getLstMonthDomain(), dailyDtoEditAll, range, lstItemEdits);
		Map<Integer, List<DPItemValue>> errorMonth = validatorDataDaily.errorMonthNew(pairError.getErrorMonth());
		// val errorMonth = validatorDataDaily.errorMonth(resultIU.getLstMonthDomain(),
		// monthParam);
		List<EmployeeMonthlyPerError> errorYearHoliday = pairError.getErrorMonth().stream()
				.collect(Collectors.toList());
		Set<Pair<String, GeneralDate>> detailEmployeeError = new HashSet<>();
		if (!errorMonth.isEmpty() && !pairError.isOnlyErrorOldDb()) {
			resultErrorMonth.putAll(errorMonth);
			detailEmployeeError.addAll(pairError.getDetailEmployeeError());
			hasError = true;
		}

		return new ErrorAfterCalcDaily(hasError, resultErrorMonth, detailEmployeeError, resultErrorDaily,
				dataResultAfterIU.getFlexShortage(), errorYearHoliday);
	}

	public DailyCalcResult processDailyCalc(DailyCalcParam param, List<DailyRecordDto> dailyEdits,
			List<DailyRecordDto> dailyOlds, List<DailyItemValue> dailyItemOlds, List<DailyModifyQuery> queryChange,
			UpdateMonthDailyParam monthParam, boolean showErrorDialog, ExecutionType execType) {
		DataResultAfterIU dataResultAfterIU = new DataResultAfterIU();
		RCDailyCorrectionResult resultIU = new RCDailyCorrectionResult();
		// 計算前エラーチェック
		Map<Pair<String, GeneralDate>, ResultReturnDCUpdateData> lstResultReturnDailyError = checkBeforeCalc(param,
				dailyEdits);

		dailyEdits = dailyEdits.stream()
				.filter(x -> !lstResultReturnDailyError.containsKey(Pair.of(x.getEmployeeId(), x.getDate())))
				.collect(Collectors.toList());
		dailyOlds = dailyOlds.stream()
				.filter(x -> !lstResultReturnDailyError.containsKey(Pair.of(x.getEmployeeId(), x.getDate())))
				.collect(Collectors.toList());

		if (dailyEdits.isEmpty()) {
			dataResultAfterIU
					.setErrorMap(ProcessCommonCalc.convertErrorToType(lstResultReturnDailyError, new HashMap<>()));
			dataResultAfterIU.setMessageAlert("Msg_1489");
			dataResultAfterIU.setErrorAllSidDate(true);
			dataResultAfterIU.setShowErrorDialog(showErrorDialog);
			return new DailyCalcResult(dataResultAfterIU, null, null, new HashMap<>());
		}

		String sid = AppContexts.user().employeeId();
		List<DailyRecordWorkCommand> commandNew = ProcessCommonCalc.createCommands(sid, dailyEdits, queryChange);

		List<DailyRecordWorkCommand> commandOld = ProcessCommonCalc.createCommands(sid, dailyOlds, queryChange);

		resultIU = calcDaily(dailyEdits, commandNew, commandOld, dailyItemOlds, monthParam, execType);

		ErrorAfterCalcDaily checkErrorAfterCalcDaily = checkErrorAfterCalcDaily(resultIU, param.getResultOlds(),
				param.getDateRange(),
				param.getDailyDtoEditAll().stream()
						.filter(x -> !lstResultReturnDailyError.containsKey(Pair.of(x.getEmployeeId(), x.getDate())))
						.collect(Collectors.toList()),
				param.getItemValues().stream()
						.filter(x -> !lstResultReturnDailyError.containsKey(Pair.of(x.getEmployeeId(), x.getDate())))
						.collect(Collectors.toList()));

		checkErrorAfterCalcDaily.getResultError().putAll(lstResultReturnDailyError);
		return new DailyCalcResult(dataResultAfterIU, resultIU, checkErrorAfterCalcDaily, lstResultReturnDailyError);
	}

	public RCDailyCorrectionResult calcDaily(List<DailyRecordDto> dtoNews, List<DailyRecordWorkCommand> commandNew,
			List<DailyRecordWorkCommand> commandOld, List<DailyItemValue> dailyItems, UpdateMonthDailyParam month, ExecutionType execType) {

		val result = this.handler.processCalcDaily(commandNew, commandOld, dailyItems, true, month, execType);
		// 備考で日次エラー解除
		validatorDataDaily.removeErrorRemarkAll(AppContexts.user().companyId(), result.getLstDailyDomain(), dtoNews);
		return result;
	}

}
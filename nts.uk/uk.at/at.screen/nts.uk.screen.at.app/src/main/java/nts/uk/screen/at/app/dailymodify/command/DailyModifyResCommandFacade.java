package nts.uk.screen.at.app.dailymodify.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.commons.lang3.tuple.Pair;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.app.command.dailyperform.DailyRecordWorkCommand;
import nts.uk.ctx.at.record.app.command.dailyperform.DailyRecordWorkCommandHandler;
import nts.uk.ctx.at.record.app.command.dailyperform.audittrail.DPAttendanceItemRC;
import nts.uk.ctx.at.record.app.command.dailyperform.checkdata.RCDailyCorrectionResult;
import nts.uk.ctx.at.record.app.command.dailyperform.month.UpdateMonthDailyParam;
import nts.uk.ctx.at.record.app.find.dailyperform.DailyRecordDto;
import nts.uk.ctx.at.record.app.find.monthly.root.MonthlyRecordWorkDto;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.approvalmanagement.dailyperformance.algorithm.ContentApproval;
import nts.uk.ctx.at.record.dom.approvalmanagement.dailyperformance.algorithm.ParamDayApproval;
import nts.uk.ctx.at.record.dom.approvalmanagement.dailyperformance.algorithm.RegisterDayApproval;
import nts.uk.ctx.at.record.dom.daily.itemvalue.DailyItemValue;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.editstate.EditStateOfDailyPerformance;
import nts.uk.ctx.at.record.dom.editstate.enums.EditStateSetting;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.erroralarm.EmployeeMonthlyPerError;
import nts.uk.ctx.at.record.dom.monthly.erroralarm.ErrorType;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.IntegrationOfMonthly;
import nts.uk.ctx.at.record.dom.optitem.OptionalItemAtr;
import nts.uk.ctx.at.record.dom.optitem.OptionalItemRepository;
import nts.uk.ctx.at.record.dom.service.TimeOffRemainErrorInfor;
import nts.uk.ctx.at.record.dom.service.TimeOffRemainErrorInputParam;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerErrorRepository;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.algorithm.ParamIdentityConfirmDay;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.algorithm.RegisterIdentityConfirmDay;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.algorithm.SelfConfirmDay;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.IdentificationRepository;
import nts.uk.ctx.at.shared.dom.attendance.util.AttendanceItemUtil;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.EmpProvisionalInput;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.InterimRemainDataMngRegisterDateChange;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.RegisterProvisionalData;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.GetClosureStartForEmployee;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeClassification;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeUnit;
import nts.uk.screen.at.app.dailymodify.query.DailyModifyQuery;
import nts.uk.screen.at.app.dailymodify.query.DailyModifyResult;
import nts.uk.screen.at.app.dailyperformance.correction.DailyPerformanceCorrectionProcessor;
import nts.uk.screen.at.app.dailyperformance.correction.DailyPerformanceScreenRepo;
import nts.uk.screen.at.app.dailyperformance.correction.checkdata.ValidatorDataDailyRes;
import nts.uk.screen.at.app.dailyperformance.correction.checkdata.dto.ErrorAfterCalcDaily;
import nts.uk.screen.at.app.dailyperformance.correction.checkdata.dto.ItemFlex;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPAttendanceItem;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPItemCheckBox;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPItemParent;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPItemValue;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DataResultAfterIU;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DateRange;
import nts.uk.screen.at.app.dailyperformance.correction.dto.OperationOfDailyPerformanceDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.TypeError;
import nts.uk.screen.at.app.dailyperformance.correction.dto.month.DPMonthValue;
import nts.uk.screen.at.app.dailyperformance.correction.text.DPText;
import nts.uk.screen.at.app.monthlyperformance.correction.command.MonthModifyCommandFacade;
import nts.uk.screen.at.app.monthlyperformance.correction.query.MonthlyModifyQuery;
import nts.uk.screen.at.app.monthlyperformance.correction.query.MonthlyModifyResult;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
@Transactional
/** 日別修正CommandFacade */
public class DailyModifyResCommandFacade {

	@Inject
	private DailyRecordWorkCommandHandler handler;

	@Inject
	private RegisterIdentityConfirmDay registerIdentityConfirmDay;

	@Inject
	private RegisterDayApproval registerDayApproval;

	@Inject
	private OptionalItemRepository optionalMasterRepo;

	@Inject
	private ValidatorDataDailyRes validatorDataDaily;

	@Inject
	private DailyPerformanceCorrectionProcessor processor;

	@Inject
	private MonthModifyCommandFacade monthModifyCommandFacade;

	@Inject
	private InterimRemainDataMngRegisterDateChange interimRemainDataMngRegisterDateChange;

	@Inject
	private RegisterProvisionalData registerProvisionalData;

	@Inject
	private InsertAllData insertAllData;
	
	@Inject
	private IdentificationRepository identificationRepository;
	
	@Inject
	private EmployeeDailyPerErrorRepository employeeDailyPerErrorRepository;
	
	@Inject
	private DailyPerformanceScreenRepo repo;
	
	@Inject
	private GetClosureStartForEmployee getClosureStartForEmployee;
	
	@Inject
	private TimeOffRemainErrorInfor timeOffRemainErrorInfor;
	
	@Inject
	private WorkTypeRepository workTypeRepository;

	public RCDailyCorrectionResult handleUpdate(List<DailyModifyQuery> querys, List<DailyRecordDto> dtoOlds,
			List<DailyRecordDto> dtoNews, List<DailyRecordWorkCommand> commandNew, List<DailyRecordWorkCommand> commandOld, List<DailyItemValue> dailyItems, UpdateMonthDailyParam month, int mode,
			boolean flagCalculation, Map<Integer, DPAttendanceItemRC> lstAttendanceItem, DPItemParent dataParent) {
		
		if (!flagCalculation) {
			val result =  this.handler.processCalcDaily(commandNew, commandOld, dailyItems, true, month);
			validatorDataDaily.removeErrorRemarkAll(AppContexts.user().companyId(), result.getLstDailyDomain(), dtoNews);
			return result;
		} else {
//			List<EmployeeDailyPerErrorDto> lstErrorDto = dtoNews.stream().map(result -> result.getErrors())
//					.flatMap(List::stream).collect(Collectors.toList());
//			List<EmployeeDailyPerError> lstError = lstErrorDto.stream()
//					.map(x -> x.toDomain(x.getEmployeeID(), x.getDate())).collect(Collectors.toList());
//			lstError = validatorDataDaily.removeErrorRemark(AppContexts.user().companyId(), lstError, dtoNews);
			val result = this.handler.handlerNoCalc(commandNew, commandOld, new ArrayList<>(), dailyItems, true, month, mode,
					lstAttendanceItem);
			validatorDataDaily.removeErrorRemarkAll(AppContexts.user().companyId(), result.getLstDailyDomain(), dtoNews);
//			if (dataParent.getSpr() != null) {
//				processor.insertStampSourceInfo(dataParent.getSpr().getEmployeeId(), dataParent.getSpr().getDate(),
//						dataParent.getSpr().isChange31(), dataParent.getSpr().isChange34());
//			}
			return result;
		}
	}

	private List<EditStateOfDailyPerformance> convertTo(String sid, DailyModifyQuery query) {
		List<EditStateOfDailyPerformance> editData = query.getItemValues().stream().map(x -> {
			return new EditStateOfDailyPerformance(query.getEmployeeId(), x.getItemId(), query.getBaseDate(),
					sid.equals(query.getEmployeeId()) ? EditStateSetting.HAND_CORRECTION_MYSELF
							: EditStateSetting.HAND_CORRECTION_OTHER);
		}).collect(Collectors.toList());
		return editData;
	}

	private void processDto(List<DailyRecordDto> dailyOlds, List<DailyRecordDto> dailyEdits, DPItemParent dataParent,
			List<DailyModifyQuery> querys, Map<Pair<String, GeneralDate>, List<DPItemValue>> mapSidDate,
			List<DailyModifyQuery> queryNotChanges) {
		if (!querys.isEmpty() && !dataParent.isFlagCalculation()) {
			dailyOlds.addAll(dataParent.getDailyOlds().stream()
					.filter(x -> mapSidDate.containsKey(Pair.of(x.getEmployeeId(), x.getDate())))
					.collect(Collectors.toList()));
			List<DailyRecordDto> temp = dataParent.getDailyEdits().stream()
					.filter(x -> mapSidDate.containsKey(Pair.of(x.getEmployeeId(), x.getDate())))
					.collect(Collectors.toList());
			dailyEdits.addAll(queryNotChanges.isEmpty() ? temp : toDto(queryNotChanges, temp));
		} else {
			dailyOlds.addAll(dataParent.getDailyOlds());
			dailyEdits.addAll(dataParent.getDailyEdits());
		}
		Map<Integer, OptionalItemAtr> optionalMaster = optionalMasterRepo.findAll(AppContexts.user().companyId())
				.stream().collect(Collectors.toMap(c -> c.getOptionalItemNo().v(), c -> c.getOptionalItemAtr()));

		dailyOlds.stream().forEach(o -> {
			o.getOptionalItem().ifPresent(optional -> {
				optional.correctItemsWith(optionalMaster);
			});
		});

		dailyEdits.stream().forEach(o -> {
			o.getOptionalItem().ifPresent(optional -> {
				optional.correctItemsWith(optionalMaster);
			});
		});
	}

	public List<DailyRecordDto> toDto(List<DailyModifyQuery> querys, List<DailyRecordDto> dtoEdits) {
		List<DailyRecordDto> dtoNews = new ArrayList<>();

		dtoNews = dtoEdits.stream().map(o -> {
			val itemChanges = querys.stream()
					.filter(q -> q.getBaseDate().equals(o.workingDate()) && q.getEmployeeId().equals(o.employeeId()))
					.findFirst();
			if (!itemChanges.isPresent())
				return o;
			List<ItemValue> itemValues = itemChanges.get().getItemValues();

			AttendanceItemUtil.fromItemValues(o, itemValues);

			o.getTimeLeaving().ifPresent(dto -> {
				if (dto.getWorkAndLeave() != null)
					dto.getWorkAndLeave().removeIf(tl -> tl.getWorking() == null && tl.getLeave() == null);
			});
			return o;
		}).collect(Collectors.toList());
		return dtoNews;
	}

	private DailyRecordWorkCommand createCommand(String sid, DailyRecordDto dto, DailyModifyQuery query) {
		if (query == null) {
			return DailyRecordWorkCommand.open().withData(dto).forEmployeeIdAndDate(dto.employeeId(), dto.getDate())
					.fromItems(Collections.emptyList());
		}
		DailyRecordWorkCommand command = DailyRecordWorkCommand.open().forEmployeeId(query.getEmployeeId())
				.withWokingDate(query.getBaseDate()).withData(dto)
				.fromItems(query == null ? Collections.emptyList() : query.getItemValues());
		command.getEditState().updateDatas(convertTo(sid, query));
		return command;
	}

	private List<DailyRecordWorkCommand> createCommands(String sid, List<DailyRecordDto> lstDto,
			List<DailyModifyQuery> querys) {
		if (querys.isEmpty())
			return lstDto.stream().map(o -> {
				return createCommand(sid, o, null);
			}).collect(Collectors.toList());

		return lstDto.stream().map(o -> {
			DailyModifyQuery query = querys.stream()
					.filter(q -> q.getBaseDate().equals(o.workingDate()) && q.getEmployeeId().equals(o.employeeId()))
					.findFirst().orElse(null);
			return createCommand(sid, o, query);
		}).collect(Collectors.toList());
	}

	public DataResultAfterIU insertItemDomain(DPItemParent dataParent) {
		Map<Integer, List<DPItemValue>> resultError = new HashMap<>();
		DataResultAfterIU dataResultAfterIU = new DataResultAfterIU();
		// insert flex
		UpdateMonthDailyParam monthParam = null;
		if (dataParent.getMonthValue() != null) {
			val month = dataParent.getMonthValue();
			if (month != null && month.getItems() != null) {
				MonthlyModifyQuery monthQuery = new MonthlyModifyQuery(month.getItems().stream().map(x -> {
					return ItemValue.builder().itemId(x.getItemId()).layout(x.getLayoutCode()).value(x.getValue())
							.valueType(ValueType.valueOf(x.getValueType())).withPath("");
				}).collect(Collectors.toList()), month.getYearMonth(), month.getEmployeeId(), month.getClosureId(),
						month.getClosureDate());
				IntegrationOfMonthly domainMonth = monthModifyCommandFacade.toDto(monthQuery).toDomain(
						month.getEmployeeId(), new YearMonth(month.getYearMonth()), month.getClosureId(),
						month.getClosureDate());
				Optional<IntegrationOfMonthly> domainMonthOpt = Optional.of(domainMonth);
				monthParam = new UpdateMonthDailyParam(month.getYearMonth(), month.getEmployeeId(),
						month.getClosureId(), month.getClosureDate(), domainMonthOpt,
						new DatePeriod(dataParent.getDateRange().getStartDate(),
								dataParent.getDateRange().getEndDate()),
						month.getRedConditionMessage(), month.getHasFlex(), month.getNeedCallCalc());
			} else {
				monthParam = new UpdateMonthDailyParam(month.getYearMonth(), month.getEmployeeId(),
						month.getClosureId(), month.getClosureDate(), Optional.empty(),
						new DatePeriod(dataParent.getDateRange().getStartDate(),
								dataParent.getDateRange().getEndDate()),
						month.getRedConditionMessage(), month.getHasFlex(), month.getNeedCallCalc());
			}
		}

		Map<Pair<String, GeneralDate>, List<DPItemValue>> mapSidDate = dataParent.getItemValues().stream()
				.collect(Collectors.groupingBy(x -> Pair.of(x.getEmployeeId(), x.getDate())));

		Map<Pair<String, GeneralDate>, List<DPItemValue>> mapSidDateNotChange = dataParent.getItemValues().stream()
				.filter(x -> !DPText.ITEM_CHANGE.contains(x.getItemId()))
				.collect(Collectors.groupingBy(x -> Pair.of(x.getEmployeeId(), x.getDate())));

		List<DailyModifyQuery> querys = createQuerys(mapSidDate);
		List<DailyModifyQuery> queryNotChanges = createQuerys(mapSidDateNotChange);
		// map to list result -> check error;
		List<DailyRecordDto> dailyOlds = new ArrayList<>(), dailyEdits = new ArrayList<>();

		processDto(dailyOlds, dailyEdits, dataParent, querys, mapSidDate, queryNotChanges);

		List<DailyModifyResult> resultOlds = AttendanceItemUtil.toItemValues(dailyOlds).entrySet().stream()
				.map(dto -> DailyModifyResult.builder().items(dto.getValue()).employeeId(dto.getKey().getEmployeeId())
						.workingDate(dto.getKey().getDate()).completed())
				.collect(Collectors.toList());
		
		List<DailyModifyResult> newResultBefore = AttendanceItemUtil.toItemValues(dailyEdits).entrySet().stream()
				.map(dto -> DailyModifyResult.builder().items(dto.getValue()).employeeId(dto.getKey().getEmployeeId())
						.workingDate(dto.getKey().getDate()).completed())
				.collect(Collectors.toList());

		Map<Pair<String, GeneralDate>, List<DailyModifyResult>> mapSidDateData = newResultBefore.stream()
				.collect(Collectors.groupingBy(x -> Pair.of(x.getEmployeeId(), x.getDate())));

		// check error care item
		List<DPItemValue> itemErrors = new ArrayList<>();
		List<DPItemValue> itemInputErors = new ArrayList<>();
		List<DPItemValue> itemInputError28 = new ArrayList<>();
		// List<DPItemValue> itemInputDeviation = new ArrayList<>();
		//計算フラグをチェックする
		if (!dataParent.isFlagCalculation()) {
			mapSidDate.entrySet().forEach(x -> {
				//計算前エラーチェック
				List<DPItemValue> itemCovert = x.getValue().stream().filter(y -> y.getValue() != null)
						.collect(Collectors.toList()).stream().filter(distinctByKey(p -> p.getItemId()))
						.collect(Collectors.toList());
				List<DailyModifyResult> itemValues = itemCovert.isEmpty() ? Collections.emptyList()
						: mapSidDateData.get(Pair.of(itemCovert.get(0).getEmployeeId(), itemCovert.get(0).getDate()));
				List<DPItemValue> items = validatorDataDaily.checkCareItemDuplicate(itemCovert);
				if (!items.isEmpty()) {
					itemErrors.addAll(items);
				} else {
					List<DPItemValue> itemInputs = validatorDataDaily.checkInputData(itemCovert, itemValues);
					itemInputErors.addAll(itemInputs);
				}

				List<DPItemValue> itemInputs28 = validatorDataDaily.checkInput28And1(itemCovert, itemValues);
				itemInputError28.addAll(itemInputs28);

			});
		}
		
		// insert , update item
		boolean hasError = false;
		if (!itemErrors.isEmpty() || !itemInputErors.isEmpty() || !itemInputError28.isEmpty()) {
			//発生しているエラーを「エラー参照ダイアログ」に表示する
			resultError.put(TypeError.DUPLICATE.value, itemErrors);
			resultError.put(TypeError.COUPLE.value, itemInputErors);
			resultError.put(TypeError.ITEM28.value, itemInputError28);
			dataResultAfterIU.setErrorMap(resultError);
			return dataResultAfterIU;
		}
		
		RCDailyCorrectionResult resultIU = new RCDailyCorrectionResult();
		List<DPItemValue> errorRelease = new ArrayList<>();
		List<DailyItemValue> dailyItems = resultOlds.stream().map(
				x -> DailyItemValue.build().createEmpAndDate(x.getEmployeeId(), x.getDate()).createItems(x.getItems()))
				.collect(Collectors.toList());
		if (querys.isEmpty() && !dataParent.isFlagCalculation()
				&& (dataParent.getMonthValue() == null || dataParent.getMonthValue().getItems() == null)) {
			errorRelease = releaseSign(dataParent.getDataCheckSign(), new ArrayList<>(), dailyEdits,
					AppContexts.user().employeeId(), true);
			// only insert check box
			// insert sign
			insertSign(dataParent.getDataCheckSign());
			// insert approval
			insertApproval(dataParent.getDataCheckApproval());
			dataResultAfterIU.setShowErrorDialog(null);

		} else {
			// if (querys.isEmpty() ? !dataParent.isFlagCalculation() :
			// true) {
			Map<Integer, DPAttendanceItemRC> itemAtr = dataParent.getLstAttendanceItem().entrySet().stream()
					.collect(Collectors.toMap(x -> x.getKey(), x -> convertItemAtr(x.getValue())));
			
			//日別実績の修正からの計算
			String sid = AppContexts.user().employeeId();

			List<DailyRecordWorkCommand> commandNew = createCommands(sid, dailyEdits, querys);

			List<DailyRecordWorkCommand> commandOld = createCommands(sid, dailyOlds, querys);
			
			resultIU = handleUpdate(querys, dailyOlds, dailyEdits, commandNew, commandOld, dailyItems, monthParam, dataParent.getMode(),
					dataParent.isFlagCalculation(), itemAtr, dataParent);
			
			
			if (resultIU != null) {
				//計算後エラーチェック
				ErrorAfterCalcDaily errorCheck = checkErrorAfterCalcDaily(resultIU, monthParam, resultOlds, dataParent.getMode(), dataParent.getMonthValue(), dataParent.getDateRange(), dataParent.getDailyEdits(), dataParent.getItemValues());
				hasError = errorCheck.getHasError();
				resultError = errorCheck.getResultError();
				if(hasError) {
					dataResultAfterIU.setErrorMap(resultError);
					return dataResultAfterIU;
				}

				//乖離エラー発生時の本人確認解除
				val errorSign = validatorDataDaily.releaseDivergence(resultIU.getLstDailyDomain());
				if (!errorSign.isEmpty()) {
					// resultError.putAll(errorSign);
					errorRelease = releaseSign(dataParent.getDataCheckSign(), errorSign, dailyEdits,
							AppContexts.user().employeeId(), false);
				}

				//日次登録処理
				this.insertAllData.handlerInsertAllDaily(resultIU.getCommandNew(), resultIU.getLstDailyDomain(),
						resultIU.getCommandOld(), dailyItems, resultIU.isUpdate(),
						monthParam, itemAtr);
				// insert sign
				insertSign(dataParent.getDataCheckSign());
				// insert approval
				insertApproval(dataParent.getDataCheckApproval());

				if (dataParent.getSpr() != null) {
					processor.insertStampSourceInfo(dataParent.getSpr().getEmployeeId(), dataParent.getSpr().getDate(),
							dataParent.getSpr().isChange31(), dataParent.getSpr().isChange34());
				}

				// 暫定データを登録する - Register provisional data
				List<DailyModifyResult> resultNews = AttendanceItemUtil.toItemValues(dailyEdits).entrySet().stream()
						.map(dto -> DailyModifyResult.builder().items(dto.getValue())
								.employeeId(dto.getKey().getEmployeeId()).workingDate(dto.getKey().getDate())
								.completed())
						.collect(Collectors.toList());

				registerTempData(dataParent.getMode(), resultOlds, resultNews);
				dataResultAfterIU.setShowErrorDialog(showError(resultIU.getLstDailyDomain(), new ArrayList<>()));
				
				//processCalcMonth
				RCDailyCorrectionResult resultMonth = this.handler.processCalcMonth(commandNew, commandOld, resultIU.getLstDailyDomain(), dailyItems, true, monthParam, dataParent.getMode());
				
				//月次登録処理
				this.insertAllData.handlerInsertAllMonth(resultMonth.getLstMonthDomain(), monthParam);
				
				ErrorAfterCalcDaily errorMonth = checkErrorAfterCalcMonth(resultMonth, monthParam, resultOlds, dataParent.getMode(), dataParent.getMonthValue(), dataParent.getDateRange());
				//dataResultAfterIU.setErrorMap(errorMonth.getResultError());
				dataResultAfterIU.setFlexShortage(errorMonth.getFlexShortage());
				
			} else {
				if (dataParent.getDataCheckSign() != null && !dataParent.getDataCheckSign().isEmpty())
					insertSign(dataParent.getDataCheckSign());
				// insert approval
				if (dataParent.getDataCheckApproval() != null && !dataParent.getDataCheckApproval().isEmpty())
					insertApproval(dataParent.getDataCheckApproval());
				dataResultAfterIU.setShowErrorDialog(showError(new ArrayList<>(), dailyEdits));
			}
		}

		if(!errorRelease.isEmpty()) {
			resultError.put(TypeError.RELEASE_CHECKBOX.value, errorRelease);
		}
		
		if (dataParent.getMode() == 0) {
			List<DPItemValue> dataCheck = new ArrayList<>();
			if (!dataParent.isFlagCalculation() && resultIU.getCommandNew() != null) {
				dataCheck = validatorDataDaily.checkContinuousHolidays(dataParent.getEmployeeId(),
						dataParent.getDateRange(), resultIU.getCommandNew().stream().map(c -> c.getWorkInfo().getData())
								.filter(c -> c != null).collect(Collectors.toList()));
			}else if(dataParent.isFlagCalculation()) {
				dataCheck = validatorDataDaily.checkContinuousHolidays(dataParent.getEmployeeId(),
						dataParent.getDateRange(), dailyEdits.stream().map(c -> c.getWorkInfo().toDomain(null, null))
								.filter(c -> c != null).collect(Collectors.toList()));
			}
			if (!dataCheck.isEmpty()) {
				resultError.put(TypeError.CONTINUOUS.value, dataCheck);
			}
		}
		dataResultAfterIU.setErrorMap(resultError);
		return dataResultAfterIU;
	}

	private List<DailyModifyQuery> createQuerys(Map<Pair<String, GeneralDate>, List<DPItemValue>> mapSidDate) {
		List<DailyModifyQuery> querys = new ArrayList<>();
		mapSidDate.entrySet().forEach(x -> {
			List<ItemValue> itemCovert = x.getValue().stream()
					.map(y -> new ItemValue(y.getValue(), ValueType.valueOf(y.getValueType()), y.getLayoutCode(),
							y.getItemId()))
					.collect(Collectors.toList()).stream().filter(distinctByKey(p -> p.itemId()))
					.collect(Collectors.toList());
			if (!itemCovert.isEmpty())
				querys.add(new DailyModifyQuery(x.getKey().getKey(), x.getKey().getValue(), itemCovert));
		});
		return querys;
	}

	public void insertSign(List<DPItemCheckBox> dataCheckSign) {
		if (dataCheckSign.isEmpty())
			return;
		ParamIdentityConfirmDay day = new ParamIdentityConfirmDay(AppContexts.user().employeeId(), dataCheckSign
				.stream().map(x -> new SelfConfirmDay(x.getDate(), x.isValue())).collect(Collectors.toList()));
		registerIdentityConfirmDay.registerIdentity(day);
	}

	public void insertApproval(List<DPItemCheckBox> dataCheckApproval) {
		if (dataCheckApproval.isEmpty())
			return;
		ParamDayApproval param = new ParamDayApproval(AppContexts.user().employeeId(),
				dataCheckApproval.stream()
						.map(x -> new ContentApproval(x.getDate(), x.isValue(), x.getEmployeeId(), x.isFlagRemoveAll()))
						.collect(Collectors.toList()));
		registerDayApproval.registerDayApproval(param);
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		final Set<Object> seen = new HashSet<>();
		return t -> seen.add(keyExtractor.apply(t));
	}

	public List<EmployeeMonthlyPerError> mapDomainMonthChange(List<Pair<String, GeneralDate>> employeeChange,
			List<IntegrationOfDaily> domainDailyNew, List<IntegrationOfMonthly> domainMonthNew, List<DailyRecordDto> dailyDtoEditAll, DateRange dateRange, List<DPItemValue> lstItemEdits) {
		Set<String> employeeIds = employeeChange.stream().map(x -> x.getLeft()).collect(Collectors.toSet());
		String companyId = AppContexts.user().companyId();
		List<EmployeeMonthlyPerError> monthPer = new ArrayList<>();
		employeeIds.stream().forEach(emp -> {
			List<IntegrationOfDaily> domainDailyEditAll  = dailyDtoEditAll.stream().filter(x -> x.getEmployeeId().equals(emp)).map(x -> x.toDomain(null, null)).collect(Collectors.toList());
			domainDailyEditAll = unionDomain(domainDailyEditAll, domainDailyNew);
			// Acquire closing date corresponding to employee
			List<IntegrationOfDaily> dailyOfEmp = domainDailyEditAll.stream()
					.filter(x -> x.getWorkInformation().getEmployeeId().equals(emp)).collect(Collectors.toList());
			List<AttendanceTimeOfDailyPerformance> lstAttendanceTimeData = dailyOfEmp.stream()
					.filter(x -> x.getAttendanceTimeOfDailyPerformance().isPresent())
					.map(x -> x.getAttendanceTimeOfDailyPerformance().get()).collect(Collectors.toList());

			List<WorkInfoOfDailyPerformance> lstWorkInfor = dailyOfEmp.stream()
					.filter(x -> x.getWorkInformation() != null).map(x -> x.getWorkInformation())
					.collect(Collectors.toList()).stream().sorted((x, y) -> x.getYmd().compareTo(y.getYmd())).collect(Collectors.toList());

			Optional<GeneralDate> date = getClosureStartForEmployee.algorithm(emp);
			List<EmployeeMonthlyPerError> lstEmpMonthError = new ArrayList<>();
			if (domainMonthNew != null && !domainMonthNew.isEmpty()) {
				for (IntegrationOfMonthly month : domainMonthNew) {
					TimeOffRemainErrorInputParam param = new TimeOffRemainErrorInputParam(companyId, emp,
							new DatePeriod(date.get(), date.get().addYears(1).addDays(-1)),
							new DatePeriod(dateRange.getStartDate(), dateRange.getEndDate()), false,
							lstAttendanceTimeData, lstWorkInfor, month.getAttendanceTime());
					// monthPer.addAll(timeOffRemainErrorInfor.getErrorInfor(param));
					lstEmpMonthError.addAll(timeOffRemainErrorInfor.getErrorInfor(param));
				};
			} else {
				Optional<AttendanceTimeOfMonthly> optMonthlyData = (domainMonthNew == null || domainMonthNew.isEmpty()) ? Optional.empty()
						: domainMonthNew.get(0).getAttendanceTime();
				TimeOffRemainErrorInputParam param = new TimeOffRemainErrorInputParam(companyId, emp,
						new DatePeriod(date.get(), date.get().addYears(1).addDays(-1)),
						new DatePeriod(dateRange.getStartDate(), dateRange.getEndDate()), false, lstAttendanceTimeData,
						lstWorkInfor, optMonthlyData);
				lstEmpMonthError.addAll(timeOffRemainErrorInfor.getErrorInfor(param));
				//monthPer.addAll(timeOffRemainErrorInfor.getErrorInfor(param));
			}
			
			// 勤務種類が変更されているかチェックする
			val itemEdit28s = lstItemEdits.stream().filter(it -> it.getEmployeeId().equals(emp) && it.getItemId() == 28)
					.map(it -> it.getValue()).collect(Collectors.toList());
			val lstWTClassification = new HashSet<>();
			if (!itemEdit28s.isEmpty()) {
				List<WorkType> lstWType = workTypeRepository.getPossibleWorkType(companyId, itemEdit28s);
				lstWType.stream().forEach(wt -> {
					val wtTemp = checkInGroupWorkPer(wt);
					if (wtTemp != null)
						lstWTClassification.add(convertError(wtTemp));
				});

			}
			
			lstEmpMonthError = lstEmpMonthError.stream().filter(lstErrorTemp -> !lstWTClassification.contains(lstErrorTemp.getErrorType())).collect(Collectors.toList());
			
			monthPer.addAll(lstEmpMonthError);
		});
		
		return monthPer;
	}
	
	private WorkTypeClassification checkInGroupWorkPer(WorkType wt) {
		if (wt.getDailyWork() == null)
			return null;

		WorkTypeUnit unit = wt.getDailyWork().getWorkTypeUnit();
		if (unit == WorkTypeUnit.OneDay) {
			val oneDay = wt.getDailyWork().getOneDay();
			if (oneDay == WorkTypeClassification.AnnualHoliday || oneDay == WorkTypeClassification.SpecialHoliday
					|| oneDay == WorkTypeClassification.SubstituteHoliday || oneDay == WorkTypeClassification.Pause)
				return oneDay;
			// AnnualHoliday , SpecialHoliday, SubstituteHoliday, Pause
		} else {
			val morDay = wt.getDailyWork().getMorning();
			val aftDay = wt.getDailyWork().getAfternoon();
			if (morDay == WorkTypeClassification.AnnualHoliday || morDay == WorkTypeClassification.SpecialHoliday
					|| morDay == WorkTypeClassification.SubstituteHoliday || morDay == WorkTypeClassification.Pause)
				return morDay;

			if (aftDay == WorkTypeClassification.AnnualHoliday || aftDay == WorkTypeClassification.SpecialHoliday
					|| aftDay == WorkTypeClassification.SubstituteHoliday || aftDay == WorkTypeClassification.Pause)
				return aftDay;
		}
		return null;
	}

	private ErrorType convertError(WorkTypeClassification wtc) {
		switch (wtc) {
		case AnnualHoliday:
			return ErrorType.YEARLY_HOLIDAY;
			
		case SpecialHoliday:
			return ErrorType.SPECIAL_REMAIN_HOLIDAY_NUMBER;
			
		case SubstituteHoliday:
			return ErrorType.REMAINING_ALTERNATION_NUMBER;
			
		case Pause:
			return ErrorType.REMAIN_LEFT;

		default:
		  return ErrorType.YEARLY_HOLIDAY;
		}
	}
	public List<Pair<String, GeneralDate>> itemInGroupChange(List<IntegrationOfDaily> domainDailyNew, List<DailyModifyResult> resultOlds) {
		List<DailyRecordDto> dtoNews = domainDailyNew.stream().map(x -> DailyRecordDto.from(x)).collect(Collectors.toList());
		// 暫定データを登録する - Register provisional data
		List<DailyModifyResult> resultNews = AttendanceItemUtil.toItemValues(dtoNews).entrySet()
															.stream().map(dto -> DailyModifyResult.builder()
																	.								items(dto.getValue())
																									.employeeId(dto.getKey().getEmployeeId())
																									.workingDate(dto.getKey().getDate())
																									.completed())
															.collect(Collectors.toList());
		return checkEditedItems(resultOlds, resultNews);
		
	}
	
	public void registerTempData(int displayFormat, List<DailyModifyResult> resultOlds,
			List<DailyModifyResult> resultNews) {
		switch (displayFormat) {
		case 0: // person
			List<Pair<String, GeneralDate>> listEmpDate = checkEditedItems(resultOlds, resultNews);
			if (!listEmpDate.isEmpty()) {
				interimRemainDataMngRegisterDateChange.registerDateChange(AppContexts.user().companyId(),
						listEmpDate.get(0).getLeft(),
						listEmpDate.stream().map(i -> i.getRight()).collect(Collectors.toList()));
			}
			break;
		case 1: // date
			listEmpDate = checkEditedItems(resultOlds, resultNews);
			if (!listEmpDate.isEmpty()) {
				registerProvisionalData.registerProvisionalData(AppContexts.user().companyId(),
						listEmpDate.stream().map(i -> new EmpProvisionalInput(i.getLeft(), Arrays.asList(i.getRight())))
								.collect(Collectors.toList()));
			}
			break;
		default: // error
			listEmpDate = checkEditedItems(resultOlds, resultNews);
			Map<String, List<Pair<String, GeneralDate>>> mapEmpDate = listEmpDate.stream()
					.collect(Collectors.groupingBy(x -> x.getLeft()));
			mapEmpDate.entrySet().forEach(x -> {
				interimRemainDataMngRegisterDateChange.registerDateChange(AppContexts.user().companyId(), x.getKey(),
						x.getValue().stream().map(i -> i.getRight()).collect(Collectors.toList()));
			});
			break;
		}
	}

	public List<Pair<String, GeneralDate>> checkEditedItems(List<DailyModifyResult> resultOlds,
			List<DailyModifyResult> resultNews) {
		List<Pair<String, GeneralDate>> editedDate = new ArrayList<>();
		val old = mapTo(resultOlds);
		val news = mapTo(resultNews);
		old.entrySet().forEach(o -> {
			List<ItemValue> niv = getFrom(news, o.getKey());
			if(!CollectionUtil.isEmpty(niv)){
				if(niv.stream().anyMatch(c -> o.getValue().stream().filter(oi -> c.valueAsObjet() != null && c.equals(oi)).findFirst().isPresent())){
					editedDate.add(o.getKey());
				}
			}
		});
//		for (DailyModifyResult r : resultOlds) {
//			val newR = resultNews.stream()
//					.filter(n -> n.getEmployeeId().equals(r.getEmployeeId()) && n.getDate().equals(r.getDate()))
//					.findFirst();
//			if (newR.isPresent()) {
//				List<ItemValue> oldItems = r.getItems();
//				List<ItemValue> newItems = newR.get().getItems();
//				oldItems.forEach(ov -> {
//					val nv = newItems.stream().filter(n -> n.getItemId() == ov.getItemId()).findFirst();
//					if (nv.isPresent() && nv.get().getValue() != null && !nv.get().getValue().equals(ov.getValue())
//							&& DPText.TMP_DATA_CHECK_ITEMS.contains(nv.get().getItemId())) {
//						editedDate.add(Pair.of(r.getEmployeeId(), r.getDate()));
//					}
//				});
//			}
//		}
		return editedDate;
	}
	
	private List<ItemValue> getFrom(Map<Pair<String, GeneralDate>, List<ItemValue>> source, Pair<String, GeneralDate> key){
		if(source.containsKey(key)){
			return source.get(key);
		}
		return null;
	}
	
	private Map<Pair<String, GeneralDate>, List<ItemValue>> mapTo(List<DailyModifyResult> source){
		return source.stream().collect(Collectors.groupingBy(r -> Pair.of(r.getEmployeeId(), r.getDate()), 
				Collectors.collectingAndThen(Collectors.toList(), 
						list -> list.stream().map(c -> c.getItems()).flatMap(List::stream).distinct()
						.filter(c -> DPText.TMP_DATA_CHECK_ITEMS.contains(c.getItemId())).collect(Collectors.toList()))));
	}

	private DPAttendanceItemRC convertItemAtr(DPAttendanceItem item) {
		return new DPAttendanceItemRC(item.getId(), item.getName(), item.getDisplayNumber(), item.isUserCanSet(),
				item.getLineBreakPosition(), item.getAttendanceAtr(), item.getTypeGroup(), item.getPrimitive());
	}

	private ItemFlex convertMonthToItem(MonthlyRecordWorkDto monthDto, DPMonthValue monthValue) {
		ItemFlex itemResult = new ItemFlex();
		MonthlyModifyResult result = MonthlyModifyResult.builder()
				.items(AttendanceItemUtil.toItemValues(monthDto, Arrays.asList(18, 21, 189, 190, 191),
						AttendanceItemUtil.AttendanceItemType.MONTHLY_ITEM))
				.employeeId(monthValue.getEmployeeId()).yearMonth(monthValue.getYearMonth())
				.closureId(monthValue.getClosureId()).closureDate(monthValue.getClosureDate()).completed();
		mapValue(result.getItems(), itemResult);
		return itemResult;
	}

	private void mapValue(List<ItemValue> items, ItemFlex dataCalc) {
		for (ItemValue item : items) {
			setValueMonth(dataCalc, item);
		}
	}

	private void setValueMonth(ItemFlex dataCalc, ItemValue item) {
		switch (item.getItemId()) {
		case 18:
			dataCalc.setValue18(item);
			break;
		case 21:
			dataCalc.setValue21(item);
			break;
		case 189:
			dataCalc.setValue189(item);
			break;
		case 190:
			dataCalc.setValue190(item);
			break;
		case 191:
			dataCalc.setValue191(item);
			break;
		default:
			break;
		}
	}
	
	private List<DPItemValue> releaseSign(List<DPItemCheckBox> dataCheckApproval, List<DPItemValue> resultError, List<DailyRecordDto> dailys, String employeeId, boolean onlyCheckBox) {
		List<DPItemValue> itemUi = new ArrayList<>();
		if (resultError.isEmpty() && !onlyCheckBox)
			return itemUi;
		val dailyClone = dailys.stream().map(x -> x.clone()).collect(Collectors.toList());
		val dailyTemps = dailyClone.stream().filter(x -> x.getEmployeeId().equals(employeeId))
				.sorted((x, y) -> x.getDate().compareTo(y.getDate())).collect(Collectors.toList());
		if (dailys.isEmpty() || dailyTemps.isEmpty())
			return itemUi;
		
		val lstEmployeeId = dailys.stream().map(x -> x.getEmployeeId()).collect(Collectors.toSet());
		val indentity = identificationRepository.findByListEmployeeID(new ArrayList<>(lstEmployeeId),
				dailyTemps.get(0).getDate(), dailyTemps.get(dailyTemps.size() - 1).getDate()).stream()
				.collect(Collectors.toMap(x -> Pair.of(x.getEmployeeId(), x.getProcessingYmd()), x -> ""));
		
		if (onlyCheckBox) {
			resultError = employeeDailyPerErrorRepository
					.findByPeriodOrderByYmd(employeeId,
							new DatePeriod(
									dailyTemps.get(0).getDate(), dailyTemps.get(dailyTemps.size() - 1).getDate()))
					.stream()
					.filter(err -> err.getErrorAlarmWorkRecordCode().v().startsWith("D")
							&& (!err.getErrorAlarmMessage().isPresent()
									|| !err.getErrorAlarmMessage().get().v().equals(TextResource.localize("Msg_1298"))))
					.map(x -> {
						return new DPItemValue("", x.getEmployeeID(), x.getDate(), 0);
					}).collect(Collectors.toList());
		}
		
		Map<Pair<String, GeneralDate>, Boolean> mapRelease = resultError.stream()
				.collect(Collectors.toMap(x -> Pair.of(x.getEmployeeId(), x.getDate()), x -> true, (x, y) -> x));

		dataCheckApproval.stream().map(x -> {
			if (mapRelease.containsKey(Pair.of(x.getEmployeeId(), x.getDate())) && x.isValue()) {
				itemUi.add(
						new DPItemValue("", x.getEmployeeId(), x.getDate(), 0, "", TextResource.localize("Msg_1455")));
				mapRelease.remove(Pair.of(x.getEmployeeId(), x.getDate()));
				x.setValue(false);
				return x;
			} else {
				return x;
			}
		}).collect(Collectors.toList());
		
		val dateEmp = dataCheckApproval.stream()
				.collect(Collectors.toMap(x -> Pair.of(x.getEmployeeId(), x.getDate()), x -> ""));
		val itemNotUiRelease = dailyTemps.stream()
				.filter(x -> mapRelease.containsKey(Pair.of(x.getEmployeeId(), x.getDate()))
						&& !dateEmp.containsKey(Pair.of(x.getEmployeeId(), x.getDate())))
				.collect(Collectors.toList());
		
		itemNotUiRelease.stream().forEach(x ->{
			if(indentity.containsKey(Pair.of(x.getEmployeeId(), x.getDate()))) {
				itemUi.add(
						new DPItemValue("", x.getEmployeeId(), x.getDate(), 0, "", TextResource.localize("Msg_1455")));
				dataCheckApproval.add(new DPItemCheckBox("", null, false, "", x.getEmployeeId(), x.getDate(), false));
			}
		});
		return itemUi;
	}
	
	private boolean showError(List<IntegrationOfDaily> dailys, List<DailyRecordDto> dailyRecord) {
		// アルゴリズム「実績修正画面で利用するフォーマットを取得する」を実行する(thực hiện xử lý 「実績修正画面で利用するフォーマットを取得する」)
		val lstError = dailys.stream().flatMap(x -> x.getEmployeeError().stream()).collect(Collectors.toList());
		val lstErrorDto = dailyRecord.stream().flatMap(x -> x.getErrors().stream()).collect(Collectors.toList());
		OperationOfDailyPerformanceDto settingMaster = repo.findOperationOfDailyPerformance();
		if (lstError.isEmpty() && lstErrorDto.isEmpty())
			return false;
		//fix bug 102116
		//them thuat toan ドメインモデル「勤務実績のエラーアラーム」を取得する
		//check table 'era set'
		List<String> errorList = lstError.stream().map(e -> e.getErrorAlarmWorkRecordCode().toString()).collect(Collectors.toList());
		errorList.addAll(lstErrorDto.stream().map(e -> e.getErrorCode()).collect(Collectors.toList()));
		boolean isErAl = repo.isErAl(AppContexts.user().companyId(), 
			 lstError.stream().map(e -> e.getErrorAlarmWorkRecordCode().toString()).collect(Collectors.toList()));
		if(isErAl == false)
			return false;
		return settingMaster == null ? false : settingMaster.isShowError();
	}
	
	public ErrorAfterCalcDaily checkErrorAfterCalcDaily(RCDailyCorrectionResult resultIU, UpdateMonthDailyParam monthlyParam, List<DailyModifyResult> resultOlds, int mode, DPMonthValue monthValue, DateRange range, List<DailyRecordDto> dailyDtoEditAll, List<DPItemValue> lstItemEdits) {
		Map<Integer, List<DPItemValue>> resultError = new HashMap<>();
		boolean hasError = false;
		DataResultAfterIU dataResultAfterIU = new DataResultAfterIU();
		
		val errorDivergence = validatorDataDaily.errorCheckDivergence(resultIU.getLstDailyDomain(),
				resultIU.getLstMonthDomain());
		if (!errorDivergence.isEmpty()) {
			resultError.putAll(errorDivergence);
			hasError = true;
		}
		
		// 残数系のエラーチェック（月次集計なし）
		val sidChange = itemInGroupChange(resultIU.getLstDailyDomain(), resultOlds);
		val errorMonth = validatorDataDaily.errorMonthNew(
				mapDomainMonthChange(sidChange, resultIU.getLstDailyDomain(), resultIU.getLstMonthDomain(), dailyDtoEditAll, range, lstItemEdits));
		// val errorMonth = validatorDataDaily.errorMonth(resultIU.getLstMonthDomain(),
		// monthParam);

		if (!errorMonth.isEmpty()) {
			resultError.putAll(errorMonth);
			hasError = true;
		}
				
		return new ErrorAfterCalcDaily(hasError, resultError, dataResultAfterIU.getFlexShortage());
	}
	
	public ErrorAfterCalcDaily checkErrorAfterCalcMonth(RCDailyCorrectionResult resultIU, UpdateMonthDailyParam monthlyParam, List<DailyModifyResult> resultOlds, int mode, DPMonthValue monthValue, DateRange range) {
		Map<Integer, List<DPItemValue>> resultError = new HashMap<>();
		boolean hasError = false;
		DataResultAfterIU dataResultAfterIU = new DataResultAfterIU();
		if (mode == 0 && monthlyParam.getHasFlex() != null && monthlyParam.getHasFlex()) {
			val flexShortageRCDto = validatorDataDaily.errorCheckFlex(resultIU.getLstMonthDomain(),
					monthlyParam);
			if (flexShortageRCDto.isError() || !flexShortageRCDto.getMessageError().isEmpty()) {
				hasError = true;
				if(!resultIU.getLstMonthDomain().isEmpty()) flexShortageRCDto.createDataCalc(convertMonthToItem(MonthlyRecordWorkDto.fromOnlyAttTime(resultIU.getLstMonthDomain().get(0)), monthValue));
			}
			dataResultAfterIU.setFlexShortage(flexShortageRCDto);
		}
		
		return new ErrorAfterCalcDaily(hasError, resultError, dataResultAfterIU.getFlexShortage());
	}
	
	public ErrorAfterCalcDaily checkErrorAfterCalc(RCDailyCorrectionResult resultIU, UpdateMonthDailyParam monthlyParam, List<DailyModifyResult> resultOlds, int mode, DPMonthValue monthValue, DateRange range, List<DailyRecordDto> dailyEditAll, List<DPItemValue> lstItemEdits) {
		Map<Integer, List<DPItemValue>> resultError = new HashMap<>();
		boolean hasError = false;
		DataResultAfterIU dataResultAfterIU = new DataResultAfterIU();
		
		val errorDivergence = validatorDataDaily.errorCheckDivergence(resultIU.getLstDailyDomain(),
				resultIU.getLstMonthDomain());
		if (!errorDivergence.isEmpty()) {
			resultError.putAll(errorDivergence);
			hasError = true;
		}
		if (mode == 0 && monthlyParam.getHasFlex() != null && monthlyParam.getHasFlex()) {
			val flexShortageRCDto = validatorDataDaily.errorCheckFlex(resultIU.getLstMonthDomain(),
					monthlyParam);
			if (flexShortageRCDto.isError() || !flexShortageRCDto.getMessageError().isEmpty()) {
				hasError = true;
				if(!resultIU.getLstMonthDomain().isEmpty()) flexShortageRCDto.createDataCalc(convertMonthToItem(MonthlyRecordWorkDto.fromOnlyAttTime(resultIU.getLstMonthDomain().get(0)), monthValue));
			}
			dataResultAfterIU.setFlexShortage(flexShortageRCDto);
		}
		// 残数系のエラーチェック（月次集計なし）
		val sidChange = itemInGroupChange(resultIU.getLstDailyDomain(), resultOlds);
		val errorMonth = validatorDataDaily.errorMonthNew(mapDomainMonthChange(sidChange, resultIU.getLstDailyDomain(), resultIU.getLstMonthDomain(), dailyEditAll, range, lstItemEdits));
		//val errorMonth = validatorDataDaily.errorMonth(resultIU.getLstMonthDomain(), monthParam);
		
		if (!errorMonth.isEmpty()) {
			resultError.putAll(errorMonth);
			hasError = true;
		}
		
		return new ErrorAfterCalcDaily(hasError, resultError, dataResultAfterIU.getFlexShortage());
	}
	
	private List<IntegrationOfDaily> unionDomain(List<IntegrationOfDaily> parent, List<IntegrationOfDaily> child){
		val date = child.stream().collect(Collectors.toMap(x -> x.getWorkInformation().getYmd(), x -> "", (x, y) -> x));
		val resultFilter = parent.stream().filter(x -> !date.containsKey(x.getWorkInformation().getYmd())).collect(Collectors.toList());
		resultFilter.addAll(child);
		return resultFilter;
	}
}

package nts.uk.screen.at.app.dailymodify.command;

import java.util.ArrayList;
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

import org.apache.commons.lang3.tuple.Pair;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.app.command.dailyperform.month.UpdateMonthDailyParam;
import nts.uk.ctx.at.record.app.find.dailyperform.DailyRecordDto;
import nts.uk.ctx.at.record.app.find.dailyperform.DailyRecordWorkFinder;
import nts.uk.ctx.at.record.app.service.dailycheck.CheckCalcMonthService;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.CalculateDailyRecordServiceCenter;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.CommonCompanySettingForCalc;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.ManagePerCompanySet;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.IntegrationOfMonthly;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.export.AggregateSpecifiedDailys;
import nts.uk.ctx.at.record.dom.optitem.OptionalItem;
import nts.uk.ctx.at.record.dom.optitem.OptionalItemRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionType;
import nts.uk.ctx.at.shared.dom.attendance.util.AttendanceItemUtil;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;
import nts.uk.screen.at.app.dailymodify.query.DailyModifyQuery;
import nts.uk.screen.at.app.dailymodify.query.DailyModifyResult;
import nts.uk.screen.at.app.dailyperformance.correction.checkdata.ValidatorDataDailyRes;
import nts.uk.screen.at.app.dailyperformance.correction.checkdata.dto.FlexShortageRCDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPItemParent;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPItemValue;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DailyPerformanceCalculationDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DataResultAfterIU;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DateRange;
import nts.uk.screen.at.app.dailyperformance.correction.dto.TypeError;
import nts.uk.screen.at.app.dailyperformance.correction.dto.month.DPMonthValue;
import nts.uk.screen.at.app.dailyperformance.correction.loadupdate.DPLoadRowProcessor;
import nts.uk.screen.at.app.monthlyperformance.correction.command.MonthModifyCommandFacade;
import nts.uk.screen.at.app.monthlyperformance.correction.query.MonthlyModifyQuery;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 
 * @author HungTT
 *
 */

@Stateless
public class DailyCalculationCommandFacade {

	@Inject
	private DailyRecordWorkFinder finder;

	@Inject
	private OptionalItemRepository optionalMasterRepo;

	@Inject
	private CalculateDailyRecordServiceCenter calcService;

	@Inject
	private ValidatorDataDailyRes validatorDataDaily;

	@Inject
	private CheckCalcMonthService calCheckMonthService;

	@Inject
	private AggregateSpecifiedDailys aggregateSpecifiedDailys;

	@Inject
	private MonthModifyCommandFacade monthModifyCommandFacade;

	@Inject
	private CommonCompanySettingForCalc commonCompanySettingForCalc;
	
	@Inject
	private DPLoadRowProcessor dpLoadRowProcessor;
	
	@Inject
	private DailyModifyResCommandFacade dailyModifyResCommandFacade;
	
	public static final int MINUTES_OF_DAY = 24 * 60;

	private static final String FORMAT_HH_MM = "%d:%02d";

	/**
	 * 修正した実績を計算する
	 */
	public DailyPerformanceCalculationDto calculateCorrectedResults(DPItemParent dataParent) {
		// chuan bi data
		String companyId = AppContexts.user().companyId();
		List<DailyRecordDto> editedDtos = dataParent.getDailyEdits();
		Map<Pair<String, GeneralDate>, List<DPItemValue>> mapSidDateEdit = dataParent.getItemValues().stream()
				.collect(Collectors.groupingBy(x -> Pair.of(x.getEmployeeId(), x.getDate())));

		List<DailyModifyQuery> querys = createQuerys(mapSidDateEdit);
		dailyModifyResCommandFacade.toDto(querys, editedDtos);
		//List<DailyRecordDto> oldDtos = dataParent.getDailyOlds();
		val mapDtoOld = editedDtos.stream().collect(Collectors.toMap(x -> Pair.of(x.getEmployeeId(), x.getDate()), x -> x));
		List<IntegrationOfDaily> editedDomains = editedDtos.stream()
				.map(d -> d.toDomain(d.getEmployeeId(), d.getDate())).collect(Collectors.toList());

		// check error truoc khi tinh toan
		Map<Integer, List<DPItemValue>> resultError = errorCheckBeforeCalculation(dataParent.getItemValues(), querys, mapSidDateEdit);
		FlexShortageRCDto flexShortage = null;
		if (resultError.values().stream().filter(z -> z.size() > 0).collect(Collectors.toList()).isEmpty()) {
			// tinh toan daily result
			ManagePerCompanySet manageComanySet = commonCompanySettingForCalc.getCompanySetting();
			editedDomains = calcService.calculatePassCompanySetting(editedDomains, Optional.ofNullable(manageComanySet),
					dataParent.isFlagCalculation() ? ExecutionType.RERUN : ExecutionType.NORMAL_EXECUTION);
//			editedDomains = calcService.calculate(editedDomains);

			List<IntegrationOfMonthly> monthlyResults = new ArrayList<>();
			// check format display = individual
			if (dataParent.getMode() == 0) {
				// kiem tra co can tinh toan monthly khong, neu can thi tinh lai
				// monthly result
				val results = updateMonth(editedDomains, companyId, dataParent.getEmployeeId(),
						editedDtos.stream().map(d -> d.getDate()).collect(Collectors.toList()),
						dataParent.getMonthValue());
				monthlyResults.addAll(results);
			}

			// check error sau khi tinh toan
			DataResultAfterIU afterError = errorCheckAfterCalculation(editedDomains, monthlyResults, dataParent.getMonthValue(), dataParent.getDateRange(), dataParent.getMode());
			resultError = afterError.getErrorMap();
			flexShortage = afterError.getFlexShortage();

			if (resultError.values().stream().filter(z -> z.size() > 0).collect(Collectors.toList()).isEmpty()) {
				// update lai daily results gui ve client
				List<DailyRecordDto> calculatedDtos = editedDomains.stream().map(d -> DailyRecordDto.from(d))
						.collect(Collectors.toList());
//				List<DailyModifyResult> resultValues = calculatedDtos.stream().map(
//						c -> DailyModifyResult.builder().items(AttendanceItemUtil.toItemValues(c).stream().map(item -> {
//							return (item.getValueType() == ValueType.TIME || item.getValueType() == ValueType.CLOCK
//									|| item.getValueType() == ValueType.TIME_WITH_DAY)
//											? new ItemValue(
//													item.getValue() == null ? ""
//															: converTime(item.getValueType().value, item.getValue()),
//													item.getValueType(), item.getLayoutCode(), item.getItemId(),
//													item.getPathLink())
//											: item;
//						}).collect(Collectors.toList())).workingDate(c.workingDate()).employeeId(c.employeeId())
//								.completed())
//						.collect(Collectors.toList());
				// set state calc 
				val mapDtoEdits = calculatedDtos.stream().collect(Collectors.toMap(x -> Pair.of(x.getEmployeeId(), x.getDate()), x -> x));
				val resultCompare = dpLoadRowProcessor.itemCalcScreen(mapDtoEdits, mapDtoOld, dataParent.getLstData(), dataParent.getLstAttendanceItem(), dataParent.getCellEdits());
				List<DailyModifyResult> resultValues = resultCompare.getRight().stream().map(x ->{
					x.items(x.getItems().stream().map(item -> {
							return (item.getValueType() == ValueType.TIME || item.getValueType() == ValueType.CLOCK
									|| item.getValueType() == ValueType.TIME_WITH_DAY)
											? new ItemValue(
													item.getValue() == null ? ""
															: converTime(item.getValueType().value, item.getValue()),
													item.getValueType(), item.getLayoutCode(), item.getItemId(),
													item.getPathLink())
											: item;
						}).collect(Collectors.toList()));
					return x;
				}).collect(Collectors.toList());
				DailyPerformanceCalculationDto returnData = new DailyPerformanceCalculationDto(calculatedDtos,
						resultValues, null, resultCompare.getLeft());
				return returnData;
			}
		}
		return new DailyPerformanceCalculationDto(null, new ArrayList<>(), new DataResultAfterIU(resultError, flexShortage, false), Collections.emptyList());
	}

	/**
	 * 計算前エラーチェック
	 */
	private Map<Integer, List<DPItemValue>> errorCheckBeforeCalculation(List<DPItemValue> editedItems, List<DailyModifyQuery> querys, Map<Pair<String, GeneralDate>, List<DPItemValue>> mapSidDateEdit) {
		Map<Integer, List<DPItemValue>> resultError = new HashMap<>();
		Pair<List<DailyRecordDto>, List<DailyRecordDto>> mergeDto = toDto(querys);
		List<DailyRecordDto> dtoOlds = mergeDto.getLeft();
		// map to list result -> check error;
		List<DailyModifyResult> resultOlds = dtoOlds.stream()
				.map(c -> DailyModifyResult.builder().items(AttendanceItemUtil.toItemValues(c))
						.workingDate(c.workingDate()).employeeId(c.employeeId()).completed())
				.collect(Collectors.toList());
		Map<Pair<String, GeneralDate>, List<DailyModifyResult>> mapSidDateOrigin = resultOlds.stream()
				.collect(Collectors.groupingBy(x -> Pair.of(x.getEmployeeId(), x.getDate())));
		List<DPItemValue> itemErrors = new ArrayList<>();
		List<DPItemValue> itemInputErors = new ArrayList<>();
		List<DPItemValue> itemInputError28 = new ArrayList<>();
		// List<DPItemValue> itemInputDeviation = new ArrayList<>();
		mapSidDateEdit.entrySet().forEach(x -> {
			List<DPItemValue> itemCovert = x.getValue().stream().filter(y -> y.getValue() != null)
					.collect(Collectors.toList()).stream().filter(distinctByKey(p -> p.getItemId()))
					.collect(Collectors.toList());
			List<DailyModifyResult> itemValues = itemCovert.isEmpty() ? Collections.emptyList()
					: mapSidDateOrigin.get(Pair.of(itemCovert.get(0).getEmployeeId(), itemCovert.get(0).getDate()));
			List<DPItemValue> items = validatorDataDaily.checkCareItemDuplicate(itemCovert);
			itemErrors.addAll(items);
			List<DPItemValue> itemInputs = validatorDataDaily.checkInputData(itemCovert, itemValues);
			itemInputErors.addAll(itemInputs);
			List<DPItemValue> itemInputs28 = validatorDataDaily.checkInput28And1(itemCovert, itemValues);
			itemInputError28.addAll(itemInputs28);
		});
		resultError.put(TypeError.DUPLICATE.value, itemErrors);
		resultError.put(TypeError.COUPLE.value, itemInputErors);
		resultError.put(TypeError.ITEM28.value, itemInputError28);
		return resultError;
	}

	private List<IntegrationOfMonthly> updateMonth(List<IntegrationOfDaily> domainDailyNew, String companyId,
			String employeeId, List<GeneralDate> lstDayChange, DPMonthValue monthlyParam) {
		IntegrationOfMonthly domainMonth = null;
		if (monthlyParam != null) {
			if (monthlyParam != null && monthlyParam.getItems() != null) {
				MonthlyModifyQuery monthQuery = new MonthlyModifyQuery(monthlyParam.getItems().stream().map(x -> {
					return ItemValue.builder().itemId(x.getItemId()).layout(x.getLayoutCode()).value(x.getValue())
							.valueType(ValueType.valueOf(x.getValueType())).withPath("");
				}).collect(Collectors.toList()), monthlyParam.getYearMonth(), monthlyParam.getEmployeeId(),
						monthlyParam.getClosureId(), monthlyParam.getClosureDate());
				domainMonth = monthModifyCommandFacade.toDto(monthQuery).toDomain(monthlyParam.getEmployeeId(),
						new YearMonth(monthlyParam.getYearMonth()), monthlyParam.getClosureId(),
						monthlyParam.getClosureDate());
			}
		}
		final Optional<IntegrationOfMonthly> domainMonthOpt = Optional.ofNullable(domainMonth);
		List<IntegrationOfMonthly> result = new ArrayList<>();
		// 月次集計を実施する必要があるかチェックする
		val needCalc = calCheckMonthService.isNeedCalcMonth(companyId, employeeId, lstDayChange);
		if (needCalc.getLeft()) {
			needCalc.getRight().forEach(data -> {
				// 月の実績を集計する
				Optional<IntegrationOfMonthly> monthDomainOpt = aggregateSpecifiedDailys.algorithm(companyId,
						employeeId, data.getYearMonth(), data.getClosureId(), data.getClosureDate(), data.getPeriod(),
						Optional.empty(), domainDailyNew, domainMonthOpt);
				if (monthDomainOpt.isPresent())
					result.add(monthDomainOpt.get());
			});
		}
		return result;
	}

	/**
	 * 計算後エラーチェック
	 */
	private DataResultAfterIU errorCheckAfterCalculation(List<IntegrationOfDaily> dailyResults,
			List<IntegrationOfMonthly> monthlyResults, DPMonthValue monthlyParam, DateRange dateRange, int mode) {
		Map<Integer, List<DPItemValue>> resultError = new HashMap<>();
		
		// 乖離エラーのチェック
		Map<Integer, List<DPItemValue>> divergenceErrors = validatorDataDaily.errorCheckDivergence(dailyResults, monthlyResults);
		resultError.putAll(divergenceErrors);

		// フレックス繰越時間が正しい範囲で入力されているかチェックする
		UpdateMonthDailyParam monthParam = null;
		FlexShortageRCDto flexError = null;
		if (monthlyParam != null) {
			val month = monthlyParam;
			if (month != null && month.getItems() != null) {
				MonthlyModifyQuery monthQuery = new MonthlyModifyQuery(month.getItems().stream().map(x -> {
					return ItemValue.builder().itemId(x.getItemId()).layout(x.getLayoutCode()).value(x.getValue())
							.valueType(ValueType.valueOf(x.getValueType())).withPath("");
				}).collect(Collectors.toList()), month.getYearMonth(), month.getEmployeeId(), month.getClosureId(),
						month.getClosureDate());
				IntegrationOfMonthly domainMonth = monthModifyCommandFacade.toDto(monthQuery).toDomain(month.getEmployeeId(), new YearMonth(month.getYearMonth()), month.getClosureId(), month.getClosureDate());
				Optional<IntegrationOfMonthly> domainMonthOpt = Optional.of(domainMonth);
				monthParam = new UpdateMonthDailyParam(month.getYearMonth(), month.getEmployeeId(),
						month.getClosureId(), month.getClosureDate(), domainMonthOpt, new DatePeriod(
								dateRange.getStartDate(), dateRange.getEndDate()), month.getRedConditionMessage(), month.getHasFlex(), month.getNeedCallCalc());
			}else{
				monthParam = new UpdateMonthDailyParam(month.getYearMonth(), month.getEmployeeId(),
						month.getClosureId(), month.getClosureDate(), Optional.empty(), new DatePeriod(
								dateRange.getStartDate(), dateRange.getEndDate()), month.getRedConditionMessage(), month.getHasFlex(), month.getNeedCallCalc());
			}
		}
		if (mode == 0 && monthParam.getHasFlex()) {
			flexError = validatorDataDaily.errorCheckFlex(monthlyResults, monthParam);
		}

		// 残数系のエラーチェック
		List<DPItemValue> errorMonth = validatorDataDaily.errorMonth(monthlyResults, null).get(TypeError.ERROR_MONTH.value);
		resultError.put(TypeError.ERROR_MONTH.value, errorMonth == null ? Collections.emptyList() : errorMonth);
		
		return new DataResultAfterIU(resultError, flexError, false);
	}

	private Map<String, List<GeneralDate>> dtoToMapParam(List<DailyRecordDto> dtos) {
		return dtos.stream()
				.collect(Collectors.groupingBy(c -> c.getEmployeeId(), Collectors.collectingAndThen(Collectors.toList(),
						c -> c.stream().map(q -> q.getDate()).collect(Collectors.toList()))));
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

	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		final Set<Object> seen = new HashSet<>();
		return t -> seen.add(keyExtractor.apply(t));
	}

	private Pair<List<DailyRecordDto>, List<DailyRecordDto>> toDto(List<DailyModifyQuery> query) {
		List<DailyRecordDto> dtoNews, dtoOlds = new ArrayList<>();
		Map<Integer, OptionalItem> optionalMaster = optionalMasterRepo
				.findAll(AppContexts.user().companyId()).stream()
				.collect(Collectors.toMap(c -> c.getOptionalItemNo().v(), c -> c));
		dtoOlds = finder.find(query.stream()
				.collect(Collectors.groupingBy(c -> c.getEmployeeId(), Collectors.collectingAndThen(Collectors.toList(),
						c -> c.stream().map(q -> q.getBaseDate()).collect(Collectors.toList())))));
		dtoNews = dtoOlds.stream().map(o -> {

			List<ItemValue> itemValues = query.stream()
					.filter(q -> q.getBaseDate().equals(o.workingDate()) && q.getEmployeeId().equals(o.employeeId()))
					.findFirst().get().getItemValues();
			DailyRecordDto dtoClone = o.clone();
			AttendanceItemUtil.fromItemValues(dtoClone, itemValues);
			dtoClone.getOptionalItem().ifPresent(optional -> {
				optional.correctItems(optionalMaster);
			});
			dtoClone.getTimeLeaving().ifPresent(dto -> {
				if (dto.getWorkAndLeave() != null)
					dto.getWorkAndLeave().removeIf(tl -> tl.getWorking() == null && tl.getLeave() == null);
			});
			return dtoClone;
		}).collect(Collectors.toList());
		return Pair.of(dtoOlds, dtoNews);
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

}

package nts.uk.screen.at.app.dailymodify.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.commons.lang3.tuple.Pair;

import lombok.val;
import nts.arc.task.AsyncTask;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.gul.collection.CollectionUtil;
import nts.gul.util.value.MutableValue;
import nts.uk.ctx.at.record.app.command.dailyperform.DailyRecordWorkCommand;
import nts.uk.ctx.at.record.app.command.dailyperform.DailyRecordWorkCommandHandler;
import nts.uk.ctx.at.record.app.command.dailyperform.audittrail.DPAttendanceItemRC;
import nts.uk.ctx.at.record.app.command.dailyperform.checkdata.RCDailyCorrectionResult;
import nts.uk.ctx.at.record.app.command.dailyperform.month.UpdateMonthDailyParam;
import nts.uk.ctx.at.record.app.find.dailyperform.DailyRecordDto;
import nts.uk.ctx.at.record.app.find.monthly.root.MonthlyRecordWorkDto;
import nts.uk.ctx.at.record.app.service.workrecord.erroralarm.recordcheck.ErAlWorkRecordCheckService;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.approvalmanagement.dailyperformance.algorithm.ContentApproval;
import nts.uk.ctx.at.record.dom.approvalmanagement.dailyperformance.algorithm.ParamDayApproval;
import nts.uk.ctx.at.record.dom.approvalmanagement.dailyperformance.algorithm.RegisterDayApproval;
import nts.uk.ctx.at.record.dom.daily.DailyRecordTransactionService;
import nts.uk.ctx.at.record.dom.daily.itemvalue.DailyItemValue;
import nts.uk.ctx.at.record.dom.daily.optionalitemtime.AnyItemValueOfDailyRepo;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.erroralarm.EmployeeMonthlyPerError;
import nts.uk.ctx.at.record.dom.monthly.erroralarm.ErrorType;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.IntegrationOfMonthly;
import nts.uk.ctx.at.record.dom.optitem.OptionalItemAtr;
import nts.uk.ctx.at.record.dom.optitem.OptionalItemRepository;
import nts.uk.ctx.at.record.dom.service.TimeOffRemainErrorInfor;
import nts.uk.ctx.at.record.dom.service.TimeOffRemainErrorInputParam;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.CheckShortage;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.CheckShortageFlex;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.approvalsituationmanagement.export.clearapprovalconfirm.ClearConfirmApprovalService;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.algorithm.ParamIdentityConfirmDay;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.algorithm.RegisterIdentityConfirmDay;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.algorithm.SelfConfirmDay;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.IdentificationRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionType;
import nts.uk.ctx.at.record.dom.worktime.enums.StampSourceInfo;
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
import nts.uk.screen.at.app.dailymodify.command.common.ProcessCommonCalc;
import nts.uk.screen.at.app.dailymodify.command.common.ProcessDailyCalc;
import nts.uk.screen.at.app.dailymodify.command.common.ProcessMonthlyCalc;
import nts.uk.screen.at.app.dailymodify.query.DailyModifyQuery;
import nts.uk.screen.at.app.dailymodify.query.DailyModifyResult;
import nts.uk.screen.at.app.dailyperformance.correction.DailyPerformanceCorrectionProcessor;
import nts.uk.screen.at.app.dailyperformance.correction.DailyPerformanceScreenRepo;
import nts.uk.screen.at.app.dailyperformance.correction.checkdata.ValidatorDataDailyRes;
import nts.uk.screen.at.app.dailyperformance.correction.checkdata.dto.ErrorAfterCalcDaily;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPAttendanceItem;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPItemCheckBox;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPItemParent;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPItemValue;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DataResultAfterIU;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DateRange;
import nts.uk.screen.at.app.dailyperformance.correction.dto.EmpAndDate;
import nts.uk.screen.at.app.dailyperformance.correction.dto.EmpErrorCode;
import nts.uk.screen.at.app.dailyperformance.correction.dto.OperationOfDailyPerformanceDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.ResultReturnDCUpdateData;
import nts.uk.screen.at.app.dailyperformance.correction.dto.TypeError;
import nts.uk.screen.at.app.dailyperformance.correction.dto.month.DPMonthValue;
import nts.uk.screen.at.app.dailyperformance.correction.dto.month.LeaveDayErrorDto;
import nts.uk.screen.at.app.dailyperformance.correction.text.DPText;
import nts.uk.screen.at.app.monthlyperformance.correction.command.MonthModifyCommandFacade;
import nts.uk.screen.at.app.monthlyperformance.correction.query.MonthlyModifyQuery;
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

	// @Inject
	// private EmployeeDailyPerErrorRepository employeeDailyPerErrorRepository;

	@Inject
	private DailyPerformanceScreenRepo repo;

	@Inject
	private GetClosureStartForEmployee getClosureStartForEmployee;

	@Inject
	private TimeOffRemainErrorInfor timeOffRemainErrorInfor;

	@Inject
	private WorkTypeRepository workTypeRepository;

	@Inject
	private AnyItemValueOfDailyRepo anyItemValueOfDailyRepo;

	@Inject
	private DailyRecordTransactionService dailyTransaction;

	@Inject
	private CheckShortageFlex checkShortageFlex;

	@Inject
	private ClearConfirmApprovalService clearConfirmApprovalService;

	/** 並列処理用 */
	@Resource
	private ManagedExecutorService executerService;
	
	@Inject
	private ProcessDailyCalc processDailyCalc;
	
	@Inject
	private ProcessMonthlyCalc processMonthlyCalc;

	private void processDto(List<DailyRecordDto> dailyOlds, List<DailyRecordDto> dailyEdits, DPItemParent dataParent,
			List<DailyModifyQuery> querys, Map<Pair<String, GeneralDate>, List<DPItemValue>> mapSidDate,
			Set<Pair<String, GeneralDate>> pairSidDateCheck, List<DailyModifyQuery> queryNotChanges) {
		// list cell change by checkbox
		if (!querys.isEmpty() && !dataParent.isFlagCalculation()) {
			dailyOlds.addAll(dataParent.getDailyOlds().stream()
					.filter(x -> mapSidDate.containsKey(Pair.of(x.getEmployeeId(), x.getDate()))
							|| pairSidDateCheck.contains(Pair.of(x.getEmployeeId(), x.getDate())))
					.collect(Collectors.toList()));
			List<DailyRecordDto> temp = dataParent.getDailyEdits().stream()
					.filter(x -> mapSidDate.containsKey(Pair.of(x.getEmployeeId(), x.getDate()))
							|| pairSidDateCheck.contains(Pair.of(x.getEmployeeId(), x.getDate())))
					.collect(Collectors.toList());
			dailyEdits.addAll(queryNotChanges.isEmpty() ? temp.stream().map(x -> {
				createStampSourceInfo(x, querys);
				return x;
			}).collect(Collectors.toList()) : toDto(queryNotChanges, temp));
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
			createStampSourceInfo(o, querys);
			o.getTimeLeaving().ifPresent(dto -> {
				if (dto.getWorkAndLeave() != null)
					dto.getWorkAndLeave().removeIf(tl -> tl.getWorking() == null && tl.getLeave() == null);
			});
			return o;
		}).collect(Collectors.toList());
		return dtoNews;
	}


	public DataResultAfterIU insertItemDomain(DPItemParent dataParent) {
		// Map<Integer, List<DPItemValue>> resultError = new HashMap<>();
		Map<Integer, List<DPItemValue>> resultErrorMonth = new HashMap<>();
		DataResultAfterIU dataResultAfterIU = new DataResultAfterIU();
		Map<Pair<String, GeneralDate>, ResultReturnDCUpdateData> lstResultReturnDailyError = new HashMap<>();
		boolean hasErrorRow = false;
		boolean errorMonthAfterCalc = false;
		boolean flagTempCalc = dataParent.isFlagCalculation();
		dataParent.setFlagCalculation(false);
		boolean editFlex = (dataParent.getMode() == 0 && dataParent.getMonthValue() != null
				&& !CollectionUtil.isEmpty(dataParent.getMonthValue().getItems()));
		List<DPItemValue> dataCheck = new ArrayList<>();
		// insert flex
		UpdateMonthDailyParam monthParam = null;
		if (dataParent.getMonthValue() != null) {
			val month = dataParent.getMonthValue();
			if (month != null && month.getItems() != null && !month.getItems().isEmpty()) {
				MonthlyModifyQuery monthQuery = new MonthlyModifyQuery(month.getItems().stream().map(x -> {
					return ItemValue.builder().itemId(x.getItemId()).layout(x.getLayoutCode()).value(x.getValue())
							.valueType(ValueType.valueOf(x.getValueType())).withPath("");
				}).collect(Collectors.toList()), month.getYearMonth(), month.getEmployeeId(), month.getClosureId(),
						month.getClosureDate());
				IntegrationOfMonthly domainMonth = monthModifyCommandFacade.toDto(monthQuery).toDomain(
						month.getEmployeeId(), new YearMonth(month.getYearMonth()), month.getClosureId(),
						month.getClosureDate());
				domainMonth.getAffiliationInfo().ifPresent(d -> {
					d.setVersion(dataParent.getMonthValue().getVersion());
				});
				domainMonth.getAttendanceTime().ifPresent(d -> {
					d.setVersion(dataParent.getMonthValue().getVersion());
				});
				Optional<IntegrationOfMonthly> domainMonthOpt = Optional.of(domainMonth);
				monthParam = new UpdateMonthDailyParam(month.getYearMonth(), month.getEmployeeId(),
						month.getClosureId(), month.getClosureDate(), domainMonthOpt,
						new DatePeriod(dataParent.getDateRange().getStartDate(),
								dataParent.getDateRange().getEndDate()),
						month.getRedConditionMessage(), month.getHasFlex(), month.getNeedCallCalc(),
						dataParent.getMonthValue().getVersion());
			} else {
				monthParam = new UpdateMonthDailyParam(month.getYearMonth(), month.getEmployeeId(),
						month.getClosureId(), month.getClosureDate(), Optional.empty(),
						new DatePeriod(dataParent.getDateRange().getStartDate(),
								dataParent.getDateRange().getEndDate()),
						month.getRedConditionMessage(), month.getHasFlex(), month.getNeedCallCalc(),
						dataParent.getMonthValue().getVersion());
			}
		}

		// filter domain calc error
		if (dataParent.isFlagCalculation()) {
			int sizeAll = dataParent.getDailyEdits().size();
			dataParent.setDailyEdits(dataParent.getDailyEdits().stream().filter(
					x -> dataParent.getLstSidDateDomainError().contains(Pair.of(x.getEmployeeId(), x.getDate())))
					.collect(Collectors.toList()));
			dataParent.setDailyOlds(dataParent.getDailyOlds().stream().filter(
					x -> dataParent.getLstSidDateDomainError().contains(Pair.of(x.getEmployeeId(), x.getDate())))
					.collect(Collectors.toList()));
			dataParent.setItemValues(dataParent.getItemValues().stream().filter(
					x -> dataParent.getLstSidDateDomainError().contains(Pair.of(x.getEmployeeId(), x.getDate())))
					.collect(Collectors.toList()));
			dataParent.setDataCheckSign(dataParent.getDataCheckSign().stream().filter(
					x -> dataParent.getLstSidDateDomainError().contains(Pair.of(x.getEmployeeId(), x.getDate())))
					.collect(Collectors.toList()));
			dataParent.setDataCheckApproval(dataParent.getDataCheckApproval().stream().filter(
					x -> dataParent.getLstSidDateDomainError().contains(Pair.of(x.getEmployeeId(), x.getDate())))
					.collect(Collectors.toList()));
			int sizeAllAfter = dataParent.getDailyEdits().size();
			if (sizeAll > sizeAllAfter) {
				hasErrorRow = true;
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

		Set<Pair<String, GeneralDate>> pairSidDateCheck = new HashSet<>();
		dataParent.getDataCheckSign().stream().forEach(x -> {
			pairSidDateCheck.add(Pair.of(x.getEmployeeId(), x.getDate()));
		});

		dataParent.getDataCheckApproval().stream().forEach(x -> {
			pairSidDateCheck.add(Pair.of(x.getEmployeeId(), x.getDate()));
		});
		processDto(dailyOlds, dailyEdits, dataParent, querys, mapSidDate, pairSidDateCheck, queryNotChanges);
		// row data will insert
		Set<Pair<String, GeneralDate>> rowWillInsert = dailyEdits.stream()
				.map(x -> Pair.of(x.getEmployeeId(), x.getDate())).collect(Collectors.toSet());

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

		// List<DPItemValue> itemInputDeviation = new ArrayList<>();
		// 計算フラグをチェックする
		if (!dataParent.isFlagCalculation() || (dataParent.isFlagCalculation() && dataParent.isErrorAllSidDate())) {
			Map<Pair<String, GeneralDate>, ResultReturnDCUpdateData> lstResultTemp = new HashMap<>();
			for (Entry<Pair<String, GeneralDate>, List<DPItemValue>> x : mapSidDate.entrySet()) {
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
				List<DailyModifyResult> itemOlds = resultOlds.stream()
						.filter(y -> x.getKey().equals(Pair.of(y.getEmployeeId(), y.getDate())))
						.collect(Collectors.toList());
				List<DPItemValue> items = validatorDataDaily.checkCareItemDuplicate(itemCovert, itemOlds);
				// if (!items.isEmpty()) {
				itemErrors.addAll(items);
				// } else {
				List<DPItemValue> itemInputs = validatorDataDaily.checkInputData(itemCovert, itemValues);
				itemInputErors.addAll(itemInputs);
				// }
				itemInputWorkType = dataParent.getLstNotFoundWorkType().stream()
						.filter(wt -> wt.getEmployeeId().equals(x.getKey().getLeft())
								&& wt.getDate().equals(x.getKey().getRight()))
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
		}

		dailyEdits = dailyEdits.stream()
				.filter(x -> !lstResultReturnDailyError.containsKey(Pair.of(x.getEmployeeId(), x.getDate())))
				.collect(Collectors.toList());
		dailyOlds = dailyOlds.stream()
				.filter(x -> !lstResultReturnDailyError.containsKey(Pair.of(x.getEmployeeId(), x.getDate())))
				.collect(Collectors.toList());

		if (dailyEdits.isEmpty()) {
			dataResultAfterIU.setErrorMap(ProcessCommonCalc.convertErrorToType(lstResultReturnDailyError, resultErrorMonth));
			dataResultAfterIU.setMessageAlert("Msg_1489");
			dataResultAfterIU.setErrorAllSidDate(true);
			dataResultAfterIU.setShowErrorDialog(dataParent.getShowDialogError());
			return dataResultAfterIU;
		}

		RCDailyCorrectionResult resultIU = new RCDailyCorrectionResult();
		Map<Pair<String, GeneralDate>, List<DPItemValue>> errorRelease = new HashMap<>();
		List<DailyItemValue> dailyItems = resultOlds.stream().map(
				x -> DailyItemValue.build().createEmpAndDate(x.getEmployeeId(), x.getDate()).createItems(x.getItems()))
				.collect(Collectors.toList());
		Set<Pair<String, GeneralDate>> updated = new HashSet<>();
		if (querys.isEmpty()
				&& (dataParent.getMonthValue() == null || dataParent.getMonthValue().getItems() == null
						|| dataParent.getMonthValue().getItems().isEmpty())
				&& (!dataParent.getDataCheckSign().isEmpty() || !dataParent.getDataCheckApproval().isEmpty()
						|| dataParent.getSpr() != null)) {
			if (dataParent.getMode() == 0) {
				long startTime = System.currentTimeMillis();
				int type = repo.getTypeAtrErrorSet(AppContexts.user().companyId(),
						ErAlWorkRecordCheckService.CONTINUOUS_CHECK_CODE);
				List<EmpErrorCode> lstEmpError = repo.getListErAlItem28(AppContexts.user().companyId(), type,
						dataParent.getDateRange(), dataParent.getEmployeeId());
				Map<GeneralDate, List<EmpErrorCode>> mapEmpError = lstEmpError.stream()
						.collect(Collectors.groupingBy(x -> x.getDate()));
				dataResultAfterIU.setLstErOldHoliday(getRemoveState(dataParent.getEmployeeId(), type, mapEmpError));

				System.out.println("tg get error: " + (System.currentTimeMillis() - startTime));
				dataCheck = validatorDataDaily.checkContinuousHolidays(dataParent.getEmployeeId(),
						dataParent.getDateRange(),
						dailyEdits.stream().map(c -> c.getWorkInfo().toDomain(c.getEmployeeId(), c.getDate()))
								.filter(c -> c != null).collect(Collectors.toList()));
				dataCheck = dataCheck.stream().map(x -> {
					x.setLayoutCode(String.valueOf(type));
					return x;
				}).collect(Collectors.toList());
				System.out.println("tg load check holiday: " + (System.currentTimeMillis() - startTime));
			}

			errorRelease = releaseSign(dataParent.getDataCheckSign(), new ArrayList<>(), dailyEdits,
					AppContexts.user().employeeId(), true);

			validatorDataDaily.checkVerConfirmApproval(dataParent.getApprovalConfirmCache(),
					dataParent.getDataCheckSign(), dataParent.getDataCheckApproval(), dataParent.getItemValues());
			// only insert check box
			// insert sign
			insertSign(dataParent.getDataCheckSign(), dailyEdits, dataParent.getDailyOlds(), updated);
			// if(dataParent.getDataCheckSign() != null){
			// updated.addAll(dataParent.getDataCheckSign().stream().map(c ->
			// Pair.of(c.getEmployeeId(), c.getDate())).collect(Collectors.toList()));
			// }
			// insert approval
			Set<Pair<String, GeneralDate>> dataApprovalCheck = insertApproval(dataParent.getDataCheckApproval(),
					updated);
			// if(dataParent.getDataCheckApproval() != null){
			// updated.addAll(dataParent.getDataCheckApproval().stream().map(c ->
			// Pair.of(c.getEmployeeId(), c.getDate())).collect(Collectors.toList()));
			// }

			if (dataParent.getSpr() != null) {
				processor.insertStampSourceInfo(dataParent.getSpr().getEmployeeId(), dataParent.getSpr().getDate(),
						dataParent.getSpr().isChange31(), dataParent.getSpr().isChange34());
				dailyEdits.stream()
						.filter(x -> x.getDate().equals(dataParent.getSpr().getDate())
								&& x.getEmployeeId().equals(dataParent.getSpr().getEmployeeId()))
						.map(x -> x.toDomain(x.getEmployeeId(), x.getDate())).forEach(d -> {
							// 任意項目更新
							d.getAnyItemValue().ifPresent(ai -> {
								anyItemValueOfDailyRepo.persistAndUpdate(ai);
							});
							updated.add(
									Pair.of(d.getWorkInformation().getEmployeeId(), d.getWorkInformation().getYmd()));
							// dailyTransaction.updated(d.getWorkInformation().getEmployeeId(),
							// d.getWorkInformation().getYmd());
						});
				// SPR連携時の確認承認解除
				clearConfirmApprovalService.clearConfirmApproval(dataParent.getSpr().getEmployeeId(),
						Arrays.asList(dataParent.getSpr().getDate()));
			}
			List<String> empList = updated.stream().map(x -> x.getLeft()).distinct().collect(Collectors.toList());
			List<GeneralDate> empDate = updated.stream().map(x -> x.getRight()).sorted((x, y) -> x.compareTo(y))
					.distinct().collect(Collectors.toList());
			Set<EmpAndDate> indentityChecked = dataParent.getDataCheckSign().isEmpty() ? new HashSet<>()
					: identificationRepository
							.findByListEmployeeID(new ArrayList<>(empList), empDate.get(0),
									empDate.get(empDate.size() - 1))
							.stream().map(x -> new EmpAndDate(x.getEmployeeId(), x.getProcessingYmd()))
							.collect(Collectors.toSet());
			dataResultAfterIU.setMapIndentityCheck(indentityChecked);
			dataResultAfterIU.setMapApprovalCheck(dataApprovalCheck.stream()
					.map(x -> new EmpAndDate(x.getLeft(), x.getRight())).collect(Collectors.toSet()));
			if (!dataParent.getDataCheckSign().isEmpty() || !dataParent.getDataCheckApproval().isEmpty())
				dataResultAfterIU.setOnlyLoadCheckBox(true);
			if (dataParent.isShowFlex() && !dataParent.getDataCheckSign().isEmpty()) {
				CheckShortage checkShortage = checkShortageFlex.checkShortageFlex(
						dataParent.getDataCheckSign().get(0).getEmployeeId(), dataParent.getDateRange().getEndDate());
				boolean checkFlex = checkShortage.isCheckShortage()
						&& dataParent.getDataCheckSign().get(0).getEmployeeId().equals(AppContexts.user().employeeId());
				dataResultAfterIU.setCanFlex(checkFlex);
			}
			dataResultAfterIU.setShowErrorDialog(null);

		} else {
			// if (querys.isEmpty() ? !dataParent.isFlagCalculation() :
			// true) {
			Map<Integer, DPAttendanceItemRC> itemAtr = dataParent.getLstAttendanceItem().entrySet().stream()
					.collect(Collectors.toMap(x -> x.getKey(), x -> convertItemAtr(x.getValue())));

			// 日別実績の修正からの計算
			String sid = AppContexts.user().employeeId();
			List<DailyRecordWorkCommand> commandNew = ProcessCommonCalc.createCommands(sid, dailyEdits, querys);

			List<DailyRecordWorkCommand> commandOld = ProcessCommonCalc.createCommands(sid, dailyOlds, querys);

			// 日別実績の修正からの計算
			resultIU = processDailyCalc.calcDaily(dailyEdits, commandNew, commandOld, dailyItems, monthParam, ExecutionType.NORMAL_EXECUTION);

			List<EmployeeMonthlyPerError> errorMonthHoliday = new ArrayList<>();
			if (!dataParent.isFlagCalculation() && dataParent.isCheckDailyChange()) {
				// 計算後エラーチェック
				ErrorAfterCalcDaily errorCheck = checkErrorAfterCalcDaily(resultIU, resultOlds, dataParent.getDateRange(),
						dataParent.getDailyEdits().stream().filter(
								x -> !lstResultReturnDailyError.containsKey(Pair.of(x.getEmployeeId(), x.getDate())))
								.collect(Collectors.toList()),
						dataParent.getItemValues().stream().filter(
								x -> !lstResultReturnDailyError.containsKey(Pair.of(x.getEmployeeId(), x.getDate())))
								.collect(Collectors.toList()));
				errorMonthHoliday.addAll(errorCheck.getErrorMonth());
				boolean hasError = errorCheck.getHasError();
				if (hasError) {
					resultErrorMonth = errorCheck.getResultErrorMonth();
					lstResultReturnDailyError.putAll(errorCheck.getResultError());
					dataResultAfterIU.setErrorMap(resultErrorMonth);
                    List<String> lstErrorMonthId = resultErrorMonth.values().stream().flatMap(x -> x.stream())
                            .map(x -> x.getEmployeeId()).distinct().collect(Collectors.toList());
					dailyEdits = dailyEdits.stream().filter(x -> !errorCheck.getResultError()
							.containsKey(Pair.of(x.getEmployeeId(), x.getDate()))
                            && !lstErrorMonthId.contains(x.getEmployeeId()))
							.collect(Collectors.toList());
					dailyOlds = dailyOlds.stream().filter(x -> !errorCheck.getResultError()
							.containsKey(Pair.of(x.getEmployeeId(), x.getDate()))
                            && !lstErrorMonthId.contains(x.getEmployeeId()))
							.collect(Collectors.toList());

					resultIU.filterDataError(
							errorCheck.getResultError().keySet().stream().map(x -> x.getLeft() + "|" + x.getRight())
									.collect(Collectors.toSet()),
							errorCheck.getLstErrorEmpMonth().stream().map(x -> x.getLeft() + "|" + x.getRight())
									.collect(Collectors.toSet()));
					if (dailyEdits.isEmpty()) {
						dataResultAfterIU.setErrorMap(ProcessCommonCalc.convertErrorToType(lstResultReturnDailyError, resultErrorMonth));
						dataResultAfterIU.setMessageAlert("Msg_1489");
						dataResultAfterIU.setErrorAllSidDate(true);
						dataResultAfterIU.setShowErrorDialog(dataParent.getShowDialogError());
						return dataResultAfterIU;
					}
				}
			}

			// 大塚カスタマイズチェック処理
			// 乖離エラー発生時の本人確認解除
			if (dataParent.getMode() == 0) {
				if (!dataParent.isFlagCalculation() && resultIU.getCommandNew() != null) {
					dataCheck = validatorDataDaily.checkContinuousHolidays(dataParent.getEmployeeId(),
							dataParent.getDateRange(),
							resultIU.getCommandNew().stream().map(c -> c.getWorkInfo().getData()).filter(c -> c != null)
									.collect(Collectors.toList()));
				} else if (dataParent.isFlagCalculation()) {
					dataCheck = validatorDataDaily.checkContinuousHolidays(dataParent.getEmployeeId(),
							dataParent.getDateRange(),
							dailyEdits.stream().map(c -> c.getWorkInfo().toDomain(null, null)).filter(c -> c != null)
									.collect(Collectors.toList()));
				}
			}
			val errorSign = validatorDataDaily.releaseDivergence(resultIU.getLstDailyDomain());
			if (!errorSign.isEmpty()) {
				// resultError.putAll(errorSign);
				errorRelease = releaseSign(dataParent.getDataCheckSign().stream()
						.filter(x -> !lstResultReturnDailyError.containsKey(Pair.of(x.getEmployeeId(), x.getDate())))
						.collect(Collectors.toList()), errorSign, dailyEdits, AppContexts.user().employeeId(), false);
			}
			
			validatorDataDaily.checkVerConfirmApproval(dataParent.getApprovalConfirmCache(),
					dataParent.getDataCheckSign(), dataParent.getDataCheckApproval(), dataParent.getItemValues());

			// 日次登録処理
			dailyItems = dailyItems.stream()
					.filter(x -> !lstResultReturnDailyError.containsKey(Pair.of(x.getEmployeeId(), x.getDate())))
					.collect(Collectors.toList());

			{
				CountDownLatch cdlDaily = new CountDownLatch(1);
				MutableValue<RuntimeException> excepDaily = new MutableValue<>();
				RCDailyCorrectionResult resultIUTemp = resultIU;
				UpdateMonthDailyParam monthParamTemp = monthParam;
				List<DailyRecordDto> dailyEditsTemp = dailyEdits;
				Runnable asyncDaily = () -> {
					try {
						if (dataParent.isCheckDailyChange() || flagTempCalc) {
							List<DailyItemValue> dailyItemForLog = AttendanceItemUtil
									.toItemValues(dataParent.getDailyOldForLog()).entrySet().stream()
									.map(c -> DailyItemValue.build()
											.createEmpAndDate(c.getKey().employeeId(), c.getKey().workingDate())
											.createItems(c.getValue()))
									.collect(Collectors.toList());

							this.insertAllData.handlerInsertAllDaily(resultIUTemp.getCommandNew(),
									resultIUTemp.getLstDailyDomain(), resultIUTemp.getCommandOld(), dailyItemForLog,
									resultIUTemp.isUpdate(), monthParamTemp, itemAtr);
							// 暫定データを登録する - Register provisional data
							List<DailyModifyResult> resultNews = AttendanceItemUtil.toItemValues(dailyEditsTemp)
									.entrySet().stream()
									.map(dto -> DailyModifyResult.builder().items(dto.getValue())
											.employeeId(dto.getKey().getEmployeeId())
											.workingDate(dto.getKey().getDate()).completed())
									.collect(Collectors.toList());
                            //暫定データを登録する
							registerTempData(dataParent.getMode(), resultOlds, resultNews);
						}
					} catch (RuntimeException ex) {
						excepDaily.set(ex);
					} finally {
						cdlDaily.countDown();
					}
				};

				if (Thread.currentThread().getName().indexOf("REQUEST:") == 0) {
					this.executerService
							.submit(AsyncTask.builder().withContexts().threadName("Daily").build(asyncDaily));
				} else {
					asyncDaily.run();
				}

				// processCalcMonth
				//月別実績の集計
				RCDailyCorrectionResult resultMonth = this.handler.processCalcMonth(commandNew, commandOld,
						resultIU.getLstDailyDomain(), dailyItems, true, monthParam, dataParent.getMode());

				// 集計後エラーチェック 
				ErrorAfterCalcDaily errorMonth = processMonthlyCalc.checkErrorAfterCalcMonth(resultMonth, monthParam,
						dataParent.getMode(), dataParent.getMonthValue(), dataParent.getDateRange(),
						editFlex ? errorMonthHoliday : new ArrayList<>());
				// map error holiday into result
				List<DPItemValue> lstItemErrorMonth = errorMonth.getResultErrorMonth().get(TypeError.ERROR_MONTH.value);
				if (lstItemErrorMonth != null) {
					List<DPItemValue> itemErrorMonth = dataResultAfterIU.getErrorMap().get(TypeError.ERROR_MONTH.value);
					if (itemErrorMonth == null) {
						// dataResultAfterIU.getErrorMap().put(TypeError.ERROR_MONTH.value,
						// lstItemErrorMonth);
						resultErrorMonth.put(TypeError.ERROR_MONTH.value, lstItemErrorMonth);
					} else {
						lstItemErrorMonth.addAll(itemErrorMonth);
						// dataResultAfterIU.getErrorMap().put(TypeError.ERROR_MONTH.value,
						// lstItemErrorMonth);
						resultErrorMonth.put(TypeError.ERROR_MONTH.value, lstItemErrorMonth);
					}
				}
				// 月次登録処理
				errorMonthAfterCalc = errorMonth.getHasError();
				if (!errorMonthAfterCalc)
					this.insertAllData.handlerInsertAllMonth(resultMonth.getLstMonthDomain(), monthParam);
				// dataResultAfterIU.setErrorMap(errorMonth.getResultError());
				dataResultAfterIU.setFlexShortage(errorMonth.getFlexShortage());

				try {
					cdlDaily.await();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}

				// 集計処理で例外が起きていたらここでthrow
				if (excepDaily.optional().isPresent()) {
					throw excepDaily.get();
				}
			}

			Set<Pair<String, GeneralDate>> rowAfterCheck = dailyEdits.stream()
					.map(x -> Pair.of(x.getEmployeeId(), x.getDate())).collect(Collectors.toSet());
			Set<Pair<String, GeneralDate>> rowRemoveInsert = rowWillInsert.stream()
					.filter(x -> !rowAfterCheck.contains(x)).collect(Collectors.toSet());
			dataParent.setDataCheckSign(dataParent.getDataCheckSign().stream()
					.filter(x -> !rowRemoveInsert.contains(Pair.of(x.getEmployeeId(), x.getDate())))
					.collect(Collectors.toList()));
			dataParent.setDataCheckApproval(dataParent.getDataCheckApproval().stream()
					.filter(x -> !rowRemoveInsert.contains(Pair.of(x.getEmployeeId(), x.getDate())))
					.collect(Collectors.toList()));
			// insert sign
			//日の本人確認を登録する
			insertSignD(dataParent.getDataCheckSign(), resultIU.getLstDailyDomain(), dataParent.getDailyOlds(),
					updated);
			// insert approval
			//日の承認を登録する
			insertApproval(dataParent.getDataCheckApproval(), updated);

			// SPR連携時の確認承認解除
			if (dataParent.getSpr() != null) {
				clearConfirmApprovalService.clearConfirmApproval(dataParent.getSpr().getEmployeeId(),
						Arrays.asList(dataParent.getSpr().getDate()));
			}
			if (dataParent.getSpr() != null && !lstResultReturnDailyError
					.containsKey(Pair.of(dataParent.getSpr().getEmployeeId(), dataParent.getSpr().getDate()))) {
				processor.insertStampSourceInfo(dataParent.getSpr().getEmployeeId(), dataParent.getSpr().getDate(),
						dataParent.getSpr().isChange31(), dataParent.getSpr().isChange34());

			}

			dataResultAfterIU.setShowErrorDialog(showError(resultIU.getLstDailyDomain(), new ArrayList<>()));

		}

		/** Finish update daily record */
		// finishDailyRecordRegis(updated, dataParent.getDailyOlds(), querys);

		if (!errorRelease.isEmpty()) {
			Map<Integer, List<DPItemValue>> errorTempDaily = new HashMap<>();
			errorRelease.forEach((key, value) -> {
				errorTempDaily.put(TypeError.RELEASE_CHECKBOX.value, value);
				lstResultReturnDailyError.put(key,
						new ResultReturnDCUpdateData(key.getLeft(), key.getRight(), errorTempDaily));
			});
		}

		// 大塚カスタマイズチェック処理
		if (dataParent.getMode() == 0) {
			val temHoliday = dataCheck;
			dataCheck.stream().forEach(x -> {
				Map<Integer, List<DPItemValue>> errorTempDaily = new HashMap<>();
				errorTempDaily.put(TypeError.CONTINUOUS.value, temHoliday);
				lstResultReturnDailyError.put(Pair.of(x.getEmployeeId(), x.getDate()),
						new ResultReturnDCUpdateData(x.getEmployeeId(), x.getDate(), errorTempDaily));
			});
		}

		dataResultAfterIU.setErrorMap(ProcessCommonCalc.convertErrorToType(lstResultReturnDailyError, resultErrorMonth));

		// 登録確認メッセージ
		if ((dataResultAfterIU.getErrorMap().isEmpty() && dataResultAfterIU.getErrorMap().values().isEmpty()
				&& !hasErrorRow
				&& (dataResultAfterIU.getFlexShortage() == null || (!dataResultAfterIU.getFlexShortage().isError()
						|| (dataResultAfterIU.getFlexShortage().isError() && !editFlex))))) {
			dataResultAfterIU.setMessageAlert("Msg_15");
		} else {
			Map<Integer, List<DPItemValue>> errorMapTemp = dataResultAfterIU.getErrorMap().entrySet().stream().filter(
					x -> x.getKey() != TypeError.CONTINUOUS.value && x.getKey() != TypeError.RELEASE_CHECKBOX.value)
					.collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue(), (x, y) -> x));
			if (errorMapTemp.values().isEmpty()
					&& (dataResultAfterIU.getFlexShortage() == null || (!dataResultAfterIU.getFlexShortage().isError()
							|| (dataResultAfterIU.getFlexShortage().isError() && !editFlex)))) {
				dataResultAfterIU.setMessageAlert("Msg_15");
			} else {
				dataResultAfterIU.setMessageAlert("Msg_1489");
			}
		}

		val empSidUpdate = dailyEdits.stream().map(x -> Pair.of(x.getEmployeeId(), x.getDate()))
				.collect(Collectors.toSet());
		empSidUpdate.addAll(updated);
		dataResultAfterIU.setLstSidDateDomainError(new ArrayList<>(empSidUpdate));
		return dataResultAfterIU;
	}

	public void finishDailyRecordRegis(Set<Pair<String, GeneralDate>> updated, List<DailyRecordDto> dailyEdits,
			List<DailyModifyQuery> querys) {
		if (!updated.isEmpty()) {
			updated.stream()
					.filter(u -> !querys.stream()
							.filter(q -> q.getBaseDate().equals(u.getValue()) && q.getEmployeeId().equals(u.getKey()))
							.findFirst().isPresent())
					.forEach(up -> {
						dailyEdits.stream().filter(
								d -> d.employeeId().equals(up.getKey()) && d.workingDate().equals(up.getValue()))
								.findFirst().ifPresent(d -> {
									dailyTransaction.updated(d.employeeId(), d.workingDate(),
											d.getWorkInfo().getVersion());
								});
					});
		}
	}

	
	private List<DailyModifyQuery> createQuerys(Map<Pair<String, GeneralDate>, List<DPItemValue>> mapSidDate) {
		List<DailyModifyQuery> querys = new ArrayList<>();
		mapSidDate.entrySet().forEach(x -> {
			List<ItemValue> itemCovert = x.getValue().stream()
					.map(y -> new ItemValue(y.getValue(), ValueType.valueOf(y.getValueType()), y.getLayoutCode(),
							y.getItemId()))
					.collect(Collectors.toList()).stream().filter(ProcessCommonCalc.distinctByKey(p -> p.itemId()))
					.collect(Collectors.toList());
			if (!itemCovert.isEmpty())
				querys.add(new DailyModifyQuery(x.getKey().getKey(), x.getKey().getValue(), itemCovert));
		});
		return querys;
	}

	private boolean insertSignD(List<DPItemCheckBox> dataCheckSign, List<IntegrationOfDaily> dailyEdit,
			List<DailyRecordDto> dailyOlds, Set<Pair<String, GeneralDate>> updated) {

		List<EmployeeDailyPerError> errors = dailyEdit.stream().map(c -> c.getEmployeeError()).flatMap(List::stream)
				.collect(Collectors.toList());

		return insertSignInternal(dataCheckSign, errors, dailyOlds, updated);
	}

	private boolean insertSign(List<DPItemCheckBox> dataCheckSign, List<DailyRecordDto> dailyEdit,
			List<DailyRecordDto> dailyOlds, Set<Pair<String, GeneralDate>> updated) {

		List<EmployeeDailyPerError> errors = dailyEdit.stream().map(c -> c.getErrors()).flatMap(List::stream)
				.map(c -> c.toDomain(c.getEmployeeID(), c.workingDate())).collect(Collectors.toList());

		return insertSignInternal(dataCheckSign, errors, dailyOlds, updated);
	}

	private boolean insertSignInternal(List<DPItemCheckBox> dataCheckSign, List<EmployeeDailyPerError> editErrors,
			List<DailyRecordDto> dailyOlds, Set<Pair<String, GeneralDate>> updated) {
		if (dataCheckSign.isEmpty())
			return false;

		editErrors.addAll(dailyOlds.stream()
				.filter(ol -> !editErrors.stream()
						.filter(e -> e.getDate().equals(ol.workingDate()) && e.getEmployeeID().equals(ol.employeeId()))
						.findFirst().isPresent())
				.map(ol -> ol.getErrors()).flatMap(List::stream)
				.map(oe -> oe.toDomain(oe.employeeId(), oe.workingDate())).collect(Collectors.toList()));

		ParamIdentityConfirmDay day = new ParamIdentityConfirmDay(AppContexts.user().employeeId(), dataCheckSign
				.stream().map(x -> new SelfConfirmDay(x.getDate(), x.isValue())).collect(Collectors.toList()));
		return registerIdentityConfirmDay.registerIdentity(day, editErrors, updated);
	}

	public Set<Pair<String, GeneralDate>> insertApproval(List<DPItemCheckBox> dataCheckApproval,
			Set<Pair<String, GeneralDate>> updated) {
		if (dataCheckApproval.isEmpty())
			return new HashSet<>();
		ParamDayApproval param = new ParamDayApproval(AppContexts.user().employeeId(),
				dataCheckApproval.stream()
						.map(x -> new ContentApproval(x.getDate(), x.isValue(), x.getEmployeeId(), x.isFlagRemoveAll()))
						.collect(Collectors.toList()));
		return registerDayApproval.registerDayApproval(param, updated);
	}

	public LeaveDayErrorDto mapDomainMonthChange(List<Pair<String, GeneralDate>> employeeChange,
			List<IntegrationOfDaily> domainDailyNew, List<IntegrationOfMonthly> domainMonthNew,
			List<DailyRecordDto> dailyDtoEditAll, DateRange dateRange, List<DPItemValue> lstItemEdits) {
		Set<String> employeeIds = employeeChange.stream().map(x -> x.getLeft()).collect(Collectors.toSet());
		String companyId = AppContexts.user().companyId();
		List<EmployeeMonthlyPerError> monthPer = new ArrayList<>();
		Set<Pair<String, GeneralDate>> detailEmployeeError = new HashSet<>();
		boolean onlyErrorOld = true;
		for (String emp : employeeIds) {
			// employeeIds.stream().forEach(emp -> {
			List<IntegrationOfDaily> domainDailyEditAll = dailyDtoEditAll.stream()
					.filter(x -> x.getEmployeeId().equals(emp)).map(x -> x.toDomain(null, null))
					.collect(Collectors.toList());
			domainDailyEditAll = unionDomain(domainDailyEditAll, domainDailyNew);
			// Acquire closing date corresponding to employee
			List<IntegrationOfDaily> dailyOfEmp = domainDailyEditAll.stream()
					.filter(x -> x.getWorkInformation().getEmployeeId().equals(emp)).collect(Collectors.toList());
			List<AttendanceTimeOfDailyPerformance> lstAttendanceTimeData = dailyOfEmp.stream()
					.filter(x -> x.getAttendanceTimeOfDailyPerformance().isPresent())
					.map(x -> x.getAttendanceTimeOfDailyPerformance().get()).collect(Collectors.toList());

			List<WorkInfoOfDailyPerformance> lstWorkInfor = dailyOfEmp.stream()
					.filter(x -> x.getWorkInformation() != null).map(x -> x.getWorkInformation())
					.collect(Collectors.toList()).stream().sorted((x, y) -> x.getYmd().compareTo(y.getYmd()))
					.collect(Collectors.toList());

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
				}
				;
			} else {
				Optional<AttendanceTimeOfMonthly> optMonthlyData = (domainMonthNew == null || domainMonthNew.isEmpty())
						? Optional.empty()
						: domainMonthNew.get(0).getAttendanceTime();
				TimeOffRemainErrorInputParam param = new TimeOffRemainErrorInputParam(companyId, emp,
						new DatePeriod(date.get(), date.get().addYears(1).addDays(-1)),
						new DatePeriod(dateRange.getStartDate(), dateRange.getEndDate()), false, lstAttendanceTimeData,
						lstWorkInfor, optMonthlyData);
				lstEmpMonthError.addAll(timeOffRemainErrorInfor.getErrorInfor(param));
				// monthPer.addAll(timeOffRemainErrorInfor.getErrorInfor(param));
			}

			// 勤務種類が変更されているかチェックする
			val itemEdit28s = lstItemEdits.stream().filter(it -> it.getEmployeeId().equals(emp) && it.getItemId() == 28)
					.map(it -> it.getValue()).collect(Collectors.toList());
			val lstWTClassification = new HashSet<>();
			if (!itemEdit28s.isEmpty()) {
				List<WorkType> lstWType = workTypeRepository.getPossibleWorkType(companyId, itemEdit28s);
				for (WorkType wt : lstWType) {
					// lstWType.stream().forEach(wt -> {
					val wtTemp = checkInGroupWorkPer(wt);
					if (wtTemp != null) {
						lstWTClassification.add(convertError(wtTemp));
						onlyErrorOld = false;
						val itemRow = lstItemEdits.stream()
								.filter(it -> it.getEmployeeId().equals(emp) && it.getItemId() == 28
										&& it.getValue().equals(wt.getWorkTypeCode().v()))
								.map(x -> Pair.of(x.getEmployeeId(), x.getDate())).collect(Collectors.toSet());
						detailEmployeeError.addAll(itemRow);
					}
					// });
				}

			}
			// boolean hasErrorInDB = !lstEmpMonthError.stream().filter(x ->
			// x.getErrorType()).collect(Collectors.toList()).isEmpty();
			lstEmpMonthError = lstWTClassification.isEmpty() ? lstEmpMonthError
					: lstEmpMonthError.stream()
							.filter(lstErrorTemp -> lstWTClassification.contains(lstErrorTemp.getErrorType()))
							.collect(Collectors.toList());

			monthPer.addAll(lstEmpMonthError);
			// });
		}

		return new LeaveDayErrorDto(onlyErrorOld, monthPer, detailEmployeeError);
	}

	private WorkTypeClassification checkInGroupWorkPer(WorkType wt) {
		if (wt.getDailyWork() == null)
			return null;

		WorkTypeUnit unit = wt.getDailyWork().getWorkTypeUnit();
		if (unit == WorkTypeUnit.OneDay) {
			val oneDay = wt.getDailyWork().getOneDay();
			if (oneDay == WorkTypeClassification.AnnualHoliday || oneDay == WorkTypeClassification.SpecialHoliday
					|| oneDay == WorkTypeClassification.SubstituteHoliday || oneDay == WorkTypeClassification.Pause || oneDay == WorkTypeClassification.YearlyReserved)
				return oneDay;
			// AnnualHoliday , SpecialHoliday, SubstituteHoliday, Pause
		} else {
			val morDay = wt.getDailyWork().getMorning();
			val aftDay = wt.getDailyWork().getAfternoon();
			if (morDay == WorkTypeClassification.AnnualHoliday || morDay == WorkTypeClassification.SpecialHoliday
					|| morDay == WorkTypeClassification.SubstituteHoliday || morDay == WorkTypeClassification.Pause || morDay == WorkTypeClassification.YearlyReserved)
				return morDay;

			if (aftDay == WorkTypeClassification.AnnualHoliday || aftDay == WorkTypeClassification.SpecialHoliday
					|| aftDay == WorkTypeClassification.SubstituteHoliday || aftDay == WorkTypeClassification.Pause || aftDay == WorkTypeClassification.YearlyReserved)
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
			
		case YearlyReserved:
		    return ErrorType.NUMBER_OF_MISSED_PIT;

		default:
			return ErrorType.YEARLY_HOLIDAY;
		}
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
		val old = ProcessCommonCalc.mapTo(resultOlds);
		val news = ProcessCommonCalc.mapTo(resultNews);
		old.entrySet().forEach(o -> {
			List<ItemValue> niv = ProcessCommonCalc.getFrom(news, o.getKey());
			if (!CollectionUtil.isEmpty(niv)) {
				if (niv.stream().anyMatch(c -> o.getValue().stream()
						.filter(oi -> c.valueAsObjet() != null && c.equals(oi)).findFirst().isPresent())) {
					editedDate.add(o.getKey());
				}
			}
		});
		return editedDate;
	}

	private DPAttendanceItemRC convertItemAtr(DPAttendanceItem item) {
		return new DPAttendanceItemRC(item.getId(), item.getName(), item.getDisplayNumber(), item.isUserCanSet(),
				item.getLineBreakPosition(), item.getAttendanceAtr(), item.getTypeGroup(), item.getPrimitive());
	}

	private Map<Pair<String, GeneralDate>, List<DPItemValue>> releaseSign(List<DPItemCheckBox> dataCheckApproval,
			List<DPItemValue> resultError, List<DailyRecordDto> dailys, String employeeId, boolean onlyCheckBox) {
		Map<Pair<String, GeneralDate>, List<DPItemValue>> itemUi = new HashMap<>();
		if (resultError.isEmpty() && !onlyCheckBox)
			return itemUi;
		val dailyClone = dailys.stream().map(x -> x.clone()).collect(Collectors.toList());
		val dailyTemps = dailyClone.stream().filter(x -> x.getEmployeeId().equals(employeeId))
				.sorted((x, y) -> x.getDate().compareTo(y.getDate())).collect(Collectors.toList());
		if (dailys.isEmpty() || dailyTemps.isEmpty())
			return itemUi;

		val lstEmployeeId = dailys.stream().map(x -> x.getEmployeeId()).collect(Collectors.toSet());
		val indentity = identificationRepository
				.findByListEmployeeID(new ArrayList<>(lstEmployeeId), dailyTemps.get(0).getDate(),
						dailyTemps.get(dailyTemps.size() - 1).getDate())
				.stream()
				.collect(Collectors.toMap(x -> Pair.of(x.getEmployeeId(), x.getProcessingYmd()), x -> "", (x, y) -> x));

		if (onlyCheckBox) {
			// resultError = employeeDailyPerErrorRepository
			// .findByPeriodOrderByYmd(employeeId,
			// new DatePeriod(
			// dailyTemps.get(0).getDate(), dailyTemps.get(dailyTemps.size() -
			// 1).getDate()))
			resultError = dailyTemps
					.stream().map(c -> c.getErrors()).flatMap(
							List::stream)
					.filter(err -> err.getErrorCode().startsWith("D")
							&& Integer.parseInt(err.getErrorCode().replace("D", "")) % 2 == 1
							&& (err.getMessage() != null
									|| !err.getMessage().equals(TextResource.localize("Msg_1298"))))
					.map(x -> {
						return new DPItemValue("", x.getEmployeeID(), x.getDate(), 0);
					}).collect(Collectors.toList());
		}

		Map<Pair<String, GeneralDate>, Boolean> mapRelease = resultError.stream()
				.collect(Collectors.toMap(x -> Pair.of(x.getEmployeeId(), x.getDate()), x -> true, (x, y) -> x));

		dataCheckApproval.stream().map(x -> {
			List<DPItemValue> itemUiTemp = new ArrayList<>();
			if (mapRelease.containsKey(Pair.of(x.getEmployeeId(), x.getDate())) && x.isValue()) {
				itemUiTemp.add(
						new DPItemValue("", x.getEmployeeId(), x.getDate(), 0, "", TextResource.localize("Msg_1455")));
				itemUi.put(Pair.of(x.getEmployeeId(), x.getDate()), itemUiTemp);
				mapRelease.remove(Pair.of(x.getEmployeeId(), x.getDate()));
				x.setValue(false);
				return x;
			} else {
				return x;
			}
		}).collect(Collectors.toList());

		val dateEmp = dataCheckApproval.stream()
				.collect(Collectors.toMap(x -> Pair.of(x.getEmployeeId(), x.getDate()), x -> "", (x, y) -> x));
		val itemNotUiRelease = dailyTemps.stream()
				.filter(x -> mapRelease.containsKey(Pair.of(x.getEmployeeId(), x.getDate()))
						&& !dateEmp.containsKey(Pair.of(x.getEmployeeId(), x.getDate())))
				.collect(Collectors.toList());

		itemNotUiRelease.stream().forEach(x -> {
			List<DPItemValue> itemUiTemp = new ArrayList<>();
			if (indentity.containsKey(Pair.of(x.getEmployeeId(), x.getDate()))) {
				itemUiTemp.add(
						new DPItemValue("", x.getEmployeeId(), x.getDate(), 0, "", TextResource.localize("Msg_1455")));
				itemUi.put(Pair.of(x.getEmployeeId(), x.getDate()), itemUiTemp);
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
		// fix bug 102116
		// them thuat toan ドメインモデル「勤務実績のエラーアラーム」を取得する
		// check table 'era set'
		List<String> errorList = lstError.stream().map(e -> e.getErrorAlarmWorkRecordCode().toString())
				.collect(Collectors.toList());
		errorList.addAll(lstErrorDto.stream().map(e -> e.getErrorCode()).collect(Collectors.toList()));
		boolean isErAl = repo.isErAl(AppContexts.user().companyId(),
				lstError.stream().map(e -> e.getErrorAlarmWorkRecordCode().toString()).collect(Collectors.toList()));
		if (isErAl == false)
			return false;
		return settingMaster == null ? false : settingMaster.isShowError();
	}

	public ErrorAfterCalcDaily checkErrorAfterCalcDaily(RCDailyCorrectionResult resultIU, List<DailyModifyResult> resultOlds,
			DateRange range, List<DailyRecordDto> dailyDtoEditAll, List<DPItemValue> lstItemEdits) {
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
		val pairError = mapDomainMonthChange(sidChange, resultIU.getLstDailyDomain(), resultIU.getLstMonthDomain(),
				dailyDtoEditAll, range, lstItemEdits);
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

	public ErrorAfterCalcDaily checkErrorAfterCalc(RCDailyCorrectionResult resultIU, UpdateMonthDailyParam monthlyParam,
			List<DailyModifyResult> resultOlds, int mode, DPMonthValue monthValue, DateRange range,
			List<DailyRecordDto> dailyEditAll, List<DPItemValue> lstItemEdits) {
		Map<Integer, List<DPItemValue>> resultError = new HashMap<>();
		Map<Pair<String, GeneralDate>, ResultReturnDCUpdateData> resultErrorDaily = new HashMap<>();
		boolean hasError = false;
		DataResultAfterIU dataResultAfterIU = new DataResultAfterIU();

		val errorDivergence = validatorDataDaily.errorCheckDivergence(resultIU.getLstDailyDomain(),
				resultIU.getLstMonthDomain());
		if (!errorDivergence.isEmpty()) {
			resultErrorDaily.putAll(errorDivergence);
			hasError = true;
		}
		if (mode == 0 && monthlyParam.getHasFlex() != null && monthlyParam.getHasFlex()) {
			val flexShortageRCDto = validatorDataDaily.errorCheckFlex(resultIU.getLstMonthDomain(), monthlyParam);
			if (flexShortageRCDto.isError() || !flexShortageRCDto.getMessageError().isEmpty()) {
				hasError = true;
				if (!resultIU.getLstMonthDomain().isEmpty())
					flexShortageRCDto.createDataCalc(ProcessCommonCalc.convertMonthToItem(
							MonthlyRecordWorkDto.fromOnlyAttTime(resultIU.getLstMonthDomain().get(0)), monthValue));
			}
			flexShortageRCDto.setVersion(monthValue.getVersion());
			dataResultAfterIU.setFlexShortage(flexShortageRCDto);
		}
		// 残数系のエラーチェック（月次集計なし）
		val sidChange = ProcessCommonCalc.itemInGroupChange(resultIU.getLstDailyDomain(), resultOlds);
		val pairError = mapDomainMonthChange(sidChange, resultIU.getLstDailyDomain(), resultIU.getLstMonthDomain(),
				dailyEditAll, range, lstItemEdits);
		Map<Integer, List<DPItemValue>> errorMonth = validatorDataDaily.errorMonthNew(pairError.getErrorMonth());
		// val errorMonth = validatorDataDaily.errorMonthNew();
		// val errorMonth = validatorDataDaily.errorMonth(resultIU.getLstMonthDomain(),
		// monthParam);
		Set<Pair<String, GeneralDate>> detailEmployeeError = new HashSet<>();
		if (!errorMonth.isEmpty()) {
			resultError.putAll(errorMonth);
			detailEmployeeError.addAll(pairError.getDetailEmployeeError());
			hasError = true;
		}

		return new ErrorAfterCalcDaily(hasError, resultError, detailEmployeeError, resultErrorDaily,
				dataResultAfterIU.getFlexShortage(), new ArrayList<>());
	}

	private List<IntegrationOfDaily> unionDomain(List<IntegrationOfDaily> parent, List<IntegrationOfDaily> child) {
		val date = child.stream().collect(Collectors.toMap(x -> x.getWorkInformation().getYmd(), x -> "", (x, y) -> x));
		val resultFilter = parent.stream().filter(x -> !date.containsKey(x.getWorkInformation().getYmd()))
				.collect(Collectors.toList());
		resultFilter.addAll(child);
		return resultFilter;
	}

	public void createStampSourceInfo(DailyRecordDto dtoEdit, List<DailyModifyQuery> querys) {
		val sidLogin = AppContexts.user().employeeId();
		boolean editBySelf = sidLogin.equals(dtoEdit.getEmployeeId());
		Integer stampSource = editBySelf ? StampSourceInfo.HAND_CORRECTION_BY_MYSELF.value
				: StampSourceInfo.HAND_CORRECTION_BY_ANOTHER.value;
		List<ItemValue> itemValueTempDay = querys.stream().filter(
				x -> x.getEmployeeId().equals(dtoEdit.getEmployeeId()) && x.getBaseDate().equals(dtoEdit.getDate()))
				.flatMap(x -> x.getItemValues().stream()).collect(Collectors.toList());
		List<ItemValue> itemValue = itemValueTempDay.stream()
				.filter(x -> DPText.ITEM_INSERT_STAMP_SOURCE.contains(x.getItemId())).collect(Collectors.toList());
		itemValue.stream().forEach(x -> {
			try {
				switch (x.getItemId()) {
				case 75:
				case 79:
				case 73:
					dtoEdit.getAttendanceLeavingGate().get().getAttendanceLeavingGateTime()
							.get(Math.abs(75 - x.getItemId()) / 4).getStart().setStampSourceInfo(stampSource);
					break;
				case 77:
				case 81:
				case 85:
					dtoEdit.getAttendanceLeavingGate().get().getAttendanceLeavingGateTime()
							.get(Math.abs(77 - x.getItemId()) / 4).getEnd()
							.setStampSourceInfo(StampSourceInfo.HAND_CORRECTION_BY_ANOTHER.value);
					break;
				case 31:
				case 41:
					if (x.getItemId() == 31 && dtoEdit.getTimeLeaving().get().getWorkAndLeave().get(0).getWorking()
							.getTime().getStampSourceInfo() != StampSourceInfo.SPR.value) {
						dtoEdit.getTimeLeaving().get().getWorkAndLeave().get(Math.abs(31 - x.getItemId()) / 10)
								.getWorking().getTime().setStampSourceInfo(stampSource);
					} else if (x.getItemId() == 41) {
						dtoEdit.getTimeLeaving().get().getWorkAndLeave().get(1).getWorking().getTime()
								.setStampSourceInfo(stampSource);
					}
					break;
				case 34:
				case 44:
					if (x.getItemId() == 34
							&& dtoEdit.getTimeLeaving().get().getWorkAndLeave().get(Math.abs(34 - x.getItemId()) / 10)
									.getLeave().getTime().getStampSourceInfo() != StampSourceInfo.SPR.value) {
						dtoEdit.getTimeLeaving().get().getWorkAndLeave().get(0).getLeave().getTime()
								.setStampSourceInfo(stampSource);
					} else if (x.getItemId() == 44) {
						dtoEdit.getTimeLeaving().get().getWorkAndLeave().get(1).getLeave().getTime()
								.setStampSourceInfo(stampSource);
					}
					break;
				case 51:
				case 59:
				case 67:
					dtoEdit.getTemporaryTime().get().getWorkLeaveTime().get(Math.abs(51 - x.getItemId()) / 8)
							.getWorking().getTime().setStampSourceInfo(stampSource);
					break;
				case 53:
				case 61:
				case 69:
					dtoEdit.getTemporaryTime().get().getWorkLeaveTime().get(Math.abs(53 - x.getItemId()) / 8).getLeave()
							.getTime().setStampSourceInfo(stampSource);
					break;

				default:
					break;
				}
			} catch (Exception e) {
				System.out.print("Lỗi map createStampSourceInfo itemId: " + x.getItemId());
			}
		});
	}

	private List<EmpErrorCode> getRemoveState(String employeeId, int typeError,
			Map<GeneralDate, List<EmpErrorCode>> mapErrorCode) {
		List<EmpErrorCode> lstResult = new ArrayList<>();
		mapErrorCode.forEach((key, values) -> {
			List<EmpErrorCode> lstError28 = values.stream().filter(x -> x.getItemId() != null && x.getItemId() == 28)
					.collect(Collectors.toList());
			List<EmpErrorCode> lstErrorOtherHoliday28 = values.stream()
					.filter(x -> x.getItemId() == null || (x.getItemId() != 28)
							|| (x.getItemId() == 28
									&& !x.getErrorCode().equals(ErAlWorkRecordCheckService.CONTINUOUS_CHECK_CODE)))
					.collect(Collectors.toList());
			Optional<EmpErrorCode> errorCheck = lstError28.stream()
					.filter(y -> y.getErrorCode().equals(ErAlWorkRecordCheckService.CONTINUOUS_CHECK_CODE)).findFirst();
			if (errorCheck.isPresent()) {
				lstResult.add(new EmpErrorCode(employeeId, key, String.valueOf(typeError),
						!lstErrorOtherHoliday28.isEmpty() ? 1 : 0, lstError28.size() == 1));
			}
		});
		return lstResult;
	}
}

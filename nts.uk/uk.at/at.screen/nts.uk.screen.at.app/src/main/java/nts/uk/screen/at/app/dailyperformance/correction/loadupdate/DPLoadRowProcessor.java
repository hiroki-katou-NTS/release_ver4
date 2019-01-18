package nts.uk.screen.at.app.dailyperformance.correction.loadupdate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.tuple.Pair;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.dailyperform.DailyRecordDto;
import nts.uk.ctx.at.record.dom.daily.itemvalue.DailyItemValue;
import nts.uk.ctx.at.record.dom.workinformation.enums.CalculationState;
import nts.uk.ctx.at.shared.dom.attendance.util.AttendanceItemUtil;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ItemValue;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.screen.at.app.dailymodify.query.DailyModifyQueryProcessor;
import nts.uk.screen.at.app.dailymodify.query.DailyModifyResult;
import nts.uk.screen.at.app.dailyperformance.correction.DailyPerformanceCorrectionProcessor;
import nts.uk.screen.at.app.dailyperformance.correction.DailyPerformanceScreenRepo;
import nts.uk.screen.at.app.dailyperformance.correction.GetDataDaily;
import nts.uk.screen.at.app.dailyperformance.correction.datadialog.CodeName;
import nts.uk.screen.at.app.dailyperformance.correction.datadialog.CodeNameType;
import nts.uk.screen.at.app.dailyperformance.correction.datadialog.DataDialogWithTypeProcessor;
import nts.uk.screen.at.app.dailyperformance.correction.dto.ApprovalUseSettingDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.CellEdit;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPAttendanceItem;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPCellDataDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPCellStateDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPControlDisplayItem;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPDataDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPErrorDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPErrorSettingDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DailyPerformanceCorrectionDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DailyRecEditSetDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DateRange;
import nts.uk.screen.at.app.dailyperformance.correction.dto.IdentityProcessUseSetDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.OperationOfDailyPerformanceDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.WorkInfoOfDailyPerformanceDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.checkapproval.ApproveRootStatusForEmpDto;
import nts.uk.screen.at.app.dailyperformance.correction.identitymonth.CheckIndentityMonth;
import nts.uk.screen.at.app.dailyperformance.correction.identitymonth.IndentityMonthParam;
import nts.uk.screen.at.app.dailyperformance.correction.identitymonth.IndentityMonthResult;
import nts.uk.screen.at.app.dailyperformance.correction.lock.DPLock;
import nts.uk.screen.at.app.dailyperformance.correction.lock.DPLockDto;
import nts.uk.screen.at.app.dailyperformance.correction.monthflex.DPMonthFlexParam;
import nts.uk.screen.at.app.dailyperformance.correction.monthflex.DPMonthFlexProcessor;
import nts.uk.screen.at.app.dailyperformance.correction.text.DPText;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class DPLoadRowProcessor {
	
	@Inject
	private DailyPerformanceScreenRepo repo;
	
	@Inject 
	private DailyPerformanceCorrectionProcessor process;
	
	@Inject
	private DailyModifyQueryProcessor dailyModifyQueryProcessor;
	
    @Inject
	private DataDialogWithTypeProcessor dataDialogWithTypeProcessor;
    
    @Inject
    private DPMonthFlexProcessor dPMonthFlexProcessor;
    
    @Inject
	private DPLock findLock;
    
//    @Inject
//	private CheckIndentityDayConfirm checkIndentityDayConfirm;
    
//    @Inject
//	private ClosureService closureService;
    
    @Inject
	private CheckIndentityMonth checkIndentityMonth;
    
    @Inject
	private ClosureService closureService;
    
	public DailyPerformanceCorrectionDto reloadGrid(DPPramLoadRow param){
		DailyPerformanceCorrectionDto result = new DailyPerformanceCorrectionDto();
		
		DateRange dateRange = param.getDateRange();
		Integer mode = param.getMode();
		Integer displayFormat = param.getDisplayFormat();
		List<DPDataDto> lstDataTemp = param.getLstData();
//		List<Integer> itemIds = param.getLstAttendanceItem().stream().map(x -> x.getId()).collect(Collectors.toList());
		result.setIdentityProcessDto(param.getIdentityProcess());
		String NAME_EMPTY = TextResource.localize("KDW003_82");
		String NAME_NOT_FOUND = TextResource.localize("KDW003_81");
		String companyId = AppContexts.user().companyId();
		String sId = AppContexts.user().employeeId();
		
		List<String> listEmployeeId = param.getLstData().stream().map(x -> x.getEmployeeId()).collect(Collectors.toSet()).stream().collect(Collectors.toList());
		result.setLstData(lstDataTemp);
		
		if (displayFormat == 0) {
			// フレックス情報を表示する
			OperationOfDailyPerformanceDto dailyPerformanceDto = repo.findOperationOfDailyPerformance();
			//if (!listEmployeeId.isEmpty()){
			String emp = param.getOnlyLoadMonth() ? param.getLstEmployee().get(0).getId() : listEmployeeId.get(0);
				result.setMonthResult(dPMonthFlexProcessor
						.getDPMonthFlex(new DPMonthFlexParam(companyId, emp, param.getDateMonth(),
								process.getEmploymentCode(companyId, param.getDateMonth(), emp), dailyPerformanceDto, param.getAutBussCode())));
			// screenDto.setFlexShortage(null);
			//}
			if (emp.equals(sId) && !param.getOnlyLoadMonth()) {
				//社員に対応する締め期間を取得する
				DatePeriod period = closureService.findClosurePeriod(emp, dateRange.getEndDate());
				
				//パラメータ「日別実績の修正の状態．対象期間．終了日」がパラメータ「締め期間」に含まれているかチェックする
				if (!period.contains(dateRange.getEndDate())) {
					result.setIndentityMonthResult(new IndentityMonthResult(false, true, true));
					//対象日の本人確認が済んでいるかチェックする
					//screenDto.checkShowTighProcess(displayFormat, true);
				} else {
					// checkIndenityMonth
					result.setIndentityMonthResult(checkIndentityMonth.checkIndenityMonth(
							new IndentityMonthParam(companyId, sId, GeneralDate.today())));
					//対象日の本人確認が済んでいるかチェックする
					result.checkShowTighProcess(displayFormat, true);
				}
			}else {
				result.getIndentityMonthResult().setHideAll(true);
			}
		}
		if(param.getOnlyLoadMonth()){
			return result;
		}
		List<DailyRecEditSetDto> dailyRecEditSets = repo.getDailyRecEditSet(listEmployeeId, dateRange);
		Map<String, Integer> dailyRecEditSetsMap = dailyRecEditSets.stream()
				.collect(Collectors.toMap(x -> process.mergeString(String.valueOf(x.getAttendanceItemId()), "|", x.getEmployeeId(), "|",
						process.converDateToString(x.getProcessingYmd())),
						x -> x.getEditState()));
		/// アルゴリズム「実績エラーをすべて取得する」を実行する | Execute "Acquire all actual errors"
		List<DPErrorDto> lstError = listEmployeeId.isEmpty() ? new ArrayList<>()
				: repo.getListDPError(dateRange, listEmployeeId);
				
		Map<String, String> listEmployeeError = lstError.stream()
				.collect(Collectors.toMap(e -> e.getEmployeeId(), e -> "", (x, y) -> x));
		if (displayFormat == 2) {
			// only filter data error
			listEmployeeId = listEmployeeId.stream().filter(x -> listEmployeeError.containsKey(x))
					.collect(Collectors.toList());
			result.setLstData(result.getLstData().stream()
					.filter(x -> listEmployeeError.containsKey(x.getEmployeeId())).collect(Collectors.toList()));
		}
		
		
		//List<DailyModifyResult> results = new GetDataDaily(listEmployeeId, dateRange, itemIds, dailyModifyQueryProcessor).call();
		Map<String, List<GeneralDate>> mapDate = param.getLstData().stream()
				.collect(Collectors.groupingBy(x -> x.getEmployeeId(), Collectors.collectingAndThen(Collectors.toList(),
						x -> x.stream().map(y -> y.getDate()).collect(Collectors.toList()))));
		Pair<List<DailyModifyResult>, List<DailyRecordDto>> resultPair = new GetDataDaily(mapDate, dailyModifyQueryProcessor).getDataRow();
		List<DailyModifyResult> resultDailys = resultPair.getLeft();
		//List<DailyRecordDto> dailyRow = resultPair.getRight();
		Map<Pair<String, GeneralDate>, DailyRecordDto> mapDtoChange = resultPair.getRight().stream().collect(Collectors.toMap(x -> Pair.of(x.getEmployeeId(), x.getDate()), x -> x));
		result.setDomainOld(param.getDailys().stream().map(x ->{
			return mapDtoChange.getOrDefault(Pair.of(x.getEmployeeId(), x.getDate()), x);
		}).collect(Collectors.toList()));
		
		Map<Pair<String, GeneralDate>, DailyRecordDto> mapDtoOld = param.getDailys().stream().collect(Collectors.toMap(x -> Pair.of(x.getEmployeeId(), x.getDate()), x -> x));
		result.setLstCellStateCalc(itemCalcScreen(mapDtoChange, mapDtoOld, param.getLstData(), param.getLstAttendanceItem().stream().collect(Collectors.toMap(x -> x.getId(), x -> x)), param.getCellEdits()).getLeft());
		Map<String, DailyModifyResult> resultDailyMap = resultDailys.stream().collect(Collectors
				.toMap(x -> process.mergeString(x.getEmployeeId(), "|", x.getDate().toString()), Function.identity(), (x, y) -> x));
		
		List<WorkInfoOfDailyPerformanceDto> workInfoOfDaily = repo.getListWorkInfoOfDailyPerformance(listEmployeeId,
				dateRange);
		
		Map<Integer, DPAttendanceItem> mapDP = param.getLstAttendanceItem().stream()
						.collect(Collectors.toMap(DPAttendanceItem::getId, x -> x));
		result.setLstEmployee(param.getLstEmployee());
		
		result.markLoginUser(sId);
		long start1 = System.currentTimeMillis();
		result.createModifierCellStateCaseRow(mapDP, param.getLstHeader());
		System.out.println("time disable : " + (System.currentTimeMillis() - start1));
		
		if (result.getLstEmployee().size() > 0) {
			if (lstError.size() > 0) {
				// Get list error setting
				List<DPErrorSettingDto> lstErrorSetting = this.repo
						.getErrorSetting(companyId, lstError.stream().map(e -> e.getErrorCode()).collect(Collectors.toList()), true, true, false);
				// Seperate Error and Alarm
				if(lstErrorSetting.isEmpty()) {
					lstError = new ArrayList<>();
				}
				result.addErrorToResponseData(lstError, lstErrorSetting, mapDP, false);
			}
		}
		
		Set<Integer> types = param.getLstAttendanceItem().stream().map(x -> x.getTypeGroup()).filter(x -> x != null)
						.collect(Collectors.toSet());
		Map<Integer, Map<String, CodeName>> mapGetName = dataDialogWithTypeProcessor
				.getAllCodeName(new ArrayList<>(types), companyId, dateRange.getEndDate());
		CodeNameType codeNameReason = dataDialogWithTypeProcessor.getReason(companyId);
		Map<String, CodeName> codeNameReasonMap = codeNameReason != null
				? codeNameReason.getCodeNames().stream()
						.collect(Collectors.toMap(x -> process.mergeString(x.getCode(), "|", x.getId()), x -> x))
				: Collections.emptyMap();

		// No 20 get submitted application
		// disable check box sign
		Map<String, Boolean> disableSignMap = new HashMap<>();
		Map<String, String> appMapDateSid = process.getApplication(listEmployeeId, dateRange, disableSignMap);
		
		Optional<IdentityProcessUseSetDto> identityProcessDtoOpt = repo.findIdentityProcessUseSet(companyId);
		result.setIdentityProcessDto(identityProcessDtoOpt.isPresent() ? identityProcessDtoOpt.get()
				: new IdentityProcessUseSetDto(false, false, null));
		Optional<ApprovalUseSettingDto> approvalUseSettingDtoOpt = repo.findApprovalUseSettingDto(companyId);		
		
		Map<String, ItemValue> itemValueMap = new HashMap<>();
		List<DPDataDto> lstData = new ArrayList<DPDataDto>();
		//get status check box 
		DPLockDto dpLock = findLock.checkLockAll(companyId, listEmployeeId, dateRange, sId, mode, identityProcessDtoOpt, approvalUseSettingDtoOpt);
		for (DPDataDto data : result.getLstData()) {
			data.resetData();
			data.setEmploymentCode(result.getEmploymentCode());
			if (!sId.equals(data.getEmployeeId())) {
				result.setCellSate(data.getId(), DPText.LOCK_APPLICATION, DPText.STATE_DISABLE);
			}
			// map name submitted into cell
			if (appMapDateSid.containsKey(data.getEmployeeId() + "|" + data.getDate())) {
				data.addCellData(new DPCellDataDto(DPText.COLUMN_SUBMITTED,
						appMapDateSid.get(data.getEmployeeId() + "|" + data.getDate()), "", ""));
			} else {
				data.addCellData(new DPCellDataDto(DPText.COLUMN_SUBMITTED, "", "", ""));
			}
			data.addCellData(new DPCellDataDto(DPText.COLUMN_SUBMITTED, "", "", ""));
			data.addCellData(new DPCellDataDto(DPText.LOCK_APPLICATION, "", "", ""));
			//set checkbox sign
			data.setSign(dpLock.getSignDayMap().containsKey(data.getEmployeeId() + "|" + data.getDate()));
			if(disableSignMap.containsKey(data.getEmployeeId() + "|" + data.getDate()) && disableSignMap.get(data.getEmployeeId() + "|" + data.getDate())){
				result.setCellSate(data.getId(), DPText.LOCK_SIGN, DPText.STATE_DISABLE);
			}
			
			ApproveRootStatusForEmpDto approveRootStatus =  dpLock.getLockCheckApprovalDay().get(data.getEmployeeId() + "|" + data.getDate());
		//	if(mode == ScreenMode.APPROVAL.value){
			data.setApproval(approveRootStatus == null ? false : approveRootStatus.isCheckApproval());
		//	}
			ApproveRootStatusForEmpDto approvalCheckMonth = dpLock.getLockCheckMonth().get(data.getEmployeeId() + "|" + data.getDate());
			DailyModifyResult resultOfOneRow = process.getRow(resultDailyMap, data.getEmployeeId(), data.getDate());
			
			if (resultOfOneRow != null && (displayFormat == 2 ? !data.getError().equals("") : true)) {
				process.lockDataCheckbox(sId, result, data, identityProcessDtoOpt, approvalUseSettingDtoOpt, approveRootStatus, mode, data.isApproval(), data.isSign());
				boolean lockDaykWpl = false, lockHist = false, lockApprovalMonth = false, lockConfirmMonth = false;
				if (param.isShowLock()) {
					lockDaykWpl = process.checkLockAndSetState(dpLock.getLockDayAndWpl(), data);
					lockHist = process.lockHist(dpLock.getLockHist(), data);
					lockApprovalMonth = approvalCheckMonth == null ? false : approvalCheckMonth.isCheckApproval();
					lockConfirmMonth = process.checkLockConfirmMonth(dpLock.getLockConfirmMonth(), data);
					lockDaykWpl = process.lockAndDisable(result, data, mode, lockDaykWpl, data.isApproval(), lockHist,
							data.isSign(), lockApprovalMonth, lockConfirmMonth);
				} else {
					lockDaykWpl = process.lockAndDisable(result, data, mode, lockDaykWpl, false, lockHist,
							false, lockApprovalMonth, lockConfirmMonth);
				}
				
				itemValueMap = resultOfOneRow.getItems().stream()
						.collect(Collectors.toMap(x -> process.mergeString(String.valueOf(x.getItemId()), "|",
								data.getEmployeeId(), "|", data.getDate().toString()), x -> x));
				DPControlDisplayItem dPControlDisplayItem = new DPControlDisplayItem();
				dPControlDisplayItem.setLstAttendanceItem(param.getLstAttendanceItem());
				process.processCellData(NAME_EMPTY, NAME_NOT_FOUND, result, dPControlDisplayItem, mapGetName, codeNameReasonMap,
						itemValueMap,  data, lockDaykWpl, dailyRecEditSetsMap, null);
				lstData.add(data);
				Optional<WorkInfoOfDailyPerformanceDto> optWorkInfoOfDailyPerformanceDto = workInfoOfDaily.stream()
						.filter(w -> w.getEmployeeId().equals(data.getEmployeeId())
								&& w.getYmd().equals(data.getDate()))
						.findFirst();
				if (optWorkInfoOfDailyPerformanceDto.isPresent()
						&& optWorkInfoOfDailyPerformanceDto.get().getState() == CalculationState.No_Calculated)
					result.setAlarmCellForFixedColumn(data.getId(), displayFormat);
			}
			
		}
		
		result.setLstData(lstData);
		return result;
	}
	
	public Pair<List<DPCellStateDto>, List<DailyModifyResult>> itemCalcScreen(Map<Pair<String, GeneralDate>, DailyRecordDto> mapDtoChange, Map<Pair<String, GeneralDate>, DailyRecordDto> mapDtoOld, List<DPDataDto> lstData, Map<Integer, DPAttendanceItem> itemHeaders, List<CellEdit> cellEdits){
		List<DPCellStateDto> lstCellCalcAll = new ArrayList<>();
		List<DailyModifyResult> dailyModifyResult = new ArrayList<>();
		mapDtoChange.forEach((key, dtoNew) -> {
			val dtoOld = mapDtoOld.get(key);
			DailyItemValue dailyItemNew = DailyItemValue.build().createItems(AttendanceItemUtil.toItemValues(dtoNew, itemHeaders.keySet()).stream().sorted((x, y) -> x.getItemId() - y.getItemId()).collect(Collectors.toList()))
							.createEmpAndDate(dtoNew.employeeId(), dtoNew.workingDate());
			
			DailyItemValue dailyItemOld = DailyItemValue.build().createItems(AttendanceItemUtil.toItemValues(dtoOld, itemHeaders.keySet()).stream().sorted((x, y) -> x.getItemId() - y.getItemId()).collect(Collectors.toList()))
					.createEmpAndDate(dtoOld.employeeId(), dtoOld.workingDate());
			
			Optional<DPDataDto> data = lstData.stream().filter(x -> x.getDate().equals(key.getRight()) && x.getEmployeeId().equals(key.getLeft())).findFirst();
			
			if (data.isPresent()) {
				val resultCompare = lstCellCalc(dailyItemOld.getItems(), dailyItemNew.getItems(), itemHeaders,
						data.get(), cellEdits);
				lstCellCalcAll.addAll(resultCompare.getLeft());
				dailyModifyResult.add(new DailyModifyResult().employeeId(dailyItemNew.getEmployeeId())
						.workingDate(dailyItemNew.getDate()).items(resultCompare.getRight()));
			}
		});
		return Pair.of(lstCellCalcAll, dailyModifyResult);
	}
	
	public Pair<List<DPCellStateDto>, List<ItemValue>>  lstCellCalc(List<ItemValue> itemOlds, List<ItemValue> itemNews, Map<Integer, DPAttendanceItem> itemHeaders, DPDataDto dataSource, List<CellEdit> cellEdits){
		List<DPCellStateDto> lstCellCalc = new ArrayList<>();
		itemOlds = itemOlds.stream().sorted((x, y) -> x.getItemId() - y.getItemId()).collect(Collectors.toList());
		List<ItemValue> itemNewTemps = itemNews.stream().sorted((x, y) -> x.getItemId() - y.getItemId()).collect(Collectors.toList());
		List<ItemValue> items = compareValue(itemNewTemps, itemOlds, itemHeaders).stream().filter(x ->!cellEdits.contains(new CellEdit("_"+dataSource.getId(), x.getItemId()))).collect(Collectors.toList());
		items.stream().forEach(x ->{
			val item = itemHeaders.get(x.getItemId());
			if (item != null && itemHeaders.get(x.getItemId()).getAttendanceAtr() == 4) {
				lstCellCalc.add(new DPCellStateDto("_"+dataSource.getId(), DPText.NO+ x.getItemId(), Arrays.asList(DPText.STATE_CALC)));
				lstCellCalc.add(new DPCellStateDto("_"+dataSource.getId(), DPText.NAME+ x.getItemId(), Arrays.asList(DPText.STATE_CALC)));
			} else if (item != null) {
				lstCellCalc.add(new DPCellStateDto("_"+dataSource.getId(), DPText.ADD_CHARACTER+ x.getItemId(), Arrays.asList(DPText.STATE_CALC)));
			}else{
				//
			}
		});
		return Pair.of(lstCellCalc, items);
	}
	
	private List<ItemValue> compareValue(List<ItemValue> itemNewTemps, List<ItemValue> itemOlds,
			Map<Integer, DPAttendanceItem> itemHeaders) {
		List<ItemValue> result = new ArrayList<>();
		val mapItemNew = itemNewTemps.stream().collect(Collectors.toMap(x -> x.getItemId(), x -> x));
		itemOlds.stream().forEach(x -> {
			val itemNew = mapItemNew.get(x.getItemId());
			val atr = itemHeaders.get(x.itemId());
			// TimeWithDay
			if (atr.isNumber()
					&& ((itemNew.getValue() == null && x.getValue() != null && Double.parseDouble(x.getValue()) == 0)
					   || (x.getValue() == null && itemNew.getValue() != null && Double.parseDouble(itemNew.getValue()) == 0) 
					   || (itemNew.getValue() != null && x.getValue() != null && Double.parseDouble(x.getValue()) == 0 && Double.parseDouble(itemNew.getValue()) == 0))) {
				itemNew.value(0);
				x.value(0);
			}
            if(x.getValue() != null && itemNew.getValue() != null) {
            	if(!x.getValue().equals(itemNew.getValue())) result.add(itemNew);
            }else if (!x.equals(itemNew)) {
				result.add(itemNew);
			}
		});
		return result;
	}

}

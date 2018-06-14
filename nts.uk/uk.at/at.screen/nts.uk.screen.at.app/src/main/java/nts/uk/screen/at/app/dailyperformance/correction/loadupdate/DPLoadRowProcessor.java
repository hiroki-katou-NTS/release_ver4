package nts.uk.screen.at.app.dailyperformance.correction.loadupdate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.ApprovalStatusAdapter;
import nts.uk.ctx.at.record.dom.workinformation.enums.CalculationState;
import nts.uk.ctx.at.request.app.find.application.applicationlist.ApplicationExportDto;
import nts.uk.ctx.at.request.app.find.application.applicationlist.ApplicationListForScreen;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ItemValue;
import nts.uk.screen.at.app.dailymodify.query.DailyModifyQueryProcessor;
import nts.uk.screen.at.app.dailymodify.query.DailyModifyResult;
import nts.uk.screen.at.app.dailyperformance.correction.DailyPerformanceCorrectionProcessor;
import nts.uk.screen.at.app.dailyperformance.correction.DailyPerformanceScreenRepo;
import nts.uk.screen.at.app.dailyperformance.correction.GetDataDaily;
import nts.uk.screen.at.app.dailyperformance.correction.datadialog.CodeName;
import nts.uk.screen.at.app.dailyperformance.correction.datadialog.CodeNameType;
import nts.uk.screen.at.app.dailyperformance.correction.datadialog.DataDialogWithTypeProcessor;
import nts.uk.screen.at.app.dailyperformance.correction.dto.ApprovalUseSettingDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPAttendanceItem;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPCellDataDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPControlDisplayItem;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPDataDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPErrorDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPErrorSettingDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DailyPerformanceCorrectionDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DailyRecEditSetDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DateRange;
import nts.uk.screen.at.app.dailyperformance.correction.dto.IdentityProcessUseSetDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.ScreenMode;
import nts.uk.screen.at.app.dailyperformance.correction.dto.WorkInfoOfDailyPerformanceDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.checkapproval.ApproveRootStatusForEmpDto;
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
	private ApplicationListForScreen applicationListFinder;
    
    @Inject
	private ApprovalStatusAdapter approvalStatusAdapter;
    

	private static final String LOCK_EDIT_APPROVAL = "A";
	private static final String LOCK_APPLICATION = "Application";
	private static final String COLUMN_SUBMITTED = "Submitted";
	public static final int MINUTES_OF_DAY = 24 * 60;
	private static final String STATE_DISABLE = "ntsgrid-disable";
	private static final String LOCK_SIGN = "sign";


	
	public DailyPerformanceCorrectionDto reloadGrid(DPPramLoadRow param){
		DailyPerformanceCorrectionDto result = new DailyPerformanceCorrectionDto();
		
		DateRange dateRange = param.getDateRange();
		Integer mode = param.getMode();
		Integer displayFormat = param.getDisplayFormat();
		List<DPDataDto> lstDataTemp = param.getLstData();
		List<Integer> itemIds = param.getLstAttendanceItem().stream().map(x -> x.getId()).collect(Collectors.toList());
		
		String NAME_EMPTY = TextResource.localize("KDW003_82");
		String NAME_NOT_FOUND = TextResource.localize("KDW003_81");
		String companyId = AppContexts.user().companyId();
		String sId = AppContexts.user().employeeId();
		
		List<String> listEmployeeId = param.getLstData().stream().map(x -> x.getEmployeeId()).collect(Collectors.toSet()).stream().collect(Collectors.toList());
		result.setLstData(lstDataTemp);
		
		
		List<DailyRecEditSetDto> dailyRecEditSets = repo.getDailyRecEditSet(listEmployeeId, dateRange);
		Map<String, Integer> dailyRecEditSetsMap = dailyRecEditSets.stream()
				.collect(Collectors.toMap(x -> process.mergeString(String.valueOf(x.getAttendanceItemId()), "|", x.getEmployeeId(), "|",
						process.converDateToString(x.getProcessingYmd())),
						x -> x.getEditState()));
		/// アルゴリズム「実績エラーをすべて取得する」を実行する | Execute "Acquire all actual errors"
		List<DPErrorDto> lstError = listEmployeeId.isEmpty() ? new ArrayList<>()
				: repo.getListDPError(dateRange, listEmployeeId);;
				
		Map<String, String> listEmployeeError = lstError.stream()
				.collect(Collectors.toMap(e -> e.getEmployeeId(), e -> "", (x, y) -> x));
		if (displayFormat == 2) {
			// only filter data error
			listEmployeeId = listEmployeeId.stream().filter(x -> listEmployeeError.containsKey(x))
					.collect(Collectors.toList());
			result.setLstData(result.getLstData().stream()
					.filter(x -> listEmployeeError.containsKey(x.getEmployeeId())).collect(Collectors.toList()));
		}
		
		
		List<DailyModifyResult> results = new GetDataDaily(listEmployeeId, dateRange, itemIds, dailyModifyQueryProcessor).call();
		
		Map<String, DailyModifyResult> resultDailyMap = results.stream().collect(Collectors
				.toMap(x -> process.mergeString(x.getEmployeeId(), "|", x.getDate().toString()), Function.identity(), (x, y) -> x));
		
		List<WorkInfoOfDailyPerformanceDto> workInfoOfDaily = repo.getListWorkInfoOfDailyPerformance(listEmployeeId,
				dateRange);
		
		Map<Integer, DPAttendanceItem> mapDP =param.getLstAttendanceItem().stream()
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
						.getErrorSetting(lstError.stream().map(e -> e.getErrorCode()).collect(Collectors.toList()));
				// Seperate Error and Alarm
				result.addErrorToResponseData(lstError, lstErrorSetting, mapDP);
			}
		}
		
		Set<Integer> types = param.getLstAttendanceItem().stream().map(x -> x.getTypeGroup()).filter(x -> x != null)
						.collect(Collectors.toSet());
		Map<Integer, Map<String, String>> mapGetName = dataDialogWithTypeProcessor
				.getAllCodeName(new ArrayList<>(types), companyId);
		CodeNameType codeNameReason = dataDialogWithTypeProcessor.getReason(companyId);
		Map<String, CodeName> codeNameReasonMap = codeNameReason != null
				? codeNameReason.getCodeNames().stream()
						.collect(Collectors.toMap(x -> process.mergeString(x.getCode(), "|", x.getId()), x -> x))
				: Collections.emptyMap();

		// No 20 get submitted application
		// disable check box sign
		Map<String, Boolean> disableSignMap = new HashMap<>();
		Map<String, String> appMapDateSid = process.getApplication(listEmployeeId, dateRange, disableSignMap);
		//get  check box sign(Confirm day)
		Map<String, Boolean> signDayMap = repo.getConfirmDay(companyId, listEmployeeId, dateRange);
		
		Optional<IdentityProcessUseSetDto> identityProcessDtoOpt = repo.findIdentityProcessUseSet(companyId);
		result.setIdentityProcessDto(identityProcessDtoOpt.isPresent() ? identityProcessDtoOpt.get()
				: new IdentityProcessUseSetDto(false, false, null));
		Optional<ApprovalUseSettingDto> approvalUseSettingDtoOpt = repo.findApprovalUseSettingDto(companyId);		
		
		Map<String, ItemValue> itemValueMap = new HashMap<>();
		List<DPDataDto> lstData = new ArrayList<DPDataDto>();
		for (DPDataDto data : result.getLstData()) {
			data.resetData();
			data.setEmploymentCode(result.getEmploymentCode());
			if (!sId.equals(data.getEmployeeId())) {
				result.setLock(data.getId(), LOCK_APPLICATION, STATE_DISABLE);
			}
			// map name submitted into cell
			if (appMapDateSid.containsKey(data.getEmployeeId() + "|" + data.getDate())) {
				data.addCellData(new DPCellDataDto(COLUMN_SUBMITTED,
						appMapDateSid.get(data.getEmployeeId() + "|" + data.getDate()), "", ""));
			} else {
				data.addCellData(new DPCellDataDto(COLUMN_SUBMITTED, "", "", ""));
			}
			data.addCellData(new DPCellDataDto(COLUMN_SUBMITTED, "", "", ""));
			data.addCellData(new DPCellDataDto(LOCK_APPLICATION, "", "", ""));
			//set checkbox sign
			data.setSign(signDayMap.containsKey(data.getEmployeeId() + "|" + data.getDate()));
			if(disableSignMap.containsKey(data.getEmployeeId() + "|" + data.getDate()) && disableSignMap.get(data.getEmployeeId() + "|" + data.getDate())){
				result.setLock(data.getId(), LOCK_SIGN, STATE_DISABLE);
			}
			
			//get status check box 
			Map<String, ApproveRootStatusForEmpDto> approvalDayMap =  process.getCheckApproval(approvalStatusAdapter, listEmployeeId, dateRange, sId, mode);
			ApproveRootStatusForEmpDto approveRootStatus =  approvalDayMap.get(data.getEmployeeId() + "|" + data.getDate());
		//	if(mode == ScreenMode.APPROVAL.value){
			data.setApproval(approveRootStatus == null ? false : approveRootStatus.isCheckApproval());
		//	}
			DailyModifyResult resultOfOneRow = process.getRow(resultDailyMap, data.getEmployeeId(), data.getDate());
			Map<String, DatePeriod> employeeAndDateRange = process.extractEmpAndRange(dateRange, companyId, listEmployeeId);
			if (resultOfOneRow != null && (displayFormat == 2 ? !data.getError().equals("") : true)) {
				process.lockDataCheckbox(sId, result, data, identityProcessDtoOpt, approvalUseSettingDtoOpt, approveRootStatus, mode);

				boolean lock = process.checkLockAndSetState(employeeAndDateRange, data);

				if (lock || data.isApproval()) {
					process.lockCell(result, data, true);
					if (data.isApproval()) {
						val typeLock = data.getState();
						if (typeLock.equals(""))
							data.setState("lock|" + LOCK_EDIT_APPROVAL);
						else
							data.setState(typeLock + "|" + LOCK_EDIT_APPROVAL);
						lock = true;
					}
				}
				
				if(data.isSign() && mode == ScreenMode.NORMAL.value){
					process.lockCell(result, data, false);
					lock = true;
				}
				
				itemValueMap = resultOfOneRow.getItems().stream()
						.collect(Collectors.toMap(x -> process.mergeString(String.valueOf(x.getItemId()), "|",
								data.getEmployeeId(), "|", data.getDate().toString()), x -> x));
				DPControlDisplayItem dPControlDisplayItem = new DPControlDisplayItem();
				dPControlDisplayItem.setLstAttendanceItem(param.getLstAttendanceItem());
				process.processCellData(NAME_EMPTY, NAME_NOT_FOUND, result, dPControlDisplayItem, mapDP, mapGetName, codeNameReasonMap,
						itemValueMap,  data, lock, dailyRecEditSetsMap, null);
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

}

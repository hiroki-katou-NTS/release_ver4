package nts.uk.screen.at.app.dailyperformance.correction.lock.button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workinformation.enums.CalculationState;
import nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.enums.DailyAttendanceAtr;
import nts.uk.screen.at.app.dailyperformance.correction.DailyPerformanceCorrectionProcessor;
import nts.uk.screen.at.app.dailyperformance.correction.DailyPerformanceScreenRepo;
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
import nts.uk.screen.at.app.dailyperformance.correction.dto.WorkInfoOfDailyPerformanceDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.checkapproval.ApproveRootStatusForEmpDto;
import nts.uk.screen.at.app.dailyperformance.correction.identitymonth.CheckIndentityMonth;
import nts.uk.screen.at.app.dailyperformance.correction.identitymonth.IndentityMonthParam;
import nts.uk.screen.at.app.dailyperformance.correction.lock.DPLock;
import nts.uk.screen.at.app.dailyperformance.correction.lock.DPLockDto;
import nts.uk.screen.at.app.dailyperformance.correction.text.DPText;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class DPDisplayLockProcessor {

	@Inject
	private DailyPerformanceScreenRepo repo;

	@Inject
	private DailyPerformanceCorrectionProcessor process;

	@Inject
	private DPLock findLock;
	
	@Inject
	private CheckIndentityMonth checkIndentityMonth;

	public DailyPerformanceCorrectionDto processDisplayLock(DPDisplayLockParam param) {
		DailyPerformanceCorrectionDto result = new DailyPerformanceCorrectionDto();
		DateRange dateRange = param.getDateRange();
		Integer mode = param.getMode();
		Integer displayFormat = param.getDisplayFormat();
		String sId = AppContexts.user().employeeId();
		String companyId = AppContexts.user().companyId();
		result.setLstData(param.getLstData());
		result.setLstEmployee(param.getLstEmployee());

		List<String> listEmployeeId = param.getLstData().stream().map(x -> x.getEmployeeId())
				.collect(Collectors.toSet()).stream().collect(Collectors.toList());

		List<DailyRecEditSetDto> dailyRecEditSets = repo.getDailyRecEditSet(listEmployeeId, dateRange);
		Map<String, Integer> dailyRecEditSetsMap = dailyRecEditSets.stream()
				.collect(
						Collectors.toMap(
								x -> process.mergeString(String.valueOf(x.getAttendanceItemId()), "|",
										x.getEmployeeId(), "|", process.converDateToString(x.getProcessingYmd())),
								x -> x.getEditState()));

		List<DPErrorDto> lstError = listEmployeeId.isEmpty() ? new ArrayList<>()
				: repo.getListDPError(dateRange, listEmployeeId);

		Map<Integer, DPAttendanceItem> mapDP = param.getLstAttendanceItem().stream()
				.collect(Collectors.toMap(DPAttendanceItem::getId, x -> x));

		result.markLoginUser(sId);
		result.createModifierCellStateCaseRow(mapDP, param.getLstHeader());

		if (result.getLstEmployee().size() > 0) {
			if (lstError.size() > 0) {
				// Get list error setting
				List<DPErrorSettingDto> lstErrorSetting = this.repo.getErrorSetting(companyId,
						lstError.stream().map(e -> e.getErrorCode()).collect(Collectors.toList()), true, true, false);
				// Seperate Error and Alarm
				if(lstErrorSetting.isEmpty()) {
					lstError = new ArrayList<>();
				}
				result.addErrorToResponseData(lstError, lstErrorSetting, mapDP, false);
			}
		}

		Optional<IdentityProcessUseSetDto> identityProcessDtoOpt = repo.findIdentityProcessUseSet(companyId);
		result.setIdentityProcessDto(identityProcessDtoOpt.isPresent() ? identityProcessDtoOpt.get()
				: new IdentityProcessUseSetDto(false, false, null));
		Optional<ApprovalUseSettingDto> approvalUseSettingDtoOpt = repo.findApprovalUseSettingDto(companyId);

		List<DPDataDto> lstData = new ArrayList<DPDataDto>();
		// get status check box
		DPLockDto dpLock = findLock.checkLockAll(companyId, listEmployeeId, dateRange, sId, mode, identityProcessDtoOpt,
				approvalUseSettingDtoOpt);
		Map<String, Boolean> disableSignMap = new HashMap<>();
		process.getApplication(listEmployeeId, dateRange, disableSignMap);
		List<WorkInfoOfDailyPerformanceDto> workInfoOfDaily = repo.getListWorkInfoOfDailyPerformance(listEmployeeId,
				dateRange);
		
		if (displayFormat == 0) {
			// フレックス情報を表示する
			if (listEmployeeId.get(0).equals(sId)) {
				//checkIndenityMonth
				result.setIndentityMonthResult(checkIndentityMonth.checkIndenityMonth(new IndentityMonthParam(companyId, sId, GeneralDate.today())));
				//対象日の本人確認が済んでいるかチェックする
				result.checkShowTighProcess(displayFormat, true);
			}else {
				result.getIndentityMonthResult().setHideAll(true);
			}
			// screenDto.setFlexShortage(null);
		}
		
		for (DPDataDto data : result.getLstData()) {
			data.resetData();
			if (!sId.equals(data.getEmployeeId())) {
				result.setCellSate(data.getId(), DPText.LOCK_APPLICATION, DPText.STATE_DISABLE);
			}
			// set checkbox sign
			data.setSign(dpLock.getSignDayMap().containsKey(data.getEmployeeId() + "|" + data.getDate()));
			if (disableSignMap.containsKey(data.getEmployeeId() + "|" + data.getDate())
					&& disableSignMap.get(data.getEmployeeId() + "|" + data.getDate())) {
				result.setCellSate(data.getId(), DPText.LOCK_SIGN, DPText.STATE_DISABLE);
			}

			ApproveRootStatusForEmpDto approveRootStatus = dpLock.getLockCheckApprovalDay()
					.get(data.getEmployeeId() + "|" + data.getDate());
			data.setApproval(approveRootStatus == null ? false : approveRootStatus.isCheckApproval());
			ApproveRootStatusForEmpDto approvalCheckMonth = dpLock.getLockCheckMonth()
					.get(data.getEmployeeId() + "|" + data.getDate());

			process.lockDataCheckbox(sId, result, data, identityProcessDtoOpt, approvalUseSettingDtoOpt,
					approveRootStatus, mode, data.isApproval(), data.isSign());
			boolean lockDaykWpl = false, lockHist = false, lockApprovalMonth = false, lockConfirmMonth = false;
			if (param.isShowLock()) {
				lockDaykWpl = process.checkLockAndSetState(dpLock.getLockDayAndWpl(), data);
				lockHist = process.lockHist(dpLock.getLockHist(), data);
				lockApprovalMonth = approvalCheckMonth == null ? false : approveRootStatus.isCheckApproval();
				lockConfirmMonth = process.checkLockConfirmMonth(dpLock.getLockConfirmMonth(), data);
				lockDaykWpl = process.lockAndDisable(result, data, mode, lockDaykWpl, data.isApproval(), lockHist,
						data.isSign(), lockApprovalMonth, lockConfirmMonth);
			} else {
				lockDaykWpl = process.lockAndDisable(result, data, mode, lockDaykWpl, false, lockHist,
						false, lockApprovalMonth, lockConfirmMonth);
			}

			DPControlDisplayItem dPControlDisplayItem = new DPControlDisplayItem();
			dPControlDisplayItem.setLstAttendanceItem(param.getLstAttendanceItem());
			processCellData(result, dPControlDisplayItem, data, lockDaykWpl, dailyRecEditSetsMap);
			lstData.add(data);
			Optional<WorkInfoOfDailyPerformanceDto> optWorkInfoOfDailyPerformanceDto = workInfoOfDaily.stream()
					.filter(w -> w.getEmployeeId().equals(data.getEmployeeId()) && w.getYmd().equals(data.getDate()))
					.findFirst();
			if (optWorkInfoOfDailyPerformanceDto.isPresent()
					&& optWorkInfoOfDailyPerformanceDto.get().getState() == CalculationState.No_Calculated)
				result.setAlarmCellForFixedColumn(data.getId(), displayFormat);

		}

		result.setLstData(lstData);
		return result;
	}
	
	public void processCellData(DailyPerformanceCorrectionDto screenDto,
			DPControlDisplayItem dPControlDisplayItem, DPDataDto data,
			boolean lock, Map<String, Integer> dailyRecEditSetsMap) {
		Set<DPCellDataDto> cellDatas = data.getCellDatas();
		String typeGroup = "";
		Integer cellEdit;
		if (dPControlDisplayItem.getLstAttendanceItem() != null) {
			for (DPAttendanceItem item : dPControlDisplayItem.getLstAttendanceItem()) {
				//DPAttendanceItem dpAttenItem1 = mapDP.get(item.getId());
				String itemIdAsString = item.getId().toString();
				// int a = 1;
				int attendanceAtr = item.getAttendanceAtr();
				Integer groupType = item.getTypeGroup();
				cellEdit = dailyRecEditSetsMap.get(process.mergeString(itemIdAsString, "|", data.getEmployeeId(), "|" + process.converDateToString(data.getDate())));
				
				if (attendanceAtr == DailyAttendanceAtr.Code.value
						|| attendanceAtr == DailyAttendanceAtr.Classification.value) {
					String nameColKey = process.mergeString(DPText.NAME, itemIdAsString);
					if (attendanceAtr == DailyAttendanceAtr.Code.value) {
						String codeColKey = process.mergeString(DPText.CODE, itemIdAsString);
						typeGroup = typeGroup
								+ process.mergeString(String.valueOf(item.getId()), ":", String.valueOf(groupType), "|");
						if (lock) {
							screenDto.setCellSate(data.getId(), codeColKey, DPText.STATE_DISABLE);
							screenDto.setCellSate(data.getId(), nameColKey, DPText.STATE_DISABLE);
						}
						process.cellEditColor(screenDto, data.getId(), nameColKey, cellEdit);
						process.cellEditColor(screenDto, data.getId(), codeColKey, cellEdit);
					} else {
						String noColKey = process.mergeString(DPText.NO, itemIdAsString);
						if (lock) {
							screenDto.setCellSate(data.getId(), noColKey, DPText.STATE_DISABLE);
							screenDto.setCellSate(data.getId(), nameColKey, DPText.STATE_DISABLE);
						}
						process.cellEditColor(screenDto, data.getId(), nameColKey, cellEdit);
						process.cellEditColor(screenDto, data.getId(), noColKey, cellEdit);
					}

				} else {
					String anyChar = process.mergeString(DPText.ADD_CHARACTER, itemIdAsString);
					// set color edit
					process.cellEditColor(screenDto, data.getId(), anyChar, cellEdit);
					if (lock) {
						screenDto.setCellSate(data.getId(), anyChar, DPText.STATE_DISABLE);
					}
				}
			}
		}
		data.setTypeGroup(typeGroup);
		data.setCellDatas(cellDatas);
	}
}

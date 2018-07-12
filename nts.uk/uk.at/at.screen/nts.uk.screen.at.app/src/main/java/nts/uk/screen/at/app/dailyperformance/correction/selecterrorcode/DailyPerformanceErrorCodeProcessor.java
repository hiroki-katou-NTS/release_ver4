package nts.uk.screen.at.app.dailyperformance.correction.selecterrorcode;

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

import nts.arc.error.BusinessException;
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
import nts.uk.screen.at.app.dailyperformance.correction.dto.AffEmploymentHistoryDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.ApprovalUseSettingDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.ColumnSetting;
import nts.uk.screen.at.app.dailyperformance.correction.dto.CorrectionOfDailyPerformance;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPAttendanceItem;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPCellDataDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPControlDisplayItem;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPDataDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPErrorDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPErrorSettingDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DailyPerformanceCorrectionDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DailyPerformanceEmployeeDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DailyRecEditSetDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DateRange;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DisplayItem;
import nts.uk.screen.at.app.dailyperformance.correction.dto.IdentityProcessUseSetDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.OperationOfDailyPerformanceDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.WorkInfoOfDailyPerformanceDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.checkapproval.ApproveRootStatusForEmpDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.checkshowbutton.DailyPerformanceAuthorityDto;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class DailyPerformanceErrorCodeProcessor {

	@Inject
	private DailyPerformanceScreenRepo repo;

	@Inject
	private DailyModifyQueryProcessor dailyModifyQueryProcessor;

	@Inject
	private DataDialogWithTypeProcessor dataDialogWithTypeProcessor;

	@Inject
	private ApplicationListForScreen applicationListFinder;

	@Inject
	private DailyPerformanceCorrectionProcessor dailyProcessor;
	
	@Inject
	private ApprovalStatusAdapter approvalStatusAdapter;

	private static final String LOCK_APPLICATION = "Application";
	private static final String COLUMN_SUBMITTED = "Submitted";
	public static final int MINUTES_OF_DAY = 24 * 60;
	private static final String STATE_DISABLE = "ntsgrid-disable";

	public DailyPerformanceCorrectionDto generateData(DateRange dateRange,
			List<DailyPerformanceEmployeeDto> lstEmployee, Integer initScreen, Integer mode, Integer displayFormat,
			CorrectionOfDailyPerformance correct, List<String> errorCodes, List<String> formatCodes) {
		String sId = AppContexts.user().employeeId();
		String companyId = AppContexts.user().companyId();
		DailyPerformanceCorrectionDto screenDto = new DailyPerformanceCorrectionDto();
		String NAME_EMPTY = TextResource.localize("KDW003_82");
		String NAME_NOT_FOUND = TextResource.localize("KDW003_81");
		// identityProcessDto show button A2_6
		// アルゴリズム「本人確認処理の利用設定を取得する」を実行する
		Optional<IdentityProcessUseSetDto> identityProcessDtoOpt = repo.findIdentityProcessUseSet(companyId);
		screenDto.setIdentityProcessDto(identityProcessDtoOpt.isPresent() ? identityProcessDtoOpt.get()
				: new IdentityProcessUseSetDto(false, false, null));
		Optional<ApprovalUseSettingDto> approvalUseSettingDtoOpt = repo.findApprovalUseSettingDto(companyId);
		dailyProcessor.setHideCheckbok(screenDto, identityProcessDtoOpt, approvalUseSettingDtoOpt, companyId, mode);

		/**
		 * システム日付を基準に1ヵ月前の期間を設定する | Set date range one month before system date
		 */
		screenDto.setDateRange(dateRange);

		/** 画面制御に関する情報を取得する | Acquire information on screen control */
		// アルゴリズム「社員の日別実績の権限をすべて取得する」を実行する | Execute "Acquire all permissions of
		// employee's daily performance"--
		String roleId = AppContexts.user().roles().forAttendance();
		List<DailyPerformanceAuthorityDto> dailyPerformans = new ArrayList<>();
		if (roleId != null) {
			dailyPerformans = repo.findDailyAuthority(roleId);
		}
		if (dailyPerformans.isEmpty()) {
			throw new BusinessException("Msg_671");
		} else {
			// NO.15
			screenDto.setAuthorityDto(dailyPerformans);
		}

		// get employmentCode
		AffEmploymentHistoryDto employment = repo.getAffEmploymentHistory(sId, dateRange);
		screenDto.setEmploymentCode(employment == null ? "" : employment.getEmploymentCode());
		List<String> listEmployeeId = lstEmployee.stream().map(e -> e.getId()).collect(Collectors.toList());
		List<DPErrorDto> lstError = this.repo.getListDPError(screenDto.getDateRange(), listEmployeeId, errorCodes);
		Map<String, String> mapIdError = new HashMap<>();
		for (DPErrorDto dto : lstError) {
			mapIdError.put(dto.getEmployeeId(), "");
		}
		//lstEmployee = lstEmployee.stream().filter(x -> mapIdError.get(x.getId()) != null).collect(Collectors.toList());
		if (lstEmployee.isEmpty()) {
			throw new BusinessException("Msg_672");
		}
		screenDto.setLstEmployee(lstEmployee);
		// 表示形式をチェックする | Check display format => UI
		// Create lstData: Get by listEmployee & listDate
		// 日付別の情報を取得する + 個人別の情報を取得する + エラーアラームの情報を取得する | Acquire information by
		// date + Acquire personalized information + Acquire error alarm
		// information
		screenDto.setLstData(dailyProcessor.getListData(screenDto.getLstEmployee(), dateRange, displayFormat));
		// Lấy thành tích nhân viên theo ngày
		/// アルゴリズム「対象日に対応する社員の実績の編集状態を取得する」を実行する | Execute "Acquire edit status
		/// of employee's record corresponding to target date"| lay ve trang
		/// thai sua cua thanh tich nhan vien tuong ung
		List<DailyRecEditSetDto> dailyRecEditSets = repo.getDailyRecEditSet(listEmployeeId, dateRange);
		Map<String, Integer> dailyRecEditSetsMap = dailyRecEditSets.stream()
				.collect(Collectors.toMap(x -> dailyProcessor.mergeString(String.valueOf(x.getAttendanceItemId()), "|",
						x.getEmployeeId(), "|", dailyProcessor.converDateToString(x.getProcessingYmd())),
						x -> x.getEditState()));
		Map<String, DatePeriod> employeeAndDateRange = dailyProcessor.extractEmpAndRange(dateRange, companyId,
				listEmployeeId);
		// No 19, 20 show/hide button
		boolean showButton = true;
		if (displayFormat == 0) {
			if (!listEmployeeId.isEmpty() && !sId.equals(listEmployeeId.get(0))) {
				showButton = false;
			}
		}
		OperationOfDailyPerformanceDto dailyPerformanceDto = repo.findOperationOfDailyPerformance();
		DisplayItem disItem = dailyProcessor.getDisplayItems(correct, formatCodes, companyId, screenDto, listEmployeeId,
				showButton, dailyPerformanceDto);
		List<DailyModifyResult> results = new ArrayList<>();
		results = new GetDataDaily(listEmployeeId, dateRange, disItem.getLstAtdItemUnique(), dailyModifyQueryProcessor)
				.call();
		DPControlDisplayItem dPControlDisplayItem = dailyProcessor.getItemIdNames(disItem, showButton);
		/// アルゴリズム「対象日に対応する社員の実績の編集状態を取得する」を実行する | Execute "Acquire edit status
		/// of employee's record corresponding to target date"| lay ve trang
		/// アルゴリズム「実績エラーをすべて取得する」を実行する | Execute "Acquire all actual errors"
		Map<Integer, DPAttendanceItem> mapDP =  dPControlDisplayItem.getMapDPAttendance();
		if (screenDto.getLstEmployee().size() > 0) {
			/// ドメインモデル「社員の日別実績エラー一覧」をすべて取得する +
			/// 対応するドメインモデル「勤務実績のエラーアラーム」をすべて取得する
			/// Acquire all domain model "employee's daily performance error
			/// list" + "work error error alarm" | lay loi thanh tich trong
			/// khoang thoi gian
			if (lstError.size() > 0) {
				// Get list error setting
				List<DPErrorSettingDto> lstErrorSetting = this.repo
						.getErrorSetting(companyId, lstError.stream().map(e -> e.getErrorCode()).collect(Collectors.toList()));
				// Seperate Error and Alarm
				screenDto.addErrorToResponseData(lstError, lstErrorSetting, mapDP);
			}
		}
		screenDto.setLstControlDisplayItem(dPControlDisplayItem);
		screenDto.getItemValues().addAll(results.isEmpty() ? new ArrayList<>() : results.get(0).getItems());
		Map<String, DailyModifyResult> resultDailyMap = results.stream()
				.collect(Collectors.toMap(
						x -> dailyProcessor.mergeString(x.getEmployeeId(), "|", x.getDate().toString()),
						Function.identity(), (x, y) -> x));
		//// 11. Excel: 未計算のアラームがある場合は日付又は名前に表示する
		List<WorkInfoOfDailyPerformanceDto> workInfoOfDaily = repo.getListWorkInfoOfDailyPerformance(listEmployeeId,
				dateRange);
		List<DPDataDto> lstData = new ArrayList<DPDataDto>();
		// set error, alarm
		if (screenDto.getLstEmployee().size() > 0) {
			if (lstError.size() > 0) {
				// Get list error setting
				List<DPErrorSettingDto> lstErrorSetting = this.repo
						.getErrorSetting(companyId, lstError.stream().map(e -> e.getErrorCode()).collect(Collectors.toList()));
				// Seperate Error and Alarm
				screenDto.addErrorToResponseData(lstError, lstErrorSetting, mapDP);
			}
		}

		Set<Integer> types = dPControlDisplayItem.getLstAttendanceItem() == null ? new HashSet<>()
				: dPControlDisplayItem.getLstAttendanceItem().stream().map(x -> x.getTypeGroup()).filter(x -> x != null)
						.collect(Collectors.toSet());
		Map<Integer, Map<String, String>> mapGetName = dataDialogWithTypeProcessor
				.getAllCodeName(new ArrayList<>(types), companyId);
		CodeNameType codeNameReason = dataDialogWithTypeProcessor.getReason(companyId);
		Map<String, CodeName> codeNameReasonMap = codeNameReason != null
				? codeNameReason.getCodeNames().stream()
						.collect(Collectors.toMap(x -> dailyProcessor.mergeString(x.getCode(), "|", x.getId()), x -> x))
				: Collections.emptyMap();
		// No 20 get submitted application
		List<ApplicationExportDto> appplication = listEmployeeId.isEmpty() ? Collections.emptyList()
				: applicationListFinder.getApplicationBySID(listEmployeeId, dateRange.getStartDate(),
						dateRange.getEndDate());
		Map<String, String> appMapDateSid = new HashMap<>();
		appplication.forEach(x -> {
			String key = x.getEmployeeID() + "|" + x.getAppDate();
			if (appMapDateSid.containsKey(key)) {
				appMapDateSid.put(key, appMapDateSid.get(key) + "  " + x.getAppTypeName());
			} else {
				appMapDateSid.put(key, x.getAppTypeName());
			}
		});
		// get check box sign(Confirm day)
		Map<String, Boolean> signDayMap = repo.getConfirmDay(companyId, listEmployeeId, dateRange);
		screenDto.markLoginUser(sId);
		screenDto.createAccessModifierCellState(mapDP);
		screenDto.getLstFixedHeader().forEach(column -> {
			screenDto.getLstControlDisplayItem().getColumnSettings().add(new ColumnSetting(column.getKey(), false));
		});
		
		// set cell data
		Map<String, ItemValue> itemValueMap = new HashMap<>();
		for (DPDataDto data : screenDto.getLstData()) {
			data.setEmploymentCode(screenDto.getEmploymentCode());
			if (!sId.equals(data.getEmployeeId())) {
				screenDto.setLock(data.getId(), LOCK_APPLICATION, STATE_DISABLE);
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
			// set checkbox sign
			data.setSign(signDayMap.containsKey(data.getEmployeeId() + "|" + data.getDate()));
			Map<String, ApproveRootStatusForEmpDto> approvalDayMap =  dailyProcessor.getCheckApproval(approvalStatusAdapter, listEmployeeId, dateRange, sId, mode);
			ApproveRootStatusForEmpDto approveRootStatus =  approvalDayMap.get(data.getEmployeeId() + "|" + data.getDate());
		//	if(mode == ScreenMode.APPROVAL.value){
			data.setApproval(approveRootStatus == null ? false : approveRootStatus.isCheckApproval());
			DailyModifyResult resultOfOneRow = dailyProcessor.getRow(resultDailyMap, data.getEmployeeId(),
					data.getDate());
			if (resultOfOneRow != null && !data.getError().equals("")) {
				dailyProcessor.lockDataCheckbox(sId, screenDto, data, identityProcessDtoOpt, approvalUseSettingDtoOpt, approveRootStatus, mode);
				boolean lock = dailyProcessor.checkLockAndSetState(employeeAndDateRange, data);
				if (lock) {
					dailyProcessor.lockCell(screenDto, data, true);
				}
				if (resultOfOneRow != null) {
					itemValueMap = resultOfOneRow.getItems().stream()
							.collect(Collectors.toMap(x -> dailyProcessor.mergeString(String.valueOf(x.getItemId()),
									"|", data.getEmployeeId(), "|", data.getDate().toString()), x -> x));
				}
				dailyProcessor.processCellData(NAME_EMPTY, NAME_NOT_FOUND, screenDto, dPControlDisplayItem,
						mapGetName, codeNameReasonMap, itemValueMap, data, lock, dailyRecEditSetsMap, null);
				lstData.add(data);
				Optional<WorkInfoOfDailyPerformanceDto> optWorkInfoOfDailyPerformanceDto = workInfoOfDaily.stream()
						.filter(w -> w.getEmployeeId().equals(data.getEmployeeId())
								&& w.getYmd().equals(data.getDate()))
						.findFirst();
				if (optWorkInfoOfDailyPerformanceDto.isPresent()
						&& optWorkInfoOfDailyPerformanceDto.get().getState() == CalculationState.No_Calculated)
					screenDto.setAlarmCellForFixedColumn(data.getId(), displayFormat);
			}
		}
		screenDto.setLstData(lstData);
		return screenDto;
	}

}

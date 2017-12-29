package nts.uk.screen.at.app.dailyperformance.correction.selecterrorcode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.workinformation.enums.CalculationState;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.SettingUnit;
import nts.uk.ctx.at.shared.app.util.attendanceitem.type.ItemValue;
import nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.adapter.DailyAttendanceItemNameAdapter;
import nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.adapter.DailyAttendanceItemNameAdapterDto;
import nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.enums.DailyAttendanceAtr;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.screen.at.app.dailymodify.query.DailyModifyQueryProcessor;
import nts.uk.screen.at.app.dailyperformance.correction.DailyPerformanceScreenRepo;
import nts.uk.screen.at.app.dailyperformance.correction.dto.ActualLockDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.AffEmploymentHistoryDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.AuthorityFomatDailyDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.AuthorityFormatInitialDisplayDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.AuthorityFormatSheetDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.ClosureDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.ColumnSetting;
import nts.uk.screen.at.app.dailyperformance.correction.dto.CorrectionOfDailyPerformance;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPAttendanceItem;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPAttendanceItemControl;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPBusinessTypeControl;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPCellDataDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPControlDisplayItem;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPDataDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPErrorDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPErrorSettingDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPHeaderDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPSheetDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DailyPerformanceCorrectionDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DailyPerformanceEmployeeDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DateRange;
import nts.uk.screen.at.app.dailyperformance.correction.dto.FormatDPCorrectionDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.OperationOfDailyPerformanceDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.WorkFixedDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.WorkInfoOfDailyPerformanceDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.checkshowbutton.DailyPerformanceAuthorityDto;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class DailyPerformanceErrorCodeProcessor {

	@Inject
	private DailyPerformanceScreenRepo repo;
	
	@Inject
	private ClosureService closureService;

	@Inject
	private DailyModifyQueryProcessor dailyModifyQueryProcessor;
	
	@Inject
	private DailyAttendanceItemNameAdapter dailyAttendanceItemNameAdapter;

	public DailyPerformanceCorrectionDto generateData(DateRange dateRange,
			List<DailyPerformanceEmployeeDto> lstEmployee, int displayFormat, CorrectionOfDailyPerformance correct,
			List<String> errorCodes, List<String> formatCodes) {
		String sId = AppContexts.user().employeeId();
		DailyPerformanceCorrectionDto screenDto = new DailyPerformanceCorrectionDto();

		/**
		 * システム日付を基準に1ヵ月前の期間を設定する | Set date range one month before system date
		 */
		screenDto.setDateRange(dateRange);

		/** 画面制御に関する情報を取得する | Acquire information on screen control */
		// アルゴリズム「社員の日別実績の権限をすべて取得する」を実行する | Execute "Acquire all permissions of
		// employee's daily performance"--
		// roleId = AppContexts.user().roles().forPersonalInfo() fixed
		List<DailyPerformanceAuthorityDto> dailyPerformans = repo
				.findDailyAuthority("00000000-0000-0000-0000-000000000001");
		if (dailyPerformans.isEmpty()) {
			throw new BusinessException("Msg_671");
		} else {
			// NO.15
			screenDto.setAuthorityDto(dailyPerformans);
		}

		// get employmentCode
		AffEmploymentHistoryDto employment = repo.getAffEmploymentHistory(sId, dateRange);
		screenDto.setEmploymentCode(employment == null ? "" : employment.getEmploymentCode());
		// アルゴリズム「休暇の管理状況をチェックする」を実行する | Get holiday setting data
		getHolidaySettingData(screenDto);

		/**
		 * アルゴリズム「表示形式に従って情報を取得する」を実行する | Execute "Get information according to
		 * display format"
		 */
		// アルゴリズム「対象者を抽出する」を実行する | Execute "Extract subject"

		List<String> listEmployeeId = lstEmployee.stream().map(e -> e.getId()).collect(Collectors.toList());
		List<DPErrorDto> lstError = this.repo.getListDPError(screenDto.getDateRange(), listEmployeeId, errorCodes);
		Map<String, String> mapIdError = new HashMap<>();
		for (DPErrorDto dto : lstError) {
			mapIdError.put(dto.getEmployeeId(), "");
		}
		lstEmployee = lstEmployee.stream().filter(x -> mapIdError.get(x.getId()) != null).collect(Collectors.toList());
		if (lstEmployee.isEmpty()) {
			throw new BusinessException("Msg_672");
		}
		screenDto.setLstEmployee(lstEmployee);
		// 表示形式をチェックする | Check display format => UI
		// Create lstData: Get by listEmployee & listDate
		// 日付別の情報を取得する + 個人別の情報を取得する + エラーアラームの情報を取得する | Acquire information by
		// date + Acquire personalized information + Acquire error alarm
		// information
		screenDto.setLstData(getListData(screenDto.getLstEmployee(), dateRange));
		/// 対応する「日別実績」をすべて取得する-- lay tat ca thanh tich theo ngay tuong ung
		//// 日別実績の勤務情報
//		List<DailyModifyResult> results = dailyModifyQueryProcessor
//				.initScreens(new DailyModifyQuery(listEmployeeId, dateRange.toListDate()));
//		Map<String, DailyModifyResult> resultDailyMap = results.stream()
//				.collect(Collectors.toMap((x) -> x.getEmployId() + "|" + x.getDate(), Function.identity()));
		/// アルゴリズム「対象日に対応する社員の実績の編集状態を取得する」を実行する | Execute "Acquire edit status
		/// of employee's record corresponding to target date"| lay ve trang
		/// thai sua cua thanh tich nhan vien tuong ung
		// --List<DailyRecEditSetDto> dailyRecEditSets =
		/// repo.getDailyRecEditSet(listEmployeeId, dateRange);
		/// アルゴリズム「実績エラーをすべて取得する」を実行する | Execute "Acquire all actual errors"
		if (screenDto.getLstEmployee().size() > 0) {
			/// ドメインモデル「社員の日別実績エラー一覧」をすべて取得する +
			/// 対応するドメインモデル「勤務実績のエラーアラーム」をすべて取得する
			/// Acquire all domain model "employee's daily performance error
			/// list" + "work error error alarm" | lay loi thanh tich trong
			/// khoang thoi gian
			if (lstError.size() > 0) {
				// Get list error setting
				List<DPErrorSettingDto> lstErrorSetting = this.repo
						.getErrorSetting(lstError.stream().map(e -> e.getErrorCode()).collect(Collectors.toList()));
				// Seperate Error and Alarm
				screenDto.addErrorToResponseData(lstError, lstErrorSetting);
			}
		}

		// アルゴリズム「社員に対応する処理締めを取得する」を実行する | Execute "Acquire Process Tightening
		// Corresponding to Employees"--
		List<ClosureDto> closureDtos = repo.getClosureId(listEmployeeId, dateRange.getEndDate());
		/// TODO : アルゴリズム「対象日に対応する承認者確認情報を取得する」を実行する | Execute "Acquire Approver
		/// Confirmation Information Corresponding to Target Date"
		// アルゴリズム「就業確定情報を取得する」を実行する
		/// アルゴリズム「日別実績のロックを取得する」を実行する (Tiến hành xử lý "Lấy về lock của thành
		/// tích theo ngày")
		Map<String, DatePeriod> employeeAndDateRange = new HashMap<>();
		if (!closureDtos.isEmpty()) {
			closureDtos.forEach(x -> {
				DatePeriod datePeriod = closureService.getClosurePeriod(x.getClosureId(),
						new YearMonth(x.getClosureMonth()));
				Optional<ActualLockDto> actualLockDto = repo.findAutualLockById(AppContexts.user().companyId(),
						x.getClosureId());
				if (actualLockDto.isPresent()) {
					if (actualLockDto.get().getDailyLockState() == 1) {
						employeeAndDateRange.put(x.getSid() + "|" + x.getClosureId() + "|" + "D", datePeriod);
					}
					;
					if (actualLockDto.get().getMonthlyLockState() == 1) {
						employeeAndDateRange.put(x.getSid() + "|" + x.getClosureId() + "|" + "M", datePeriod);
					}
				}
				// アルゴリズム「表示項目を制御する」を実行する | Execute "control display items"
				Optional<WorkFixedDto> workFixedOp = repo.findWorkFixed(x.getClosureId(), x.getClosureMonth());
				if (workFixedOp.isPresent()) {
					employeeAndDateRange.put(
							x.getSid() + "|" + x.getClosureId() + "|" + workFixedOp.get().getWkpId() + "|" + "C",
							datePeriod);
				}
			});
		}

		OperationOfDailyPerformanceDto dailyPerformanceDto = repo.findOperationOfDailyPerformance();
		screenDto.setComment(dailyPerformanceDto != null && dailyPerformanceDto.getComment() != null
				? dailyPerformanceDto.getComment() : null);
		DPControlDisplayItem dPControlDisplayItem = getControlDisplayItems(listEmployeeId, screenDto.getDateRange(),
				correct, formatCodes, dailyPerformanceDto);
		screenDto.setLstControlDisplayItem(dPControlDisplayItem);
		screenDto.getLstFixedHeader().forEach(column -> {
			screenDto.getLstControlDisplayItem().getColumnSettings().add(new ColumnSetting(column.getKey(), false));
		});
		//// 11. Excel: 未計算のアラームがある場合は日付又は名前に表示する
		// Map<Integer, Integer> typeControl =
		//// lstAttendanceItem.stream().collect(Collectors.toMap(DPAttendanceItem::
		//// getId, DPAttendanceItem::getAttendanceAtr));
		List<WorkInfoOfDailyPerformanceDto> workInfoOfDaily = repo.getListWorkInfoOfDailyPerformance(listEmployeeId,
				dateRange);
		List<DPDataDto> lstData = new ArrayList<DPDataDto>();
		Map<Integer, DPAttendanceItem> mapDP = dPControlDisplayItem.getLstAttendanceItem() != null
				? dPControlDisplayItem.getLstAttendanceItem().stream()
						.collect(Collectors.toMap(DPAttendanceItem::getId, x -> x))
				: new HashMap<>();
		Map<String, ItemValue> itemValueMap = new HashMap<>();
		for (DPDataDto data : screenDto.getLstData()) {
			boolean lock = false;
			if (!employeeAndDateRange.isEmpty()) {
				for (int i = 1; i <= 5; i++) {
					DatePeriod dateD = employeeAndDateRange.get(data.getEmployeeId() + "|" + i + "|" + "D");
					DatePeriod dateM = employeeAndDateRange.get(data.getEmployeeId() + "|" + i + "|" + "M");
					DatePeriod dateC = employeeAndDateRange
							.get(data.getEmployeeId() + "|" + i + "|" + data.getWorkplaceId() + "|" + "C");
					String lockD = "";
					String lockM = "";
					String lockC = "";
					if ((dateD != null && (data.getDate().afterOrEquals(dateD.start())
							&& data.getDate().beforeOrEquals(dateD.end())))) {
						lockD = "|" + "D";
					}
					if ((dateM != null && (data.getDate().afterOrEquals(dateM.start())
							&& data.getDate().beforeOrEquals(dateM.end())))) {
						lockM = "|" + "M";
					}
					if ((dateC != null && (data.getDate().afterOrEquals(dateC.start())
							&& data.getDate().beforeOrEquals(dateC.end())))) {
						lockC = "|" + "C";
					}
					if (!lockD.equals("") || !lockM.equals("") || !lockC.equals("")) {
						data.setState("lock" + lockD + lockM + lockC);
						lock = true;
					}
				}
			}
			if (lock) {
				screenDto.setLock(data.getId(), "date");
				screenDto.setLock(data.getId(), "employeeCode");
				screenDto.setLock(data.getId(), "employeeName");
				screenDto.setLock(data.getId(), "error");
				screenDto.setLock(data.getId(), "sign");
				screenDto.setLock(data.getId(), "picture-person");

			}
//			DailyModifyResult resultOfOneRow = resultDailyMap.isEmpty() ? null
//					: resultDailyMap.get(data.getEmployeeId() + "|" + data.getDate());
//			if (resultOfOneRow != null) {
//				List<ItemValue> attendanceTimes = resultOfOneRow.getItems().get("AttendanceTimeOfDailyPerformance");
//				itemValueMap = attendanceTimes.isEmpty() ? Collections.emptyMap()
//						: attendanceTimes.stream()
//								.collect(Collectors.toMap(
//										x -> x.getItemId() + "|" + data.getEmployeeId() + "|" + data.getDate(), x -> x,
//										(x, q) -> x));
//			}
			List<DPCellDataDto> cellDatas = new ArrayList<>();
			if (dPControlDisplayItem.getLstAttendanceItem() != null) {
				for (DPAttendanceItem item : dPControlDisplayItem.getLstAttendanceItem()) {
					Random rn = new Random();
					int a = 1;
					int attendanceAtr = mapDP.get(item.getId()).getAttendanceAtr();
					String key = item.getId() + "|" + data.getEmployeeId() + "|" + data.getDate();
					String value = itemValueMap.get(key) == null ? "" : itemValueMap.get(key).value().toString();
					if (attendanceAtr == DailyAttendanceAtr.Code.value
							|| attendanceAtr == DailyAttendanceAtr.Classification.value) {
						if (attendanceAtr == DailyAttendanceAtr.Code.value) {
							if (lock) {
								screenDto.setLock(data.getId(), "Code" + String.valueOf(item.getId()));
								screenDto.setLock(data.getId(), "Name" + String.valueOf(item.getId()));
							}
							cellDatas.add(new DPCellDataDto("Code" + String.valueOf(item.getId()),
									value != null ? value : String.valueOf(a), String.valueOf(item.getAttendanceAtr()),
									"label"));
							cellDatas.add(new DPCellDataDto("Name" + String.valueOf(item.getId()),
									"Link Name" + item.getId(), String.valueOf(item.getAttendanceAtr()), "Link2"));
						} else {
							if (lock) {
								screenDto.setLock(data.getId(), "NO" + String.valueOf(item.getId()));
								screenDto.setLock(data.getId(), "Name" + String.valueOf(item.getId()));
							}
							cellDatas.add(new DPCellDataDto("NO" + String.valueOf(item.getId()),
									value != null ? value : String.valueOf(a), String.valueOf(item.getAttendanceAtr()),
									"label"));
							cellDatas.add(new DPCellDataDto("Name" + String.valueOf(item.getId()), String.valueOf(a),
									String.valueOf(item.getAttendanceAtr()), "Link2"));
						}

					} else {
						if (lock) {
							screenDto.setLock(data.getId(), "A" + String.valueOf(item.getId()));
						}
						cellDatas.add(new DPCellDataDto("A" + String.valueOf(item.getId()),
								value != null ? value : String.valueOf(a), String.valueOf(item.getAttendanceAtr()),
								"label"));
					}
				}
				;
			}
			data.setCellDatas(cellDatas);
			lstData.add(data);
			// DPCellDataDto bPCellDataDto = new DPCellDataDto(columnKey, value,
			// dataType, type);
			Optional<WorkInfoOfDailyPerformanceDto> optWorkInfoOfDailyPerformanceDto = workInfoOfDaily.stream()
					.filter(w -> w.getEmployeeId().equals(data.getEmployeeId()) && w.getYmd().equals(data.getDate()))
					.findFirst();
			if (optWorkInfoOfDailyPerformanceDto.isPresent()
					&& optWorkInfoOfDailyPerformanceDto.get().getState() == CalculationState.No_Calculated)
				screenDto.setAlarmCellForFixedColumn(data.getId());
		}
		// screenDto.setLstData(lstData);
		screenDto.markLoginUser();
		screenDto.createAccessModifierCellState(mapDP);
		return screenDto;
	}

	/** アルゴリズム「休暇の管理状況をチェックする」を実行する */
	private void getHolidaySettingData(DailyPerformanceCorrectionDto dailyPerformanceCorrectionDto) {
		// アルゴリズム「年休設定を取得する」を実行する
		dailyPerformanceCorrectionDto.setYearHolidaySettingDto(this.repo.getYearHolidaySetting());
		// アルゴリズム「振休管理設定を取得する」を実行する
		dailyPerformanceCorrectionDto.setSubstVacationDto(this.repo.getSubstVacationDto());
		// アルゴリズム「代休管理設定を取得する」を実行する
		dailyPerformanceCorrectionDto.setCompensLeaveComDto(this.repo.getCompensLeaveComDto());
		// アルゴリズム「60H超休管理設定を取得する」を実行する
		dailyPerformanceCorrectionDto.setCom60HVacationDto(this.repo.getCom60HVacationDto());
	}

	/**
	 * Get List Data include:<br/>
	 * Employee and Date
	 **/
	private List<DPDataDto> getListData(List<DailyPerformanceEmployeeDto> listEmployee, DateRange dateRange) {
		List<DPDataDto> result = new ArrayList<>();
		if (listEmployee.size() > 0) {
			List<GeneralDate> lstDate = dateRange.toListDate();
			int dataId = 0;
			for (int j = 0; j < listEmployee.size(); j++) {
				DailyPerformanceEmployeeDto employee = listEmployee.get(j);
				for (int i = 0; i < lstDate.size(); i++) {
					GeneralDate filterDate = lstDate.get(i);
					result.add(new DPDataDto(dataId, "", "", filterDate, false, employee.getId(), employee.getCode(),
							employee.getBusinessName(), employee.getWorkplaceId()));
					dataId++;
				}
			}
		}
		return result;
	}

	/**
	 * アルゴリズム「表示項目を制御する」を実行する | Execute the algorithm "control display items"
	 */
	private DPControlDisplayItem getControlDisplayItems(List<String> lstEmployeeId, DateRange dateRange,
			CorrectionOfDailyPerformance correct, List<String> formatCodeSelects,
			OperationOfDailyPerformanceDto dailyPerformanceDto) {
		DPControlDisplayItem result = new DPControlDisplayItem();
		String companyId = AppContexts.user().companyId();
		if (lstEmployeeId.size() > 0) {
			// 対応するドメインモデル「日別実績の運用」を取得する | Acquire corresponding domain model
			// "Operation of daily performance"
			// 取得したドメインモデル「日別実績の運用」をチェックする | Check the acquired domain model
			// "Operation of daily performance"
			List<FormatDPCorrectionDto> lstFormat = new ArrayList<FormatDPCorrectionDto>();
			List<DPSheetDto> lstSheet = new ArrayList<DPSheetDto>();
			List<Integer> lstAtdItem = new ArrayList<>();
			List<Integer> lstAtdItemUnique = new ArrayList<>();
			List<DPAttendanceItem> lstAttendanceItem = new ArrayList<>();
			if (dailyPerformanceDto != null && dailyPerformanceDto.getSettingUnit() == SettingUnit.AUTHORITY) {
				List<AuthorityFomatDailyDto> authorityFomatDailys = new ArrayList<>();
				List<AuthorityFormatSheetDto> authorityFormatSheets = new ArrayList<>();
				// アルゴリズム「社員の権限に対応する表示項目を取得する」を実行する
				// kiem tra thong tin rieng biet user
				if (correct == null) {
					if (formatCodeSelects.isEmpty()) {
						List<AuthorityFormatInitialDisplayDto> initialDisplayDtos = repo
								.findAuthorityFormatInitialDisplay(companyId);
						if (!initialDisplayDtos.isEmpty()) {
							List<String> formatCodes = initialDisplayDtos.stream()
									.map(x -> x.getDailyPerformanceFormatCode()).collect(Collectors.toList());
							// Lấy về domain model "会社の日別実績の修正のフォーマット" tương ứng
							authorityFomatDailys = repo.findAuthorityFomatDaily(companyId, formatCodes);
							List<BigDecimal> sheetNos = authorityFomatDailys.stream().map(x -> x.getSheetNo())
									.collect(Collectors.toList());
							authorityFormatSheets = repo.findAuthorityFormatSheet(companyId, formatCodes, sheetNos);
						}
					} else {
						authorityFomatDailys = repo.findAuthorityFomatDaily(companyId, formatCodeSelects);
						List<BigDecimal> sheetNos = authorityFomatDailys.stream().map(x -> x.getSheetNo())
								.collect(Collectors.toList());
						authorityFormatSheets = repo.findAuthorityFormatSheet(companyId, formatCodeSelects, sheetNos);
					}
				} else {
					// Lấy về domain model "会社の日別実績の修正のフォーマット" tương ứng
					List<String> formatCodes = Arrays.asList(correct.getPreviousDisplayItem());
					authorityFomatDailys = repo.findAuthorityFomatDaily(companyId, formatCodes);
					List<BigDecimal> sheetNos = authorityFomatDailys.stream().map(x -> x.getSheetNo())
							.collect(Collectors.toList());
					authorityFormatSheets = repo.findAuthorityFormatSheet(companyId, formatCodes, sheetNos);
				}
				if (!authorityFomatDailys.isEmpty()) {
					lstFormat = new ArrayList<FormatDPCorrectionDto>();
					lstSheet = new ArrayList<DPSheetDto>();
					Map<Integer, DPAttendanceItem> mapDP = new HashMap<>();
					// set FormatCode for button A2_4
					result.setFormatCode(authorityFomatDailys.stream().map(x -> x.getDailyPerformanceFormatCode())
							.collect(Collectors.toSet()));
					lstSheet = authorityFormatSheets.stream()
							.map(x -> new DPSheetDto(x.getSheetNo().toString(), x.getSheetName().toString()))
							.collect(Collectors.toList());
					lstFormat = authorityFomatDailys.stream()
							.map(x -> new FormatDPCorrectionDto(companyId, x.getDailyPerformanceFormatCode(),
									x.getAttendanceItemId(), x.getSheetNo().toString(), x.getDisplayOrder(),
									x.getColumnWidth().intValue()))
							.collect(Collectors.toList());
					lstAtdItem = lstFormat.stream().map(f -> f.getAttendanceItemId()).collect(Collectors.toList());
					lstAtdItemUnique = new HashSet<Integer>(lstAtdItem).stream().collect(Collectors.toList());
					if (!lstAtdItemUnique.isEmpty()) {
						Map<Integer, DailyAttendanceItemNameAdapterDto> itemName = dailyAttendanceItemNameAdapter
								.getDailyAttendanceItemName(lstAtdItemUnique).stream().collect(Collectors
										.toMap(DailyAttendanceItemNameAdapterDto::getAttendanceItemId, x -> x));
						lstAttendanceItem = lstAtdItemUnique.isEmpty() ? Collections.emptyList()
								: this.repo.getListAttendanceItem(lstAtdItemUnique).stream()
										.map(x -> new DPAttendanceItem(x.getId(),
												itemName.get(x.getId()).getAttendanceItemName(), x.getDisplayNumber(),
												x.isUserCanSet(), x.getLineBreakPosition(), x.getAttendanceAtr(),
												x.getTypeGroup()))
										.collect(Collectors.toList());
						mapDP = lstAttendanceItem.stream().collect(Collectors.toMap(DPAttendanceItem::getId, x -> x));
					}
					List<DPHeaderDto> lstHeader = new ArrayList<>();
					for (FormatDPCorrectionDto dto : lstFormat) {
						// chia cot con code name cua AttendanceItemId chinh va
						// set
						lstHeader.add(DPHeaderDto.createSimpleHeader("A" + String.valueOf(dto.getAttendanceItemId()),
								String.valueOf(dto.getColumnWidth()) + "px", mapDP));
					}
					result.setLstHeader(lstHeader);
					// result.setLstSheet(lstSheet);
					result.createSheets(lstSheet);
					result.addColumnsToSheet(lstFormat, mapDP);
				}
			} else {
				// アルゴリズム「社員の勤務種別に対応する表示項目を取得する」を実行する
				/// アルゴリズム「社員の勤務種別をすべて取得する」を実行する
				List<String> lstBusinessTypeCode = this.repo.getListBusinessType(lstEmployeeId, dateRange);

				// Create header & sheet
				if (lstBusinessTypeCode.size() > 0) {

					lstSheet = this.repo.getFormatSheets(lstBusinessTypeCode);
					/// 対応するドメインモデル「勤務種別日別実績の修正のフォーマット」を取得する
					lstFormat = this.repo.getListFormatDPCorrection(lstBusinessTypeCode);
					Map<Integer, DPAttendanceItem> mapDP = new HashMap<>();
					lstAtdItem = lstFormat.stream().map(f -> f.getAttendanceItemId()).collect(Collectors.toList());
					lstAtdItemUnique = new HashSet<Integer>(lstAtdItem).stream().collect(Collectors.toList());
					result.createSheets(lstSheet);
					if (!lstAtdItemUnique.isEmpty()) {
						Map<Integer, DailyAttendanceItemNameAdapterDto> itemName = dailyAttendanceItemNameAdapter
								.getDailyAttendanceItemName(lstAtdItemUnique).stream().collect(Collectors
										.toMap(DailyAttendanceItemNameAdapterDto::getAttendanceItemId, x -> x));
						lstAttendanceItem = lstAtdItemUnique.isEmpty() ? Collections.emptyList()
								: this.repo.getListAttendanceItem(lstAtdItemUnique).stream()
										.map(x -> new DPAttendanceItem(x.getId(),
												itemName.get(x.getId()).getAttendanceItemName(), x.getDisplayNumber(),
												x.isUserCanSet(), x.getLineBreakPosition(), x.getAttendanceAtr(),
												x.getTypeGroup()))
										.collect(Collectors.toList());
						mapDP = lstAttendanceItem.stream().collect(Collectors.toMap(DPAttendanceItem::getId, x -> x));
					}
					result.addColumnsToSheet(lstFormat, mapDP);
					List<DPHeaderDto> lstHeader = new ArrayList<>();
					for (FormatDPCorrectionDto dto : lstFormat) {
						lstHeader.add(DPHeaderDto.createSimpleHeader("A" + String.valueOf(dto.getAttendanceItemId()),
								String.valueOf(dto.getColumnWidth()) + "px", mapDP));
					}
					result.setLstHeader(lstHeader);
				}

				List<DPBusinessTypeControl> lstDPBusinessTypeControl = new ArrayList<>();
				if (lstFormat.size() > 0) {
					lstDPBusinessTypeControl = this.repo.getListBusinessTypeControl(lstBusinessTypeCode,
							lstAtdItemUnique);
				}
				if (lstDPBusinessTypeControl.size() > 0) {
					// set header access modifier
					// only user are login can edit or others can edit
					result.setColumnsAccessModifier(lstDPBusinessTypeControl);
				}
			}
			for (DPHeaderDto key : result.getLstHeader()) {
				ColumnSetting columnSetting = new ColumnSetting(key.getKey(), false);
				if (!key.getGroup().isEmpty()) {
					result.getColumnSettings().add(new ColumnSetting(key.getGroup().get(0).getKey(), false));
					result.getColumnSettings().add(new ColumnSetting(key.getGroup().get(1).getKey(), false));
				}
				result.getColumnSettings().add(columnSetting);

			}
			;
			// set text to header
			result.setHeaderText(lstAttendanceItem);
			// set color to header
			List<DPAttendanceItemControl> lstAttendanceItemControl = this.repo
					.getListAttendanceItemControl(lstAtdItemUnique);
			result.setLstAttendanceItem(lstAttendanceItem);
			result.setHeaderColor(lstAttendanceItemControl);
		}
		return result;
	}
}

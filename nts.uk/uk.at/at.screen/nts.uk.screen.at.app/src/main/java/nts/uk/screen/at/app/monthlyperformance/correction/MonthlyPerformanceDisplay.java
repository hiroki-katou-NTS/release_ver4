package nts.uk.screen.at.app.monthlyperformance.correction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;

import nts.arc.error.BundledBusinessException;
import nts.arc.error.BusinessException;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.function.app.find.monthlycorrection.fixedformatmonthly.DisplayTimeItemDto;
import nts.uk.ctx.at.function.app.find.monthlycorrection.fixedformatmonthly.MonPfmCorrectionFormatDto;
import nts.uk.ctx.at.function.app.find.monthlycorrection.fixedformatmonthly.MonPfmCorrectionFormatFinder;
import nts.uk.ctx.at.function.app.find.monthlycorrection.fixedformatmonthly.MonthlyActualResultsDto;
import nts.uk.ctx.at.function.app.find.monthlycorrection.fixedformatmonthly.MonthlyRecordWorkTypeDto;
import nts.uk.ctx.at.function.app.find.monthlycorrection.fixedformatmonthly.MonthlyRecordWorkTypeFinder;
import nts.uk.ctx.at.function.app.find.monthlycorrection.fixedformatmonthly.SheetCorrectedMonthlyDto;
import nts.uk.ctx.at.function.dom.monthlycorrection.fixedformatmonthly.ColumnWidtgByMonthly;
import nts.uk.ctx.at.function.dom.monthlycorrection.fixedformatmonthly.ColumnWidtgByMonthlyRepository;
import nts.uk.ctx.at.function.dom.monthlycorrection.fixedformatmonthly.ColumnWidthOfDisplayItem;
import nts.uk.ctx.at.function.dom.monthlycorrection.fixedformatmonthly.InitialDisplayMonthly;
import nts.uk.ctx.at.function.dom.monthlycorrection.fixedformatmonthly.InitialDisplayMonthlyRepository;
import nts.uk.ctx.at.record.dom.adapter.workplace.affiliate.AffAtWorkplaceImport;
import nts.uk.ctx.at.record.dom.adapter.workplace.affiliate.AffWorkplaceAdapter;
import nts.uk.ctx.at.record.dom.workrecord.actuallock.LockStatus;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerErrorRepository;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.Identification;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.IdentificationRepository;
import nts.uk.ctx.at.record.dom.workrecord.managectualsituation.AcquireActualStatus;
import nts.uk.ctx.at.record.dom.workrecord.managectualsituation.ApprovalStatus;
import nts.uk.ctx.at.record.dom.workrecord.managectualsituation.EmploymentFixedStatus;
import nts.uk.ctx.at.record.dom.workrecord.managectualsituation.MonthlyActualSituationOutput;
import nts.uk.ctx.at.record.dom.workrecord.managectualsituation.MonthlyActualSituationStatus;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.ApprovalProcess;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.ApprovalProcessRepository;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.IdentityProcess;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.IdentityProcessRepository;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.SettingUnitType;
import nts.uk.ctx.at.shared.app.find.scherec.monthlyattditem.MonthlyItemControlByAuthDto;
import nts.uk.ctx.at.shared.app.find.scherec.monthlyattditem.MonthlyItemControlByAuthFinder;
import nts.uk.ctx.at.shared.dom.adapter.attendanceitemname.AttendanceItemNameAdapter;
import nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.adapter.DailyAttendanceItemNameAdapter;
import nts.uk.ctx.at.shared.dom.adapter.jobtitle.SharedAffJobTitleHisImport;
import nts.uk.ctx.at.shared.dom.adapter.jobtitle.SharedAffJobtitleHisAdapter;
import nts.uk.screen.at.app.dailymodify.command.DailyModifyResCommandFacade;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DateRange;
import nts.uk.screen.at.app.monthlyperformance.correction.dto.ActualTime;
import nts.uk.screen.at.app.monthlyperformance.correction.dto.ActualTimeState;
import nts.uk.screen.at.app.monthlyperformance.correction.dto.CorrectionOfMonthlyPerformance;
import nts.uk.screen.at.app.monthlyperformance.correction.dto.MonthlyAttendanceItemDto;
import nts.uk.screen.at.app.monthlyperformance.correction.dto.MonthlyPerformanceCorrectionDto;
import nts.uk.screen.at.app.monthlyperformance.correction.param.MonthlyPerformaceLockStatus;
import nts.uk.screen.at.app.monthlyperformance.correction.param.MonthlyPerformanceParam;
import nts.uk.screen.at.app.monthlyperformance.correction.param.PAttendanceItem;
import nts.uk.screen.at.app.monthlyperformance.correction.param.PSheet;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class MonthlyPerformanceDisplay {
	/** 権限別月次項目制御 */
	@Inject
	MonthlyItemControlByAuthFinder monthlyItemControlByAuthFinder;
	@Inject
	private MonthlyPerformanceScreenRepo repo;

	@Inject
	private MonthlyActualSituationStatus monthlyActualStatus;
	@Inject
	AffWorkplaceAdapter affWorkplaceAdapter;
	/** 月次の初期表示フォーマット */
	@Inject
	InitialDisplayMonthlyRepository initialDisplayMonthlyRepository;
	/** 会社の月別実績の修正のフォーマット */
	@Inject
	MonPfmCorrectionFormatFinder monPfmCorrectionFormatFinder;
	/** 月別実績のグリッドの列幅 */
	@Inject
	ColumnWidtgByMonthlyRepository columnWidtgByMonthlyRepository;
	/** 勤務種別の月別実績の修正のフォーマット */
	@Inject
	MonthlyRecordWorkTypeFinder monthlyRecordWorkTypeFinder;
	// @Inject
	// private AttendanceItemLinkingRepository attendanceItemLinkingRepository;
	/**
	 * 実績の時系列をチェックする
	 */
	@Inject
	private MonthlyPerformanceCheck monthlyCheck;
	@Inject
	private AttendanceItemNameAdapter attendanceItemNameAdapter;
	private static final String KMW003_SELECT_FORMATCODE = "KMW003_SELECT_FORMATCODE";

	/**
	 * 表示フォーマットの取得
	 * 
	 * @param lstEmployees:
	 *            社員一覧：List＜社員ID＞
	 * @param formatCodes:
	 *            使用するフォーマットコード：月別実績フォーマットコード 表示する項目一覧
	 */
	public void getDisplayFormat(List<String> lstEmployeeIds, SettingUnitType unitType,
			MonthlyPerformanceCorrectionDto screenDto) {
		// 会社ID：ログイン会社に一致する
		String cId = AppContexts.user().companyId();
		// ロールID：ログイン社員の就業ロールに一致する
		String employmentRoleID = AppContexts.user().roles().forAttendance();
		// パラメータ
		MonthlyPerformanceParam param = screenDto.getParam();
		DateRange dateRange = new DateRange(screenDto.getSelectedActualTime().getStartDate(),
				screenDto.getSelectedActualTime().getEndDate());
		// 権限の場合
		if (unitType == SettingUnitType.AUTHORITY) {
			// アルゴリズム「社員の権限に対応する表示項目を取得する」を実行する
			getDisplayItemAuthority(cId, param, param.getCorrectionOfMonthly());
		}
		// 勤務種別の場合
		else {
			// 社員の勤務種別に対応する表示項目を取得する
			getDisplayItemBussiness(cId, lstEmployeeIds, dateRange, param, screenDto);
		}
		Set<Integer> kintaiIDList = new HashSet<>();
		if (param.getSheets() == null || param.getSheets().isEmpty()) {
			throw new BusinessException("Msg_1261");
		}
		param.getSheets().forEach(item -> {
			kintaiIDList.addAll(item.getDisplayItems().stream().map(kintai -> {
				return kintai.getId();
			}).collect(Collectors.toList()));
		});
		List<Integer> newkintaiIDList = kintaiIDList.stream().collect(Collectors.toList());
		// 対応するドメインモデル「権限別月次項目制御」を取得する
		MonthlyItemControlByAuthDto monthlyItemAuthDto = monthlyItemControlByAuthFinder
				.getMonthlyItemControlByToUse(cId, employmentRoleID, newkintaiIDList, 1);
		// 取得したドメインモデル「権限別月次項目制御」でパラメータ「表示する項目一覧」をしぼり込む
		// Filter param 「表示する項目一覧」 by domain 「権限別月次項目制御」
		screenDto.setAuthDto(monthlyItemAuthDto);
		Map<Integer, PAttendanceItem> lstAtdItemUnique = new HashMap<>();
		List<PSheet> listSheet = new ArrayList<>();
		if (monthlyItemAuthDto != null) {
			for (PSheet sheet : param.getSheets()) {
				sheet.getDisplayItems().retainAll(monthlyItemAuthDto.getListDisplayAndInputMonthly());
				if (sheet.getDisplayItems() != null && sheet.getDisplayItems().size() > 0) {
					listSheet.add(sheet);
					lstAtdItemUnique.putAll(sheet.getDisplayItems().stream()
							.collect(Collectors.toMap(PAttendanceItem::getId, x -> x, (x, y) -> x)));
				}
			}
		} 
//		else {
//			for (PSheet sheet2 : param.getSheets()) {
//				if (sheet2.getDisplayItems() != null && sheet2.getDisplayItems().size() > 0)
//					lstAtdItemUnique.putAll(sheet2.getDisplayItems().stream()
//							.collect(Collectors.toMap(PAttendanceItem::getId, x -> x, (x, y) -> x)));
//			}
//		}
		
		// set lai sheet
		param.setSheets(listSheet);

		// 絞り込んだ勤怠項目の件数をチェックする
		if (lstAtdItemUnique.size() > 0) {
			// ドメインモデル「月次の勤怠項目」を取得する
			List<Integer> attdanceIds = lstAtdItemUnique.keySet().stream().collect(Collectors.toList());
			List<MonthlyAttendanceItemDto> lstAttendanceData = repo.findByAttendanceItemId(cId, attdanceIds);
			lstAttendanceData.sort((t1, t2) -> t1.getDisplayNumber() - t2.getDisplayNumber());

			// 対応するドメインモデル「勤怠項目と枠の紐付け」を取得する - attendanceItemLinkingRepository
			// 取得したドメインモデルの名称をドメインモデル「勤怠項目．名称」に埋め込む
			// Update Attendance Name
			Map<Integer, String> attdanceNames = attendanceItemNameAdapter.getAttendanceItemNameAsMapName(attdanceIds,
					2);
			lstAttendanceData.forEach(c -> {
				PAttendanceItem item = lstAtdItemUnique.get(c.getAttendanceItemId());
				item.setDisplayNumber(c.getDisplayNumber());
				item.setAttendanceAtr(c.getMonthlyAttendanceAtr());
				item.setName(attdanceNames.get(c.getAttendanceItemId()));
				item.setUserCanChange(c.getNameLineFeedPosition() == 1 ? true : false);
				item.setUserCanUpdateAtr(c.getUserCanUpdateAtr());
			});
			param.setLstAtdItemUnique(lstAtdItemUnique);
		}
		// アルゴリズム「ロック状態をチェックする」を実行する
		List<MonthlyPerformaceLockStatus> lstLockStatus = checkLockStatus(cId, lstEmployeeIds,
				screenDto.getProcessDate(), screenDto.getClosureId(),
				new DatePeriod(dateRange.getStartDate(), dateRange.getEndDate()), param.getInitScreenMode());
		param.setLstLockStatus(lstLockStatus);
	}

	/**
	 * アルゴリズム「社員の権限に対応する表示項目を取得する」を実行する (Lấy các item hiển thị ứng với quyền
	 * employee)
	 * 
	 * @param cId
	 * @param formatCodes
	 * @param correctionOfMonthly
	 * @return 表示する項目一覧
	 */
	private void getDisplayItemAuthority(String cId, MonthlyPerformanceParam param,
			CorrectionOfMonthlyPerformance correctionOfMonthly) {
		// 表示する項目一覧
		List<PSheet> resultSheets = new ArrayList<>();
		List<String> formatCodes = param.getFormatCodes();
		if (CollectionUtil.isEmpty(formatCodes)) {
			// ユーザー固有情報「月別実績のの修正」．前回の表示項目が取得できたかチェックする
			if (null != correctionOfMonthly && !Strings.isEmpty(correctionOfMonthly.getPreviousDisplayItem())) {
				// 取得したユーザー固有情報「月別実績の修正．前回の表示項目」をパラメータ「使用するフォーマットコード」にセットする
				formatCodes = Arrays.asList(correctionOfMonthly.getPreviousDisplayItem());
			} else {
				// 対応するドメインモデル「月次の初期表示フォーマット」を取得する
				// 月次の初期表示フォーマット
				List<InitialDisplayMonthly> initDisp = initialDisplayMonthlyRepository.getAllInitialDisMon(cId);
				if (!initDisp.isEmpty()) {
					// 取得したドメインモデル「月次の初期表示フォーマット」をパラメータ「使用するフォーマットコード」にセットする
					formatCodes = initDisp.stream().map(item -> {
						return item.getMonthlyPfmFormatCode().v();
					}).collect(Collectors.toList());
				} else {
					// アルゴリズム「表示項目の選択を起動する」を実行する
					// ダイアログで選択していたフォーマットコードをパラメータ「使用するフォーマットコード」にセットする
					throw new BusinessException(KMW003_SELECT_FORMATCODE);
				}
			}
		}
		// 対応するドメインモデル「会社の月別実績の修正フォーマット」を取得する
		// Contains list of sheet (月別実績の修正のシート) and list of display item in
		// sheet (表示する項目)
		List<MonPfmCorrectionFormatDto> lstMPformats = monPfmCorrectionFormatFinder.getMonPfmCorrectionFormat(cId,
				formatCodes);
		Optional<ColumnWidtgByMonthly> columnWidtgByMonthly = columnWidtgByMonthlyRepository.getColumnWidtgByMonthly(cId);
		for(MonPfmCorrectionFormatDto monPfmCorrectionFormatDto : lstMPformats){
			List<SheetCorrectedMonthlyDto> listSheetCorrectedMonthly = monPfmCorrectionFormatDto.getDisplayItem().getListSheetCorrectedMonthly();
			for(SheetCorrectedMonthlyDto sheetCorrectedMonthlyDto : listSheetCorrectedMonthly){
				List<DisplayTimeItemDto> listDisplayTimeItem = sheetCorrectedMonthlyDto.getListDisplayTimeItem();
				for(DisplayTimeItemDto displayTimeItemDto :listDisplayTimeItem){
					if (displayTimeItemDto.getColumnWidthTable() == null) {
						if (columnWidtgByMonthly.isPresent()) {
							Optional<ColumnWidthOfDisplayItem> optinal = columnWidtgByMonthly.get()
									.getListColumnWidthOfDisplayItem().stream()
									.filter(item -> item.getAttdanceItemID() == displayTimeItemDto.getItemDaily())
									.findFirst();
							if (optinal.isPresent()) {
								displayTimeItemDto.setColumnWidthTable(optinal.get().getColumnWidthTable());
							} else {
								displayTimeItemDto.setColumnWidthTable(100);
							}							
						}
					}
				}				
			}
		}
		
//		if (lstMPformats.isEmpty()) {
//			// 対応するドメインモデル「月別実績のグリッドの列幅」を取得する
//			lstMPformats = getColumnWidtgByMonthly(cId);
//		}
		// アルゴリズム「複数フォーマットの場合のフォーマットを生成する」を実行する
		// 補足資料A_7参照
		// Merge sheet
		Set<Integer> sheetNos = new HashSet<>();
		lstMPformats.forEach(format -> {
			sheetNos.addAll(format.getDisplayItem().getListSheetCorrectedMonthly().stream()
					.map(sheet -> sheet.getSheetNo()).collect(Collectors.toSet()));
		});
		// Merge Attendance Item in sheet
		for (Integer sheet : sheetNos) {
			PSheet pSheet = new PSheet(sheet.toString(), Strings.EMPTY, null);
			List<PAttendanceItem> displayItems = new ArrayList<>();
			lstMPformats.forEach(format -> {
				SheetCorrectedMonthlyDto sheetDto = format.getDisplayItem().getListSheetCorrectedMonthly().stream()
						.filter(item -> item.getSheetNo() == sheet).findAny().orElse(null);
				if (sheetDto != null) {
					pSheet.setSheetName(sheetDto.getSheetName());
					sheetDto.getListDisplayTimeItem().sort((t1, t2) -> t1.getDisplayOrder() - t2.getDisplayOrder());
					displayItems.addAll(sheetDto.getListDisplayTimeItem().stream().map(
							x -> new PAttendanceItem(x.getItemDaily(), x.getDisplayOrder(), x.getColumnWidthTable()))
							.collect(Collectors.toList()));
				}
			});
			pSheet.setDisplayItems(displayItems);
			resultSheets.add(pSheet);
		}
		// 表示する項目一覧
		param.setSheets(resultSheets);

	}

	/**
	 * 社員の勤務種別に対応する表示項目を取得する (Lấy các item hiển thị ứng với loại đi làm của
	 * employee)
	 * 
	 * @return
	 */
	private void getDisplayItemBussiness(String cId, List<String> lstEmployeeId, DateRange dateRange,
			MonthlyPerformanceParam param, MonthlyPerformanceCorrectionDto screenDto) {
		// 表示する項目一覧
		List<PSheet> resultSheets = new ArrayList<>();
		if (CollectionUtil.isEmpty(lstEmployeeId)) {
			return;
		}

		// 特定の社員が所属する勤務種別をすべて取得する Lấy tất cả các loại đi làm của employee chỉ
		// định
		List<String> lstBusinessTypeCode = this.repo.getListBusinessType(lstEmployeeId, dateRange);
		// Create header & sheet
		// 取得したImported「（就業機能）勤務種別」の件数をチェックする
		if (lstBusinessTypeCode.size() == 0) {
			// エラーメッセージ（#Msg_1403）を表示する(hiển thị error message （#Msg_1403）)
			BundledBusinessException bundleExeption = BundledBusinessException.newInstance();
			screenDto.getLstEmployee().stream().forEach(x ->{
				bundleExeption.addMessage(new BusinessException("Msg_1403", x.getCode() + " " + x.getBusinessName()));
			});
			throw bundleExeption;
		}
		param.setFormatCodes(lstBusinessTypeCode);
		// 対応するドメインモデル「勤務種別の月別実績の修正のフォーマット」を取得する
		List<MonthlyRecordWorkTypeDto> monthlyRecordWorkTypeDtos = monthlyRecordWorkTypeFinder
				.getMonthlyRecordWorkTypeByListCode(cId, lstBusinessTypeCode);

		Optional<ColumnWidtgByMonthly> columnWidtgByMonthly = columnWidtgByMonthlyRepository.getColumnWidtgByMonthly(cId);
		// 取得した「勤務種別の月別実績の修正のフォーマット」に表示するすべての項目の列幅があるかチェックする
		for (MonthlyRecordWorkTypeDto monthlyRecordWorkTypeDto : monthlyRecordWorkTypeDtos) {
			List<SheetCorrectedMonthlyDto> listSheetCorrectedMonthly = monthlyRecordWorkTypeDto.getDisplayItem()
					.getListSheetCorrectedMonthly();
			for (SheetCorrectedMonthlyDto sheetCorrectedMonthlyDto : listSheetCorrectedMonthly) {
				List<DisplayTimeItemDto> listDisplayTimeItem = sheetCorrectedMonthlyDto.getListDisplayTimeItem();
				for (DisplayTimeItemDto displayTimeItemDto : listDisplayTimeItem) {
					if (displayTimeItemDto.getColumnWidthTable() == null) {
						if (columnWidtgByMonthly.isPresent()) {
							Optional<ColumnWidthOfDisplayItem> optinal = columnWidtgByMonthly.get()
									.getListColumnWidthOfDisplayItem().stream()
									.filter(item -> item.getAttdanceItemID() == displayTimeItemDto.getItemDaily())
									.findFirst();
							if (optinal.isPresent()) {
								displayTimeItemDto.setColumnWidthTable(optinal.get().getColumnWidthTable());
							} else {
								displayTimeItemDto.setColumnWidthTable(100);
							}							
						}
					}
				}
			}
		}

		// if (monthlyRecordWorkTypeDtos.isEmpty()) {
		// // 対応するドメインモデル「月別実績のグリッドの列幅」を取得する
		// monthlyRecordWorkTypeDtos = getColumnWidtgByMonthly(cId).stream()
		// .map(x -> new MonthlyRecordWorkTypeDto(x.getCompanyID(),
		// Strings.EMPTY, x.getDisplayItem()))
		// .collect(Collectors.toList());
		// }
		// アルゴリズム「複数フォーマットの場合のフォーマットを生成する」を実行する
		// 補足資料A_7参照
		// Merge sheet
		Set<Integer> sheetNos = new HashSet<>();
		monthlyRecordWorkTypeDtos.forEach(format -> {
			sheetNos.addAll(format.getDisplayItem().getListSheetCorrectedMonthly().stream()
					.map(sheet -> sheet.getSheetNo()).collect(Collectors.toSet()));
		});
		// Merge Attendance Item in sheet
		for (Integer sheet : sheetNos) {
			PSheet pSheet = new PSheet(sheet.toString(), Strings.EMPTY, null);
			List<PAttendanceItem> displayItemsTmp = new ArrayList<>();
			List<PAttendanceItem> displayItems = new ArrayList<>();
			monthlyRecordWorkTypeDtos.forEach(format -> {
				SheetCorrectedMonthlyDto sheetDto = format.getDisplayItem().getListSheetCorrectedMonthly().stream()
						.filter(item -> item.getSheetNo() == sheet).findAny().orElse(null);
				if (sheetDto != null) {
					pSheet.setSheetName(sheetDto.getSheetName());
					sheetDto.getListDisplayTimeItem().sort((t1, t2) -> t1.getDisplayOrder() - t2.getDisplayOrder());

					displayItemsTmp.addAll(sheetDto.getListDisplayTimeItem().stream().map(
							x -> new PAttendanceItem(x.getItemDaily(), x.getDisplayOrder(), x.getColumnWidthTable()))
							.collect(Collectors.toList()));
					// displayItems.stream().sorted(Comparator.comparing(PAttendanceItem::getDisplayOrder).reversed());
				}
			});
			// xoa nhung column trung nhau
			displayItems = displayItemsTmp.stream().distinct().collect(Collectors.toList());
			
			pSheet.setDisplayItems(displayItems);
			resultSheets.add(pSheet);
		}
		// 表示する項目一覧
		param.setSheets(resultSheets);
	}

	/**
	 * 対応するドメインモデル「月別実績のグリッドの列幅」を取得する
	 * 
	 * @param cId
	 * @return 表示する項目一覧
	 */
	private List<MonPfmCorrectionFormatDto> getColumnWidtgByMonthly(String cId) {
		List<MonPfmCorrectionFormatDto> lstMPformats = new ArrayList<>();
		Optional<ColumnWidtgByMonthly> columnWidtgByMonthly = columnWidtgByMonthlyRepository
				.getColumnWidtgByMonthly(cId);
		if (columnWidtgByMonthly.isPresent()) {
			List<DisplayTimeItemDto> dispItems = columnWidtgByMonthly.get().getListColumnWidthOfDisplayItem().stream()
					.map(item -> {
						return new DisplayTimeItemDto(0, item.getAttdanceItemID(), item.getColumnWidthTable());
					}).collect(Collectors.toList());
			SheetCorrectedMonthlyDto sheet = new SheetCorrectedMonthlyDto(1, Strings.EMPTY, dispItems);
			MonthlyActualResultsDto resultDto = new MonthlyActualResultsDto(Arrays.asList(sheet));
			MonPfmCorrectionFormatDto formatDto = new MonPfmCorrectionFormatDto(
					columnWidtgByMonthly.get().getCompanyID(), Strings.EMPTY, Strings.EMPTY, resultDto);
			lstMPformats = Arrays.asList(formatDto);
		}
		return lstMPformats;
	}

	/**
	 * ロック状態をチェックする
	 * 
	 * @param empIds
	 *            社員一覧：List＜社員ID＞
	 * @param processDateYM
	 *            処理年月：年月
	 * @param closureId
	 *            締めID：締めID
	 * @param dateRange
	 *            締め期間：期間
	 * 
	 *            ロック状態一覧：List＜月の実績のロック状態＞
	 */
	
	@Inject
	private SharedAffJobtitleHisAdapter affJobTitleAdapter;
	
	@Inject
	private IdentityProcessRepository identityProcessRepo;
	
	@Inject
	private IdentificationRepository identificationRepository;
	
	@Inject 
	private EmployeeDailyPerErrorRepository employeeDailyPerErrorRepo;
	
	@Inject 
	private ApprovalProcessRepository approvalRepo;
	
	public List<MonthlyPerformaceLockStatus> checkLockStatus(String cid, List<String> empIds, Integer processDateYM,
			Integer closureId, DatePeriod closureTime, int intScreenMode) {
		List<MonthlyPerformaceLockStatus> monthlyLockStatusLst = new ArrayList<MonthlyPerformaceLockStatus>();
		// ロック解除モード の場合
		if (intScreenMode == 1) {
			return monthlyLockStatusLst;
		}
		// 社員ID（List）と基準日から所属職場IDを取得
		// 基準日：パラメータ「締め期間」の終了日
		List<AffAtWorkplaceImport> affWorkplaceLst = affWorkplaceAdapter.findBySIdAndBaseDate(empIds,
				closureTime.end());
		if (CollectionUtil.isEmpty(affWorkplaceLst)) {
			return monthlyLockStatusLst;
		}
		// 「List＜所属職場履歴項目＞」の件数ループしてください
		MonthlyPerformaceLockStatus monthlyLockStatus = null;
		
		List<SharedAffJobTitleHisImport> listShareAff = affJobTitleAdapter.findAffJobTitleHisByListSid(empIds, closureTime.end());
		
		Optional<IdentityProcess> identityOp = identityProcessRepo.getIdentityProcessById(cid);
		boolean checkIdentityOp = false;
		//対応するドメインモデル「本人確認処理の利用設定」を取得する
		if(!identityOp.isPresent()) {
			checkIdentityOp = true;
		}else {
			//取得したドメインモデル「本人確認処理の利用設定．日の本人確認を利用する」チェックする
			if(identityOp.get().getUseDailySelfCk() == 0){
				checkIdentityOp = true;
			}
		}
		
		List<Identification> listIdentification = identificationRepository.findByListEmployeeID(empIds, closureTime.start(), closureTime.end());
		
		List<EmployeeDailyPerError> listEmployeeDailyPerError =  employeeDailyPerErrorRepo.finds(empIds, new DatePeriod(closureTime.start(), closureTime.end()));
		
		Optional<ApprovalProcess> approvalProcOp = approvalRepo.getApprovalProcessById(cid);
		
		for (AffAtWorkplaceImport affWorkplaceImport : affWorkplaceLst) {
			
			List<Identification> listIdenByEmpID = new ArrayList<>();
			for(Identification iden : listIdentification) {
				if(iden.getEmployeeId().equals(affWorkplaceImport.getEmployeeId())) {
					listIdenByEmpID.add(iden);
				}
			}
			
			boolean checkExistRecordErrorListDate = false;
			for(EmployeeDailyPerError employeeDailyPerError : listEmployeeDailyPerError) {
				if(employeeDailyPerError.getEmployeeID().equals(affWorkplaceImport.getEmployeeId())) {
					checkExistRecordErrorListDate = true;
					break;
				}
			}
			
			// 月の実績の状況を取得する
			AcquireActualStatus param = new AcquireActualStatus(cid, affWorkplaceImport.getEmployeeId(), processDateYM,
					closureId, closureTime.end(), closureTime, affWorkplaceImport.getWorkplaceId());
			MonthlyActualSituationOutput monthlymonthlyActualStatusOutput = monthlyActualStatus
					.getMonthlyActualSituationStatus(param,approvalProcOp,listShareAff,checkIdentityOp,listIdenByEmpID,checkExistRecordErrorListDate);
			// Output「月の実績の状況」を元に「ロック状態一覧」をセットする
			monthlyLockStatus = new MonthlyPerformaceLockStatus(
					monthlymonthlyActualStatusOutput.getEmployeeClosingInfo().getEmployeeId(),
					// TODO
					LockStatus.UNLOCK,
					// 職場の就業確定状態
					monthlymonthlyActualStatusOutput.getEmploymentFixedStatus().equals(EmploymentFixedStatus.CONFIRM)
							? LockStatus.LOCK : LockStatus.UNLOCK,
					// 月の承認状況
					monthlymonthlyActualStatusOutput.getApprovalStatus().equals(ApprovalStatus.APPROVAL)
							? LockStatus.LOCK : LockStatus.UNLOCK,
					// 月別実績のロック状態
					monthlymonthlyActualStatusOutput.getMonthlyLockStatus(),
					// 本人確認が完了している
					monthlymonthlyActualStatusOutput.getDailyActualSituation().isIdentificationCompleted()
							? LockStatus.UNLOCK : LockStatus.LOCK,
					// 日の実績が存在する
					monthlymonthlyActualStatusOutput.getDailyActualSituation().isDailyAchievementsExist()
							? LockStatus.UNLOCK : LockStatus.LOCK,
					// エラーが0件である
					monthlymonthlyActualStatusOutput.getDailyActualSituation().isDailyRecordError() ? LockStatus.LOCK
							: LockStatus.UNLOCK);
			monthlyLockStatusLst.add(monthlyLockStatus);
		}
		// 過去実績の修正ロック
		LockStatus pastLockStatus = editLockOfPastResult(processDateYM, closureId,
				new ActualTime(closureTime.start(), closureTime.end()));
		// Output「ロック状態」を「ロック状態一覧.過去実績のロック」にセットする
		monthlyLockStatusLst = monthlyLockStatusLst.stream().map(item -> {
			item.setPastPerformaceLock(pastLockStatus);
			return item;
		}).collect(Collectors.toList());

		return monthlyLockStatusLst;
	}

	/**
	 * 過去実績の修正ロック
	 * 
	 * @param processDateYM
	 * @param closureId
	 * @param actualTime
	 * @return ロック状態：ロック or アンロック
	 */
	private LockStatus editLockOfPastResult(Integer processDateYM, Integer closureId, ActualTime actualTime) {
		ActualTimeState actualTimeState = monthlyCheck.checkActualTime(closureId, processDateYM, actualTime);
		if (actualTimeState.equals(ActualTimeState.Past)) {
			return LockStatus.LOCK;
		}
		return LockStatus.UNLOCK;
	}
}

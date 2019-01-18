package nts.uk.screen.at.app.monthlyperformance.correction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import nts.arc.error.BusinessException;
import nts.arc.time.YearMonth;
import nts.gul.collection.CollectionUtil;
import nts.gul.text.StringUtil;
import nts.uk.ctx.at.record.app.find.monthly.root.common.ClosureDateDto;
import nts.uk.ctx.at.record.app.find.workrecord.operationsetting.IdentityProcessDto;
import nts.uk.ctx.at.record.app.find.workrecord.operationsetting.IdentityProcessFinder;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.ApprovalStatusAdapter;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.AppRootOfEmpMonthImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.AppRootSituationMonth;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.AppRootSttMonthEmpImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.EmpPerformMonthParamImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.enums.ApprovalActionByEmpl;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.enums.ApprovalStatusForEmployee;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.enums.ApproverEmployeeState;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.enums.ReleasedProprietyDivision;
import nts.uk.ctx.at.record.dom.adapter.workplace.affiliate.AffAtWorkplaceImport;
import nts.uk.ctx.at.record.dom.adapter.workplace.affiliate.AffWorkplaceAdapter;
import nts.uk.ctx.at.record.dom.approvalmanagement.ApprovalProcessingUseSetting;
import nts.uk.ctx.at.record.dom.approvalmanagement.repository.ApprovalProcessingUseSettingRepository;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimeOfMonthlyRepository;
import nts.uk.ctx.at.record.dom.monthly.agreement.AgreementTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.calc.MonthlyCalculation;
import nts.uk.ctx.at.record.dom.workrecord.actuallock.LockStatus;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerErrorRepository;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.Identification;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.month.ConfirmationMonth;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.ConfirmationMonthRepository;
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
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.MonPerformanceFun;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.MonPerformanceFunRepository;
import nts.uk.ctx.at.shared.app.find.scherec.monthlyattditem.ControlOfMonthlyDto;
import nts.uk.ctx.at.shared.app.find.scherec.monthlyattditem.ControlOfMonthlyFinder;
import nts.uk.ctx.at.shared.app.find.scherec.monthlyattditem.MonthlyItemControlByAuthDto;
import nts.uk.ctx.at.shared.app.find.scherec.monthlyattditem.MonthlyItemControlByAuthFinder;
import nts.uk.ctx.at.shared.dom.adapter.jobtitle.SharedAffJobTitleHisImport;
import nts.uk.ctx.at.shared.dom.adapter.jobtitle.SharedAffJobtitleHisAdapter;
import nts.uk.ctx.at.shared.dom.monthlyattditem.MonthlyAttendanceItemAtr;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureHistory;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.screen.at.app.monthlyperformance.correction.dto.ActualTime;
import nts.uk.screen.at.app.monthlyperformance.correction.dto.ActualTimeState;
import nts.uk.screen.at.app.monthlyperformance.correction.dto.EditStateOfMonthlyPerformanceDto;
import nts.uk.screen.at.app.monthlyperformance.correction.dto.MPCellDataDto;
import nts.uk.screen.at.app.monthlyperformance.correction.dto.MPCellStateDto;
import nts.uk.screen.at.app.monthlyperformance.correction.dto.MPDataDto;
import nts.uk.screen.at.app.monthlyperformance.correction.dto.MPHeaderDto;
import nts.uk.screen.at.app.monthlyperformance.correction.dto.MonthlyAttendanceItemDto;
import nts.uk.screen.at.app.monthlyperformance.correction.dto.MonthlyPerformanceCorrectionDto;
import nts.uk.screen.at.app.monthlyperformance.correction.dto.MonthlyPerformanceEmployeeDto;
import nts.uk.screen.at.app.monthlyperformance.correction.param.MonthlyPerformaceLockStatus;
import nts.uk.screen.at.app.monthlyperformance.correction.param.MonthlyPerformanceParam;
import nts.uk.screen.at.app.monthlyperformance.correction.param.PAttendanceItem;
import nts.uk.screen.at.app.monthlyperformance.correction.query.MonthlyModifyQueryProcessor;
import nts.uk.screen.at.app.monthlyperformance.correction.query.MonthlyModifyResult;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class MonthlyPerformanceReload {

	@Inject
	private ClosureRepository closureRepository;

	@Inject
	private MonthlyModifyQueryProcessor monthlyModifyQueryProcessor;

	@Inject
	private AffWorkplaceAdapter affWorkplaceAdapter;

	@Inject
	private MonthlyActualSituationStatus monthlyActualStatus;

	@Inject
	private MonthlyPerformanceCheck monthlyCheck;

	@Inject
	private MonthlyPerformanceScreenRepo repo;
	
	private static final String STATE_DISABLE = "mgrid-disable";
	private static final String HAND_CORRECTION_MYSELF = "mgrid-manual-edit-target";
	private static final String HAND_CORRECTION_OTHER = "mgrid-manual-edit-other";
//	private static final String REFLECT_APPLICATION = "ntsgrid-reflect";
	private static final String STATE_ERROR = "mgrid-error";
	private static final String STATE_ALARM = "mgrid-alarm";
	private static final String STATE_SPECIAL = "mgrid-special";
	private static final String ADD_CHARACTER = "A";
//	private static final String DATE_FORMAT = "yyyy-MM-dd";

	@Inject
	private MonPerformanceFunRepository monPerformanceFunRepository;
	
	@Inject
	private IdentityProcessFinder identityProcessFinder;
	
	@Inject
	private ApprovalProcessingUseSettingRepository approvalProcessingUseSettingRepo;
	
	@Inject
	private ApprovalStatusAdapter approvalStatusAdapter;
	
	@Inject
	private AttendanceTimeOfMonthlyRepository attendanceTimeOfMonthlyRepo;
	
	@Inject
	private ConfirmationMonthRepository confirmationMonthRepository;

	@Inject
	private ControlOfMonthlyFinder controlOfMonthlyFinder;
	
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
	
	@Inject
	MonthlyItemControlByAuthFinder monthlyItemControlByAuthFinder;
	
	public MonthlyPerformanceCorrectionDto reloadScreen(MonthlyPerformanceParam param) {

		String companyId = AppContexts.user().companyId();

		MonthlyPerformanceCorrectionDto screenDto = new MonthlyPerformanceCorrectionDto();
		screenDto.setClosureId(param.getClosureId());
		screenDto.setProcessDate(param.getYearMonth());
		// ドメインモデル「月別実績の修正の機能」を取得する
		Optional<MonPerformanceFun> monPerformanceFun = monPerformanceFunRepository.getMonPerformanceFunById(companyId);
		// ドメインモデル「本人確認処理の利用設定」を取得する
		IdentityProcessDto identityProcess = identityProcessFinder.getAllIdentityProcessById(companyId);
		//アルゴリズム「承認処理の利用設定を取得する」を実行する
		Optional<ApprovalProcessingUseSetting> optApprovalProcessingUseSetting = this.approvalProcessingUseSettingRepo.findByCompanyId(companyId);
		
		// Comment
		if (monPerformanceFun.isPresent()) {
			screenDto.setComment(monPerformanceFun.get().getComment().v());
			screenDto.setDailySelfChkDispAtr(monPerformanceFun.get().getDailySelfChkDispAtr());
		}
		screenDto.setIdentityProcess(identityProcess);
		this.displayClosure(screenDto, companyId, param.getClosureId(), param.getYearMonth());
		screenDto.setSelectedActualTime(param.getActualTime());
		List<String> employeeIds = param.getLstEmployees().stream().map(e -> e.getId())
				.collect(Collectors.toList());
		
		List<Integer> itemIds = new ArrayList<>(param.getLstAtdItemUnique().keySet());
		MonthlyItemControlByAuthDto monthlyItemAuthDto = monthlyItemControlByAuthFinder
				.getMonthlyItemControlByToUse(AppContexts.user().companyId(), AppContexts.user().roles().forAttendance(), itemIds, 1);
		// 取得したドメインモデル「権限別月次項目制御」でパラメータ「表示する項目一覧」をしぼり込む
		// Filter param 「表示する項目一覧」 by domain 「権限別月次項目制御」
		// set quyen chinh sua item theo user
		screenDto.setAuthDto(monthlyItemAuthDto);
		
		// アルゴリズム「ロック状態をチェックする」を実行する - set lock
		List<MonthlyPerformaceLockStatus> lstLockStatus = checkLockStatus(companyId, employeeIds,
				param.getYearMonth(), param.getClosureId(),
				new DatePeriod(param.getActualTime().getStartDate(), param.getActualTime().getEndDate()), param.getInitScreenMode());		
		param.setLstLockStatus(lstLockStatus);
		
		// lay lai lock status vi khong the gui tu client len duoc
		screenDto.setParam(param);
		screenDto.setLstEmployee(param.getLstEmployees());

		// アルゴリズム「月別実績を表示する」を実行する(Hiển thị monthly actual result)
		displayMonthlyResult(screenDto, param.getYearMonth(), param.getClosureId(), optApprovalProcessingUseSetting.get(), companyId);
		// set trang thai disable theo quyen chinh sua item
		screenDto.createAccessModifierCellState();
		return screenDto;
	}

	private void displayClosure(MonthlyPerformanceCorrectionDto screenDto, String companyId, Integer closureId,
			Integer processYM) {
		// アルゴリズム「締めの名称を取得する」を実行する
		Optional<Closure> closure = closureRepository.findById(companyId, closureId);
		if (!closure.isPresent()) {
			return;
		}
		Optional<ClosureHistory> closureHis = closure.get().getHistoryByYearMonth(YearMonth.of(processYM));
		if (closureHis.isPresent()) {
			// 締め名称 → 画面項目「A4_2：対象締め日」
			screenDto.setClosureName(closureHis.get().getClosureName().v());
			screenDto.setClosureDate(ClosureDateDto.from(closureHis.get().getClosureDate()));
		}
	}

	/**
	 * 月別実績を表示する
	 */
	private void displayMonthlyResult(MonthlyPerformanceCorrectionDto screenDto, Integer yearMonth, Integer closureId,
			ApprovalProcessingUseSetting approvalProcessingUseSetting, String companyId) {
		/**
		 * Create Grid Sheet DTO
		 */

		MonthlyPerformanceParam param = screenDto.getParam();
		List<ConfirmationMonth> listConfirmationMonth = new ArrayList<>();
		List<String> listEmployeeIds = param.getLstEmployees().stream().map(x -> x.getId())
				.collect(Collectors.toList());

		// アルゴリズム「対象年月に対応する月別実績を取得する」を実行する Lấy monthly result ứng với năm tháng
		if (param.getLstAtdItemUnique() == null || param.getLstAtdItemUnique().isEmpty()) {
			throw new BusinessException("Msg_1261");
		}

		List<MPHeaderDto> lstMPHeaderDto = MPHeaderDto.GenerateFixedHeader();

		// G7 G8 G9 hidden column identitfy, approval, dailyconfirm
		for (Iterator<MPHeaderDto> iter = lstMPHeaderDto.listIterator(); iter.hasNext();) {
			MPHeaderDto mpHeaderDto = iter.next();
			if ("identify".equals(mpHeaderDto.getKey()) && screenDto.getIdentityProcess().getUseMonthSelfCK() == 0) {
				iter.remove();
				continue;
			}
			if ("approval".equals(mpHeaderDto.getKey())
					&& approvalProcessingUseSetting.getUseMonthApproverConfirm() == false) {
				iter.remove();
				continue;
			}
			if ("dailyconfirm".equals(mpHeaderDto.getKey()) && screenDto.getDailySelfChkDispAtr() == 0) {
				iter.remove();
				continue;
			}
		}

		/**
		 * Create Header DTO
		 */
		List<MPHeaderDto> lstHeader = new ArrayList<>();
		lstHeader.addAll(lstMPHeaderDto);
		if (param.getLstAtdItemUnique() != null) {
			List<Integer> itemIds = param.getLstAtdItemUnique().keySet().stream().collect(Collectors.toList());
			List<MonthlyAttendanceItemDto> lstAttendanceItem = repo.findByAttendanceItemId(companyId, itemIds);
			Map<Integer, MonthlyAttendanceItemDto> mapMP = lstAttendanceItem.stream().filter(x -> x.getMonthlyAttendanceAtr() != MonthlyAttendanceItemAtr.REFER_TO_MASTER.value).collect(Collectors.toMap(MonthlyAttendanceItemDto::getAttendanceItemId, x -> x));
			List<ControlOfMonthlyDto> listCtrOfMonthlyDto = controlOfMonthlyFinder
					.getListControlOfAttendanceItem(itemIds);
			for (Integer key : param.getLstAtdItemUnique().keySet()) {
				PAttendanceItem item = param.getLstAtdItemUnique().get(key);
				MonthlyAttendanceItemDto dto = mapMP.get(key);
				// ドメインモデル「月次の勤怠項目の制御」を取得する
				// Setting Header color & time input
				Optional<ControlOfMonthlyDto> ctrOfMonthlyDto = listCtrOfMonthlyDto.stream()
						.filter(c -> c.getItemMonthlyId() == item.getId()).findFirst();
				lstHeader.add(MPHeaderDto.createSimpleHeader(item,
						ctrOfMonthlyDto.isPresent() ? ctrOfMonthlyDto.get() : null, dto));
			}
		}

		// 本人確認状況の取得
		// 取得している「本人確認処理の利用設定．月の本人確認を利用する」をチェックする
		if (screenDto.getIdentityProcess().getUseMonthSelfCK() == 1) {
			// 月の本人確認を取得する
			listConfirmationMonth = this.confirmationMonthRepository.findBySomeProperty(listEmployeeIds,
					yearMonth, screenDto.getClosureDate().getClosureDay(), screenDto.getClosureDate().getLastDayOfMonth(),
					closureId);
		}

		// get data approve
		List<AppRootSttMonthEmpImport> approvalByListEmplAndListApprovalRecordDate = null;
		AppRootOfEmpMonthImport approvalRootOfEmloyee = null;
		if (approvalProcessingUseSetting.getUseMonthApproverConfirm()) {
			if (param.getInitMenuMode() == 0 || param.getInitMenuMode() == 1) { // lay trang thai approve mode normal hoac unlock
				// *10 request list 533
				List<EmpPerformMonthParamImport> params = new ArrayList<>();
				for (MonthlyPerformanceEmployeeDto emp : screenDto.getLstEmployee()) {
					EmpPerformMonthParamImport p = new EmpPerformMonthParamImport(new YearMonth(yearMonth), closureId,
							screenDto.getClosureDate().toDomain(), screenDto.getSelectedActualTime().getEndDate(), emp.getId());
					params.add(p);
				}
				approvalByListEmplAndListApprovalRecordDate = this.approvalStatusAdapter.getAppRootStatusByEmpsMonth(params);
			} else if (param.getInitMenuMode() == 2) { // lay trang thai approve mode approve
				// *8 request list 534
				approvalRootOfEmloyee = this.approvalStatusAdapter.getApprovalEmpStatusMonth(
						AppContexts.user().employeeId(), new YearMonth(yearMonth), closureId,
						screenDto.getClosureDate().toDomain(), screenDto.getSelectedActualTime().getEndDate());
			}
		}

		/**
		 * Get Data
		 */
		List<MonthlyModifyResult> results = new ArrayList<>();
		List<Integer> attdanceIds = param.getLstAtdItemUnique().keySet().stream()
				.collect(Collectors.toList());
		results = new GetDataMonthly(listEmployeeIds, new YearMonth(yearMonth), ClosureId.valueOf(closureId),
				screenDto.getClosureDate().toDomain(), attdanceIds, monthlyModifyQueryProcessor).call();
		if (results.size() > 0) {
			screenDto.getItemValues().addAll(results.get(0).getItems());
		}
		Map<String, MonthlyModifyResult> employeeDataMap = results.stream()
				.collect(Collectors.toMap(x -> x.getEmployeeId(), Function.identity(), (x, y) -> x));

		List<MPDataDto> lstData = new ArrayList<>(); // List all data
		List<MPCellStateDto> lstCellState = new ArrayList<>(); // List cell
																// state
		screenDto.setLstData(lstData);
		screenDto.setLstCellState(lstCellState);

		Map<String, MonthlyPerformaceLockStatus> lockStatusMap = param.getLstLockStatus().stream()
				.collect(Collectors.toMap(x -> x.getEmployeeId(), Function.identity(), (x, y) -> x));
		String employeeIdLogin = AppContexts.user().employeeId();

		List<EditStateOfMonthlyPerformanceDto> editStateOfMonthlyPerformanceDtos = this.repo
				.findEditStateOfMonthlyPer(new YearMonth(screenDto.getProcessDate()), listEmployeeIds, attdanceIds);

		for (int i = 0; i < param.getLstEmployees().size(); i++) {
			MonthlyPerformanceEmployeeDto employee = param.getLstEmployees().get(i);
			String employeeId = employee.getId();
			MonthlyModifyResult rowData = employeeDataMap.get(employeeId);
			if (rowData == null) continue;
			
			String lockStatus = lockStatusMap.isEmpty() || !lockStatusMap.containsKey(employee.getId()) || param.getInitMenuMode() == 1 ? ""
					: lockStatusMap.get(employee.getId()).getLockStatusString();

			// set state approval
//			if (param.getInitMenuMode() == 2) { // mode approve disable cot approve theo ket qua no.534
//				if (approvalRootOfEmloyee != null && approvalRootOfEmloyee.getApprovalRootSituations() != null) {
//					for (AppRootSituationMonth approvalRootSituation : approvalRootOfEmloyee
//							.getApprovalRootSituations()) {
//						// 基準社員の承認状況 ＝ フェーズ最中 の場合 => unlock
//						if (approvalRootSituation.getTargetID().equals(employeeId)
//								&& approvalRootSituation.getApprovalAtr() != ApproverEmployeeState.PHASE_DURING) {
//							lstCellState.add(new MPCellStateDto(employeeId, "approval", Arrays.asList(STATE_DISABLE)));
//							break;
//						}
//					}
//				}
//			} else { // cac mode khac luon disable cot approve
//				lstCellState.add(new MPCellStateDto(employeeId, "approval", Arrays.asList(STATE_DISABLE)));
//			}

			// set dailyConfirm
			MonthlyPerformaceLockStatus monthlyPerformaceLockStatus = lockStatusMap.get(employeeId);
			String dailyConfirm = null;
			List<String> listCss = new ArrayList<>();
			listCss.add("daily-confirm-color");
			if (monthlyPerformaceLockStatus != null) {
				if (monthlyPerformaceLockStatus.getMonthlyResultConfirm() == LockStatus.LOCK) {
					dailyConfirm = "！";
					// mau cua kiban chua dap ung duoc nen dang tu set mau
					// set color for cell dailyConfirm
					listCss.add("color-cell-un-approved");
					screenDto.setListStateCell("dailyconfirm", employeeId, listCss);
				} else {
					dailyConfirm = "〇";
					// mau cua kiban chua dap ung duoc nen dang tu set mau
					// set color for cell dailyConfirm
					listCss.add("color-cell-approved");
					screenDto.setListStateCell("dailyconfirm", employeeId, listCss);
				}
			}

			// check true false identify
			boolean identify = listConfirmationMonth.stream().filter(x -> x.getEmployeeId().equals(employeeId))
					.findFirst().isPresent();

			// check true false approve
			boolean approve = false;
			if (approvalProcessingUseSetting.getUseMonthApproverConfirm()) {
				if (param.getInitMenuMode() == 0 || param.getInitMenuMode() == 1) { //mode normal hoac unlock set checkbox theo ket qua no.533
					// *10
					if (approvalByListEmplAndListApprovalRecordDate != null) {
						for (AppRootSttMonthEmpImport approvalApprovalRecordDate : approvalByListEmplAndListApprovalRecordDate) {
							// 承認状況 ＝ 承認済 or 承認中 の場合
							if (approvalApprovalRecordDate.getEmployeeID().equals(employeeId)
									&& approvalApprovalRecordDate
											.getApprovalStatus() == ApprovalStatusForEmployee.DURING_APPROVAL
									|| approvalApprovalRecordDate
											.getApprovalStatus() == ApprovalStatusForEmployee.APPROVED) {
								approve = true;
							}
						}
					}
				} else if (param.getInitMenuMode() == 2) { //mode approve set checkbox theo ket qua no.533
					// *8
					if (approvalRootOfEmloyee != null && approvalRootOfEmloyee.getApprovalRootSituations() != null) {
						for (AppRootSituationMonth approvalRootSituation : approvalRootOfEmloyee
								.getApprovalRootSituations()) {
							// ◆基準社員の承認アクション ＝ 承認した の場合
							if (approvalRootSituation.getTargetID().equals(employeeId) && approvalRootSituation
									.getApprovalStatus().getApprovalActionByEmpl() == ApprovalActionByEmpl.APPROVALED) {
								approve = true;
							}
						}
					}

				}
			}
			
			// set state approval 
			//*7
			if (param.getInitMenuMode() == 2) { // mode approve disable cot approve theo data lay duoc tu no.534
				if(approvalRootOfEmloyee!=null && approvalRootOfEmloyee.getApprovalRootSituations()!=null){
					for (AppRootSituationMonth approvalRootSituation : approvalRootOfEmloyee.getApprovalRootSituations()) {
						// 基準社員の承認状況　＝　フェーズ最中　の場合 => unlock
						if(approvalRootSituation.getTargetID().equals(employeeId) && approvalRootSituation.getApprovalAtr() != ApproverEmployeeState.PHASE_DURING && !approve){
							lstCellState.add(new MPCellStateDto(employeeId, "approval", Arrays.asList(STATE_DISABLE)));
							break;
						}else if(approve && approvalRootSituation.getTargetID().equals(employeeId) && approvalRootSituation.getApprovalStatus().getReleaseDivision() != ReleasedProprietyDivision.NOT_RELEASE && !approve) {
							lstCellState.add(new MPCellStateDto(employeeId, "approval", Arrays.asList(STATE_DISABLE)));
							break;
						}else if( approvalRootSituation.getTargetID().equals(employeeId) && approvalRootSituation.getApprovalAtr() == ApproverEmployeeState.PHASE_DURING && !approve) {
							if(screenDto.getIdentityProcess().getUseMonthSelfCK() == 1 ) {
								if(!identify) {
									lstCellState.add(new MPCellStateDto(employeeId, "approval", Arrays.asList(STATE_DISABLE)));
								}
							}
							break;
						}
					}
				}
			} else { // mode khac luon disable cot approve
				lstCellState.add(new MPCellStateDto(employeeId, "approval", Arrays.asList(STATE_DISABLE)));
			}

			MPDataDto mpdata = new MPDataDto(employeeId, lockStatus, "", employee.getCode(), employee.getBusinessName(),
					employeeId, "", identify, approve, dailyConfirm, "");

			// lock check box1 identify
			if (!employeeIdLogin.equals(employeeId) || param.getInitMenuMode() == 2 
					|| ((!StringUtil.isNullOrEmpty(lockStatus, true)) && (approvalProcessingUseSetting.getUseMonthApproverConfirm() && approve == true))) {
				lstCellState.add(new MPCellStateDto(employeeId, "identify", Arrays.asList(STATE_DISABLE)));
			}
			// Setting data for dynamic column
			List<EditStateOfMonthlyPerformanceDto> newList = editStateOfMonthlyPerformanceDtos.stream()
					.filter(item -> item.getEmployeeId().equals(employeeId)).collect(Collectors.toList());
			if (null != rowData) {
				if (null != rowData.getItems()) {
					rowData.getItems().forEach(item -> {
						// Cell Data
						// TODO item.getValueType().value
						String attendanceAtrAsString = String.valueOf(item.getValueType());
						String attendanceKey = mergeString(ADD_CHARACTER, "" + item.getItemId());
						PAttendanceItem pA = param.getLstAtdItemUnique().get(item.getItemId());
						List<String> cellStatus = new ArrayList<>();

						if (pA.getAttendanceAtr() == 1) { // neu item la thoi gian thi format lai theo dinh dang
							int minute = 0;
							if (item.getValue() != null) {
								minute = Integer.parseInt(item.getValue());
							}
							int hours = Math.abs(minute) / 60;
							int minutes = Math.abs(minute) % 60;
							String valueConvert = (minute < 0) ? "-" + String.format("%d:%02d", hours, minutes)
									: String.format("%d:%02d", hours, minutes);

							mpdata.addCellData(
									new MPCellDataDto(attendanceKey, valueConvert, attendanceAtrAsString, "label"));
						} else
							mpdata.addCellData(new MPCellDataDto(attendanceKey,
									item.getValue() != null ? item.getValue() : "", attendanceAtrAsString, ""));
						if (param.getInitMenuMode() == 2) { // set state mode approve, bat cu lock nao ngoai lock monthly approve thi disable
							if (!StringUtil.isNullOrEmpty(lockStatus, true))
								cellStatus.add(STATE_DISABLE);
						} else { // set state cac mode khac, cu co lock la disable
							if (!StringUtil.isNullOrEmpty(lockStatus, true))
								cellStatus.add(STATE_DISABLE);
						}
						// Cell Data
						lstCellState.add(new MPCellStateDto(employeeId, attendanceKey, cellStatus));

						Optional<EditStateOfMonthlyPerformanceDto> dto = newList.stream()
								.filter(item2 -> item2.getAttendanceItemId().equals(item.getItemId())).findFirst();
						if (dto.isPresent()) { // set mau sua tay cua cell
							if (dto.get().getStateOfEdit() == 0) {
								screenDto.setStateCell(attendanceKey, employeeId, HAND_CORRECTION_MYSELF);
							} else {
								screenDto.setStateCell(attendanceKey, employeeId, HAND_CORRECTION_OTHER);
							}
						}
						// color for attendance Item 202
						if (item.getItemId() == 202) {
							// 月別実績の勤怠時間．月の計算．36協定時間．36協定時間のエラー状態
							Optional<AttendanceTimeOfMonthly> optAttendanceTimeOfMonthly = this.attendanceTimeOfMonthlyRepo
									.find(employeeId, new YearMonth(rowData.getYearMonth()),
											ClosureId.valueOf(rowData.getClosureId()),
											new ClosureDate(rowData.getClosureDate().getClosureDay(),
													rowData.getClosureDate().getLastDayOfMonth()));
							if (optAttendanceTimeOfMonthly.isPresent()) {
								MonthlyCalculation monthlyCalculation = optAttendanceTimeOfMonthly.get()
										.getMonthlyCalculation();
								if (monthlyCalculation != null) {
									AgreementTimeOfMonthly agreementTime = monthlyCalculation.getAgreementTime();
									if (agreementTime != null) {
										switch (agreementTime.getStatus().value) {
										// 限度アラーム時間超過
										case 2:
											// 特例限度アラーム時間超過
										case 4:
											screenDto.setStateCell(attendanceKey, employeeId, STATE_ALARM);
											break;
										// 限度エラー時間超過
										case 1:
											// 特例限度エラー時間超過
										case 3:
											screenDto.setStateCell(attendanceKey, employeeId, STATE_ERROR);
											break;
										// 正常（特例あり）
										case 5:
											// 限度アラーム時間超過（特例あり）
										case 7:
											// 限度エラー時間超過（特例あり）
										case 6:
											screenDto.setStateCell(attendanceKey, employeeId, STATE_SPECIAL);
											break;
										default:
											break;
										}
									}
								}
							}
						}

					});
				}
			}
			lstData.add(mpdata);
		}
	}

	// copy ben MonthlyPerformanceDisplay
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
		} else {
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

	private LockStatus editLockOfPastResult(Integer processDateYM, Integer closureId, ActualTime actualTime) {
		ActualTimeState actualTimeState = monthlyCheck.checkActualTime(closureId, processDateYM, actualTime);
		if (actualTimeState.equals(ActualTimeState.Past)) {
			return LockStatus.LOCK;
		}
		return LockStatus.UNLOCK;
	}

	private String mergeString(String... x) {
		return StringUtils.join(x);
	}
	
}

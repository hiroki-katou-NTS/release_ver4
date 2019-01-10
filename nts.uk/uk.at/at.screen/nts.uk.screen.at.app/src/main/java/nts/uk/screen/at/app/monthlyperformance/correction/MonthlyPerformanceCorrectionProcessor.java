package nts.uk.screen.at.app.monthlyperformance.correction;

import java.math.BigDecimal;
//import java.text.Format;
//import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;

import nts.arc.error.BusinessException;
//import nts.arc.task.parallel.ParallelExceptions.Item;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.gul.collection.CollectionUtil;
import nts.gul.text.StringUtil;
import nts.uk.ctx.at.auth.dom.employmentrole.EmployeeReferenceRange;
import nts.uk.ctx.at.function.dom.adapter.annualworkschedule.EmployeeInformationAdapter;
import nts.uk.ctx.at.function.dom.adapter.annualworkschedule.EmployeeInformationQueryDtoImport;
import nts.uk.ctx.at.record.app.find.monthly.root.common.ClosureDateDto;
import nts.uk.ctx.at.record.app.find.workrecord.operationsetting.FormatPerformanceDto;
import nts.uk.ctx.at.record.app.find.workrecord.operationsetting.IdentityProcessDto;
import nts.uk.ctx.at.record.app.find.workrecord.operationsetting.IdentityProcessFinder;
import nts.uk.ctx.at.record.dom.adapter.query.employee.RegulationInfoEmployeeQuery;
import nts.uk.ctx.at.record.dom.adapter.query.employee.RegulationInfoEmployeeQueryAdapter;
import nts.uk.ctx.at.record.dom.adapter.query.employee.RegulationInfoEmployeeQueryR;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.ApprovalStatusAdapter;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.AppRootOfEmpMonthImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.AppRootSituationMonth;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.AppRootSttMonthEmpImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApprovalRootOfEmployeeImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApprovalRootSituation;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.EmpPerformMonthParamImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.enums.ApprovalActionByEmpl;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.enums.ApprovalStatusForEmployee;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.enums.ApproverEmployeeState;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.enums.ReleasedProprietyDivision;
import nts.uk.ctx.at.record.dom.approvalmanagement.ApprovalProcessingUseSetting;
import nts.uk.ctx.at.record.dom.approvalmanagement.repository.ApprovalProcessingUseSettingRepository;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimeOfMonthlyRepository;
import nts.uk.ctx.at.record.dom.monthly.agreement.AgreementTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.calc.MonthlyCalculation;
import nts.uk.ctx.at.record.dom.organization.EmploymentHistoryImported;
import nts.uk.ctx.at.record.dom.organization.adapter.EmploymentAdapter;
import nts.uk.ctx.at.record.dom.workrecord.actuallock.LockStatus;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.month.ConfirmationMonth;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.ConfirmationMonthRepository;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.FormatPerformance;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.FormatPerformanceRepository;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.MonPerformanceFun;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.MonPerformanceFunRepository;
import nts.uk.ctx.at.shared.app.find.scherec.monthlyattditem.ControlOfMonthlyDto;
import nts.uk.ctx.at.shared.app.find.scherec.monthlyattditem.ControlOfMonthlyFinder;
import nts.uk.ctx.at.shared.app.query.workrule.closure.WorkClosureQueryProcessor;
import nts.uk.ctx.at.shared.dom.attendance.util.AttendanceItemIdContainer;
import nts.uk.ctx.at.shared.dom.attendance.util.AttendanceItemUtil.AttendanceItemType;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
//import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureHistory;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.ctx.at.shared.pub.workrule.closure.PresentClosingPeriodExport;
import nts.uk.ctx.at.shared.pub.workrule.closure.ShClosurePub;
import nts.uk.screen.at.app.dailyperformance.correction.DailyPerformanceScreenRepo;
//import nts.uk.screen.at.app.dailyperformance.correction.dto.DateRange;
import nts.uk.screen.at.app.monthlyperformance.correction.dto.ActualTime;
import nts.uk.screen.at.app.monthlyperformance.correction.dto.ClosureInfoOuput;
import nts.uk.screen.at.app.monthlyperformance.correction.dto.ColumnSetting;
import nts.uk.screen.at.app.monthlyperformance.correction.dto.EditStateOfMonthlyPerformanceDto;
import nts.uk.screen.at.app.monthlyperformance.correction.dto.MPCellDataDto;
import nts.uk.screen.at.app.monthlyperformance.correction.dto.MPCellStateDto;
import nts.uk.screen.at.app.monthlyperformance.correction.dto.MPControlDisplayItem;
import nts.uk.screen.at.app.monthlyperformance.correction.dto.MPDataDto;
import nts.uk.screen.at.app.monthlyperformance.correction.dto.MPHeaderDto;
import nts.uk.screen.at.app.monthlyperformance.correction.dto.MPSheetDto;
import nts.uk.screen.at.app.monthlyperformance.correction.dto.MonthlyAttendanceItemDto;
import nts.uk.screen.at.app.monthlyperformance.correction.dto.MonthlyPerformanceAuthorityDto;
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

/**
 * TODO
 */
@Stateless
public class MonthlyPerformanceCorrectionProcessor {

	@Inject
	private FormatPerformanceRepository formatPerformanceRepository;
	@Inject
	private MonPerformanceFunRepository monPerformanceFunRepository;
	@Inject
	private IdentityProcessFinder identityProcessFinder;
	@Inject
	private EmploymentAdapter employmentAdapter;
//	@Inject
//	private ClosureEmploymentRepository closureEmploymentRepository;
	@Inject
	private ShClosurePub shClosurePub;

	@Inject
	private ClosureRepository closureRepository;

	@Inject
	private ClosureService closureService;
	@Inject
	private MonthlyPerformanceDisplay monthlyDisplay;
	/**
	 * 実績の時系列をチェックする
	 */
	@Inject
	private MonthlyPerformanceCheck monthlyCheck;

	@Inject
	private MonthlyPerformanceScreenRepo repo;

	@Inject
	private MonthlyModifyQueryProcessor monthlyModifyQueryProcessor;

	@Inject
	private DailyPerformanceScreenRepo dailyPerformanceScreenRepo;

	@Inject
	private RegulationInfoEmployeeQueryAdapter regulationInfoEmployeePub;
	
	@Inject
	private ApprovalProcessingUseSettingRepository approvalProcessingUseSettingRepo;
	
//	@Inject
//	private FormatPerformanceRepository formatPerformanceRepo;
	
	@Inject
	private ApprovalStatusAdapter approvalStatusAdapter;
	
	@Inject
	private AttendanceTimeOfMonthlyRepository attendanceTimeOfMonthlyRepo;
	
	@Inject
	private WorkClosureQueryProcessor workClosureQueryProcessor;
	
	@Inject
	private ConfirmationMonthRepository confirmationMonthRepository;

	@Inject
	private EmployeeInformationAdapter employeeInformationAdapter;
	
	/** 月次の勤怠項目の制御 */
	@Inject
	private ControlOfMonthlyFinder controlOfMonthlyFinder;
	
	private static final String STATE_DISABLE = "mgrid-disable";
	private static final String HAND_CORRECTION_MYSELF = "mgrid-manual-edit-target";
	private static final String HAND_CORRECTION_OTHER = "mgrid-manual-edit-other";
//	private static final String REFLECT_APPLICATION = "ntsgrid-reflect";
	private static final String STATE_ERROR = "mgrid-error";
	private static final String STATE_ALARM = "mgrid-alarm";
	private static final String STATE_SPECIAL = "mgrid-special";
	private static final String ADD_CHARACTER = "A";
//	private static final String DATE_FORMAT = "yyyy-MM-dd";
	
	private final static List<Integer> ITEM_ID_ALL = AttendanceItemIdContainer.getIds(AttendanceItemType.MONTHLY_ITEM)
			.stream().map(x -> x.getItemId()).collect(Collectors.toList());

	public MonthlyPerformanceCorrectionDto initScreen(MonthlyPerformanceParam param) {
		String companyId = AppContexts.user().companyId();
		String employeeId = AppContexts.user().employeeId();
//		String rollId = AppContexts.user().roles().forAttendance();
		AppContexts.user().roles();
		MonthlyPerformanceCorrectionDto screenDto = new MonthlyPerformanceCorrectionDto();
		screenDto.setParam(param);
		// 1. 起動に必要な情報の取得
		// ドメインモデル「実績修正画面で利用するフォーマット」を取得する
		Optional<FormatPerformance> formatPerformance = formatPerformanceRepository.getFormatPerformanceById(companyId);
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
		if (formatPerformance.isPresent()) {
			screenDto.setFormatPerformance(FormatPerformanceDto.fromDomain(formatPerformance.get()));
		}
		// 本人確認処理の利用設定
		screenDto.setIdentityProcess(identityProcess);
		
		// 2. アルゴリズム「ログイン社員の月別実績の権限を取得する」を実行する
		// TODO 勤務実績の権限 Authority of the work record
		boolean isExistAuthorityWorkRecord = true;
		// 存在しない場合
		if (!isExistAuthorityWorkRecord) {
			// エラーメッセージ（Msg_914）を表示する
			throw new BusinessException("Msg_914");
		}
		
		Integer closureId = null;
		Integer yearMonth = null;
		
		// 3. アルゴリズム「ログイン社員の締めを取得する」を実行する   move  ログイン社員の締めを取得する  in authority 1.1
		// 基準日：システム日付
		if(param.getClosureId() == null){
			closureId = this.getClosureId(companyId, employeeId, GeneralDate.today());
			param.setClosureId(closureId);
		} else {
			closureId = param.getClosureId();
		}
		screenDto.setClosureId(closureId);
		
		// 4.アルゴリズム「処理年月の取得」を実行する   move   処理年月の取得   in authority 1.
		Optional<PresentClosingPeriodExport> presentClosingPeriodExport = shClosurePub.find(companyId, closureId);

		// アルゴリズム「締め情報の表示」を実行する move 締め情報の表示 in authority 2.
		//set A3_2
		yearMonth = 0;
		if (param.getYearMonth() == 0) {
			if (presentClosingPeriodExport.isPresent()) {
				yearMonth = presentClosingPeriodExport.get().getProcessingYm().v();
				param.setYearMonth(yearMonth);
				// 処理年月
				screenDto.setProcessDate(yearMonth);
			}
		} else {
			yearMonth = param.getYearMonth();
			screenDto.setProcessDate(yearMonth);
		}
		// 5. アルゴリズム「締め情報の表示」を実行する  move  実績期間の表示 in authority 3.
		this.displayClosure(screenDto, companyId, closureId, yearMonth);

		// 6. どのメニューから起動したのかをチェックする (Check xem khởi động từ menu nào)
		// 「月別実績の修正」からの場合         
		if (param.getInitMenuMode() == 0 || param.getInitMenuMode() == 1) {// normal mode hoac unlock mode
			// 7. アルゴリズム「通常モードで起動する」を実行する
			// アルゴリズム「<<Public>> 就業条件で社員を検索して並び替える」を実行する
			if (param.getLstEmployees() != null && !param.getLstEmployees().isEmpty()) {
				screenDto.setLstEmployee(param.getLstEmployees());
			} else {
				List<RegulationInfoEmployeeQueryR> regulationRs = regulationInfoEmployeePub.search(
						createQueryEmployee(new ArrayList<>(), presentClosingPeriodExport.get().getClosureStartDate(),
								presentClosingPeriodExport.get().getClosureEndDate()));

				List<MonthlyPerformanceEmployeeDto> lstEmployeeDto = regulationRs.stream().map(item -> {
					return new MonthlyPerformanceEmployeeDto(item.getEmployeeId(), item.getEmployeeCode(),
							item.getEmployeeName(), item.getWorkplaceName(), item.getWorkplaceId(), "", false);
				}).collect(Collectors.toList());
				screenDto.setLstEmployee(lstEmployeeDto);
				param.setLstEmployees(lstEmployeeDto);
			}
			screenDto.setLoginUser(employeeId);

			List<String> employeeIds = screenDto.getLstEmployee().stream().map(e -> e.getId())
					.collect(Collectors.toList());
			
			// lay data xem thang nao co du lieu de hien thi
			List<MonthlyModifyResult> results = new GetDataMonthly(employeeIds, new YearMonth(yearMonth),
					ClosureId.valueOf(closureId), screenDto.getClosureDate().toDomain(), ITEM_ID_ALL,
					monthlyModifyQueryProcessor).call();
			
			// fix bug 
			if (results.isEmpty()) {
				String mess = new String("Msg_1450");
				createFixedHeader(screenDto, yearMonth, closureId, optApprovalProcessingUseSetting.get(),mess);
				return screenDto;
			}

			// lay lai employeeID cua nhung nhan vien co du lieu
			employeeIds = results.stream().map(e -> e.getEmployeeId()).collect(Collectors.toList());
			
			// アルゴリズム「表示フォーマットの取得」を実行する(Thực hiện 「Lấy format hiển thị」)
			// TODO Data null confirm??formatPerformance
			if (formatPerformance.isPresent()) {
				monthlyDisplay.getDisplayFormat(employeeIds, formatPerformance.get().getSettingUnitType(), screenDto);
			} else {
				String mess = new String("Msg_1452");
				createFixedHeader(screenDto, yearMonth, closureId, optApprovalProcessingUseSetting.get(),mess);
				return screenDto;
			}
			if (screenDto.getParam().getLstAtdItemUnique().isEmpty()){
				String mess = new String("Msg_1452");
				createFixedHeader(screenDto, yearMonth, closureId, optApprovalProcessingUseSetting.get(),mess);
				return screenDto;
			}

			List<MonthlyPerformaceLockStatus> lstLockStatus = screenDto.getParam().getLstLockStatus();
			if (lstLockStatus.stream().allMatch(item -> item.getLockStatusString() != Strings.EMPTY)) {
				screenDto.setShowRegisterButton(false);
			} else
				screenDto.setShowRegisterButton(true);
			
			// アルゴリズム「月別実績を表示する」を実行する Hiển thị monthly result
			displayMonthlyResult(screenDto, yearMonth, closureId, optApprovalProcessingUseSetting.get(), companyId, results);
		
		} else { // 「月別実績の承認」からの場合
			//アルゴリズム「締め情報の表示」を実行する       move 実績期間の表示
			this.displayClosureInfo( screenDto,  companyId,  closureId,
					yearMonth);
			
			//アルゴリズム「承認モードで起動する」を実行する
			this.startUpInApprovalMode(optApprovalProcessingUseSetting, formatPerformance,screenDto,yearMonth, companyId);
			if(screenDto.getMess() != null && !screenDto.getMess().isEmpty()) {
				return screenDto;
			}
		}
		
		// set data of lstControlDisplayItem
		List<Integer> attdanceIds = screenDto.getParam().getLstAtdItemUnique().keySet().stream()
				.collect(Collectors.toList());
		screenDto.getLstControlDisplayItem().setItemIds(attdanceIds);
		screenDto.getLstControlDisplayItem().getLstAttendanceItem();

		// 画面項目の非活制御をする
		// アルゴリズム「実績の時系列をチェックする」を実行する (Check actual time)
		screenDto.setActualTimeState(
				monthlyCheck.checkActualTime(closureId, yearMonth, screenDto.getSelectedActualTime()).value);

		param.setActualTime(screenDto.getSelectedActualTime());
		// author
		screenDto.setAuthorityDto(getAuthority(screenDto));
		
		screenDto.createAccessModifierCellState();
		return screenDto;
	}
	
	//締め情報の表示
	private void displayClosureInfo(MonthlyPerformanceCorrectionDto screenDto, String companyId, Integer closureId,
			Integer processYM){
		//ドメインモデル「締め」を取得する
		//List<ClosureHistory> lstClosureHistory = this.closureRepository.findByCurrentMonth(companyId, new YearMonth(processYM));
		List<Closure> lstClosure = this.closureRepository.findAllUse(companyId);
		List<ClosureHistory> lstClosureHistory = new ArrayList<>();
		lstClosure.forEach(item -> {
			lstClosureHistory.addAll(item.getClosureHistories().stream().filter(closureItem -> 
				closureItem.getStartYearMonth().v() <= processYM.intValue() && 
						closureItem.getEndYearMonth().v() >= processYM.intValue()).collect(Collectors.toList()));
			
		});
		
		
		List<ClosureInfoOuput> lstClosureInfoOuput = new ArrayList<>();
		//取得した実績期間を画面に表示する
		//set A4_8
		lstClosureInfoOuput = lstClosureHistory.stream().map(x->{
			return new ClosureInfoOuput(x.getClosureName().v(), x.getClosureId().value);
		}).collect(Collectors.toList());
		screenDto.setLstclosureInfoOuput(lstClosureInfoOuput);
		
		//set A4_7
		int closureSelected = closureId;
		Optional<ClosureHistory> checkClosureExist = lstClosureHistory.stream()
        .filter(x ->{
       return x.getClosureId().value==closureId;
        })
        .findFirst();
		
		if(!checkClosureExist.isPresent() && lstClosureHistory!=null && !lstClosureHistory.isEmpty()){
			closureSelected = lstClosureHistory.get(0).getClosureId().value;
		}	
		screenDto.setSelectedClosure(closureSelected);
		
	}
	
	
	private void startUpInApprovalMode(Optional<ApprovalProcessingUseSetting> optApprovalProcessingUseSetting,
			Optional<FormatPerformance> formatPerformance, MonthlyPerformanceCorrectionDto screenDto, Integer yearMonth,
			String companyId) {
		if (optApprovalProcessingUseSetting.isPresent()) {
			// 取得している「承認処理の利用設定．月の承認者確認を利用する」をチェックする
			ApprovalProcessingUseSetting approvalProcessingUseSetting = optApprovalProcessingUseSetting.get();
			if (approvalProcessingUseSetting.getUseMonthApproverConfirm()) {
				// アルゴリズム「ログイン社員の承認対象者の取得」を実行する
//				// request list 534
//				ApprovalRootOfEmployeeImport approvalRootOfEmloyee = this.approvalStatusAdapter
//						.getApprovalEmpStatusMonth(AppContexts.user().employeeId(), new YearMonth(yearMonth),
//								screenDto.getClosureId(), screenDto.getClosureDate().toDomain(),
//								screenDto.getSelectedActualTime().getEndDate());
				//Imported（就業）「基準社員の承認対象者」を取得する request list 133
				ApprovalRootOfEmployeeImport approvalRootOfEmloyee = this.approvalStatusAdapter
						.getApprovalRootOfEmloyeeNew(screenDto.getSelectedActualTime().getEndDate(), 
								screenDto.getSelectedActualTime().getEndDate(), AppContexts.user().employeeId(), companyId, Integer.valueOf(2));
				
				if (approvalRootOfEmloyee == null || approvalRootOfEmloyee.getApprovalRootSituations().isEmpty()) {
					String mess = new String("Msg_1451");
					createFixedHeader(screenDto, yearMonth, screenDto.getSelectedClosure(), approvalProcessingUseSetting,mess);
					return;
				}

				// 社員(list)に対応する処理締めを取得する
				List<ApprovalRootSituation> approvalRootSituations = approvalRootOfEmloyee.getApprovalRootSituations();
				Set<String> empIds = approvalRootSituations.stream().map(a -> a.getTargetID())
						.collect(Collectors.toSet());
				List<String> employeeIds = new ArrayList<>();
				for (String empId : empIds) {
					Closure closureDataByEmployee = closureService.getClosureDataByEmployee(empId,
							screenDto.getSelectedActualTime().getEndDate());
					if (closureDataByEmployee != null  && closureDataByEmployee.getClosureId().value ==  screenDto.getClosureId()) {
						employeeIds.add(empId);
					}
				}
				if (employeeIds.isEmpty()) {
					String mess = new String("Msg_1450");
					createFixedHeader(screenDto, yearMonth, screenDto.getSelectedClosure(), approvalProcessingUseSetting, mess);
					return;
				}

				// lay thong tin nhan vien theo empID thu duoc
				EmployeeInformationQueryDtoImport params = new EmployeeInformationQueryDtoImport(employeeIds,
						screenDto.getSelectedActualTime().getEndDate(), true, false, false, true, false, false);
				List<MonthlyPerformanceEmployeeDto> lstEmployee = employeeInformationAdapter.getEmployeeInfo(params)
						.stream()
						.map(item -> new MonthlyPerformanceEmployeeDto(item.getEmployeeId(), item.getEmployeeCode(),
								item.getBusinessName(),
								item.getWorkplace() == null ? null : item.getWorkplace().getWorkplaceName(),
								item.getWorkplace() == null ? null : item.getWorkplace().getWorkplaceCode(), "", false))
						.collect(Collectors.toList());
				lstEmployee.sort((e1, e2) -> e1.getCode().compareTo(e2.getCode()));
				screenDto.setLstEmployee(lstEmployee);
				screenDto.setLoginUser(AppContexts.user().employeeId());

				// lay data xem thang nao co du lieu de hien thi
				List<MonthlyModifyResult> results = new GetDataMonthly(employeeIds, new YearMonth(yearMonth),
						ClosureId.valueOf(screenDto.getClosureId()), screenDto.getClosureDate().toDomain(), ITEM_ID_ALL,
						monthlyModifyQueryProcessor).call();

				// lay lai employeeID cua nhung nhan vien co du lieu
				employeeIds = results.stream().map(e -> e.getEmployeeId()).collect(Collectors.toList());

				// アルゴリズム「表示フォーマットの取得」を実行する(Thực hiện 「Lấy format hiển thị」)
				// TODO Data null confirm??formatPerformance
				if (formatPerformance.isPresent()) {
					monthlyDisplay.getDisplayFormat(employeeIds, formatPerformance.get().getSettingUnitType(),
							screenDto);
				} else {
					throw new BusinessException("Msg_1452");
				}

				List<MonthlyPerformaceLockStatus> lstLockStatus = screenDto.getParam().getLstLockStatus();
				if (lstLockStatus.stream().allMatch(item -> item.getLockStatusString() != Strings.EMPTY)) {
					screenDto.setShowRegisterButton(false);
				} else
					screenDto.setShowRegisterButton(true);

				// アルゴリズム「月別実績を表示する」を実行する Hiển thị monthly result
				displayMonthlyResult(screenDto, yearMonth, screenDto.getSelectedClosure(), approvalProcessingUseSetting,
						companyId, results);

			} else {
				throw new BusinessException("Msg_873");
			}
		}
		// fixed
		else {
			throw new BusinessException("Msg_873");
		}
	}
	/*
	private void obtainDisplayFormat(List<String> lstEmpId, String formatCode, String companyId){
		//ドメインモデル「実績修正画面で利用するフォーマット」を取得する
		Optional<FormatPerformance> optFormatPerformance = this.formatPerformanceRepo.getFormatPerformanceById(companyId);
		if(optFormatPerformance.isPresent()){
			SettingUnitType settingUnitType = optFormatPerformance.get().getSettingUnitType();
			//権限
			if(settingUnitType.value==0){
				//アルゴリズム「社員の権限に対応する表示項目を取得する」を実行する
			}
			//勤務種別
			else{
				
			}
		}
	}
	*/
	

	private RegulationInfoEmployeeQuery createQueryEmployee(List<String> employeeCodes, GeneralDate startDate,
			GeneralDate endDate) {
		RegulationInfoEmployeeQuery query = new RegulationInfoEmployeeQuery();
		query.setBaseDate(GeneralDate.today());
		query.setReferenceRange(EmployeeReferenceRange.DEPARTMENT_ONLY.value);
		query.setFilterByEmployment(false);
		query.setEmploymentCodes(Collections.emptyList());
		// query.setFilterByDepartment(false);
		// query.setDepartmentCodes(Collections.emptyList());
		query.setFilterByWorkplace(false);
		query.setWorkplaceCodes(Collections.emptyList());
		query.setFilterByClassification(false);
		query.setClassificationCodes(Collections.emptyList());
		query.setFilterByJobTitle(false);
		query.setJobTitleCodes(Collections.emptyList());
		query.setFilterByWorktype(false);
		query.setWorktypeCodes(Collections.emptyList());
		query.setPeriodStart(startDate);
		query.setPeriodEnd(endDate);
		query.setIncludeIncumbents(true);
		query.setIncludeWorkersOnLeave(true);
		query.setIncludeOccupancy(true);
		// query.setIncludeAreOnLoan(true);
		// query.setIncludeGoingOnLoan(false);
		query.setIncludeRetirees(false);
		query.setRetireStart(GeneralDate.today());
		query.setRetireEnd(GeneralDate.today());
		query.setFilterByClosure(false);
		return query;
	}

	private List<MonthlyPerformanceAuthorityDto> getAuthority(MonthlyPerformanceCorrectionDto screenDto) {
		String roleId = AppContexts.user().roles().forAttendance();
		if (roleId != null) {
			List<MonthlyPerformanceAuthorityDto> dailyPerformans = dailyPerformanceScreenRepo.findAuthority(roleId, new BigDecimal(1));
			if (!dailyPerformans.isEmpty()) {
				return dailyPerformans;
			}
		}
		throw new BusinessException("Msg_914");
	}

	/**
	 * アルゴリズム「ログイン社員の締めを取得する」を実行する
	 * 
	 * @return 対象締め：締めID
	 */
	private Integer getClosureId(String cId, String employeeId, GeneralDate sysDate) {
		// アルゴリズム「社員IDと基準日から社員の雇用コードを取得」を実行する
		// Thực hiện thuật toán「Lấy employment từ employee ID và base date」
		// 社員IDと基準日から社員の雇用コードを取得
		Optional<EmploymentHistoryImported> empHistory = this.employmentAdapter.getEmpHistBySid(cId, employeeId,
				sysDate);
		String employmentCode = empHistory.orElseThrow(() -> new BusinessException("Msg_1143")).getEmploymentCode();
		// 雇用に紐づく締めを取得する
		Integer closureId = workClosureQueryProcessor.findClosureByEmploymentCode(employmentCode);

		// 対象締め：締めID
		return closureId;
	}

	/**
	 * Get id of employee list.
	 */
	/*
	private List<MonthlyPerformanceEmployeeDto> extractEmployeeList(List<MonthlyPerformanceEmployeeDto> lstEmployee,
			String sId, DateRange range) {
		if (!lstEmployee.isEmpty()) {
			return lstEmployee;
		} else {
			return getListEmployee(sId, range);
		}
	}
	*/

	/** アルゴリズム「対象者を抽出する」を実行する */
//	private List<MonthlyPerformanceEmployeeDto> getListEmployee(String sId, DateRange dateRange) {
//		// アルゴリズム「自職場を取得する」を実行する
//		// List<String> lstJobTitle = this.repo.getListJobTitle(dateRange);
//		// List<String> lstEmployment = this.repo.getListEmployment();
//		/// 対応するドメインモデル「所属職場」を取得する + 対応するドメインモデル「職場」を取得する
//		Map<String, String> lstWorkplace = this.repo.getListWorkplace(sId, dateRange);
//		// List<String> lstClassification = this.repo.getListClassification();
//		// 取得したドメインモデル「所属職場．社員ID」に対応するImported「（就業）社員」を取得する
//		if (lstWorkplace.isEmpty()) {
//			return new ArrayList<>();
//		}
//		return this.repo.getListEmployee(null, null, lstWorkplace, null);
//	}

	/**
	 * 締め情報の表示
	 * 
	 * @param screenDto
	 *            return result to DTO
	 * @param companyId
	 * @param closureId
	 * @param processYM
	 */
	
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
		// アルゴリズム「実績期間の取得」を実行する
		List<ActualTime> actualTimes = closure.get().getPeriodByYearMonth(YearMonth.of(processYM)).stream()
				.map(time -> {
					return new ActualTime(time.start(), time.end());
				}).collect(Collectors.toList());
		if (CollectionUtil.isEmpty(actualTimes))
			return;
		// 実績期間 → 画面項目「A4_5：実績期間選択肢」
		screenDto.setLstActualTimes(actualTimes);
		// 実績期間の件数をチェックする
		if (actualTimes.size() == 1) {
			//set A4_4
			screenDto.setSelectedActualTime(
					new ActualTime(actualTimes.get(0).getStartDate(), actualTimes.get(0).getEndDate()));
		} else if (actualTimes.size() == 2) {
			// 当月の期間を算出する
			DatePeriod datePeriod = closureService.getClosurePeriod(closureId, new YearMonth(processYM));
			// 画面項目「A4_4：実績期間選択」の選択状態を変更する
			screenDto.setSelectedActualTime(new ActualTime(datePeriod.start(), datePeriod.end()));
		}
	}

	/**
	 * 月別実績を表示する
	 */
	private void displayMonthlyResult(MonthlyPerformanceCorrectionDto screenDto, Integer yearMonth, Integer closureId,
			ApprovalProcessingUseSetting approvalProcessingUseSetting, String companyId,
			List<MonthlyModifyResult> results) {
		/**
		 * Create Grid Sheet DTO
		 */

		MPControlDisplayItem displayItem = screenDto.getLstControlDisplayItem();
		MonthlyPerformanceParam param = screenDto.getParam();
		List<ConfirmationMonth> listConfirmationMonth = new ArrayList<>();
		List<String> listEmployeeIds = screenDto.getLstEmployee().stream().map(x->x.getId()).collect(Collectors.toList());

		// アルゴリズム「対象年月に対応する月別実績を取得する」を実行する Lấy monthly result ứng với năm tháng
		if (param.getLstAtdItemUnique() == null || param.getLstAtdItemUnique().isEmpty()) {
			throw new BusinessException("Msg_1450");
		}

		List<MPSheetDto> lstSheets = param.getSheets().stream().map(c -> {
			MPSheetDto sh = new MPSheetDto(c.getSheetNo(), c.getSheetName());
			for (PAttendanceItem attend : c.getDisplayItems()) {
				sh.addColumn(mergeString(ADD_CHARACTER, attend.getId().toString()));
			}
			return sh;
		}).collect(Collectors.toList());
		displayItem.createSheets(lstSheets);

		List<MPHeaderDto> lstMPHeaderDto = MPHeaderDto.GenerateFixedHeader();
		
		//G7 G8 G9 hidden column identitfy, approval, dailyconfirm
		for (Iterator<MPHeaderDto> iter = lstMPHeaderDto.listIterator(); iter.hasNext(); ) {
			MPHeaderDto mpHeaderDto = iter.next();
			if ("identify".equals(mpHeaderDto.getKey())
					&& screenDto.getIdentityProcess().getUseMonthSelfCK() == 0) {
		        iter.remove();
		        continue;
		    }
			if ("approval".equals(mpHeaderDto.getKey())
					&& approvalProcessingUseSetting.getUseMonthApproverConfirm() == false) {
				iter.remove();
				continue;
			}
			if ("dailyconfirm".equals(mpHeaderDto.getKey())
					&& screenDto.getDailySelfChkDispAtr() == 0) {
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
			Map<Integer, MonthlyAttendanceItemDto> mapMP = lstAttendanceItem.stream().collect(Collectors.toMap(MonthlyAttendanceItemDto::getAttendanceItemId, x -> x));
			List<ControlOfMonthlyDto> listCtrOfMonthlyDto = controlOfMonthlyFinder.getListControlOfAttendanceItem(itemIds);
			for (Integer key : param.getLstAtdItemUnique().keySet()) {
				PAttendanceItem item = param.getLstAtdItemUnique().get(key);
				MonthlyAttendanceItemDto dto = mapMP.get(key);
				// ドメインモデル「月次の勤怠項目の制御」を取得する
				// Setting Header color & time input
				Optional<ControlOfMonthlyDto> ctrOfMonthlyDto = listCtrOfMonthlyDto.stream().filter(c -> c.getItemMonthlyId() == item.getId()).findFirst();
				lstHeader.add(MPHeaderDto.createSimpleHeader(item, ctrOfMonthlyDto.isPresent() ? ctrOfMonthlyDto.get() : null, dto));
			}
		}
		displayItem.setLstHeader(lstHeader);
		// Fixed header
		screenDto.setLstFixedHeader(lstMPHeaderDto);
		screenDto.getLstFixedHeader().forEach(column -> {
			screenDto.getLstControlDisplayItem().getColumnSettings().add(new ColumnSetting(column.getKey(), false));
		});
		//36 ->A36 (A36-> A40)
		for (MPHeaderDto key : displayItem.getLstHeader()) {
			ColumnSetting columnSetting = new ColumnSetting(key.getKey(), false);
			if (!key.getGroup().isEmpty()) {
				displayItem.getColumnSettings().add(new ColumnSetting(key.getGroup().get(0).getKey(), false));
				displayItem.getColumnSettings().add(new ColumnSetting(key.getGroup().get(1).getKey(), false));
			} else {
				if (key.getKey().contains("A")) {
					PAttendanceItem item = param.getLstAtdItemUnique()
							.get(Integer.parseInt(key.getKey().substring(1, key.getKey().length()).trim()));
					columnSetting.setTypeFormat(item.getAttendanceAtr());
				}
			}
			displayItem.getColumnSettings().add(columnSetting);
		}
		
		// 本人確認状況の取得
		// 取得している「本人確認処理の利用設定．月の本人確認を利用する」をチェックする
		if(screenDto.getIdentityProcess().getUseMonthSelfCK() == 1){
			// 月の本人確認を取得する
			listConfirmationMonth = this.confirmationMonthRepository.findBySomeProperty(listEmployeeIds,
					yearMonth, screenDto.getClosureDate().getClosureDay(), screenDto.getClosureDate().getLastDayOfMonth(),
					closureId);
		}
		
		//get data approve
		List<AppRootSttMonthEmpImport> approvalByListEmplAndListApprovalRecordDate = null;
		AppRootOfEmpMonthImport approvalRootOfEmloyee = null;
		if (approvalProcessingUseSetting.getUseMonthApproverConfirm()) { // neu hien thi cot approve
			if (param.getInitMenuMode() == 0 || param.getInitMenuMode() == 1) { // lay data approve mode normal hoac unlock
				// *10 request list 533
				List<EmpPerformMonthParamImport> params = new ArrayList<>();
				for (MonthlyPerformanceEmployeeDto emp : screenDto.getLstEmployee()) {
					EmpPerformMonthParamImport p = new EmpPerformMonthParamImport(new YearMonth(yearMonth), closureId,
							screenDto.getClosureDate().toDomain(), screenDto.getSelectedActualTime().getEndDate(), emp.getId());
					params.add(p);
				}
				approvalByListEmplAndListApprovalRecordDate = this.approvalStatusAdapter.getAppRootStatusByEmpsMonth(params);
			} else if (param.getInitMenuMode() == 2) { // lay data approve mode approve
				// *8 request list 534
				approvalRootOfEmloyee = this.approvalStatusAdapter.getApprovalEmpStatusMonth(
						AppContexts.user().employeeId(), new YearMonth(yearMonth), closureId,
						screenDto.getClosureDate().toDomain(), screenDto.getSelectedActualTime().getEndDate());
			}
		}
		

		/**
		 * Get Data
		 */
//		List<MonthlyModifyResult> results = new ArrayList<>();
		List<Integer> attdanceIds = screenDto.getParam().getLstAtdItemUnique().keySet().stream()
				.collect(Collectors.toList());
		// loc lai list item cua tung nhan vien theo cac item lay duoc truoc do
		results.forEach(r -> {
			r.setItems(r.getItems().stream().filter(i -> attdanceIds.contains(i.getItemId())).collect(Collectors.toList()));
		});;
		if (results.size() > 0) {
			screenDto.getItemValues().addAll(results.get(0).getItems());
		}
		Map<String, MonthlyModifyResult> employeeDataMap = results.stream()
				.collect(Collectors.toMap(x -> x.getEmployeeId(), Function.identity(), (x, y) -> x));

		List<MPDataDto> lstData = new ArrayList<>(); // List all data
		List<MPCellStateDto> lstCellState = new ArrayList<>(); // List cell state
		screenDto.setLstData(lstData);
		screenDto.setLstCellState(lstCellState);

		Map<String, MonthlyPerformaceLockStatus> lockStatusMap = param.getLstLockStatus().stream()
				.collect(Collectors.toMap(x -> x.getEmployeeId(), Function.identity(), (x, y) -> x));
		String employeeIdLogin = AppContexts.user().employeeId();

		List<EditStateOfMonthlyPerformanceDto> editStateOfMonthlyPerformanceDtos = this.repo
				.findEditStateOfMonthlyPer(new YearMonth(screenDto.getProcessDate()), listEmployeeIds, attdanceIds);

		for (int i = 0; i < screenDto.getLstEmployee().size(); i++) {
			MonthlyPerformanceEmployeeDto employee = screenDto.getLstEmployee().get(i);
			String employeeId = employee.getId();
			MonthlyModifyResult rowData = employeeDataMap.get(employeeId);
			if (rowData == null) continue; //neu khong co data cua nhan vien thi bo qua
			
			String lockStatus = lockStatusMap.isEmpty() || !lockStatusMap.containsKey(employee.getId()) || param.getInitMenuMode() == 1 ? ""
					: lockStatusMap.get(employee.getId()).getLockStatusString();
			
			
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
			boolean identify = listConfirmationMonth.stream().filter(x->x.getEmployeeId().equals(employeeId)).findFirst().isPresent() ;

			// check true false approve
			boolean approve = false;
			if(approvalProcessingUseSetting.getUseMonthApproverConfirm()){
				if(param.getInitMenuMode() == 0 || param.getInitMenuMode() == 1){ //lay gia tri checkbox approve mode normal hoac unlock theo no.533
					//*10 
					if (approvalByListEmplAndListApprovalRecordDate != null) {
						for (AppRootSttMonthEmpImport approvalApprovalRecordDate : approvalByListEmplAndListApprovalRecordDate) {
							// 承認状況 ＝ 承認済 or 承認中 の場合
							if (approvalApprovalRecordDate.getEmployeeID().equals(employeeId)
									&& (approvalApprovalRecordDate
											.getApprovalStatus() == ApprovalStatusForEmployee.DURING_APPROVAL
									|| approvalApprovalRecordDate
											.getApprovalStatus() == ApprovalStatusForEmployee.APPROVED)) {
								approve = true;
								break;
							}
						}
					}
				} else if (param.getInitMenuMode() == 2) { //lay gia tri checkbox approve theo ket qua no.534
					//*8 
					if(approvalRootOfEmloyee!=null && approvalRootOfEmloyee.getApprovalRootSituations()!=null){
						for (AppRootSituationMonth approvalRootSituation : approvalRootOfEmloyee.getApprovalRootSituations()) {
							//◆基準社員の承認アクション　＝　承認した　の場合
							if(approvalRootSituation.getTargetID().equals(employeeId) && approvalRootSituation.getApprovalStatus().getApprovalActionByEmpl() == ApprovalActionByEmpl.APPROVALED){
								approve = true;
								break;
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

						if (pA.getAttendanceAtr() == 1) { // neu item loai thoi gian thi format lai dinh dang
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
						// *3 and *6
						if (param.getInitMenuMode() == 2) { // mode approve disable neu co bat ki lock nao khac month approve lock
							//if (!StringUtil.isNullOrEmpty(lockStatus, true) && !lockStatus.equals(MonthlyPerformaceLockStatus.LOCK_MONTHLY_APPROVAL))
							if (!StringUtil.isNullOrEmpty(lockStatus, true))
								cellStatus.add(STATE_DISABLE);	
							
						} else { // mode khac cu co lock la approve
							if (!StringUtil.isNullOrEmpty(lockStatus, true))
								cellStatus.add(STATE_DISABLE);
						}
						// Cell Data
						lstCellState.add(new MPCellStateDto(employeeId, attendanceKey, cellStatus));

						Optional<EditStateOfMonthlyPerformanceDto> dto = newList.stream()
								.filter(item2 -> item2.getAttendanceItemId().equals(item.getItemId())).findFirst();
						if (dto.isPresent()) { // set mau sua tay cho cell
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

		// screenDto.getItemValues().addAll(results.isEmpty() ? new
		// ArrayList<>() : results.get(0).getItems());
		// Cellstate
		/*
		 * List<MPCellStateDto> lstCellState = new ArrayList<>(); // //setting
		 * data List<MPDataDto> lstData = new ArrayList<>(); boolean stateLock =
		 * false; String empId; for (int i = 0; i <
		 * screenDto.getLstEmployee().size(); i++) { empId =
		 * screenDto.getLstEmployee().get(i).getId(); if(i % 2 == 0){
		 * lstCellState.add(new MPCellStateDto("_" + empId, "dailyperformace",
		 * Arrays.asList(STATE_DISABLE))); lstCellState.add(new
		 * MPCellStateDto("_" + empId, "identify",
		 * Arrays.asList(HAND_CORRECTION_MYSELF, STATE_DISABLE))); stateLock =
		 * true; }else{ stateLock = false; }
		 * 
		 * lstCellState.add(new MPCellStateDto("_" + empId, "time",
		 * Arrays.asList(STATE_ALARM, STATE_DISABLE)));
		 * 
		 * MPDataDto mpdata = new MPDataDto(empId, "stateLock", "",
		 * screenDto.getLstEmployee().get(i).getCode(),screenDto.getLstEmployee(
		 * ).get(i).getBusinessName(), "", stateLock, stateLock, stateLock, "");
		 * mpdata.addCellData(new MPCellDataDto("time", "11:" + i, "String",
		 * "")); lstData.add(mpdata); } screenDto.setLstCellState(lstCellState);
		 */
		// End dummy data
		// 社員ID（List）から社員コードと表示名を取得
		// Lấy employee code và tên hiển thị từ list employeeID
		// TODO Get data from 社員データ管理情報(Employee data management information)
		// SyEmployeePub

		// ドメインモデル「月別実績の勤怠時間」の取得

		// TODO ドメインモデル「月別実績の編集状態」すべて取得する

	}

	/**
	 * Get List Data include:<br/>
	 * Employee and Date
	 **/
//	private List<MPDataDto> getListData(List<MonthlyPerformanceEmployeeDto> listEmployee, DateRange dateRange,
//			Integer displayFormat) {
//		List<MPDataDto> result = new ArrayList<>();
//		if (listEmployee.size() > 0) {
//			List<GeneralDate> lstDate = dateRange.toListDate();
//			int dataId = 0;
//			for (int j = 0; j < listEmployee.size(); j++) {
//				MonthlyPerformanceEmployeeDto employee = listEmployee.get(j);
//				boolean stateLock = false;
//				// set tam dailyConfirm  = "〇"
//				String dailyConfirm = "〇";
//				for (int i = 0; i < lstDate.size(); i++) {
//					String key = displayFormat + "_" + convertFormatString(employee.getId()) + "_"
//							+ convertFormatString(converDateToString(lstDate.get(i))) + "_"
//							+ convertFormatString(converDateToString(lstDate.get(lstDate.size() - 1))) + "_" + dataId;
//					result.add(new MPDataDto(key, "stateLock", "", employee.getCode(), employee.getBusinessName(),
//							employee.getId(), "", stateLock, stateLock, dailyConfirm, ""));
//					dataId++;
//				}
//			}
//		}
//		return result;
//	}

//	private String convertFormatString(String data) {
//		return data.replace("-", "_");
//	}
//
//	private String converDateToString(GeneralDate genDate) {
//		Format formatter = new SimpleDateFormat(DATE_FORMAT);
//		return formatter.format(genDate.date());
//	}

	private String mergeString(String... x) {
		return StringUtils.join(x);
	}
	private void createFixedHeader(MonthlyPerformanceCorrectionDto screenDto, Integer yearMonth, Integer closureId,
			ApprovalProcessingUseSetting approvalProcessingUseSetting, String mess) {
		/**
		 * Create Grid Sheet DTO
		 */

		MPControlDisplayItem displayItem = screenDto.getLstControlDisplayItem();
//		MonthlyPerformanceParam param = screenDto.getParam();
		// アルゴリズム「対象年月に対応する月別実績を取得する」を実行する Lấy monthly result ứng với năm tháng
//		if (param.getLstAtdItemUnique() == null || param.getLstAtdItemUnique().isEmpty()) {
//			throw new BusinessException("Msg_1261");
//		}
//
//		List<MPSheetDto> lstSheets = param.getSheets().stream().map(c -> {
//			MPSheetDto sh = new MPSheetDto(c.getSheetNo(), c.getSheetName());
//			for (PAttendanceItem attend : c.getDisplayItems()) {
//				sh.addColumn(mergeString(ADD_CHARACTER, attend.getId().toString()));
//			}
//			return sh;
//		}).collect(Collectors.toList());
//		displayItem.createSheets(lstSheets);

		List<MPHeaderDto> lstMPHeaderDto = MPHeaderDto.GenerateFixedHeader();
		
		//G7 G8 G9 hidden column identitfy, approval, dailyconfirm
		for (Iterator<MPHeaderDto> iter = lstMPHeaderDto.listIterator(); iter.hasNext(); ) {
			MPHeaderDto mpHeaderDto = iter.next();
			if ("identify".equals(mpHeaderDto.getKey())
					&& screenDto.getIdentityProcess().getUseMonthSelfCK() == 0) {
		        iter.remove();
		        continue;
		    }
			if ("approval".equals(mpHeaderDto.getKey())
					&& approvalProcessingUseSetting.getUseMonthApproverConfirm() == false) {
				iter.remove();
				continue;
			}
			if ("dailyconfirm".equals(mpHeaderDto.getKey())
					&& screenDto.getDailySelfChkDispAtr() == 0) {
		        iter.remove();
		        continue;
		    }
		}
		List<MPHeaderDto> lstHeader = new ArrayList<>();
		lstHeader.addAll(lstMPHeaderDto);
		displayItem.setLstHeader(lstHeader);
		// Fixed header
		screenDto.setLstFixedHeader(lstMPHeaderDto);
		screenDto.setMess(mess);
		}
}

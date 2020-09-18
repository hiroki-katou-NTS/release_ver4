package nts.uk.screen.at.app.ktgwidget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.error.BusinessException;
import nts.arc.layer.app.cache.CacheCarrier;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.auth.app.find.employmentrole.InitDisplayPeriodSwitchSetFinder;
import nts.uk.ctx.at.auth.dom.adapter.workplace.AuthWorkPlaceAdapter;
import nts.uk.ctx.at.auth.dom.adapter.workplace.WorkplaceInfoImport;
import nts.uk.ctx.at.auth.dom.employmentrole.EmployeeReferenceRange;
import nts.uk.ctx.at.function.dom.adapter.employeebasic.SyEmployeeFnAdapter;
import nts.uk.ctx.at.record.dom.approvalmanagement.dailyperformance.algorithm.closure.ClosureHistPeriod;
import nts.uk.ctx.at.record.dom.approvalmanagement.dailyperformance.algorithm.closure.GetSpecifyPeriod;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.export.AgreementTimeDetail;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.export.GetAgreementTime;
import nts.uk.ctx.at.record.dom.require.RecordDomRequireService;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.EmployeeRequestAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.dto.SWkpHistImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.closure.CurrentClosingPeriodExport;
import nts.uk.ctx.at.shared.app.query.workrule.closure.ClosureResultModel;
import nts.uk.ctx.at.shared.app.query.workrule.closure.WorkClosureQueryProcessor;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmpEmployeeAdapter;
import nts.uk.ctx.at.shared.dom.adapter.employee.PersonEmpBasicInfoImport;
import nts.uk.ctx.at.shared.dom.adapter.employment.BsEmploymentHistoryImport;
import nts.uk.ctx.at.shared.dom.adapter.employment.ShareEmploymentAdapter;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmployment;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.UseClassification;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.ctx.bs.employee.dom.employee.history.AffCompanyHist;
import nts.uk.ctx.bs.employee.dom.employee.history.AffCompanyHistByEmployee;
import nts.uk.ctx.bs.employee.dom.employee.service.SearchEmployeeService;
import nts.uk.ctx.bs.employee.dom.temporaryabsence.TempAbsHistRepository;
import nts.uk.ctx.bs.employee.dom.temporaryabsence.TempAbsenceHistory;
import nts.uk.ctx.bs.employee.pub.workplace.master.WorkplacePub;
import nts.uk.screen.at.app.ktgwidget.find.dto.AcquisitionOfOvertimeHoursOfEmployeesDto;
import nts.uk.screen.at.app.ktgwidget.find.dto.AgreementTimeList36;
import nts.uk.screen.at.app.ktgwidget.find.dto.AgreementTimeOfMonthlyDto;
import nts.uk.screen.at.app.ktgwidget.find.dto.OvertimeHours;
import nts.uk.screen.at.app.ktgwidget.find.dto.OvertimeHoursDto;
import nts.uk.screen.at.app.ktgwidget.find.dto.OvertimedDisplayForSuperiorsDto;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class KTG027QueryProcessor {
	@Inject
	private ShareEmploymentAdapter shareEmploymentAdapter;
	@Inject
	private ClosureEmploymentRepository closureEmploymentRepo;

	@Inject
	private AuthWorkPlaceAdapter authWorkPlaceAdapter;

	@Inject
	private WorkClosureQueryProcessor workClosureQueryProcessor;

	@Inject
	private ClosureRepository closureRepository;
	
	@Inject
	private SearchEmployeeService searchEmployeeService;

	@Inject
	private EmpEmployeeAdapter empEmployeeAdapter;

	@Inject
	private RecordDomRequireService requireService;

	@Inject
	private OvertimeHoursRepository overtimeHoursRepo;
	
	@Inject
	private TempAbsHistRepository  tempAbsHistRepository;
	
	@Inject
	private GetSpecifyPeriod getSpecifyPeriod;
	
	@Inject
	private InitDisplayPeriodSwitchSetFinder displayPeriodfinder;
	
	@Inject
	private EmployeeRequestAdapter employeeAdapter;
	
	@Inject
	private WorkplacePub workplacePub;
	
	@Inject
	private SyEmployeeFnAdapter syEmployeeFnAdapter;

	public GeneralDate checkSysDateOrCloseEndDate() {
		// EA luôn trả v�Systemdate
		return GeneralDate.today();
	}

	public int getLoginClosure(GeneralDate referenceDate) {
		String companyID = AppContexts.user().companyId();
		String employeeID = AppContexts.user().employeeId();
		// ログイン�雔�を取得す�
		// (Lấy employeement của login) request list 31
		Optional<BsEmploymentHistoryImport> employmentHistoryImport = shareEmploymentAdapter.findEmploymentHistory(companyID, employeeID, referenceDate);
		if (!employmentHistoryImport.isPresent()) {
			// If fail return closure ID = 1
			return 1;
		} else {
			Optional<ClosureEmployment> closureEmploymentOpt = closureEmploymentRepo.findByEmploymentCD(companyID, employmentHistoryImport.get().getEmploymentCode());
			if (!closureEmploymentOpt.isPresent()) {
				throw new RuntimeException("Not found Employment history by employeeCD:" + employmentHistoryImport.get().getEmploymentCode());
			}
			return closureEmploymentOpt.get().getClosureId();
		}
	}

	public Optional<DatePeriod> getDatePeriod(int targetMonth, int closureID) {
		String companyID = AppContexts.user().companyId();
		List<DatePeriod> optDatePeriod = new ArrayList<>();
		Optional<Closure> optClosure = closureRepository.findByIdAndUseAtr(companyID, closureID, UseClassification.UseClass_Use);
		if (optClosure.isPresent()) {
			optDatePeriod = optClosure.get().getPeriodByYearMonth(new YearMonth(targetMonth));
		}

		return optDatePeriod.isEmpty() ? Optional.empty() : Optional.of(optDatePeriod.get(0));
	}

	public OvertimeHours buttonPressingProcess(int targetMonth, int closureID) {

		String employeeID = AppContexts.user().employeeId();
		String companyID = AppContexts.user().companyId();
		// (Lấy closing period) tu domain Closure
		/*
		 * ouput: Closing period start date End date
		 */
		Optional<DatePeriod> datePeriod = getDatePeriod(targetMonth, closureID);
		if (!datePeriod.isPresent()) {
			throw new BusinessException("Msg_1134");
		}
		// (Lấy workplace trong phạm vi quyền login) = Request List 478
		// referenceDate lấy theo baseDate �xử lý 1
		GeneralDate referenceDate = checkSysDateOrCloseEndDate();
		//全社員参照　＝　しない
		// referEmployee = false
		WorkplaceInfoImport workplaceInfoImport = authWorkPlaceAdapter.getWorkplaceListId(referenceDate, employeeID, false);
		List<String> listWorkPlaceID = workplaceInfoImport.getLstWorkPlaceID();
		Integer employeeRange = workplaceInfoImport.getEmployeeRange(); 
		if (listWorkPlaceID == null ) {
			throw new BusinessException("Msg_1135");
		}
		// (lấy employeement ứng với closing)
		List<ClosureEmployment> listClosureEmployment = closureEmploymentRepo.findByClosureId(companyID, closureID);
		if (listClosureEmployment == null || listClosureEmployment.isEmpty()) {
			throw new BusinessException("Msg_1136");
		}
		List<AgreementTimeList36> data = new ArrayList<>();
		
		// (Lấy target) Lấy request list 335
		List<String> listEmploymentCD = listClosureEmployment.stream().map(c -> c.getEmploymentCD()).collect(Collectors.toList());
		//Dòng code bị comment là code gốc của RQ 335
		//List<String> listEmpID = empEmployeeAdapter.getListEmpByWkpAndEmpt(listWorkPlaceID, listEmploymentCD, datePeriod.get());
		List<String> listEmpID = getListEmpByWkpAndEmpt(listWorkPlaceID, listEmploymentCD, datePeriod.get());
		
		if (listEmpID == null || listEmpID.isEmpty() ) {
			throw new BusinessException("Msg_1137");
		}
		
		if(employeeRange == EmployeeReferenceRange.ONLY_MYSELF.value){
			//取得した対象者にログイン者がい含まれているか判定する
			if(listEmpID.contains(employeeID) == false){
				//含まれていない
				throw new BusinessException("Msg_1137");
			}else{
				listEmpID = new ArrayList<>();
				listEmpID.add(employeeID);
			}
		}
		
		// (Lấy worktime ngoài period) lấy RequestList 333
		List<AgreementTimeDetail> listAgreementTimeDetail = GetAgreementTime.get(
				requireService.createRequire(), new CacheCarrier(),
				companyID, listEmpID, YearMonth.of(targetMonth), ClosureId.valueOf(closureID));
		
		if (listAgreementTimeDetail.isEmpty()) {
			throw new BusinessException("Msg_1138");
		}
		
		for (AgreementTimeDetail agreementTimeDetail : listAgreementTimeDetail){
			if (agreementTimeDetail.getErrorMessage().isPresent()){
				throw new BusinessException("Msg_1138");
			}
		}
		
		// (Set thông tin công việc ngoài giờ đã lấy)
		List<String> lstEmpID = listAgreementTimeDetail.stream().map(c -> c.getEmployeeId()).collect(Collectors.toList());
		// Lay Request61
		List<PersonEmpBasicInfoImport> listEmpBasicInfoImport = empEmployeeAdapter.getPerEmpBasicInfo(lstEmpID);
		for (AgreementTimeDetail agreementTimeDetail : listAgreementTimeDetail) {
			Optional<PersonEmpBasicInfoImport> personInfor = listEmpBasicInfoImport.stream().filter(c -> c.getEmployeeId().equals(agreementTimeDetail.getEmployeeId())).findFirst();
			if (!personInfor.isPresent()) {
				break;
			}
			AgreementTimeList36 agreementTimeList36 = new AgreementTimeList36(personInfor.get().getEmployeeCode(), personInfor.get().getBusinessName(), null,
					new AgreementTimeOfMonthlyDto(!agreementTimeDetail.getConfirmed().isPresent()? 0 : agreementTimeDetail.getConfirmed().get().getAgreementTime().v(),
							!agreementTimeDetail.getConfirmed().isPresent() ? 0 : agreementTimeDetail.getConfirmed().get().getLimitErrorTime().v(),
							!agreementTimeDetail.getConfirmed().isPresent() ? 0 : agreementTimeDetail.getConfirmed().get().getLimitAlarmTime().v(),
							!agreementTimeDetail.getConfirmed().isPresent() ? 0 : (!agreementTimeDetail.getConfirmed().get().getExceptionLimitErrorTime().isPresent() ? agreementTimeDetail.getConfirmed().get().getLimitErrorTime().v() :agreementTimeDetail.getConfirmed().get().getExceptionLimitErrorTime().get().v()),
							!agreementTimeDetail.getConfirmed().isPresent() ? 0 : (!agreementTimeDetail.getConfirmed().get().getExceptionLimitAlarmTime().isPresent() ? 0 :agreementTimeDetail.getConfirmed().get().getExceptionLimitAlarmTime().get().v()),
							!agreementTimeDetail.getConfirmed().isPresent() ? 0 : agreementTimeDetail.getConfirmed().get().getStatus().value),
					new AgreementTimeOfMonthlyDto(!agreementTimeDetail.getAfterAppReflect().isPresent()? 0 : agreementTimeDetail.getAfterAppReflect().get().getAgreementTime().v(),
							!agreementTimeDetail.getAfterAppReflect().isPresent() ? 0 : agreementTimeDetail.getAfterAppReflect().get().getLimitErrorTime().v(),
							!agreementTimeDetail.getAfterAppReflect().isPresent() ? 0 : agreementTimeDetail.getAfterAppReflect().get().getLimitAlarmTime().v(),
							!agreementTimeDetail.getAfterAppReflect().isPresent() ? 0 : (!agreementTimeDetail.getAfterAppReflect().get().getExceptionLimitErrorTime().isPresent() ? 0 :agreementTimeDetail.getAfterAppReflect().get().getExceptionLimitErrorTime().get().v()),
							!agreementTimeDetail.getAfterAppReflect().isPresent() ? 0 : (!agreementTimeDetail.getAfterAppReflect().get().getExceptionLimitAlarmTime().isPresent() ? 0 :agreementTimeDetail.getAfterAppReflect().get().getExceptionLimitAlarmTime().get().v()),
							!agreementTimeDetail.getAfterAppReflect().isPresent() ? 0 : agreementTimeDetail.getAfterAppReflect().get().getStatus().value));
			data.add(agreementTimeList36);
		}
		
		data.sort((a, b) ->
			 {
				 if(a.getAfterAppReflect().getAgreementTime()== b.getAfterAppReflect().getAgreementTime()){
					 return a.getEmployeeCD().compareTo(b.getEmployeeCD());
				 }else{
					 return  b.getAfterAppReflect().getAgreementTime()-a.getAfterAppReflect().getAgreementTime();
				 }
			}
		);

		return new OvertimeHours(null, data);
	}

	public boolean displayItem(List<AgreementTimeDetail> listAgreementTimeDetail, GeneralDate targetMonth) {
		// �６協定申請を使用するかどぁ�チェヂ�(Check có sủ dụng đơn xin hiệp ddinh36 ko)
		// 一旦常に「使用する�mặc định la 「sử dungj - 使用する�)
		// �６協定申請を使用する権限があるかどぁ�チェヂ�(check xem có quyển sử dụng đơn hiệp định 36
		// ko) 一旦常に「使用する�mặc định la 「sử dungj - 使用する�)
		// 対象月が過去月かどぁ�チェヂ�(Check targetMonth có phải l� tháng quá khứ ko)
		for (AgreementTimeDetail agreeTime : listAgreementTimeDetail) {
			if (!GeneralDate.today().after(targetMonth) == true) {
				if (agreeTime.getAfterAppReflect().get().getStatus().value == 1 || agreeTime.getAfterAppReflect().get().getStatus().value == 2) {
					return true;
				}
				return true;
			} else
				return false;
		}
		return false;

	}

	public List<ClosureResultModel> getListClosure() {
		GeneralDate referenceDate = checkSysDateOrCloseEndDate();
		List<ClosureResultModel> listClosureResultModel = workClosureQueryProcessor.findClosureByReferenceDate(referenceDate);
		return listClosureResultModel;
	}

	// get request 335
	public List<String> getListEmpByWkpAndEmpt(List<String> wkpsId, List<String> lstemptsCode, DatePeriod dateperiod) {
		// lấy List workplace history items từ dateperiod and list workplaceId
		
		List<String> lstEmpIdOfWkp = overtimeHoursRepo.getAffWkpHistItemByListWkpIdAndDatePeriod(dateperiod, wkpsId);
		if (lstEmpIdOfWkp.isEmpty()) {
			return Collections.emptyList();
		}
		
		List<String> lstEmpIdOfEmpt = overtimeHoursRepo.getListEmptByListCodeAndDatePeriod(dateperiod, lstemptsCode);
		if (lstEmpIdOfEmpt.isEmpty()) {
			return Collections.emptyList();
		}
		// lấy list sid chung từ 2 list lstEmpIdOfWkp vs lstEmpIdOfEmpt
		List<String> lstId = lstEmpIdOfWkp.stream().filter(lstEmpIdOfEmpt::contains).collect(Collectors.toList());

		if (lstId.isEmpty()) {
			return Collections.emptyList();
		}
		// lây list Employee từ list sid v� dateperiod
		long startTime2 = System.nanoTime();
		List<AffCompanyHist> lstAffComHist = overtimeHoursRepo.getAffComHisEmpByLstSidAndPeriod(lstId, dateperiod);
		long endTime2 = System.nanoTime();	
		long duration2 = (endTime2 - startTime2) / 1000000; // ms;
		System.out.println("Time113:" + duration2);		
		if (lstAffComHist.isEmpty()) {
			return Collections.emptyList();
		}
		List<AffCompanyHistByEmployee> lstAffComHistByEmp = getAffCompanyHistByEmployee(lstAffComHist);

		// List sid from List<AffCompanyHistByEmployee>
		List<String> lstSidFromAffComHist = lstAffComHistByEmp.stream().map(m -> m.getSId())
				.collect(Collectors.toList());
		
		// (Lấy danh sách sid từ domain 「休職休業履歴」) 		
		List<String> lstSidAbsHist_NoCheckDate = tempAbsHistRepository.getByListSid(lstSidFromAffComHist);
		
		// List sid tồn tại ở lstId nhưng không tồn tại ở list sid
		List<String> result = lstSidFromAffComHist.stream().filter(i -> !lstSidAbsHist_NoCheckDate.contains(i))
				.collect(Collectors.toList());

		// lây list TempAbsenceHistory từ list sid và dateperiod
		List<TempAbsenceHistory> lstTempAbsenceHistory = tempAbsHistRepository.getByListSid(lstSidAbsHist_NoCheckDate,
				dateperiod);

		// List sid from List<TempAbsenceHistory>
		List<String> lstSidFromTempAbsHis = lstTempAbsenceHistory.stream().map(m -> m.getEmployeeId())
				.collect(Collectors.toList());

		if(!lstSidFromTempAbsHis.isEmpty()) {
			result.addAll(lstSidFromTempAbsHis);
		}
		
		if (result.isEmpty()) {
			return Collections.emptyList();
		}
		
		return result;
	}
	public List<AffCompanyHistByEmployee> getAffCompanyHistByEmployee(List<AffCompanyHist> lstAffComHist) {
		if (lstAffComHist.isEmpty()) {
			return Collections.emptyList();
		}
		List<AffCompanyHistByEmployee> result = new ArrayList<>();

		lstAffComHist.forEach(m -> {
			result.addAll(m.getLstAffCompanyHistByEmployee());
		});
		return result;
		
	}


	public OvertimeHoursDto initialActivationArocess(int targetMonth) {
		// 基準日を取得す�
		// (Lấy base date)
		GeneralDate referenceDate = checkSysDateOrCloseEndDate();
		// ログイン��めを取得す�
		// (Lấy closing của login)
		int closureIDInit = getLoginClosure(referenceDate);
		// �め�一覧を取得す�
		// (Lấy list closing) Request 142
		List<ClosureResultModel> listClosureResultModel = workClosureQueryProcessor.findClosureByReferenceDate(referenceDate);
		// 時間外労働時間を取得す�
		// (Lấy work time )
		OvertimeHours overtimeHours = buttonPressingProcess(targetMonth, closureIDInit);
		OvertimeHoursDto reusult = new OvertimeHoursDto(closureIDInit, listClosureResultModel, overtimeHours);
		return reusult;
	}
	
	/**
	 * UKDesign.UniversalK.就業.KTG_ウィジェット.KTG027_時間外労働時間の表示(上長用).ユースケース.起動する(Khởi động).システム.起動する
	 * @return
	 * @param currentOrNextMonth
	 */
	public OvertimedDisplayForSuperiorsDto getOvertimeDisplayForSuperiorsDto(int currentOrNextMonth) {
		val require = requireService.createRequire();
		val cacheCarrier = new CacheCarrier();
		String sID = AppContexts.user().employeeId();
		GeneralDate baseDate = GeneralDate.today();
		// 社員に対応する処理締めを取得する
		Closure closure = ClosureService.getClosureDataByEmployee(require, cacheCarrier, sID, baseDate);
		OvertimedDisplayForSuperiorsDto result = OvertimedDisplayForSuperiorsDto.builder().build();
		// 指定した年月の締め期間を取得する
		List<ClosureHistPeriod> lstClosure = getSpecifyPeriod
				.getSpecifyPeriod(closure.getClosureMonth().getProcessingYm());
		// 上長用の時間外時間表示．当月の締め情報．処理年月＝取得したドメインモデル「締め」．当月
		// 上長用の時間外時間表示．当月の締め情報．締め開始日＝取得した締め期間．開始日
		// 上長用の時間外時間表示．当月の締め情報．締め終了日＝取得した締め期間．終了日
		// 上長用の時間外時間表示．ログイン者の締めID＝取得したドメインモデル「締め」．締めID
		CurrentClosingPeriodExport closingInformationForCurrentMonth = CurrentClosingPeriodExport.builder()
				.processingYm(closure.getClosureMonth().getProcessingYm().v())
				.closureEndDate(lstClosure.get(0).getPeriod().end().toString())
				.closureStartDate(lstClosure.get(0).getPeriod().start().toString()).build();
		result.setClosingInformationForCurrentMonth(closingInformationForCurrentMonth);
		result.setClosureId(closure.getClosureId().value);
		// INPUT．表示年月＝当月表示
		if (currentOrNextMonth == 1) {
			// 対象年月を指定するログイン者の配下社員の時間外時間の取得
			AcquisitionOfOvertimeHoursOfEmployeesDto acquisitionOfOvertimeHoursOfEmployeesDto = this
					.getAcquisitionOfOvertimeHoursOfEmployeesDto(closure.getClosureId().value,
							closure.getClosureMonth().getProcessingYm(), Optional.of(lstClosure.get(0).getPeriod()));
			// 上長用の時間外時間表示．配下社員の個人情報＝取得したList＜個人社員基本情報＞
			// 上長用の時間外時間表示．配下社員の時間外時間＝取得したList＜管理期間の36協定時間＞

			result.setOvertimeOfSubordinateEmployees(
					acquisitionOfOvertimeHoursOfEmployeesDto.getOvertimeOfSubordinateEmployees());
			result.setPersonalInformationOfSubordinateEmployees(
					acquisitionOfOvertimeHoursOfEmployeesDto.getPersonalInformationOfSubordinateEmployees());

		} else {
			// INPUT．表示年月＝翌月表示
			// 指定した年月の期間を算出する
			DatePeriod datePeriodClosure = ClosureService.getClosurePeriod(requireService.createRequire(),
					result.getClosureId(), closure.getClosureMonth().getProcessingYm().addMonths(1));
			// 上長用の時間外時間表示．翌月の締め情報．処理年月＝上長用の時間外時間表示．当月の締め情報．処理年月．AddMonth(1)
			// 上長用の時間外時間表示．翌月の締め情報．締め期間＝取得した締め期間
			CurrentClosingPeriodExport closingInformationForNextMonth = CurrentClosingPeriodExport.builder()
					.processingYm(closure.getClosureMonth().getProcessingYm().addMonths(1).v())
					.closureEndDate(datePeriodClosure.end().toString())
					.closureStartDate(datePeriodClosure.start().toString()).build();
			result.setClosingInformationForNextMonth(closingInformationForNextMonth);
			// /対象年月を指定するログイン者の配下社員の時間外時間の取得
			AcquisitionOfOvertimeHoursOfEmployeesDto acquisitionOfOvertimeHoursOfEmployeesDto = this
					.getAcquisitionOfOvertimeHoursOfEmployeesDto(closure.getClosureId().value,
							closure.getClosureMonth().getProcessingYm().addMonths(1),
							Optional.ofNullable(datePeriodClosure));
			// 上長用の時間外時間表示．配下社員の個人情報＝取得したList＜個人社員基本情報＞
			// 上長用の時間外時間表示．配下社員の時間外時間＝取得したList＜管理期間の36協定時間＞
			result.setOvertimeOfSubordinateEmployees(
					acquisitionOfOvertimeHoursOfEmployeesDto.getOvertimeOfSubordinateEmployees());
			result.setPersonalInformationOfSubordinateEmployees(
					acquisitionOfOvertimeHoursOfEmployeesDto.getPersonalInformationOfSubordinateEmployees());
		}

		return result;
	}
	
	/**
	 * UKDesign.UniversalK.就業.KTG_ウィジェット.KTG027_時間外労働時間の表示(上長用).アルゴリズム.起動する.対象年月を指定するログイン者の配下社員の時間外時間の取得
	 * @param closureId
	 * @param targetDate
	 * @param referencePeriod
	 * @return
	 */
	public AcquisitionOfOvertimeHoursOfEmployeesDto getAcquisitionOfOvertimeHoursOfEmployeesDto(int closureId,
			YearMonth targetDate, Optional<DatePeriod> referencePeriodParam) {
		String cID = AppContexts.user().companyId();
		String sID = AppContexts.user().employeeId();
		Optional<DatePeriod> referencePeriod;
		List<AgreementTimeList36> lstAgreementTimeList36 = new ArrayList<AgreementTimeList36>();
		if (referencePeriodParam.isPresent()) {
			// 基準期間＝INPUT．基準期間
			referencePeriod = referencePeriodParam;
		} else {
			// 指定した年月の期間を算出する
			DatePeriod datePeriodClosure = ClosureService.getClosurePeriod(requireService.createRequire(), closureId,
					targetDate);
			// 基準期間＝取得した締め期間
			referencePeriod = Optional.ofNullable(datePeriodClosure);
		}
		// [RQ30]社員所属職場履歴を取得
		SWkpHistImport sWkpHistImport = employeeAdapter.getSWkpHistByEmployeeID(sID, referencePeriod.get().end());

		// [No.573]職場の下位職場を基準職場を含めて取得する
		List<String> lstWorkPlaceId = workplacePub.getAllChildrenOfWorkplaceId(cID, referencePeriod.get().end(),
				sWkpHistImport.getWorkplaceId());

		// 期間内に特定の職場（List）に所属している社員一覧を取得
		List<String> lstEmployeeId = syEmployeeFnAdapter.getListEmployeeId(lstWorkPlaceId, referencePeriod.get());

		// 社員ID(List)から個人社員基本情報を取得
		List<PersonEmpBasicInfoImport> listPersonEmp = empEmployeeAdapter.getPerEmpBasicInfo(lstEmployeeId);
		// 指定する年月と指定する社員の時間外時間の取得
		List<AgreementTimeDetail> listAgreementTimeDetail = this.getOvertimeByEmployee(lstEmployeeId, targetDate,
				referencePeriod.get().end());

		for (AgreementTimeDetail item : listAgreementTimeDetail) {
			Optional<PersonEmpBasicInfoImport> personInfor = listPersonEmp.stream()
					.filter(c -> c.getEmployeeId().equals(item.getEmployeeId())).findFirst();
			if (!personInfor.isPresent()) {
				break;
			}
			AgreementTimeList36 agreementTimeList36 = new AgreementTimeList36(item.getEmployeeId(), personInfor.get().getBusinessName(), null,
					new AgreementTimeOfMonthlyDto(!item.getConfirmed().isPresent()? 0 : item.getConfirmed().get().getAgreementTime().v(),
							!item.getConfirmed().isPresent() ? 0 : item.getConfirmed().get().getLimitErrorTime().v(),
							!item.getConfirmed().isPresent() ? 0 : item.getConfirmed().get().getLimitAlarmTime().v(),
							!item.getConfirmed().isPresent() ? 0 : (!item.getConfirmed().get().getExceptionLimitErrorTime().isPresent() ? item.getConfirmed().get().getLimitErrorTime().v() :item.getConfirmed().get().getExceptionLimitErrorTime().get().v()),
							!item.getConfirmed().isPresent() ? 0 : (!item.getConfirmed().get().getExceptionLimitAlarmTime().isPresent() ? 0 :item.getConfirmed().get().getExceptionLimitAlarmTime().get().v()),
							!item.getConfirmed().isPresent() ? 0 : item.getConfirmed().get().getStatus().value),
					new AgreementTimeOfMonthlyDto(!item.getAfterAppReflect().isPresent()? 0 : item.getAfterAppReflect().get().getAgreementTime().v(),
							!item.getAfterAppReflect().isPresent() ? 0 : item.getAfterAppReflect().get().getLimitErrorTime().v(),
							!item.getAfterAppReflect().isPresent() ? 0 : item.getAfterAppReflect().get().getLimitAlarmTime().v(),
							!item.getAfterAppReflect().isPresent() ? 0 : (!item.getAfterAppReflect().get().getExceptionLimitErrorTime().isPresent() ? 0 :item.getAfterAppReflect().get().getExceptionLimitErrorTime().get().v()),
							!item.getAfterAppReflect().isPresent() ? 0 : (!item.getAfterAppReflect().get().getExceptionLimitAlarmTime().isPresent() ? 0 :item.getAfterAppReflect().get().getExceptionLimitAlarmTime().get().v()),
							!item.getAfterAppReflect().isPresent() ? 0 : item.getAfterAppReflect().get().getStatus().value));
			lstAgreementTimeList36.add(agreementTimeList36);
		}
		// OUTPUT：
		// List＜個人社員基本情報＞
		// List＜管理期間の36協定時間＞
		AcquisitionOfOvertimeHoursOfEmployeesDto result = AcquisitionOfOvertimeHoursOfEmployeesDto.builder()
				.personalInformationOfSubordinateEmployees(listPersonEmp)
				.OvertimeOfSubordinateEmployees(lstAgreementTimeList36).build();
		return result;
	}
	 
	 /**
	 * UKDesign.UniversalK.就業.KTG_ウィジェット.KTG027_時間外労働時間の表示(上長用).アルゴリズム.対象年月を指定するログイン者の配下社員の時間外時間の取得.指定する年月と指定する社員の時間外時間の取得
	 * @param lstEmployeeId
	 * @param targetDate
	 * @param endDate
	 * @return
	 */ 
	public List<AgreementTimeDetail> getOvertimeByEmployee(List<String> lstEmployeeId, YearMonth targetDate,
			GeneralDate endDate) {
		val require = requireService.createRequire();
		val cacheCarrier = new CacheCarrier();
		String cId = AppContexts.user().companyId();
		GeneralDate baseDate = GeneralDate.today();
		List<AgreementTimeDetail> listAgreementTimeDetail = new ArrayList<AgreementTimeDetail>();
		for (String empCode : lstEmployeeId) {
			// 社員に対応する処理締めを取得する
			Closure closure = ClosureService.getClosureDataByEmployee(require, cacheCarrier, empCode, baseDate);
			// [取得したドメインモデル「締め」．当月<=INPUT．対象年月]がtrue
			if (closure != null) {
				if (closure.getClosureMonth().getProcessingYm().lessThanOrEqualTo(targetDate)) {
					// sau khi có update cần phải sửa
					// 【NO.333】36協定時間の取得:
					List<AgreementTimeDetail> lstAgreementTimeDetail = GetAgreementTime.get(
							requireService.createRequire(), new CacheCarrier(), cId, lstEmployeeId, targetDate,
							closure.getClosureId());
					if (lstAgreementTimeDetail.isEmpty()) {
						throw new RuntimeException("ListAgreementTimeDetailRQ333 Empty");
					}
					for (AgreementTimeDetail item : lstAgreementTimeDetail) {
						listAgreementTimeDetail.add(item);
					}
				} else {
					// 【NO.333】36協定時間の取得:
					List<AgreementTimeDetail> lstAgreementTimeDetail = GetAgreementTime.get(
							requireService.createRequire(), new CacheCarrier(), cId, lstEmployeeId, targetDate,
							closure.getClosureId());
					if (lstAgreementTimeDetail.isEmpty()) {
						throw new RuntimeException("ListAgreementTimeDetailRQ333 Empty");
					}
					for (AgreementTimeDetail item : lstAgreementTimeDetail) {
						listAgreementTimeDetail.add(item);
					}
				}
			}
		}

		// sủa lại sau khi có update 30/09
		return listAgreementTimeDetail;
	}
}

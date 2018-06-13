/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.function.app.find.dailyworkschedule.scrA;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.function.app.find.dailyworkschedule.DataInforReturnDto;
import nts.uk.ctx.at.function.app.find.dailyworkschedule.scrB.ErrorAlarmCodeDto;
import nts.uk.ctx.at.function.dom.dailyworkschedule.OutputItemDailyWorkSchedule;
import nts.uk.ctx.at.function.dom.dailyworkschedule.OutputItemDailyWorkScheduleRepository;
import nts.uk.ctx.at.function.dom.dailyworkschedule.scrA.SEmpHistExportAdapter;
import nts.uk.ctx.at.function.dom.dailyworkschedule.scrA.SEmpHistExportImported;
import nts.uk.ctx.at.function.dom.holidaysremaining.PermissionOfEmploymentForm;
import nts.uk.ctx.at.function.dom.holidaysremaining.repository.PermissionOfEmploymentFormRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.ErrorAlarmWorkRecord;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.ErrorAlarmWorkRecordRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmployment;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Class WorkScheduleOutputConditionFinder.
 * @author HoangDD
 */
@Stateless
public class WorkScheduleOutputConditionFinder {
	
	/** The permission of employment form repository. */
	@Inject
	private PermissionOfEmploymentFormRepository permissionOfEmploymentFormRepository;
	
	/** The closure employment repository. */
	@Inject
	private ClosureEmploymentRepository closureEmploymentRepository;
	
	/** The s emp hist export adapter. */
	@Inject
	private SEmpHistExportAdapter sEmpHistExportAdapter;
	
	/** The closure repository. */
	@Inject
	private ClosureRepository closureRepository;
	
	/** The closure service. */
	@Inject
	private ClosureService closureService;
	
	/** The output item daily work schedule repository. */
	@Inject
	private OutputItemDailyWorkScheduleRepository outputItemDailyWorkScheduleRepository;
	
	/** The error alarm work record repository. */
	@Inject
	private ErrorAlarmWorkRecordRepository errorAlarmWorkRecordRepository;
	
	/** The Constant STRING_EMPTY. */
	private static final String STRING_EMPTY = "";
	
	/** The Constant FUNCTION_NO. */
	private static final Integer FUNCTION_NO = 2;
	
	/** The Constant SHOW_CHARACTERISTIC. */
	private static final String SHOW_CHARACTERISTIC = "SHOW_CHARACTERISTIC";
	
	/** The Constant OPEN_SCREEN_C. */
	private static final String OPEN_SCREEN_C = "Open_ScrC";
	
	/**
	 * Start scr.
	 *
	 * @param isExistWorkScheduleOutputCondition the is exist work schedule output condition
	 * @return the work schedule output condition dto
	 */
	public WorkScheduleOutputConditionDto startScr(boolean isExistWorkScheduleOutputCondition) {
		
		WorkScheduleOutputConditionDto dto = new WorkScheduleOutputConditionDto();
		
		String companyId = AppContexts.user().companyId();
		String roleId = AppContexts.user().roles().forAttendance();
		GeneralDate systemDate = GeneralDate.today();
		String employeeId = AppContexts.user().employeeId();
		
		// ドメインモデル「就業帳票の権限」を取得する(Acquire the domain model "authority of employment form")
		Optional<PermissionOfEmploymentForm> optPermissionOfEmploymentForm = permissionOfEmploymentFormRepository.find(companyId, roleId, FUNCTION_NO);
		if (optPermissionOfEmploymentForm.isPresent()) {
			if (!optPermissionOfEmploymentForm.get().isAvailable()) {
				optPermissionOfEmploymentForm = Optional.empty();
			}
			dto.setExistAuthority(true);
		} else {
			dto.setExistAuthority(false);
		}
		
		// アルゴリズム「社員に対応する締め期間を取得する」を実行する(Execute the algorithm "Acquire closing period corresponding to employee")
		Optional<Closure> optClosure = getDomClosure(employeeId, systemDate);
		
		// 「締め」がある
		if (optClosure.isPresent()) {
			Closure closure = optClosure.get();
			dto.setMsgErrClosingPeriod(null);
			
			// アルゴリズム「当月の期間を算出する」を実行する(Execute the algorithm "Calculate the period of the current month")
			DatePeriod datePeriod = closureService.getClosurePeriodNws(closure.getClosureId().value, closure.getClosureMonth().getProcessingYm());
			dto.setStartDate(datePeriod.start());
			dto.setEndDate(datePeriod.end());
		} else {
			// 「締め」がない
			dto.setMsgErrClosingPeriod("Msg_1134");
			
			dto.setStartDate(null);
			dto.setEndDate(null);
		}

		// ドメインモデル「日別勤務表の出力項目」をすべて取得する(Acquire all domain model "Output items of daily work schedule")
		List<OutputItemDailyWorkSchedule> lstOutputItemDailyWorkSchedule = outputItemDailyWorkScheduleRepository.findByCid(companyId);
		if (!lstOutputItemDailyWorkSchedule.isEmpty()) {
			if (isExistWorkScheduleOutputCondition) {
				dto.setStrReturn(SHOW_CHARACTERISTIC);
			} else {
				dto.setStrReturn(STRING_EMPTY);
			}
		} else {
			// 「就業帳票の権限」が取得できたか ( 「............」 đã được acquire chưa?)
			if (optPermissionOfEmploymentForm.isPresent()) {
				dto.setStrReturn(OPEN_SCREEN_C);
			} else {
				dto.setStrReturn("Msg_1141");
			}
		}
		
		dto.setLstOutputItemDailyWorkSchedule(getOutputItemDailyWorkSchedule(lstOutputItemDailyWorkSchedule));
		
		return dto;
	}
	
	/**
	 * Gets the dom closure.
	 *
	 * @param employeeId the employee id
	 * @param systemDate the system date
	 * @return the dom closure
	 */
	// アルゴリズム「社員に対応する締め期間を取得する」を実行する(Execute the algorithm "Acquire closing period corresponding to employee")
	private Optional<Closure> getDomClosure(String employeeId, GeneralDate systemDate) {
		
		String companyId = AppContexts.user().companyId();
		
		// Imported「（就業）所属雇用履歴」を取得する (Lấy Imported「（就業）所属雇用履歴」)
		Optional<SEmpHistExportImported> optSEmpHistExportImported = sEmpHistExportAdapter.getSEmpHistExport(companyId, employeeId, systemDate);
		if (!optSEmpHistExportImported.isPresent()) {
			return Optional.empty();
		}
		SEmpHistExportImported sEmpHistExportImported = optSEmpHistExportImported.get();
		
		// Get domain 対応するドメインモデル「雇用に紐づく就業締め」を取得する (Lấy về domain model "Thuê" tương ứng)
		Optional<ClosureEmployment> optClosureEmployment = closureEmploymentRepository.findByEmploymentCD(companyId, sEmpHistExportImported.getEmploymentCode());
		if (optClosureEmployment.isPresent()) {
			// Get domain 対応するドメインモデル「締め」を取得する (Lấy về domain model "Hạn định" tương ứng)
			return closureRepository.findById(companyId, optClosureEmployment.get().getClosureId());
		} else { // 締め1の対応するドメインモデル「締め」を取得する ((get domain model đối ứng của closing day 1)
			return closureRepository.findById(companyId, 1);
		}
	}
	
	/**
	 * Gets the error alarm code dto.
	 *
	 * @return the error alarm code dto
	 */
	public List<ErrorAlarmCodeDto> getErrorAlarmCodeDto() {
		String companyId = AppContexts.user().companyId();
		
		// ドメインモデル「勤務実績のエラーアラーム」を取得する(Acquire domain model "work error actual alarm")
		List<ErrorAlarmWorkRecord> lstErrorAlarmWorkRecord = errorAlarmWorkRecordRepository.getAllErAlCompanyAndUseAtr(companyId, true);
		return lstErrorAlarmWorkRecord.stream()
								.map(domain -> new ErrorAlarmCodeDto(domain.getCode().v(), domain.getName().v()))
								.sorted(Comparator.comparing(ErrorAlarmCodeDto::getCode))
								.collect(Collectors.toList());
	} 
	
	// convert to DTO
	private List<DataInforReturnDto> getOutputItemDailyWorkSchedule(List<OutputItemDailyWorkSchedule> lstOutputItemDailyWorkSchedule) {
		return lstOutputItemDailyWorkSchedule.stream()
						.map(domain -> new DataInforReturnDto(String.valueOf(domain.getItemCode().v()), 
																							domain.getItemName().v()))
						.collect(Collectors.toList());
	}
}

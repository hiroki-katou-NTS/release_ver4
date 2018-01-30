/**
 * 2:15:59 PM Aug 21, 2017
 */
package nts.uk.screen.at.app.dailyperformance.correction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import nts.arc.time.GeneralDate;
import nts.uk.screen.at.app.dailyperformance.correction.datadialog.CodeName;
import nts.uk.screen.at.app.dailyperformance.correction.datadialog.WorkTimeWorkplaceDto;
import nts.uk.screen.at.app.dailyperformance.correction.datadialog.WorkTypeChangedDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.ActualLockDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.AffEmploymentHistoryDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.AuthorityFomatDailyDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.AuthorityFormatInitialDisplayDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.AuthorityFormatSheetDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.ClosureDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.ClosureEmploymentDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.Com60HVacationDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.CompensLeaveComDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPAttendanceItem;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPAttendanceItemControl;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPBusinessTypeControl;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPErrorDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPErrorSettingDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DPSheetDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DailyPerformanceEmployeeDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DailyRecEditSetDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DateRange;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DivergenceTimeDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.EmploymentDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.FormatDPCorrectionDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.OperationOfDailyPerformanceDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.SubstVacationDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.WorkFixedDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.WorkInfoOfDailyPerformanceDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.YearHolidaySettingDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.checkshowbutton.DailyPerformanceAuthorityDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.reasondiscrepancy.ReasonCodeName;
import nts.uk.screen.at.app.dailyperformance.correction.dto.workinfomation.WorkInfoOfDailyPerformanceDetailDto;

/**
 * @author hungnm
 *
 */
public interface DailyPerformanceScreenRepo {

	/**
	 * param: employee id, baseDate 
	 * KMNMT_AFFI_EMP_HIST (employee id, basedate)
	 * -> employment code 
	 * CEMPT_EMPLOYMENT (company id, employment code) 
	 * -> closure id 
	 * KCLMT_CLOSURE (company id, closure id) -> closure object
	 * return: closure object
	 */
	
	List<ClosureDto> getClosureId(List<String> sId, GeneralDate baseDate);

	/** Query select KALMT_ANNUAL_PAID_LEAVE by company id */
	YearHolidaySettingDto getYearHolidaySetting();

	/** Query select KSVST_COM_SUBST_VACATION 振休（会社） by company id */
	SubstVacationDto getSubstVacationDto();

	/** Query select KCLMT_COMPENS_LEAVE_COM 代休管理設定(会社) by company id */
	CompensLeaveComDto getCompensLeaveComDto();

	/** Query select KSHST_COM_60H_VACATION 60H超休（会社） by company id */
	Com60HVacationDto getCom60HVacationDto();

	/** Get list sorted job titles */
	List<String> getListJobTitle(DateRange dateRange);

	/** Get list employment by closure */
	List<String> getListEmployment();

	/** Get list workplace of login user */
	Map<String, String> getListWorkplace(String employeeId, DateRange dateRange);

	/** 
	 * Get list WorkInfoOfDailyPerformance DTO
	 * @see WorkInfoOfDailyPerformanceDto
	 **/
	List<WorkInfoOfDailyPerformanceDto> getListWorkInfoOfDailyPerformance(List<String> lstEmployee, DateRange dateRange);

	

	/** Get list classification of login company */
	List<String> getListClassification();

	/** Get list employee by jobTitle, employment, workplace, classification */
	List<DailyPerformanceEmployeeDto> getListEmployee(List<String> lstJobTitle, List<String> lstEmployment,
			Map<String, String> lstWorkplace, List<String> lstClassification);

	/** Get list business type of list employee (no duplicated) */
	List<String> getListBusinessType(List<String> lstEmployee, DateRange dateRange);

	/** Get format daily performance correction */
	List<FormatDPCorrectionDto> getListFormatDPCorrection(List<String> lstBusinessType);

	/** Get Daily performance business type type control */
	List<DPBusinessTypeControl> getListBusinessTypeControl(List<String> lstBusinessType, List<Integer> lstAttendanceItem);

	/** Get list attendance item */
	List<DPAttendanceItem> getListAttendanceItem(List<Integer> lstAttendanceItem);

	/** Get list attendance item control */
	List<DPAttendanceItemControl> getListAttendanceItemControl(List<Integer> lstAttendanceItem);

	/** Get list daily performance error */
	List<DPErrorDto> getListDPError(DateRange dateRange, List<String> lstEmployee);
	
	/** Get list daily performance error with error list code */
	List<DPErrorDto> getListDPError(DateRange dateRange, List<String> lstEmployee, List<String>errorCodes);
	
	/** Get error settings */
	List<DPErrorSettingDto> getErrorSetting(List<String> listErrorCode);
	
	/** Get list sheet */
	List<DPSheetDto> getFormatSheets(List<String> lstBusinessType);
	
	AffEmploymentHistoryDto getAffEmploymentHistory(String employeeId, DateRange dateRange);
	
	EmploymentDto findEmployment(String companyId, String employmentCode);
	
	List<CodeName> findEmployment(String companyId);
	
	List<CodeName> findJobInfo(String companyId, GeneralDate baseDate);
	
	Optional<CodeName> findJobInfoId(String companyId, GeneralDate baseDate, String id);
	
	List<CodeName> findClassification(String companyId);
	
	List<CodeName> findWorkplace(String companyId, GeneralDate date);
	
	Optional<CodeName> findWorkplaceId(String companyId, GeneralDate date, String id);
	
	List<CodeName> findWorkplaceLocation(String companyId);
	
	List<CodeName> findReason(String companyId);
	
	ClosureEmploymentDto findByEmploymentCD(String companyID, String employmentCD);
	
	List<DailyRecEditSetDto> getDailyRecEditSet(List<String> listEmployeeId, DateRange dateRange);
	
	List<WorkInfoOfDailyPerformanceDetailDto> find(List<String> listEmployeeId, DateRange dateRange);
	
	Optional<ActualLockDto> findAutualLockById(String companyId, int closureId);
	
	Optional<WorkFixedDto> findWorkFixed(int closureId, int yearMonth);
	
	OperationOfDailyPerformanceDto findOperationOfDailyPerformance();
	
	List<AuthorityFormatInitialDisplayDto> findAuthorityFormatInitialDisplay(String companyId);
	
	List<AuthorityFomatDailyDto> findAuthorityFomatDaily(String companyId, List<String> formatCode);
	
	List<AuthorityFormatSheetDto> findAuthorityFormatSheet(String companyId, List<String> formatCode,  List<BigDecimal>sheetNo);
	
	Optional<DivergenceTimeDto> findDivergenceTime(String companyId, int divTimeId);
	
	List<ReasonCodeName> findDivergenceReason(String companyId, int divTimeId);
	
	List<DailyPerformanceAuthorityDto> findDailyAuthority(String roleId);
	
	List<WorkTimeWorkplaceDto> findWorkHours(String companyId, String workplaceId);
	
	List<CodeName> findWorkTimeZone(String companyId, List<String> shifCode);
	
	List<WorkTypeChangedDto> findWorkTypeChanged(String employmentCode, String typeCode, String companyId);
	
	List<CodeName> findWorkType(String companyId, Set<String> typeCodes);
	
	void updateColumnsWidth(Map<Integer, Integer> lstHeader, List<String> formatCodes);
 }

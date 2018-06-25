/**
 * 2:14:20 PM Aug 21, 2017
 */
package nts.uk.screen.at.infra.dailyperformance.correction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.enums.EnumAdaptor;
import nts.arc.enums.EnumConstant;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.function.infra.entity.dailymodification.KfnmtApplicationCall;
import nts.uk.ctx.at.function.infra.entity.dailyperformanceformat.KfnmtAuthorityDailyItem;
import nts.uk.ctx.at.function.infra.entity.dailyperformanceformat.KfnmtAuthorityDailyItemPK;
import nts.uk.ctx.at.function.infra.entity.dailyperformanceformat.KfnmtAuthorityFormSheet;
import nts.uk.ctx.at.function.infra.entity.dailyperformanceformat.KfnmtDailyPerformanceDisplay;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.SettingUnitType;
import nts.uk.ctx.at.record.infra.entity.dailyperformanceformat.KrcmtBusinessFormatSheet;
import nts.uk.ctx.at.record.infra.entity.dailyperformanceformat.KrcmtBusinessTypeDaily;
import nts.uk.ctx.at.record.infra.entity.divergence.reason.KrcstDvgcReason;
import nts.uk.ctx.at.record.infra.entity.divergence.time.KrcstDvgcTime;
import nts.uk.ctx.at.record.infra.entity.divergencetime.KmkmtDivergenceReason;
import nts.uk.ctx.at.record.infra.entity.editstate.KrcdtDailyRecEditSet;
import nts.uk.ctx.at.record.infra.entity.monthly.erroralarm.KrcdtEmployeeMonthlyPerError;
import nts.uk.ctx.at.record.infra.entity.monthly.erroralarm.KrcdtEmployeeMonthlyPerErrorPK;
import nts.uk.ctx.at.record.infra.entity.monthlyaggrmethod.flex.KrcstFlexShortageLimit;
import nts.uk.ctx.at.record.infra.entity.workinformation.KrcdtDaiPerWorkInfo;
import nts.uk.ctx.at.record.infra.entity.worklocation.KwlmtWorkLocation;
import nts.uk.ctx.at.record.infra.entity.workrecord.actuallock.KrcstActualLock;
import nts.uk.ctx.at.record.infra.entity.workrecord.actuallock.KrcstActualLockPK;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.KrcdtSyainDpErList;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.KwrmtErAlWorkRecord;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.condition.KrcstErAlApplication;
import nts.uk.ctx.at.record.infra.entity.workrecord.identificationstatus.KrcdtIdentificationStatus;
import nts.uk.ctx.at.record.infra.entity.workrecord.operationsetting.KrcmtApprovalProcess;
import nts.uk.ctx.at.record.infra.entity.workrecord.operationsetting.KrcmtApprovalProcessPk;
import nts.uk.ctx.at.record.infra.entity.workrecord.operationsetting.KrcmtDaiPerformEdFun;
import nts.uk.ctx.at.record.infra.entity.workrecord.operationsetting.KrcmtDaiPerformEdFunPk;
import nts.uk.ctx.at.record.infra.entity.workrecord.operationsetting.KrcmtDaiPerformanceAut;
import nts.uk.ctx.at.record.infra.entity.workrecord.operationsetting.KrcmtFormatPerformance;
import nts.uk.ctx.at.record.infra.entity.workrecord.operationsetting.KrcmtFormatPerformancePk;
import nts.uk.ctx.at.record.infra.entity.workrecord.operationsetting.KrcmtIdentityProcess;
import nts.uk.ctx.at.record.infra.entity.workrecord.operationsetting.KrcmtIdentityProcessPk;
import nts.uk.ctx.at.record.infra.entity.workrecord.operationsetting.KrcmtWorktypeChangeable;
import nts.uk.ctx.at.record.infra.entity.workrecord.workfixed.KrcstWorkFixed;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.ctx.at.shared.infra.entity.scherec.dailyattendanceitem.KrcmtDailyAttendanceItem;
import nts.uk.ctx.at.shared.infra.entity.scherec.dailyattendanceitem.KshstControlOfAttendanceItems;
import nts.uk.ctx.at.shared.infra.entity.scherec.dailyattendanceitem.KshstDailyServiceTypeControl;
import nts.uk.ctx.at.shared.infra.entity.vacation.setting.annualpaidleave.KalmtAnnualPaidLeave;
import nts.uk.ctx.at.shared.infra.entity.vacation.setting.compensatoryleave.KclmtCompensLeaveCom;
import nts.uk.ctx.at.shared.infra.entity.vacation.setting.sixtyhours.KshstCom60hVacation;
import nts.uk.ctx.at.shared.infra.entity.vacation.setting.subst.KsvstComSubstVacation;
import nts.uk.ctx.at.shared.infra.entity.workingcondition.KshmtWorkingCond;
import nts.uk.ctx.at.shared.infra.entity.workplace.KshmtWorkTimeWorkplace;
import nts.uk.ctx.at.shared.infra.entity.workrule.closure.KclmpClosureEmploymentPK;
import nts.uk.ctx.at.shared.infra.entity.workrule.closure.KclmtClosureEmployment;
import nts.uk.ctx.at.shared.infra.entity.worktime.KshmtWorkTimeSet;
import nts.uk.ctx.at.shared.infra.entity.worktype.KshmtWorkType;
import nts.uk.ctx.bs.employee.infra.entity.classification.BsymtClassification;
import nts.uk.ctx.bs.employee.infra.entity.employee.history.BsymtAffCompanyHist;
import nts.uk.ctx.bs.employee.infra.entity.employee.mngdata.BsymtEmployeeDataMngInfo;
import nts.uk.ctx.bs.employee.infra.entity.employment.BsymtEmployment;
import nts.uk.ctx.bs.employee.infra.entity.employment.BsymtEmploymentPK;
import nts.uk.ctx.bs.employee.infra.entity.employment.history.BsymtEmploymentHistItem;
import nts.uk.ctx.bs.employee.infra.entity.workplace.BsymtWorkplaceInfo;
import nts.uk.ctx.bs.employee.infra.entity.workplace.affiliate.BsymtAffiWorkplaceHistItem;
import nts.uk.ctx.bs.person.infra.entity.person.info.BpsmtPerson;
import nts.uk.screen.at.app.dailyperformance.correction.DailyPerformanceScreenRepo;
import nts.uk.screen.at.app.dailyperformance.correction.datadialog.CodeName;
import nts.uk.screen.at.app.dailyperformance.correction.datadialog.WorkTimeWorkplaceDto;
import nts.uk.screen.at.app.dailyperformance.correction.datadialog.WorkTypeChangedDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.ActualLockDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.AffEmploymentHistoryDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.ApplicationType;
import nts.uk.screen.at.app.dailyperformance.correction.dto.ApprovalUseSettingDto;
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
import nts.uk.screen.at.app.dailyperformance.correction.dto.IdentityProcessUseSetDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.OperationOfDailyPerformanceDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.SubstVacationDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.WorkFixedDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.WorkInfoOfDailyPerformanceDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.YearHolidaySettingDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.checkshowbutton.DailyPerformanceAuthorityDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.companyhist.AffComHistItemAtScreen;
import nts.uk.screen.at.app.dailyperformance.correction.dto.reasondiscrepancy.ReasonCodeName;
import nts.uk.screen.at.app.dailyperformance.correction.dto.workinfomation.CalculationStateDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.workinfomation.NotUseAttributeDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.workinfomation.ScheduleTimeSheetDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.workinfomation.WorkInfoOfDailyPerformanceDetailDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.workinfomation.WorkInformationDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.workplacehist.WorkPlaceHistTemp;
import nts.uk.screen.at.app.dailyperformance.correction.dto.workplacehist.WorkPlaceIdPeriodAtScreen;
import nts.uk.screen.at.app.dailyperformance.correction.flex.change.ErrorFlexMonthDto;
import nts.uk.screen.at.app.monthlyperformance.correction.dto.MonthlyPerformanceAuthorityDto;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.TimeWithDayAttr;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * @author hungnm
 *
 */
@Stateless
public class JpaDailyPerformanceScreenRepo extends JpaRepository implements DailyPerformanceScreenRepo {

	private final static String SEL_BUSINESS_TYPE;

	private final static String SEL_FORMAT_DP_CORRECTION;

	private final static String SEL_FORMAT_DP_CORRECTION_MULTI;

	private final static String SEL_CLOSURE_IDS;

	private final static String SEL_EMPLOYMENT_BY_CLOSURE;

	private final static String SEL_WORKPLACE;

	private final static String SEL_CLASSIFICATION = "SELECT c FROM BsymtClassification c WHERE c.bsymtClassificationPK.cid = :companyId";

	private final static String SEL_EMPLOYEE;

	private final static String SEL_EMPLOYEE_WITH_SID;

	private final static String SEL_PERSON = "SELECT p FROM BpsmtPerson p WHERE p.bpsmtPersonPk.pId IN :lstPersonId";

	private final static String SEL_DAILY_WORK_INFO = "SELECT d FROM KrcdtDaiPerWorkInfo d "
			+ "WHERE d.krcdtDaiPerWorkInfoPK.ymd IN :lstDate "
			+ "AND d.krcdtDaiPerWorkInfoPK.employeeId IN :lstEmployee";

	private final static String SEL_DP_TYPE_CONTROL;

	private final static String SEL_ATTENDANCE_ITEM;

	private final static String SEL_ATTENDANCE_ITEM_CONTROL = "SELECT c FROM KshstControlOfAttendanceItems c WHERE c.kshstControlOfAttendanceItemsPK.itemDailyID IN :lstItem";

	private final static String SEL_DP_ERROR_EMPLOYEE;

	private final static String SEL_DP_ERROR_EMPLOYEE_CONDITION_ERRORS;

	private final static String SEL_ERROR_SETTING;

	private final static String SEL_FORMAT_SHEET;

	private final static String SEL_EMPLOYMENT_HISTORY;

	private final static String SEL_DAILY_REC_EDIT_SET;

	private final static String SEL_FIND_WORK_FIXED;

	private final static String SEL_AUTHOR_DAILY_ITEM;

	private final static String SEL_DAILY_PERFORMACE_DISPLAY;

	private final static String SEL_AUTHOR_FORM_SHEET;

	private final static String SEL_DIVERGENCE_REASON;

	private final static String SEL_FIND_JOB_INFO;

	private final static String SEL_FIND_ID_JOB_INFO;

	private final static String SEL_FIND_CLASSIFICATION;

	private final static String SEL_FIND_WORKPLACE_LOCATION;

	private final static String SEL_ALL_WORKPLACE;

	private final static String SEL_FIND_WORKPLACE;

	private final static String SEL_FIND_ER_AL_APP;

	private final static String FIND_DVGC_TIME;

	private final static String FIND_WORK_CONDITION;

	private final static String GET_DAI_PER_AUTH_WITH_ROLE = "SELECT da FROM KrcmtDaiPerformanceAut da WHERE da.pk.roleId =:roleId";

	private final static String GET_DAI_PER_AUTH_WITH_ROLE_AND_AVAILABILITY = "SELECT da FROM KrcmtDaiPerformanceAut da WHERE da.pk.roleId =:roleId AND da.availability = :availability";

	private final static String SELECT_WORKTIME_WORKPLACE_BYID = "SELECT a FROM KshmtWorkTimeWorkplace a "
			+ " WHERE a.kshmtWorkTimeWorkplacePK.companyID = :companyID "
			+ " AND a.kshmtWorkTimeWorkplacePK.workplaceID = :workplaceID ";

	private final static String FIND_WORK_TIME_ZONE = "SELECT a FROM KshmtWorkTimeSet a "
			+ "WHERE a.kshmtWorkTimeSetPK.cid = :companyID";

	private final static String GET_ALL_WORK_TYPE_CHANGED = "SELECT wtc FROM KrcmtWorktypeChangeable wtc"
			+ " WHERE wtc.pk.cid = :companyId AND wtc.pk.empCode = :employeeCode";

	private final static String SELECT_WORKTYPE = " SELECT c FROM KshmtWorkType c WHERE c.kshmtWorkTypePK.companyId = :companyId";

	private final static String SELECT_ALL_DIVREASON = "SELECT c FROM KrcstDvgcReason c"
			+ " WHERE c.id.cid = :companyId";

	private final static String SELECT_CONFIRM_DAY = "SELECT c FROM KrcdtIdentificationStatus c"
			+ " WHERE c.krcdtIdentificationStatusPK.companyID = :companyID"
			+ " AND c.krcdtIdentificationStatusPK.employeeId IN :sids"
			+ " AND c.krcdtIdentificationStatusPK.processingYmd IN :processingYmds";

	public final static String SELECT_BY_LIST_EMPID = "SELECT e FROM BsymtEmployeeDataMngInfo e WHERE e.bsymtEmployeeDataMngInfoPk.sId IN :listSid ";

	private static final String SELECT_BY_EMPLOYEE_ID_AFF_COM = "SELECT c FROM BsymtAffCompanyHist c WHERE c.bsymtAffCompanyHistPk.sId IN :sIds and c.companyId = :cid ORDER BY c.startDate ";

	private static final String SELECT_BY_LISTSID_WPH;

	private final static String FIND_APPLICATION_CALL = "SELECT a FROM KfnmtApplicationCall a WHERE a.kfnmtApplicationCallPK.companyId = :companyId ORDER BY a.kfnmtApplicationCallPK.applicationType";

	private final static String FIND_ITEM_MON_AUT;
	
	private final static String FIND_ITEM_MON_BUSS;
	
	private final static String FIND_PERIOD_ORDER_BY_STR_D_FOR_MULTI = "SELECT wi FROM KshmtWorkingCond wi "
			+ "WHERE wi.kshmtWorkingCondPK.sid = :employeeId " 
			+ "AND wi.strD <= :endDate " + "AND wi.endD >= :startDate "
			+ "AND wi.kshmtWorkingCondItem.laborSys = " +WorkingSystem.FLEX_TIME_WORK.value;
	
	private final static String GET_LIMIT_FLEX_MON = "SELECT f FROM KrcstFlexShortageLimit f";
	
	static {
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT DISTINCT b.businessTypeCode");
		builderString.append(" FROM KrcmtBusinessTypeOfEmployee b");
		builderString.append(" JOIN KrcmtBusinessTypeOfHistory h");
		builderString
				.append(" ON b.krcmtBusinessTypeOfEmployeePK.historyId = h.krcmtBusinessTypeOfHistoryPK.historyId");
		builderString.append(" WHERE b.sId IN :lstSID");
		builderString.append(" AND h.startDate <= :endYmd");
		builderString.append(" AND h.endDate >= :startYmd");
		builderString.append(" ORDER BY b.businessTypeCode ASC");
		SEL_BUSINESS_TYPE = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT b");
		builderString.append(" FROM KrcmtBusinessTypeDaily b");
		builderString.append(" WHERE b.krcmtBusinessTypeDailyPK.companyId = :companyId");
		builderString.append(" AND b.krcmtBusinessTypeDailyPK.businessTypeCode IN :lstBusinessTypeCode ");
		builderString.append(" ORDER BY b.order ASC, b.krcmtBusinessTypeDailyPK.attendanceItemId ASC");
		SEL_FORMAT_DP_CORRECTION = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT b");
		builderString.append(" FROM KrcmtBusinessTypeDaily b INNER JOIN");
		builderString.append(" KrcstBusinessTypeSorted s");
		builderString.append(" WHERE s.krcstBusinessTypeSortedPK.companyId = :companyId");
		builderString.append(
				" AND b.krcmtBusinessTypeDailyPK.attendanceItemId = s.krcstBusinessTypeSortedPK.attendanceItemId");
		builderString.append(" AND b.krcmtBusinessTypeDailyPK.companyId = :companyId");
		builderString.append(" AND b.krcmtBusinessTypeDailyPK.businessTypeCode IN :lstBusinessTypeCode ");
		builderString.append(" ORDER BY s.order ASC, b.krcmtBusinessTypeDailyPK.attendanceItemId ASC");
		SEL_FORMAT_DP_CORRECTION_MULTI = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT NEW ");
		builderString.append(ClosureDto.class.getName());
		builderString.append(
				" (closure.kclmtClosurePK.cid, closure.kclmtClosurePK.closureId , closure.useClass, closure.closureMonth, '',emp.kclmpClosureEmploymentPK.employmentCD) ");
		builderString.append("FROM KclmtClosure closure JOIN ");
		builderString.append("KclmtClosureEmployment emp ");
		builderString.append("WHERE emp.kclmpClosureEmploymentPK.companyId = :companyId ");
		builderString.append("AND emp.kclmpClosureEmploymentPK.employmentCD IN :emptcd ");
		builderString.append("AND closure.kclmtClosurePK.cid = :companyId ");
		builderString.append("AND closure.kclmtClosurePK.closureId = emp.closureId");
		SEL_CLOSURE_IDS = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT e FROM BsymtEmployment e WHERE e.bsymtEmploymentPK.cid = :companyId");
		SEL_EMPLOYMENT_BY_CLOSURE = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT w FROM BsymtAffiWorkplaceHistItem w JOIN ");
		builderString.append("BsymtAffiWorkplaceHist a ON  w.hisId = a.hisId");
		builderString.append("WHERE a.sid = :sId ");
		builderString.append("AND a.strDate <= :baseDate ");
		builderString.append("AND a.endDate >= :baseDate ");
		builderString.append("AND w.sid = a.sid ");
		// builderString.append("AND w.bsymtWorkplaceHist.strD <= :baseDate ");
		// builderString.append("AND w.bsymtWorkplaceHist.endD >= :baseDate");
		SEL_WORKPLACE = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT DISTINCT s FROM BsymtEmployeeDataMngInfo s ");
		// builderString.append("JOIN KmnmtAffiliClassificationHist c ");
		// builderString.append("JOIN KmnmtAffiliEmploymentHist e ");
		// builderString.append("JOIN KmnmtAffiliJobTitleHist j ");
		builderString.append("JOIN BsymtAffiWorkplaceHistItem w ");
		builderString.append("WHERE w.workPlaceId IN :lstWkp ");
		// builderString.append("AND e.kmnmtEmploymentHistPK.emptcd IN :lstEmp
		// ");
		// builderString.append("AND j.kmnmtJobTitleHistPK.jobId IN :lstJob ");
		// builderString.append("AND c.kmnmtClassificationHistPK.clscd IN
		// :lstClas ");
		builderString.append("AND s.bsymtEmployeeDataMngInfoPk.sId = w.sid ");
		// builderString.append("OR s.bsymtEmployeePk.sId =
		// e.kmnmtEmploymentHistPK.empId ");
		// builderString.append("OR s.bsymtEmployeePk.sId =
		// j.kmnmtJobTitleHistPK.empId ");
		// builderString.append("OR s.bsymtEmployeePk.sId =
		// c.kmnmtClassificationHistPK.empId ");
		SEL_EMPLOYEE = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT DISTINCT s FROM BsymtEmployeeDataMngInfo s ");
		builderString.append("JOIN BsymtAffiWorkplaceHistItem w ");
		builderString.append("WHERE s.bsymtEmployeeDataMngInfoPk.sId IN :sids ");
		builderString.append("AND s.bsymtEmployeeDataMngInfoPk.sId = w.sid ");
		SEL_EMPLOYEE_WITH_SID = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT c FROM KshstDailyServiceTypeControl c ");
		builderString.append("WHERE c.kshstDailyServiceTypeControlPK.authorityDailyID = :authorityDailyID ");
		builderString.append("AND c.kshstDailyServiceTypeControlPK.itemDailyID IN :lstItem ");
		builderString.append("AND c.toUse = 1");
		SEL_DP_TYPE_CONTROL = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT i FROM KrcmtDailyAttendanceItem i ");
		builderString.append("WHERE i.krcmtDailyAttendanceItemPK.companyId = :companyId ");
		builderString.append("AND i.krcmtDailyAttendanceItemPK.attendanceItemId IN :lstItem");
		SEL_ATTENDANCE_ITEM = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT e FROM KrcdtSyainDpErList e ");
		builderString.append("WHERE ( e.processingDate BETWEEN :startDate AND :endDate ) ");
		builderString.append("AND e.employeeId IN :lstEmployee");
		SEL_DP_ERROR_EMPLOYEE = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT e FROM KrcdtSyainDpErList e ");
		builderString.append("WHERE ( e.processingDate BETWEEN :startDate AND :endDate ) ");
		builderString.append("AND e.employeeId IN :lstEmployee ");
		builderString.append("AND e.errorCode IN :errorCodes");
		SEL_DP_ERROR_EMPLOYEE_CONDITION_ERRORS = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT s FROM KwrmtErAlWorkRecord s");
		builderString.append(" WHERE s.kwrmtErAlWorkRecordPK.companyId = :companyId ");
		builderString.append("AND s.kwrmtErAlWorkRecordPK.errorAlarmCode IN :lstCode");
		SEL_ERROR_SETTING = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT s FROM KrcmtBusinessFormatSheet s ");
		builderString.append("WHERE s.krcmtBusinessFormatSheetPK.companyId = :companyId ");
		builderString.append("AND s.krcmtBusinessFormatSheetPK.businessTypeCode IN :lstBusinessTypeCode");
		SEL_FORMAT_SHEET = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT e FROM BsymtEmploymentHistItem e ");
		builderString.append("JOIN  BsymtEmploymentHist b ");
		builderString.append("ON e.hisId =  b.hisId ");
		builderString.append("WHERE b.sid = :empId ");
		builderString.append("AND b.companyId = :companyId ");
		builderString.append("AND b.strDate <= :baseDate ");
		builderString.append("AND b.endDate >= :baseDate");
		SEL_EMPLOYMENT_HISTORY = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KrcdtDailyRecEditSet a ");
		builderString.append("WHERE a.krcdtDailyRecEditSetPK.employeeId IN :employeeIds ");
		builderString.append("AND a.krcdtDailyRecEditSetPK.processingYmd IN :ymds ");
		SEL_DAILY_REC_EDIT_SET = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KrcstWorkFixed a ");
		builderString.append("WHERE a.krcstWorkFixedPK.closureId = :closureId ");
		builderString.append("AND a.krcstWorkFixedPK.cid = :cid ");
		builderString.append("AND a.krcstWorkFixedPK.cid = :cid ");
		builderString.append("AND a.confirmCls = 1 ");
		SEL_FIND_WORK_FIXED = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KfnmtAuthorityDailyItem a ");
		builderString.append("WHERE a.kfnmtAuthorityDailyItemPK.companyId = :companyId ");
		builderString
				.append("AND a.kfnmtAuthorityDailyItemPK.dailyPerformanceFormatCode IN :dailyPerformanceFormatCodes ");
		SEL_AUTHOR_DAILY_ITEM = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KfnmtDailyPerformanceDisplay a ");
		builderString.append("WHERE a.kfnmtDailyPerformanceDisplayPK.companyId = :companyId ");
		SEL_DAILY_PERFORMACE_DISPLAY = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KfnmtAuthorityFormSheet a ");
		builderString.append("WHERE a.kfnmtAuthorityFormSheetPK.companyId = :companyId ");
		builderString
				.append("AND a.kfnmtAuthorityFormSheetPK.dailyPerformanceFormatCode IN :dailyPerformanceFormatCode ");
		builderString.append("AND a.kfnmtAuthorityFormSheetPK.sheetNo IN :sheetNo ");
		builderString.append("ORDER BY a.kfnmtAuthorityFormSheetPK.sheetNo ASC");
		SEL_AUTHOR_FORM_SHEET = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KmkmtDivergenceReason a ");
		builderString.append("WHERE a.kmkmtDivergenceReasonPK.companyId = :companyId ");
		builderString.append("AND a.kmkmtDivergenceReasonPK.divTimeId = :divTimeId ");
		SEL_DIVERGENCE_REASON = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT NEW ");
		builderString.append(CodeName.class.getName());
		builderString.append("(i.jobCd , i.jobName, i.bsymtJobInfoPK.jobId)");
		builderString.append("FROM BsymtJobInfo i ");
		builderString.append("JOIN BsymtJobHist h ");
		builderString.append("ON i.bsymtJobInfoPK.cid = h.bsymtJobHistPK.cid ");
		builderString.append("AND i.bsymtJobInfoPK.histId = h.bsymtJobHistPK.histId ");
		builderString.append("AND i.bsymtJobInfoPK.jobId = h.bsymtJobHistPK.jobId ");
		builderString.append("WHERE i.bsymtJobInfoPK.cid = :companyId ");
		builderString.append("AND h.startDate <= :date ");
		builderString.append("AND h.endDate >= :date ");
		SEL_FIND_JOB_INFO = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT NEW ");
		builderString.append(CodeName.class.getName());
		builderString.append("(i.jobCd , i.jobName, i.bsymtJobInfoPK.jobId)");
		builderString.append("FROM BsymtJobInfo i ");
		builderString.append("JOIN BsymtJobHist h ");
		builderString.append("ON i.bsymtJobInfoPK.cid = h.bsymtJobHistPK.cid ");
		builderString.append("AND i.bsymtJobInfoPK.histId = h.bsymtJobHistPK.histId ");
		builderString.append("AND i.bsymtJobInfoPK.jobId = h.bsymtJobHistPK.jobId ");
		builderString.append("WHERE i.bsymtJobInfoPK.cid = :companyId ");
		builderString.append("AND i.bsymtJobInfoPK.jobId = :jobId ");
		builderString.append("AND h.startDate <= :date ");
		builderString.append("AND h.endDate >= :date ");
		SEL_FIND_ID_JOB_INFO = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM BsymtClassification a ");
		builderString.append("WHERE a.bsymtClassificationPK.cid = :companyId ");
		SEL_FIND_CLASSIFICATION = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT C ");
		builderString.append("FROM KwlmtWorkLocation c ");
		builderString.append("WHERE c.kwlmtWorkLocationPK.companyID = :companyId");
		SEL_FIND_WORKPLACE_LOCATION = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT w FROM BsymtWorkplaceInfo w JOIN ");
		builderString.append("BsymtWorkplaceHist a ");
		builderString.append("ON w.bsymtWorkplaceInfoPK.wkpid = a.bsymtWorkplaceHistPK.wkpid ");
		builderString.append("AND w.bsymtWorkplaceInfoPK.cid = a.bsymtWorkplaceHistPK.cid ");
		builderString.append("AND w.bsymtWorkplaceInfoPK.historyId = a.bsymtWorkplaceHistPK.historyId ");
		builderString.append("WHERE a.bsymtWorkplaceHistPK.cid = :cid ");
		builderString.append("AND a.strD <= :baseDate ");
		builderString.append("AND a.endD >= :baseDate ");
		SEL_ALL_WORKPLACE = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT w FROM BsymtWorkplaceInfo w JOIN ");
		builderString.append("BsymtWorkplaceHist a ");
		builderString.append("ON w.bsymtWorkplaceInfoPK.wkpid = a.bsymtWorkplaceHistPK.wkpid ");
		builderString.append("AND w.bsymtWorkplaceInfoPK.cid = a.bsymtWorkplaceHistPK.cid ");
		builderString.append("AND w.bsymtWorkplaceInfoPK.historyId = a.bsymtWorkplaceHistPK.historyId ");
		builderString.append("WHERE a.bsymtWorkplaceHistPK.cid = :cid ");
		builderString.append("AND a.bsymtWorkplaceHistPK.wkpid = :wkpid ");
		builderString.append("AND a.strD <= :baseDate ");
		builderString.append("AND a.endD >= :baseDate ");
		SEL_FIND_WORKPLACE = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT e FROM KrcstErAlApplication e ");
		builderString.append("WHERE e.krcstErAlApplicationPK.cid = :cid ");
		builderString.append("AND e.krcstErAlApplicationPK.errorCd IN :errorCd");
		SEL_FIND_ER_AL_APP = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT d FROM KrcstDvgcTime d ");
		builderString.append("WHERE d.id.cid = :cid ");
		builderString.append("AND d.id.no IN :no");
		FIND_DVGC_TIME = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT NEW ");
		builderString.append(WorkPlaceHistTemp.class.getName());
		builderString.append("(aw.sid , awit.workPlaceId, aw.strDate, aw.endDate)");
		builderString.append(" FROM BsymtAffiWorkplaceHist aw ");
		builderString.append(" LEFT JOIN BsymtAffiWorkplaceHistItem awit on aw.hisId = awit.hisId");
		builderString.append(" WHERE aw.sid IN :listSid");
		SELECT_BY_LISTSID_WPH = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT w FROM KshmtWorkingCond w");
		builderString.append(" WHERE w.kshmtWorkingCondPK.historyId IN :historyId");
		builderString.append(" AND w.kshmtWorkingCondPK.sid = :sid");
		builderString.append(" ORDER BY w.endD DESC");
		FIND_WORK_CONDITION = builderString.toString();
		
		builderString = new StringBuilder();
		builderString.append("SELECT a.kfnmtAuthorityMonthlyItemPK.attendanceItemId ");
		builderString.append("FROM KfnmtAuthorityMonthlyItem a ");
		builderString.append("WHERE a.kfnmtAuthorityMonthlyItemPK.companyId = :companyId ");
		builderString.append("AND a.kfnmtAuthorityMonthlyItemPK.dailyPerformanceFormatCode IN :formats ");
		FIND_ITEM_MON_AUT =  builderString.toString();
		
		builderString = new StringBuilder();
		builderString.append("SELECT a.krcmtBusinessTypeMonthlyPK.attendanceItemId ");
		builderString.append("FROM KrcmtBusinessTypeMonthly a ");
		builderString.append("WHERE a.krcmtBusinessTypeMonthlyPK.companyId = :companyId ");
		builderString.append("AND a.krcmtBusinessTypeMonthlyPK.businessTypeCode IN :formats ");
		FIND_ITEM_MON_BUSS =  builderString.toString();

	}

	@Override
	public List<ClosureDto> getClosureId(List<String> sIds, GeneralDate baseDate) {
		// get employment codes
		if (sIds.isEmpty())
			return new ArrayList<>();
		String query_empCodes = "SELECT e FROM BsymtEmploymentHistItem e JOIN BsymtEmploymentHist h ON e.hisId = h.hisId WHERE "
				+ " h.strDate <= :baseDate AND h.endDate >= :baseDate AND h.companyId = :companyId AND h.sid IN :sIds";
		Map<String, String> empCodes = new HashMap<>();
		CollectionUtil.split(sIds, 1000, (subList) -> {
			empCodes.putAll(this.queryProxy().query(query_empCodes, BsymtEmploymentHistItem.class)
					.setParameter("companyId", AppContexts.user().companyId()).setParameter("baseDate", baseDate)
					.setParameter("sIds", subList).getList().stream()
					.collect(Collectors.toMap(x -> x.sid, x -> x.empCode, (x, y) -> x)));
		});
		if (empCodes.isEmpty()) {
			return new ArrayList<>();
		}
		List<ClosureDto> closureDtos = new ArrayList<>();
		CollectionUtil.split(empCodes.values().stream().collect(Collectors.toList()), 1000, (subList) -> {
			closureDtos.addAll(this.queryProxy().query(SEL_CLOSURE_IDS, ClosureDto.class)
					.setParameter("companyId", AppContexts.user().companyId()).setParameter("emptcd", subList)
					.getList());
		});
		List<ClosureDto> result = new ArrayList<>();
		empCodes.forEach((key, value) -> {
			Optional<ClosureDto> optional = closureDtos.stream().filter(item -> item.getEmploymentCode().equals(value))
					.findFirst();
			if (optional.isPresent()) {
				ClosureDto closureDto = new ClosureDto();
				closureDto.setSid(key);
				closureDto.setEmploymentCode(optional.get().getEmploymentCode());
				closureDto.setClosureId(optional.get().getClosureId());
				closureDto.setClosureMonth(optional.get().getClosureMonth());
				closureDto.setCompanyId(optional.get().getCompanyId());
				closureDto.setUseAtr(optional.get().getUseAtr());
				result.add(closureDto);
			}
		});
		return result;
	}

	@Override
	public YearHolidaySettingDto getYearHolidaySetting() {
		Optional<KalmtAnnualPaidLeave> entity = this.queryProxy().find(AppContexts.user().companyId(),
				KalmtAnnualPaidLeave.class);
		if (entity.isPresent()) {
			return new YearHolidaySettingDto(entity.get().getCid(), entity.get().getManageAtr() == 1 ? true : false,
					entity.get().getPriorityType());
		}
		return null;
	}

	@Override
	public SubstVacationDto getSubstVacationDto() {
		Optional<KsvstComSubstVacation> entity = this.queryProxy().find(AppContexts.user().companyId(),
				KsvstComSubstVacation.class);
		if (entity.isPresent()) {
			return new SubstVacationDto(entity.get().getCid(), entity.get().getIsManage() == 1 ? true : false,
					entity.get().getExpirationDateSet(), entity.get().getAllowPrepaidLeave() == 1 ? true : false);
		}
		return null;
	}

	@Override
	public CompensLeaveComDto getCompensLeaveComDto() {
		Optional<KclmtCompensLeaveCom> entity = this.queryProxy().find(AppContexts.user().companyId(),
				KclmtCompensLeaveCom.class);
		if (entity.isPresent()) {
			return new CompensLeaveComDto(entity.get().getCid(), entity.get().getManageAtr() == 1 ? true : false);
		}
		return null;
	}

	@Override
	public Com60HVacationDto getCom60HVacationDto() {
		Optional<KshstCom60hVacation> entity = this.queryProxy().find(AppContexts.user().companyId(),
				KshstCom60hVacation.class);
		if (entity.isPresent()) {
			return new Com60HVacationDto(entity.get().getCid(), entity.get().getManageDistinct() == 1 ? true : false,
					entity.get().getSixtyHourExtra(), entity.get().getTimeDigestTive());
		}
		return null;
	}

	@Override
	public List<String> getListJobTitle(DateRange dateRange) {
		return null;
	}

	@Override
	public List<String> getListEmployment() {
		return this.queryProxy().query(SEL_EMPLOYMENT_BY_CLOSURE, BsymtEmployment.class)
				.setParameter("companyId", AppContexts.user().companyId()).getList().stream().map(e -> {
					return e.getBsymtEmploymentPK().getCode();
				}).collect(Collectors.toList());
	}

	@Override
	public Map<String, String> getListWorkplace(String employeeId, DateRange dateRange) {
		Map<String, String> lstWkp = new HashMap<>();
		List<BsymtAffiWorkplaceHistItem> bsymtAffiWorkplaceHistItem = this.queryProxy()
				.query(SEL_WORKPLACE, BsymtAffiWorkplaceHistItem.class).setParameter("sId", employeeId)
				.setParameter("baseDate", dateRange.getEndDate()).getList();
		List<String> workPlaceIds = bsymtAffiWorkplaceHistItem.stream().map(item -> item.getWorkPlaceId())
				.collect(Collectors.toList());

		String query = "SELECT w FROM BsymtWorkplaceInfo w WHERE w.bsymtWorkplaceInfoPK.wkpid IN :wkpId AND w.bsymtWorkplaceHist.strD <= :baseDate AND w.bsymtWorkplaceHist.endD >= :baseDate";
		this.queryProxy().query(query, BsymtWorkplaceInfo.class).setParameter("wkpId", workPlaceIds)
				.setParameter("baseDate", dateRange.getEndDate()).getList().stream().forEach(w -> {
					lstWkp.put(w.getBsymtWorkplaceInfoPK().getWkpid(), w.getWkpName());
				});
		return lstWkp;
	}

	@Override
	public List<WorkInfoOfDailyPerformanceDto> getListWorkInfoOfDailyPerformance(List<String> lstEmployee,
			DateRange dateRange) {
		if (lstEmployee.isEmpty())
			return new ArrayList<>();
		List<WorkInfoOfDailyPerformanceDto> results = new ArrayList<>();
		CollectionUtil.split(lstEmployee, 1000, subList -> {
			results.addAll(this.queryProxy().query(SEL_DAILY_WORK_INFO, KrcdtDaiPerWorkInfo.class)
					.setParameter("lstDate", dateRange.toListDate()).setParameter("lstEmployee", subList).getList(e -> {
						return new WorkInfoOfDailyPerformanceDto(e.krcdtDaiPerWorkInfoPK.employeeId, e.calculationState,
								e.krcdtDaiPerWorkInfoPK.ymd, e.recordWorkWorktypeCode, e.recordWorkWorktimeCode,
								e.scheduleWorkWorktypeCode, e.scheduleWorkWorktimeCode,
								e.scheduleTimes == null ? false : true);
					}));
		});
		return results;
	}

	@Override
	public List<String> getListClassification() {
		return this.queryProxy().query(SEL_CLASSIFICATION, BsymtClassification.class)
				.setParameter("companyId", AppContexts.user().companyId()).getList().stream().map(c -> {
					return c.getBsymtClassificationPK().getClscd();
				}).collect(Collectors.toList());
	}

	@Override
	public List<DailyPerformanceEmployeeDto> getListEmployee(List<String> lstJobTitle, List<String> lstEmployment,
			Map<String, String> lstWorkplace, List<String> lstClassification) {
		List<BsymtEmployeeDataMngInfo> lstEmployee = new ArrayList<>();
		CollectionUtil.split(lstWorkplace.keySet().stream().collect(Collectors.toList()), 1000, subList -> {
			lstEmployee.addAll(this.queryProxy().query(SEL_EMPLOYEE, BsymtEmployeeDataMngInfo.class)
					.setParameter("lstWkp", subList).getList());
		});

		List<String> ids = lstEmployee.stream().map((employee) -> {
			return employee.bsymtEmployeeDataMngInfoPk.pId.trim();
		}).collect(Collectors.toList());
		List<BpsmtPerson> lstPerson = new ArrayList<>();
		CollectionUtil.split(ids, 1000, subList -> {
			lstPerson.addAll(this.queryProxy().query(SEL_PERSON, BpsmtPerson.class).setParameter("lstPersonId", subList)
					.getList());
		});

		return lstEmployee.stream().map((employee) -> {
			for (BpsmtPerson person : lstPerson) {
				if (person.bpsmtPersonPk.pId.equals(employee.bsymtEmployeeDataMngInfoPk.pId)) {
					return new DailyPerformanceEmployeeDto(employee.bsymtEmployeeDataMngInfoPk.sId,
							employee.employeeCode, person.personName, lstWorkplace.values().stream().findFirst().get(),
							lstWorkplace.keySet().stream().findFirst().get(), "", false);
				}
			}
			return new DailyPerformanceEmployeeDto(employee.bsymtEmployeeDataMngInfoPk.sId, employee.employeeCode, "",
					lstWorkplace.values().stream().findFirst().get(), lstWorkplace.keySet().stream().findFirst().get(),
					"", false);
		}).collect(Collectors.toList());
	}

	@Override
	public List<DailyPerformanceEmployeeDto> getListEmployee(List<String> sids) {
		if (sids.isEmpty())
			return Collections.emptyList();
		List<BsymtEmployeeDataMngInfo> resultList = new ArrayList<>();
		CollectionUtil.split(sids, 1000, (subList) -> {
			resultList.addAll(this.queryProxy().query(SELECT_BY_LIST_EMPID, BsymtEmployeeDataMngInfo.class)
					.setParameter("listSid", subList).getList());
		});

		List<String> ids = resultList.stream().map((employee) -> {
			return employee.bsymtEmployeeDataMngInfoPk.pId.trim();
		}).collect(Collectors.toList());
		List<BpsmtPerson> lstPerson = new ArrayList<>();
		CollectionUtil.split(ids, 1000, (subList) -> {
			lstPerson.addAll(this.queryProxy().query(SEL_PERSON, BpsmtPerson.class).setParameter("lstPersonId", subList)
					.getList());
		});
		return resultList.stream().map((employee) -> {
			for (BpsmtPerson person : lstPerson) {
				if (person.bpsmtPersonPk.pId.equals(employee.bsymtEmployeeDataMngInfoPk.pId)) {
					return new DailyPerformanceEmployeeDto(employee.bsymtEmployeeDataMngInfoPk.sId,
							employee.employeeCode, person.personName, "", "", "", false);
				}
			}
			return new DailyPerformanceEmployeeDto(employee.bsymtEmployeeDataMngInfoPk.sId, employee.employeeCode, "",
					"", "", "", false);
		}).collect(Collectors.toList());
	}

	@Override
	public List<String> getListBusinessType(List<String> lstEmployee, DateRange dateRange) {
		List<String> businessTypes = new ArrayList<>();
		CollectionUtil.split(lstEmployee, 1000, subList -> {
			businessTypes.addAll(this.queryProxy().query(SEL_BUSINESS_TYPE, String.class)
					.setParameter("lstSID", subList).setParameter("startYmd", dateRange.getStartDate())
					.setParameter("endYmd", dateRange.getEndDate()).getList());
		});
		return businessTypes;
	}

	@Override
	public List<FormatDPCorrectionDto> getListFormatDPCorrection(List<String> lstBusinessType) {
		if (lstBusinessType.size() > 1) {
			return this.queryProxy().query(SEL_FORMAT_DP_CORRECTION_MULTI, KrcmtBusinessTypeDaily.class)
					.setParameter("companyId", AppContexts.user().companyId())
					.setParameter("lstBusinessTypeCode", lstBusinessType).getList().stream()
					.map(f -> new FormatDPCorrectionDto(f.krcmtBusinessTypeDailyPK.companyId,
							f.krcmtBusinessTypeDailyPK.businessTypeCode, f.krcmtBusinessTypeDailyPK.attendanceItemId,
							String.valueOf(f.krcmtBusinessTypeDailyPK.sheetNo),
							f.order, f.columnWidth != null
									? f.columnWidth.intValue() > 0 ? f.columnWidth.intValue() : 100 : 100))
					.distinct().collect(Collectors.toList());
		} else {
			return this.queryProxy().query(SEL_FORMAT_DP_CORRECTION, KrcmtBusinessTypeDaily.class)
					.setParameter("companyId", AppContexts.user().companyId())
					.setParameter("lstBusinessTypeCode", lstBusinessType).getList().stream()
					.map(f -> new FormatDPCorrectionDto(f.krcmtBusinessTypeDailyPK.companyId,
							f.krcmtBusinessTypeDailyPK.businessTypeCode, f.krcmtBusinessTypeDailyPK.attendanceItemId,
							String.valueOf(f.krcmtBusinessTypeDailyPK.sheetNo),
							f.order, f.columnWidth != null
									? f.columnWidth.intValue() > 0 ? f.columnWidth.intValue() : 100 : 100))
					.distinct().collect(Collectors.toList());
		}
	}

	@Override
	public List<DPBusinessTypeControl> getListBusinessTypeControl(String companyId, String authorityDailyID,
			List<Integer> lstAttendanceItem, boolean use) {
		return this.queryProxy().query(SEL_DP_TYPE_CONTROL, KshstDailyServiceTypeControl.class)
				.setParameter("authorityDailyID", authorityDailyID).setParameter("lstItem", lstAttendanceItem).getList()
				.stream().map(c -> {
					return new DPBusinessTypeControl(c.kshstDailyServiceTypeControlPK.itemDailyID, c.toUse == 1,
							c.canBeChangedByOthers == 1, c.youCanChangeIt == 1);
				}).collect(Collectors.toList());
	}

	@Override
	public List<DPAttendanceItem> getListAttendanceItem(List<Integer> lstAttendanceItem) {
		return this.queryProxy().query(SEL_ATTENDANCE_ITEM, KrcmtDailyAttendanceItem.class)
				.setParameter("companyId", AppContexts.user().companyId()).setParameter("lstItem", lstAttendanceItem)
				.getList().stream().map(i -> {
					return new DPAttendanceItem(i.krcmtDailyAttendanceItemPK.attendanceItemId, i.attendanceItemName,
							i.displayNumber.intValue(), i.userCanSet.intValue() == 1 ? true : false,
							i.nameLineFeedPosition.intValue(), i.dailyAttendanceAtr.intValue(),
							i.typeOfMaster != null ? i.typeOfMaster.intValue() : null);
				}).collect(Collectors.toList());
	}

	@Override
	public List<DPAttendanceItemControl> getListAttendanceItemControl(List<Integer> lstAttendanceItem) {
		return this.queryProxy().query(SEL_ATTENDANCE_ITEM_CONTROL, KshstControlOfAttendanceItems.class)
				.setParameter("lstItem", lstAttendanceItem).getList().stream().map(c -> {
					return new DPAttendanceItemControl(c.kshstControlOfAttendanceItemsPK.itemDailyID,
							c.inputUnitOfTimeItem, c.headerBgColorOfDailyPer != null ? c.headerBgColorOfDailyPer : "",
							null);
				}).collect(Collectors.toList());
	}

	@Override
	public List<DPErrorDto> getListDPError(DateRange dateRange, List<String> lstEmployee) {
		List<DPErrorDto> listDPError = new ArrayList<>();
		CollectionUtil.split(lstEmployee, 1000, subList -> {
			listDPError.addAll(this.queryProxy().query(SEL_DP_ERROR_EMPLOYEE, KrcdtSyainDpErList.class)
					.setParameter("startDate", dateRange.getStartDate()).setParameter("endDate", dateRange.getEndDate())
					.setParameter("lstEmployee", subList).getList().stream().map(e -> {
						return new DPErrorDto(e.errorCode, "", e.employeeId, e.processingDate,
								!e.erAttendanceItem.isEmpty() ? e.erAttendanceItem.stream()
										.map(x -> x.krcdtErAttendanceItemPK.attendanceItemId)
										.collect(Collectors.toList()) : Collections.emptyList(),
								e.errorCancelable.intValue() == 1 ? true : false, e.errorAlarmMessage);
					}).collect(Collectors.toList()));
		});
		return listDPError;
	}

	@Override
	public List<DPErrorDto> getListDPError(DateRange dateRange, List<String> lstEmployee, List<String> errorCodes) {
		List<DPErrorDto> dpErrors = new ArrayList<>();
		CollectionUtil.split(lstEmployee, 1000, subList -> {
			dpErrors.addAll(this.queryProxy().query(SEL_DP_ERROR_EMPLOYEE_CONDITION_ERRORS, KrcdtSyainDpErList.class)
					.setParameter("startDate", dateRange.getStartDate()).setParameter("endDate", dateRange.getEndDate())
					.setParameter("lstEmployee", subList).setParameter("errorCodes", errorCodes).getList().stream()
					.map(e -> {
						return new DPErrorDto(e.errorCode, "", e.employeeId, e.processingDate,
								!e.erAttendanceItem.isEmpty() ? e.erAttendanceItem.stream()
										.map(x -> x.krcdtErAttendanceItemPK.attendanceItemId)
										.collect(Collectors.toList()) : Collections.emptyList(),
								e.errorCancelable.intValue() == 1 ? true : false, e.errorAlarmMessage);
					}).collect(Collectors.toList()));
		});
		return dpErrors;
	}

	@Override
	public List<DPErrorSettingDto> getErrorSetting(List<String> listErrorCode) {
		return this.queryProxy().query(SEL_ERROR_SETTING, KwrmtErAlWorkRecord.class)
				.setParameter("companyId", AppContexts.user().companyId()).setParameter("lstCode", listErrorCode)
				.getList().stream().map(s -> {
					return new DPErrorSettingDto(s.kwrmtErAlWorkRecordPK.companyId,
							s.kwrmtErAlWorkRecordPK.errorAlarmCode, s.errorAlarmName, s.fixedAtr == 1 ? true : false,
							s.useAtr == 1 ? true : false, s.typeAtr,
							s.krcmtErAlCondition == null ? "" : s.krcmtErAlCondition.messageDisplay,
							s.boldAtr == 1 ? true : false, s.messageColor, s.cancelableAtr == 1 ? true : false,
							s.errorDisplayItem == null ? null : s.errorDisplayItem.intValue());
				}).collect(Collectors.toList());
	}

	@Override
	public List<DPSheetDto> getFormatSheets(List<String> lstBusinessType) {
		return this.queryProxy().query(SEL_FORMAT_SHEET, KrcmtBusinessFormatSheet.class)
				.setParameter("companyId", AppContexts.user().companyId())
				.setParameter("lstBusinessTypeCode", lstBusinessType).getList().stream().map(s -> {
					return new DPSheetDto(String.valueOf(s.krcmtBusinessFormatSheetPK.sheetNo), s.sheetName);
				}).collect(Collectors.toList());
	}

	@Override
	public AffEmploymentHistoryDto getAffEmploymentHistory(String employeeId, DateRange dateRange) {
		List<BsymtEmploymentHistItem> entity = this.queryProxy()
				.query(SEL_EMPLOYMENT_HISTORY, BsymtEmploymentHistItem.class).setParameter("empId", employeeId)
				.setParameter("baseDate", dateRange.getEndDate())
				.setParameter("companyId", AppContexts.user().companyId()).getList();
		return entity.isEmpty() ? null : new AffEmploymentHistoryDto(entity.get(0).empCode, employeeId);
	}

	@Override
	public EmploymentDto findEmployment(String companyId, String employmentCode) {
		return this.queryProxy().find(new BsymtEmploymentPK(companyId, employmentCode), BsymtEmployment.class).map(
				e -> new EmploymentDto(companyId, employmentCode, e.getName(), e.getEmpExternalCode(), e.getMemo()))
				.orElse(null);
	}

	@Override
	public List<CodeName> findEmployment(String companyId) {
		return this.queryProxy().query(SEL_EMPLOYMENT_BY_CLOSURE, BsymtEmployment.class)
				.setParameter("companyId", companyId).getList().stream().map(e -> {
					return new CodeName(e.getBsymtEmploymentPK().getCode(), e.getName(), "");
				}).collect(Collectors.toList());
	}

	@Override
	public List<CodeName> findJobInfo(String companyId, GeneralDate date) {
		return this.queryProxy().query(SEL_FIND_JOB_INFO, CodeName.class).setParameter("companyId", companyId)
				.setParameter("date", date).getList();
	}

	@Override
	public List<CodeName> findClassification(String companyId) {
		return this.queryProxy().query(SEL_FIND_CLASSIFICATION, BsymtClassification.class)
				.setParameter("companyId", companyId)
				.getList(x -> new CodeName(x.getBsymtClassificationPK().getClscd(), x.getClsname(), ""));
	}

	@Override
	public List<CodeName> findWorkplace(String companyId, GeneralDate date) {
		return this.queryProxy().query(SEL_ALL_WORKPLACE, BsymtWorkplaceInfo.class).setParameter("cid", companyId)
				.setParameter("baseDate", date)
				.getList(w -> new CodeName(w.getWkpcd(), w.getWkpName(), w.getBsymtWorkplaceInfoPK().getWkpid()));
	}

	@Override
	public List<CodeName> findWorkplaceLocation(String companyId) {
		return this.queryProxy().query(SEL_FIND_WORKPLACE_LOCATION, KwlmtWorkLocation.class)
				.setParameter("companyId", companyId)
				.getList(w -> new CodeName(w.kwlmtWorkLocationPK.workLocationCD, w.workLocationName, ""));
	}

	@Override
	public List<CodeName> findReason(String companyId) {
		return this.queryProxy().query(SELECT_ALL_DIVREASON, KrcstDvgcReason.class).setParameter("companyId", companyId)
				.getList(c -> new CodeName(c.getId().getReasonCd(), c.getReason(), String.valueOf(c.getId().getNo())));
	}

	@Override
	public ClosureEmploymentDto findByEmploymentCD(String companyID, String employmentCD) {
		return this.queryProxy()
				.find(new KclmpClosureEmploymentPK(companyID, employmentCD), KclmtClosureEmployment.class)
				.map(x -> new ClosureEmploymentDto(x.kclmpClosureEmploymentPK.companyId,
						x.kclmpClosureEmploymentPK.employmentCD, x.closureId))
				.orElse(null);
	}

	@Override
	public List<DailyRecEditSetDto> getDailyRecEditSet(List<String> listEmployeeId, DateRange dateRange) {
		if (listEmployeeId.isEmpty())
			return Collections.emptyList();
		List<DailyRecEditSetDto> editSets = new ArrayList<>();
		CollectionUtil.split(listEmployeeId, 1000, subList -> {
			editSets.addAll(this.queryProxy().query(SEL_DAILY_REC_EDIT_SET, KrcdtDailyRecEditSet.class)
					.setParameter("employeeIds", subList).setParameter("ymds", dateRange.toListDate()).getList()
					.stream().map(s -> {
						return new DailyRecEditSetDto(s.krcdtDailyRecEditSetPK.employeeId,
								s.krcdtDailyRecEditSetPK.processingYmd, s.krcdtDailyRecEditSetPK.attendanceItemId,
								s.editState);
					}).collect(Collectors.toList()));
		});
		return editSets;
	}

	@Override
	public List<WorkInfoOfDailyPerformanceDetailDto> find(List<String> listEmployeeId, DateRange dateRange) {
		List<WorkInfoOfDailyPerformanceDetailDto> datas = new ArrayList<>();
		CollectionUtil.split(listEmployeeId, 1000, subList -> {
			datas.addAll(this.queryProxy().query(SEL_DAILY_WORK_INFO, KrcdtDaiPerWorkInfo.class)
					.setParameter("lstDate", dateRange.toListDate()).setParameter("lstEmployee", subList)
					.getList(c -> new WorkInfoOfDailyPerformanceDetailDto(c.krcdtDaiPerWorkInfoPK.employeeId,
							new WorkInformationDto(c.recordWorkWorktimeCode, c.recordWorkWorktypeCode),
							new WorkInformationDto(c.scheduleWorkWorktimeCode, c.recordWorkWorktypeCode),
							EnumAdaptor.valueOf(c.calculationState, CalculationStateDto.class),
							EnumAdaptor.valueOf(c.goStraightAttribute, NotUseAttributeDto.class),
							EnumAdaptor.valueOf(c.backStraightAttribute, NotUseAttributeDto.class),
							c.krcdtDaiPerWorkInfoPK.ymd,
							c.scheduleTimes.isEmpty() ? null
									: c.scheduleTimes.stream()
											.map(s -> new ScheduleTimeSheetDto(s.krcdtWorkScheduleTimePK.workNo,
													new TimeWithDayAttr(s.attendance),
													new TimeWithDayAttr(s.leaveWork)))
											.collect(Collectors.toList()))));
		});
		return datas;
	}

	@Override
	public Optional<ActualLockDto> findAutualLockById(String companyId, int closureId) {
		return this.queryProxy().find(new KrcstActualLockPK(companyId, closureId), KrcstActualLock.class)
				.map(c -> new ActualLockDto(companyId, closureId, c.getDLockState(), c.getMLockState()));
	}

	@Override
	public List<WorkFixedDto> findWorkFixed(int closureId, int yearMonth) {
		List<WorkFixedDto> workOp = this.queryProxy().query(SEL_FIND_WORK_FIXED, KrcstWorkFixed.class)
				.setParameter("closureId", closureId).setParameter("cid", AppContexts.user().companyId())
				.getList(w -> new WorkFixedDto(closureId, w.getConfirmPid(), w.getKrcstWorkFixedPK().getWkpid(),
						w.getConfirmCls(), w.getFixedDate(), yearMonth, w.getKrcstWorkFixedPK().getCid()));
		return workOp;
	}

	@Override
	public OperationOfDailyPerformanceDto findOperationOfDailyPerformance() {
		String companyId = AppContexts.user().companyId();
		OperationOfDailyPerformanceDto dto = new OperationOfDailyPerformanceDto();
		Optional<KrcmtFormatPerformance> format = this.queryProxy().find(new KrcmtFormatPerformancePk(companyId),
				KrcmtFormatPerformance.class);
		Optional<KrcmtDaiPerformEdFun> edFunc = this.queryProxy().find(new KrcmtDaiPerformEdFunPk(companyId),
				KrcmtDaiPerformEdFun.class);
		dto.setComment(edFunc.isPresent() ? edFunc.get().comment : "");
		dto.setSettingUnit(
				EnumAdaptor.valueOf(format.isPresent() ? format.get().settingUnitType : 1, SettingUnitType.class));
		return dto;
	}

	@Override
	public List<AuthorityFormatInitialDisplayDto> findAuthorityFormatInitialDisplay(String companyId) {
		return this.queryProxy().query(SEL_DAILY_PERFORMACE_DISPLAY, KfnmtDailyPerformanceDisplay.class)
				.setParameter("companyId", companyId).getList(k -> new AuthorityFormatInitialDisplayDto(companyId,
						k.kfnmtDailyPerformanceDisplayPK.dailyPerformanceFormatCode));
	}

	@Override
	public List<AuthorityFomatDailyDto> findAuthorityFomatDaily(String companyId, List<String> formatCodes) {
		return this.queryProxy().query(SEL_AUTHOR_DAILY_ITEM, KfnmtAuthorityDailyItem.class)
				.setParameter("companyId", companyId).setParameter("dailyPerformanceFormatCodes", formatCodes)
				.getList(f -> new AuthorityFomatDailyDto(f.kfnmtAuthorityDailyItemPK.companyId,
						f.kfnmtAuthorityDailyItemPK.dailyPerformanceFormatCode,
						f.kfnmtAuthorityDailyItemPK.attendanceItemId, f.kfnmtAuthorityDailyItemPK.sheetNo,
						f.displayOrder, f.columnWidth));
	}

	@Override
	public List<AuthorityFormatSheetDto> findAuthorityFormatSheet(String companyId, List<String> formatCode,
			List<BigDecimal> sheetNo) {
		List<KfnmtAuthorityFormSheet> ent = this.queryProxy()
				.query(SEL_AUTHOR_FORM_SHEET, KfnmtAuthorityFormSheet.class).setParameter("companyId", companyId)
				.setParameter("dailyPerformanceFormatCode", formatCode).setParameter("sheetNo", sheetNo).getList();
		return ent.isEmpty() ? Collections.emptyList()
				: ent.stream()
						.map(f -> new AuthorityFormatSheetDto(f.kfnmtAuthorityFormSheetPK.companyId,
								f.kfnmtAuthorityFormSheetPK.dailyPerformanceFormatCode,
								f.kfnmtAuthorityFormSheetPK.sheetNo, f.sheetName))
						.collect(Collectors.toList());
	}

	@Override
	public List<DivergenceTimeDto> findDivergenceTime(String companyId, List<Integer> divergenceNo) {
		return this.queryProxy().query(FIND_DVGC_TIME, KrcstDvgcTime.class).setParameter("cid", companyId)
				.setParameter("no", divergenceNo)
				.getList(x -> new DivergenceTimeDto(x.getId().getNo(), companyId, x.getDvgcTimeUseSet().intValue(),
						x.getDvgcReasonInputed().intValue() == 1 ? true : false,
						x.getDvgcReasonSelected().intValue() == 1 ? true : false));

	}

	@Override
	public List<ReasonCodeName> findDivergenceReason(String companyId, int divTimeId) {
		return this.queryProxy().query(SEL_DIVERGENCE_REASON, KmkmtDivergenceReason.class)
				.setParameter("companyId", companyId).setParameter("divTimeId", divTimeId)
				.getList(x -> new ReasonCodeName(x.kmkmtDivergenceReasonPK.divReasonCode, x.divReason));
	}

	@Override
	public List<DailyPerformanceAuthorityDto> findDailyAuthority(String roleId) {
		List<KrcmtDaiPerformanceAut> entities = this.queryProxy()
				.query(GET_DAI_PER_AUTH_WITH_ROLE, KrcmtDaiPerformanceAut.class).setParameter("roleId", roleId)
				.getList();
		List<DailyPerformanceAuthorityDto> results = new ArrayList<>();
		entities.forEach(ent -> {
			BigDecimal avaiBigDecimal = ent.availability;
			boolean availability = false;
			if (avaiBigDecimal.intValue() == 1) {
				availability = true;
			}
			results.add(new DailyPerformanceAuthorityDto(roleId, ent.pk.functionNo, availability));
		});
		return results;
	}

	/**
	 * find authority for monthlyPer kmw003 screen
	 */
	@Override
	public List<MonthlyPerformanceAuthorityDto> findAuthority(String roleId, BigDecimal availability) {
		List<KrcmtDaiPerformanceAut> entities = this.queryProxy()
				.query(GET_DAI_PER_AUTH_WITH_ROLE_AND_AVAILABILITY, KrcmtDaiPerformanceAut.class)
				.setParameter("roleId", roleId).setParameter("availability", availability).getList();
		List<MonthlyPerformanceAuthorityDto> results = new ArrayList<>();
		entities.forEach(ent -> {
			BigDecimal avaiBigDecimal = ent.availability;
			boolean availa = false;
			if (avaiBigDecimal.intValue() == 1) {
				availa = true;
			}
			results.add(new MonthlyPerformanceAuthorityDto(roleId, ent.pk.functionNo, availa));
		});
		return results;
	}

	@Override
	public List<WorkTimeWorkplaceDto> findWorkHours(String companyId, String workplaceId) {
		return this.queryProxy().query(SELECT_WORKTIME_WORKPLACE_BYID, KshmtWorkTimeWorkplace.class)
				.setParameter("companyID", companyId).setParameter("workplaceID", workplaceId)
				.getList(c -> new WorkTimeWorkplaceDto(c.kshmtWorkTimeWorkplacePK.companyID,
						c.kshmtWorkTimeWorkplacePK.workplaceID, c.kshmtWorkTimeWorkplacePK.workTimeID));
	}

	@Override
	public List<CodeName> findWorkTimeZone(String companyId, List<String> shifCode) {
		if (shifCode.isEmpty()) {
			return this.queryProxy()
					.query(FIND_WORK_TIME_ZONE + " ORDER BY a.kshmtWorkTimeSetPK.worktimeCd ASC ",
							KshmtWorkTimeSet.class)
					.setParameter("companyID", companyId)
					.getList(x -> new CodeName(x.getKshmtWorkTimeSetPK().getWorktimeCd(), x.getName(), ""));
		} else {
			return this.queryProxy()
					.query(FIND_WORK_TIME_ZONE
							+ " AND  a.kshmtWorkTimeSetPK.worktimeCd IN :shifCode ORDER BY a.kshmtWorkTimeSetPK.worktimeCd ASC",
							KshmtWorkTimeSet.class)
					.setParameter("companyID", companyId).setParameter("shifCode", shifCode)
					.getList(x -> new CodeName(x.getKshmtWorkTimeSetPK().getWorktimeCd(), x.getName(), ""));
		}
	}

	@Override
	public List<WorkTypeChangedDto> findWorkTypeChanged(String employmentCode, String typeCode, String companyId) {
		List<WorkTypeChangedDto> dtos = this.queryProxy()
				.query(GET_ALL_WORK_TYPE_CHANGED, KrcmtWorktypeChangeable.class).setParameter("companyId", companyId)
				.setParameter("employeeCode", employmentCode)
				.getList(x -> new WorkTypeChangedDto(String.valueOf(x.pk.workTypeGroupNo), x.pk.workTypeCode));
		if (!dtos.isEmpty()) {
			Map<String, List<WorkTypeChangedDto>> mapGroupNo = dtos.stream().filter(x -> x.getTypeCode() != "")
					.collect(Collectors.groupingBy(WorkTypeChangedDto::getGroupNo));
			List<WorkTypeChangedDto> temps = new ArrayList<>();
			mapGroupNo.entrySet().forEach(x -> {
				x.getValue().forEach(data -> {
					if (data.getTypeCode().equals(typeCode)) {
						temps.addAll(x.getValue());
					}
				});
			});
			return temps;
		} else {
			return Collections.emptyList();
		}

	}

	@Override
	public List<CodeName> findWorkType(String companyId, Set<String> typeCodes) {
		if (typeCodes.isEmpty()) {
			return this.queryProxy().query(SELECT_WORKTYPE, KshmtWorkType.class).setParameter("companyId", companyId)
					.getList(c -> new CodeName(c.kshmtWorkTypePK.workTypeCode, c.name, ""));
		} else {
			return this.queryProxy()
					.query(SELECT_WORKTYPE + " AND c.kshmtWorkTypePK.workTypeCode IN :workTypeCodes",
							KshmtWorkType.class)
					.setParameter("companyId", companyId).setParameter("workTypeCodes", typeCodes)
					.getList(c -> new CodeName(c.kshmtWorkTypePK.workTypeCode, c.name, ""));
		}
	}

	@Override
	public void updateColumnsWidth(Map<Integer, Integer> lstHeader, List<String> formatCodes) {
		List<AuthorityFomatDailyDto> items = this.findAuthorityFomatDaily(AppContexts.user().companyId(), formatCodes);
		List<KfnmtAuthorityDailyItem> entitys = items.stream()
				.map(x -> new KfnmtAuthorityDailyItem(
						new KfnmtAuthorityDailyItemPK(x.getCompanyId(), x.getDailyPerformanceFormatCode(),
								x.getAttendanceItemId(), x.getSheetNo()),
						x.getDisplayOrder(), new BigDecimal(lstHeader.get(x.getAttendanceItemId()))))
				.collect(Collectors.toList());
		this.commandProxy().updateAll(entitys);
	}

	@Override
	public Optional<CodeName> findJobInfoId(String companyId, GeneralDate baseDate, String id) {
		return this.queryProxy().query(SEL_FIND_ID_JOB_INFO, CodeName.class).setParameter("companyId", companyId)
				.setParameter("jobId", id).setParameter("date", baseDate).getSingle();
	}

	@Override
	public Optional<CodeName> findWorkplaceId(String companyId, GeneralDate date, String id) {
		return this.queryProxy().query(SEL_FIND_WORKPLACE, BsymtWorkplaceInfo.class).setParameter("cid", companyId)
				.setParameter("wkpid", id).setParameter("baseDate", date)
				.getSingle(w -> new CodeName(w.getWkpcd(), w.getWkpName(), w.getBsymtWorkplaceInfoPK().getWkpid()));
	}

	@Override
	public List<DailyPerformanceEmployeeDto> getListEmployeeWithSid(List<String> sid) {
		List<BsymtEmployeeDataMngInfo> lstEmployee = new ArrayList<>();
		CollectionUtil.split(sid, 1000, subList -> {
			lstEmployee.addAll(this.queryProxy().query(SEL_EMPLOYEE_WITH_SID, BsymtEmployeeDataMngInfo.class)
					.setParameter("sids", subList).getList());
		});

		List<String> ids = lstEmployee.stream().map((employee) -> {
			return employee.bsymtEmployeeDataMngInfoPk.pId.trim();
		}).collect(Collectors.toList());

		List<BpsmtPerson> lstPerson = new ArrayList<>();
		CollectionUtil.split(ids, 1000, subList -> {
			lstPerson.addAll(this.queryProxy().query(SEL_PERSON, BpsmtPerson.class).setParameter("lstPersonId", subList)
					.getList());
		});

		return lstEmployee.stream().map((employee) -> {
			for (BpsmtPerson person : lstPerson) {
				if (person.bpsmtPersonPk.pId.equals(employee.bsymtEmployeeDataMngInfoPk.pId)) {
					return new DailyPerformanceEmployeeDto(employee.bsymtEmployeeDataMngInfoPk.sId,
							employee.employeeCode, person.personName, "", "", "", false);
				}
			}
			return new DailyPerformanceEmployeeDto(employee.bsymtEmployeeDataMngInfoPk.sId, employee.employeeCode, "",
					"", "", "", false);
		}).collect(Collectors.toList());
	}

	@Override
	public Map<String, List<EnumConstant>> findErAlApplicationByCidAndListErrCd(String companyId,
			List<String> errorCode) {
		List<KrcstErAlApplication> entity = this.queryProxy().query(SEL_FIND_ER_AL_APP, KrcstErAlApplication.class)
				.setParameter("cid", companyId).setParameter("errorCd", errorCode).getList();
		Map<String, List<EnumConstant>> result = new HashMap<>();
		if (!entity.isEmpty()) {
			result = entity.stream()
					.collect(
							Collectors
									.groupingBy(x -> x.krcstErAlApplicationPK.errorCd,
											Collectors.mapping(
													x -> new EnumConstant(x.krcstErAlApplicationPK.appTypeCd,
															EnumAdaptor.valueOf(x.krcstErAlApplicationPK.appTypeCd,
																	ApplicationType.class).nameId,
															""),
													Collectors.toList())));
		}

		return result;
	}

	@Override
	public Optional<IdentityProcessUseSetDto> findIdentityProcessUseSet(String comapnyId) {
		return this.queryProxy().find(new KrcmtIdentityProcessPk(comapnyId), KrcmtIdentityProcess.class)
				.map(x -> new IdentityProcessUseSetDto(x.useDailySelfCk == 1 ? true : false,
						x.useMonthSelfCK == 1 ? true : false,
						x.yourselfConfirmError != null ? x.yourselfConfirmError : null));
	}

	@Override
	public Map<String, Boolean> getConfirmDay(String companyId, List<String> sids, DateRange dates) {
		if (!sids.isEmpty()) {
			Map<String, Boolean> result = new HashMap<>();
			CollectionUtil.split(sids, 1000, subList -> {
				result.putAll(this.queryProxy().query(SELECT_CONFIRM_DAY, KrcdtIdentificationStatus.class)
						.setParameter("companyID", companyId).setParameter("sids", subList)
						.setParameter("processingYmds", dates.toListDate())
						.getList(x -> x.krcdtIdentificationStatusPK.employeeId + "|"
								+ x.krcdtIdentificationStatusPK.processingYmd)
						.stream().collect(Collectors.toMap(y -> y, y -> true)));
			});
			return result;
		} else {
			return Collections.emptyMap();
		}
	}

	@Override
	public Optional<ApprovalUseSettingDto> findApprovalUseSettingDto(String comapnyId) {
		return this.queryProxy().find(new KrcmtApprovalProcessPk(comapnyId), KrcmtApprovalProcess.class)
				.map(x -> new ApprovalUseSettingDto(x.useDailyBossChk == 1 ? true : false,
						x.useMonthBossChk == 1 ? true : false,
						x.supervisorConfirmError != null ? x.supervisorConfirmError : null));
	}

	@Override
	public Map<String, List<WorkPlaceIdPeriodAtScreen>> getWplByListSidAndPeriod(List<String> sids) {
		// Split query.
		if (sids.isEmpty())
			return Collections.emptyMap();
		List<WorkPlaceHistTemp> resultList = new ArrayList<>();
		CollectionUtil.split(sids, 1000, (subList) -> {
			resultList.addAll(this.queryProxy().query(SELECT_BY_LISTSID_WPH, WorkPlaceHistTemp.class)
					.setParameter("listSid", subList).getList());
		});
		return resultList.stream()
				.collect(
						Collectors.groupingBy(WorkPlaceHistTemp::getEmployeeId,
								Collectors.mapping(
										x -> new WorkPlaceIdPeriodAtScreen(
												new DatePeriod(x.getStartDate(), x.getEndDate()), x.getWorkplaceId()),
										Collectors.toList())));
	}

	@Override
	public Map<String, List<AffComHistItemAtScreen>> getAffCompanyHistoryOfEmployee(String cid,
			List<String> employeeIds) {
		if (employeeIds.isEmpty())
			return Collections.emptyMap();
		List<AffComHistItemAtScreen> resultList = new ArrayList<>();
		CollectionUtil.split(employeeIds, 1000, (subList) -> {
			resultList.addAll(this.queryProxy().query(SELECT_BY_EMPLOYEE_ID_AFF_COM, BsymtAffCompanyHist.class)
					.setParameter("sIds", subList).setParameter("cid", cid)
					.getList(x -> new AffComHistItemAtScreen(x.bsymtAffCompanyHistPk.sId,
							new DatePeriod(x.startDate, x.endDate))));
		});
		return resultList.stream().collect(Collectors.groupingBy(AffComHistItemAtScreen::getEmployeeId,
				Collectors.mapping(x -> x, Collectors.toList())));
	}

	@Override
	public List<EnumConstant> findApplicationCall(String companyId) {
		List<KfnmtApplicationCall> entities = this.queryProxy().query(FIND_APPLICATION_CALL, KfnmtApplicationCall.class)
				.setParameter("companyId", companyId).getList();
		if (!entities.isEmpty()) {
			return entities.stream().map(x -> {
				return new EnumConstant(x.kfnmtApplicationCallPK.applicationType,
						EnumAdaptor.valueOf(x.kfnmtApplicationCallPK.applicationType, ApplicationType.class).nameId,
						"");
			}).collect(Collectors.toList());
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public String findWorkConditionLastest(List<String> hists, String employeeId) {
		List<KshmtWorkingCond> entitys = this.queryProxy().query(FIND_WORK_CONDITION, KshmtWorkingCond.class)
				.setParameter("historyId", hists).setParameter("sid", employeeId).getList();
		return entitys.isEmpty() ? "" : entitys.get(0).getKshmtWorkingCondItem().getHistoryId();
	}

	@Override
	public List<Integer> getItemIdsMonthByAuthority(String companyId, Set<String> formats) {
	     return this.queryProxy().query(FIND_ITEM_MON_AUT, Integer.class).setParameter("companyId", companyId).setParameter("formats", formats).getList();
	}

	@Override
	public List<Integer> getItemIdsMonthByBussiness(String companyId, Set<String> formats) {
		return this.queryProxy().query(FIND_ITEM_MON_BUSS, Integer.class).setParameter("companyId", companyId).setParameter("formats", formats).getList();
	}

	@Override
	public List<DateRange> getWorkConditionFlexDatePeriod(String employeeId, DatePeriod date) {
		List<KshmtWorkingCond> ents = this.queryProxy()
				.query(FIND_PERIOD_ORDER_BY_STR_D_FOR_MULTI, KshmtWorkingCond.class).setParameter("endDate", date.end())
				.setParameter("startDate", date.start()).setParameter("employeeId", employeeId).getList();
		return ents.stream().map(x -> new DateRange(x.getStrD().beforeOrEquals(date.start()) ? date.start() : x.getStrD(), x.getEndD().beforeOrEquals(date.end()) ? x.getEndD() : date.end())).collect(Collectors.toList());
	}

	@Override
	public Integer getLimitFexMonth() {
		Optional<KrcstFlexShortageLimit> ent = this.queryProxy().query(GET_LIMIT_FLEX_MON, KrcstFlexShortageLimit.class).getSingle();
		return ent.isPresent() ? ent.get().upperLimitTime : 0;
	}

	@Override
	public Optional<ErrorFlexMonthDto> getErrorFlexMonth(Integer errorType, Integer yearMonth, String employeeId,
			Integer closureId, Integer closeDay, Integer isLastDay) {
		 Optional<ErrorFlexMonthDto> errorFlex = this.queryProxy().find(
				new KrcdtEmployeeMonthlyPerErrorPK(errorType, yearMonth, employeeId, closureId, closeDay, isLastDay),
				KrcdtEmployeeMonthlyPerError.class).map(x -> new ErrorFlexMonthDto(x.flex, x.annualHoliday, x.yearlyReserved));
		return errorFlex;
	}
}

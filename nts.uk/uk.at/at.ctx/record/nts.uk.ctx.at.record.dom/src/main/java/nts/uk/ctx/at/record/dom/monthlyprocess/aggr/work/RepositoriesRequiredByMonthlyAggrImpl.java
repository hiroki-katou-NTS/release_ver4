package nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.Getter;
import nts.arc.task.parallel.ManagedParallelWithContext;
import nts.uk.ctx.at.record.dom.actualworkinghours.repository.AttendanceTimeRepository;
import nts.uk.ctx.at.record.dom.adapter.workplace.affiliate.AffWorkplaceAdapter;
import nts.uk.ctx.at.record.dom.affiliationinformation.repository.AffiliationInforOfDailyPerforRepository;
import nts.uk.ctx.at.record.dom.affiliationinformation.repository.WorkTypeOfDailyPerforRepository;
import nts.uk.ctx.at.record.dom.attendanceitem.util.AttendanceItemConvertFactory;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.repo.PCLogOnInfoOfDailyRepo;
import nts.uk.ctx.at.record.dom.daily.optionalitemtime.AnyItemValueOfDailyRepo;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimeOfMonthlyRepository;
import nts.uk.ctx.at.record.dom.monthly.anyitem.AnyItemOfMonthlyRepository;
//import nts.uk.ctx.at.record.dom.monthly.flex.CheckBeforeCalcFlexChangeService;
import nts.uk.ctx.at.record.dom.monthly.roundingset.RoundingSetOfMonthlyRepository;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.GetVacationAddSet;
import nts.uk.ctx.at.record.dom.monthly.vtotalmethod.PayItemCountOfMonthlyRepository;
import nts.uk.ctx.at.record.dom.monthly.workform.flex.MonthlyAggrSetOfFlexRepository;
import nts.uk.ctx.at.record.dom.monthlyaggrmethod.legaltransferorder.LegalTransferOrderSetOfAggrMonthlyRepository;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.export.period.GetWeekPeriod;
import nts.uk.ctx.at.record.dom.optitem.OptionalItemRepository;
import nts.uk.ctx.at.record.dom.optitem.applicable.EmpConditionRepository;
import nts.uk.ctx.at.record.dom.optitem.calculation.FormulaRepository;
import nts.uk.ctx.at.record.dom.raisesalarytime.repo.SpecificDateAttrOfDailyPerforRepo;
import nts.uk.ctx.at.record.dom.standardtime.repository.AgreementDomainService;
import nts.uk.ctx.at.record.dom.standardtime.repository.AgreementMonthSettingRepository;
import nts.uk.ctx.at.record.dom.standardtime.repository.AgreementOperationSettingRepository;
import nts.uk.ctx.at.record.dom.statutoryworkinghours.DailyStatutoryWorkingHours;
import nts.uk.ctx.at.record.dom.statutoryworkinghours.monthly.GetWeekStart;
import nts.uk.ctx.at.record.dom.statutoryworkinghours.monthly.MonthlyStatutoryWorkingHours;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.record.dom.workrecord.actuallock.ActualLockRepository;
import nts.uk.ctx.at.record.dom.workrecord.closurestatus.ClosureStatusManagementRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerErrorRepository;
import nts.uk.ctx.at.record.dom.workrecord.monthcal.company.ComDeforLaborMonthActCalSetRepository;
import nts.uk.ctx.at.record.dom.workrecord.monthcal.company.ComFlexMonthActCalSetRepository;
import nts.uk.ctx.at.record.dom.workrecord.monthcal.company.ComRegulaMonthActCalSetRepository;
import nts.uk.ctx.at.record.dom.workrecord.monthcal.employee.ShaDeforLaborMonthActCalSetRepository;
import nts.uk.ctx.at.record.dom.workrecord.monthcal.employee.ShaFlexMonthActCalSetRepository;
import nts.uk.ctx.at.record.dom.workrecord.monthcal.employee.ShaRegulaMonthActCalSetRepository;
import nts.uk.ctx.at.record.dom.workrecord.monthcal.export.GetDeforAggrSet;
import nts.uk.ctx.at.record.dom.workrecord.monthcal.export.GetFlexAggrSet;
import nts.uk.ctx.at.record.dom.workrecord.monthcal.export.GetRegularAggrSet;
import nts.uk.ctx.at.record.dom.worktime.repository.TemporaryTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.worktime.repository.TimeLeavingOfDailyPerformanceRepository;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmpEmployeeAdapter;
import nts.uk.ctx.at.shared.dom.adapter.employment.ShareEmploymentAdapter;
import nts.uk.ctx.at.shared.dom.calculation.holiday.HolidayAddtionRepository;
import nts.uk.ctx.at.shared.dom.calculation.holiday.flex.FlexShortageLimitRepository;
import nts.uk.ctx.at.shared.dom.calculation.holiday.flex.InsufficientFlexHolidayMntRepository;
import nts.uk.ctx.at.shared.dom.outsideot.OutsideOTSettingRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.basicinfo.AnnLeaEmpBasicInfoRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.AnnLeaGrantRemDataRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.reserveleave.empinfo.grantremainingdata.RervLeaGrantRemDataRepository;
import nts.uk.ctx.at.shared.dom.scherec.totaltimes.TotalTimesRepository;
import nts.uk.ctx.at.shared.dom.scherec.totaltimes.algorithm.GetTotalTimesFromDailyRecord;
import nts.uk.ctx.at.shared.dom.statutory.worktime.UsageUnitSettingRepository;
import nts.uk.ctx.at.shared.dom.statutory.worktime.companyNew.ComRegularLaborTimeRepository;
import nts.uk.ctx.at.shared.dom.statutory.worktime.companyNew.ComTransLaborTimeRepository;
import nts.uk.ctx.at.shared.dom.statutory.worktime.employeeNew.ShainRegularWorkTimeRepository;
import nts.uk.ctx.at.shared.dom.statutory.worktime.employeeNew.ShainTransLaborTimeRepository;
import nts.uk.ctx.at.shared.dom.statutory.worktime.employmentNew.EmpRegularWorkTimeRepository;
import nts.uk.ctx.at.shared.dom.statutory.worktime.employmentNew.EmpTransWorkTimeRepository;
import nts.uk.ctx.at.shared.dom.statutory.worktime.workplaceNew.WkpRegularLaborTimeRepository;
import nts.uk.ctx.at.shared.dom.statutory.worktime.workplaceNew.WkpTransLaborTimeRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.AnnualPaidLeaveSettingRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.OperationStartSetDailyPerformRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensLeaveComSetRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.retentionyearly.EmploymentSettingRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.retentionyearly.RetentionYearlySettingRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.ComSubstVacationRepository;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionRepository;
import nts.uk.ctx.at.shared.dom.workrecord.monthlyresults.roleofovertimework.RoleOvertimeWorkRepository;
import nts.uk.ctx.at.shared.dom.workrecord.monthlyresults.roleopenperiod.RoleOfOpenPeriodRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.ctx.at.shared.dom.workrule.statutoryworktime.flex.GetFlexPredWorkTimeRepository;
import nts.uk.ctx.at.shared.dom.worktime.algorithm.getcommonset.GetCommonSet;
import nts.uk.ctx.at.shared.dom.worktime.common.subholtransferset.GetHolidayWorkAndTransferOrder;
import nts.uk.ctx.at.shared.dom.worktime.common.subholtransferset.GetOverTimeAndTransferOrder;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.LengthServiceRepository;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.YearHolidayRepository;

/**
 * 実装：月別集計が必要とするリポジトリ
 * @author shuichi_ishida
 */
@Stateless
@Getter
public class RepositoriesRequiredByMonthlyAggrImpl implements RepositoriesRequiredByMonthlyAggr {

	/** 並列化処理 */
	@Inject
	private ManagedParallelWithContext parallel;
	
	/** 社員の取得 */
	@Inject
	private EmpEmployeeAdapter empEmployee;

	/** 労働条件項目の取得 */
	@Inject
	private WorkingConditionItemRepository workingConditionItem;
	/** 労働条件の取得 */
	@Inject
	private WorkingConditionRepository workingCondition;
	/** 所属職場履歴の取得 */
	@Inject
	private AffWorkplaceAdapter affWorkplace;
	/** 所属雇用履歴の取得 */
	@Inject
	private ShareEmploymentAdapter employment;
	
	/** 日別実績の勤務種別の取得 */
	@Inject
	private WorkTypeOfDailyPerforRepository workTypeOfDaily;
	/** 日別実績の勤怠時間の取得 */
	@Inject
	private AttendanceTimeRepository attendanceTimeOfDaily;
	/** 日別実績の勤務情報の取得 */
	@Inject
	private WorkInformationRepository workInformationOfDaily;
	/** 日別実績の所属情報の取得 */
	@Inject
	private AffiliationInforOfDailyPerforRepository affiliationInfoOfDaily;
	/** 日別実績の出退勤の取得 */
	@Inject
	private TimeLeavingOfDailyPerformanceRepository timeLeavingOfDaily;
	/** 日別実績の臨時出退勤の取得 */
	@Inject
	private TemporaryTimeOfDailyPerformanceRepository temporaryTimeOfDaily;
	/** 日別実績の特定日区分の取得 */
	@Inject
	private SpecificDateAttrOfDailyPerforRepo specificDateAttrOfDaily;
	/** 日別実績のPCログオン情報 */
	@Inject
	private PCLogOnInfoOfDailyRepo PCLogonInfoOfDaily;
	/** 社員の日別積実績エラー一覧 */
	@Inject
	private EmployeeDailyPerErrorRepository employeeDailyError;
	/** 日別実績の任意項目の取得 */
	@Inject
	private AnyItemValueOfDailyRepo anyItemValueOfDaily;
	/** 年休付与残数データ */
	@Inject
	private AnnLeaGrantRemDataRepository annLeaGrantRemData;
	/** 積立年休付与残数データ */
	@Inject
	private RervLeaGrantRemDataRepository rsvLeaGrantRemData;
	/** 年休社員基本情報 */
	@Inject
	private AnnLeaEmpBasicInfoRepository annLeaEmpBasicInfo;
	/** 年休付与テーブル設定 */
	@Inject
	private YearHolidayRepository yearHoliday;
	/** 勤続年数テーブル */
	@Inject
	private LengthServiceRepository lengthService;
	/** 日別実績の運用開始設定 */
	@Inject
	private OperationStartSetDailyPerformRepository operationStartSet;

	/** 勤怠項目値変換 */
	@Inject
	private AttendanceItemConvertFactory attendanceItemConverter;
	
	/** 勤務情報の取得 */
	@Inject
	private WorkTypeRepository workType;
	/** 就業時間帯：共通設定の取得 */
	@Inject
	private GetCommonSet commonSet;
	/** 所定時間設定の取得 */
	@Inject
	private PredetemineTimeSettingRepository predetermineTimeSet;
	/** 締めの取得 */
	@Inject
	private ClosureRepository closure;
	/** 実績ロック */
	@Inject
	private ActualLockRepository actualLock;
	/** 締め状態管理 */
	@Inject
	private ClosureStatusManagementRepository closureStatusMng;
	/** 雇用に紐づく就業締めの取得 */
	@Inject
	private ClosureEmploymentRepository closureEmployment;
	
	/** 日の法定労働時間の取得 */
	@Inject
	private DailyStatutoryWorkingHours dailyStatutoryWorkingHours;
	/** 週・月の法定労働時間の取得*/
	@Inject
	private MonthlyStatutoryWorkingHours monthlyStatutoryWorkingHours;
	/** 社員別通常勤務労働時間 */
	@Inject
	private ShainRegularWorkTimeRepository shainRegularWorkTime;
	/** 社員別変形労働労働時間 */
	@Inject
	private ShainTransLaborTimeRepository shainTransLaborTime;
	/** 職場別通常勤務労働時間 */
	@Inject
	private WkpRegularLaborTimeRepository wkpRegularLaborTime;
	/** 職場別変形労働労働時間 */
	@Inject
	private WkpTransLaborTimeRepository wkpTransLaborTime;
	/** 雇用別通常勤務労働時間 */
	@Inject
	private EmpRegularWorkTimeRepository empRegularWorkTime;
	/** 雇用別変形労働労働時間 */
	@Inject
	private EmpTransWorkTimeRepository empTransWorkTime;
	/** 会社別通常勤務労働時間 */
	@Inject
	private ComRegularLaborTimeRepository comRegularLaborTime;
	/** 会社別変形労働労働時間 */
	@Inject
	private ComTransLaborTimeRepository comTransLaborTime;
	/** 社員のフレックス繰越上限時間を求める */
	//@Inject
	//private CheckBeforeCalcFlexChangeService checkBeforeCalcFlex;
	
	/** 月別実績の勤怠時間 */
	@Inject
	private AttendanceTimeOfMonthlyRepository attendanceTimeOfMonthly;
	/** 月別実績の任意項目 */
	@Inject
	private AnyItemOfMonthlyRepository anyItemOfMonthly;
	
	/** 集計設定の取得（通常勤務） */
	@Inject
	private GetRegularAggrSet regularAggrSet;
	/** 集計設定の取得（変形労働） */
	@Inject
	private GetDeforAggrSet deforAggrSet;
	/** 集計設定の取得（フレックス） */
	@Inject
	private GetFlexAggrSet flexAggrSet;
	
	/** 労働時間と日数の設定の利用単位の設定 */
	@Inject
	private UsageUnitSettingRepository usageUnitSetRepo;
	/** 通常勤務会社別月別実績集計設定 */
	@Inject
	private ComRegulaMonthActCalSetRepository comRegSetRepo;
	/** 通常勤務社員別月別実績集計設定 */
	@Inject
	private ShaRegulaMonthActCalSetRepository shaRegSetRepo;
	/** 変形労働会社別月別実績集計設定 */
	@Inject
	private ComDeforLaborMonthActCalSetRepository comIrgSetRepo;
	/** 変形労働社員別月別実績集計設定 */
	@Inject
	private ShaDeforLaborMonthActCalSetRepository shaIrgSetRepo;
	/** フレックス会社別月別実績集計設定 */
	@Inject
	private ComFlexMonthActCalSetRepository comFlexSetRepo;
	/** フレックス社員別月別実績集計設定 */
	@Inject
	private ShaFlexMonthActCalSetRepository shaFlexSetRepo;
	
	/** フレックス勤務の月別集計設定の取得 */
	@Inject
	private MonthlyAggrSetOfFlexRepository monthlyAggrSetOfFlex;
	/** フレックス勤務所定労働時間取得 */
	@Inject
	private GetFlexPredWorkTimeRepository flexPredWorktime;
	/** フレックス不足の年休補填管理 */
	@Inject
	private InsufficientFlexHolidayMntRepository insufficientFlex;
	/** フレックス不足の繰越上限管理 */
	@Inject
	private FlexShortageLimitRepository flexShortageLimit;
	/** 残業・振替の処理順序を取得する */
	@Inject
	private GetOverTimeAndTransferOrder overTimeAndTransferOrder;
	/** 休出・振替の処理順序を取得する */
	@Inject
	private GetHolidayWorkAndTransferOrder holidayWorkAndTransferOrder;
	/** 残業枠の役割 */
	@Inject
	private RoleOvertimeWorkRepository roleOverTimeFrame;
	/** 休出枠の役割 */
	@Inject
	private RoleOfOpenPeriodRepository roleHolidayWorkFrame;
	/** 月次集計の法定内振替順設定の取得 */
	@Inject
	private LegalTransferOrderSetOfAggrMonthlyRepository legalTransferOrderSetOfAggrMonthly;
	/** 休日加算設定 */
	@Inject
	private HolidayAddtionRepository holidayAddition;
	/** 休暇加算設定を取得する */
	@Inject
	private GetVacationAddSet vacationAddSet;
	/** 回数集計 */
	@Inject
	private TotalTimesRepository totalTimes;
	/** 任意項目 */
	@Inject
	private OptionalItemRepository optionalItem;
	/** 適用する雇用条件 */
	@Inject
	private EmpConditionRepository empCondition;
	/** 計算式 */
	@Inject
	private FormulaRepository formula;
	/** 年休設定 */
	@Inject
	private AnnualPaidLeaveSettingRepository annualPaidLeaveSet;
	/** 積立年休設定 */
	@Inject
	private RetentionYearlySettingRepository retentionYearlySet;
	/** 雇用積立年休設定 */
	@Inject
	private EmploymentSettingRepository employmentSet;
	/** 振休管理設定 */
	@Inject
	private ComSubstVacationRepository substVacationMng;
	/** 代休管理設定 */
	@Inject
	private CompensLeaveComSetRepository compensLeaveMng;
	
	/** 週開始の取得 */
	@Inject
	private GetWeekStart weekStart;
	/** 週集計期間を取得する */
	@Inject
	private GetWeekPeriod weekPeriod;
	
	/** 時間外超過設定の取得 */
	@Inject
	private OutsideOTSettingRepository outsideOTSet;
	
	/** ドメインサービス：36協定 */
	@Inject
	private AgreementDomainService agreementDomainService;
	/** 36協定運用設定の取得 */
	@Inject
	private AgreementOperationSettingRepository agreementOperationSet;
	/** 36協定年月設定の取得 */
	@Inject
	private AgreementMonthSettingRepository agreementMonthSet;
	
	/** 月別実績の給与項目カウントの取得 */
	@Inject
	private PayItemCountOfMonthlyRepository payItemCountOfMonthly;
	/** 月別実績の丸め設定の取得 */
	@Inject
	private RoundingSetOfMonthlyRepository roundingSetOfMonthly;

	/** 日別実績から回数集計結果を取得する */
	@Inject
	private GetTotalTimesFromDailyRecord timeAndCountFromDailyRecord;
}

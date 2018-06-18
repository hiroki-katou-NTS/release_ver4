package nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work;

import nts.uk.ctx.at.record.dom.actualworkinghours.repository.AttendanceTimeRepository;
import nts.uk.ctx.at.record.dom.adapter.employment.SyEmploymentAdapter;
import nts.uk.ctx.at.record.dom.adapter.workplace.affiliate.AffWorkplaceAdapter;
import nts.uk.ctx.at.record.dom.affiliationinformation.repository.AffiliationInforOfDailyPerforRepository;
import nts.uk.ctx.at.record.dom.affiliationinformation.repository.WorkTypeOfDailyPerforRepository;
import nts.uk.ctx.at.record.dom.attendanceitem.util.AttendanceItemConvertFactory;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.repo.PCLogOnInfoOfDailyRepo;
import nts.uk.ctx.at.record.dom.daily.optionalitemtime.AnyItemValueOfDailyRepo;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimeOfMonthlyRepository;
import nts.uk.ctx.at.record.dom.monthly.anyitem.AnyItemOfMonthlyRepository;
import nts.uk.ctx.at.record.dom.monthly.roundingset.RoundingSetOfMonthlyRepository;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.GetVacationAddSet;
import nts.uk.ctx.at.record.dom.monthly.vtotalmethod.PayItemCountOfMonthlyRepository;
import nts.uk.ctx.at.record.dom.monthly.workform.flex.MonthlyAggrSetOfFlexRepository;
import nts.uk.ctx.at.record.dom.monthlyaggrmethod.legaltransferorder.LegalTransferOrderSetOfAggrMonthlyRepository;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.export.period.GetWeekPeriod;
import nts.uk.ctx.at.record.dom.optitem.OptionalItemRepository;
import nts.uk.ctx.at.record.dom.raisesalarytime.repo.SpecificDateAttrOfDailyPerforRepo;
import nts.uk.ctx.at.record.dom.standardtime.repository.AgreementDomainService;
import nts.uk.ctx.at.record.dom.standardtime.repository.AgreementMonthSettingRepository;
import nts.uk.ctx.at.record.dom.standardtime.repository.AgreementOperationSettingRepository;
import nts.uk.ctx.at.record.dom.statutoryworkinghours.DailyStatutoryWorkingHours;
import nts.uk.ctx.at.record.dom.statutoryworkinghours.monthly.GetWeekStart;
import nts.uk.ctx.at.record.dom.statutoryworkinghours.monthly.MonthlyStatutoryWorkingHours;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerErrorRepository;
import nts.uk.ctx.at.record.dom.workrecord.monthcal.export.GetDeforAggrSet;
import nts.uk.ctx.at.record.dom.workrecord.monthcal.export.GetFlexAggrSet;
import nts.uk.ctx.at.record.dom.workrecord.monthcal.export.GetRegularAggrSet;
import nts.uk.ctx.at.record.dom.worktime.repository.TemporaryTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.worktime.repository.TimeLeavingOfDailyPerformanceRepository;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmpEmployeeAdapter;
import nts.uk.ctx.at.shared.dom.calculation.holiday.HolidayAddtionRepository;
import nts.uk.ctx.at.shared.dom.outsideot.OutsideOTSettingRepository;
import nts.uk.ctx.at.shared.dom.scherec.totaltimes.TotalTimesRepository;
import nts.uk.ctx.at.shared.dom.scherec.totaltimes.algorithm.GetTotalTimesFromDailyRecord;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionRepository;
import nts.uk.ctx.at.shared.dom.workrecord.monthlyresults.roleofovertimework.RoleOvertimeWorkRepository;
import nts.uk.ctx.at.shared.dom.workrecord.monthlyresults.roleopenperiod.RoleOfOpenPeriodRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.ctx.at.shared.dom.workrule.statutoryworktime.flex.GetFlexPredWorkTimeRepository;
import nts.uk.ctx.at.shared.dom.worktime.algorithm.getcommonset.GetCommonSet;
import nts.uk.ctx.at.shared.dom.worktime.common.subholtransferset.GetHolidayWorkAndTransferOrder;
import nts.uk.ctx.at.shared.dom.worktime.common.subholtransferset.GetOverTimeAndTransferOrder;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;

/**
 * 月別集計が必要とするリポジトリ
 * @author shuichu_ishida
 */
public interface RepositoriesRequiredByMonthlyAggr {

	/** 社員の取得 */
	EmpEmployeeAdapter getEmpEmployee();

	/** 労働条件項目の取得 */
	WorkingConditionItemRepository getWorkingConditionItem();
	/** 労働条件の取得 */
	WorkingConditionRepository getWorkingCondition();
	/** 所属職場履歴の取得 */
	AffWorkplaceAdapter getAffWorkplace();
	/** 所属雇用履歴の取得 */
	SyEmploymentAdapter getSyEmployment();
	
	/** 日別実績の勤務種別の取得 */
	WorkTypeOfDailyPerforRepository getWorkTypeOfDaily();
	/** 日別実績の勤怠時間の取得 */
	AttendanceTimeRepository getAttendanceTimeOfDaily();
	/** 日別実績の勤務情報の取得 */
	WorkInformationRepository getWorkInformationOfDaily();
	/** 日別実績の所属情報の取得 */
	AffiliationInforOfDailyPerforRepository getAffiliationInfoOfDaily();
	/** 日別実績の出退勤の取得 */
	TimeLeavingOfDailyPerformanceRepository getTimeLeavingOfDaily();
	/** 日別実績の臨時出退勤の取得 */
	TemporaryTimeOfDailyPerformanceRepository getTemporaryTimeOfDaily();
	/** 日別実績の特定日区分の取得 */
	SpecificDateAttrOfDailyPerforRepo getSpecificDateAttrOfDaily();
	/** 日別実績のPCログオン情報 */
	PCLogOnInfoOfDailyRepo getPCLogonInfoOfDaily();
	/** 社員の日別積実績エラー一覧 */
	EmployeeDailyPerErrorRepository getEmployeeDailyError();
	/** 日別実績の任意項目の取得 */
	AnyItemValueOfDailyRepo getAnyItemValueOfDaily();

	/** 勤怠項目値変換 */
	AttendanceItemConvertFactory getAttendanceItemConverter();
	
	/** 勤務情報の取得 */
	WorkTypeRepository getWorkType();
	/** 就業時間帯：共通設定の取得 */
	GetCommonSet getCommonSet();
	/** 所定時間設定の取得 */
	PredetemineTimeSettingRepository getPredetermineTimeSet();
	/** 締めの取得 */
	ClosureRepository getClosure();

	/** 日の法定労働時間の取得 */
	DailyStatutoryWorkingHours getDailyStatutoryWorkingHours();
	/** 週・月の法定労働時間の取得*/
	MonthlyStatutoryWorkingHours getMonthlyStatutoryWorkingHours();
	
	/** 月別実績の勤怠時間 */
	AttendanceTimeOfMonthlyRepository getAttendanceTimeOfMonthly();
	/** 月別実績の任意項目 */
	AnyItemOfMonthlyRepository getAnyItemOfMonthly();
	
	/** 集計設定の取得（通常勤務） */
	GetRegularAggrSet getRegularAggrSet();
	/** 集計設定の取得（変形労働） */
	GetDeforAggrSet getDeforAggrSet();
	/** 集計設定の取得（フレックス） */
	GetFlexAggrSet getFlexAggrSet();
	/** フレックス勤務の月別集計設定の取得 */
	MonthlyAggrSetOfFlexRepository getMonthlyAggrSetOfFlex();
	/** フレックス勤務所定労働時間取得 */
	GetFlexPredWorkTimeRepository getFlexPredWorktime();
	/** 残業・振替の処理順序を取得する */
	GetOverTimeAndTransferOrder getOverTimeAndTransferOrder();
	/** 休出・振替の処理順序を取得する */
	GetHolidayWorkAndTransferOrder getHolidayWorkAndTransferOrder();
	/** 残業枠の役割 */
	RoleOvertimeWorkRepository getRoleOverTimeFrame();
	/** 休出枠の役割 */
	RoleOfOpenPeriodRepository getRoleHolidayWorkFrame();
	/** 月次集計の法定内振替順設定の取得 */
	LegalTransferOrderSetOfAggrMonthlyRepository getLegalTransferOrderSetOfAggrMonthly();
	/** 休日加算設定 */
	HolidayAddtionRepository getHolidayAddition();
	/** 休暇加算設定を取得する */
	GetVacationAddSet getVacationAddSet();
	/** 回数集計 */
	TotalTimesRepository getTotalTimes();
	/** 任意項目 */
	OptionalItemRepository getOptionalItem();

	/** 週開始の取得 */
	GetWeekStart getWeekStart();
	/** 週集計期間を取得する */
	GetWeekPeriod getWeekPeriod();
	
	/** 時間外超過設定の取得 */
	OutsideOTSettingRepository getOutsideOTSet();

	/** ドメインサービス：36協定 */
	AgreementDomainService getAgreementDomainService();
	/** 36協定運用設定の取得 */
	AgreementOperationSettingRepository getAgreementOperationSet();
	/** 36協定年月設定の取得 */
	AgreementMonthSettingRepository getAgreementMonthSet();
	
	/** 月別実績の給与項目カウントの取得 */
	PayItemCountOfMonthlyRepository getPayItemCountOfMonthly();
	/** 月別実績の丸め設定の取得 */
	RoundingSetOfMonthlyRepository getRoundingSetOfMonthly();
	
	/** 日別実績から回数集計結果を取得する */
	GetTotalTimesFromDailyRecord getTimeAndCountFromDailyRecord();
}

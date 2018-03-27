package nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work;

import nts.uk.ctx.at.record.dom.actualworkinghours.repository.AttendanceTimeRepository;
import nts.uk.ctx.at.record.dom.adapter.employment.SyEmploymentAdapter;
import nts.uk.ctx.at.record.dom.adapter.workplace.affiliate.AffWorkplaceAdapter;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimeOfMonthlyRepository;
import nts.uk.ctx.at.record.dom.monthly.roundingset.RoundingSetOfMonthlyRepository;
import nts.uk.ctx.at.record.dom.monthly.vtotalmethod.PayItemCountOfMonthlyRepository;
import nts.uk.ctx.at.record.dom.monthlyaggrmethod.GetAggrSettingMonthly;
import nts.uk.ctx.at.record.dom.monthlyaggrmethod.legaltransferorder.LegalTransferOrderSetOfAggrMonthlyRepository;
import nts.uk.ctx.at.record.dom.raisesalarytime.repo.SpecificDateAttrOfDailyPerforRepo;
import nts.uk.ctx.at.record.dom.standardtime.repository.AgreementDomainService;
import nts.uk.ctx.at.record.dom.standardtime.repository.AgreementMonthSettingRepository;
import nts.uk.ctx.at.record.dom.standardtime.repository.AgreementOperationSettingRepository;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerErrorRepository;
import nts.uk.ctx.at.record.dom.worktime.repository.TemporaryTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.worktime.repository.TimeLeavingOfDailyPerformanceRepository;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmpEmployeeAdapter;
import nts.uk.ctx.at.shared.dom.calculation.holiday.HolidayAddtionRepository;
import nts.uk.ctx.at.shared.dom.outsideot.OutsideOTSettingRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.AnnualPaidLeaveSettingRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.retentionyearly.RetentionYearlySettingRepository;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionRepository;
import nts.uk.ctx.at.shared.dom.workrule.statutoryworktime.GetOfStatutoryWorkTime;
import nts.uk.ctx.at.shared.dom.workrule.statutoryworktime.GetWeekStart;
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
	
	/** 日別実績の勤怠時間の取得 */
	AttendanceTimeRepository getAttendanceTimeOfDaily();
	
	/** 日別実績の勤務情報の取得 */
	WorkInformationRepository getWorkInformationOfDaily();

	/** 日別実績の出退勤の取得 */
	TimeLeavingOfDailyPerformanceRepository getTimeLeavingOfDaily();

	/** 日別実績の臨時出退勤の取得 */
	TemporaryTimeOfDailyPerformanceRepository getTemporaryTimeOfDaily();
	
	/** 日別実績の特定日区分の取得 */
	SpecificDateAttrOfDailyPerforRepo getSpecificDateAttrOfDaily();
	
	/** 勤務情報の取得 */
	WorkTypeRepository getWorkType();
	
	/** 就業時間帯：共通設定の取得 */
	GetCommonSet getCommonSet();
	
	/** 所定時間設定の取得 */
	PredetemineTimeSettingRepository getPredetermineTimeSet();

	/** 社員の日別積実績エラー一覧 */
	EmployeeDailyPerErrorRepository getEmployeeDailyError();
	
	/** 月別実績の勤怠時間の取得 */
	AttendanceTimeOfMonthlyRepository getAttendanceTimeOfMonthly();
	
	/** 月別実績集計設定の取得 */
	GetAggrSettingMonthly getAggrSettingMonthly();

	/** 月次集計の法定内振替順設定の取得 */
	LegalTransferOrderSetOfAggrMonthlyRepository getLegalTransferOrderSetOfAggrMonthly();
	
	/** 36協定運用設定の取得 */
	AgreementOperationSettingRepository getAgreementOperationSet();
	
	/** ドメインサービス：36協定 */
	AgreementDomainService getAgreementDomainService();
	
	/** 36協定年月設定の取得 */
	AgreementMonthSettingRepository getAgreementMonthSet();
	
	/** 月別実績の縦計方法の取得 */
	//*****(未)　特定日の振り分け方法の設計待ち。
	
	/** 月別実績の給与項目カウントの取得 */
	PayItemCountOfMonthlyRepository getPayItemCountOfMonthly();
	
	/** 月別実績の丸め設定の取得 */
	RoundingSetOfMonthlyRepository getRoundingSetOfMonthly();
	
	/** 法定労働時間の取得 */
	GetOfStatutoryWorkTime getGetOfStatutoryWorkTime();
	
	/** 時間外超過設定の取得 */
	OutsideOTSettingRepository getOutsideOTSet();
	
	/** 残業・振替の処理順序を取得する */
	GetOverTimeAndTransferOrder getOverTimeAndTransferOrder();
	/** 休出・振替の処理順序を取得する */
	GetHolidayWorkAndTransferOrder getHolidayWorkAndTransferOrder();
	
	/** 休日加算設定 */
	HolidayAddtionRepository getHolidayAddition();
	
	/** 年休設定 */
	AnnualPaidLeaveSettingRepository getAnnualPaidLeaveSet();
	
	/** 積立年休設定 */
	RetentionYearlySettingRepository getRetentionYearlySet();
	
	/** 特別休暇設定 */
	//SpecialHolidayRepository getSpecialHolidaySet();
	
	/** 週開始の取得 */
	GetWeekStart getGetWeekStart();
}

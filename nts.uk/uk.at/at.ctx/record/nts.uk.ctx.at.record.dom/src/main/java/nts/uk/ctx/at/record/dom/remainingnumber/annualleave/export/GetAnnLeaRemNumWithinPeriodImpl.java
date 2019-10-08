package nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimeOfMonthlyRepository;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.MonAggrCompanySettings;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.MonAggrEmployeeSettings;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.MonthlyCalculatingDailys;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.param.AggrResultOfAnnualLeave;
import nts.uk.ctx.at.record.dom.workrecord.closurestatus.ClosureStatusManagementRepository;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmpEmployeeAdapter;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.InterimRemainOffMonthProcess;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.basicinfo.AnnLeaEmpBasicInfoRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.basicinfo.CalcNextAnnualLeaveGrantDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.AnnLeaGrantRemDataRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.maxdata.AnnLeaMaxDataRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.interim.TmpAnnualHolidayMngRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.interim.TmpAnnualLeaveMngWork;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemainRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.AnnualPaidLeaveSettingRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.OperationStartSetDailyPerformRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.GetClosureStartForEmployee;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.GrantYearHolidayRepository;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.LengthServiceRepository;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.YearHolidayRepository;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 実装：期間中の年休残数を取得
 * @author shuichi_ishida
 */
@Stateless
public class GetAnnLeaRemNumWithinPeriodImpl implements GetAnnLeaRemNumWithinPeriod {

	/** 社員の取得 */
	@Inject
	private EmpEmployeeAdapter empEmployee;
	/** 年休社員基本情報 */
	@Inject
	private AnnLeaEmpBasicInfoRepository annLeaEmpBasicInfoRepo;
	/** 年休付与テーブル設定 */
	@Inject
	private YearHolidayRepository yearHolidayRepo;
	/** 勤続年数テーブル */
	@Inject
	private LengthServiceRepository lengthServiceRepo;
	/** 年休設定 */
	@Inject
	private AnnualPaidLeaveSettingRepository annualPaidLeaveSet;
	/** 年休付与残数データ */
	@Inject
	private AnnLeaGrantRemDataRepository annLeaGrantRemDataRepo;
	/** 年休上限データ */
	@Inject
	private AnnLeaMaxDataRepository annLeaMaxDataRepo;
	/** 社員に対応する締め開始日を取得する */
	@Inject
	private GetClosureStartForEmployee getClosureStartForEmployee;
	/** 次回年休付与日を計算 */
	@Inject
	private CalcNextAnnualLeaveGrantDate calcNextAnnualLeaveGrantDate;
	/** 月次処理用の暫定残数管理データを作成する */
	@Inject
	private InterimRemainOffMonthProcess interimRemOffMonth;
	/** 暫定年休管理データを作成する */
	@Inject
	private CreateInterimAnnualMngData createInterimAnnual;
	/** 暫定残数管理データ */
	@Inject
	private InterimRemainRepository interimRemainRepo;
	/** 暫定年休管理データ */
	@Inject
	private TmpAnnualHolidayMngRepository tmpAnnualLeaveMng;
	/** 月別実績の勤怠時間 */
	@Inject
	private AttendanceTimeOfMonthlyRepository attendanceTimeOfMonthlyRepo;
	/** 期間中の年休残数を取得 */
	@Inject
	private GetAnnLeaRemNumWithinPeriod getAnnLeaRemNumWithinPeriod;
	/** 締め状態管理 */
	@Inject
	private ClosureStatusManagementRepository closureSttMngRepo;
	/** 年休出勤率を計算する */
	@Inject
	private CalcAnnLeaAttendanceRate calcAnnLeaAttendanceRate;
	/** 年休付与テーブル */
	@Inject
	private GrantYearHolidayRepository grantYearHolidayRepo;
	/** 日別実績の運用開始設定 */
	@Inject
	private OperationStartSetDailyPerformRepository operationStartSetRepo;
	
	/** 期間中の年休残数を取得 */
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Optional<AggrResultOfAnnualLeave> algorithm(
			String companyId, String employeeId, DatePeriod aggrPeriod, InterimRemainMngMode mode,
			GeneralDate criteriaDate, boolean isGetNextMonthData, boolean isCalcAttendanceRate,
			Optional<Boolean> isOverWriteOpt, Optional<List<TmpAnnualLeaveMngWork>> forOverWriteListOpt,
			Optional<AggrResultOfAnnualLeave> prevAnnualLeaveOpt, Optional<Boolean> noCheckStartDate) {

		GetAnnLeaRemNumWithinPeriodProc proc = new GetAnnLeaRemNumWithinPeriodProc(
				this.empEmployee,
				this.annLeaEmpBasicInfoRepo,
				this.yearHolidayRepo,
				this.lengthServiceRepo,
				this.annualPaidLeaveSet,
				this.annLeaGrantRemDataRepo,
				this.annLeaMaxDataRepo,
				this.getClosureStartForEmployee,
				this.calcNextAnnualLeaveGrantDate,
				this.interimRemOffMonth,
				this.createInterimAnnual,
				this.interimRemainRepo,
				this.tmpAnnualLeaveMng,
				this.attendanceTimeOfMonthlyRepo,
				this.getAnnLeaRemNumWithinPeriod,
				this.closureSttMngRepo,
				this.calcAnnLeaAttendanceRate,
				this.grantYearHolidayRepo,
				this.operationStartSetRepo);
		return proc.algorithm(companyId, employeeId, aggrPeriod, mode, criteriaDate,
				isGetNextMonthData, isCalcAttendanceRate,
				isOverWriteOpt, forOverWriteListOpt, prevAnnualLeaveOpt, noCheckStartDate);
	}

	/** 期間中の年休残数を取得　（月別集計用） */
	@Override
	public Optional<AggrResultOfAnnualLeave> algorithm(String companyId, String employeeId, DatePeriod aggrPeriod,
			InterimRemainMngMode mode, GeneralDate criteriaDate, boolean isGetNextMonthData,
			boolean isCalcAttendanceRate, Optional<Boolean> isOverWriteOpt,
			Optional<List<TmpAnnualLeaveMngWork>> forOverWriteListOpt,
			Optional<AggrResultOfAnnualLeave> prevAnnualLeaveOpt,
			boolean noCheckStartDate,
			Optional<MonAggrCompanySettings> companySets,
			Optional<MonAggrEmployeeSettings> employeeSets,
			Optional<MonthlyCalculatingDailys> monthlyCalcDailys) {

		GetAnnLeaRemNumWithinPeriodProc proc = new GetAnnLeaRemNumWithinPeriodProc(
				this.empEmployee,
				this.annLeaEmpBasicInfoRepo,
				this.yearHolidayRepo,
				this.lengthServiceRepo,
				this.annualPaidLeaveSet,
				this.annLeaGrantRemDataRepo,
				this.annLeaMaxDataRepo,
				this.getClosureStartForEmployee,
				this.calcNextAnnualLeaveGrantDate,
				this.interimRemOffMonth,
				this.createInterimAnnual,
				this.interimRemainRepo,
				this.tmpAnnualLeaveMng,
				this.attendanceTimeOfMonthlyRepo,
				this.getAnnLeaRemNumWithinPeriod,
				this.closureSttMngRepo,
				this.calcAnnLeaAttendanceRate,
				this.grantYearHolidayRepo,
				this.operationStartSetRepo);
		return proc.algorithm(companyId, employeeId, aggrPeriod, mode, criteriaDate,
				isGetNextMonthData, isCalcAttendanceRate,
				isOverWriteOpt, forOverWriteListOpt, prevAnnualLeaveOpt,
				noCheckStartDate, companySets, employeeSets, monthlyCalcDailys);
	}

	@Override
	public GetAnnLeaRemNumWithinPeriodProc createProc() {
		GetAnnLeaRemNumWithinPeriodProc proc = new GetAnnLeaRemNumWithinPeriodProc(
				this.empEmployee,
				this.annLeaEmpBasicInfoRepo,
				this.yearHolidayRepo,
				this.lengthServiceRepo,
				this.annualPaidLeaveSet,
				this.annLeaGrantRemDataRepo,
				this.annLeaMaxDataRepo,
				this.getClosureStartForEmployee,
				this.calcNextAnnualLeaveGrantDate,
				this.interimRemOffMonth,
				this.createInterimAnnual,
				this.interimRemainRepo,
				this.tmpAnnualLeaveMng,
				this.attendanceTimeOfMonthlyRepo,
				this.getAnnLeaRemNumWithinPeriod,
				this.closureSttMngRepo,
				this.calcAnnLeaAttendanceRate,
				this.grantYearHolidayRepo,
				this.operationStartSetRepo);
		return proc;
	}
}

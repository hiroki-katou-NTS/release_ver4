package nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.adapter.basicschedule.BasicScheduleAdapter;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimeOfMonthlyRepository;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.GetVacationAddSet;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.MonAggrCompanySettings;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.MonthlyCalculatingDailys;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemain;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemainRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 実装：暫定年休管理データを作成する
 * @author shuichu_ishida
 */
@Stateless
public class CreateTempAnnualLeaveManagementImpl implements CreateTempAnnualLeaveManagement {

	/** 日別実績の勤務情報 */
	@Inject
	private WorkInformationRepository workInformationRepo;
	/** 勤務予定基本情報 */
	@Inject
	private BasicScheduleAdapter basicScheduleAdapter;
	/** 暫定年休管理データ */
	@Inject
	private InterimRemainRepository tempAnnualLeaveMngRepo;
	/** 勤務情報の取得 */
	@Inject
	public WorkTypeRepository workTypeRepo;
	/** 休暇加算設定の取得 */
	@Inject
	private GetVacationAddSet getVacationAddSet;
	/** 月別実績の勤怠時間 */
	@Inject
	private AttendanceTimeOfMonthlyRepository attendanceTimeOfMonthlyRepo;
	
	/** 暫定年休管理データを作成する */
	@Override
	public List<InterimRemain> algorithm(String companyId, String employeeId, DatePeriod period,
			InterimRemainMngMode mode) {
		
		CreateTempAnnLeaMngProc proc = new CreateTempAnnLeaMngProc(
				this.workInformationRepo,
				this.basicScheduleAdapter,
				this.tempAnnualLeaveMngRepo,
				this.workTypeRepo,
				this.getVacationAddSet,
				this.attendanceTimeOfMonthlyRepo);
		return proc.algorithm(companyId, employeeId, period, mode);
	}
	
	/** 暫定年休管理データを作成する　（月別集計用） */
	@Override
	public List<InterimRemain> algorithm(String companyId, String employeeId, DatePeriod period,
			InterimRemainMngMode mode, Optional<MonAggrCompanySettings> companySets,
			Optional<MonthlyCalculatingDailys> monthlyCalcDailys) {
		
		CreateTempAnnLeaMngProc proc = new CreateTempAnnLeaMngProc(
				this.workInformationRepo,
				this.basicScheduleAdapter,
				this.tempAnnualLeaveMngRepo,
				this.workTypeRepo,
				this.getVacationAddSet,
				this.attendanceTimeOfMonthlyRepo);
		return proc.algorithm(companyId, employeeId, period, mode, companySets, monthlyCalcDailys);
	}
}

package nts.uk.ctx.at.record.dom.monthlyclosureupdateprocess.remainnumberprocess;

import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthlyclosureupdateprocess.remainnumberprocess.annualleave.AnnualLeaveProcess;
import nts.uk.ctx.at.record.dom.monthlyclosureupdateprocess.remainnumberprocess.compensatoryholiday.CompensatoryHolidayProcess;
import nts.uk.ctx.at.record.dom.monthlyclosureupdateprocess.remainnumberprocess.specialholiday.SpecialHolidayProcess;
import nts.uk.ctx.at.record.dom.monthlyclosureupdateprocess.remainnumberprocess.substitutionholiday.SubstitutionHolidayProcess;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.AggrPeriodEachActualClosure;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.DailyInterimRemainMngData;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.InterimRemainOffMonthProcess;
import nts.uk.shr.com.context.AppContexts;

/**
 * 
 * @author HungTT - 月締め更新残数処理
 *
 */

@Stateless
public class MonthlyClosureRemainNumProcess {

	@Inject
	private InterimRemainOffMonthProcess interimRemainOffMonthProc;
	
	@Inject
	private AnnualLeaveProcess annualLeaveProc;
	
	@Inject
	private SubstitutionHolidayProcess substitutionProc;
	
	@Inject
	private CompensatoryHolidayProcess compensatoryProc;

	@Inject
	private SpecialHolidayProcess specialProc;

	/**
	 * 残数処理
	 * @param period 実締め毎集計期間
	 * @param empId 社員ID
	 * @param attTimeMonthly 月別実績の勤怠時間
	 */
	public void remainNumberProcess(AggrPeriodEachActualClosure period, String empId,
			AttendanceTimeOfMonthly attTimeMonthly) {

		String companyId = AppContexts.user().companyId();
		
		// 「月次処理用の暫定残数管理データを作成する」を実行する
		Map<GeneralDate, DailyInterimRemainMngData> interimRemainMngMap =
				this.interimRemainOffMonthProc.monthInterimRemainData(companyId, empId, period.getPeriod());
		
		// 年休（・積立年休）処理
		annualLeaveProc.annualHolidayProcess(period, empId, interimRemainMngMap, attTimeMonthly);
		
		// 振休処理
		substitutionProc.substitutionHolidayProcess(period, empId, interimRemainMngMap);
		
		// 代休処理
		compensatoryProc.compensatoryHolidayProcess(period, empId, interimRemainMngMap);
		
		// 特別休暇処理
		specialProc.specialHolidayProcess(period, empId, interimRemainMngMap, false);//自動付与区分: とりあえず固定値：　False、二次開発に対応します
	}
}

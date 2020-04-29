package nts.uk.ctx.at.record.dom.monthlyclosureupdateprocess.remainnumberprocess.annualleave;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthlyclosureupdateprocess.remainnumberprocess.annualleave.calculateremainnum.RemainAnnualLeaveCalculation;
import nts.uk.ctx.at.record.dom.monthlyclosureupdateprocess.remainnumberprocess.annualleave.deletetempdata.AnnualLeaveTempDataDeleting;
import nts.uk.ctx.at.record.dom.monthlyclosureupdateprocess.remainnumberprocess.annualleave.deletetempreserveannual.RsvAnnualLeaveTempDataDeleting;
import nts.uk.ctx.at.record.dom.monthlyclosureupdateprocess.remainnumberprocess.annualleave.updateremainnum.RemainAnnualLeaveUpdating;
import nts.uk.ctx.at.record.dom.monthlyclosureupdateprocess.remainnumberprocess.annualleave.updatereserveannual.RemainReserveAnnualLeaveUpdating;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.AggrPeriodEachActualClosure;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.param.AggrResultOfAnnAndRsvLeave;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.DailyInterimRemainMngData;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.interim.TmpAnnualHolidayMngRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemain;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemainRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.reserveleave.interim.TmpResereLeaveMngRepository;

/**
 * 
 * @author HungTT - <<Work>> 年休処理
 *
 */

@Stateless
public class AnnualLeaveProcess {

	@Inject
	private RemainAnnualLeaveCalculation remainHolidayCalculation;

	@Inject
	private RemainAnnualLeaveUpdating remainHolidayUpdating;

	@Inject
	private AnnualLeaveTempDataDeleting tmpAnnualLeaveDeleting;

	@Inject
	private RemainReserveAnnualLeaveUpdating rsvRemainAnnualLeaveUpdating;

	@Inject
	private RsvAnnualLeaveTempDataDeleting tmpRsvAnnualLeaveDeleting;
	
	@Inject 
	private TmpAnnualHolidayMngRepository annualHolidayMngRepository;
	
	@Inject 
	private InterimRemainRepository interimRemainRepository;
	
	@Inject 
	private TmpResereLeaveMngRepository leaveMngRepository;

	/**
	 * 年休残数処理
	 * @param period 実締め毎集計期間
	 * @param empId 社員ID
	 * @param interimRemainMngMap 暫定管理データリスト
	 * @param attTimeMonthly 月別実績の勤怠時間
	 */
	public void annualHolidayProcess(AggrPeriodEachActualClosure period, String empId,
			Map<GeneralDate, DailyInterimRemainMngData> interimRemainMngMap, AttendanceTimeOfMonthly attTimeMonthly) {
		
		// 年休残数計算
		AggrResultOfAnnAndRsvLeave output = remainHolidayCalculation.calculateRemainAnnualHoliday(
				period, empId, interimRemainMngMap, attTimeMonthly);
		
		List<InterimRemain> interimRemain = interimRemainRepository.findByEmployeeID(empId);
		String mngId = !interimRemain.isEmpty() ? interimRemain.get(0).getRemainManaID() :null;
		
		// 年休残数更新
		if (output.getAnnualLeave().isPresent())
			remainHolidayUpdating.updateRemainAnnualLeave(output.getAnnualLeave().get(), period, empId);
		
		// 年休暫定データ削除
		tmpAnnualLeaveDeleting.deleteTempAnnualLeaveData(empId, period.getPeriod());
		
		// Fix bug 109524
		if(mngId !=null)
		annualHolidayMngRepository.deleteById(mngId);
		
		// 積立年休残数更新
		if (output.getReserveLeave().isPresent())
			rsvRemainAnnualLeaveUpdating.updateReservedAnnualLeaveRemainNumber(output.getReserveLeave().get(), period, empId);
		
		// 積立年休暫定データ削除
		tmpRsvAnnualLeaveDeleting.deleteTempRsvAnnualLeaveData(empId, period.getPeriod());
		// Fix bug 109524
		if(mngId !=null)
		leaveMngRepository.deleteById(mngId);
	}
}

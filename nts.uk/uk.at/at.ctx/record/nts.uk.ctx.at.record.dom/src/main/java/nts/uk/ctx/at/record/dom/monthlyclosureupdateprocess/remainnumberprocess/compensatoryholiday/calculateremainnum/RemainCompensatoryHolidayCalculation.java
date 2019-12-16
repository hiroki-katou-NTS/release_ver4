package nts.uk.ctx.at.record.dom.monthlyclosureupdateprocess.remainnumberprocess.compensatoryholiday.calculateremainnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.AggrPeriodEachActualClosure;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.DailyInterimRemainMngData;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.BreakDayOffMngInPeriodQuery;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.BreakDayOffRemainMngOfInPeriod;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.BreakDayOffRemainMngParam;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.interim.InterimBreakMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.interim.InterimDayOffMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemain;
import nts.uk.shr.com.context.AppContexts;

/**
 * 
 * @author HungTT - <<Work>> 代休残数計算
 *
 */

@Stateless
public class RemainCompensatoryHolidayCalculation {

	@Inject
	private BreakDayOffMngInPeriodQuery breakDayoffMng;

	/**
	 * 代休残数計算
	 * @param period 実締め毎集計期間
	 * @param empId 社員ID
	 * @param interimRemainMngMap 暫定管理データリスト
	 * @return 代休の集計結果
	 */
	public BreakDayOffRemainMngOfInPeriod calculateRemainCompensatory(AggrPeriodEachActualClosure period,
			String empId, Map<GeneralDate, DailyInterimRemainMngData> interimRemainMngMap) {
		
		String companyId = AppContexts.user().companyId();

		// 暫定残数データを休出・代休に絞り込む
		List<InterimRemain> interimMng = new ArrayList<>();
		List<InterimBreakMng> breakMng = new ArrayList<>();
		List<InterimDayOffMng> dayOffMng = new ArrayList<>();
		for (val interimRemainMng : interimRemainMngMap.values()){
			if (interimRemainMng.getRecAbsData().size() <= 0) continue;
			interimMng.addAll(interimRemainMng.getRecAbsData());

			// 休出
			if (interimRemainMng.getBreakData().isPresent()){
				breakMng.add(interimRemainMng.getBreakData().get());
			}
			
			// 代休
			if (interimRemainMng.getDayOffData().isPresent()){
				dayOffMng.add(interimRemainMng.getDayOffData().get());
			}
		}
		
		// 期間内の休出代休残数を取得する
		BreakDayOffRemainMngParam param = new BreakDayOffRemainMngParam(companyId, empId, period.getPeriod(), true,
				period.getPeriod().end(), true, interimMng, breakMng, dayOffMng, Optional.empty(), Optional.empty(), Optional.empty());
		return this.breakDayoffMng.getBreakDayOffMngInPeriod(param);
	}
}

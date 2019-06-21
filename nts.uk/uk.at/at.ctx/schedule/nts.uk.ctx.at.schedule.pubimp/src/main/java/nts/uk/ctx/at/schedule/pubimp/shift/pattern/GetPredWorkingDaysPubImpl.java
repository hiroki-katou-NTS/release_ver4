package nts.uk.ctx.at.schedule.pubimp.shift.pattern;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.schedule.dom.shift.pattern.export.GetPredWorkingDays;
import nts.uk.ctx.at.schedule.pub.shift.pattern.GetPredWorkindDaysPub;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 実装：所定労働日数を取得する
 * @author shuichi_ishida
 */
@Stateless
public class GetPredWorkingDaysPubImpl implements GetPredWorkindDaysPub {

	/** 所定労働日数を取得する */
	@Inject
	private GetPredWorkingDays getPredWorkingDays;
	
	/** 指定期間の所定労働日数を取得する(大塚用) */
	@Override
	public double byPeriod(DatePeriod period) {
		return this.getPredWorkingDays.byPeriod(period).v();
	}
	
	/** 指定期間の所定労働日数を取得する(大塚用) */
	@Override
	public double byPeriod(DatePeriod period, Map<String, Object> workTypeMap) {
		Map<String, WorkType> _workTypeMap = new HashMap<>();
		for (Map.Entry<String, Object> worktype : workTypeMap.entrySet()) {
			_workTypeMap.put(worktype.getKey(), (WorkType)worktype.getValue());
		}
		return this.getPredWorkingDays.byPeriod(period, _workTypeMap).v();
	}
}

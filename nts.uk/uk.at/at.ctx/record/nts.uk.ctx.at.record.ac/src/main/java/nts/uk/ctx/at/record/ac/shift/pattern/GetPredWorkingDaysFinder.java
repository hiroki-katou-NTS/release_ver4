package nts.uk.ctx.at.record.ac.shift.pattern;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.adapter.shift.pattern.GetPredWorkingDaysAdaptor;
import nts.uk.ctx.at.schedule.pub.shift.pattern.GetPredWorkindDaysPub;
import nts.uk.ctx.at.shared.dom.common.days.AttendanceDaysMonth;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 実装：所定労働日数を取得する
 * @author shuichi_ishida
 */
@Stateless
public class GetPredWorkingDaysFinder implements GetPredWorkingDaysAdaptor {

	/** 所定労働日数を取得する */
	@Inject
	private GetPredWorkindDaysPub getPredWorkindDaysPub;
	
	/** 指定期間の所定労働日数を取得する(大塚用) */
	@Override
	public AttendanceDaysMonth byPeriod(DatePeriod period) {
		return new AttendanceDaysMonth(this.getPredWorkindDaysPub.byPeriod(period));
	}
	
	/** 指定期間の所定労働日数を取得する(大塚用) */
	@Override
	public AttendanceDaysMonth byPeriod(DatePeriod period, Map<String, WorkType> workTypeMap) {
		Map<String, Object> _workTypeMap = new HashMap<>();
		for (Map.Entry<String, WorkType> worktype : workTypeMap.entrySet()) {
			_workTypeMap.put(worktype.getKey(), worktype.getValue());
		}
		return new AttendanceDaysMonth(this.getPredWorkindDaysPub.byPeriod(period, _workTypeMap));
	}
}

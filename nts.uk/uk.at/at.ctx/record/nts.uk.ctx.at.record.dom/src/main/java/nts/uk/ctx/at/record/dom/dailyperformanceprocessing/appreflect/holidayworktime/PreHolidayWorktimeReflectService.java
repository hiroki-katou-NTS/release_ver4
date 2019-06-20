package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.holidayworktime;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;

/**
 * 勤務実績に反映
 * 事前申請の処理(休日出勤申請)
 * @author do_dt
 *
 */
public interface PreHolidayWorktimeReflectService {
	/**
	 * 事前申請の処理(休日出勤申請)
	 * @param holidayWorkPara
	 * @return
	 */
	public void preHolidayWorktimeReflect(HolidayWorktimePara holidayWorkPara, boolean isPre);
	/**
	 * create IntegrationOfDaily
	 * @param employeeId
	 * @param baseDate
	 * @return
	 */
	public IntegrationOfDaily createIntegrationOfDailyStart(String employeeId, GeneralDate baseDate);
}

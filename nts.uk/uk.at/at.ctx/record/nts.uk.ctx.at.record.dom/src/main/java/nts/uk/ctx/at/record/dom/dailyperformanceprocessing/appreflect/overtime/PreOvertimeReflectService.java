package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.overtime;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.ApplicationReflectOutput;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;

/**
 * 残業申請: 事前申請処理
 * @author do_dt
 *
 */
public interface PreOvertimeReflectService {
	/**
	 * 事前申請の処理
	 * @param param
	 * @return
	 */
	public ApplicationReflectOutput overtimeReflect(OvertimeParameter param);
	/**
	 * 日別実績の修正からの計算
	 * @param dailyData
	 * @param employeeId
	 * @param dataData
	 * @return
	 */
	public IntegrationOfDaily calculateForAppReflect(String employeeId, GeneralDate dataData);
}

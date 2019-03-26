package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;

/**
 * 反映状況によるチェック
 * @author do_dt
 *
 */
public interface CommonProcessCheckService {
	/**
	 * 反映状況によるチェック
	 * @param para
	 * @return false: 反映しない, true: 反映する
	 */
	public boolean commonProcessCheck(CommonCheckParameter para);	

	
	/**
	 * 予定勤種の反映
	 * @param absencePara
	 */
	public WorkInfoOfDailyPerformance reflectScheWorkTimeWorkType(CommonReflectParameter commonPara, boolean isPre,
			WorkInfoOfDailyPerformance dailyInfo);
	/**
	 * 予定勤種を反映できるかチェックする
	 * @param absencePara
	 * @return
	 */
	public boolean checkReflectScheWorkTimeType(CommonReflectParameter commonPara, boolean isPre, String workTimeCode);
	/**
	 * 
	 * @param integrationOfDaily
	 * @param isOT: True 残業申請, False：　残業申請じゃない
	 */
	public void calculateOfAppReflect(IntegrationOfDaily integrationOfDaily, String sid, GeneralDate ymd, boolean isOT);
	/**
	 * 就業時間帯の休憩時間帯を日別実績に反映する
	 * @param sid
	 * @param ymd
	 * @param optTimeLeaving
	 */
	public IntegrationOfDaily updateBreakTimeInfor(String sid, GeneralDate ymd, IntegrationOfDaily integrationOfDaily, String companyId);
}

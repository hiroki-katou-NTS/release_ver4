package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.ApplicationType;

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
	public void reflectScheWorkTimeWorkType(CommonReflectParameter commonPara, boolean isPre,
			IntegrationOfDaily dailyInfo);
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
	public void calculateOfAppReflect(CommonCalculateOfAppReflectParam commonPara);
	/**
	 * 就業時間帯の休憩時間帯を日別実績に反映する
	 * @param sid
	 * @param ymd
	 * @param optTimeLeaving
	 */
	public IntegrationOfDaily updateBreakTimeInfor(String sid, GeneralDate ymd, IntegrationOfDaily integrationOfDaily, String companyId,
			ApplicationType appType);
	public void createLogError(String sid, GeneralDate ymd, String excLogId);
}

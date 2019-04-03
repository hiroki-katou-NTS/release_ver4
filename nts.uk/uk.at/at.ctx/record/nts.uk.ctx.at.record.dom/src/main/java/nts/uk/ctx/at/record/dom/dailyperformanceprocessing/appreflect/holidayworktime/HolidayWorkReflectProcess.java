package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.holidayworktime;

import java.util.Map;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.ScheAndRecordSameChangeFlg;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;

public interface HolidayWorkReflectProcess {
	/**
	 * 予定勤種・就時の反映
	 * @param employeeId
	 * @param baseDate
	 * @param workTypeCode
	 * @param workTimeCode 就業時間帯コード
	 * @param scheReflectFlg
	 * @param scheAndRecordSameChangeFlg
	 */
	public boolean updateScheWorkTimeType(String employeeId, 
			GeneralDate baseDate, 
			String workTypeCode, 
			String workTimeCode, 
			boolean scheReflectFlg,
			boolean isPre,
			ScheAndRecordSameChangeFlg scheAndRecordSameChangeFlg,
			IntegrationOfDaily dailyData);
	/**
	 * 予定勤種・就時を反映できるかチェックする
	 * @param employeeId
	 * @param baseDate
	 * @param workTimeCode 就業時間帯コード
	 * @param scheReflectFlg 予定反映区分
	 * @param scheAndRecordSameChangeFlg 予定と実績を同じに変更する区分
	 * @return
	 */
	public boolean checkScheWorkTimeReflect(String employeeId, 
			GeneralDate baseDate, 
			String workTimeCode, 
			boolean scheReflectFlg, boolean isPre,
			ScheAndRecordSameChangeFlg scheAndRecordSameChangeFlg);
	/**
	 * 
	 * @param employeeId
	 * @param baseDate
	 * @param mapWorkTimeFrame 休出時間1～10
	 */
	public void reflectWorkTimeFrame(HolidayWorktimePara holidayWorkPara, 
			IntegrationOfDaily dailyData, boolean isPre);
	/**
	 * 休憩時間の反映
	 * @param holidayWorkPara
	 * @param dailyData
	 * @param isPre：　True：　事前、False：　事後
	 */
	public void reflectBreakTimeFrame(HolidayWorktimePara holidayWorkPara, 
			IntegrationOfDaily dailyData, boolean isPre);

}

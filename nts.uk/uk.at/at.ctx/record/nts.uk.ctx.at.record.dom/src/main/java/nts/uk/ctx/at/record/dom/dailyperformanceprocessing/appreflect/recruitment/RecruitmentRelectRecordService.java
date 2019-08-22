package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.recruitment;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.CommonReflectParameter;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
/**
 * 事前申請の処理(振出申請)
 * @author do_dt
 *
 */
public interface RecruitmentRelectRecordService {
	/**
	 * 事前申請の処理(振出申請)
	 * @param param
	 * @param isPre
	 * @return
	 */
	public void recruitmentReflect(CommonReflectParameter param, boolean isPre);
	/**
	 * 予定勤種就時の反映
	 * @param param
	 */
	public void reflectScheWorkTimeType(CommonReflectParameter param, boolean isPre, IntegrationOfDaily dailyInfo);
	/**
	 * 開始終了時刻の反映
	 * @param param
	 */
	public IntegrationOfDaily reflectRecordStartEndTime(CommonReflectParameter param, IntegrationOfDaily daily);
	/**
	 * 休出時間振替時間をクリアする
	 * @param employeeId
	 * @param baseDate
	 */
	public IntegrationOfDaily clearRecruitmenFrameTime(String employeeId, GeneralDate baseDate, IntegrationOfDaily daily);
	/**
	 * 開始時刻が反映できるか
	 * @param employeeId
	 * @param baseDate
	 * @param frameNo
	 * @param isAttendence
	 * @param optTimeLeaving
	 * @return
	 */
	public boolean checkReflectRecordStartEndTime(String workTypeCode, Integer frameNo, boolean isAttendence, String employeeId, GeneralDate baseDate);
}

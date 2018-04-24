package nts.uk.ctx.at.record.pub.dailyperform.appreflect;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.goback.GobackReflectPubParameter;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.overtime.PreOvertimePubParameter;

/**
 * 反映状況によるチェック
 * @author do_dt
 *
 */
public interface AppReflectProcessRecordPub {
	/**
	 * チェック処理(Xử lý check)
	 * @param para
	 * @return
	 */
	public boolean appReflectProcess(AppCommonPara para);
	/**
	 * 事前申請の処理(Xử lý xin trước) 　直行直帰
	 * @param para
	 * @return
	 */
	public AppReflectPubOutput preGobackReflect(GobackReflectPubParameter para);
	/**
	 * 事後申請の処理 　直行直帰
	 * @param para
	 * @return
	 */
	public AppReflectPubOutput afterGobackReflect(GobackReflectPubParameter para);
	/**
	 * 	勤務実績に反映
	 * 事前申請の処理　残業申請
	 * @param param
	 * @return
	 */
	public AppReflectPubOutput preOvertimeReflect(PreOvertimePubParameter param);
	/**
	 * 勤務実績に反映: 事後残業申請処理
	 * @param param
	 * @return
	 */
	public AppReflectPubOutput afterOvertimeReflect(PreOvertimePubParameter param);
	/**
	 * (休暇申請)
	 * @param param
	 * @param isPre : true: 事前申請処理, false: 事後申請処理
	 * @return
	 */
	public AppReflectPubOutput absenceReflect(CommonReflectPubParameter param, boolean isPre);
	/**
	 * 	勤務実績に反映: 事前申請の処理(休日出勤申請)
	 * @param param
	 * @return
	 */
	public AppReflectPubOutput holidayWorkReflect(HolidayWorkReflectPubPara param);
	/**
	 * 勤務変更申請
	 * @param param
	 * @param isPre : true: 事前申請処理, false: 事後申請処理
	 * @return
	 */
	public AppReflectPubOutput workChangeReflect(CommonReflectPubParameter param, boolean isPre);
	
}

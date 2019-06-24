package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.goback;

import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.ScheAndRecordSameChangeFlg;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;

/**
 * 事後申請の処理: 予定時刻の反映
 * @author do_dt
 *
 */
public interface AfterScheTimeReflect {
	/**
	 * 予定時刻の反映
	 * @param para
	 * @param timeTypeScheReflect: 予定勤種・就時の反映
	 * @return
	 */
	public void reflectScheTime(GobackReflectParameter para, boolean timeTypeScheReflect, IntegrationOfDaily dailyInfor);
	/**
	 * 予定時刻反映できるかチェックする
	 * @param workTimeCode 申請する就業時間帯コード
	 * @param scheAndRecordSameChange 予定と実績を同じに変更する区分
	 */
	public boolean checkScheTimeCanReflect(String workTimeCode, ScheAndRecordSameChangeFlg scheAndRecordSameChange);

}

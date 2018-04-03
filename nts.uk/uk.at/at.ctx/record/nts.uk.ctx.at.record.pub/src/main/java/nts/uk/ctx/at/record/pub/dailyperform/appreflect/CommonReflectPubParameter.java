package nts.uk.ctx.at.record.pub.dailyperform.appreflect;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.arc.time.GeneralDate;
@AllArgsConstructor
@Setter
@Getter
public class CommonReflectPubParameter {
	/**
	 * 社員ID
	 */
	private String employeeId;
	/**
	 * 年月日
	 */
	private GeneralDate baseDate;
	/**
	 * 予定と実績を同じに変更する区分
	 */
	private ScheAndRecordSameChangePubFlg scheAndRecordSameChangeFlg;
	/**
	 * 予定反映区分
	 */
	private boolean scheTimeReflectAtr;
	/**
	 * 勤務種類コード
	 */
	private String workTypeCode;/**
	 * 就業時間帯コード
	 */
	private String workTimeCode;
	/**
	 * 反映状態
	 */
	private ReflectedStatePubRecord reflectState;
	/**
	 * 予定反映不可理由
	 */
	private ReasonNotReflectPubRecord reasoNotReflect;
}

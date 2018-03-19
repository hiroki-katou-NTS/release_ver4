package nts.uk.ctx.at.record.pub.dailyperform.appreflect.overtime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.ReasonNotReflectPubRecord;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.ReflectedStatePubRecord;
@AllArgsConstructor
@Getter
@Setter
public class OvertimeAppPubParameter {
	/**
	 * 反映状態
	 */
	private ReflectedStatePubRecord reflectedState;
	/**
	 * 反映不可理由
	 */
	private ReasonNotReflectPubRecord reasonNotReflect;
	/**
	 * 勤務種類コード
	 */
	private String workTypeCode;
			
	/**就業時間帯コード	 */
	private String workTimeCode;
	/**
	 * 開始時刻１
	 */
	private Integer startTime1;
	/**
	 * 終了時刻１
	 */
	private Integer endTime1;
	/**
	 * 開始時刻２
	 */
	private Integer startTime2;
	/**
	 * 終了時刻２
	 */
	private Integer endTime2;
}

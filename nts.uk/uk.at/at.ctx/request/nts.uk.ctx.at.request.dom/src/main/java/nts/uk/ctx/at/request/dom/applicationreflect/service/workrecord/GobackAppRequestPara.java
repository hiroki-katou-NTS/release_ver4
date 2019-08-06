package nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.request.dom.application.UseAtr;
@AllArgsConstructor
@Setter
@Getter
public class GobackAppRequestPara {
	/**
	 * 勤務を変更する
	 */
	private UseAtr changeAppGobackAtr;
	/**
	 * 直行直帰申請．就業時間帯
	 */
	private String workTimeCode;
	/**
	 * 直行直帰申請．就業種類
	 */
	private String workTypeCode;
	/**
	 * 直行直帰申請．勤務時間開始1
	 */
	private Integer startTime1;
	/**
	 * 直行直帰申請．勤務時間終了1
	 */
	private Integer endTime1;
	/**
	 * 直行直帰申請．勤務時間開始2
	 */
	private Integer startTime2;
	/**
	 * 直行直帰申請．勤務時間終了2
	 */
	private Integer endTime2;
}

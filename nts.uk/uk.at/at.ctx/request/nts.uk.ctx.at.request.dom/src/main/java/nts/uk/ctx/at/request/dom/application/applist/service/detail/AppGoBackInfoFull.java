package nts.uk.ctx.at.request.dom.application.applist.service.detail;

import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor
public class AppGoBackInfoFull {

	private String appID;
	/**
	 * 勤務直行1
	 */
	private int goWorkAtr1;
	/**
	 * 勤務時間開始1
	 */
	private String workTimeStart1;
	/**
	 * 勤務直帰1
	 */
	private int backHomeAtr1;
	/**
	 * 勤務時間終了1
	 */
	private String workTimeEnd1;
	/**
	 * 勤務直行2
	 */
	private int goWorkAtr2;
	/**
	 * 勤務時間開始2
	 */
	private String workTimeStart2;
	/**
	 * 勤務直帰2
	 */
	private int backHomeAtr2;
	/**
	 * 勤務時間終了2
	 */
	private String workTimeEnd2;
}

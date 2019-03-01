package nts.uk.ctx.at.request.dom.application.applist.service;

import java.util.List;

import lombok.Value;

@Value
public class TimeResultOutput {

	private boolean checkColor;
	private List<OverTimeFrame> lstFrameResult;
	/**出勤時刻  - 開始時刻1*/
	private String strTime1;
	/**退勤時刻  - 終了時刻1*/
	private String endTime1;
	/**出勤時刻2  - 開始時刻2*/
	private String strTime2;
	/**退勤時刻2  - 終了時刻2*/
	private String endTime2;
	/**就業時間外深夜 - 計算就業外深夜*/
	private Integer shiftNightTime;
	/**フレックス超過時間 - 計算フレックス*/
	private Integer flexTime;
	/**勤務種類name*/
	private String workTypeName;
	/**就業時間帯name*/
	private String workTimeName;
}

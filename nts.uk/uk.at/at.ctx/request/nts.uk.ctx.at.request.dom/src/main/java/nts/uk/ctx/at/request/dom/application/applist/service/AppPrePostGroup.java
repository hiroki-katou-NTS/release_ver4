package nts.uk.ctx.at.request.dom.application.applist.service;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.request.dom.application.applist.service.detail.AppHolidayWorkFull;
import nts.uk.ctx.at.request.dom.application.applist.service.detail.AppOverTimeInfoFull;

@Getter
@AllArgsConstructor
public class AppPrePostGroup {
	//事前
	private String preAppID;
	//事後
	private String postAppID;
	//実績
	@Setter
	private List<OverTimeFrame> time;
	
	/**出勤時刻  - 開始時刻1*/
	@Setter
	private String strTime1;
	/**退勤時刻  - 終了時刻1*/
	@Setter
	private String endTime1;
	/**出勤時刻2  - 開始時刻2*/
	@Setter
	private String strTime2;
	/**退勤時刻2  - 終了時刻2*/
	@Setter
	private String endTime2;
	
	private AppOverTimeInfoFull appPre;
	private String reasonAppPre;
	private AppHolidayWorkFull appPreHd;
	/**就業時間外深夜 - 計算就業外深夜*/
	@Setter
	private Integer shiftNightTime;
	/**フレックス超過時間 - 計算フレックス*/
	@Setter
	private Integer flexTime;
	@Setter
	private String workTypeName;
	@Setter
	private String workTimeName;
}

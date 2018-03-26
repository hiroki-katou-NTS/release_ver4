package nts.uk.ctx.at.request.dom.application.applicationlist.service;

import java.util.List;

import lombok.Value;
@Value
public class AppHolidayWorkFull {

	private String appId;
	/**勤務種類 name*/
	private String workTypeName;
	/**就業時間帯 name*/
	private String workTimeName;
	//勤務開始時刻1
	private String startTime1;
	//勤務終了時刻1
	private String endTime1;
	//勤務開始時刻2
	private String startTime2;
	//勤務終了時刻2
	private String endTime2;
	
	private List<OverTimeFrame> lstFrame;
}

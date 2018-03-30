package nts.uk.ctx.at.request.dom.application.applist.service.detail;

import lombok.Value;
/**
 * 
 * @author hoatt
 *
 */
@Value
public class AppWorkChangeFull {
	
	private String appId;
	/**勤務種類名*/
	private String workTypeName;
    /**就業時間帯名*/
    private String workTimeName;
	/**勤務直行1*/
    private Integer goWorkAtr1;
    /**勤務時間開始1*/
	private String workTimeStart1;
	/**勤務直帰1*/
	private Integer backHomeAtr1;
	/**勤務時間終了1*/
	private String workTimeEnd1;
	/**勤務直行2*/
    private Integer goWorkAtr2;
    /**勤務時間開始2*/
	private String workTimeStart2;
	/**勤務直帰2*/
	private Integer backHomeAtr2;
	/**勤務時間終了2*/
	private String workTimeEnd2;
	/**休憩時間開始1*/
	private String breakTimeStart1;
	/**休憩時間終了1*/
	private String breakTimeEnd1;
	//0: する
	//1: しない
}

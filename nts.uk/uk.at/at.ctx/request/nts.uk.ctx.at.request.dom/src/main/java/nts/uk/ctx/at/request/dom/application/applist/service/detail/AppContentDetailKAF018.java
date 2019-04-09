package nts.uk.ctx.at.request.dom.application.applist.service.detail;

public interface AppContentDetailKAF018 {

	/**
	 * get content over time 
	 * @param appId
	 * @param detailSet
	 * @return
	 */
	public String getContentOverTimeBf(String companyId, String appId, int detailSet);
	/**
	 * get content absence
	 * 休暇申請 kaf006 - appType = 1
	 * @param appId
	 * @param detailSet
	 * @return
	 */
	public String getContentAbsence(String companyId, String appId, int detailSet, int day);
	/**
	 * get content work change
	 * 勤務変更申請 kaf007 - appType = 2
	 * @param appId
	 * @param detailSet
	 * @return
	 */
	public String getContentWorkChange(String companyId, String appId, int detailSet);
	/**
	 * get content go back
	 * 直行直帰申請 kaf009 - appType = 4
	 * @param appId
	 * @param detailSet
	 * @return
	 */
	public String getContentGoBack(String companyId, String appId, int detailSet);
	/**
	 * get Content HdWorkBf
	 * 休日出勤時間申請 kaf010 - appTYpe = 6
	 * @param appId
	 * @param detailSet
	 * @return
	 */
	public String getContentHdWorkBf(String companyId, String appId, int detailSet);
}

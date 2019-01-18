package nts.uk.ctx.at.schedule.pub.appreflectprocess;

import nts.arc.time.GeneralDate;

public interface AppReflectProcessSchePub {
	/**
	 * 	直行直帰申請: 勤務予定への反映
	 * @param reflectPara
	 * @return
	 */
	public boolean goBackDirectlyReflectSch(ApplicationReflectParamScheDto reflectPara);
	/**
	 * 休暇申請
	 * @param reflectPara
	 */
	public boolean appForLeaveSche(WorkChangeCommonReflectSchePubParam appForleaverPara);
	/**
	 * 勤務変更申請
	 * @param workChangeParam
	 */
	public boolean appWorkChangeReflect(WorkChangeCommonReflectSchePubParam workChangeParam);
	/**
	 * 休日出勤申請
	 * @param holidayWorkParam
	 * @return
	 */
	public boolean holidayWorkReflectSche(CommonReflectSchePubParam holidayWorkParam);
	/**
	 * 振休申請の反映
	 * @param absenceLeaverParam
	 * @return
	 */
	public boolean absenceLeaveReflectSche(CommonReflectSchePubParam absenceLeaverParam);
	/**
	 * 振出申請の反映
	 * @param absenceLeaverParam
	 * @return
	 */
	public boolean recruitmentReflectSche(CommonReflectSchePubParam absenceLeaverParam);

	public boolean isSche(String employeeId, GeneralDate baseDate);
}

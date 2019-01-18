package nts.uk.ctx.at.schedule.dom.appreflectprocess.service.appforleave;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.CommonReflectParamSche;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.workchange.WorkChangecommonReflectParamSche;

/**
 * (休暇申請)勤務予定への反映
 * @author do_dt
 *
 */
public interface ForleaveReflectSche {
	/**
	 * (休暇申請)勤務予定への反映
	 * @param reflectParam
	 */
	public boolean forlearveReflectSche(WorkChangecommonReflectParamSche reflectParam);
	/**
	 * 時刻の反映
	 * @param employeeId
	 * @param dateData
	 * @param workTypeCode
	 */
	public void reflectTime(String employeeId, GeneralDate dateData, String workTypeCode, Integer startTime, Integer endTime);

}

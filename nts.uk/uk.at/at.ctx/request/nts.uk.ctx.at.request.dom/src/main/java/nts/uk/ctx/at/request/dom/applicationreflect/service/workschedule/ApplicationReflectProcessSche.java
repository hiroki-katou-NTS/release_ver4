package nts.uk.ctx.at.request.dom.applicationreflect.service.workschedule;

import nts.uk.ctx.at.request.dom.applicationreflect.service.WorkChangeCommonReflectPara;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.CommonReflectPara;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.GobackReflectPara;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.HolidayWorkReflectPara;

public interface ApplicationReflectProcessSche {
	/**
	 * 直行直帰申請
	 * @param reflectSche
	 * @return
	 */
	public void goBackDirectlyReflect(GobackReflectPara gobackInfor);
	/**
	 * 休暇申請
	 * @param reflectSche
	 */
	public void forleaveReflect(WorkChangeCommonReflectPara absenceInfor);
	/**
	 * 勤務変更申請
	 * @param reflectSche
	 * @return
	 */
	public void workChangeReflect(WorkChangeCommonReflectPara workchangeInfor);
	/**
	 * 休日出勤申請
	 * @param relectSche
	 * @return
	 */
	public void holidayWorkReflect(HolidayWorkReflectPara holidayworkInfor);
	/**
	 * 振休申請の反映
	 * @param relectSche
	 * @return
	 */
	public void ebsenceLeaveReflect(CommonReflectPara absenceLeaveAppInfor);
	/**
	 * 振出申請の反映
	 * @param relectSche
	 * @return
	 */
	public void recruitmentReflect(CommonReflectPara recruitmentInfor);	
}

package nts.uk.ctx.at.request.dom.applicationreflect.service.workschedule;

import javax.ejb.Stateless;
import javax.inject.Inject;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.AppReflectRecordPara;
@Stateless
public class WorkScheduleReflectServiceImpl implements WorkScheduleReflectService{
	@Inject
	private ApplicationReflectProcessSche processScheReflect;
	@Override
	public void workscheReflect(AppReflectRecordPara appRecordInfor) {
		switch (appRecordInfor.getAppType()) {
		case GO_RETURN_DIRECTLY_APPLICATION:
			processScheReflect.goBackDirectlyReflect(appRecordInfor.getGobackInfor());
			break;
		case WORK_CHANGE_APPLICATION:
			processScheReflect.workChangeReflect(appRecordInfor.getWorkchangeInfor());
			break;
		case ABSENCE_APPLICATION:
			processScheReflect.forleaveReflect(appRecordInfor.getAbsenceInfor());
			break;
		case BREAK_TIME_APPLICATION:
			processScheReflect.holidayWorkReflect(appRecordInfor.getHolidayworkInfor());
			break;
		case COMPLEMENT_LEAVE_APPLICATION:
			if(appRecordInfor.getAbsenceLeaveAppInfor() != null) {
				processScheReflect.ebsenceLeaveReflect(appRecordInfor.getAbsenceLeaveAppInfor());
			} 
			if(appRecordInfor.getRecruitmentInfor() != null) {
				processScheReflect.recruitmentReflect(appRecordInfor.getRecruitmentInfor());
			}
			break;
		default:
			break;
		}
	}
}

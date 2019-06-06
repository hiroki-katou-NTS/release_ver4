package nts.uk.ctx.at.request.dom.applicationreflect.service.workschedule;

import javax.ejb.Stateless;
import javax.inject.Inject;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.AppReflectProcessRecord;
@Stateless
public class WorkScheduleReflectServiceImpl implements WorkScheduleReflectService{
	@Inject
	private ApplicationReflectProcessSche processScheReflect;
	@Inject
	private AppReflectProcessRecord checkReflect;
	@Override
	public void workscheReflect(ReflectScheDto reflectParam) {
		Application_New application = reflectParam.getAppInfor();
		//反映チェック処理(Xử lý check phản ánh)		
		if(!checkReflect.appReflectProcessRecord(application, false, reflectParam.getExecutionType())) {
			return;
		}
		switch(application.getAppType()) {
		case GO_RETURN_DIRECTLY_APPLICATION:
			processScheReflect.goBackDirectlyReflect(reflectParam);
			break;
		case WORK_CHANGE_APPLICATION:
			processScheReflect.workChangeReflect(reflectParam);
			break;
		case ABSENCE_APPLICATION:
			processScheReflect.forleaveReflect(reflectParam);
			break;
		case BREAK_TIME_APPLICATION:
			processScheReflect.holidayWorkReflect(reflectParam);
			break;
		case COMPLEMENT_LEAVE_APPLICATION:
			if(reflectParam.getAbsenceLeave() != null) {
				processScheReflect.ebsenceLeaveReflect(reflectParam);
			} 
			if(reflectParam.getRecruitment() != null) {
				processScheReflect.recruitmentReflect(reflectParam);
			}
			break;
		default:
			break;
		}
		
	}

}

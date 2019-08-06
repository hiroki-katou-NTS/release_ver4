package nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord;

/*import nts.arc.time.GeneralDate;*/
import javax.ejb.Stateless;
import javax.inject.Inject;
import nts.uk.ctx.at.request.dom.application.PrePostAtr;
import nts.uk.ctx.at.request.dom.applicationreflect.service.WorkChangeCommonReflectPara;

@Stateless
public class WorkRecordReflectServiceImpl implements WorkRecordReflectService{
	@Inject
	private AppReflectProcessRecord reflectRecord;
	@Override
	public void workRecordreflect(AppReflectRecordPara appRecordInfor) {
		boolean isPre = appRecordInfor.getPrePosAtr() == PrePostAtr.PREDICT ? true : false;
		//申請種類
		switch (appRecordInfor.getAppType()) {
		case OVER_TIME_APPLICATION:
			reflectRecord.overtimeReflectRecord(appRecordInfor.getOvertimeInfor(), true); 
			break;
		case GO_RETURN_DIRECTLY_APPLICATION:
			GobackReflectPara gobackpara = appRecordInfor.getGobackInfor();
			reflectRecord.gobackReflectRecord(gobackpara, isPre);
			break;
		case ABSENCE_APPLICATION:
			WorkChangeCommonReflectPara absenceInfor = appRecordInfor.getAbsenceInfor();
			reflectRecord.absenceReflectRecor(absenceInfor, isPre);
			break;
		case BREAK_TIME_APPLICATION:
			HolidayWorkReflectPara holidayworkData = appRecordInfor.getHolidayworkInfor();
			reflectRecord.holidayWorkReflectRecord(holidayworkData, isPre);
			break;
		case WORK_CHANGE_APPLICATION:
			WorkChangeCommonReflectPara workChangeData = appRecordInfor.getWorkchangeInfor();
			reflectRecord.workChangeReflectRecord(workChangeData, isPre);
			break;
		case COMPLEMENT_LEAVE_APPLICATION:
			CommonReflectPara absenceLeaveData = appRecordInfor.getAbsenceLeaveAppInfor();
			CommonReflectPara recruitmentData = appRecordInfor.getRecruitmentInfor();
			if(absenceLeaveData != null) {
				reflectRecord.absenceLeaveReflectRecord(absenceLeaveData, isPre);
			}
			if(recruitmentData != null) {
				reflectRecord.recruitmentReflectRecord(recruitmentData, isPre);
			}
			break;
		default:
			break;
		}
	}
}

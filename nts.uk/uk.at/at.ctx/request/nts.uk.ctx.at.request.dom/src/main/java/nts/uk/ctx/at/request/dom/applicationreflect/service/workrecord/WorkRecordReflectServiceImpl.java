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
	public boolean workRecordreflect(AppReflectRecordPara appRecordInfor) {
		ReflectRecordInfor recordInfor = appRecordInfor.getRecordInfor();
		//事前チェック処理
		boolean checkReflect = reflectRecord.appReflectProcessRecord(appRecordInfor.getRecordInfor().getAppInfor(), true,
				appRecordInfor.getExecuTionType());
		if (!checkReflect) {
			return false;
		}
		boolean isPre = recordInfor.getAppInfor().getPrePostAtr() == PrePostAtr.PREDICT ? true : false;
		//申請種類
		switch (recordInfor.getAppInfor().getAppType()) {
		case OVER_TIME_APPLICATION:
			reflectRecord.overtimeReflectRecord(appRecordInfor.getOvertimeInfor(), true); 
			return true;
		case GO_RETURN_DIRECTLY_APPLICATION:
			GobackReflectPara gobackpara = appRecordInfor.getGobackInfor();
			reflectRecord.gobackReflectRecord(gobackpara, isPre);
			return true;
		case ABSENCE_APPLICATION:
			WorkChangeCommonReflectPara absenceInfor = appRecordInfor.getAbsenceInfor();
			reflectRecord.absenceReflectRecor(absenceInfor, isPre);
			return true;
		case BREAK_TIME_APPLICATION:
			HolidayWorkReflectPara holidayworkData = appRecordInfor.getHolidayworkInfor();
			reflectRecord.holidayWorkReflectRecord(holidayworkData, isPre);
			return true;
		case WORK_CHANGE_APPLICATION:
			WorkChangeCommonReflectPara workChangeData = appRecordInfor.getWorkchangeInfor();
			reflectRecord.workChangeReflectRecord(workChangeData, isPre);
			return true;
		case COMPLEMENT_LEAVE_APPLICATION:
			CommonReflectPara absenceLeaveData = appRecordInfor.getAbsenceLeaveAppInfor();
			CommonReflectPara recruitmentData = appRecordInfor.getRecruitmentInfor();
			if(absenceLeaveData != null) {
				reflectRecord.absenceLeaveReflectRecord(absenceLeaveData, isPre);
			}
			if(recruitmentData != null) {
				reflectRecord.recruitmentReflectRecord(recruitmentData, isPre);
			}
			return true;
		default:
			return false;
		}
	}
}

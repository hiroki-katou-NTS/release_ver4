package nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord;

/*import nts.arc.time.GeneralDate;*/
import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.PrePostAtr;
import nts.uk.ctx.at.request.dom.applicationreflect.service.ReflectInformationResult;
import nts.uk.ctx.at.request.dom.applicationreflect.service.WorkChangeCommonReflectPara;

@Stateless
public class WorkRecordReflectServiceImpl implements WorkRecordReflectService{
	@Inject
	private AppReflectProcessRecord reflectRecord;

	@Override
	public ReflectInformationResult workRecordreflect(AppReflectRecordPara appRecordInfor) {
		ReflectRecordInfor recordInfor = appRecordInfor.getRecordInfor();
		//事前チェック処理
		boolean checkReflect = reflectRecord.appReflectProcessRecord(appRecordInfor.getRecordInfor().getAppInfor(), true,
				appRecordInfor.getExecuTionType());
		if (!checkReflect) {
			return ReflectInformationResult.CHECKFALSE;
		}
		boolean isPre = recordInfor.getAppInfor().getPrePostAtr() == PrePostAtr.PREDICT ? true : false;
			//申請種類
		if(recordInfor.getAppInfor().getAppType() == ApplicationType.OVER_TIME_APPLICATION
				&& recordInfor.getAppInfor().getPrePostAtr() == PrePostAtr.PREDICT) {
			return reflectRecord.overtimeReflectRecord(appRecordInfor.getOvertimeInfor(), true) 						
					? ReflectInformationResult.DONE : ReflectInformationResult.NOTDONE;
		} 
		if (recordInfor.getAppInfor().getAppType() == ApplicationType.GO_RETURN_DIRECTLY_APPLICATION) {
			GobackReflectPara gobackpara = appRecordInfor.getGobackInfor();
			return reflectRecord.gobackReflectRecord(gobackpara, isPre) 
					? ReflectInformationResult.DONE : ReflectInformationResult.NOTDONE;
		} 
		if (recordInfor.getAppInfor().getAppType() == ApplicationType.ABSENCE_APPLICATION) {
			WorkChangeCommonReflectPara absenceInfor = appRecordInfor.getAbsenceInfor();
			return reflectRecord.absenceReflectRecor(absenceInfor, isPre)
					? ReflectInformationResult.DONE : ReflectInformationResult.NOTDONE;
		} 
		if (recordInfor.getAppInfor().getAppType() == ApplicationType.BREAK_TIME_APPLICATION) {
			HolidayWorkReflectPara holidayworkData = appRecordInfor.getHolidayworkInfor();
			return reflectRecord.holidayWorkReflectRecord(holidayworkData, isPre)
					? ReflectInformationResult.DONE : ReflectInformationResult.NOTDONE;
		} 
		if (recordInfor.getAppInfor().getAppType() == ApplicationType.WORK_CHANGE_APPLICATION) {
			WorkChangeCommonReflectPara workChangeData = appRecordInfor.getWorkchangeInfor();
			return reflectRecord.workChangeReflectRecord(workChangeData, isPre)
					? ReflectInformationResult.DONE : ReflectInformationResult.NOTDONE;
		} 
		if (recordInfor.getAppInfor().getAppType() == ApplicationType.COMPLEMENT_LEAVE_APPLICATION) {
			CommonReflectPara absenceLeaveData = appRecordInfor.getAbsenceLeaveAppInfor();
			CommonReflectPara recruitmentData = appRecordInfor.getRecruitmentInfor();
			boolean kaf011 = true;
			if(absenceLeaveData != null) {
				kaf011 = reflectRecord.absenceLeaveReflectRecord(absenceLeaveData, isPre);
			}
			if(recruitmentData != null) {
				kaf011 = reflectRecord.recruitmentReflectRecord(recruitmentData, isPre);
			}
			return kaf011 ?  ReflectInformationResult.DONE : ReflectInformationResult.NOTDONE;
		}
		
		
		return ReflectInformationResult.CHECKFALSE;
	}

}

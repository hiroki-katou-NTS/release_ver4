package nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.PrePostAtr;
import nts.uk.ctx.at.request.dom.application.ReasonNotReflectDaily_New;

@Stateless
public class WorkRecordReflectServiceImpl implements WorkRecordReflectService{
	@Inject
	private AppReflectProcessRecord reflectRecord;

	@Override
	public WorkReflectedStatesInfo workRecordreflect(AppReflectRecordPara appRecordInfor) {
		ReflectRecordInfor recordInfor = appRecordInfor.getRecordInfor();
		WorkReflectedStatesInfo statesInfor = new WorkReflectedStatesInfo(recordInfor.getAppInfor().getReflectionInformation().getStateReflection(),
				recordInfor.getAppInfor().getReflectionInformation().getNotReason().isPresent() ? recordInfor.getAppInfor().getReflectionInformation().getNotReasonReal().get() : ReasonNotReflectDaily_New.NOT_PROBLEM);
		AppReflectInfor reflectInfor = new AppReflectInfor(recordInfor.getDegressAtr(),
				recordInfor.getExecutiontype(),
				recordInfor.getAppInfor().getReflectionInformation().getStateReflection(),
				recordInfor.getAppInfor().getReflectionInformation().getStateReflectionReal());
		/*boolean checkReflect = reflectRecord.appReflectProcessRecord(reflectInfor);
		if (!checkReflect) {
			return statesInfor;
		}*/
		//事前事後区分を取得
		if(recordInfor.getAppInfor().getPrePostAtr() == PrePostAtr.PREDICT) {
			//申請種類
			if(recordInfor.getAppInfor().getAppType() == ApplicationType.OVER_TIME_APPLICATION) {
				return reflectRecord.overtimeReflectRecord(appRecordInfor.getOvertimeInfor(), true);
			} else if (recordInfor.getAppInfor().getAppType() == ApplicationType.GO_RETURN_DIRECTLY_APPLICATION) {
				GobackReflectPara gobackpara = appRecordInfor.getGobackInfor();
				return reflectRecord.gobackReflectRecord(gobackpara, true);
			}
		} else {
			if(recordInfor.getAppInfor().getAppType() == ApplicationType.OVER_TIME_APPLICATION) {
				//return reflectRecord.overtimeReflectRecord(appRecordInfor.getOvertimeInfor(), false);
			}
		}
		
		return null;
	}

}

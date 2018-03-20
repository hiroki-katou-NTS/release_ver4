package nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord;

import javax.ejb.Stateless;

import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.PrePostAtr;
import nts.uk.ctx.at.request.dom.application.ReasonNotReflect_New;
import nts.uk.ctx.at.request.dom.applicationreflect.service.ReflectedStatesInfo;

@Stateless
public class WorkRecordReflectServiceImpl implements WorkRecordReflectService{
	
	private AppReflectProcessRecord reflectRecord;

	@Override
	public ReflectedStatesInfo workRecordreflect(AppReflectRecordPara appRecordInfor) {
		ReflectRecordInfor recordInfor = appRecordInfor.getRecordInfor();
		ReflectedStatesInfo statesInfor = new ReflectedStatesInfo(recordInfor.getAppInfor().getReflectionInformation().getStateReflection(),
				recordInfor.getAppInfor().getReflectionInformation().getNotReason().isPresent() ? recordInfor.getAppInfor().getReflectionInformation().getNotReason().get() : ReasonNotReflect_New.NOT_PROBLEM);
		AppReflectInfor reflectInfor = new AppReflectInfor(recordInfor.getDegressAtr(),
				recordInfor.getExecutiontype(),
				recordInfor.getAppInfor().getReflectionInformation().getStateReflection(),
				recordInfor.getAppInfor().getReflectionInformation().getStateReflectionReal());
		boolean checkReflect = reflectRecord.appReflectProcessRecord(reflectInfor);
		if (!checkReflect) {
			return statesInfor;
		}
		//事前事後区分を取得
		if(recordInfor.getAppInfor().getPrePostAtr() == PrePostAtr.PREDICT) {
			//申請種類
			if(recordInfor.getAppInfor().getAppType() == ApplicationType.OVER_TIME_APPLICATION) {
				
			} else if (recordInfor.getAppInfor().getAppType() == ApplicationType.GO_RETURN_DIRECTLY_APPLICATION) {
				GobackReflectPara gobackpara = appRecordInfor.getGobackInfor();
				return reflectRecord.gobackReflectRecord(gobackpara, true);
			}
		} else {
			
		}
		
		return null;
	}

}

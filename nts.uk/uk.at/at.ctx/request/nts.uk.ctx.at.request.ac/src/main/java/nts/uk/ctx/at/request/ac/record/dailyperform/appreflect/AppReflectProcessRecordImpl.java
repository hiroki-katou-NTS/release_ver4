package nts.uk.ctx.at.request.ac.record.dailyperform.appreflect;

import javax.ejb.Stateless;
import javax.inject.Inject;
import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.AppCommonPara;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.AppReflectProcessRecordPub;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.AppReflectPubOutput;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.DegreeReflectionPubAtr;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.ExecutionPubType;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.ReasonNotReflectPubRecord;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.ReflectedStatePubRecord;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.ScheAndRecordSameChangePubFlg;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.goback.ChangeAppGobackPubAtr;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.goback.GobackAppPubParameter;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.goback.GobackReflectPubParameter;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.goback.PriorStampPubAtr;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.goback.ScheTimeReflectPubAtr;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.overtime.OvertimeAppPubParameter;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.overtime.PreOvertimePubParameter;
import nts.uk.ctx.at.request.dom.application.ReasonNotReflectDaily_New;
import nts.uk.ctx.at.request.dom.application.ReflectedState_New;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.AppReflectInfor;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.AppReflectProcessRecord;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.GobackReflectPara;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.OvertimeReflectPara;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.WorkReflectedStatesInfo;

@Stateless
public class AppReflectProcessRecordImpl implements AppReflectProcessRecord {
	@Inject
	private AppReflectProcessRecordPub recordPub;
	
	@Override
	public boolean appReflectProcessRecord(AppReflectInfor info) {
		AppCommonPara para = new AppCommonPara(EnumAdaptor.valueOf(info.getDegressAtr().value, DegreeReflectionPubAtr.class),
				EnumAdaptor.valueOf(info.getExecutiontype().value, ExecutionPubType.class),
				EnumAdaptor.valueOf(info.getStateReflection().value, ReflectedStatePubRecord.class),
				EnumAdaptor.valueOf(info.getStateReflectionReal().value, ReflectedStatePubRecord.class)); 
		return recordPub.appReflectProcess(para);
	}

	@Override
	public WorkReflectedStatesInfo gobackReflectRecord(GobackReflectPara para, boolean isPre) {
		GobackAppPubParameter gobackPra = new GobackAppPubParameter(EnumAdaptor.valueOf(para.getGobackData().getChangeAppGobackAtr().value,
				ChangeAppGobackPubAtr.class), para.getGobackData().getWorkTimeCode(),
				para.getGobackData().getWorkTypeCode(), 
				para.getGobackData().getStartTime1(),
				para.getGobackData().getEndTime1(), 
				para.getGobackData().getStartTime1(),
				para.getGobackData().getEndTime2(),
				EnumAdaptor.valueOf(para.getGobackData().getReflectState().value, ReflectedStatePubRecord.class), 
				EnumAdaptor.valueOf(para.getGobackData().getReasoNotReflect().value, ReasonNotReflectPubRecord.class));
		GobackReflectPubParameter pubPara = new GobackReflectPubParameter(para.getEmployeeId(), 
				para.getDateData(),
				para.isOutResReflectAtr(),
				EnumAdaptor.valueOf(para.getPriorStampAtr().value, PriorStampPubAtr.class),
				EnumAdaptor.valueOf(para.getScheAndRecordSameChangeFlg().value, ScheAndRecordSameChangePubFlg.class),
				EnumAdaptor.valueOf(para.getScheTimeReflectAtr().value, ScheTimeReflectPubAtr.class),
				para.isScheReflectAtr(),
				gobackPra);
		AppReflectPubOutput gobackReflect;
		if(isPre) {
			gobackReflect = recordPub.preGobackReflect(pubPara);
		} else {
			gobackReflect = recordPub.afterGobackReflect(pubPara);
		}
		
		
		WorkReflectedStatesInfo preGobackData = new WorkReflectedStatesInfo(EnumAdaptor.valueOf(gobackReflect.getReflectedState().value, ReflectedState_New.class), 
				EnumAdaptor.valueOf(gobackReflect.getReasonNotReflect().value, ReasonNotReflectDaily_New.class));
		return preGobackData;
	}

	@Override
	public WorkReflectedStatesInfo overtimeReflectRecord(OvertimeReflectPara para, boolean isPre) {
		OvertimeAppPubParameter overtimePara = new OvertimeAppPubParameter(EnumAdaptor.valueOf(para.getOvertimePara().getReflectedState().value, ReflectedStatePubRecord.class),
				EnumAdaptor.valueOf(para.getOvertimePara().getReasonNotReflect() == null ? 0 : para.getOvertimePara().getReasonNotReflect().value, ReasonNotReflectPubRecord.class),
				para.getOvertimePara().getWorkTypeCode(),
				para.getOvertimePara().getWorkTimeCode(),
				para.getOvertimePara().getStartTime1(),
				para.getOvertimePara().getEndTime1(),
				para.getOvertimePara().getStartTime2(),
				para.getOvertimePara().getEndTime2(),
				para.getOvertimePara().getMapOvertimeFrame(),
				para.getOvertimePara().getOverTimeShiftNight(),
				para.getOvertimePara().getFlexExessTime());
		PreOvertimePubParameter preOvertimePara = new PreOvertimePubParameter(para.getEmployeeId(), 
				para.getDateInfo(), 
				para.isActualReflectFlg(), 
				para.isScheReflectFlg(), 
				para.isTimeReflectFlg(), 
				para.isAutoClearStampFlg(), 
				EnumAdaptor.valueOf(para.getScheAndRecordSameChangeFlg().value, ScheAndRecordSameChangePubFlg.class), 
				para.isScheTimeOutFlg(), 
				overtimePara);
		AppReflectPubOutput appReflect;
		if(isPre) {
			appReflect = recordPub.preOvertimeReflect(preOvertimePara);	
		} else {
			appReflect = recordPub.afterOvertimeReflect(preOvertimePara);
		}
		
		WorkReflectedStatesInfo overtimeReflect = new WorkReflectedStatesInfo(EnumAdaptor.valueOf(appReflect.getReflectedState().value, ReflectedState_New.class), 
				EnumAdaptor.valueOf(appReflect.getReasonNotReflect().value, ReasonNotReflectDaily_New.class));
		return overtimeReflect;
	}
	
	

}

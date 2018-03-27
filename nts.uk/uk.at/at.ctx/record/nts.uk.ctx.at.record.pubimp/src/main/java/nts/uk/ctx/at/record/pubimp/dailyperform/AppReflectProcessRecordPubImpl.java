package nts.uk.ctx.at.record.pubimp.dailyperform;

import javax.ejb.Stateless;
import javax.inject.Inject;
import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.ApplicationReflectOutput;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.CommonCheckParameter;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.CommonProcessCheckService;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.DegreeReflectionAtr;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.ExecutionType;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.ReasonNotReflectRecord;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.ReflectedStateRecord;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.ScheAndRecordSameChangeFlg;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.goback.ChangeAppGobackAtr;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.goback.GobackAppParameter;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.goback.GobackReflectParameter;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.goback.PreGoBackReflectService;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.goback.PriorStampAtr;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.goback.ScheTimeReflectAtr;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.overtime.AfterOvertimeReflectService;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.overtime.OvertimeAppParameter;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.overtime.OvertimeParameter;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.overtime.PreOvertimeReflectService;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.AppCommonPara;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.AppReflectProcessRecordPub;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.AppReflectPubOutput;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.ReasonNotReflectPubRecord;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.ReflectedStatePubRecord;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.goback.GobackReflectPubParameter;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.overtime.PreOvertimePubParameter;

@Stateless
public class AppReflectProcessRecordPubImpl implements AppReflectProcessRecordPub{
	@Inject
	private CommonProcessCheckService commonProcess;
	@Inject
	private PreGoBackReflectService preGobackReflect;
	@Inject
	private PreOvertimeReflectService preOvertimeReflect;
	@Inject
	private AfterOvertimeReflectService afterOvertimeReflect;
	@Override
	public boolean appReflectProcess(AppCommonPara para) {
		CommonCheckParameter paraTemp = new CommonCheckParameter(EnumAdaptor.valueOf(para.getDegressAtr().value, DegreeReflectionAtr.class),
				EnumAdaptor.valueOf(para.getExecutiontype().value, ExecutionType.class),
				EnumAdaptor.valueOf(para.getStateReflectionReal().value, ReflectedStateRecord.class),
				EnumAdaptor.valueOf(para.getStateReflection().value, ReflectedStateRecord.class));
		return commonProcess.commonProcessCheck(paraTemp);
	}

	@Override
	public AppReflectPubOutput preGobackReflect(GobackReflectPubParameter para) {
		ApplicationReflectOutput reflectInfor = preGobackReflect.gobackReflect(this.toDomainGobackReflect(para));
		AppReflectPubOutput reflectOutput = new AppReflectPubOutput(EnumAdaptor.valueOf(reflectInfor.getReflectedState().value, ReflectedStatePubRecord.class), 
				EnumAdaptor.valueOf(reflectInfor.getReasonNotReflect().value, ReasonNotReflectPubRecord.class));
		return reflectOutput;
	}

	@Override
	public AppReflectPubOutput afterGobackReflect(GobackReflectPubParameter para) {		
		ApplicationReflectOutput reflectInfor = preGobackReflect.afterGobackReflect(this.toDomainGobackReflect(para));
		AppReflectPubOutput reflectOutput = new AppReflectPubOutput(EnumAdaptor.valueOf(reflectInfor.getReflectedState().value, ReflectedStatePubRecord.class), 
				EnumAdaptor.valueOf(reflectInfor.getReasonNotReflect().value, ReasonNotReflectPubRecord.class));
		return reflectOutput;
	}
	private GobackReflectParameter toDomainGobackReflect(GobackReflectPubParameter para) {
		GobackAppParameter appPara = new GobackAppParameter(EnumAdaptor.valueOf(para.getGobackData().getChangeAppGobackAtr().value, ChangeAppGobackAtr.class),
				para.getGobackData().getWorkTimeCode(),
				para.getGobackData().getWorkTypeCode(),
				para.getGobackData().getStartTime1(),
				para.getGobackData().getEndTime1(),
				para.getGobackData().getStartTime2(),
				para.getGobackData().getEndTime2(),
				EnumAdaptor.valueOf(para.getGobackData().getReflectState().value, ReflectedStateRecord.class),
				EnumAdaptor.valueOf(para.getGobackData().getReasoNotReflect().value, ReasonNotReflectRecord.class));
		GobackReflectParameter gobackPara = new GobackReflectParameter(para.getEmployeeId(),
				para.getDateData(),
				para.isOutResReflectAtr(),
				EnumAdaptor.valueOf(para.getPriorStampAtr().value, PriorStampAtr.class),
				EnumAdaptor.valueOf(para.getScheAndRecordSameChangeFlg().value, ScheAndRecordSameChangeFlg.class),
				EnumAdaptor.valueOf(para.getScheTimeReflectAtr().value, ScheTimeReflectAtr.class),
				para.isScheReflectAtr(),
				appPara);
		return gobackPara;
		
	}

	@Override
	public AppReflectPubOutput preOvertimeReflect(PreOvertimePubParameter param) {
		ApplicationReflectOutput reflect = preOvertimeReflect.overtimeReflect(this.toDomainOvertimeReflect(param));
		AppReflectPubOutput reflectOutput = new AppReflectPubOutput(EnumAdaptor.valueOf(reflect.getReflectedState().value, ReflectedStatePubRecord.class),
				EnumAdaptor.valueOf(reflect.getReasonNotReflect().value, ReasonNotReflectPubRecord.class));
		return reflectOutput;
	}

	@Override
	public AppReflectPubOutput afterOvertimeReflect(PreOvertimePubParameter param) {
		ApplicationReflectOutput reflect = afterOvertimeReflect.reflectAfterOvertime(this.toDomainOvertimeReflect(param));
		AppReflectPubOutput reflectOutput = new AppReflectPubOutput(EnumAdaptor.valueOf(reflect.getReflectedState().value, ReflectedStatePubRecord.class),
				EnumAdaptor.valueOf(reflect.getReasonNotReflect().value, ReasonNotReflectPubRecord.class));
		return reflectOutput;
	}

	private OvertimeParameter toDomainOvertimeReflect(PreOvertimePubParameter param) {
		OvertimeAppParameter appOver = new OvertimeAppParameter(EnumAdaptor.valueOf(param.getOvertimePara().getReflectedState().value, ReflectedStateRecord.class),
				EnumAdaptor.valueOf(param.getOvertimePara().getReasonNotReflect().value, ReasonNotReflectRecord.class),
				param.getOvertimePara().getWorkTypeCode(), 
				param.getOvertimePara().getWorkTimeCode(),
				param.getOvertimePara().getStartTime1(), 
				param.getOvertimePara().getEndTime1(),
				param.getOvertimePara().getStartTime2(),
				param.getOvertimePara().getEndTime2(),
				param.getOvertimePara().getMapOvertimeFrame(),
				param.getOvertimePara().getOverTimeShiftNight(),
				param.getOvertimePara().getFlexExessTime());
		OvertimeParameter overtimePara = new OvertimeParameter(param.getEmployeeId(), 
				param.getDateInfo(), 
				param.isActualReflectFlg(), 
				param.isScheReflectFlg(), 
				param.isTimeReflectFlg(), 
				param.isAutoClearStampFlg(), 
				EnumAdaptor.valueOf(param.getScheAndRecordSameChangeFlg().value, ScheAndRecordSameChangeFlg.class),
				param.isScheTimeOutFlg(),
				appOver);
		return overtimePara;
	}

}

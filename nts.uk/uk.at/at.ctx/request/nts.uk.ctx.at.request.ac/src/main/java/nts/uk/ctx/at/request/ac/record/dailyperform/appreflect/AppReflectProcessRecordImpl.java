package nts.uk.ctx.at.request.ac.record.dailyperform.appreflect;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.ExecutionType;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.AppCommonPara;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.AppReflectProcessRecordPub;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.ApprovalProcessingUseSettingPub;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.BreakTimePubParam;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.CommonReflectPubParameter;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.HolidayWorkReflectPubPara;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.HolidayWorktimeAppPubPara;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.IdentityProcessUseSetPub;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.PrePostRecordAtr;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.ReasonNotReflectDailyPubRecord;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.ReflectRecordAtr;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.ReflectedStatePubRecord;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.ScheAndRecordIsReflectPub;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.ScheAndRecordSameChangePubFlg;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.WorkChangeCommonReflectPubPara;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.goback.ChangeAppGobackPubAtr;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.goback.GobackAppPubParameter;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.goback.GobackReflectPubParameter;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.goback.PriorStampPubAtr;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.goback.ScheTimeReflectPubAtr;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.overtime.OverTimeRecordPubAtr;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.overtime.OvertimeAppPubParameter;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.overtime.PreOvertimePubParameter;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.CommonReflectPara;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.GobackAppRequestPara;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.DisabledSegment_New;
import nts.uk.ctx.at.request.dom.applicationreflect.service.WorkChangeCommonReflectPara;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.AppReflectProcessRecord;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.ApprovalProcessingUseSettingAc;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.GobackReflectPara;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.HolidayWorkReflectPara;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.IdentityProcessUseSetAc;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.OvertimeAppParameter;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.OvertimeReflectPara;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.ScheAndRecordIsReflect;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.dailymonthlyprocessing.ExecutionTypeExImport;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.ApplicationType;

@Stateless
public class AppReflectProcessRecordImpl implements AppReflectProcessRecord {
	@Inject
	private AppReflectProcessRecordPub recordPub;
	@Override
	public ScheAndRecordIsReflect appReflectProcessRecord(Application_New appInfor, ExecutionTypeExImport executionType, GeneralDate appDate) {
		//Optional<RequestSetting> settingData = requestSetting.findByCompany(appInfor.getCompanyID());
		/*settingData.isPresent() ?
				EnumAdaptor.valueOf(settingData.get().getAppReflectAfterConfirm().getAchievementConfirmedAtr().value, ReflectRecordAtr.class) 
				: ReflectRecordAtr.NOT_RFFLECT_CANNOT_REF*/
		AppCommonPara para = new AppCommonPara(appInfor.getCompanyID(), 
				appInfor.getEmployeeID(),
				appDate, 
				ReflectRecordAtr.REFLECT,
				appInfor.getReflectionInformation().getForcedReflectionReal() == DisabledSegment_New.TODO ? true : false,
				EnumAdaptor.valueOf(appInfor.getReflectionInformation().getStateReflectionReal().value, ReflectedStatePubRecord.class),
				EnumAdaptor.valueOf(appInfor.getReflectionInformation().getStateReflection().value,  ReflectedStatePubRecord.class),
				EnumAdaptor.valueOf(appInfor.getPrePostAtr().value, PrePostRecordAtr.class),
				EnumAdaptor.valueOf(appInfor.getAppType().value, ApplicationType.class),
				appInfor.getReflectionInformation().getForcedReflection() == DisabledSegment_New.TODO ? true : false);
		ScheAndRecordIsReflectPub checkResult = recordPub.appReflectProcess(para, EnumAdaptor.valueOf(executionType.value, ExecutionType.class));
		return new ScheAndRecordIsReflect(checkResult.isScheReflect(), checkResult.isRecordReflect());
	}

	@Override
	public void gobackReflectRecord(GobackReflectPara para, boolean isPre) {
		GobackAppRequestPara gobackData = para.getGobackData();
		GobackAppPubParameter gobackPra = new GobackAppPubParameter(EnumAdaptor.valueOf(gobackData.getChangeAppGobackAtr().value,
				ChangeAppGobackPubAtr.class), gobackData.getWorkTimeCode(),
				gobackData.getWorkTypeCode(), 
				gobackData.getStartTime1(),
				gobackData.getEndTime1(), 
				gobackData.getStartTime2(),
				gobackData.getEndTime2());
		GobackReflectPubParameter pubPara = new GobackReflectPubParameter(para.getEmployeeId(), 
				para.getAppDate(),
				para.isOutResReflectAtr(),
				EnumAdaptor.valueOf(para.getPriorStampAtr().value, PriorStampPubAtr.class),
				EnumAdaptor.valueOf(para.getScheAndRecordSameChangeFlg().value, ScheAndRecordSameChangePubFlg.class),
				EnumAdaptor.valueOf(para.getScheTimeReflectAtr().value, ScheTimeReflectPubAtr.class),
				para.isScheReflectAtr(),
				gobackPra,
				para.getExcLogId(),
				this.indentitySet(gobackData.getGetIdentityProcessUseSet()),
				this.approvalSet(gobackData.getGetApprovalProcessingUseSetting()));
		if(isPre) {
			 recordPub.preGobackReflect(pubPara, true);
		} else {
			recordPub.preGobackReflect(pubPara, false);
		}
	}

	@Override
	public void overtimeReflectRecord(OvertimeReflectPara para, boolean isPre) {
		OvertimeAppParameter overtimeInfo = para.getOvertimePara();
		OvertimeAppPubParameter overtimePara = new OvertimeAppPubParameter(overtimeInfo.getWorkTypeCode(),
				overtimeInfo.getWorkTimeCode(),
				overtimeInfo.getStartTime1(),
				overtimeInfo.getEndTime1(),
				overtimeInfo.getStartTime2(),
				overtimeInfo.getEndTime2(),
				overtimeInfo.getMapOvertimeFrame(),
				overtimeInfo.getOverTimeShiftNight(),
				overtimeInfo.getFlexExessTime(),
				EnumAdaptor.valueOf(overtimeInfo.getOverTimeAtr().value, OverTimeRecordPubAtr.class),
				para.getOvertimePara().getAppReason());
		PreOvertimePubParameter preOvertimePara = new PreOvertimePubParameter(para.getEmployeeId(), 
				para.getDateInfo(), 
				para.isActualReflectFlg(), 
				para.isScheReflectFlg(), 
				para.isTimeReflectFlg(), 
				para.isAutoClearStampFlg(), 
				EnumAdaptor.valueOf(para.getScheAndRecordSameChangeFlg().value, ScheAndRecordSameChangePubFlg.class), 
				para.isScheTimeOutFlg(), 
				overtimePara,
				para.getExcLogId(),
				this.indentitySet(para.getGetIdentityProcessUseSet()),
				this.approvalSet(para.getGetApprovalProcessingUseSetting()));
		if(isPre) {
			recordPub.preOvertimeReflect(preOvertimePara);	
		} else {
			//recordPub.afterOvertimeReflect(preOvertimePara);
		}
	}
	private Optional<IdentityProcessUseSetPub> indentitySet(Optional<IdentityProcessUseSetAc> data){
		if(data.isPresent()) {
			IdentityProcessUseSetAc set = data.get();
			IdentityProcessUseSetPub pub = new IdentityProcessUseSetPub(set.getCid(),
					set.isUseConfirmByYourself(),
					set.isUseIdentityOfMonth(),
					set.getYourSelfConfirmError());
			return Optional.of(pub);
		}
		return Optional.empty();
	}
	
	private Optional<ApprovalProcessingUseSettingPub> approvalSet(Optional<ApprovalProcessingUseSettingAc> data){
		if(data.isPresent()) {
			ApprovalProcessingUseSettingAc set = data.get();
			ApprovalProcessingUseSettingPub pub = new ApprovalProcessingUseSettingPub(set.getCid(),
					set.isUseDayApproverConfirm(),
					set.isUseMonthApproverConfirm(),
					set.getLstJobTitleNotUse(),
					set.getSupervisorConfirmErrorAtr());
			return Optional.of(pub);
		}
		return Optional.empty();
	}
	@Override
	public void absenceReflectRecor(WorkChangeCommonReflectPara para, boolean isPre) {
		recordPub.absenceReflect(new WorkChangeCommonReflectPubPara(toPubPara(para.getCommonPara()), para.getExcludeHolidayAtr()), isPre);		
	}

	@Override
	public void holidayWorkReflectRecord(HolidayWorkReflectPara para, boolean isPre) {
		Map<Integer, BreakTimePubParam> mapBreakTimeFrame = new HashMap<>();
		para.getHolidayWorkPara().getMapBreakTimeFrame().forEach((a,b) -> {
			BreakTimePubParam breakTime = new BreakTimePubParam(b.getStartTime(), b.getEndTime());
			mapBreakTimeFrame.put(a, breakTime);
		});
		HolidayWorktimeAppPubPara appPara = new HolidayWorktimeAppPubPara(para.getHolidayWorkPara().getWorkTypeCode(), 
				para.getHolidayWorkPara().getWorkTimeCode(), 
				para.getHolidayWorkPara().getMapWorkTimeFrame(), 
				para.getHolidayWorkPara().getNightTime(), 
				EnumAdaptor.valueOf(para.getHolidayWorkPara().getReflectedState().value, ReflectedStatePubRecord.class), 
				para.getHolidayWorkPara().getReasonNotReflect() == null ? null : EnumAdaptor.valueOf(para.getHolidayWorkPara().getReasonNotReflect().value, ReasonNotReflectDailyPubRecord.class),
				para.getHolidayWorkPara().getStartTime(),
				para.getHolidayWorkPara().getEndTime(),
				mapBreakTimeFrame); 
		HolidayWorkReflectPubPara pubPara = new HolidayWorkReflectPubPara(para.getEmployeeId(), 
				para.getAppDate(),
				para.isHolidayWorkReflectFlg(),
			 	EnumAdaptor.valueOf(para.getScheAndRecordSameChangeFlg().value, ScheAndRecordSameChangePubFlg.class), 
				para.isScheReflectFlg(),
				para.isRecordReflectTimeFlg(),
				para.isRecordReflectBreakFlg(),
				appPara,
				para.getExcLogId(),
				this.indentitySet(para.getGetIdentityProcessUseSet()),
				this.approvalSet(para.getGetApprovalProcessingUseSetting()));
		
		recordPub.holidayWorkReflect(pubPara, isPre);		
	}

	@Override
	public void workChangeReflectRecord(WorkChangeCommonReflectPara para, boolean isPre) {		
		recordPub.workChangeReflect(new WorkChangeCommonReflectPubPara(this.toPubPara(para.getCommonPara()), para.getExcludeHolidayAtr()), isPre);		
	}
	
	private CommonReflectPubParameter toPubPara(CommonReflectPara para) {
		CommonReflectPubParameter pubPara = new CommonReflectPubParameter(para.getEmployeeId(),
				para.getAppDate(), 
				EnumAdaptor.valueOf(para.getScheAndRecordSameChangeFlg().value, ScheAndRecordSameChangePubFlg.class),
				para.isScheTimeReflectAtr(),
				para.getWorktypeCode(), 
				para.getWorkTimeCode(),
				para.getStartTime(),
				para.getEndTime(),
				para.getExcLogId(),
				this.indentitySet(para.getGetIdentityProcessUseSet()),
				this.approvalSet(para.getGetApprovalProcessingUseSetting()));
		return pubPara;
	}

	@Override
	public void absenceLeaveReflectRecord(CommonReflectPara para, boolean isPre) {
		recordPub.absenceLeaveReflect(this.toPubPara(para), isPre);
	}

	@Override
	public void recruitmentReflectRecord(CommonReflectPara para, boolean isPre) {
		recordPub.recruitmentReflect(this.toPubPara(para), isPre);
	}

	@Override
	public void createLogError(String sid, GeneralDate ymd, String excLogId) {
		recordPub.createLogError(sid, ymd, excLogId);
	}

	@Override
	public Optional<IdentityProcessUseSetAc> getIdentityProcessUseSet(String cid) {
		Optional<IdentityProcessUseSetPub> indenSet = recordPub.getIdentityProcessUseSet(cid);
		if(indenSet.isPresent()) {
			IdentityProcessUseSetPub x = indenSet.get();
			IdentityProcessUseSetAc output = new IdentityProcessUseSetAc(x.getCid(),
					x.isUseConfirmByYourself(),
					x.isUseConfirmByYourself(),
					x.getYourSelfConfirmError());
			return Optional.of(output);
		}
		return Optional.empty();
	}

	@Override
	public Optional<ApprovalProcessingUseSettingAc> getApprovalProcessingUseSetting(String cid) {
		Optional<ApprovalProcessingUseSettingPub> appProcSetting = recordPub.getApprovalProcessingUseSetting(cid);
		if(appProcSetting.isPresent()) {
			ApprovalProcessingUseSettingPub x = appProcSetting.get();
			ApprovalProcessingUseSettingAc output = new ApprovalProcessingUseSettingAc(x.getCid(),
					x.isUseDayApproverConfirm(),
					x.isUseMonthApproverConfirm(),
					x.getLstJobTitleNotUse(),
					x.getSupervisorConfirmErrorAtr());
			return Optional.of(output);
		}
		return Optional.empty();
	}
	
	

}

package nts.uk.ctx.at.request.ac.record.dailyperform.appreflect;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.AppCommonPara;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.AppReflectProcessRecordPub;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.CommonReflectPubParameter;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.HolidayWorkReflectPubPara;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.HolidayWorktimeAppPubPara;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.PrePostRecordAtr;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.ReasonNotReflectDailyPubRecord;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.ReasonNotReflectPubRecord;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.ReflectRecordAtr;
import nts.uk.ctx.at.record.pub.dailyperform.appreflect.ReflectedStatePubRecord;
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
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.DisabledSegment_New;
import nts.uk.ctx.at.request.dom.applicationreflect.service.WorkChangeCommonReflectPara;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.AppReflectProcessRecord;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.GobackReflectPara;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.HolidayWorkReflectPara;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.OvertimeAppParameter;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.OvertimeReflectPara;
import nts.uk.ctx.at.request.dom.setting.company.request.RequestSetting;
import nts.uk.ctx.at.request.dom.setting.company.request.RequestSettingRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.ApplicationType;

@Stateless
public class AppReflectProcessRecordImpl implements AppReflectProcessRecord {
	@Inject
	private AppReflectProcessRecordPub recordPub;
	@Inject
	private RequestSettingRepository requestSetting;
	@Override
	public boolean appReflectProcessRecord(Application_New appInfor, boolean chkRecord) {
		Optional<RequestSetting> settingData = requestSetting.findByCompany(appInfor.getCompanyID());
		if(appInfor.getStartDate().isPresent() && appInfor.getEndDate().isPresent()) {
			for(int i = 0; appInfor.getStartDate().get().daysTo(appInfor.getEndDate().get()) - i >= 0; i++){
				GeneralDate loopDate = appInfor.getStartDate().get().addDays(i);
				AppCommonPara para = new AppCommonPara(appInfor.getCompanyID(), 
						appInfor.getEmployeeID(),
						loopDate, 
						settingData.isPresent() ?
								EnumAdaptor.valueOf(settingData.get().getAppReflectAfterConfirm().getAchievementConfirmedAtr().value, ReflectRecordAtr.class) 
								: ReflectRecordAtr.NOT_RFFLECT_CANNOT_REF,
						appInfor.getReflectionInformation().getForcedReflectionReal() == DisabledSegment_New.TODO ? true : false,
						EnumAdaptor.valueOf(appInfor.getReflectionInformation().getStateReflectionReal().value, ReflectedStatePubRecord.class),
						EnumAdaptor.valueOf(appInfor.getReflectionInformation().getStateReflection().value,  ReflectedStatePubRecord.class),
						EnumAdaptor.valueOf(appInfor.getPrePostAtr().value, PrePostRecordAtr.class),
						EnumAdaptor.valueOf(appInfor.getAppType().value, ApplicationType.class),
						appInfor.getReflectionInformation().getForcedReflection() == DisabledSegment_New.TODO ? true : false,
								chkRecord);
				if(!recordPub.appReflectProcess(para)) {
					return false;
				}
			}	
		} else {
			AppCommonPara para = new AppCommonPara(appInfor.getCompanyID(), 
					appInfor.getEmployeeID(),
					appInfor.getAppDate(), 
					settingData.isPresent() ? EnumAdaptor.valueOf(settingData.get().getAppReflectAfterConfirm().getAchievementConfirmedAtr().value, ReflectRecordAtr.class) 
							: ReflectRecordAtr.NOT_RFFLECT_CANNOT_REF,
					appInfor.getReflectionInformation().getForcedReflectionReal() == DisabledSegment_New.TODO ? true : false,
					EnumAdaptor.valueOf(appInfor.getReflectionInformation().getStateReflectionReal().value, ReflectedStatePubRecord.class),
					EnumAdaptor.valueOf(appInfor.getReflectionInformation().getStateReflection().value,  ReflectedStatePubRecord.class),
					EnumAdaptor.valueOf(appInfor.getPrePostAtr().value, PrePostRecordAtr.class),
					EnumAdaptor.valueOf(appInfor.getAppType().value, ApplicationType.class),
					appInfor.getReflectionInformation().getForcedReflection() == DisabledSegment_New.TODO ? true : false,
							chkRecord);
			return recordPub.appReflectProcess(para);
		}
		return true;
	}

	@Override
	public boolean gobackReflectRecord(GobackReflectPara para, boolean isPre) {
		GobackAppPubParameter gobackPra = new GobackAppPubParameter(EnumAdaptor.valueOf(para.getGobackData().getChangeAppGobackAtr().value,
				ChangeAppGobackPubAtr.class), para.getGobackData().getWorkTimeCode(),
				para.getGobackData().getWorkTypeCode(), 
				para.getGobackData().getStartTime1(),
				para.getGobackData().getEndTime1(), 
				para.getGobackData().getStartTime2(),
				para.getGobackData().getEndTime2(),
				EnumAdaptor.valueOf(para.getGobackData().getReflectState().value, ReflectedStatePubRecord.class), 
				para.getGobackData().getReasoNotReflect() == null ? null : EnumAdaptor.valueOf(para.getGobackData().getReasoNotReflect().value, ReasonNotReflectPubRecord.class));
		GobackReflectPubParameter pubPara = new GobackReflectPubParameter(para.getEmployeeId(), 
				para.getDateData(),
				para.isOutResReflectAtr(),
				EnumAdaptor.valueOf(para.getPriorStampAtr().value, PriorStampPubAtr.class),
				EnumAdaptor.valueOf(para.getScheAndRecordSameChangeFlg().value, ScheAndRecordSameChangePubFlg.class),
				EnumAdaptor.valueOf(para.getScheTimeReflectAtr().value, ScheTimeReflectPubAtr.class),
				para.isScheReflectAtr(),
				gobackPra);
		if(isPre) {
			 return recordPub.preGobackReflect(pubPara);
		} else {
			return recordPub.afterGobackReflect(pubPara);
		}
	}

	@Override
	public boolean overtimeReflectRecord(OvertimeReflectPara para, boolean isPre) {
		OvertimeAppParameter overtimeInfo = para.getOvertimePara();
		OvertimeAppPubParameter overtimePara = new OvertimeAppPubParameter(EnumAdaptor.valueOf(overtimeInfo.getReflectedState().value, ReflectedStatePubRecord.class),
				EnumAdaptor.valueOf(overtimeInfo.getReasonNotReflect() == null ? 0 : overtimeInfo.getReasonNotReflect().value, ReasonNotReflectPubRecord.class),
				overtimeInfo.getWorkTypeCode(),
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
				overtimePara);
		if(isPre) {
			return recordPub.preOvertimeReflect(preOvertimePara);	
		} else {
			return recordPub.afterOvertimeReflect(preOvertimePara);
		}
	}

	@Override
	public boolean absenceReflectRecor(WorkChangeCommonReflectPara para, boolean isPre) {
		return recordPub.absenceReflect(new WorkChangeCommonReflectPubPara(toPubPara(para.getCommonPara()), para.getExcludeHolidayAtr()), isPre);		
	}

	@Override
	public boolean holidayWorkReflectRecord(HolidayWorkReflectPara para, boolean isPre) {
		HolidayWorktimeAppPubPara appPara = new HolidayWorktimeAppPubPara(para.getHolidayWorkPara().getWorkTypeCode(), 
				para.getHolidayWorkPara().getWorkTimeCode(), 
				para.getHolidayWorkPara().getMapWorkTimeFrame(), 
				para.getHolidayWorkPara().getNightTime(), 
				EnumAdaptor.valueOf(para.getHolidayWorkPara().getReflectedState().value, ReflectedStatePubRecord.class), 
				para.getHolidayWorkPara().getReasonNotReflect() == null ? null : EnumAdaptor.valueOf(para.getHolidayWorkPara().getReasonNotReflect().value, ReasonNotReflectDailyPubRecord.class),
				para.getHolidayWorkPara().getStartTime(),
				para.getHolidayWorkPara().getEndTime()); 
		HolidayWorkReflectPubPara pubPara = new HolidayWorkReflectPubPara(para.getEmployeeId(), 
				para.getBaseDate(),
				para.isHolidayWorkReflectFlg(),
			 	EnumAdaptor.valueOf(para.getScheAndRecordSameChangeFlg().value, ScheAndRecordSameChangePubFlg.class), 
				para.isScheReflectFlg(), 
				appPara);
		
		return recordPub.holidayWorkReflect(pubPara, isPre);		
	}

	@Override
	public boolean workChangeReflectRecord(WorkChangeCommonReflectPara para, boolean isPre) {		
		return recordPub.workChangeReflect(new WorkChangeCommonReflectPubPara(this.toPubPara(para.getCommonPara()), para.getExcludeHolidayAtr()), isPre);		
	}
	
	private CommonReflectPubParameter toPubPara(CommonReflectPara para) {
		CommonReflectPubParameter pubPara = new CommonReflectPubParameter(para.getEmployeeId(),
				para.getBaseDate(), 
				EnumAdaptor.valueOf(para.getScheAndRecordSameChangeFlg().value, ScheAndRecordSameChangePubFlg.class),
				para.isScheTimeReflectAtr(),
				para.getWorkTypeCode(), 
				para.getWorkTimeCode(),
				para.getStartDate(),
				para.getEndDate(),
				para.getStartTime(),
				para.getEndTime());
		return pubPara;
	}

	@Override
	public boolean absenceLeaveReflectRecord(CommonReflectPara para, boolean isPre) {
		return recordPub.absenceLeaveReflect(this.toPubPara(para), isPre);
	}

	@Override
	public boolean recruitmentReflectRecord(CommonReflectPara para, boolean isPre) {
		return recordPub.recruitmentReflect(this.toPubPara(para), isPre);
	}

	@Override
	public boolean isRecordData(String employeeId, GeneralDate baseDate) {
		// TODO Auto-generated method stub
		return recordPub.isRecordData(employeeId, baseDate);
	}
	
	

}

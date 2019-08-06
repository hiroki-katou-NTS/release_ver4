package nts.uk.ctx.at.request.ac.schedule.appreflectprocess;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.schedule.pub.appreflectprocess.ApplicationGobackScheInforDto;
import nts.uk.ctx.at.request.dom.applicationreflect.service.WorkChangeCommonReflectPara;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.CommonReflectPara;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.GobackAppRequestPara;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.GobackReflectPara;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.HolidayWorkReflectPara;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.HolidayWorktimeAppRequestPara;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workschedule.ApplicationReflectProcessSche;
import nts.uk.ctx.at.schedule.pub.appreflectprocess.CommonReflectSchePubParam;
import nts.uk.ctx.at.schedule.pub.appreflectprocess.WorkChangeCommonReflectSchePubParam;
import nts.uk.ctx.at.schedule.pub.appreflectprocess.AppReflectProcessSchePub;
import nts.uk.ctx.at.schedule.pub.appreflectprocess.ApplyTimeAtrPub;
import nts.uk.ctx.at.schedule.pub.appreflectprocess.ChangeAtrAppGobackPub;
import nts.uk.ctx.at.schedule.pub.appreflectprocess.ApplicationReflectParamScheDto;

@Stateless
public class ApplicationReflectProcessScheImpl implements ApplicationReflectProcessSche{
	@Inject
	private AppReflectProcessSchePub appReflectSchePub;

	@Override
	public void goBackDirectlyReflect(GobackReflectPara gobackInfor) {
		GobackAppRequestPara gobackData = gobackInfor.getGobackData();
		ApplicationGobackScheInforDto appInfor = new ApplicationGobackScheInforDto(EnumAdaptor.valueOf(gobackData.getChangeAppGobackAtr().value, ChangeAtrAppGobackPub.class) ,
				gobackData.getWorkTypeCode(),
				gobackData.getWorkTimeCode(),
				gobackData.getStartTime1(),
				gobackData.getEndTime1(),
				gobackData.getStartTime2(),
				gobackData.getEndTime2());
		ApplicationReflectParamScheDto dto = new ApplicationReflectParamScheDto(gobackInfor.getEmployeeId(), 
				gobackInfor.getAppDate(),
				gobackInfor.isOutResReflectAtr(),
				appInfor, 
				EnumAdaptor.valueOf(gobackInfor.getApplyTimeAtr().value, ApplyTimeAtrPub.class)); 
		appReflectSchePub.goBackDirectlyReflectSch(dto);
	}

	@Override
	public void forleaveReflect(WorkChangeCommonReflectPara absenceInfor) {
		CommonReflectPara commonPara = absenceInfor.getCommonPara();
		CommonReflectSchePubParam leavePra = new CommonReflectSchePubParam(commonPara.getEmployeeId(),
				commonPara.getAppDate(),
				commonPara.getWorktypeCode(), //勤務種類=INPUT．勤務種類コード chi update workType
				commonPara.getWorkTimeCode(),
				commonPara.getStartTime(),
				commonPara.getEndTime());
		WorkChangeCommonReflectSchePubParam paraInput = new WorkChangeCommonReflectSchePubParam(leavePra, absenceInfor.getExcludeHolidayAtr());
		appReflectSchePub.appForLeaveSche(paraInput);
	}

	@Override
	public void workChangeReflect(WorkChangeCommonReflectPara workchangeInfor) {
		CommonReflectPara commonPara = workchangeInfor.getCommonPara();
		CommonReflectSchePubParam workChangePara = new CommonReflectSchePubParam(commonPara.getEmployeeId(), 
				commonPara.getAppDate(), 
				commonPara.getWorktypeCode(), 
				commonPara.getWorkTimeCode(),
				commonPara.getStartTime(),
				commonPara.getEndTime());
		WorkChangeCommonReflectSchePubParam paramInput = new WorkChangeCommonReflectSchePubParam(workChangePara, workchangeInfor.getExcludeHolidayAtr());
		appReflectSchePub.appWorkChangeReflect(paramInput);
	}

	@Override
	public void holidayWorkReflect(HolidayWorkReflectPara holidayworkInfor) {
		HolidayWorktimeAppRequestPara holidayWorkPara = holidayworkInfor.getHolidayWorkPara();
		CommonReflectSchePubParam holidayWork = new CommonReflectSchePubParam(holidayworkInfor.getEmployeeId(), 
				holidayworkInfor.getAppDate(), 
				holidayWorkPara.getWorkTypeCode(),
				holidayWorkPara.getWorkTimeCode(),
				holidayWorkPara.getStartTime(),
				holidayWorkPara.getEndTime());
		appReflectSchePub.holidayWorkReflectSche(holidayWork);
	}

	@Override
	public void ebsenceLeaveReflect(CommonReflectPara absenceLeaveAppInfor) {
		CommonReflectSchePubParam absenceLeave = new CommonReflectSchePubParam(absenceLeaveAppInfor.getEmployeeId(),
				absenceLeaveAppInfor.getAppDate(),
				absenceLeaveAppInfor.getWorktypeCode(),
				absenceLeaveAppInfor.getWorkTimeCode(), 
				absenceLeaveAppInfor.getStartTime(),
				absenceLeaveAppInfor.getEndTime());
		appReflectSchePub.absenceLeaveReflectSche(absenceLeave);
	}

	@Override
	public void recruitmentReflect(CommonReflectPara recruitmentInfor) {
		CommonReflectSchePubParam recruitment = new CommonReflectSchePubParam(recruitmentInfor.getEmployeeId(), 
				recruitmentInfor.getAppDate(), 
				recruitmentInfor.getWorktypeCode(), 
				recruitmentInfor.getWorkTimeCode(), 
				recruitmentInfor.getStartTime(), 
				recruitmentInfor.getEndTime());
		appReflectSchePub.recruitmentReflectSche(recruitment);
	}
}

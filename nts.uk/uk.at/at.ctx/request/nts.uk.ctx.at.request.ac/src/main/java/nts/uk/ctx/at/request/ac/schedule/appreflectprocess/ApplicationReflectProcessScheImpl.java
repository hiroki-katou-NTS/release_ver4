package nts.uk.ctx.at.request.ac.schedule.appreflectprocess;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.schedule.pub.appreflectprocess.ApplicationGobackScheInforDto;
import nts.uk.ctx.at.request.dom.application.appabsence.AppAbsence;
import nts.uk.ctx.at.request.dom.application.holidayshipment.recruitmentapp.RecruitmentApp;
import nts.uk.ctx.at.request.dom.application.workchange.AppWorkChange;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workschedule.ApplicationReflectProcessSche;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workschedule.ReflectScheDto;
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
	public boolean goBackDirectlyReflect(ReflectScheDto reflectSche) {
		ApplicationGobackScheInforDto appInfo = new ApplicationGobackScheInforDto(
				reflectSche.getGoBackDirectly().getWorkChangeAtr().isPresent() ? EnumAdaptor.valueOf(reflectSche.getGoBackDirectly().getWorkChangeAtr().get().value, ChangeAtrAppGobackPub.class) : null,
				reflectSche.getGoBackDirectly().getWorkTypeCD().isPresent() ? reflectSche.getGoBackDirectly().getWorkTypeCD().get().v() : null,
				reflectSche.getGoBackDirectly().getSiftCD().isPresent() ? reflectSche.getGoBackDirectly().getSiftCD().get().v() : null,
				reflectSche.getGoBackDirectly().getWorkTimeStart1().isPresent() ? reflectSche.getGoBackDirectly().getWorkTimeStart1().get().v() : null,
				reflectSche.getGoBackDirectly().getWorkTimeEnd1().isPresent() ? reflectSche.getGoBackDirectly().getWorkTimeEnd1().get().v() : null,
				reflectSche.getGoBackDirectly().getWorkTimeStart2().isPresent() ? reflectSche.getGoBackDirectly().getWorkTimeStart2().get().v() : null,
				reflectSche.getGoBackDirectly().getWorkTimeEnd2().isPresent() ? reflectSche.getGoBackDirectly().getWorkTimeEnd2().get().v() : null);
		ApplicationReflectParamScheDto dto = new ApplicationReflectParamScheDto(reflectSche.getEmployeeId(), 
				reflectSche.getDatePara(),
				reflectSche.getReflectSetting().isChokochoki(),
				appInfo, 
				EnumAdaptor.valueOf(reflectSche.getTimeAtr().value,
						ApplyTimeAtrPub.class)); 
		return appReflectSchePub.goBackDirectlyReflectSch(dto);
	}

	@Override
	public boolean forleaveReflect(ReflectScheDto reflectSche) {
		AppAbsence forLeave = reflectSche.getForLeave();
		CommonReflectSchePubParam leavePra = new CommonReflectSchePubParam(reflectSche.getEmployeeId(),
				reflectSche.getDatePara(),
				reflectSche.getForLeave().getWorkTypeCode() != null ? reflectSche.getForLeave().getWorkTypeCode().v() : null, //勤務種類=INPUT．勤務種類コード chi update workType
				null,
				reflectSche.getAppInfor().getStartDate().isPresent() ? reflectSche.getAppInfor().getStartDate().get() : null,
				reflectSche.getAppInfor().getEndDate().isPresent() ? reflectSche.getAppInfor().getEndDate().get() : null,
						forLeave.getStartTime1() != null ? forLeave.getStartTime1().v() : null,
								forLeave.getEndTime1() != null ? forLeave.getEndTime1().v() : null);
		WorkChangeCommonReflectSchePubParam paraInput = new WorkChangeCommonReflectSchePubParam(leavePra, forLeave.isChangeWorkHour() == true ? 1 : 0);
		return appReflectSchePub.appForLeaveSche(paraInput);
	}

	@Override
	public boolean workChangeReflect(ReflectScheDto reflectSche) {
		AppWorkChange workChange = reflectSche.getWorkChange();
		CommonReflectSchePubParam workChangePara = new CommonReflectSchePubParam(reflectSche.getEmployeeId(), 
				reflectSche.getDatePara(), 
				workChange.getWorkTypeCd(), 
				workChange.getWorkTimeCd(),
				reflectSche.getAppInfor().getStartDate() == null ? null : reflectSche.getAppInfor().getStartDate().get(),
				reflectSche.getAppInfor().getEndDate() == null ? null : reflectSche.getAppInfor().getEndDate().get(),
				null,
				null);
		WorkChangeCommonReflectSchePubParam paramInput = new WorkChangeCommonReflectSchePubParam(workChangePara, workChange.getExcludeHolidayAtr());
		return appReflectSchePub.appWorkChangeReflect(paramInput);
	}

	@Override
	public boolean holidayWorkReflect(ReflectScheDto relectSche) {
		CommonReflectSchePubParam holidayWork = new CommonReflectSchePubParam(relectSche.getEmployeeId(), 
				relectSche.getDatePara(), 
				relectSche.getHolidayWork().getWorkTypeCode().v(),
				relectSche.getHolidayWork().getWorkTimeCode().v(),
				null, 
				null,
				relectSche.getHolidayWork().getWorkClock1().getStartTime() != null ? relectSche.getHolidayWork().getWorkClock1().getStartTime().v() : null,
				relectSche.getHolidayWork().getWorkClock1().getEndTime() != null ? relectSche.getHolidayWork().getWorkClock1().getEndTime().v() : null);
		return appReflectSchePub.holidayWorkReflectSche(holidayWork);
	}

	@Override
	public boolean ebsenceLeaveReflect(ReflectScheDto relectSche) {
		CommonReflectSchePubParam absenceLeave = new CommonReflectSchePubParam(relectSche.getEmployeeId(),
				relectSche.getDatePara(),
				relectSche.getAbsenceLeave().getWorkTypeCD() == null ? null : relectSche.getAbsenceLeave().getWorkTypeCD().v(),
				relectSche.getAbsenceLeave().getWorkTimeCD(), 
				relectSche.getAppInfor().getStartDate().isPresent() ? relectSche.getAppInfor().getStartDate().get() : null,
				relectSche.getAppInfor().getEndDate().isPresent() ? relectSche.getAppInfor().getEndDate().get() : null,
				relectSche.getAbsenceLeave().getWorkTime1().getStartTime() != null ? relectSche.getAbsenceLeave().getWorkTime1().getStartTime().v() : null,
				relectSche.getAbsenceLeave().getWorkTime1().getEndTime() != null ? relectSche.getAbsenceLeave().getWorkTime1().getEndTime().v() : null);
		return appReflectSchePub.absenceLeaveReflectSche(absenceLeave);
	}

	@Override
	public boolean recruitmentReflect(ReflectScheDto relectSche) {
		RecruitmentApp recruitmentData = relectSche.getRecruitment();
		CommonReflectSchePubParam recruitment = new CommonReflectSchePubParam(relectSche.getEmployeeId(), 
				relectSche.getDatePara(), 
				recruitmentData.getWorkTypeCD() != null ? recruitmentData.getWorkTypeCD().v() : null, 
				recruitmentData.getWorkTimeCD() != null ? recruitmentData.getWorkTimeCD().v() : null, 
				relectSche.getAppInfor().getStartDate().isPresent() ? relectSche.getAppInfor().getStartDate().get() : null, 
				relectSche.getAppInfor().getEndDate().isPresent() ? relectSche.getAppInfor().getEndDate().get() : null, 
				recruitmentData.getWorkTime1().getStartTime() != null ? recruitmentData.getWorkTime1().getStartTime().v() : null, 
				recruitmentData.getWorkTime1().getEndTime() != null ? recruitmentData.getWorkTime1().getEndTime().v() : null);
		return appReflectSchePub.recruitmentReflectSche(recruitment);
	}

	@Override
	public boolean isSche(String employeeId, GeneralDate baseDate) {
		
		return appReflectSchePub.isSche(employeeId, baseDate);
	}
	
	

}

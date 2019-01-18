package nts.uk.ctx.at.schedule.pubimp.appreflectprocess;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.ApplyTimeAtr;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.CommonReflectParamSche;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.absenceleave.AbsenceLeaveReflectSche;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.appforleave.ForleaveReflectSche;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.gobacksche.ApplicationGobackScheInfor;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.gobacksche.ChangeAtrAppGoback;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.gobacksche.GoBackDirectlyReflectSche;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.gobacksche.GobackReflectParam;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.holidaywork.HolidayWorkReflectSche;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.recruitment.RecruitmentAppReflectSche;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.workchange.WorkChangeReflectServiceSche;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.workchange.WorkChangecommonReflectParamSche;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicSchedule;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicScheduleRepository;
import nts.uk.ctx.at.schedule.pub.appreflectprocess.CommonReflectSchePubParam;
import nts.uk.ctx.at.schedule.pub.appreflectprocess.WorkChangeCommonReflectSchePubParam;
import nts.uk.ctx.at.schedule.pub.appreflectprocess.AppReflectProcessSchePub;
import nts.uk.ctx.at.schedule.pub.appreflectprocess.ApplicationReflectParamScheDto;

@Stateless
public class AppReflectProcessSchePubImpl implements AppReflectProcessSchePub{
	@Inject
	private GoBackDirectlyReflectSche goBackReflect;
	@Inject
	private ForleaveReflectSche leaveReflect;
	@Inject
	private WorkChangeReflectServiceSche workchangeReflect;
	@Inject
	private HolidayWorkReflectSche holidayWorkReflect;
	@Inject
	private AbsenceLeaveReflectSche absenceLeaveReflect;
	@Inject
	private RecruitmentAppReflectSche recruitmentReflect;
	@Inject
	private BasicScheduleRepository basicSche;
	@Override
	public boolean goBackDirectlyReflectSch(ApplicationReflectParamScheDto reflectPara) {
		ApplicationGobackScheInfor gobackInfo = new ApplicationGobackScheInfor(EnumAdaptor.valueOf(reflectPara.getGobackInfor().getChangeAtrAppGoback().value, ChangeAtrAppGoback.class),
				reflectPara.getGobackInfor().getWorkType(),
				reflectPara.getGobackInfor().getWorkTime(),
				reflectPara.getGobackInfor().getWorkTimeStart1(),
				reflectPara.getGobackInfor().getWorkTimeEnd1(),
				reflectPara.getGobackInfor().getWorkTimeStart2(),
				reflectPara.getGobackInfor().getWorkTimeEnd2()); 
		GobackReflectParam data = new GobackReflectParam(reflectPara.getEmployeeId(), 
				reflectPara.getDatePara(), 
				reflectPara.isOutsetBreakReflectAtr(),
				gobackInfo, 
				EnumAdaptor.valueOf(reflectPara.getApplyTimeAtr().value, ApplyTimeAtr.class)); 
			
		return  goBackReflect.goBackDirectlyReflectSch(data);
	}

	@Override
	public boolean appForLeaveSche(WorkChangeCommonReflectSchePubParam appForleaverPara) {
		return leaveReflect.forlearveReflectSche(new WorkChangecommonReflectParamSche(this.toParamSche(appForleaverPara.getCommon()),
				appForleaverPara.getExcludeHolidayAtr()));
	}

	@Override
	public boolean appWorkChangeReflect(WorkChangeCommonReflectSchePubParam workChangeParam) {
		return workchangeReflect.reflectWorkChange(new WorkChangecommonReflectParamSche(this.toParamSche(workChangeParam.getCommon()),
				workChangeParam.getExcludeHolidayAtr()));
	}
	
	private CommonReflectParamSche toParamSche(CommonReflectSchePubParam schePubParam) {
		CommonReflectParamSche paramSche = new CommonReflectParamSche(schePubParam.getEmployeeId(), 
				schePubParam.getDatePara(),
				schePubParam.getWorktypeCode(),
				schePubParam.getWorkTimeCode(),
				schePubParam.getStartDate(),
				schePubParam.getEndDate(),
				schePubParam.getStartTime(),
				schePubParam.getEndTime());
		return paramSche;
	}

	@Override
	public boolean holidayWorkReflectSche(CommonReflectSchePubParam holidayWorkParam) {		
		return holidayWorkReflect.holidayWorkReflect(this.toParamSche(holidayWorkParam));
	}

	@Override
	public boolean absenceLeaveReflectSche(CommonReflectSchePubParam absenceLeaverParam) {		
		return absenceLeaveReflect.absenceLeaveReflect(this.toParamSche(absenceLeaverParam));
	}

	@Override
	public boolean recruitmentReflectSche(CommonReflectSchePubParam recruitmentParam) {
		return recruitmentReflect.recruitmentReflect(this.toParamSche(recruitmentParam));
	}

	@Override
	public boolean isSche(String employeeId, GeneralDate baseDate) {
		Optional<BasicSchedule> optBasicScheOpt = basicSche.find(employeeId, baseDate);		
		if(!optBasicScheOpt.isPresent()) {
			return false;
		}
		return true;
	}


}

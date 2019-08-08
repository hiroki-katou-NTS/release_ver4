package nts.uk.ctx.at.schedule.pubimp.appreflectprocess;
import javax.ejb.Stateless;
import javax.inject.Inject;
import nts.arc.enums.EnumAdaptor;
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
	@Override
	public void goBackDirectlyReflectSch(ApplicationReflectParamScheDto reflectPara) {
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
			
		goBackReflect.goBackDirectlyReflectSch(data);
	}

	@Override
	public void appForLeaveSche(WorkChangeCommonReflectSchePubParam appForleaverPara) {
		leaveReflect.forlearveReflectSche(new WorkChangecommonReflectParamSche(this.toParamSche(appForleaverPara.getCommon()),
				appForleaverPara.getExcludeHolidayAtr()));
	}

	@Override
	public void appWorkChangeReflect(WorkChangeCommonReflectSchePubParam workChangeParam) {
		workchangeReflect.reflectWorkChange(new WorkChangecommonReflectParamSche(this.toParamSche(workChangeParam.getCommon()),
				workChangeParam.getExcludeHolidayAtr()));
	}
	
	private CommonReflectParamSche toParamSche(CommonReflectSchePubParam schePubParam) {
		CommonReflectParamSche paramSche = new CommonReflectParamSche(schePubParam.getEmployeeId(), 
				schePubParam.getDatePara(),
				schePubParam.getWorktypeCode(),
				schePubParam.getWorkTimeCode(),
				schePubParam.getStartTime(),
				schePubParam.getEndTime());
		return paramSche;
	}

	@Override
	public void holidayWorkReflectSche(CommonReflectSchePubParam holidayWorkParam) {		
		holidayWorkReflect.holidayWorkReflect(this.toParamSche(holidayWorkParam));
	}

	@Override
	public void absenceLeaveReflectSche(CommonReflectSchePubParam absenceLeaverParam) {		
		absenceLeaveReflect.absenceLeaveReflect(this.toParamSche(absenceLeaverParam));
	}

	@Override
	public void recruitmentReflectSche(CommonReflectSchePubParam recruitmentParam) {
		recruitmentReflect.recruitmentReflect(this.toParamSche(recruitmentParam));
	}
}

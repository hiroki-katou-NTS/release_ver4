package nts.uk.ctx.at.schedule.pubimp.appreflectprocess;

import javax.ejb.Stateless;
import javax.inject.Inject;
import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.ApplyTimeAtr;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.CommonReflectParamSche;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.appforleave.ForleaveReflectSche;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.gobacksche.ApplicationGobackScheInfor;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.gobacksche.ChangeAtrAppGoback;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.gobacksche.GoBackDirectlyReflectSche;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.gobacksche.GobackReflectParam;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.service.workchange.WorkChangeReflectServiceSche;
import nts.uk.ctx.at.schedule.pub.appreflectprocess.CommonReflectSchePubParam;
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
				true,
				gobackInfo, 
				EnumAdaptor.valueOf(reflectPara.getApplyTimeAtr().value, ApplyTimeAtr.class)); 
			
		return  goBackReflect.goBackDirectlyReflectSch(data);
	}

	@Override
	public void appForLeaveSche(CommonReflectSchePubParam appForleaverPara) {
		CommonReflectParamSche leaverPara = new CommonReflectParamSche(appForleaverPara.getEmployeeId(), 
				appForleaverPara.getDatePara(),
				appForleaverPara.getWorktypeCode(),
				appForleaverPara.getWorkTimeCode());
		leaveReflect.forlearveReflectSche(leaverPara);
	}

	@Override
	public boolean appWorkChangeReflect(CommonReflectSchePubParam workChangeParam) {
		CommonReflectParamSche paraWorkchange = new CommonReflectParamSche(workChangeParam.getEmployeeId(), 
				workChangeParam.getDatePara(), 
				workChangeParam.getWorktypeCode(),
				workChangeParam.getWorkTimeCode());
		return workchangeReflect.reflectWorkChange(paraWorkchange);
	}

}

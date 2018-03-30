package nts.uk.ctx.at.request.ac.schedule.appreflectprocess;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.schedule.pub.appreflectprocess.ApplicationGobackScheInforDto;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workschedule.ApplicationReflectProcessSche;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workschedule.ReflectScheDto;
import nts.uk.ctx.at.schedule.pub.appreflectprocess.AppForLeavePubDto;
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
		ApplicationGobackScheInforDto appInfo = new ApplicationGobackScheInforDto(EnumAdaptor.valueOf(reflectSche.getGoBackDirectly().getWorkChangeAtr().value, ChangeAtrAppGobackPub.class),
				reflectSche.getGoBackDirectly().getWorkTypeCD().v(),
				reflectSche.getGoBackDirectly().getSiftCD().v(),
				reflectSche.getGoBackDirectly().getWorkTimeStart1().v(),
				reflectSche.getGoBackDirectly().getWorkTimeEnd2().v(),
				reflectSche.getGoBackDirectly().getWorkTimeStart2().v(),
				reflectSche.getGoBackDirectly().getWorkTimeEnd2().v());
		ApplicationReflectParamScheDto dto = new ApplicationReflectParamScheDto(reflectSche.getEmployeeId(), 
				reflectSche.getDatePara(),
				true,
				appInfo, 
				EnumAdaptor.valueOf(reflectSche.getTimeAtr().value, ApplyTimeAtrPub.class)); 
		return appReflectSchePub.goBackDirectlyReflectSch(dto);
	}

	@Override
	public void forleaveReflect(ReflectScheDto reflectSche) {
		AppForLeavePubDto leavePra = new AppForLeavePubDto(reflectSche.getEmployeeId(), reflectSche.getDatePara(), reflectSche.getForLeave().getWorkTypeCode().v());
		appReflectSchePub.appForLeaveSche(leavePra);
	}
	
	

}

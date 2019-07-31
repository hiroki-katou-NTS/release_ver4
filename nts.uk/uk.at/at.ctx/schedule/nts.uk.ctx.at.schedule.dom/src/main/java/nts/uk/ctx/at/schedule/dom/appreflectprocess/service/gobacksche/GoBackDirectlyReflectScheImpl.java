package nts.uk.ctx.at.schedule.dom.appreflectprocess.service.gobacksche;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicSchedule;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicScheduleRepository;
import nts.uk.ctx.at.schedule.dom.schedule.workschedulestate.WorkScheduleState;
import nts.uk.ctx.at.schedule.dom.schedule.workschedulestate.WorkScheduleStateRepository;

@Stateless
public class GoBackDirectlyReflectScheImpl implements GoBackDirectlyReflectSche{
	@Inject
	private WorkTypeHoursReflectSche workTypeHoursReflectSche;
	@Inject
	private TimeOfDayReflectGoBackSche gobackSche;
	@Inject
	private WorkScheduleStateRepository workScheReposi;

	@Inject
	private BasicScheduleRepository basicScheRepo;
	@Override
	public void goBackDirectlyReflectSch(GobackReflectParam reflectPara) {
		BasicSchedule scheData = basicScheRepo.find(reflectPara.getEmployeeId(), reflectPara.getDatePara()).get();
		List<WorkScheduleState> lstState = workScheReposi.findByDateAndEmpId(reflectPara.getEmployeeId(), reflectPara.getDatePara());
		//勤種・就時の反映
		boolean workTypeReflect = workTypeHoursReflectSche.isReflectFlag(reflectPara, scheData, lstState);
		//時刻の反映
		reflectPara.setOutsetBreakReflectAtr(workTypeReflect);
		gobackSche.stampReflectGobackSche(reflectPara, scheData, lstState);
		basicScheRepo.update(scheData);
		workScheReposi.updateOrInsert(lstState);
	}

}

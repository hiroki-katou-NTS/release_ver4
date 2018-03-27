package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.overtime;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.ApplicationReflectOutput;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.ReasonNotReflectRecord;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.ReflectedStateRecord;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.ReflectParameter;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.ScheWorkUpdateService;


@Stateless
public class AfterOvertimeReflectServiceImpl implements AfterOvertimeReflectService {
	@Inject
	private AfterOvertimeReflectProcess afterOvertimeProcess;
	@Inject
	private ScheWorkUpdateService workUpdate;
	@Override
	public void scheReflectJobType(OvertimeParameter overtimePara) {
		if(!afterOvertimeProcess.checkScheReflect(overtimePara) ) {
			return;
		}
		//予定勤種・就時の反映
		ReflectParameter reflectInfo = new ReflectParameter(overtimePara.getEmployeeId(), 
				overtimePara.getDateInfo(), 
				overtimePara.getOvertimePara().getWorkTimeCode(), 
				overtimePara.getOvertimePara().getWorkTypeCode()); 
		workUpdate.updateWorkTimeType(reflectInfo, true);
	}

	@Override
	public ApplicationReflectOutput reflectAfterOvertime(OvertimeParameter overtimePara) {
		try {
			ApplicationReflectOutput output = new ApplicationReflectOutput(overtimePara.getOvertimePara().getReflectedState(), overtimePara.getOvertimePara().getReasonNotReflect());
			//予定勤種就時の反映
			this.scheReflectJobType(overtimePara);
			//勤種・就時の反映
			//予定勤種・就時の反映
			ReflectParameter reflectInfo = new ReflectParameter(overtimePara.getEmployeeId(), 
					overtimePara.getDateInfo(), 
					overtimePara.getOvertimePara().getWorkTimeCode(), 
					overtimePara.getOvertimePara().getWorkTypeCode()); 
			workUpdate.updateWorkTimeType(reflectInfo, false);
			output.setReflectedState(ReflectedStateRecord.REFLECTED);
			//dang lay nham thong tin enum
			output.setReasonNotReflect(ReasonNotReflectRecord.ACTUAL_CONFIRMED);
			return output;
		} catch (Exception e) {
			return new ApplicationReflectOutput(overtimePara.getOvertimePara().getReflectedState(), overtimePara.getOvertimePara().getReasonNotReflect());
		}
		
	}

}

package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.absence;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.CommonReflectParameter;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.ApplicationReflectOutput;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.CommonProcessCheckService;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.ReasonNotReflectRecord;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.ReflectedStateRecord;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.ScheWorkUpdateService;
@Stateless
public class AbsenceReflectServiceImpl implements AbsenceReflectService{
	
	@Inject
	private ScheWorkUpdateService workTimeUpdate;
	@Inject
	private CommonProcessCheckService commonService;
	@Override
	public ApplicationReflectOutput absenceReflect(CommonReflectParameter absencePara, boolean isPre) {
		try {
			//予定勤種の反映
			commonService.reflectScheWorkTimeWorkType(absencePara, isPre);
			//勤種の反映
			workTimeUpdate.updateRecordWorkType(absencePara.getEmployeeId(), absencePara.getBaseDate(), absencePara.getWorkTypeCode(), false);
			return new ApplicationReflectOutput(ReflectedStateRecord.REFLECTED, ReasonNotReflectRecord.ACTUAL_CONFIRMED);
		} catch (Exception e) {
			return new ApplicationReflectOutput(absencePara.getReflectState(), absencePara.getReasoNotReflect());
		}
	}

	
}

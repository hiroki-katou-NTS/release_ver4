package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.overtime;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.ScheAndRecordSameChangeFlg;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeIsFluidWork;

@Stateless
public class AfterOvertimeReflectProcessImpl implements AfterOvertimeReflectProcess {
	@Inject
	private WorkTimeIsFluidWork workTimeService;
	@Override
	public boolean checkScheReflect(OvertimeParameter overtimePara) {
		//ＩNPUT．勤務種類コードとＩNPUT．就業時間帯コードをチェックする
		if((overtimePara.getOvertimePara().getWorkTimeCode().isEmpty()
				&& overtimePara.getOvertimePara().getWorkTypeCode().isEmpty())
				|| !overtimePara.isActualReflectFlg()) {
			return true;
		}
		return false;
	}

}

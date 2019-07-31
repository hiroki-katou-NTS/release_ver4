package nts.uk.ctx.at.schedule.dom.appreflectprocess.service.gobacksche;

import java.util.List;

import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicSchedule;
import nts.uk.ctx.at.schedule.dom.schedule.workschedulestate.WorkScheduleState;

public interface WorkTypeHoursReflectSche {
	/**
	 * 勤種・就時の反映
	 * @param gobackPara
	 * @return
	 */
	public boolean isReflectFlag(GobackReflectParam gobackPara, BasicSchedule scheData, List<WorkScheduleState> lstState);
	/**
	 * 勤種・就時を反映できるかチェックする
	 * @param gobackPara
	 * @return 反映できるフラグ（true, false）
	 */
	public boolean isCheckReflect(GobackReflectParam gobackPara, BasicSchedule basicScheOpt);
}

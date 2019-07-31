package nts.uk.ctx.at.schedule.dom.appreflectprocess.service.gobacksche;

import java.util.List;

import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicSchedule;
import nts.uk.ctx.at.schedule.dom.schedule.workschedulestate.WorkScheduleState;

/**
 * 時刻の反映
 * @author dudt
 *
 */
public interface TimeOfDayReflectGoBackSche {
	/**
	 * 時刻の反映
	 * @param reflectPara
	 * @return
	 */
	public void stampReflectGobackSche(GobackReflectParam reflectPara, BasicSchedule scheData, List<WorkScheduleState> lstState);
	/**
	 * 反映する時刻を求める
	 * @param reflectPara
	 * @return
	 */
	public TimeOfDayReflectFindDto timeReflectFind(GobackReflectParam reflectPara);

}

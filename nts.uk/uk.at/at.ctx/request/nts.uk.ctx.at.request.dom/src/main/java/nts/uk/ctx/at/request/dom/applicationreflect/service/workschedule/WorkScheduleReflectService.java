package nts.uk.ctx.at.request.dom.applicationreflect.service.workschedule;

import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.AppReflectRecordPara;

/**
 * 勤務予定に反映
 * @author dudt
 *
 */
public interface WorkScheduleReflectService {
	/**
	 * 勤務予定に反映
	 * @param executionTupe
	 * @return
	 */
	public void workscheReflect(AppReflectRecordPara appRecordInfor);
}

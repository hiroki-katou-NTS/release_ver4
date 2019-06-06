package nts.uk.ctx.at.schedule.dom.appreflectprocess.service.workchange;


/**
 * 勤務予定に反映 (勤務変更申請)勤務予定への反映
 * @author do_dt
 *
 */
public interface WorkChangeReflectServiceSche {
	/**
	 * (勤務変更申請)勤務予定への反映
	 * @param param
	 * @return
	 */
	public void reflectWorkChange(WorkChangecommonReflectParamSche workchangeParam);

}

package nts.uk.ctx.at.request.dom.applicationreflect.service.workschedule;

public interface ApplicationReflectProcessSche {
	/**
	 * 直行直帰申請
	 * @param reflectSche
	 * @return
	 */
	public void goBackDirectlyReflect(ReflectScheDto reflectSche);
	/**
	 * 休暇申請
	 * @param reflectSche
	 */
	public void forleaveReflect(ReflectScheDto reflectSche);
	/**
	 * 勤務変更申請
	 * @param reflectSche
	 * @return
	 */
	public void workChangeReflect(ReflectScheDto reflectSche);
	/**
	 * 休日出勤申請
	 * @param relectSche
	 * @return
	 */
	public void holidayWorkReflect(ReflectScheDto relectSche);
	/**
	 * 振休申請の反映
	 * @param relectSche
	 * @return
	 */
	public void ebsenceLeaveReflect(ReflectScheDto relectSche);
	/**
	 * 振出申請の反映
	 * @param relectSche
	 * @return
	 */
	public void recruitmentReflect(ReflectScheDto relectSche);	
}

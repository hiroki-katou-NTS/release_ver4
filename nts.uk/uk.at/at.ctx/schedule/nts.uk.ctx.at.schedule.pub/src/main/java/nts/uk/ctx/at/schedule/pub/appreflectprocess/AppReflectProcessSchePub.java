package nts.uk.ctx.at.schedule.pub.appreflectprocess;

public interface AppReflectProcessSchePub {
	/**
	 * 	直行直帰申請: 勤務予定への反映
	 * @param reflectPara
	 * @return
	 */
	public boolean goBackDirectlyReflectSch(ApplicationReflectParamScheDto reflectPara);
	/**
	 * 休暇申請
	 * @param reflectPara
	 */
	public void appForLeaveSche(CommonReflectSchePubParam appForleaverPara);
	/**
	 * 勤務変更申請
	 * @param workChangeParam
	 */
	public boolean appWorkChangeReflect(CommonReflectSchePubParam workChangeParam);

}

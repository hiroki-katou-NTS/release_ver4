package nts.uk.ctx.at.request.dom.application.appforleave;

/**
 * @author loivt
 * 終日半日休暇
 */
public enum AllDayHalfDayLeaveAtr {
	/**
	 * 終日休暇
	 */
	ALL_DAY_LEAVE(1),
	/**
	 * 半日休暇
	 */
	HALF_DAY_LEAVE(0);
	
	public final int value;
	
	private AllDayHalfDayLeaveAtr(int value){
		this.value = value;
	}
}

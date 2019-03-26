package nts.uk.ctx.at.record.dom.workrecord.actualsituation.confirmstatusmonthly;
/**
 * 実施可否区分
 * @author tutk
 *
 */
public enum AvailabilityAtr {
	/**
	 * 実施できない
	 */
	CAN_NOT_RELEASE(0), 
	/**
	 * 実施できる
	 */
	CAN_RELEASE(1);

	public final int value;

	private AvailabilityAtr(int value) {
		this.value = value;
	}
}

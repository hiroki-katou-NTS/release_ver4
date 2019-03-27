package nts.uk.ctx.at.record.dom.workrecord.actualsituation.confirmstatusmonthly;
/**
 * 解除可否区分
 * @author tutk
 *
 */
public enum ReleasedAtr {
	/**
	 * 解除できない
	 */
	CAN_NOT_RELEASE(0), 
	/**
	 * 解除できる
	 */
	CAN_RELEASE(1);

	public final int value;

	private ReleasedAtr(int value) {
		this.value = value;
	}
}

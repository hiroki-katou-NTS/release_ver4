package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus;

/**
 * @author thanhnx
 * 実施可否区分 - 解除可否区分
 */
public enum ReleasedAtr {
	
	/**
	 * 実施できない - 解除できない
	 */
	CAN_NOT_IMPLEMENT(0),
	
	/**
	 * 実施できる - 解除できる
	 */
	CAN_IMPLEMENT(1);
	
	public final int value;
	
    private ReleasedAtr(int value){
		this.value = value;
	}

	private final static ReleasedAtr[] values = ReleasedAtr.values();
	
    public static ReleasedAtr valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (ReleasedAtr val : ReleasedAtr.values) {
			if (val.value == value) {
				return val;
			}
		}
		return null;
	}
}

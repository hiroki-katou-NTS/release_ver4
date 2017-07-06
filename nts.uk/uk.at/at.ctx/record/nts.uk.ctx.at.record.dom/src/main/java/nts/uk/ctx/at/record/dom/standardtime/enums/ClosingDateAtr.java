package nts.uk.ctx.at.record.dom.standardtime.enums;

/**
 * 
 * @author nampt
 *
 */
public enum ClosingDateAtr {
	
	/*
	 * ３６協定締め日区分
	 */
	// 0: 勤怠の締め日と同じ
	SAME_AS_CLOSING_DATE(0),
	// 1: 締め日を指定
	DESIGNATE_CLOSING_DATE(1);

	public final int value;
	
	private ClosingDateAtr(int type) {
		this.value = type;
	}

}

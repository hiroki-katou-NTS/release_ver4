package nts.uk.ctx.sys.shared.dom.user;

public enum DisabledSegment {

	True(1), // ăă
	False(0); // ăȘă

	public int value;

	private DisabledSegment(int type) {
		this.value = type;
	}
}

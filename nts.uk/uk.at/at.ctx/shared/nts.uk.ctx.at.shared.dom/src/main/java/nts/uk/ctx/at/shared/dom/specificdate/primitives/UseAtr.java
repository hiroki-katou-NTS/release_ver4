package nts.uk.ctx.at.shared.dom.specificdate.primitives;


public enum UseAtr {
	
	UNUSE(0),
	
	USE(1);

	public final int value;
	
	private UseAtr(int type) {
		this.value = type;
	}

}

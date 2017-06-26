package nts.uk.ctx.basic.dom.system.era;

public  enum FixAttribute {
	/** 0 - **/
	NOTEDITABLE(0),
	/** 1 - **/
	EDITABLE(1);
	
	public final int value;

	private FixAttribute(int value) {
		this.value = value;
	}
}

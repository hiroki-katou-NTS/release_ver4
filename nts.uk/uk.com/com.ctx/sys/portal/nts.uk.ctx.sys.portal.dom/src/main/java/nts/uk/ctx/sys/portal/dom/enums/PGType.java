package nts.uk.ctx.sys.portal.dom.enums;

/**
 *  0: My Page <br/>
 *  1: Title Menu <br/>
 *  2: My Page
 *  */
public enum PGType {
	/** Top Page */
	TopPage(0),
	/** Title Menu */
	TitleMenu(1),
	/** My Page */
	MyPage(2);
	
	private int value;
	
	PGType(int type) {
		this.value = type;
	}
	
	public int getValue(){
		return this.value;
	}
}

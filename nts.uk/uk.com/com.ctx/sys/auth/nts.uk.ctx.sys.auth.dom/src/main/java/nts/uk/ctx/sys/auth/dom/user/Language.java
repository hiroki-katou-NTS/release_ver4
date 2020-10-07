package nts.uk.ctx.sys.auth.dom.user;

public enum Language {
	
	/**
	 * 日本語
	 */
	JAPANESE(0),
	/**
	 * 英語
	 */
	ENGLISH(1),
	/**
	 * その他
	 */
	OTHER(2);
	
	public int value;

	private Language(int type) {
		this.value = type;
	}
}

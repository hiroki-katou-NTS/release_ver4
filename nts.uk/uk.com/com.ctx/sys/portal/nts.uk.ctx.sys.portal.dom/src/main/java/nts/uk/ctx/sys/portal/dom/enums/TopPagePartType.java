package nts.uk.ctx.sys.portal.dom.enums;

/**
 * The Enum TopPagePartType.
 */
public enum TopPagePartType {
	
	/** The Widget. */
	Widget(0),

	/** The Dash board. */
	DashBoard(1),
	
	/** The Folow menu. */
	FolowMenu(2);

	/** The value. */
	public int value;

	/** The Constant values. */
	private final static TopPagePartType[] values = TopPagePartType.values();

	/**
	 * Instantiates a new top page part type.
	 *
	 * @param value the value
	 */
	private TopPagePartType(int value) {
		this.value = value;
	}

	/**
	 * Value of.
	 *
	 * @param value the value
	 * @return the top page part type
	 */
	public static TopPagePartType valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (TopPagePartType val : TopPagePartType.values) {
			if (val.value == value) {
				return val;
			}
		}
		// Not found.
		return null;
	}
}

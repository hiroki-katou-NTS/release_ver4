package nts.uk.ctx.at.record.dom.divergence.time;

/**
 * The Enum DivergenceTimeUseSet.
 */
// 使用区分
public enum DivergenceTimeUseSet {

	/** The not use. */
	NOT_USE(0, "Enum_UseClassificationAtr_NOT_USE"),

	/** The use. */
	USE(1, "Enum_UseClassificationAtr_USE");

	/** The value. */
	public final int value;

	/** The name id. */
	public final String nameId;

	/** The Constant values. */
	private final static DivergenceTimeUseSet[] values = DivergenceTimeUseSet.values();

	/**
	 * Instantiates a new rounding.
	 *
	 * @param value
	 *            the value
	 * @param nameId
	 *            the name id
	 */
	private DivergenceTimeUseSet(int value, String nameId) {
		this.value = value;
		this.nameId = nameId;
	}

	/**
	 * Value of.
	 *
	 * @param value
	 *            the value
	 * @return the rounding
	 */
	public static DivergenceTimeUseSet valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (DivergenceTimeUseSet val : DivergenceTimeUseSet.values) {
			if (val.value == value) {
				return val;
			}
		}

		// Not found.
		return null;
	}
}

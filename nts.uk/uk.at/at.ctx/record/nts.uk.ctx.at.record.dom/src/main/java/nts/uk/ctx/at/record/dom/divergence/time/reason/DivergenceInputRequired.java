package nts.uk.ctx.at.record.dom.divergence.time.reason;

/**
 * The Enum DivergenceInputRequired.
 */
public enum DivergenceInputRequired {

	/** The Require. */
	REQUIRE(0, "Enum_UseClassificationAtr_REQUIRE"),

	/** The Not require. */
	NOT_REQUIRE(1, "Enum_UseClassificationAtr_NOT_REQUIRE");

	/** The value. */
	public final int value;

	/** The name id. */
	public final String nameId;

	/** The Constant values. */
	private final static DivergenceInputRequired[] values = DivergenceInputRequired.values();

	/**
	 * Instantiates a new divergence input required.
	 *
	 * @param value
	 *            the value
	 * @param nameId
	 *            the name id
	 */
	private DivergenceInputRequired(int value, String nameId) {
		this.value = value;
		this.nameId = nameId;
	}

	/**
	 * Value of.
	 *
	 * @param value
	 *            the value
	 * @return the divergence input required
	 */
	public static DivergenceInputRequired valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (DivergenceInputRequired val : DivergenceInputRequired.values) {
			if (val.value == value) {
				return val;
			}
		}

		// Not found.
		return null;
	}
}

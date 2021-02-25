package nts.uk.ctx.at.function.dom.attendancerecord.item;

/**
 * The Enum CalculateItemAttributes.
 */
//算出する項目の属性
public enum CalculateItemAttributes {

	/** The time. */
	// 時間
	TIME(16, "時間"),

	/** The times. */
	// 回数
	TIMES(17, "回数"),

	/** The money amount. */
	// 金額
	MONEY_AMOUNT(18, "金額"),
	
	// 日数
	DAILY_TIME(22, "日数");

	/** The value. */
	public final int value;

	/** The name id. */
	public final String name;

	/** The Constant values. */
	private final static CalculateItemAttributes[] values = CalculateItemAttributes.values();

	/**
	 * Instantiates a new rounding.
	 *
	 * @param value            the value
	 * @param name the name
	 */
	private CalculateItemAttributes(int value, String name) {
		this.value = value;
		this.name = name;
	}

	/**
	 * Value of.
	 *
	 * @param value
	 *            the value
	 * @return the rounding
	 */
	public static CalculateItemAttributes valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (CalculateItemAttributes val : CalculateItemAttributes.values) {
			if (val.value == value) {

				return val;
			}
		}

		// Not found.
		return null;
	}
}

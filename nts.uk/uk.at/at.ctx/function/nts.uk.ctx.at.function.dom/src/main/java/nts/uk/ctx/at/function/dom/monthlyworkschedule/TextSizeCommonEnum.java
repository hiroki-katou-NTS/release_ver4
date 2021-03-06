package nts.uk.ctx.at.function.dom.monthlyworkschedule;

import lombok.AllArgsConstructor;

/**
 * The Enum TextSizeCommonEnum.
 */
//@author ChienDM
//帳票共通の文字の大きさ
@AllArgsConstructor
public enum TextSizeCommonEnum {
	/** Big*/
	// 大
	BIG(1, "BIG"),
	/** During */
	// 中 
	DURING(2, "DURING"),
	/** Small. */
	// 小
	SMALL(3, "SMALL");
	
	/** The value. */
	public final int value;

	/** The name id. */
	public final String nameId;

	/** The Constant values. */
	private final static TextSizeCommonEnum[] values = TextSizeCommonEnum.values();

	/**
	 * Value of.
	 *
	 * @param value the value
	 * @return the prints the setting remarks column
	 */
	public static TextSizeCommonEnum valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (TextSizeCommonEnum val : TextSizeCommonEnum.values) {
			if (val.value == value) {
				return val;
			}
		}

		// Not found.
		return null;
	}
}

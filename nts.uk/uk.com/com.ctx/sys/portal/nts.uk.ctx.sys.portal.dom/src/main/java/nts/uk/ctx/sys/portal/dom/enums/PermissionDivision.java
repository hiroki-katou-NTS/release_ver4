package nts.uk.ctx.sys.portal.dom.enums;

public enum PermissionDivision {
	
	/** The Not allow. */
	NotAllow(0),

	/** The Allow. */
	Allow(1);

	/** The value. */
	public int value;

	/** The Constant values. */
	private final static PermissionDivision[] values = PermissionDivision.values();

	/**
	 * Instantiates a new external URL allow.
	 *
	 * @param value the value
	 */
	private PermissionDivision(int value) {
		this.value = value;
	}

	/**
	 * Value of.
	 *
	 * @param value the value
	 * @return the external URL allow
	 */
	public static PermissionDivision valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (PermissionDivision val : PermissionDivision.values) {
			if (val.value == value) {
				return val;
			}
		}
		// Not found.
		return null;
	}
}

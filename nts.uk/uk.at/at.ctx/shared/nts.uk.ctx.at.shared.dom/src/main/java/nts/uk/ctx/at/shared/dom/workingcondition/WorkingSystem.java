package nts.uk.ctx.at.shared.dom.workingcondition;

public enum WorkingSystem {
	REGULAR_WORK(0, "Enum_WorkingSystem_REGULAR_WORK"),

	FLEX_TIME_WORK(1, "Enum_WorkingSystem_FLEX_TIME_WORK"),

	VARIABLE_WORKING_TIME_WORK(2, "Enum_WorkingSystem_VARIABLE_WORKING_TIME_WORK"),
	
	EXCLUDED_WORKING_CALCULATE(3, "Enum_WorkingSystem_EXCLUDED_WORKING_CALCULATE");

	/** The value. */
	public final int value;

	/** The name id. */
	public final String nameId;

	/** The Constant values. */
	private final static WorkingSystem[] values = WorkingSystem.values();

	/**
	 * Instantiates a new rounding.
	 *
	 * @param value
	 *            the value
	 * @param nameId
	 *            the name id
	 */
	private WorkingSystem(int value, String nameId) {
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
	public static WorkingSystem valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (WorkingSystem val : WorkingSystem.values) {
			if (val.value == value) {
				return val;
			}
		}

		// Not found.
		return null;
	}
}

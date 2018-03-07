package nts.uk.ctx.exio.app.command.exi.csvimport;

public enum ExecutionState {

	/** The done. */
	// 完了
	DONE(0, "Enum_ExecutionState_done", "出力完了"),

	/** The processing. */
	// 処理中
	PROCESSING(1, "Enum_ExecutionState_processing", "処理中");

	/** The value. */
	public final int value;

	/** The name id. */
	public final String nameId;

	/** The description. */
	public final String description;

	/** The Constant values. */
	private final static ExecutionState[] values = ExecutionState.values();

	/**
	 * Instantiates a new execution state.
	 *
	 * @param value the value
	 * @param nameId the name id
	 * @param description the description
	 */
	private ExecutionState(int value, String nameId, String description) {
		this.value = value;
		this.nameId = nameId;
		this.description = description;
	}

	/**
	 * Value of.
	 *
	 * @param value the value
	 * @return the execution state
	 */
	public static ExecutionState valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (ExecutionState val : ExecutionState.values) {
			if (val.value == value) {
				return val;
			}
		}

		// Not found.
		return null;
	}

}
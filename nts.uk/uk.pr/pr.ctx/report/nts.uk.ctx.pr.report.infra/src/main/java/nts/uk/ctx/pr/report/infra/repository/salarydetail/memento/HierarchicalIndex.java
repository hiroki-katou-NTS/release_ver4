/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.infra.repository.salarydetail.memento;

/**
 * The Enum HierarchicalIndex.
 */
public enum HierarchicalIndex {

	/** The no setting. */
	NO_SETTING(0),

	/** The hierarchical index 1. */
	HIERARCHICAL_INDEX_1(1),

	/** The hierarchical index 2. */
	HIERARCHICAL_INDEX_2(2),

	/** The hierarchical index 3. */
	HIERARCHICAL_INDEX_3(3),

	/** The hierarchical index 4. */
	HIERARCHICAL_INDEX_4(4),

	/** The hierarchical index 5. */
	HIERARCHICAL_INDEX_5(5),

	/** The hierarchical index 6. */
	HIERARCHICAL_INDEX_6(6),

	/** The hierarchical index 7. */
	HIERARCHICAL_INDEX_7(7),

	/** The hierarchical index 8. */
	HIERARCHICAL_INDEX_8(8),

	/** The hierarchical index 9. */
	HIERARCHICAL_INDEX_9(9);

	/** The value. */
	public final Integer value;

	/**
	 * Instantiates a new hierarchical index.
	 *
	 * @param value
	 *            the value
	 */
	private HierarchicalIndex(Integer value) {
		this.value = value;
	}

	/**
	 * Value of.
	 *
	 * @param value
	 *            the value
	 * @return the hierarchical index
	 */
	public static HierarchicalIndex valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (HierarchicalIndex val : HierarchicalIndex.values()) {
			if (val.value == value) {
				return val;
			}
		}

		// Not found.
		return null;
	}
}

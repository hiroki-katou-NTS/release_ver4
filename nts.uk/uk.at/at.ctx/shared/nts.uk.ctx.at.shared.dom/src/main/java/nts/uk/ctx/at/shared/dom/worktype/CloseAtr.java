package nts.uk.ctx.at.shared.dom.worktype;

import lombok.AllArgsConstructor;
/**
 * The Enum Colse Attribute
 * 
 * @author sonnh
 *
 */
@AllArgsConstructor
public enum CloseAtr {
	
	/**
	 * 産前休業
	 */
	PRENATAL(0, "Enum_CloseAtr_PRENATAL"),
	
	/**
	 * 産後休業
	 */
	POSTPARTUM(1, "Enum_CloseAtr_POSTPARTUM"),
	
	/**
	 * 育児休業
	 */
	CHILD_CARE(2, "Enum_CloseAtr_CHILD_CARE"),
	
	/**
	 * 介護休業
	 */
	CARE(3, "Enum_CloseAtr_CARE"),
	
	/**
	 * 傷病休業
	 */
	INJURY_OR_ILLNESS(4, "Enum_CloseAtr_INJURY_OR_ILLNESS"),
	
	/**
	 * 任意休業1
	 */
	OPTIONAL_LEAVE_1(5, "Enum_CloseAtr_OPTIONAL_LEAVE_1"),
	
	/**
	 * 任意休業2
	 */
	OPTIONAL_LEAVE_2(6, "Enum_CloseAtr_OPTIONAL_LEAVE_2"),
	
	/**
	 * 任意休業3
	 */
	OPTIONAL_LEAVE_3(7, "Enum_CloseAtr_OPTIONAL_LEAVE_3"),
	
	/**
	 * 任意休業4
	 */
	OPTIONAL_LEAVE_4(8, "Enum_CloseAtr_OPTIONAL_LEAVE_4");
	
    public final int value;
    public final String nameId;
	/** The Constant values. */
	private final static CloseAtr[] values = CloseAtr.values();
	
	/**
	 * Value of.
	 *
	 * @param value the value
	 * @return the workAtr
	 */
	public static CloseAtr valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (CloseAtr val : CloseAtr.values) {
			if (val.value == value) {
				return val;
			}
		}

		// Not found.
		return null;
	}
}

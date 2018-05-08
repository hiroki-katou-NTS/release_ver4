package nts.uk.ctx.sys.assist.dom.system;

import nts.uk.shr.com.i18n.TextResource;

public enum SystemTypeEnum {
	POSSIBILITY_SYSTEM(0, TextResource.localize("CMF005_170")),
	ATTENDANCE_SYSTEM(1, TextResource.localize("CMF005_171")),
	PAYROLL_SYSTEM(2, TextResource.localize("CMF005_172")),
	OFFICE_HELPER(3, TextResource.localize("CMF005_173"));
	
	/** The value. */
	public final int value;

	/** The name id. */
	public final String name;

	private SystemTypeEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}
	
	/** The Constant values. */
	private final static SystemTypeEnum[] values = SystemTypeEnum.values();
	
	/**
	 * Value of.
	 *
	 * @param value the value
	 * @return the role type
	 */
	public static SystemTypeEnum valueOf(int value) {
		
		// Find value.
		for (SystemTypeEnum val : SystemTypeEnum.values) {
			if (val.value == value) {
				return val;
			}
		}

		// Not found.
		return null;
	}
}

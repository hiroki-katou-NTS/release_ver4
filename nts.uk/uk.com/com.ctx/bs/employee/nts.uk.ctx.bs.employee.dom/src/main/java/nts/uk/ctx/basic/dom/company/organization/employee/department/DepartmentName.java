/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.basic.dom.company.organization.employee.department;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * The Class DepartmentName. 部門名称
 */
@StringMaxLength(20)
public class DepartmentName extends StringPrimitiveValue<DepartmentName>{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new department name.
	 *
	 * @param rawValue the raw value
	 */
	public DepartmentName(String rawValue) {
		super(rawValue);
	}

}

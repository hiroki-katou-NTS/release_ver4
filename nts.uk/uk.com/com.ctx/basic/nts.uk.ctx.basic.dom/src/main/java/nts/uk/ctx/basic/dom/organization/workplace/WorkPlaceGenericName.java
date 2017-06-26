package nts.uk.ctx.basic.dom.organization.workplace;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * 
 * 職場総称
 */
@StringMaxLength(40)
public class WorkPlaceGenericName extends StringPrimitiveValue<WorkPlaceGenericName>{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public WorkPlaceGenericName(String rawValue) {
		super(rawValue);
	}

}

package nts.uk.ctx.basic.dom.organization.workplace;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * 
 * 職場名称
 */
@StringMaxLength(20)
public class WorkPlaceName extends StringPrimitiveValue<WorkPlaceName>{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public WorkPlaceName(String rawValue) {
		super(rawValue);
	}

}

package nts.uk.ctx.basic.dom.organization.position;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

@StringMaxLength(10)
public class HiterarchyOrderCode extends StringPrimitiveValue<HiterarchyOrderCode> {
	/*
	 * HIERARCHY_ORDER_CD
	 */
	public HiterarchyOrderCode(String rawValue) {
		super(rawValue);
		
	}

	private static final long serialVersionUID = 1L;

	public static boolean isExisted(String companyCode, JobCode jobCode) {
		return false;
	} 
}



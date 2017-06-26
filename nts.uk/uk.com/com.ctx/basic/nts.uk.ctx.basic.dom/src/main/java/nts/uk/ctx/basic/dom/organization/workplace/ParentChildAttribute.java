package nts.uk.ctx.basic.dom.organization.workplace;

import java.math.BigDecimal;

import nts.arc.primitive.DecimalPrimitiveValue;
import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.DecimalMantissaMaxLength;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * 
 * 親子属性
 *
 */
@DecimalMantissaMaxLength(1)
public class ParentChildAttribute extends DecimalPrimitiveValue<ParentChildAttribute>{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public ParentChildAttribute(BigDecimal rawValue) {
		super(rawValue);
	}
}

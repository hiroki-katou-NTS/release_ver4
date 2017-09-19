package nts.uk.ctx.at.shared.dom.specialholiday.yearservice.yearserviceper.primitives;

import nts.arc.primitive.PrimitiveValue;
import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;
@StringCharType(CharType.ALPHA_NUMERIC)
@StringMaxLength(2)
/**
 * 
 * @author yennth
 *
 */
public class YearServiceCode extends StringPrimitiveValue<PrimitiveValue<String>>{
	private static final long serialVersionUID = 1L;
	public YearServiceCode (String rawValue){
		super(rawValue);
	}
}

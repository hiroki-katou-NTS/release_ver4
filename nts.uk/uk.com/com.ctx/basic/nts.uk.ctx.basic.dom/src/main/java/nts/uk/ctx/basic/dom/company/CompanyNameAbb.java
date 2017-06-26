package nts.uk.ctx.basic.dom.company;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;
/**
 * @author lanlt
 *
 */
@StringMaxLength(20)
/*@StringCharType(CharType.ALPHA_NUMERIC)*/
public class CompanyNameAbb  extends StringPrimitiveValue<CompanyNameAbb>{
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	/**
	 * contructors
	 * @param rawValue
	 */
	public CompanyNameAbb(String rawValue) {
		super(rawValue);
	}
	

}

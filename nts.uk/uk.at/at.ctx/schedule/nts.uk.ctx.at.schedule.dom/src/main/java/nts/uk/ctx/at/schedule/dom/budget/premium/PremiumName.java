package nts.uk.ctx.at.schedule.dom.budget.premium;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@StringMaxLength(30)
public class PremiumName extends StringPrimitiveValue<PremiumName>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5794442674634451699L;

	public PremiumName(String rawValue) {
		super(rawValue);
	}

}

package nts.uk.ctx.pr.core.dom.rule.law.tax.residential;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;
@StringMaxLength(2)
public class PrefectureCode extends StringPrimitiveValue<PrefectureCode> {

	public PrefectureCode(String rawValue) {
		super(rawValue);
	}
	private static final long serialVersionUID = 1L;


}

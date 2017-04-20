package nts.uk.ctx.pr.report.dom.payment.comparing.settingoutputitem;

import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;
import nts.uk.shr.com.primitive.CodePrimitiveValue;

@StringMaxLength(2)
@StringCharType(CharType.ALPHA_NUMERIC)
public class FormCode extends CodePrimitiveValue<FormCode> {

	private static final long serialVersionUID = 1L;

	public FormCode(String rawValue) {
		super(rawValue);
	}

}

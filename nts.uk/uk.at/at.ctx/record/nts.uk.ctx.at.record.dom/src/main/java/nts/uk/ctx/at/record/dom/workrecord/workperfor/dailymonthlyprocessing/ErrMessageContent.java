package nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

@StringMaxLength(500)
public class ErrMessageContent extends StringPrimitiveValue<ErrMessageContent> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ErrMessageContent(String rawValue) {
		super(rawValue);
	}

}

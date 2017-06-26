package nts.uk.ctx.pr.core.dom.paymentdata;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * メモ
 */
@StringMaxLength(1000)
public class Comment extends StringPrimitiveValue<Comment> {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs.
	 * 
	 * @param rawValue
	 *            raw value
	 */
	public Comment(String rawValue) {
		super(rawValue);
	}

}

package nts.uk.ctx.sys.portal.dom.primitive;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

@StringMaxLength(30)
public class TopPageName extends StringPrimitiveValue<TopPageName> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new top page name.
	 *
	 * @param rawValue the raw value
	 */
	public TopPageName(String rawValue) {
		super(rawValue);
	}
}

package nts.uk.ctx.pr.core.dom.itemmaster;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringRegEx;


@StringRegEx("/^[a-zA-Z]{6,12}$/")
public class ItemAbName extends StringPrimitiveValue<ItemAbName> {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs.
	 * 
	 * @param rawValue raw value
	 */
	public ItemAbName(String rawValue) {
		super(rawValue);		
	}

}

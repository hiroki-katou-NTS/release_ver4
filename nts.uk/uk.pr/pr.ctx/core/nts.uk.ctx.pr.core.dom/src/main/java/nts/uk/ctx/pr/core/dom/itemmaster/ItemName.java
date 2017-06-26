package nts.uk.ctx.pr.core.dom.itemmaster;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

/** 項目名称 */
@StringMaxLength(20)
public class ItemName extends StringPrimitiveValue<ItemName> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs.
	 * 
	 * @param rawValue raw value
	 */
	public ItemName(String rawValue) {
		super(rawValue);		
	}

}

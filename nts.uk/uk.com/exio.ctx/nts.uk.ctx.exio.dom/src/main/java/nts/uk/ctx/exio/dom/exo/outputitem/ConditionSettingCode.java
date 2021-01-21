package nts.uk.ctx.exio.dom.exo.outputitem;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * @author son.tc
 *
 */
@StringMaxLength(5)
public class ConditionSettingCode extends StringPrimitiveValue<ConditionSettingCode> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConditionSettingCode(String rawValue) {
		super(rawValue);
	}
}

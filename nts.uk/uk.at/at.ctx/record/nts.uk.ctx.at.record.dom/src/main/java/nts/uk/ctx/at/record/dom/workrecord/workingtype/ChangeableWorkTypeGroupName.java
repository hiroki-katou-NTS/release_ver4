/**
 * 
 */
package nts.uk.ctx.at.record.dom.workrecord.workingtype;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * @author danpv
 *
 */
@StringMaxLength(20)
public class ChangeableWorkTypeGroupName extends StringPrimitiveValue<ChangeableWorkTypeGroupName>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param rawValue
	 */
	public ChangeableWorkTypeGroupName(String rawValue) {
		super(rawValue);
		// TODO Auto-generated constructor stub
	}

	
}

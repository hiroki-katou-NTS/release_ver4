package nts.uk.ctx.basic.dom.company;

import lombok.AllArgsConstructor;
import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;
/**
 * 
 * @author lanlt
 *
 */
@AllArgsConstructor
@StringCharType(CharType.NUMERIC)
@StringMaxLength(1)
public enum DepWorkPlaceSet {
	/** 0:区別しない	 */
	DISTINCTION_NOT_USE(0),
	/** 1:区別する	 */
	DISTINCTION_USE(1);
	/**
	 * value
	 */
	 public final int value;  
}

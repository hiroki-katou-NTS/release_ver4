package nts.uk.ctx.basic.dom.company;

import lombok.AllArgsConstructor;
import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;
@AllArgsConstructor
@StringCharType(CharType.NUMERIC)
@StringMaxLength(1)
public enum DisplayAttribute {
	//0:区別しない
	DO_NOT_DISTINGUISH(0),
	//1:区別する"
	DO_DISTINGUISH(1);

	public final int value;
}

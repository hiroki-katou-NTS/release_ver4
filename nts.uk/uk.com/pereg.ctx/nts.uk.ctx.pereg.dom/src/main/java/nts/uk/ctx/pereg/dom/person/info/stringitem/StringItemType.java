package nts.uk.ctx.pereg.dom.person.info.stringitem;

import lombok.AllArgsConstructor;
import nts.arc.primitive.constraint.IntegerRange;

@AllArgsConstructor
@IntegerRange(max = 5, min = 1)
public enum StringItemType {
	// 1:すべての文字(any)
	ANY(1),

	// 2:全ての半角文字(AnyHalfWidth)
	ANYHALFWIDTH(2),

	// 3:半角英数字(AlphaNumeric)
	ALPHANUMERIC(3),

	// 4:半角数字(Numeric)
	NUMERIC(4),

	// 5:全角カタカナ(Kana)
	KANA(5);

	public final int value;
}

package nts.uk.ctx.at.function.dom.holidaysremaining;

import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.StringCharType;
import nts.arc.primitive.constraint.StringMaxLength;
import nts.uk.shr.com.primitive.CodePrimitiveValue;
import nts.uk.shr.com.primitive.ZeroPaddedCode;

/**
 * 更新処理自動実行項目コード
 */
@StringCharType(CharType.NUMERIC)
@StringMaxLength(2)
@ZeroPaddedCode
public class ExecutionCode extends CodePrimitiveValue<ExecutionCode> {
	
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	public ExecutionCode(String rawValue) {
		super(rawValue);
	}
}

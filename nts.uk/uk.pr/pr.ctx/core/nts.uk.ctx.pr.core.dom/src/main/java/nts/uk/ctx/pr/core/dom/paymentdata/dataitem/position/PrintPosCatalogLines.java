package nts.uk.ctx.pr.core.dom.paymentdata.dataitem.position;

import nts.arc.primitive.IntegerPrimitiveValue;
import nts.arc.primitive.constraint.CharType;
import nts.arc.primitive.constraint.IntegerRange;
import nts.arc.primitive.constraint.StringCharType;

/**
 * 番目印字カテゴリ行数
 */
@StringCharType(CharType.NUMERIC)
@IntegerRange(min = 0, max = 99)
public class PrintPosCatalogLines extends IntegerPrimitiveValue<PrintPosCatalogLines> {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs.
	 * 
	 * @param rawValue
	 *            raw value
	 */
	public PrintPosCatalogLines(Integer rawValue) {
		super(rawValue);
	}

}

package nts.uk.ctx.at.record.dom.daily.remarks;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

/** 実績の備考 */
@StringMaxLength(100)
public class RecordRemarks extends StringPrimitiveValue<RecordRemarks> {

	/***/
	private static final long serialVersionUID = 1L;

	public RecordRemarks(String rawValue) {
		super(rawValue);
	}

}

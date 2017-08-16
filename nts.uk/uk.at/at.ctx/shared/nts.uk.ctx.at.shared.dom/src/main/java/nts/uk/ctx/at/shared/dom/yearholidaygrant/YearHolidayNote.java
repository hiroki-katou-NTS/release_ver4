package nts.uk.ctx.at.shared.dom.yearholidaygrant;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

@StringMaxLength(100)
public class YearHolidayNote extends StringPrimitiveValue<YearHolidayNote> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public YearHolidayNote(String rawValue) {
		super(rawValue);
		// TODO Auto-generated constructor stub
	}

}

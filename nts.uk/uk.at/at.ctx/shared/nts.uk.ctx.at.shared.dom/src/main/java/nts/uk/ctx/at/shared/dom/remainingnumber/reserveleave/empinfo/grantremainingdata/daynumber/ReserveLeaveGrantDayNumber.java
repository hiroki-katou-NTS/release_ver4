package nts.uk.ctx.at.shared.dom.remainingnumber.reserveleave.empinfo.grantremainingdata.daynumber;

import lombok.Getter;
import nts.arc.primitive.HalfIntegerPrimitiveValue;
import nts.arc.primitive.constraint.HalfIntegerRange;

@Getter
@HalfIntegerRange(min = 0, max = 999.5)
public class ReserveLeaveGrantDayNumber extends HalfIntegerPrimitiveValue<ReserveLeaveGrantDayNumber> {

	private static final long serialVersionUID = -8685412044462885135L;

	public ReserveLeaveGrantDayNumber(Double rawValue) {
		super(rawValue);
	}

	@Override
	protected Double reviseRawValue(Double rawValue) {
		if (rawValue == null) return super.reviseRawValue(rawValue);
		if (rawValue > 999.5) rawValue = 999.5;
		if (rawValue < 0.0) rawValue = 0.0;
		return super.reviseRawValue(rawValue);
	}
}

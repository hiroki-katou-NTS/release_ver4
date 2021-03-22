package nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.maxdata;

import java.io.Serializable;

import nts.arc.primitive.TimeDurationPrimitiveValue;
import nts.arc.primitive.constraint.TimeRange;

@TimeRange(min ="-999:59", max="999:59")
public class RemainingMinutes extends TimeDurationPrimitiveValue<RemainingMinutes> implements Serializable{

	private static final long serialVersionUID = -8135525580841453174L;

	public RemainingMinutes(int timeAsMinutes) {
		super(timeAsMinutes);
	}

	@Override
	protected Integer reviseRawValue(Integer rawValue) {
		if (rawValue == null) return super.reviseRawValue(0);
		if (rawValue > 999 * 60 + 59) rawValue = 999 * 60 + 59;
		if (rawValue < -(999 * 60 + 59)) rawValue = -(999 * 60 + 59);
		return super.reviseRawValue(rawValue);
	}
}

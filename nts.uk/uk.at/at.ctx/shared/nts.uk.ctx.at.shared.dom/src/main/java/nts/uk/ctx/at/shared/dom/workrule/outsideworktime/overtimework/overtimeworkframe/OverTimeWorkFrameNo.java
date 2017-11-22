package nts.uk.ctx.at.shared.dom.workrule.outsideworktime.overtimework.overtimeworkframe;

import java.math.BigDecimal;

import nts.arc.primitive.DecimalPrimitiveValue;
import nts.arc.primitive.constraint.DecimalMaxValue;
import nts.arc.primitive.constraint.DecimalMinValue;
/**
 * 残業枠No
 * @author keisuke_hoshina
 *
 */

@DecimalMaxValue("5")
@DecimalMinValue("1")
public class OverTimeWorkFrameNo extends DecimalPrimitiveValue<OverTimeWorkFrameNo>{
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 *
	 * @param rawValue
	 *            the raw value
	 */
	public OverTimeWorkFrameNo(BigDecimal rawValue) {
		super(rawValue);
	}
}

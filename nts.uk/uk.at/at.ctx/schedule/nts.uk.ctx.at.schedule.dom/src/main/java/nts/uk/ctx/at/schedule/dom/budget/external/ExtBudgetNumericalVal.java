package nts.uk.ctx.at.schedule.dom.budget.external;

import nts.arc.primitive.IntegerPrimitiveValue;
import nts.arc.primitive.constraint.IntegerRange;

/**
 * The Class ExtBudgetNumericalVal.
 * 外部予算実績数値
 */
@IntegerRange(min = 0, max = 99999)
public class ExtBudgetNumericalVal extends IntegerPrimitiveValue<ExtBudgetNumericalVal> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new ext budget numerical val.
     *
     * @param rawValue the raw value
     */
    public ExtBudgetNumericalVal(Integer rawValue) {
        super(rawValue);
    }

}
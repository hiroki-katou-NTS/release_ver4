package nts.uk.ctx.at.schedule.dom.budget.external.actualresults;

import nts.arc.primitive.IntegerPrimitiveValue;
import nts.arc.primitive.constraint.IntegerRange;

/**
 * The Class ExtBudgetUnitPrice.
 * 外部予算実績単価
 */
@IntegerRange(min = 0, max = 9999999)
public class ExtBudgetUnitPrice extends IntegerPrimitiveValue<ExtBudgetUnitPrice> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new ext budget unit price.
     *
     * @param rawValue the raw value
     */
    public ExtBudgetUnitPrice(Integer rawValue) {
        super(rawValue);
    }

}
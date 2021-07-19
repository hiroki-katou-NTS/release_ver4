package nts.uk.ctx.at.function.dom.outputitemsofworkstatustable;


import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

/**
 * 勤務状況表の出力項目名称
 */
@StringMaxLength(12)
public class FormOutputItemName extends StringPrimitiveValue<FormOutputItemName> {

    /**
     * Instantiates a new output item setting code.
     *
     * @param rawValue the raw value
     */
    public FormOutputItemName(String rawValue) {
        super(rawValue);
    }

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 957215815666122623L;
}
package nts.uk.ctx.pr.core.dom.wageprovision.speclayout;

/**
 * 通勤区分
 */
public enum WorkingAtr {
    TRANSPORT_FACILITIES(0),
    TRANSPORT_EQUIPMENT(1);

    /** The value. */
    public final int value;

    private WorkingAtr(int value)
    {
        this.value = value;
    }
}

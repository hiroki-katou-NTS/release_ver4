package nts.uk.ctx.at.function.dom.alarm.checkcondition;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TargetSelectionRange {
    /* 選択 */
    SELECTION(0, "Enum_TargetSelectionRange_Selection"),
    /* 選択以外*/
    OTHER_SELECTION(1, "Enum_TargetSelectionRange_OtherSelection");
    
    public final int value;

    public final String nameId;
}

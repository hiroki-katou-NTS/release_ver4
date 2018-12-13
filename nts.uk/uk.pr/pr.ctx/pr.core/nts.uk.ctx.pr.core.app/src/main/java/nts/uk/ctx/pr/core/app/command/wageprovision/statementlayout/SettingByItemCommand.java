package nts.uk.ctx.pr.core.app.command.wageprovision.statementlayout;

import lombok.AllArgsConstructor;
import lombok.Value;
import nts.uk.ctx.pr.core.dom.wageprovision.statementlayout.SettingByItemCustom;

@AllArgsConstructor
@Value
public class SettingByItemCommand {
    private int itemPosition;
    private String itemId;
    private String shortName;
    private PaymentItemDetailCommand paymentItemDetailSet;
    private DeductionItemDetailCommand deductionItemDetailSet;
    private ItemRangeSetCommand itemRangeSet;

    public SettingByItemCustom toDomain() {
        return new SettingByItemCustom(itemPosition, itemId, shortName,
                deductionItemDetailSet == null ? null : deductionItemDetailSet.toDomain(),
                paymentItemDetailSet == null ? null : paymentItemDetailSet.toDomain(),
                itemRangeSet == null ? null : itemRangeSet.toDomain());
    }
}

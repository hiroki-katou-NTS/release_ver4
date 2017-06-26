package nts.uk.ctx.pr.core.app.command.rule.employment.unitprice.personal;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UpdatePersonalUnitPriceCommand {
	private String personalUnitPriceCode;
    private String personalUnitPriceName;
    private String personalUnitPriceShortName;
    public int displaySet;
    public String uniteCode;
    public int paymentSettingType;
    public int fixPaymentAtr;
    public int fixPaymentMonthly;
    public int fixPaymentDayMonth;
    public int fixPaymentDaily;
    public int fixPaymentHoursly;
    public int unitPriceAtr;
    public String memo;
}

package nts.uk.ctx.pr.core.app.find.rule.employment.unitprice.personal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PersonalUnitPriceDto {
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

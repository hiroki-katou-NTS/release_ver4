package nts.uk.ctx.at.schedule.app.command.budget.premium.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.time.GeneralDate;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PersonCostCalculationDto {
    private GeneralDate startDate;
    private GeneralDate endDate;
    private String historyID;
    private String companyID;
    private Integer unitPrice;

    private int howToSetUnitPrice;

    private Integer workingHoursUnitPrice;

    private  String remarks;

    private PerCostRoundSettingDto personCostRoundingSetting;

    private List<PremiumSettingDto> premiumSettingList;
}

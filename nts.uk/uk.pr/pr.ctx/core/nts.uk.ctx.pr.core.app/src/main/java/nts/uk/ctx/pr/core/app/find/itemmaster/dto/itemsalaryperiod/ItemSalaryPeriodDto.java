package nts.uk.ctx.pr.core.app.find.itemmaster.dto.itemsalaryperiod;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.pr.core.dom.itemmaster.itemsalaryperiod.ItemSalaryPeriod;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ItemSalaryPeriodDto {
	private String itemCode;
	private int periodAtr;
	private int strY;
	private int endY;
	private int cycleAtr;
	private int cycle01Atr;
	private int cycle02Atr;
	private int cycle03Atr;
	private int cycle04Atr;
	private int cycle05Atr;
	private int cycle06Atr;
	private int cycle07Atr;
	private int cycle08Atr;
	private int cycle09Atr;
	private int cycle10Atr;
	private int cycle11Atr;
	private int cycle12Atr;

	public static ItemSalaryPeriodDto fromDomain(ItemSalaryPeriod domain) {
		return new ItemSalaryPeriodDto(domain.getItemCode().v(), domain.getPeriodAtr().value, domain.getStrY().v(),
				domain.getEndY().v(), domain.getCycleAtr().value, domain.getCycle01Atr().value,
				domain.getCycle02Atr().value, domain.getCycle03Atr().value, domain.getCycle04Atr().value,
				domain.getCycle05Atr().value, domain.getCycle06Atr().value, domain.getCycle07Atr().value,
				domain.getCycle08Atr().value, domain.getCycle09Atr().value, domain.getCycle10Atr().value,
				domain.getCycle11Atr().value, domain.getCycle12Atr().value);

	}

}

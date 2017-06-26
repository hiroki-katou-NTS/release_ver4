package nts.uk.ctx.pr.core.app.find.itemmaster.dto.itemperiod;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.pr.core.dom.itemmaster.itemperiod.ItemPeriod;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ItemPeriodDto {

	private int itemClass;
	private String itemCode;
	private int StartYear;
	private int ExpYear;
	private int cycleAtr;
	private int cycle1Atr;
	private int cycle2Atr;
	private int cycle3Atr;
	private int cycle4Atr;
	private int cycle5Atr;
	private int cycle6Atr;
	private int cycle7Atr;
	private int cycle8Atr;
	private int cycle9Atr;
	private int cycle10Atr;
	private int cycle11Atr;
	private int cycle12Atr;

	public static ItemPeriodDto fromDomain(ItemPeriod domain) {
		// TODO Auto-generated method stub
		return new ItemPeriodDto(domain.getItemclass().v(), domain.getItemCode().v(), domain.getStartYear().v(),
				domain.getEndYear().v(), domain.getCycleUsageClassification().value,
				domain.getJanuaryUsageClassification().value, domain.getFebruaryUsageClassification().value,
				domain.getMarchUsageClassification().value, domain.getAprilUsageClassification().value,
				domain.getMayUsageClassification().value, domain.getJuneUsageClassification().value,
				domain.getJulyUsageClassification().value, domain.getAugustUsageClassification().value,
				domain.getSeptemberUsageClassification().value, domain.getOctoberUsageClassification().value,
				domain.getNovemberUsageClassification().value, domain.getDecemberUsageClassification().value);
	}

}

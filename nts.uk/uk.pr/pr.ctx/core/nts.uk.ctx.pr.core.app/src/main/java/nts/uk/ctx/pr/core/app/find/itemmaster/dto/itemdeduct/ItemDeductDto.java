package nts.uk.ctx.pr.core.app.find.itemmaster.dto.itemdeduct;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.pr.core.dom.itemmaster.itemdeduct.ItemDeduct;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ItemDeductDto {
	private int deductAtr;
	private int errRangeLowAtr;
	private BigDecimal errRangeLow;
	private int errRangeHighAtr;
	private BigDecimal errRangeHigh;
	private int alRangeLowAtr;
	private BigDecimal alRangeLow;
	private int alRangeHighAtr;
	private BigDecimal alRangeHigh;
	private String memo;

	public static ItemDeductDto fromDomain(ItemDeduct domain) {
		return new ItemDeductDto(domain.getDeductAtr().value, domain.getErrRangeLowAtr().value,
				domain.getErrRangeLow().v(), domain.getErrRangeHighAtr().value, domain.getErrRangeHigh().v(),
				domain.getAlRangeLowAtr().value, domain.getAlRangeLow().v(), domain.getAlRangeHighAtr().value,
				domain.getAlRangeHigh().v(), domain.getMemo().v());
	}
}

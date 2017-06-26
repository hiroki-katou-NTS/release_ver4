package nts.uk.ctx.pr.core.app.find.itemmaster.dto.itemattend;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.pr.core.dom.itemmaster.itemattend.ItemAttend;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ItemAttendDto {
	private int avePayAtr;
	private int itemAtr;
	private int errRangeLowAtr;
	private BigDecimal errRangeLow;
	private int errRangeHighAtr;
	private BigDecimal errRangeHigh;
	private int alRangeLowAtr;
	private BigDecimal alRangeLow;
	private int alRangeHighAtr;
	private BigDecimal alRangeHigh;
	private int workDaysScopeAtr;
	private String memo;

	public static ItemAttendDto fromDomain(ItemAttend domain) {
		
		return new ItemAttendDto(domain.getAvePayAtr().value, domain.getItemAtr().value,
				domain.getErrRangeLowAtr().value, domain.getErrRangeLow().v(), domain.getAlRangeHighAtr().value,
				domain.getErrRangeHigh().v(), domain.getAlRangeLowAtr().value, domain.getAlRangeLow().v(),
				domain.getAlRangeHighAtr().value, domain.getAlRangeHigh().v(), domain.getWorkDaysScopeAtr().value,
				domain.getMemo().v());
	}

}

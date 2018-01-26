package nts.uk.ctx.at.function.dom.adapter.eralworkrecorddto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ErAlAtdItemConAdapterDto {
	private int targetNO;
	private int conditionAtr;
	private boolean useAtr;
	private int uncountableAtdItem;
	private List<Integer> countableAddAtdItems;
	private List<Integer> countableSubAtdItems;
	private int conditionType;
	private int compareOperator;
	private int singleAtdItem;
	private BigDecimal compareStartValue;
	private BigDecimal compareEndValue;
	public ErAlAtdItemConAdapterDto(int targetNO, int conditionAtr, boolean useAtr, int uncountableAtdItem,
			List<Integer> countableAddAtdItems, List<Integer> countableSubAtdItems, int conditionType,
			int compareOperator, int singleAtdItem, BigDecimal compareStartValue, BigDecimal compareEndValue) {
		super();
		this.targetNO = targetNO;
		this.conditionAtr = conditionAtr;
		this.useAtr = useAtr;
		this.uncountableAtdItem = uncountableAtdItem;
		this.countableAddAtdItems = countableAddAtdItems;
		this.countableSubAtdItems = countableSubAtdItems;
		this.conditionType = conditionType;
		this.compareOperator = compareOperator;
		this.singleAtdItem = singleAtdItem;
		this.compareStartValue = compareStartValue;
		this.compareEndValue = compareEndValue;
	}
}

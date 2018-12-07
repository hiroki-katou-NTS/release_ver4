package nts.uk.ctx.exio.dom.exo.exoutsummaryservice;

import lombok.Getter;
import nts.uk.ctx.exio.dom.exo.categoryitemdata.DataType;

@Getter
public class CtgItemDataCustom {
	int seriNum;
	String itemName;
	String conditions;
	int dataType;
	
	public CtgItemDataCustom(int seriNum, String itemName, String conditions, DataType dataType) {
		this.seriNum = seriNum;
		this.itemName = itemName;
		this.conditions = conditions;
		this.dataType = dataType.value;
	}
}

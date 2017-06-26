package nts.uk.ctx.pr.formula.dom.formula;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.pr.formula.dom.primitive.ItemCode;

@Getter
public class SimpleCalSetting extends DomainObject {
	private ItemCode itemCode;

	private String itemName;

	public SimpleCalSetting(ItemCode itemCode, String itemName) {
		super();
		this.itemCode = itemCode;
		this.itemName = itemName;
	}

	public static SimpleCalSetting createFromJavaType(String itemCode, String itemName) {
		return new SimpleCalSetting(new ItemCode(itemCode), itemName);
	}
}

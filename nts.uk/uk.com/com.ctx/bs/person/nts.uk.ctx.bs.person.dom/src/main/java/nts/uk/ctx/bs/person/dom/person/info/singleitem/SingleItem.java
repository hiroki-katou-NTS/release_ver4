package nts.uk.ctx.bs.person.dom.person.info.singleitem;

import lombok.Getter;
import nts.uk.ctx.bs.person.dom.person.info.item.ItemType;
import nts.uk.ctx.bs.person.dom.person.info.item.ItemTypeState;

@Getter
public class SingleItem extends ItemTypeState {

	protected DataTypeState dataTypeState;

	private SingleItem(DataTypeState dataTypeState) {
		super();
		this.itemType = ItemType.SINGLE_ITEM;
	}

	public static SingleItem createFromJavaType(DataTypeState dataTypeState) {
		return new SingleItem(dataTypeState);
	}
}

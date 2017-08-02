package nts.uk.ctx.bs.person.dom.person.info.item;

import java.util.List;

import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.bs.person.dom.person.info.setitem.SetItem;
import nts.uk.ctx.bs.person.dom.person.info.singleitem.DataTypeState;
import nts.uk.ctx.bs.person.dom.person.info.singleitem.SingleItem;

public class ItemTypeState extends AggregateRoot {

	protected ItemType itemType;

	public static ItemTypeState createSetItem(List<String> items) {
		return SetItem.createFromJavaType(items);
	};

	public static ItemTypeState createSingleItem(DataTypeState dataTypeState) {
		return SingleItem.createFromJavaType(dataTypeState);
	};
}

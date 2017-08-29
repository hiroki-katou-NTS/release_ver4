package command.person.info.item;

import lombok.Value;

@Value
public class UpdateItemCommand {
	private String perInfoItemDefId;
	private String itemName;
	private SingleItemCommand singleItem;
}

package nts.uk.ctx.bs.person.dom.person.info.item;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ItemType {

	// 1:セット項目(SetItem)
	SET_ITEM(1),

	// 2:単体項目(SingleItem)
	SINGLE_ITEM(2);

	public final int value;
}

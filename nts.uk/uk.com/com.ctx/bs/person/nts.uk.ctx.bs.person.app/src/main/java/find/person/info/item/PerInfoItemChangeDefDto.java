package find.person.info.item;

import java.math.BigDecimal;
import java.util.List;

import lombok.Value;
import nts.arc.enums.EnumConstant;

@Value
public class PerInfoItemChangeDefDto {
	private String id;
	private String perInfoCtgId;
	private String itemCode;
	private String itemName;
	private String itemDefaultName;
	private int isAbolition;
	private int isFixed;
	private int isRequired;
	private int systemRequired;
	private int requireChangable;
	private int dispOrder;
	private BigDecimal selectionItemRefType;
	private ItemTypeStateDto itemTypeState;
	private List<EnumConstant> selectionItemRefTypes;
}
